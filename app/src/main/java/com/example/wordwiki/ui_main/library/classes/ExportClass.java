package com.example.wordwiki.ui_main.library.classes;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Environment;
import android.util.Log;

import com.example.wordwiki.MainActivity;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.ui_main.library.models.ExportedDictionaryHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExportClass {

    private static final String TAG = "ExcelExporter";
    private static DatabaseHelper myDb;

    private static String currentLanguage;
    private static String currentSection;

    /**
     * function to export db dictionary to an excel
     *
     * @param checkedItemsList
     * @param context
     */
    public static void export(ArrayList<String> checkedItemsList, Context context) {
        DatabaseHelper myDb = new DatabaseHelper(context);

        //String sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String sd = "/storage/emulated/0/Download/";

        File directory = new File(sd);
        String lastLanguage = "";
        String currentLanguage = "";
        ArrayList<String> languageList = new ArrayList<String>();
        // get language and section to have continues flow for every language
        Collections.sort(checkedItemsList);

        Map<String, List<String>> dictionary = new HashMap<String, List<String>>();

        // go through all values to create a mapping between language and its sections for later exportation
        for (String currentX : checkedItemsList) {
            Pattern regex = Pattern.compile("\\{(.*?)\\}");
            Matcher regexMatcher = regex.matcher(currentX);

            int i = 0;
            while (regexMatcher.find()) {//Finds Matching Pattern in String
                String currentString = regexMatcher.group(1);
                if (i == 0) {

                    if (!currentString.equals(lastLanguage)) {
                        languageList = new ArrayList<String>();

                        currentLanguage = currentString;
                        lastLanguage = currentLanguage;
                    } else {
                        dictionary.put(currentLanguage, languageList);
                    }
                } else {
                    languageList.add(currentString);
                    dictionary.put(currentLanguage, languageList);
                }
                i++;
            }
            dictionary.put(currentLanguage, languageList);
            Log.d(TAG, "export dic: " + dictionary);
        }

        for (String languageX : dictionary.keySet()) {
            String csvFileName = languageX + ".xls";
            try {
                //file path
                File file = new File(directory, csvFileName);
                WorkbookSettings wbSettings = new WorkbookSettings();
                wbSettings.setLocale(new Locale(Locale.GERMAN.getLanguage(), Locale.GERMAN.getCountry()));
                WritableWorkbook workbook;
                workbook = Workbook.createWorkbook(file, wbSettings);

                int p = 0;
                for (String sectionX : Objects.requireNonNull(dictionary.get(languageX))) {
                    //Excel sheetA first sheetA
                    int t = 1;
                    WritableSheet sheet = workbook.createSheet(sectionX, p);

                    // column and row titles
                    sheet.addCell(new Label(0, 0, "Word"));
                    sheet.addCell(new Label(1, 0, "Translation"));

                    Cursor exportWords = myDb.exportWords(languageX, sectionX);

                    if (exportWords == null)
                        return; // can't do anything with a null cursor.
                    try {
                        while (exportWords.moveToNext()) {
                            sheet.addCell(new Label(0, t, exportWords.getString(0)));
                            sheet.addCell(new Label(1, t, exportWords.getString(1)));
                            t++;
                        }
                    } finally {
                        exportWords.close();
                    }

                    p++;
                }
                workbook.write();
                workbook.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * function to export an excel to a cloud
     */
    public static void exportCloud(String languageName, String sectionName, String description, String level, Context context) {
        // TODO insert true measures that go to the firebase

        DatabaseHelper myDb = new DatabaseHelper(context);
        // get language and section to have continues flow for every language
        ArrayList<String> words = new ArrayList();
        ArrayList<String> translations = new ArrayList();


        Cursor exportWords = myDb.exportWords(languageName, sectionName);
        Log.d(TAG, "exportCloud: size of cloud: " + exportWords.getCount() + ": " + languageName + ": " + sectionName);
        if (exportWords == null)
            return; // can't do anything with a null cursor.
        try {
            while (exportWords.moveToNext()) {
                words.add(exportWords.getString(0));
                translations.add(exportWords.getString(1));
            }
        } finally {
            exportWords.close();
        }

        SharedPreferences sharedPreferences = context.getSharedPreferences("user_profile", Context.MODE_PRIVATE);

        // TODO add native language indicator
        ExportedDictionaryHelper emp = new ExportedDictionaryHelper("English"
                , languageName
                , 0
                , level
                , 0
                , words.size()
                , sectionName
                , words
                , translations
                , sharedPreferences.getString("username", "")
                , description
        );

        ExportedDictionaryCloudAdapter model = new ExportedDictionaryCloudAdapter();
        model.add(emp);
    }
}
