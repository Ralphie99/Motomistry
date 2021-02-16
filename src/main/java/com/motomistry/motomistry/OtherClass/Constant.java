package com.motomistry.motomistry.OtherClass;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by Amit on 24-Mar-18.
 */

public class Constant {
    public static String MOBILE_NUMBER = "";
    public static String Base_url="http://18.188.188.62/Moto_mistri/";
    public static String Add_Base_url = "http://registration_vehicle.php";
    public static int Category_id = 0;
    public static boolean IsFourVechile=false;
    public static boolean clickedFromHome = false;
    public static boolean view_from_profile = false;
    public static boolean isFromFrequent = false;
    public static boolean IsTowVechile=false;
    public static boolean fromOutside = false;
    public static boolean isCar = false;
    public static boolean isFiltered = false;
    public static boolean isBike = false;
    public static String DURATION = "";
    public static String DISTANCE = "";
    public static String MOBILE_REGISTRATION="mobile?mobile=";
    public static String OTP_VERIFICATION_URL="vrify?otp=";
    public static String CLINT_REGISTRATION_URL="client_reg?mobile=";
    public static String VEHICLES_TYPE_URL="Mdetails?vehicale_type=";
    public static String SERVICE_LIST_URL=Base_url+"Service_list";
    public static String GET_MODEL_LIST_URL="V_Man?mid=";
    public static String GET_SLIDER_IMAGE_URL=Base_url+"getslider_image";
    public static String GET_SUB_SERVICE_URL=Base_url+"sub_service?vehicle_type=";

    public static ArrayList<Integer> brand_checked_ids = new ArrayList<>();
    public static ArrayList<Integer> model_checked_ids = new ArrayList<>();

    public static String getMobileRegistration(String mobile) {
        return Base_url+MOBILE_REGISTRATION+mobile;
    }

    public static String getOtpVerificationUrl(String  otp) {
        return Base_url+OTP_VERIFICATION_URL+otp;
    }

    public static String getClintRegistrationUrl(String mobile,String name,String email,int city_id) {
        return Base_url+CLINT_REGISTRATION_URL+mobile+"&name="+ getEncodeUrl(name)+"&email="+email+"&city_id="+city_id;
    }
    public static String getEncodeUrl(String msg){
        try {
           return URLEncoder.encode(msg, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getVehiclesTypeUrl() {
        String v_id=IsFourVechile?"1":"0";
        return Base_url+VEHICLES_TYPE_URL+v_id;
    }

    public static String getGetModelListUrl(int m_id,int v_type) {
        return Base_url+GET_MODEL_LIST_URL+m_id+"&vehicale_type="+v_type;
    }

    public static String getGetSubServiceUrl(int service_id) {
        String v_id=isCar?"1":"0";
        return GET_SUB_SERVICE_URL+v_id+"&service_id="+service_id;
    }
}
