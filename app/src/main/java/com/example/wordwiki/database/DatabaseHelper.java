package com.example.wordwiki.database;

import static android.content.Context.MODE_PRIVATE;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.viewpager.widget.PagerAdapter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {
    Cursor wordInfo;

    private static final String TAG = "DB";

    public static final String DATABASE_NAME = "flashy.db";
    public static final String TABLE_NAME = "Main_Table";

    public static final String COL_1 = "id";
    public static final String COL_2 = "language";
    public static final String COL_3 = "section";
    public static final String COL_4 = "noun";
    public static final String COL_5 = "word";
    public static final String COL_6 = "translation";
    public static final String COL_7 = "significance";
    public static final String COL_8 = "classicalDate";
    public static final String COL_9 = "classicalLvl";
    public static final String COL_10 = "swappedDate";
    public static final String COL_11 = "swappedLvl";
    public static final String COL_12 = "articleDate";
    public static final String COL_13 = "articleLvl";
    public static final String COL_14 = "form";
    public static final String COL_15 = "prepositionLvl";
    public static final String COL_16 = "prepositionDate";
    public static final String COL_17 = "idiomLvl";
    public static final String COL_18 = "idiomDate";


    // progress table
    public static final String PROGRESS_TABLE_NAME = "Progress_Table";

    public static final String PROGRESS_COL1 = "id";
    public static final String PROGRESS_COL2 = "date";
    public static final String PROGRESS_COL3 = "newWordsToday";
    public static final String PROGRESS_COL4 = "wordsToday";
    public static final String PROGRESS_COL5 = "wordsTotal";
    public static final String PROGRESS_COL6 = "wordsRevised";

    // language and its code table, to recognize flags and etc
    public static final String LANGUAGE_CODE_TABLE_NAME = "ISO_Table";

    public static final String LANGUAGE_CODE_COL1 = "id";
    public static final String LANGUAGE_CODE_COL2 = "iso";
    public static final String LANGUAGE_CODE_COL3 = "language";
    public static final String LANGUAGE_CODE_COL4 = "article";

    //Setting a format of time in app
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date date = new Date();

    // dictionary inner information table
    public static final String INFORMATION_TABLE_NAME = "information_table";

    public static final String INFORMATION_COL1 = "id";
    public static final String INFORMATION_COL2 = "username";
    public static final String INFORMATION_COL3 = "language";
    public static final String INFORMATION_COL4 = "section";
    public static final String INFORMATION_COL5 = "description";
    public static final String INFORMATION_COL6 = "level";
    public static final String INFORMATION_COL7 = "rateUp";
    public static final String INFORMATION_COL8 = "rateDown";
    public static final String INFORMATION_COL9 = "created";
    public static final String INFORMATION_COL10 = "cloudID";

    // TODO connect article/iso table with the information table


    // Map of ISO ---> language setUp
    final Map<String, String> language_suggestions_map = new HashMap<String, String>() {
        {
            put("English", "gb");
            put("French", "fr");
            put("Spanish", "es");
            put("Italian", "it");
            put("Dutch", "nl");
            put("Norwegian", "no");
            put("German", "ge");
            put("Portuguese", "pt");
            put("Czech", "cz");
            put("Slovak", "sk");
            put("Hungarian", "hu");
            put("Polish", "pl");
            put("Danish", "dk");
            put("Swedish", "se");
            put("Icelandic", "is");
            put("Finnish", "fi");
            put("Latvian", "lv");
            put("Lithuanian", "lt");
            put("Estonian", "ee");
        }
    };

    final Map<String, ArrayList<String>> language_article_map = new HashMap<String, ArrayList<String>>() {
        {
            ArrayList<String> langauge_article = new ArrayList<String>();

            langauge_article.add("the");
            langauge_article.add("an");
            langauge_article.add("a");

            put("English", langauge_article);

            langauge_article = new ArrayList<String>();

            langauge_article.add("le");
            langauge_article.add("la");
            langauge_article.add("l'");
            langauge_article.add("les");
            langauge_article.add("d");
            langauge_article.add("de");
            langauge_article.add("du");
            langauge_article.add("de");
            langauge_article.add("la");
            langauge_article.add("des");
            langauge_article.add("de l'");
            langauge_article.add("un");
            langauge_article.add("une");
            langauge_article.add("des");

            put("French", langauge_article);

            langauge_article = new ArrayList<String>();
            langauge_article.add("el");
            langauge_article.add("la");
            langauge_article.add("lo");
            langauge_article.add("los");
            langauge_article.add("las");
            langauge_article.add("un");
            langauge_article.add("una");
            langauge_article.add("unos");
            langauge_article.add("unas");

            put("Spanish", langauge_article);

            langauge_article = new ArrayList<String>();
            langauge_article.add("il");
            langauge_article.add("lo");
            langauge_article.add("la");
            langauge_article.add("l'");
            langauge_article.add("i");
            langauge_article.add("gli");
            langauge_article.add("le");
            langauge_article.add("del");
            langauge_article.add("dello");
            langauge_article.add("della");
            langauge_article.add("deli'");
            langauge_article.add("dei");
            langauge_article.add("degli");
            langauge_article.add("degi'");
            langauge_article.add("delle");
            langauge_article.add("un'");
            langauge_article.add("uno");
            langauge_article.add("una");
            langauge_article.add("un");

            put("Italian", langauge_article);

            langauge_article = new ArrayList<String>();
            langauge_article.add("de");
            langauge_article.add("het");
            langauge_article.add("des");
            langauge_article.add("der");
            langauge_article.add("den");
            langauge_article.add("een");
            langauge_article.add("'n");

            put("Dutch", langauge_article);

            langauge_article = new ArrayList<String>();
            langauge_article.add("en");
            langauge_article.add("et");
            langauge_article.add("ei");

            put("Norwegian", langauge_article);

            langauge_article = new ArrayList<String>();
            langauge_article.add("der");
            langauge_article.add("die");
            langauge_article.add("das");
            langauge_article.add("des");
            langauge_article.add("dem");
            langauge_article.add("den");
            langauge_article.add("ein");
            langauge_article.add("eine");
            langauge_article.add("einer");
            langauge_article.add("eines");
            langauge_article.add("einem");
            langauge_article.add("einen");

            put("German", langauge_article);

            langauge_article = new ArrayList<String>();
            langauge_article.add("a");
            langauge_article.add("o");
            langauge_article.add("os");
            langauge_article.add("as");
            langauge_article.add("um");
            langauge_article.add("uma");
            langauge_article.add("uns");
            langauge_article.add("umas");

            put("Portuguese", langauge_article);

            langauge_article = new ArrayList<String>();
            langauge_article.add("a");
            langauge_article.add("az");
            langauge_article.add("egy");

            put("Hungarian", langauge_article);

            langauge_article = new ArrayList<String>();
            langauge_article.add("en");
            langauge_article.add("at");

            put("Danish", langauge_article);

            langauge_article = new ArrayList<String>();
            langauge_article.add("en");
            langauge_article.add("ett");

            put("Swedish", langauge_article);

            langauge_article = new ArrayList<String>();
            put("Polish", langauge_article);
            put("Icelandic", langauge_article);
            put("Finnish", langauge_article);
            put("Latvian", langauge_article);
            put("Lithuanian", langauge_article);
            put("Estonian", langauge_article);
            put("Slovak", langauge_article);
            put("Czech", langauge_article);
        }
    };

    //Creating a constructor for a database
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1); //Defining connection - creating the database
    }

    @Override  //Executes SQL - creates table "words"
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME +
                " (" + COL_1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_2 + " TEXT, " +
                COL_3 + " TEXT, " +
                COL_4 + " String, " +
                COL_5 + " TEXT, " +
                COL_6 + " TEXT, " +
                COL_7 + " String, " +
                COL_8 + " String, " +
                COL_9 + " INTEGER, " +
                COL_10 + " String, " +
                COL_11 + " INTEGER, " +
                COL_12 + " String, " +
                COL_13 + " INTEGER, " +
                COL_14 + " String, " +
                COL_15 + " INTEGER, " +
                COL_16 + " String, " +
                COL_17 + " INTEGER, " +
                COL_18 + " String " +
                ")");

        // table for progress following
        db.execSQL("CREATE TABLE " + PROGRESS_TABLE_NAME +
                " (" + PROGRESS_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PROGRESS_COL2 + " String, " +
                PROGRESS_COL3 + " INTEGER, " +
                PROGRESS_COL4 + " INTEGER, " +
                PROGRESS_COL5 + " INTEGER, " +
                PROGRESS_COL6 + " INTEGER " +
                ")");

        // table for language+iso following
        db.execSQL("CREATE TABLE " + LANGUAGE_CODE_TABLE_NAME +
                " (" + LANGUAGE_CODE_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LANGUAGE_CODE_COL2 + " INTEGER, " +
                LANGUAGE_CODE_COL3 + " String, " +
                LANGUAGE_CODE_COL4 + " String " +
                ")");

        // enter country ISO and its language
        setUpISOTable();

        db.execSQL("CREATE TABLE " + INFORMATION_TABLE_NAME +
                " (" + INFORMATION_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                INFORMATION_COL2 + " String, " +
                INFORMATION_COL3 + " String, " +
                INFORMATION_COL4 + " String, " +
                INFORMATION_COL5 + " String, " +
                INFORMATION_COL6 + " String, " +
                INFORMATION_COL7 + " Boolean, " +
                INFORMATION_COL8 + " Boolean, " +
                INFORMATION_COL9 + " Boolean, " +
                INFORMATION_COL10 + " String " +
                ")");

    }

    private void setUpISOTable() {
        SQLiteDatabase db = this.getWritableDatabase();

        for (Map.Entry<String, String> entry : language_suggestions_map.entrySet()) {
            ContentValues cv = new ContentValues();
            cv.put(LANGUAGE_CODE_COL2, entry.getValue());
            cv.put(LANGUAGE_CODE_COL3, entry.getKey());

            String articles = language_suggestions_map.get(language_article_map.get(entry.getKey()));
            cv.put(LANGUAGE_CODE_COL4, articles);

            db.insert(LANGUAGE_CODE_TABLE_NAME, null, cv);
        }
    }

    @Override //Commands triggered with an update
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //Checks if table exists
        db.execSQL("DROP TABLE IF EXISTS " + PROGRESS_TABLE_NAME); //Checks if table exists
        db.execSQL("DROP TABLE IF EXISTS " + LANGUAGE_CODE_TABLE_NAME); //Checks if table exists
        onCreate(db); // Activates SQL lines above
    }


    //Deletes on "ID" in "Main_activity".
    public Integer deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?", new String[]{id});
    }

    // Draws a new word with some constraints, also bolds articles
    public Cursor getOneWord(String mode_name, String language_name, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Recalculates a Significance of word after using app. Need optimization to speed up this sql code.

        if (mode_name.equals("Classical")) {
            return updateSignificance(mode_name, language_name, context);
        } else if (mode_name.equals("Swapped")) {
            return updateSignificance(mode_name, language_name, context);
        } else if (mode_name.equals("Article")) {
            return updateSignificance(mode_name, language_name, context);
        } else if (mode_name.equals("Preposition")) {
            return updateSignificance(mode_name, "English", context);
        } else {
            return updateSignificance(mode_name, "English", context);
        }
    }


    //Changes the "Level" column integer -> +1. Button "Got it right"
    public Boolean UpdateLevelRight(String wordInput, String Mode, String SectionNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String Level_Column;
        if (Mode.equals("Classical")) {
            Level_Column = COL_9;
        } else if (Mode.equals("Swapped")) {
            Level_Column = COL_11;
        } else if (Mode.equals("Article")) {
            Level_Column = COL_13;
        } else if (Mode.equals("Preposition")) {
            Level_Column = COL_15;
        } else {
            Level_Column = COL_17;
        }

        //Finds AND updates word's level
        Cursor wordToUpdate = db.rawQuery(
                "SELECT " + Level_Column + ", " +
                        COL_1 + " " +
                        "FROM " + TABLE_NAME + " " +
                        "WHERE " + COL_5 + " = '" + wordInput + "'" +
                        "AND " + COL_2 + " IN " + SectionNames + ";"
                , null);

        wordToUpdate.moveToFirst();

        //constraint to have a maximum limit for "Level" column
        Log.d(TAG, "UpdateLevelRight: integer: " + wordToUpdate.getString(0));
        if (wordToUpdate.getString(0) == null) {
            contentValues.put(Level_Column, 1);
        } else if (Integer.parseInt(wordToUpdate.getString(0)) < 13) {
            contentValues.put(Level_Column, Integer.parseInt(wordToUpdate.getString(0)) + Integer.parseInt("1"));
        }

        //Additional row for more accurate date entry
        Date date = new Date();

        //Updates "Level" AND also "Date" columns
        Log.d(TAG, "wordToUpdate AAAAA");
        if (Mode.equals("Classical")) {
            contentValues.put(COL_8, dateFormat.format(date));
        } else if (Mode.equals("Swapped")) {
            contentValues.put(COL_10, dateFormat.format(date));
        } else if (Mode.equals("Article")) {
            contentValues.put(COL_12, dateFormat.format(date));
        } else if (Mode.equals("Preposition")) {
            contentValues.put(COL_16, dateFormat.format(date));
        } else if (Mode.equals("Idiom")) {
            contentValues.put(COL_18, dateFormat.format(date));
        }

        db.update(TABLE_NAME, contentValues, " ID = ?", new String[]{wordToUpdate.getString(1)});
        return true;
    }

    //Decreases "Level"
    public Boolean UpdateLevelWrong(String wordInput, String Mode, String SectionNames) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String Level_Column;
        if (Mode.equals("Classical")) {
            Level_Column = COL_9;
        } else if (Mode.equals("Swapped")) {
            Level_Column = COL_11;
        } else if (Mode.equals("Article")) {
            Level_Column = COL_13;
        } else if (Mode.equals("Preposition")) {
            Level_Column = COL_15;
        } else {
            Level_Column = COL_17;
        }

        //Finds AND updates word's level
        Cursor wordToUpdate = db.rawQuery(
                "SELECT " + Level_Column + ", " +
                        COL_1 + " " +
                        "FROM " + TABLE_NAME + " " +
                        "WHERE " + COL_5 + " = '" + wordInput + "'" +
                        "AND " + COL_2 + " IN " + SectionNames + ";"
                , null);

        wordToUpdate.moveToFirst();

        //Additional row for more accurate date entry
        Date date = new Date();

        String levelToAmend;
        if (Mode.equals("Classical")) {
            contentValues.put(COL_8, dateFormat.format(date));
            levelToAmend = COL_9;
        } else {
            levelToAmend = COL_13;
            contentValues.put(COL_12, dateFormat.format(date));
        }

        Log.d(TAG, "UpdateLevelWrong: test level: " + wordToUpdate.getInt(1));
        if (wordToUpdate.getInt(1) > 1 && wordToUpdate.getInt(1) < 4) {
            contentValues.put(levelToAmend, 3);
        } else if (4 < wordToUpdate.getInt(1) && wordToUpdate.getInt(1) < 9) {
            contentValues.put(levelToAmend, wordToUpdate.getInt(1) - 2);
        } else {
            contentValues.put(levelToAmend, wordToUpdate.getInt(1) - 3);
        }

        //Updates "Level" AND also "Date" columns
        db.update(TABLE_NAME, contentValues, " ID = ?", new String[]{wordToUpdate.getString(1)});
        return true;
    }

    public Integer countTodayWords() { // calculates how many words have been approached today, implement language setting through preferances
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor word = db.rawQuery(
                "SELECT COUNT( " + COL_9 + " ) " +
                        "FROM " + TABLE_NAME + " " +
                        "WHERE " + COL_8 + " > date('now') OR " + COL_12 + " > date('now') " + "; "
                , null);

        word.moveToFirst();
        return word.getInt(0);
    }

    public Integer countLastDayWords() { // calculates how many words have been approached today, implement language setting through preferances
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor word = db.rawQuery(
                "SELECT COUNT( " + COL_9 + " ) " +
                        "FROM " + TABLE_NAME + " " +
                        "WHERE " + COL_8 + " > date('now', '-1 day') OR " + COL_12 + " > date('now','-1 day') " + "; "
                , null);

        word.moveToFirst();
        return word.getInt(0);
    }

    public Integer countTotalWords() { // calculates how many words have been approached overall, implement language setting through preferances
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor word = db.rawQuery(
                "SELECT COUNT( " + COL_9 + " )" +
                        "FROM " + TABLE_NAME + " " +
                        "WHERE " + COL_9 + " > 1 " + " OR " + COL_13 + " > 1 " + " ; "
                , null);

        word.moveToFirst();
        return word.getInt(0);
    }

    public Boolean ChecksIfIsNewDay() { // calculates how many words have been approached overall, implement language setting through preferances
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor TodayCount = db.rawQuery(
                "SELECT COUNT( " + COL_8 + " ) " +
                        "FROM " + TABLE_NAME + " " +
                        "WHERE " + COL_8 + " > date('now')" + " OR " + COL_12 + " > date('now'); "
                , null);

        TodayCount.moveToFirst();

        if (TodayCount.getInt(0) == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean resetLevels() { //Resets word's level in a dictionary, add preferance????
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME + " " +
                "SET " + COL_9 + " = 1, " +
                COL_13 + " = 1, " +
                COL_8 + " = " + " strftime(\'%Y-%m-%d %H:%M:%S\' , date(\'now\'), \'-1 second\'), " +
                COL_12 + " = " + " strftime(\'%Y-%m-%d %H:%M:%S\', date(\'now\'), \'-1 second\')" + ";"
        );
        return true;
    }

    public Cursor updateSignificance(String mode, String language_name, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        String mode_name;
        String date_to_update;


        if (mode.equals("Classical")) {
            date_to_update = "classicalDate";
            mode_name = "classicalLvl";
        } else if (mode.equals("Swapped")) {
            date_to_update = "swappedDate";
            mode_name = "swappedLvl";
        } else if (mode.equals("Article")) {
            date_to_update = "articleDate";
            mode_name = "articleLvl";
        } else if (mode.equals("Preposition")) {
            date_to_update = "prepositionDate";
            mode_name = "prepositionLvl";
        } else {
            date_to_update = "idiomDate";
            mode_name = "idiomLvl";
        }
        Log.d(TAG, "updateSignificance: preferenceOneWord: " + mode);
        Log.d(TAG, "updateSignificance: preferenceOneWord: " + date_to_update);
        Log.d(TAG, "updateSignificance: preferenceOneWord: " + mode_name);

        //Recalculates a Significance of word after using app. Need optimization to speed up this sql code.
        db.execSQL("UPDATE Main_Table \n" +
                "SET significance = \n" +
                "CASE \n" +
                "when " + mode_name + " = 1 THEN 2.1\n" +
                "\n" +
                "when " + mode_name + " = 2 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 10 \t\t\t THEN 1\n" +
                "when " + mode_name + " = 2 and 15 >= (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 10 \t\t THEN 1 + 2.2\n" +
                "when " + mode_name + " = 2 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 15       \t\t THEN 1 +3.3\n" +
                "\n" +
                "when " + mode_name + " = 3 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 25  \t\t\t THEN 1\n" +
                "when " + mode_name + " = 3 AND 45 > (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 25  \t\t THEN 1+ 2.1\n" +
                "when " + mode_name + " = 3 AND (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) >= 45      \t\t THEN 1+ 3.2\n" +
                "\n" +
                "when " + mode_name + " = 4 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 100 \t\t\t THEN 1\n" +
                "when " + mode_name + " = 4 AND 130 > (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 100 \t\t THEN 1+ 2\n" +
                "when " + mode_name + " = 4 AND 130 <= (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " ))  \t\t THEN 1+ 3.1\n" +
                "\n" +
                "when " + mode_name + " = 5 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 540 \t\t\t THEN 1\n" +
                "when " + mode_name + " = 5 AND 660 > (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 540 \t\t THEN 1+ 1.1\n" +
                "when " + mode_name + " = 5 AND 660 <= (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " ))  \t\t THEN 1+ 2.3\n" +
                "\n" +
                "when " + mode_name + " = 6 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 3400 \t\t\t THEN 1\n" +
                "when " + mode_name + " = 6 AND 3800 > (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 3400 \t THEN 1+ 1.2\n" +
                "when " + mode_name + " = 6 AND 3800 <= (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " ))  \t \t THEN 1+ 2.4\n" +
                "\n" +
                "when " + mode_name + " = 7 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 18500 \t\t\t THEN 1\n" +
                "when " + mode_name + " = 7 AND 19500 > (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 18500 \t THEN 1+ 1.3\n" +
                "when " + mode_name + " = 7 AND 19500 <= (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) \t\t THEN 1+ 2.5\n" +
                "\n" +
                "when " + mode_name + " = 8 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 85200 \t\t\t THEN 1\n" +
                "when " + mode_name + " = 8 AND 87600 > (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 85200 \t THEN 1+1.4\n" +
                "when " + mode_name + " = 8 AND 87600 <= (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) \t\t THEN 1+2.6\n" +
                "\n" +
                "when " + mode_name + " = 9 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 431000 \t\t\t THEN 1\n" +
                "when " + mode_name + " = 9 AND 433000 > (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 431000 \t THEN 1+1.5\n" +
                "when " + mode_name + " = 9 AND 433000 <= (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) \t\t THEN 1+2.7\n" +
                "\n" +
                "when " + mode_name + " = 10 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 2159000   \t\t\t THEN 1\n" +
                "when " + mode_name + " = 10 AND 2161000 > (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 2159000   THEN 1+1.6\n" +
                "when " + mode_name + " = 10 AND 2161000 <= (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) \t\t THEN 1+2.8\n" +
                "\n" +
                "when " + mode_name + " = 11 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 10365000 \t\t\t THEN 1\n" +
                "when " + mode_name + " = 11 AND 10369000 > (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 10365000 THEN 1+1.7\n" +
                "when " + mode_name + " = 11 AND 10369000 <= (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) \t\t THEN 1+2.9\n" +
                "\n" +
                "when " + mode_name + " = 12 and (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) <= 31534002  \t\t\t THEN 1\n" +
                "when " + mode_name + " = 12 AND 31538002 > (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) > 31534002 THEN 1+1.8\n" +
                "when " + mode_name + " = 12 AND 31538002 <= (strftime(\"%s\", datetime(\"now\", '+3 hour')) - strftime(\"%s\", " + date_to_update + " )) \t\t THEN 1+3\n" +
                "\n" +
                "when " + mode_name + " = 13 THEN 1\n" +
                "END;");


        SharedPreferences sharedPreferencesNames = context.getSharedPreferences("Sections", MODE_PRIVATE);
        Iterator<?> section_bool_for_commas = sharedPreferencesNames.getAll().values().iterator();
        Iterator<?> section_bool = sharedPreferencesNames.getAll().values().iterator();
        Iterator<String> section_name = sharedPreferencesNames.getAll().keySet().iterator();

        String inClause = "(";
        int i = 0;
        while (section_bool.hasNext()) {
            if (section_bool.next().toString().equals("true") && i == 0) {

                inClause = inClause + "'" + section_name.next() + "'";
                i++;
            } else if (section_bool_for_commas.next().toString().equals("true") && i > 0) {
                inClause = inClause + ", '" + section_name.next() + "'";
            } else {
                section_name.next();
            }

        }
        String sections = inClause + ")";

        if (date_to_update.equals("prepositionDate") || date_to_update.equals("idiomDate")) {
            wordInfo = db.rawQuery(
                    "SELECT  " +
                            COL_5 + ", " +
                            COL_6 + ", " +
                            COL_9 + ", " +
                            COL_2 + ", " +
                            COL_13 + ", " +
                            COL_3 + ", " +
                            COL_11 + ", " +
                            COL_15 + ", " +
                            COL_17 + " " +
                            "FROM " + TABLE_NAME + " " +
                            "WHERE Language = 'English'" + " " +
                            " AND form = '" + mode + "' " +
                            "ORDER by " + COL_7 + " DESC " +
                            "LIMIT 1 ;", null);
        } else {
            wordInfo = db.rawQuery(
                    "SELECT  " +
                            COL_5 + ", " +
                            COL_6 + ", " +
                            COL_9 + ", " +
                            COL_2 + ", " +
                            COL_13 + ", " +
                            COL_3 + ", " +
                            COL_11 + ", " +
                            COL_15 + ", " +
                            COL_17 + " " +
                            "FROM " + TABLE_NAME + " " +
                            "WHERE Language IN " + language_name + " " +
                            "AND (Language || Section) IN " + sections + " " +
                            "ORDER by " + COL_7 + " DESC " +
                            "LIMIT 1 ;", null);
        }


        wordInfo.moveToFirst();

        return wordInfo;
    }

    /**
     * Confirm if there is more words to be displayed by existing configurations
     *
     * @param language_name
     * @param mode_name
     * @return
     */
    public Boolean noMoreWords(String language_name, String mode_name, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();

        updateSignificance(mode_name, language_name, context);

        Cursor significanceData = db.rawQuery(
                "SELECT  " +
                        COL_7 +
                        " FROM " + TABLE_NAME +
                        " WHERE " + COL_2 + " IN " + language_name +
                        " AND " + COL_7 + " > 2 " +
                        " ORDER BY " + COL_7 + ";"
                , null);

        if (significanceData.getCount() < 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Functions to retreat language and section for alertDialogs
     */
    public Cursor getAllLanguages() {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.i(TAG, "getAllLanguages: " + "SELECT DISTINCT(" + COL_2 + ")" +
                " FROM " + TABLE_NAME + ";");
        Cursor exportLanguages = db.rawQuery(
                "SELECT DISTINCT " + COL_2 +
                        " FROM " + TABLE_NAME + ";",
                null
        );

        exportLanguages.moveToFirst();

        return exportLanguages;
    }

    public Cursor getAllSections() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor exportSections = db.rawQuery(
                "SELECT DISTINCT " + COL_2 + ", " + COL_3 +
                        " FROM " + TABLE_NAME + ";",
                null
        );
        // TODO check if this moveToFirst doesnt create more issues :?
        exportSections.moveToFirst();

        return exportSections;
    }

    /**
     * Function to 'remove' word from learning if you know it by heartv
     */
    public void knownWord(String CurrentWord, String CurrentLearningMode) {
        SQLiteDatabase db = this.getWritableDatabase();

        String ColumnToUpdate;
        if (CurrentLearningMode.equals("Classical")) {
            ColumnToUpdate = "Level_Classical";
        } else {
            ColumnToUpdate = "Level_Swapped";
        }


        db.execSQL("UPDATE " + TABLE_NAME +
                " SET " + ColumnToUpdate + " = 13" +
                " WHERE " + COL_5 + " = '" + CurrentWord + "';"
        );
    }

    /**
     * Function to parse data from excel to a database
     */
    public void parsingExcel(String word, String translation, String language, String section) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COL_5, word);
        cv.put(COL_6, translation);
        cv.put(COL_2, language);
        cv.put(COL_3, section);

        db.insert(TABLE_NAME, null, cv);
    }

    public Cursor exportWords(String language, String section) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT DISTINCT " + COL_5 +
                ", " + COL_6 +
                " FROM " + TABLE_NAME +
                " WHERE " + COL_2 + "='" + language +
                "' AND " + COL_3 + "='" + section +
                "' ORDER BY " + COL_5 + " DESC" + ";";
        Log.d(TAG, "exportWords: aaaaaaa " + sql);
        Cursor exportWords = db.rawQuery(sql
                , null
        );
        // TODO check if this moveToFirst doesnt create more issues :?
        //exportWords.moveToFirst();

        return exportWords;
    }


    public void storeProgress(int newWordsToday, String wordsToday, String wordsTotal, int wordsRevised) {
        SQLiteDatabase db = this.getWritableDatabase();
        DateFormat currentDayDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String lastDay = currentDayDateFormat.format(date);

        ContentValues cv = new ContentValues();

        cv.put(PROGRESS_COL2, lastDay);
        cv.put(PROGRESS_COL3, newWordsToday);
        cv.put(PROGRESS_COL4, Integer.parseInt(wordsToday));
        cv.put(PROGRESS_COL5, Integer.parseInt(wordsTotal));
        cv.put(PROGRESS_COL6, newWordsToday);

        Log.d(TAG, "storeProgress: " + lastDay + " : " + newWordsToday + " : " + Integer.parseInt(wordsToday) + " : " + Integer.parseInt(wordsTotal));
        db.insert(PROGRESS_TABLE_NAME, null, cv);
    }

    public void changeDay() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("UPDATE " + TABLE_NAME +
                " SET classicalDate = DATE(classicalDate, '-1 day')" +
                ";"
        );
    }


    public Cursor getStatistics() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT " + PROGRESS_COL2 +
                ", " + PROGRESS_COL3 +
                ", " + PROGRESS_COL4 +
                ", " + PROGRESS_COL5 +
                " FROM " + PROGRESS_TABLE_NAME +
                " ORDER BY " + PROGRESS_COL2 + " DESC" +
                " LIMIT 7" + ";";

        Cursor exportWords = db.rawQuery(sql, null);
        // TODO check if this moveToFirst doesnt create more issues :?
        exportWords.moveToFirst();

        return exportWords;
    }

    public void insertCloudDictionary(String learningLanguage, String sectionName, ArrayList<String> words, ArrayList<String> translations) {
        SQLiteDatabase db = this.getWritableDatabase();

        int i;
        Log.d(TAG, "insertCloudDictionary: 2 : " + words.size());
        for (i = 0; i < words.size(); i++) {
            ContentValues cv = new ContentValues();
            cv.put(COL_2, learningLanguage);
            cv.put(COL_3, sectionName);
            cv.put(COL_5, words.get(i));
            cv.put(COL_6, translations.get(i));

            Log.d(TAG, "insertCloudDictionary: " + learningLanguage + ": " + sectionName + ": " + words.get(i) + ": " + translations.get(i));
            db.insert(TABLE_NAME, null, cv);
        }

    }

    public String getFlagLanguage(String language_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT " + LANGUAGE_CODE_COL3 +
                " FROM " + LANGUAGE_CODE_TABLE_NAME +
                " WHERE " + LANGUAGE_CODE_COL3 + " = '" + language_name + "';";

        Cursor exportWords = db.rawQuery(sql, null);

        if (exportWords.getCount() == 0) {
            return language_name;
        } else {
            exportWords.moveToFirst();
            return exportWords.getString(0);
        }
    }

    public String getFlagISO(String language_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT " + LANGUAGE_CODE_COL2 +
                " FROM " + LANGUAGE_CODE_TABLE_NAME +
                " WHERE " + LANGUAGE_CODE_COL3 + " = '" + language_name + "';";

        Cursor exportWords = db.rawQuery(sql, null);

        if (exportWords.getCount() == 0) {
            return language_name;
        } else {
            exportWords.moveToFirst();
            return exportWords.getString(0);
        }
    }

    public String[] getLanguageArticles(String cursorLanguage) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT " + LANGUAGE_CODE_COL4 +
                " FROM " + LANGUAGE_CODE_TABLE_NAME +
                " WHERE " + LANGUAGE_CODE_COL3 + " = '" + cursorLanguage + "';";

        Cursor exportWords = db.rawQuery(sql, null);
        exportWords.moveToFirst();

        Log.d(TAG, "getLanguageArticles testas3: " + exportWords.getString(0) + " LANGUAGE: " + cursorLanguage);

        if (exportWords.getString(0) == null) {
            return "".split(",");
        } else {
            return exportWords.getString(0).split(",");
        }
    }

    public int countWordsPerLanguage(String language_name) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT COUNT(" + COL_5 + ") " +
                " FROM " + TABLE_NAME +
                " WHERE " + COL_2 + " = '" + language_name +
                //" AND (" + COL_9 + " > 1 " +
                //" OR " + COL_11 + " > 1 " +
                //" OR " + COL_15 + " > 1 " +
                // " OR " + COL_17 + " > 1 " +
                //" OR " + COL_13 + " > 1 )" +
                "';";

        Cursor exportWords = db.rawQuery(sql, null);
        exportWords.moveToFirst();

        return exportWords.getInt(0);
    }


    /** Used in the process of import of new dictionary from local storage
     *
     * @param language
     * @param section
     */
    public void enterDictionaryInformation(String language, String section) {
        SQLiteDatabase db = this.getWritableDatabase();

        // TODO ADD Unique key generator and username later on
        ContentValues cv = new ContentValues();
        cv.put(INFORMATION_COL3, language);
        cv.put(INFORMATION_COL4, section);
        cv.put(INFORMATION_COL7, 0);
        cv.put(INFORMATION_COL8, 0);
        cv.put(INFORMATION_COL9, 1);

        db.insert(INFORMATION_TABLE_NAME, null, cv);
    }

    /**
     *
     */
    public Cursor findDictionaryInformation() {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT " + " * " +
                " FROM " + INFORMATION_TABLE_NAME +
                " ORDER BY " + INFORMATION_COL3 + " ASC " + ";";

        Cursor exportWords = db.rawQuery(sql, null);
        //exportWords.moveToFirst();

        return exportWords;
    }

    public void deleteDictionary(String language, String section){
        Log.i(TAG, "deleteDictionary: " + language + " and " + section);

        // from information table
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + INFORMATION_TABLE_NAME +
                    " WHERE " + INFORMATION_COL3 + "='" + language + "'" +
                " AND " + INFORMATION_COL4 + "= '" + section + "';");

        // from the main table with all words
        db.execSQL("DELETE FROM " + TABLE_NAME +
                " WHERE " + COL_2 + "='" + language + "'" +
                " AND " + COL_3 + "= '" + section + "';");
        db.close();
    }

    public void rateDictionary(String language, String section, String type, Boolean rating) {
        int down = 0;
        int up = 0;
        if (type.equals("up") && rating) {
            down = 0;
            up = 1;
        } else if (type.equals("down") && rating){
            down = 1;
            up = 0;
        } else {
            down = 0;
            up = 0;
        }
        Log.i(TAG, "rateDictionary: down-" + down + ", up-" + up);

        SQLiteDatabase db = this.getWritableDatabase();
        String strSQL = "UPDATE " +  INFORMATION_TABLE_NAME +
                " SET " + INFORMATION_COL7 + "=" + up +
                ", " + INFORMATION_COL8 + "=" + down +
                " WHERE " + INFORMATION_COL3 + "= '" +  language + "' " +
                " AND " + INFORMATION_COL4 + "= '" +  section + "' " +
                ";";
        Log.i(TAG, "rateDictionary: sql script: " + strSQL);
        db.execSQL(strSQL);
        db.close();

        // TODO also create API that connects to the cloud rating
    }

    public Cursor getRating(String language, String section) {
        SQLiteDatabase db = this.getWritableDatabase();

        String sql = "SELECT " + INFORMATION_COL7 +
                ", " + INFORMATION_COL8 + " " +
                " FROM " + INFORMATION_TABLE_NAME +
                " WHERE " + INFORMATION_COL3 + " = '" + language  + "' " +
                " AND " + INFORMATION_COL4 + " = '" + section  + "' " +
                ";";
        Log.i(TAG, "getRating: sqlite: " + sql);
        Cursor exportWords = db.rawQuery(sql, null);
        exportWords.moveToFirst();

        return exportWords;
    }


    public int countTotalWordsPerLanguage(String languageName) { // calculates how many words have been approached overall, implement language setting through preferances
        SQLiteDatabase db = this.getWritableDatabase();

        String sqliteQuery = "SELECT COUNT( " + COL_9 + " )" +
                                ", " + COL_2 + " " +
                                "FROM " + TABLE_NAME + " " +
                                "WHERE (" + COL_9 + " > 1 " +
                                " OR " + COL_13 + " > 1 )" +
                                " AND " + COL_2 + "= '" + languageName + "' " +
                                " ; ";
        Cursor word = db.rawQuery(sqliteQuery, null);
        word.moveToFirst();

        return word.getInt(0);
    }

}

