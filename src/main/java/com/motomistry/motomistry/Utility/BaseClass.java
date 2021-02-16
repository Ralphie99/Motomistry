package com.motomistry.motomistry.Utility;

import android.content.Context;

import com.motomistry.motomistry.OtherClass.Session;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

public class BaseClass {
    Context context;
    public static String BaseUrl="http://18.188.188.62/Moto_mistri/";
    public static String GET_VENDER_LIST_URL="APP_get_vendor_list_according_service?Service_id=";
    public static String GET_INSURENCE_DETAILS_URL="insurance_companey_details?Insurance_company_ID=";
    public static String GET_DRIVING_SCHOOL_DETAILS_URL="Driveing_school_details?Driveing_school_ID=";
    public static String get_service_according_to_vehicle_type_URL="get_service_according_to_vehicle_type?vehicle_Type=";
    public static String get_vehicle_details_according_to_user_URL="get_vehicle_details_according_to_user?user_id=";
    public static String GET_VEHICAL_REG_URL="reg_vehicle?vehical_type=";
    public static String GET_BOOK_SERVICE_URL="Book_service?Vendor_ID=";
    public static String GET_VEHICLE_BOOKING_HISTORY_URL="get_vehicle_booked_history?user_id=";
    public static String MY_VEHICAL_NAME;
    public static int MY_VEHICAL_ID;
    public static int TOTAL_CHARGE;
    public static String VANDER_NAME;
    public static int VEHICAL_TYPE;
    public static int M_ID;
    public static int m_ID;
    public static int VENDER_ID;
    public static String Sub_Service_ID;
    private Session session;
    private static String USER_ID;
    public static int HOME_VEHICLE_TYPE = 0;
    public static int Service_ID_Selected;

    public BaseClass(Context context) {
        this.context = context;
        GetRecordFromSession();
    }

    private void GetRecordFromSession(){
        session=new Session(context);
        HashMap<String, String> user = session.getUserDetails();
        USER_ID = user.get(Session.USER_ID_KEY);
    }
    public static String getGetVenderListUrl(int s_id) {
        return BaseUrl+GET_VENDER_LIST_URL+s_id;
    }

    public static String getGetSubServiceUrl(int v_id,int v_type,int Service_id) {
        return BaseUrl+"APP_get_subservice_list_according_vendor?Vendor_id="+v_id+"&vehicle_type="+v_type+"&cat_id="+Service_id;
    }

    public static String getGetInsurenceDetailsUrl(int c_id) {
        return BaseUrl+GET_INSURENCE_DETAILS_URL+c_id;
    }

    public static String getGetDrivingSchoolDetailsUrl(int ds_id) {
        return BaseUrl+GET_DRIVING_SCHOOL_DETAILS_URL+ds_id;
    }

    public static String getGet_service_according_to_vehicle_type_URL(int v_type) {
        return BaseUrl+get_service_according_to_vehicle_type_URL+v_type;
    }

    public static String getGet_vehicle_details_according_to_user_URL(String user_id) {
        return BaseUrl+get_vehicle_details_according_to_user_URL+user_id;
    }

    public static String getGetVehicalRegUrl(String User_id,int VEHICAL_TYPE,int M_ID,String fuel_type,String rc_no) {
        int F_type=fuel_type.equalsIgnoreCase("Diesel")?1:0;
        return BaseUrl+GET_VEHICAL_REG_URL+VEHICAL_TYPE+"&vehicle_Model_id="+M_ID+"&fuel_type="+F_type+"&User_reg_id="+User_id+"&RCno="+rc_no;
    }
    public static String getGetBookServiceUrl(String u_id,String Date,String extra_txt,int service_type) {
        return BaseUrl+GET_BOOK_SERVICE_URL+VENDER_ID+"&Sub_Service_ID="+Sub_Service_ID+"&book_date="+Date+"&application_user_id="+u_id+"&vehicle_id="+VEHICAL_TYPE+"&extra_requerment="+extra_txt+"&vehicle_id="+MY_VEHICAL_ID+"&service_type="+service_type;
    }
    public static String encodeMsg(String message) {
        try {
            return URLEncoder.encode(message,
                    "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    public static String decodeMsg(String message) {
        String myString = null;
        try {
            return URLDecoder.decode(
                    message, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return message;
        }
    }

    public static String getGetVehicleBookingHistoryUrl(String u_id) {
        return BaseUrl+GET_VEHICLE_BOOKING_HISTORY_URL+u_id;
    }
}
