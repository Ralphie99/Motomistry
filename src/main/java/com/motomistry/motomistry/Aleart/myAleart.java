package com.motomistry.motomistry.Aleart;

import android.content.Context;
import android.graphics.Color;

import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Amit on 5/15/2018.
 */

public class myAleart {
    private Context mContext;
    private SweetAlertDialog pDialog;

    public myAleart(Context context) {
        this.mContext=context;
    }

    public void Loading(String msg,boolean isShow){
        if (isShow) {
            pDialog = new SweetAlertDialog(mContext, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText(msg);
            pDialog.setCancelable(false);
            pDialog.show();
        }else {
            pDialog.dismissWithAnimation();
        }
    }
    public void Error(String msg){
        String er_msg=msg.equals("")?"Something went wrong!":msg;
        new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Oops...")
                .setContentText(er_msg)
                .show();
    }
    public void Confirm(String msg,String btn_txt){
        new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Are you sure?")
                .setContentText(msg)
                .setConfirmText(btn_txt)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
    public void Success(String msg){
        new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Good job!")
                .setContentText(msg)
                .show();
    }
}
