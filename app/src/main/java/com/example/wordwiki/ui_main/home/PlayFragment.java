package com.example.wordwiki.ui_main.home;

import static android.content.Context.MODE_PRIVATE;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blongho.country_data.World;
import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.databinding.FragmentPlayBinding;
import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

public class PlayFragment extends Fragment {
    TextToSpeech t1;
    String textToSpeechLOCALE;
    String languageName;
    String country_name;
    String currentLocale;

    private static final String TAG = "SecondActivity";
    FragmentPlayBinding binding;
    DatabaseHelper myDb;

    Integer valueOfStoredCount, newWordsTodayCounter, wordsRevisedCounter, wordsRevisedTotalCounter;
    ImageFilterView languageImage;
    String word_to_fit, translation_to_fit, cursorWordHidden, cursorWordBolded, cursorWord;
    EditText shownWord, shownTranslation;
    TextView wordsToday, newWordsTotal, newWordsToday, languageShown, sectionShown, wordsRevisedTotal;
    Button showAnswerBtn;
    ImageButton correctBtn, wrongBtn, neutralBtn, toHome;
    BottomNavigationView navBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentPlayBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // hide bottom navigation, since Play is the main fragment and it collides with other objects
        navBar = getActivity().findViewById(R.id.nav_view);
        navBar.setVisibility(View.GONE);

        // Initiate the database helper
        myDb = new DatabaseHelper(getContext());

        //setupSharedPreferences();

        if (wordsRevisedCounter == null) {
            wordsRevisedCounter = 0;
        }

        // Objects that appears in activity
        shownWord = root.findViewById(R.id.word_text);
        shownTranslation = root.findViewById(R.id.translation_text);
        showAnswerBtn = root.findViewById(R.id.show_answer_btn);
        shownWord.setEnabled(false);
        shownTranslation.setEnabled(false);

        wordsToday = root.findViewById(R.id.wording_today_statistics_int);
        newWordsTotal = root.findViewById(R.id.wording_total_statistics_int);
        newWordsToday = root.findViewById(R.id.wording_newToday_statistics_int);
        wordsRevisedTotal = root.findViewById(R.id.wording_revised_statistics_int);

        //languageShown = root.findViewById(R.id.wording_language_text);
        languageImage = root.findViewById(R.id.wording_image);
        sectionShown = root.findViewById(R.id.wording_section_text);

        wordsToday.setText(myDb.countTotalWords().toString());
        newWordsTotal.setText(myDb.countTodayWords().toString());
        wordsRevisedTotal.setText(getRevisedStatistics());

        setButtons();
        displayNewWord();


        t1=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.i(TAG, "onInit: status: " + status);
                if(status != TextToSpeech.ERROR) {
                    Locale currentLocale = new Locale(myDb.getLanguageLocale(languageName), myDb.getFlagISO(languageName).toUpperCase(Locale.ROOT));
                    t1.setLanguage(currentLocale);
                }
            }
        });

        neutralBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick: testing lang and speecherr " + languageName + " and locale: " + t1.getLanguage());
                Toast.makeText(getContext(), "lot",Toast.LENGTH_SHORT).show();
                t1.speak(shownWord.getText().toString(), TextToSpeech.QUEUE_FLUSH, null);

            }
        });

        return root;
    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    private void displayNewWord() {
        // increase revised number, and todays revised counter
        setRevisedStatistics();

        wordsRevisedCounter++;

        Log.d(TAG, "displayNewWord: testas - revised words: " + wordsRevisedCounter);
        if (myDb.ChecksIfIsNewDay()) {
            // store last day progress

            Log.d(TAG, "displayNewWord: testas - revised words: " + wordsRevisedCounter);
            myDb.storeProgress(getCountOfTodayWords(), myDb.countLastDayWords().toString(), myDb.countTotalWords().toString(), wordsRevisedCounter);

            // restart statistics
            wordsRevisedCounter = 0;
            storeCountOfTodayWords(0);
        }

        valueOfStoredCount = getCountOfTodayWords(); //how to avoid it ???
        wordsToday.setText(myDb.countTodayWords().toString()); //after
        newWordsTotal.setText(myDb.countTotalWords().toString());
        newWordsToday.setText(valueOfStoredCount.toString());
        wordsRevisedTotal.setText(getRevisedStatistics());

        Cursor wordsCursor = myDb.getOneWord(getModePreference(), getLanguage(), getContext());
        cursorWord = wordsCursor.getString(0);
        String cursorTranslation = wordsCursor.getString(1);
        String cursorLanguage = wordsCursor.getString(3);

        //cursorWord = addBoldedArticle(cursorWord, cursorLanguage, false);
        cursorWordHidden = addBoldedArticle(cursorWord, cursorLanguage, true, false, false);
        cursorWordBolded = addBoldedArticle(cursorWord, cursorLanguage, false, false, true);
        cursorWord = addBoldedArticle(cursorWord, cursorLanguage, false, true, false);
        // article and classical has the same structure, except that article is bolded
        if (getModePreference().equals("Classical")) { // for changing mode {classical, article, swapped}
            shownWord.setVisibility(View.VISIBLE);
            shownTranslation.setVisibility(View.INVISIBLE);

            shownWord.setText(HtmlCompat.fromHtml(cursorWordBolded, HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else if (getModePreference().equals("Swapped")) {
            shownWord.setVisibility(View.INVISIBLE);
            shownTranslation.setVisibility(View.VISIBLE);

            shownWord.setText(HtmlCompat.fromHtml(cursorWordBolded, HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else if (getModePreference().equals("Article")) {
            shownWord.setVisibility(View.VISIBLE);
            shownTranslation.setVisibility(View.VISIBLE);

            shownWord.setText(HtmlCompat.fromHtml(cursorWordHidden, HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else if (getModePreference().equals("Preposition")) {
            shownWord.setVisibility(View.INVISIBLE);
            shownTranslation.setVisibility(View.VISIBLE);

            shownWord.setText(HtmlCompat.fromHtml(cursorWord, HtmlCompat.FROM_HTML_MODE_COMPACT));
        } else if (getModePreference().equals("Idiom")) {
            shownWord.setVisibility(View.INVISIBLE);
            shownTranslation.setVisibility(View.VISIBLE);

            shownWord.setText(HtmlCompat.fromHtml(cursorWord, HtmlCompat.FROM_HTML_MODE_COMPACT));
        }


        //shownWord.setText(HtmlCompat.fromHtml(cursorWord, HtmlCompat.FROM_HTML_MODE_COMPACT));
        shownTranslation.setText(cursorTranslation);


        //determine which modes level we have
        int position;
        if (getModePreference().equals("Classical")) {
            position = 2;
        } else if (getModePreference().equals("Article")) {
            position = 4;
        } else if (getModePreference().equals("Swapped")) {
            position = 6;
        } else if (getModePreference().equals("Preposition")) {
            position = 7;
        } else {
            position = 8;
        }


        if (wordsCursor.getInt(position) == 1) {
            buttonsLevelOne();

            newWordsTodayCounter = getCountOfTodayWords();
            newWordsTodayCounter++;
            storeCountOfTodayWords(newWordsTodayCounter);
        } else {
            showAnswerBtn.setVisibility(View.VISIBLE);
            neutralBtn.setVisibility(View.VISIBLE);
            wrongBtn.setVisibility(View.VISIBLE);
        }

        languageName = wordsCursor.getString(3);
        country_name = myDb.getFlagISO(languageName);
        currentLocale = myDb.getLanguageLocale(languageName);
        int flag = World.getFlagOf(country_name);

        //languageShown.setText(wordsCursor.getString(3));
        languageImage.setImageResource(flag);
        sectionShown.setText(wordsCursor.getString(5));

        // set LOCALE if it exists
        t1=new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                Log.i(TAG, "onInit: status: " + status);
                if(status != TextToSpeech.ERROR) {
                    Locale currentLocale = new Locale(myDb.getLanguageLocale(languageName), myDb.getFlagISO(languageName).toUpperCase(Locale.ROOT));
                    t1.setLanguage(currentLocale);
                }
            }
        });

    }

    private String addBoldedArticle(String cursorWord, String cursorLanguage, Boolean isArticle, Boolean theWord, Boolean boldedWord) {
        // find what articles we are using in a language we have
        String[] article_list = myDb.getLanguageArticles(cursorLanguage);

        // find first word, which is article in this case

        String[] input = cursorWord.trim().split("\\s+");
        String output_text = "";

        for (String word : input) {
            if (ArrayUtils.contains(article_list, word)) {
                if (isArticle) {
                    output_text = output_text + " " + "<B> ___ </B>";
                } else if (boldedWord) {
                    output_text = output_text + " " + "<B>" + word + "</B>";
                } else if (theWord) {
                    output_text = output_text + " " + word;
                }

            } else {
                output_text = output_text + " " + word;
            }
        }

        Log.d(TAG, "addBoldedArticle: test1: " + output_text);

        return output_text;
    }

    public void showAnswer(View v) {
        shownTranslation.setVisibility(View.VISIBLE);
        shownWord.setVisibility(View.VISIBLE);
        showAnswerBtn.setEnabled(false);

        // to change back the article from ___ to the true one
        if (getModePreference().equals("Article")) {
            shownWord.setText(HtmlCompat.fromHtml(cursorWordBolded.trim(), HtmlCompat.FROM_HTML_MODE_COMPACT));
        }
    }


    public void onRightAnswer(View v) {
        shownWord.setText(cursorWord.trim());
        showAnswerBtn.setEnabled(true);
        myDb.UpdateLevelRight(shownWord.getText().toString(), getModePreference(), getLanguage());// Might be avoided

        displayNewWord();
    }


    public void onWrongAnswer(View v) {
        shownWord.setText(cursorWord.trim());

        shownWord.setVisibility(View.VISIBLE);
        showAnswerBtn.setEnabled(true);

        myDb.UpdateLevelWrong(shownWord.getText().toString(), getModePreference(), getLanguage());
        displayNewWord();
    }

    public void buttonsLevelOne() {
        shownWord.setVisibility(View.VISIBLE);
        shownTranslation.setVisibility(View.VISIBLE);
        showAnswerBtn.setVisibility(View.INVISIBLE);
        //correctBtn.setText("Next Word");
        neutralBtn.setVisibility(View.VISIBLE);
        wrongBtn.setVisibility(View.INVISIBLE);
    }


    public void setRevisedStatistics() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("statistics", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int currentValue = sharedPreferences.getInt("revised", 0);
        editor.putInt("revised", currentValue + 1);
        editor.apply();
    }

    public String getRevisedStatistics() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("statistics", MODE_PRIVATE);

        return String.valueOf(sharedPreferences.getInt("revised", 0)); //second argument is default, if your request is empty
    }

    public void storeCountOfTodayWords(int InputInteger) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("statistics", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("The Count", InputInteger);
        editor.apply();
    }

    public int getCountOfTodayWords() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("statistics", MODE_PRIVATE);
        return sharedPreferences.getInt("The Count", 0); //second argument is default, if your request is empty
    }

    public void changeModePreference(String inputString) {//ModePreference for changing types of learning
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("general", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Mode", inputString);
        editor.apply();
    }

    public String getModePreference() {//ModePreference for changing types of learning
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Modes", MODE_PRIVATE);

        ArrayList<String> existing_modes = new ArrayList<>();
        if (sharedPreferences.getBoolean("Classical", false)) {
            existing_modes.add("Classical");
        }

        if (sharedPreferences.getBoolean("Swapped", false)) {
            existing_modes.add("Swapped");
        }

        if (sharedPreferences.getBoolean("Article", false)) {
            existing_modes.add("Article");
        }

        if (sharedPreferences.getBoolean("Preposition", false)) {
            existing_modes.add("Preposition");
        }

        if (sharedPreferences.getBoolean("Idiom", false)) {
            existing_modes.add("Idiom");
        }

        int random_index = new Random().nextInt(existing_modes.size());
        Log.d(TAG, "getModePreference: test index " + random_index);
        return existing_modes.get(random_index); //second argument is default, if your request is empty
    }

    public String getLanguage() {
        SharedPreferences sharedPreferencesNames = getActivity().getSharedPreferences("Languages", MODE_PRIVATE);
        Iterator<?> language_bool_for_commas = sharedPreferencesNames.getAll().values().iterator();
        Iterator<?> language_bool = sharedPreferencesNames.getAll().values().iterator();
        Iterator<String> language_name = sharedPreferencesNames.getAll().keySet().iterator();

        String inClause = "(";
        int i = 0;
        while (language_bool.hasNext()) {
            if (language_bool.next().toString().equals("true") && i == 0) {

                inClause = inClause + "'" + language_name.next() + "'";
                i++;
            } else if (language_bool_for_commas.next().toString().equals("true") && i > 0) {
                inClause = inClause + ", '" + language_name.next() + "'";
            } else {
                language_name.next();
            }

        }
        inClause = inClause + ")";
        return inClause;
    }

    public void setButtons() {
        showAnswerBtn = binding.getRoot().findViewById(R.id.show_answer_btn);
        correctBtn = binding.getRoot().findViewById(R.id.correct_answer_btn);
        neutralBtn = binding.getRoot().findViewById(R.id.neutral_answer_btn);
        wrongBtn = binding.getRoot().findViewById(R.id.wrong_answer_btn);

        showAnswerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswer(view);
            }
        });

        correctBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRightAnswer(view);
            }
        });


        correctBtn.setOnLongClickListener(v -> {
            myDb.knownWord(myDb.getOneWord(getModePreference(),
                    getLanguage(), getContext()).getString(0), getModePreference());

            onRightAnswer(null);
            return true;
        });

        neutralBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO remove it later
                myDb.changeDay();

            }
        });

        wrongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWrongAnswer(view);

                // increase failedCount for the word
                myDb.failedCountIncreasement(sectionShown.getText().toString(), shownWord.getText().toString());
            }
        });

        toHome = binding.getRoot().findViewById(R.id.back_to_previous);
        toHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // make the navBar visible again when getting back from Play fragment
                navBar.setVisibility(View.VISIBLE);
                NavController navController = Navigation.findNavController(view);
                navController.navigate(R.id.action_navigation_play_to_navigation_home);
            }
        });
    }
}