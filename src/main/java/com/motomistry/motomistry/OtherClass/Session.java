package com.motomistry.motomistry.OtherClass;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.Fragment.AddVehicle_fromOutside;
import com.motomistry.motomistry.Fragment.My_Orders;
import com.motomistry.motomistry.Model.Accessory_item_model;
import com.motomistry.motomistry.RegistrationActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Document on 4/30/2018.
 */

public class Session {
    public static final String KEY_PRF = "motomistry_prf";
    public final SharedPreferences.Editor editor;
    public Context mContext;
    public SharedPreferences pref;
    public static final String MOBILE_KEY = "mobile";
    public static final String NAME_KEY = "name";
    public static final String PIC_KEY = "pic";
    public static final String USER_ID_KEY = "user_id";
    public static final String EMAIL_KEY = "email";
    public static final String IS_LOGIN = "IsLoggedIn";
    public static final String CITY_KEY = "city_id";
    public static final String SLIDER_IMAGE = "slider_image";
    public static final String IS_FIRST_LAUNCH = "first_launch";
    public static final String CART_DATA = "cart_data";


    public Session(Context context) {
        this.mContext = context;
        pref = mContext.getSharedPreferences(KEY_PRF, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void createLoginSession(String uid,String mobile, String name, String email,String city_id,String pro_pic) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(USER_ID_KEY, uid);
        editor.putString(NAME_KEY, name);
        editor.putString(MOBILE_KEY, mobile);
        editor.putString(EMAIL_KEY, email);
        editor.putString(CITY_KEY, city_id);
        editor.putString(PIC_KEY, pro_pic);
        editor.commit();
    }
    public void SetSliderImage(String list){
        editor.putString(SLIDER_IMAGE ,list);
        editor.commit();
    }

    public void setCartData(String Json){
        editor.putString(CART_DATA,Json);
        editor.commit();
    }

    public void updateCartData(JSONObject jsonObject){

        JSONArray jsonArray = getCartData().put(jsonObject);
        String Json = jsonArray.toString();

        editor.putString(CART_DATA,Json);
        editor.commit();
    }

    public void setIsFirstLauch(boolean flag){
        editor.putBoolean(IS_FIRST_LAUNCH,flag);
    }

    public boolean getIsFirstLauch(){
        return pref.getBoolean(IS_FIRST_LAUNCH,true);
    }

    public JSONArray getCartData(){
        try {
            return new JSONArray(pref.getString(CART_DATA,"[]"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(mContext, RegistrationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        } else {
//            Intent i = new Intent(mContext, AddVehicle_fromOutside.class);
            Intent i = new Intent(mContext, DashbordActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        }
    }
    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(NAME_KEY, pref.getString(NAME_KEY, null));
        user.put(EMAIL_KEY, pref.getString(EMAIL_KEY, null));
        user.put(MOBILE_KEY, pref.getString(MOBILE_KEY, null));
        user.put(USER_ID_KEY, pref.getString(USER_ID_KEY, null));
        user.put(CITY_KEY, pref.getString(CITY_KEY, null));
        user.put(SLIDER_IMAGE, pref.getString(SLIDER_IMAGE , null));
        return user;
    }
    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(mContext, RegistrationActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(i);
    }


}
