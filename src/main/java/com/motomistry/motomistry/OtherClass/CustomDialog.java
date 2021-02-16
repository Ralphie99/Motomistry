package com.motomistry.motomistry.OtherClass;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.motomistry.motomistry.R;

public class CustomDialog extends Dialog {


    private final Context context;
    private AlertDialog alertDialog;
    private PopupWindow popupWindow;

    public CustomDialog(Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_alert);
    }
    public void showAlertDialog(LayoutInflater inflater, boolean t) {
        if (t) {
            alertDialog = new AlertDialog.Builder(context).create();
            View alertLayout = inflater.inflate(R.layout.loading_alert, null);
            alertDialog.setView(alertLayout);
            alertDialog.show();
        } else {
            if (alertDialog.isShowing()) alertDialog.dismiss();
        }
    }
    public PopupWindow popupDisplay() {
        popupWindow = new PopupWindow(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.popup_msg_layout, null);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
//        popupWindow.setContentView(view);
//        findViews(view);
        return popupWindow;
    }
}
