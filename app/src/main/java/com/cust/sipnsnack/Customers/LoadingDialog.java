package com.cust.sipnsnack.Customers;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.sipnsnack.R;

public class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;

    public LoadingDialog(Activity myActivity) {
        this.activity = myActivity;
    }

    void startLoadingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_screen, null));
        builder.setCancelable(false);

        dialog = builder.create();
        dialog.show();
    }

    void dismissDialog() {
        dialog.dismiss();
    }

}
