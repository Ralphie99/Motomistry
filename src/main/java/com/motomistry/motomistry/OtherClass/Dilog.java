package com.motomistry.motomistry.OtherClass;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.motomistry.motomistry.R;

/**
 * Created by Document on 4/30/2018.
 */

public class Dilog {
    private AlertDialog alertDialog;

    public void showAlertDialog(Context context, LayoutInflater inflater, boolean t) {
        if (t) {
            alertDialog = new AlertDialog.Builder(context).create();
            View alertLayout = inflater.inflate(R.layout.loading_alert, null);
            alertDialog.setView(alertLayout);
            alertDialog.show();
        } else {
            if (alertDialog.isShowing()) alertDialog.dismiss();
        }
    }
}
