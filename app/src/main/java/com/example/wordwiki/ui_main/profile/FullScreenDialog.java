package com.example.wordwiki.ui_main.profile;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.wordwiki.R;

public class FullScreenDialog extends DialogFragment implements View.OnClickListener{

    private Callback callback;

    static FullScreenDialog newInstance() {
        return new FullScreenDialog();
    }

    public void setCallback(Callback callback) {
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
        View view = inflater.inflate(R.layout.fragment_profile_fullscreen_dialog, container, false);

        Bundle bundle = getArguments();
        String dialogTitleText = bundle.getString("title","");
        int dialogFlag = bundle.getInt("flag", 0);

        ImageButton closeDialog = view.findViewById(R.id.dialog_back);
        TextView titleDialog = view.findViewById(R.id.dialog_title);
        ImageView flagDialog = view.findViewById(R.id.dialog_flag);

        closeDialog.setOnClickListener(this);
        titleDialog.setText(dialogTitleText);
        flagDialog.setImageResource(dialogFlag);


        return view;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id){
            case R.id.dialog_back:
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
}
