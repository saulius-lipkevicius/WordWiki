package com.example.wordwiki.ui_main.actionbar.setting.sub_settings.dialogs;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wordwiki.R;
import com.example.wordwiki.ui_main.actionbar.setting.models.UserFeedbackModel;
import com.example.wordwiki.ui_main.actionbar.setting.models.UserFeedbackNoStarsModel;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RequestFeatureFragmentDialog extends DialogFragment implements View.OnClickListener{
    TextInputEditText inputText;
    TextView inputTextCounter;
    Button sendFeatureRequest;
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
        // to get the intended resize when we have focus on the text field
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_request_feature, container, false);

        // to get the intended resize when we have focus on the text field
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // close the dialogFragment by connecting it to the listener which finds id of item pressed
        ImageButton closeDialog = view.findViewById(R.id.toolbar_back_btn);
        closeDialog.setOnClickListener(this);

        inputText = view.findViewById(R.id.outlined_edit_text);
        inputTextCounter = view.findViewById(R.id.fragment_create_user_description_counter);
        inputTextListener(view);

        sendFeatureRequest = view.findViewById(R.id.dialog_fragment_send);
        setSendFeedbackBtn(view);

        return view;
    }

    private void inputTextListener(View view) {
        inputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                LinearLayout btnLayout = view.findViewById(R.id.feedbackBtnLayout);
                if (!hasFocus) {
                    hideKeyboard(v);
                    btnLayout.setGravity(Gravity.END | Gravity.BOTTOM);
                } else {
                    btnLayout.setGravity(Gravity.END | Gravity.TOP);
                }
            }
        });


        inputText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputTextCounter.setText(editable.length() + " / 200");
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }
        });
    }

    private void setSendFeedbackBtn(View view) {
        sendFeatureRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("general", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                String feedbackText = inputText.getText().toString();
                Date currentTime = Calendar.getInstance().getTime();
                String fDate = new SimpleDateFormat("yyyy-MM-dd").format(currentTime);

                UserFeedbackNoStarsModel input = new UserFeedbackNoStarsModel(username, feedbackText, "saulius43@gmail.com", fDate);

                final String pushId = FirebaseDatabase.getInstance().getReference().push().getKey();
                FirebaseDatabase.getInstance("https://wordwiki-af0d4-default-rtdb.europe-west1.firebasedatabase.app/").getReference()
                        .child("Feedback").child("Feature")
                        .child(pushId).setValue(input);
                hideKeyboard(view);
                sendResultsSettings(1);
                dismiss();
            }
        });
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
        }
    }

    public  interface Callback {
        void onActionClick(String name);
    }

    public void sendResultsSettings(int requestCode) {
        // identify sender
        Intent intent = new Intent();
        intent.putExtra("isDismissed", true);
        if (requestCode == 1) {
            intent.putExtra("isSnack", true);
        }
        getTargetFragment().onActivityResult(
                getTargetRequestCode(), 0, intent);
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}