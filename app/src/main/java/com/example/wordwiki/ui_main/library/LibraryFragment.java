package com.example.wordwiki.ui_main.library;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.blongho.country_data.World;
import com.example.wordwiki.R;
import com.example.wordwiki.classes.LoadingDialog;
import com.example.wordwiki.classes.SuccessDialog;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.databinding.FragmentLibraryBinding;
import com.example.wordwiki.ui_main.library.adapters.SectionAdapter;
import com.example.wordwiki.classes.FileUtils;
import com.example.wordwiki.ui_main.library.models.SectionHelper;
import com.example.wordwiki.ui_main.library.models.SubsectionHelper;
import com.example.wordwiki.ui_main.profile.models.ExcelParsing;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellValue;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryFragment extends Fragment {
    private static final int PICKFILE_REQUEST_CODE = 3;
    private static final String TAG = "Library Fragment, root.";
    private FragmentLibraryBinding binding;
    ImageButton addDictionary;
    // recycle views
    List<SectionHelper> sectionList = new ArrayList<>();

    RecyclerView mainRecyclerView;
    SectionAdapter sectionAdapter;

    RelativeLayout emptyStateFiller;

    ArrayList<ExcelParsing> uploadData;
    DatabaseHelper myDb;
    String dictionaryLevel;

    AppCompatButton isA1, isA2, isB1, isB2, isC1, isC2;

    Map<String, String> language_suggestions_map = new HashMap<String, String>() {
        {
            put("English", "en");
            put("French", "fr");
            put("Spanish", "es");
            put("Italian", "it");
            put("Dutch", "nl");
            put("Norwegian", "no");
            put("German", "ge");
            put("Portuguese", "pt");
            put("Czech", "fcz");
            put("Slovak", "sk");
            put("Hungarian", "hu");
            put("Polish", "pl");
            put("Danish", "dk");
            put("Swedish", "sw");
            put("Icelandic", "is");
            put("Finnish", "fi");
            put("Latvian", "lv");
            put("Lithuanian", "lt");
            put("Estonian", "ee");
        }
    };


    private static final String[] language_suggestions = new String[]{
            "English", "French", "Spanish", "Italian", "Dutch", "Norwegian", "German",
            "Portuguese", "Czech", "Slovak", "Hungarian", "Polish", "Danish",
            "Swedish", "Icelandic", "Finnish", "Turkish", "Latvian", "Lithuanian",
            "Estonian"
    };

    private static final String[] section_suggestions = new String[]{
            "Food", "Sports", "Music", "Movies", "Time", "Numbers", "Holidays",
            "Weather", "Directions", "Buildings", "Family", "Colours", "Shapes",
            "Computer", "Animals", "Hobbies", "Jobs", "Patterns", "Idioms", "Environment",
            "Nouns", "Verbs", "Personality", "Clothes", "Technology", "Education", "Health",
            "Leisure", "Travel", "Services", "Feelings", "Work", "Notions", "People",
            "Nature", "Sport", "Science", "Appearance", "Communication", "Culture", "Introduction"
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentLibraryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // layout for a filler
        emptyStateFiller = binding.getRoot().findViewById(R.id.explore_fragment_empty_filler_layout);



        Toolbar tb = root.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(tb);
        myDb = new DatabaseHelper(getContext());
        //initialize recycle viewers
        initData();
        mainRecyclerView = root.findViewById(R.id.fragment_library_top_recycle_view);
        sectionAdapter = new SectionAdapter(sectionList, getContext());
        mainRecyclerView.setAdapter(sectionAdapter);


        //TODO move it to async task later
        if (sectionList.size() == 0) {
            mainRecyclerView.setVisibility(View.GONE);
            emptyStateFiller.setVisibility(View.VISIBLE);

        } else {
            mainRecyclerView.setVisibility(View.VISIBLE);
            emptyStateFiller.setVisibility(View.GONE);
        }

        //mainRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        addDictionary = root.findViewById(R.id.library_add);

        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            ContextCompat.checkSelfPermission(getContext(), Manifest.permission.MANAGE_EXTERNAL_STORAGE);
        }
        ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);


        addDictionary.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String[] mimeTypes = {"application/vnd.google-apps.spreadsheet",
                        "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                        "application/x-excel"};


                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                    if (mimeTypes.length > 0) {
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                    }
                } else {
                    String mimeTypesStr = "";

                    for (String mimeType : mimeTypes) {
                        mimeTypesStr += mimeType + "|";
                    }
                    intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                }
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                startActivityForResult(intent, PICKFILE_REQUEST_CODE);


            }
        });


        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: imported22222 file");
        if (requestCode == PICKFILE_REQUEST_CODE) {
            Log.i(TAG, "onActivityResult: imported file");
            //proImportCSV(new File(data.getData().getPath());
            if (resultCode == Activity.RESULT_OK) {
                Uri uri = data.getData();
                uploadData = new ArrayList<>();
                ClipData clipData = data.getClipData();

                if (clipData == null) {
                    read(FileUtils.getPath(getContext(), uri));
                    Log.i(TAG, "onActivityResult: path to files: " + FileUtils.getPath(getContext(), uri));
                } else {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        ClipData.Item path = clipData.getItemAt(i);
                        Log.i("Path:", path.toString());

                        String paths = FileUtils.getPath(getContext(), path.getUri());


                        //readExcelData(paths, "testLanguage", "testSection");
                        read(paths);
                    }
                }

            } else {
                // The user cancelled the request.
            }
        }
    }

    private void read(String path) {
        try {
            showAlert(path);
            //readExcelData(path, "testLanguage", "testSection");
        } catch (Exception e) {
            Log.d(TAG, "read: " + e.getMessage());
        }
    }


    /**
     * logic map:
     *      1. define objects
     *      2. get language following Map image and items
     *          2.1. items are added until new language is approached, then language is changed and
     *          in next iteration we create new listItem for languages
     *      3. final if is added to confirm that the last language is added, because of the way of defining.
     */
    private void initData() {
        // TODO create sep. table for information about dictionaries for information storage - description, level...
        /**
         * 1. CREATE DB function that finds all
         */

        Cursor dictionaryInformation = myDb.findDictionaryInformation();
        String currentLanguage = "";
        boolean rateUp = false;
        boolean rateDown = false;

        if (dictionaryInformation == null)
            return; // can't do anything with a null cursor.
        try {
            List<SubsectionHelper> sectionItems = new ArrayList<>();
            while (dictionaryInformation.moveToNext()) {
                Log.i(TAG, "currentLanguage: " + dictionaryInformation.getString(2));
                String countryISO = myDb.getFlagISO(currentLanguage);
                int flagInt = World.getFlagOf(countryISO);

                if (currentLanguage.length() > 0 && !currentLanguage.equals(dictionaryInformation.getString(2))) {
                    Log.i(TAG, "currentLanguage added is: " + dictionaryInformation.getString(2));
                    sectionList.add(new SectionHelper(currentLanguage, flagInt, sectionItems));
                    sectionItems = new ArrayList<>();
                    currentLanguage = dictionaryInformation.getString(2);
                } else if (currentLanguage.length() == 0) {
                    currentLanguage = dictionaryInformation.getString(2);
                }



                Cursor ratingCursor = myDb.getRating(dictionaryInformation.getString(2)
                        , dictionaryInformation.getString(3));
                Log.i(TAG, "initData: " + ratingCursor.getString(0).equals("1") + ", down:" + ratingCursor.getString(1).equals("1"));
                sectionItems.add(new SubsectionHelper(dictionaryInformation.getString(2)
                        , dictionaryInformation.getString(3)
                        , dictionaryInformation.getString(4)
                        , dictionaryInformation.getString(1)
                        , dictionaryInformation.getString(5)
                        , ratingCursor.getString(0).equals("1")
                        , ratingCursor.getString(1).equals("1")
                        , false));
                //ratingCursor.close();


                if (dictionaryInformation.isLast()) {
                    // to keep the last language entered, because there is no switch in the comparison

                    countryISO = myDb.getFlagISO(currentLanguage);
                    flagInt = World.getFlagOf(countryISO);

                    Log.i(TAG, "currentLanguage3: " + dictionaryInformation.getString(2));
                    sectionList.add(new SectionHelper(currentLanguage, flagInt, sectionItems));
                }

            }

        } finally {

            dictionaryInformation.close();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_library_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                sectionAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * reads the excel file columns then rows. Stores data as ExcelUploadData object
     *
     * @return
     */
    private void readExcelData(String filePath, String language, String section, String description, String level) {
        Log.d(TAG, "readExcelData: Reading Excel File.");

        //declares input file
        File inputFile = new File(filePath);

        try {
            InputStream inputStream = new FileInputStream(inputFile);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            int rowsCount = sheet.getPhysicalNumberOfRows();
            XSSFFormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            StringBuilder sb = new StringBuilder();

            // outer loop, loops through rows
            for (int r = 1; r < rowsCount; r++) {
                Row row = sheet.getRow(r);
                int cellsCount = row.getPhysicalNumberOfCells();
                //inner loop, loops through columns
                for (int c = 0; c < cellsCount; c++) {
                    //handles if there are to many columns on the excel sheet.
                    if (c > 2) {
                        Log.e(TAG, "readExcelData: ERROR. Excel File Format is incorrect! ");
                        //toastMessage("ERROR: Excel File Format is incorrect!");
                        break;
                    } else {
                        String value = getCellAsString(row, c, formulaEvaluator);
                        String cellInfo = "r:" + r + "; c:" + c + "; v:" + value;
                        Log.d(TAG, "readExcelData: Data from row: " + cellInfo);
                        sb.append(value + ", ");
                    }
                }
                sb.append(":");
            }
            Log.d(TAG, "readExcelData: STRINGBUILDER: " + sb.toString());

            Log.d(TAG, "readExcelData: parsingSize: " + sb.length());
            parseStringBuilder(sb, language, section, description, level);
            sb = new StringBuilder();

        } catch (FileNotFoundException e) {
            Log.e(TAG, "readExcelData: FileNotFoundException. " + e.getMessage());
        } catch (IOException e) {
            Log.e(TAG, "readExcelData: Error reading inputstream. " + e.getMessage());
        }
    }

    /**
     * Method for parsing imported data and storing in ArrayList<XYValue>
     */
    public void parseStringBuilder(StringBuilder mStringBuilder, String language, String section, String description, String level) {
        Log.d(TAG, "parseStringBuilder: Started parsing.");

        // splits the sb into rows.
        String[] rows = mStringBuilder.toString().split(":");
        Log.d(TAG, "parseStringBuilder: rowLength: " + rows.length);
        //Add to the ArrayList<XYValue> row by row
        for (int i = 0; i < rows.length; i++) {
            //Split the columns of the rows
            String[] columns = rows[i].split(",");
            //use try catch to make sure there are no "" that try to parse into doubles.
            try {
                String x = "";
                try {
                    x = columns[0];
                } catch (Exception e) {
                    Log.e(TAG, "parseStringBuilder: NumberFormatException1111: " + e.getMessage());
                }

                String y = "";
                try {
                    y = columns[1];
                } catch (Exception e) {
                    Log.e(TAG, "parseStringBuilder: NumberFormatException2222: " + e.getMessage());
                }


                String cellInfo = "(x,y): (" + x + "," + y + ")";
                Log.d(TAG, "ParseStringBuilder: Data from row: " + cellInfo);

                //add the the uploadData ArrayList
                uploadData.add(new ExcelParsing(x, y));

            } catch (Exception e) {

                Log.e(TAG, "parseStringBuilder: NumberFormatException33333: " + e.getMessage());

            }
        }
        printDataToLog(language, section, description, level);
    }

    private void printDataToLog(String language, String section, String description, String level) {

        Log.d(TAG, "printDataToLog: Printing data to log...");
        Log.d(TAG, "printDataToLog: (x,y): (" + language + "," + section + ")");
        for (int i = 0; i < uploadData.size(); i++) {
            // initial entry creates information table row
            if (i == 0) {
                // TODO add username to the user in the cloud.
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("user_profile", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                myDb.enterDictionaryInformation(username, language, section, description, level);
            }


            String x = uploadData.get(i).getX();
            String y = uploadData.get(i).getY();
            Log.d(TAG, "printDataToLog: (x,y): (" + i + ")");
            myDb.parsingExcel(x, y, language, section);
        }
        uploadData.clear();
    }

    /**
     * Returns the cell as a string from the excel file
     **/
    private String getCellAsString(Row row, int c, XSSFFormulaEvaluator formulaEvaluator) {
        String value = "";
        try {
            Cell cell = row.getCell(c);

            CellValue cellValue = formulaEvaluator.evaluate(cell);
            switch (cellValue.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    value = "" + cellValue.getBooleanValue();
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    double numericValue = cellValue.getNumberValue();
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        double date = cellValue.getNumberValue();
                        SimpleDateFormat formatter =
                                new SimpleDateFormat("MM/dd/yy");
                        value = formatter.format(HSSFDateUtil.getJavaDate(date));
                    } else {
                        value = "" + numericValue;
                    }
                    break;
                case Cell.CELL_TYPE_STRING:
                    value = "" + cellValue.getStringValue();
                    break;
                default:
            }
        } catch (NullPointerException e) {

            Log.e(TAG, "getCellAsString: NullPointerException: " + e.getMessage());
        }
        return value;
    }


    public void showAlert(String lastDirectory) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.fragment_library_custom_dialog_user_input);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(getDrawable(getContext(), R.drawable.fragment_library_custom_dialog_bg));
        }


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);

        dialog.getWindow().setLayout(width, height);
        dialog.setCancelable(false); //Optional
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //Setting the animations to dialog
        dialog.show();

        TextView fileDirectory = dialog.findViewById(R.id.user_input_path);
        String fileName = lastDirectory.substring(lastDirectory.lastIndexOf('/') + 1, lastDirectory.length() - 5);
        fileDirectory.setText(fileName);

        AutoCompleteTextView input_language = dialog.findViewById(R.id.enter_language_txt);
        ArrayAdapter<String> complete_language_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, language_suggestions);
        input_language.setAdapter(complete_language_adapter);

        AutoCompleteTextView input_section = dialog.findViewById(R.id.enter_section_txt);
        ArrayAdapter<String> complete_section_adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, section_suggestions);
        input_section.setAdapter(complete_section_adapter);

        // Description field
        TextView input_description = dialog.findViewById(R.id.enter_description_txt);

        // Level buttons
        // TODO implement click callback to get back level pressed

        isA1 = dialog.findViewById(R.id.fragment_library_import_dictionary_level_bar_a1);
        isA2 = dialog.findViewById(R.id.fragment_library_import_dictionary_level_bar_a2);
        isB1 = dialog.findViewById(R.id.fragment_library_import_dictionary_level_bar_b1);
        isB2 = dialog.findViewById(R.id.fragment_library_import_dictionary_level_bar_b2);
        isC1 = dialog.findViewById(R.id.fragment_library_import_dictionary_level_bar_c1);
        isC2 = dialog.findViewById(R.id.fragment_library_import_dictionary_level_bar_c2);

        // functionality of the dictionary level bar
        isA1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // function to nullify all values, then select the chosen one
                unselectDictionaryLevel();
                selectDictionaryLevel(1);

                // change text of level which later is used to store data of dictionary and etc
                dictionaryLevel = "A1";
            }
        });

        isA2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // function to nullify all values, then select the chosen one
                unselectDictionaryLevel();
                selectDictionaryLevel(2);

                // change text of level which later is used to store data of dictionary and etc
                dictionaryLevel = "A2";
            }
        });

        isB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // function to nullify all values, then select the chosen one
                unselectDictionaryLevel();
                selectDictionaryLevel(3);

                // change text of level which later is used to store data of dictionary and etc
                dictionaryLevel = "B1";
            }
        });

        isB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // function to nullify all values, then select the chosen one
                unselectDictionaryLevel();
                selectDictionaryLevel(4);

                // change text of level which later is used to store data of dictionary and etc
                dictionaryLevel = "B2";
            }
        });

        isC1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // function to nullify all values, then select the chosen one
                unselectDictionaryLevel();
                selectDictionaryLevel(5);

                // change text of level which later is used to store data of dictionary and etc
                dictionaryLevel = "C1";
            }
        });

        isC2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // function to nullify all values, then select the chosen one
                unselectDictionaryLevel();
                selectDictionaryLevel(6);

                // change text of level which later is used to store data of dictionary and etc
                dictionaryLevel = "C2";
            }
        });


        Button btn_okay = (Button) dialog.findViewById(R.id.btn_okay);
        Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);

        // Continuing with dialog flow
        final LoadingDialog loadingDialog = new LoadingDialog(getActivity());
        final SuccessDialog successDialog = new SuccessDialog(getActivity());

        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // TODO make other fields optional???
                if (!TextUtils.isEmpty(input_language.getText().toString()) && !TextUtils.isEmpty(input_section.getText().toString())) {
                    dialog.dismiss();

                    // second preference removes language_2, section_2 true values, so newly entered datasets are not instantly appearing
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("recycle_buttons", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("language_2", false);
                    editor.putBoolean("section_2", false);
                    editor.commit();

                    // instantly create loading dialog
                    loadingDialog.startLoadingDialog();

                    // TODO add levels
                    Log.i(TAG, "onClick: to readExcelData, par[0] - " + input_language.getText().toString());
                    Log.i(TAG, "onClick: to readExcelData, par[0] - " + input_section.getText().toString());
                    Log.i(TAG, "onClick: to readExcelData, par[0] - " + input_description.getText().toString());
                    Log.i(TAG, "onClick: to readExcelData, par[0] - " + dictionaryLevel);
                    readExcelData(lastDirectory, input_language.getText().toString(), input_section.getText().toString(), input_description.getText().toString(), dictionaryLevel);


                    // toastMessage("Importing Words Now...");

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadingDialog.dismissDialog();

                            successDialog.startConfirmationDialog();

                            Button main_activity_btn_confirmation = successDialog.dialog.findViewById(R.id.btn_okay_confirmation);
                            Button continue_btn_confirmation = successDialog.dialog.findViewById(R.id.btn_cont_confirmation);

                            continue_btn_confirmation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    successDialog.dismissDialog();
                                }
                            });

                            main_activity_btn_confirmation.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    successDialog.dismissDialog();

                                    //Intent intent = new Intent(view.getContext(), EntranceActivity.class);
                                    //startActivity(intent);
                                }
                            });
                        }
                    }, 1500);
                } else if (TextUtils.isEmpty(input_language.getText().toString()) && !TextUtils.isEmpty(input_section.getText().toString())) {
                    toastMessage("You Forgot To Enter Language Name");
                } else if (TextUtils.isEmpty(input_section.getText().toString()) && !TextUtils.isEmpty(input_language.getText().toString())) {
                    toastMessage("You Forgot To Enter Section Name");
                } else {
                    toastMessage("You Forgot To Enter Language and Section Name");
                }


            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    private void selectDictionaryLevel(int i) {
        Log.i(TAG, "selectDictionaryLevel: selected dictionary level: " + i);
        if (i == 1) {
            isA1.setBackgroundColor(Color.GRAY);
        } else if (i == 2) {
            isA2.setBackgroundColor(Color.GRAY);
        } else if (i == 3) {
            isB1.setBackgroundColor(Color.GRAY);
        } else if (i == 4) {
            isB2.setBackgroundColor(Color.GRAY);
        } else if (i == 5) {
            isC1.setBackgroundColor(Color.GRAY);
        } else if (i == 6) {
            isC2.setBackgroundColor(Color.GRAY);
        }
    }

    private void unselectDictionaryLevel() {
        isA1.setBackgroundColor(Color.TRANSPARENT);
        isA2.setBackgroundColor(Color.TRANSPARENT);
        isB1.setBackgroundColor(Color.TRANSPARENT);
        isB2.setBackgroundColor(Color.TRANSPARENT);
        isC1.setBackgroundColor(Color.TRANSPARENT);
        isC2.setBackgroundColor(Color.TRANSPARENT);
    }

    private void toastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}