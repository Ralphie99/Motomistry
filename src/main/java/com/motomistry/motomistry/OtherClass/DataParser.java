package com.motomistry.motomistry.OtherClass;

import android.util.Log;

import com.motomistry.motomistry.Fragment.Shop;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Nikhil on 09-Apr-18.
 */

public class DataParser {

    public List<HashMap<String, String>> parse(String jsonData) {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            Log.d("Places", "parse");
            jsonObject = new JSONObject((String) jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            Log.d("Places", "parse error");
            e.printStackTrace();
        }
        return getPlaces(jsonArray);
    }

    private List<HashMap<String, String>> getPlaces(JSONArray jsonArray) {
        int placesCount = jsonArray.length();
        List<HashMap<String, String>> placesList = new ArrayList<>();
        HashMap<String, String> placeMap = null;
        Log.d("Places", "getPlaces");

        for (int i = 0; i < placesCount; i++) {
            try {
                placeMap = getPlace((JSONObject) jsonArray.get(i));
                placesList.add(placeMap);
                Log.d("Places", "Adding places");

            } catch (JSONException e) {
                Log.d("Places", "Error in Adding places");
                e.printStackTrace();
            }
        }
        return placesList;
    }

    private HashMap<String, String> getPlace(JSONObject googlePlaceJson) {
        HashMap<String, String> googlePlaceMap = new HashMap<String, String>();
        String placeName = "-NA-";
        String vicinity = "-NA-";
        String latitude = "";
        String longitude = "";
        String reference = "";

        Log.d("getPlace", "Entered");

        try {
            if (!googlePlaceJson.isNull("name")) {
                placeName = googlePlaceJson.getString("name");
            }
            if (!googlePlaceJson.isNull("vicinity")) {
                vicinity = googlePlaceJson.getString("vicinity");
            }
            latitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lat");
            longitude = googlePlaceJson.getJSONObject("geometry").getJSONObject("location").getString("lng");
            reference = googlePlaceJson.getString("reference");
            googlePlaceMap.put("place_name", placeName);
            googlePlaceMap.put("vicinity", vicinity);
            googlePlaceMap.put("lat", latitude);
            googlePlaceMap.put("lng", longitude);
            googlePlaceMap.put("reference", reference);
            Log.d("getPlace", "Putting Places");
        } catch (JSONException e) {
            Log.d("getPlace", "Error");
            e.printStackTrace();
        }
        return googlePlaceMap;
    }

    public HashMap<String,String> parseDirections(String jsonData){

        JSONArray jsonArray = null;
        JSONArray jsonArray2 = null;
        JSONObject jsonObject;

        Log.e("JSONDATA",jsonData);

        try {
            jsonObject = new JSONObject((String) jsonData);
            Log.e("JSONOBJECT",jsonObject.toString());
            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");
            jsonArray2 = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            Log.e("JSONARRAY",jsonArray.toString());
        } catch (JSONException e) {
             e.printStackTrace();
        }

        return getDuration(jsonArray);
    }

    public String[] parseDirections2(String jsonData){

        JSONArray jsonArray = null;
        JSONObject jsonObject;

        Log.e("JSONDATA",jsonData);

        try {
            jsonObject = new JSONObject((String) jsonData);
            Log.e("JSONOBJECT",jsonObject.toString());jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs");

            jsonArray = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONArray("steps");
            Log.e("JSONARRAY",jsonArray.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getPaths(jsonArray);
    }

    public HashMap<String,String> getPhoto_Data(String jsonData){

        JSONObject jsonObject;
        String place_id="";
        String photo_reference="";
        String width="";
        HashMap<String,String> photo_data = new HashMap<>();

        try {
            jsonObject = new JSONObject((String) jsonData);

            place_id = jsonObject.getJSONArray("results").getJSONObject(0).getString("place_id");
            photo_reference = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("photos").getJSONObject(0).getString("photo_reference");
            Log.e("PHOTO_REFERENCE",photo_reference);
            width = jsonObject.getJSONArray("results").getJSONObject(0).getJSONArray("photos").getJSONObject(0).getString("width");

            photo_data.put("place_id",place_id);
            photo_data.put("photo_ref",photo_reference);
            photo_data.put("width",width);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return photo_data;
    }

    public String[] getPaths(JSONArray googleStepsJson){

        String polylines[] = new String[googleStepsJson.length()];

        for(int i =0 ;i<googleStepsJson.length();i++){

            try {
                polylines[i] = getPath(googleStepsJson.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return polylines;
    }


    public String getPath(JSONObject googlePathJson){

        String polyline = "";
        try {
           polyline  = googlePathJson.getJSONObject("polyline").getString("points");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return polyline;
    }

    private HashMap<String,String> getDuration(JSONArray googleDirectionsJson){

        HashMap<String,String> googleDirectionsMap = new HashMap<>();
        String duration="";
        String distance="";

        try {
            Log.d("json response",googleDirectionsJson.getJSONObject(0).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            duration = googleDirectionsJson.getJSONObject(0).getJSONObject("duration").getString("text");

            distance = googleDirectionsJson.getJSONObject(0).getJSONObject("distance").getString("text");

            googleDirectionsMap.put("duration",duration);
            googleDirectionsMap.put("distance",distance);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return googleDirectionsMap;
    }
}
