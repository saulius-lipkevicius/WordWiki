package com.example.wordwiki.ui_main.library;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.wordwiki.R;
import com.example.wordwiki.database.DatabaseHelper;
import com.example.wordwiki.databinding.FragmentLibraryBinding;
import com.example.wordwiki.databinding.FragmentParsingBinding;
import com.example.wordwiki.ui_main.profile.models.ExcelParsing;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ParsingFragment extends Fragment {
    FragmentParsingBinding binding;


    private static final String TAG = "MainActivity";
    DatabaseHelper myDb;
    // Declare variables
    private String[] FilePathStrings;
    private String[] FileNameStrings;
    private File[] listFile;
    File file;

    Button btnUpDirectory;

    ArrayList<String> pathHistory;
    String lastDirectory;
    int count = 0;

    ArrayList<ExcelParsing> uploadData;

    ListView lvInternalStorage;

    // for search suggestion
    Map<String, String> language_suggestions_map = new HashMap<String, String>(){
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
            "Nature", "Sport", "Science", "Appearance", "Communication", "Culture"
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentParsingBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences preferences = this.getActivity().getSharedPreferences("general", Context.MODE_PRIVATE);
        String dirPath = preferences.getString("shared_dir", "");
        Uri dir = Uri.parse(dirPath);
        DocumentFile sth = DocumentFile.fromTreeUri(getContext(), dir);


        for(DocumentFile f:sth.listFiles()){
            Log.i(TAG, "onCreateView: " + f.getName());

            for (DocumentFile ff: f.listFiles()) {
                Log.i(TAG, "onCreateView:22222 " + ff.getName());
            }
        }


        myDb = new DatabaseHelper(getContext());


        lvInternalStorage = (ListView) root.findViewById(R.id.lvInternalStorage);
        btnUpDirectory = (Button) root.findViewById(R.id.btnUpDirectory);


        uploadData = new ArrayList<>();
        String[] requiredPermissions = { Manifest.permission.READ_EXTERNAL_STORAGE };
        ActivityCompat.requestPermissions(getActivity(), requiredPermissions, 0);

        pathHistory = new ArrayList<String>();

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        pathHistory.add(count, dir.toString());
        //checkInternalStorage();

        /* lvInternalStorage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastDirectory = pathHistory.get(count);
                if (lastDirectory.equals(adapterView.getItemAtPosition(i))) {

                    main(lastDirectory);

                    Log.d(TAG, "lvInternalStorage: Selected a file for upload: " + lastDirectory);
                    //Execute method for reading the excel data.
                    if (main(lastDirectory)) {
                        showAlert();
                        //showAlertDialogButtonClicked();
                    } else {
                        showAlertDialogButtonClickedNegative();
                    }

                } else {

                    count++;

                    pathHistory.add(count, (String) adapterView.getItemAtPosition(i));
                    checkInternalStorage();
                    Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));


                    //testas
                    String filenameArray[] = pathHistory.get(count).split("\\.");
                    String extension = filenameArray[filenameArray.length - 1];

                    if (main(pathHistory.get(count))) {
                        lastDirectory = pathHistory.get(count);
                        Log.d(TAG, "lvInternalStorage: " + pathHistory.get(count));
                        showAlert();
                        //showAlertDialogButtonClicked();
                    }

                    if (!extension.substring(0, 1).equals(new String("/")) && !main(pathHistory.get(count))) {
                        showAlertDialogButtonClickedNegative();
                        pathHistory.remove(count);
                        count--;
                        checkInternalStorage();
                        Log.d(TAG, "lvInternalStorage2: " + extension.substring(0, 1));
                    }
                }
                btnUpDirectory.setVisibility(View.VISIBLE);
                btnUpDirectory.setBackground(getResources().getDrawable(R.drawable.casual_btn));
            }
        });

        //Goes up one directory level
        btnUpDirectory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                    btnUpDirectory.setBackground(getResources().getDrawable(R.drawable.casual_btn_pressed));
                    toastMessage("You reach initial level");
                } else {
                    pathHistory.remove(count);
                    count--;
                    checkInternalStorage();
                    Log.d(TAG, "btnUpDirectory: " + pathHistory.get(count));

                    if (count == 0) {
                        Log.d(TAG, "btnUpDirectory: You have reached the highest level directory.");
                        btnUpDirectory.setBackground(getResources().getDrawable(R.drawable.casual_btn_pressed));
                    }
                }
            }
        });

         */





        return root;
    }
}