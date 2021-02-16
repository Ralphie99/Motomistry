package com.motomistry.motomistry;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import com.motomistry.motomistry.Fatch.Fatch;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.Session;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {

    private Button BtnContinue;
    private EditText PhoneNumber;
    String mobile;
    private CustomDialog dialog;
    InputMethodManager mgr;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        dialog=new CustomDialog(this);
        session = new Session(this);
        mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        FindView();
        PhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==10){
                    mobile = PhoneNumber.getText().toString();
                    Constant.MOBILE_NUMBER=mobile;
                    mgr.hideSoftInputFromWindow(PhoneNumber.getWindowToken(), 0);
                    new Fatch(RegistrationActivity.this,"REGISTRATION",Constant.getMobileRegistration(mobile));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void FindView() {
        BtnContinue = (Button) findViewById(R.id.btn_continue);
        PhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        PhoneNumber.setText(GetNumberFromPhone());
        BtnContinue.setOnClickListener(this);
    }
    public void FrgmentTrans(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_out_up, R.anim.slide_out_down);
        transaction.replace(R.id.contant_fram, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void replace_Fragment(Fragment fragment, int enterAnimation, int exitAnimation) {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.setCustomAnimations(enterAnimation, exitAnimation);
        transaction.replace(R.id.contant_fram, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    @Override
    public void onClick(View view) {
        if (view == BtnContinue) {
            mobile = PhoneNumber.getText().toString();
            if(mobile.isEmpty()){
                PhoneNumber.setError("Enter Phone Number!");
            }
            else
            {
                Constant.MOBILE_NUMBER=mobile;
                mgr.hideSoftInputFromWindow(PhoneNumber.getWindowToken(), 0);
                session.setIsFirstLauch(true);
                new Fatch(this,"REGISTRATION",Constant.getMobileRegistration(mobile));
            }
        }
    }
    private String GetNumberFromPhone() {
        TelephonyManager tMgr = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") String mPhoneNumber = tMgr.getLine1Number();
        String result=(mPhoneNumber.length()>10)?mPhoneNumber.substring(2,mPhoneNumber.length()):mPhoneNumber;
        return result;
    }
}