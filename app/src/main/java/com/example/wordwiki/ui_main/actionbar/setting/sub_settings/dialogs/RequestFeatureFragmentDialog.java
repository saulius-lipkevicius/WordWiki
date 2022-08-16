package com.example.wordwiki.ui_main.actionbar.setting.sub_settings.dialogs;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.profile.FullScreenDialog;
import com.google.android.material.textfield.TextInputEditText;

public class RequestFeatureFragmentDialog extends DialogFragment implements View.OnClickListener{

    private RequestFeatureFragmentDialog.Callback callback;

    public static RequestFeatureFragmentDialog newInstance() {
        return new RequestFeatureFragmentDialog();
    }

    public void setCallback(RequestFeatureFragmentDialog.Callback callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_feature, container, false);

        // to get the intended resize when we have focus on the text field
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        /*
        Bundle bundle = getArguments();
        String dialogTitleText = bundle.getString("title","");
        int dialogFlag = bundle.getInt("flag", 0);

        ImageButton closeDialog = view.findViewById(R.id.dialog_back);
        TextView titleDialog = view.findViewById(R.id.dialog_title);
        ImageView flagDialog = view.findViewById(R.id.dialog_flag);

        closeDialog.setOnClickListener(this);
        titleDialog.setText(dialogTitleText);
        flagDialog.setImageResource(dialogFlag);


         */

        ImageButton closeDialog = view.findViewById(R.id.toolbar_back_btn);
        closeDialog.setOnClickListener(this);

        TextInputEditText editText;
        editText = view.findViewById(R.id.outlined_edit_text);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        TextView editTextCounter = view.findViewById(R.id.fragment_create_user_description_counter);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                editTextCounter.setText(editable.length() + " / 200");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });

        //Map<String, String> learningLanguages = new HashMap<>();
        //Map<String, String> knownLanguages = new HashMap<>();


        //createNewUser(username, "Lithuanian", learningLanguages, knownLanguages);



        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        Log.i(TAG, "onClick: id: " + id);
        switch (id){
            case R.id.toolbar_back_btn:
                sendResultsSettings(0);
                dismiss();
                break;

                /*
                case R.id.dialog_back:
                callback.onActionClick("This is used to communicate with the parent fragment")

                break;

                 */
        }
    }

    public  interface Callback {

        void onActionClick(String name);
    }

    public void sendResultsSettings(int requestCode) {
        // identify sender
        Intent intent = new Intent();
        intent.putExtra("isDismissed", true);
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), requestCode, intent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}