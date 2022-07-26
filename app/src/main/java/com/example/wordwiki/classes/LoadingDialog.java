package com.example.wordwiki.classes;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import com.example.wordwiki.R;

public class LoadingDialog {

    Activity activity;
    AlertDialog dialog;

    public LoadingDialog(Activity myActivity) {
        activity = myActivity;
    }

    public void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.classes_custom_dialog_load, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(activity.getDrawable(R.drawable.classes_custom_dialog_bg));
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.90);
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.40);

        dialog.getWindow().setLayout(width, height);
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
