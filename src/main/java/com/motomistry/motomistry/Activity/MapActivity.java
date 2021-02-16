package com.motomistry.motomistry.Activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.maps.android.PolyUtil;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.New.Adapter.AdapterVenderList;
import com.motomistry.motomistry.New.Model.ModelVenderList;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.DataParser;
import com.motomistry.motomistry.OtherClass.DownloadUrl;

import com.motomistry.motomistry.OtherClass.FontAwesomeIcon.FontManager;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.SpleshActivity;
import com.motomistry.motomistry.Utility.BaseClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private int PROXIMITY_RADIUS = 5000;
    private static final int PERMISSION_REQUEST_CODE = 200;
    GoogleMap mgoogleMap;
    GoogleApiClient mgoogleApiClient;
    LocationRequest mlocationRequest;
    FusedLocationProviderClient mfusedLocationProviderClient;
    LocationCallback mlocationCallback;
    double longitude, latitude;
    LinearLayout car_wash, car_repair, gas_station, bike_wash, bike_repair;
    ImageView place_image, down;
    ImageView find_fab;
    Animation fab_open, fab_close, rotate_open, rotate_close;
    private boolean isFabOpen = true;
    private String distance = "", duration = "";
    LinearLayout get_directions_button;
    private BottomSheetBehavior mbottomSheetBehavior;
    View bottom_sheet;
    LinearLayout side_layout;
    private boolean isopened = false;
    ArrayList<Polyline> polylineArrayList;
    TextView marker_name, distance_textview, duration_textview;
    CustomDialog alertDialog;
    boolean flag = false;
    String[] pathsList;
    LatLng markerlatlang;
    private RequestQueue requestQueue;
    AlertDialog unavailable;
    private String TAG = "MAP_ACTIVITY";
    private static int REQUEST_CHECK_SETTINGS = 500;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Explode explode = new Explode();
        explode.setDuration(500);
        getWindow().setEnterTransition(explode);

        if (checkPermission()) {

        } else {
            requestPermission();
        }

        find_fab = (ImageView) findViewById(R.id.find_fab);
        car_repair = (LinearLayout) findViewById(R.id.car_repair);
        car_wash = (LinearLayout) findViewById(R.id.car_wash);
        gas_station = (LinearLayout) findViewById(R.id.gas_station);
        bike_repair = (LinearLayout) findViewById(R.id.bike_repair);
        bike_wash = (LinearLayout) findViewById(R.id.bike_wash);

        fab_open = AnimationUtils.loadAnimation(this, R.anim.null_tofade);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fade_tonull);
        rotate_open = AnimationUtils.loadAnimation(this, R.anim.rotate_fab_open);
        rotate_close = AnimationUtils.loadAnimation(this, R.anim.rotate_fab_close);

        marker_name = (TextView) findViewById(R.id.marker_name);
        place_image = (ImageView) findViewById(R.id.place_image);
        distance_textview = (TextView) findViewById(R.id.distance_textview);
        duration_textview = (TextView) findViewById(R.id.duration_textview);

        Typeface iconFont = FontManager.getTypeface(this, FontManager.RALEWAY_SEMI_BOLD);
        unavailable = new AlertDialog.Builder(MapActivity.this).create();
        View alertLayout = LayoutInflater.from(MapActivity.this).inflate(R.layout.unavailable_at_your_loaction, null);
        final TextView ok_dismiss = (TextView) alertLayout.findViewById(R.id.ok_dismiss);
        ok_dismiss.setTypeface(iconFont);
        unavailable.setView(alertLayout);


        down = (ImageView) findViewById(R.id.down);

        alertDialog = new CustomDialog(MapActivity.this);
        alertDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));

        initMap();

        if (isgoogleServicesAvailable()) {
            Toast.makeText(this, "Perfect", Toast.LENGTH_LONG).show();
        }

        mgoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mfusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        mlocationCallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) return;
                for (Location location : locationResult.getLocations()) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    String postal = getPostalfromLatLng(latitude, longitude);
                    if (postal.startsWith("482") || postal.startsWith("483")) {
                    } else {
                        unavailable.show();
                        ok_dismiss.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                finish();
                            }
                        });
                    }
                    LatLng ll = new LatLng(latitude, longitude);
                    mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
                }
            }
        };
        find_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
            }
        });

        car_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mgoogleMap.clear();
                bottom_sheet.setVisibility(View.GONE);

                LatLng latLng = getLatLngFromAddress("LalMati DwarKa Nagar,Jabalpur");
                Toast.makeText(MapActivity.this, "My Lat: " + latLng.latitude + " My Lng: " + latLng.longitude, Toast.LENGTH_LONG).show();

//                GetVendors(BaseClass.BaseUrl+"APP_get_vendor_list_according_service?Service_id="+79);

//                String url = getUrl(latitude, longitude, "car_repair");
//                Object[] DataTransfer = new Object[3];
//                DataTransfer[0] = mgoogleMap;
//                DataTransfer[1] = url;
//                DataTransfer[2] = 1;
//                Log.d("onClick", url);
//                GetMapsData getNearbyPlacesData = new GetMapsData(BitmapDescriptorFactory.fromResource(R.drawable.car_repair_marker));
//                getNearbyPlacesData.execute(DataTransfer);
//                Toast.makeText(MapActivity.this, "Nearby Car Repair Centers", Toast.LENGTH_LONG).show();
            }
        });

        bike_repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mgoogleMap.clear();
                bottom_sheet.setVisibility(View.GONE);

                GetVendors(BaseClass.BaseUrl + "APP_get_vendor_list_according_service?Service_id=" + 78);

//                String url = getUrl(latitude, longitude, "car_repair");
//                Object[] DataTransfer = new Object[3];
//                DataTransfer[0] = mgoogleMap;
//                DataTransfer[1] = url;
//                DataTransfer[2] = 1;
//                Log.d("onClick", url);
//                GetMapsData getNearbyPlacesData = new GetMapsData(BitmapDescriptorFactory.fromResource(R.drawable.car_repair_marker));
//                getNearbyPlacesData.execute(DataTransfer);
//                Toast.makeText(MapActivity.this, "Nearby Car Repair Centers", Toast.LENGTH_LONG).show();
            }
        });


        car_wash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mgoogleMap.clear();
                bottom_sheet.setVisibility(View.GONE);
                String url = getUrl(latitude, longitude, "car_wash");
                Object[] DataTransfer = new Object[2];
                DataTransfer[0] = mgoogleMap;
                DataTransfer[1] = url;
                GetMapsData getNearbyPlacesData = new GetMapsData(BitmapDescriptorFactory.fromResource(R.drawable.car_wash_marker));
                getNearbyPlacesData.execute(DataTransfer);
                Toast.makeText(MapActivity.this, "Nearby Car Wash Centers", Toast.LENGTH_LONG).show();
            }
        });

        gas_station.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("gas clicked", "gas");
                mgoogleMap.clear();
                bottom_sheet.setVisibility(View.GONE);
                String url = getUrl(latitude, longitude, "gas_station");
                Object[] DataTransfer = new Object[3];
                DataTransfer[0] = mgoogleMap;
                DataTransfer[1] = url;
                DataTransfer[2] = 1;
                GetMapsData getNearbyPlacesData = new GetMapsData(BitmapDescriptorFactory.fromResource(R.drawable.gas_marker));
                getNearbyPlacesData.execute(DataTransfer);
                Toast.makeText(MapActivity.this, "Nearby Gas-Stations", Toast.LENGTH_LONG).show();
            }
        });

        get_directions_button = (LinearLayout) findViewById(R.id.get_directions_linear);
        bottom_sheet = findViewById(R.id.bottom_sheet);
        side_layout = (LinearLayout) findViewById(R.id.side_layout);

        mbottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet);

        get_directions_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mbottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                displayPaths(pathsList);
                CameraPosition cameraPosition = CameraPosition.builder().zoom(13).target(markerlatlang).build();
                mgoogleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 2000, null);
            }
        });

        down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mbottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });

        mbottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {

                    case BottomSheetBehavior.STATE_EXPANDED:

                        side_layout.startAnimation(AnimationUtils.loadAnimation(MapActivity.this, R.anim.dependency_slide_up));
//                        get_directions_button.setVisibility(View.GONE);
//                        down.setVisibility(View.VISIBLE);
//                        RelativeLayout.LayoutParams layoutParams =(RelativeLayout.LayoutParams) marker_name.getLayoutParams();
//                        layoutParams.width = 100;
//                        marker_name.setLayoutParams(layoutParams);
//                        marker_name.setTextSize(25);

                        break;

                    case BottomSheetBehavior.STATE_COLLAPSED:

                        side_layout.startAnimation(AnimationUtils.loadAnimation(MapActivity.this, R.anim.dependency_slide_down));
//                        get_directions_button.setVisibility(View.VISIBLE);
//                        down.setVisibility(View.GONE);
                        break;

                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permission, int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted) {

                    } else {
                        requestPermission();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showMessageOKCancel("You need to allow access to the location permission!",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{ACCESS_FINE_LOCATION},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(MapActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    private void initMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mgoogleMap = googleMap;

        mgoogleMap.setMaxZoomPreference(20);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mgoogleMap.setMyLocationEnabled(true);

        mgoogleApiClient.connect();


        mgoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                alertDialog.show();

                if (polylineArrayList != null) {

                    for (Polyline line : polylineArrayList) {
                        line.remove();
                    }
                    polylineArrayList.clear();
                }

                markerlatlang = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                Log.e("ZOOM_LEVEL", String.valueOf(mgoogleMap.getCameraPosition().zoom));

                isopened = true;

                GetMapsData getDirectionsData = new GetMapsData(MapActivity.this);
                Object[] DataTransfer = new Object[3];
                String url = getDirectionsUrl(latitude, longitude, marker.getPosition().latitude, marker.getPosition().longitude);
                DataTransfer[0] = mgoogleMap;
                DataTransfer[1] = url;
                DataTransfer[2] = 2;
                getDirectionsData.execute(DataTransfer);
                bottom_sheet.setVisibility(View.VISIBLE);
                marker_name.setText(marker.getTitle());
                if (isopened) {
                    Animation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -120);
                    animation.setFillAfter(true);
                    animation.setDuration(100);
                    side_layout.setAnimation(animation);
                }
                flag = false;

                return false;
            }
        });

    }

    public boolean isgoogleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailable)) {
            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't Connect to Google Play Services", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mlocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mfusedLocationProviderClient.requestLocationUpdates(mlocationRequest, mlocationCallback, null);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private String getUrl(double latitude, double longitude, String nearbyPlace) {
        Log.e("gas clicked1", "gas");
        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + PROXIMITY_RADIUS);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCG2IpUHIhnV1ZDOEyr2YLUHdA53KeX9gE");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    private String getDirectionsUrl(double current_lat, double current_lng, double end_lat, double end_lng) {
        StringBuilder googleDirectionsUrl = new StringBuilder(("https://maps.googleapis.com/maps/api/directions/json?"));
        googleDirectionsUrl.append("origin=" + current_lat + "," + current_lng);
        googleDirectionsUrl.append("&destination=" + end_lat + "," + end_lng);
        googleDirectionsUrl.append("&key=" + "AIzaSyBgHpKYJtuGKWNVfuTpTvuTeSdriAXFtPI");
        Log.d("getDirectionsUrl", googleDirectionsUrl.toString());
        return googleDirectionsUrl.toString();
    }

    private String getPlaceIdUrl(double latitude, double longitude) {
        StringBuilder googlePlaceIdUrl = new StringBuilder(("https://maps.googleapis.com/maps/api/place/nearbysearch/json?"));
        googlePlaceIdUrl.append("location=" + latitude + "," + longitude);
        googlePlaceIdUrl.append("&radius=1&type=car_repair&sensor=true&key=AIzaSyCG2IpUHIhnV1ZDOEyr2YLUHdA53KeX9gE");

        return googlePlaceIdUrl.toString();
    }

    private void animateFab() {

        if (isFabOpen) {

            find_fab.startAnimation(rotate_open);
            car_repair.startAnimation(fab_open);
            car_wash.startAnimation(fab_open);
            gas_station.startAnimation(fab_open);
            bike_repair.startAnimation(fab_open);
            bike_wash.startAnimation(fab_open);
            car_repair.setClickable(true);
            car_wash.setClickable(true);
            gas_station.setClickable(true);
            bike_repair.setClickable(true);
            bike_wash.setClickable(true);
            isFabOpen = false;
        } else {
            find_fab.startAnimation(rotate_close);
            car_repair.startAnimation(fab_close);
            car_wash.startAnimation(fab_close);
            gas_station.startAnimation(fab_close);
            bike_repair.startAnimation(fab_close);
            bike_wash.startAnimation(fab_close);
            car_repair.setClickable(false);
            car_wash.setClickable(false);
            gas_station.setClickable(false);
            bike_repair.setClickable(false);
            bike_wash.setClickable(false);
            isFabOpen = true;
        }
    }


    public void setPolyline(ArrayList<Polyline> polylines) {

        polylineArrayList = polylines;
    }

    private void displayPaths(String[] directionsList) {

        ArrayList<Polyline> polylines = new ArrayList<Polyline>();


        for (int i = 0; i < directionsList.length; i++) {
            PolylineOptions options = new PolylineOptions();
            options.color(R.color.colorAccent);
            options.width(10);
            options.addAll(PolyUtil.decode(directionsList[i]));
            polylines.add(mgoogleMap.addPolyline(options));

        }

        setPolyline(polylines);
    }


    public class GetMapsData extends AsyncTask<Object, String, String> {

        GoogleMap mMap;
        String googleMapsData;
        String url, duration, distance, place_id;
        Context context;
        BitmapDescriptor icon;
        Bitmap bitmap;
        private GeoDataClient mGeoDataClient;
        int flag = 0;

        public GetMapsData(Context context) {

            this.context = context;
            mGeoDataClient = Places.getGeoDataClient(context, null);
        }

        public GetMapsData(BitmapDescriptor icon) {
            this.icon = icon;
        }

        @Override
        protected String doInBackground(Object... params) {
            try {
                Log.e("gas clicked3", "gas");
                mMap = (GoogleMap) params[0];
                url = (String) params[1];
                flag = (int) params[2];
                DownloadUrl downloadUrl = new DownloadUrl();
                googleMapsData = downloadUrl.readUrl(url);
                Log.e("GOOGLE_MAP_DATA", googleMapsData);

            } catch (Exception e) {

            }
            return googleMapsData;
        }

        @Override
        protected void onPostExecute(String result) {

            DataParser dataParser = new DataParser();

            switch (flag) {

                case 1:

                    List<HashMap<String, String>> nearbyPlacesList = null;
                    nearbyPlacesList = dataParser.parse(result);
                    ShowNearbyPlaces(nearbyPlacesList);

                    alertDialog.dismiss();
                    break;

                case 2:

                    HashMap<String, String> directionsList = null;

                    directionsList = dataParser.parseDirections(result);
                    pathsList = dataParser.parseDirections2(result);

                    duration = directionsList.get("duration");
                    distance = directionsList.get("distance");

                    distance_textview.setText("Distance: " + distance);
                    duration_textview.setText("Duration: " + duration);
                    alertDialog.dismiss();
                    break;

                case 3:

                    HashMap<String, String> photo_data = dataParser.getPhoto_Data(result);

                    Log.e("PLACE_ID", photo_data.get("place_id"));
                    Log.e("PHOTO_REF", photo_data.get("place_id"));
                    Log.e("WIDTH", photo_data.get("place_id"));

//                    getPhotos(photo_data.get("place_id"),photo_data.get("photo_ref"),photo_data.get("width"));
                    alertDialog.dismiss();
                    break;

            }
        }


        private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
            for (int i = 0; i < nearbyPlacesList.size(); i++) {
                Log.d("onPostExecute", "Entered into showing locations");
                final MarkerOptions markerOptions = new MarkerOptions();
                HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
                final double lat = Double.parseDouble(googlePlace.get("lat"));
                final double lng = Double.parseDouble(googlePlace.get("lng"));
                String placeName = googlePlace.get("place_name");
                String vicinity = googlePlace.get("vicinity");
                final LatLng latLng = new LatLng(lat, lng);
                markerOptions.position(latLng);
                markerOptions.title(placeName + " : " + vicinity);
                markerOptions.icon(icon);
                mMap.addMarker(markerOptions);
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//            move map camera
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            }
        }


//        public void getPhotos(String place_id,String phot_ref,String width) {
//
//                String url1 = "https://maps.googleapis.com/maps/api/place/photo?maxwidth="+width+"&photoreference="+phot_ref+"&key=AIzaSyCG2IpUHIhnV1ZDOEyr2YLUHdA53KeX9gE";
//
//            URL url = null;
//            try {
//                url = new URL(url1);
//            } catch (MalformedURLException e) {
//                e.printStackTrace();
//            }
//            Bitmap bmp = null;
//            try {
//                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            place_image.setImageBitmap(bmp);
//
//        }


    }

    //========================
    @Override
    public void onLocationChanged(Location location) {
        Log.d("onLocationChanged", "entered");
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng ll = new LatLng(latitude, longitude);
        mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    public void GetVendors(String mURL) {
        alertDialog.show();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d("Vendor_Data", response.toString());
                                alertDialog.dismiss();
                                if (response.toString() != null) {
                                    ReadJson(response);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        HashMap<String, String> headers = new HashMap<>();
                        return super.getHeaders();
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        return params;
                    }
                };
        addToRequestQueue(jsonObjectRequest);
    }

    private void ReadJson(JSONObject jsonObject) {
        List<HashMap<String, String>> data = new ArrayList<>();
        try {
            JSONArray array = jsonObject.getJSONArray("Result");
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                JSONObject vendor_details = object.getJSONObject("vendor_details");
                int Vendor_id = vendor_details.getInt("id");
                String Vendor_name = vendor_details.getString("vendor_name");
                String Contact_Person = vendor_details.getString("contact_person");
                String City = vendor_details.getString("city");
                String image_path = vendor_details.getString("image");
                String vicinity = vendor_details.getString("locality");
                String Address = vendor_details.getString("Address");
                JSONArray retting = object.getJSONArray("rettting");
                String rating = retting.getJSONObject(0).getString("retting");
                Float double_rating = 0f;
                if (!(rating.equals("null"))) {
                    double_rating = Float.valueOf(rating);
                }
                LatLng vendor_latlng = getLatLngFromAddress(Address);

                HashMap<String, String> hashMap = new HashMap<>();

                hashMap.put("lat", String.valueOf(vendor_latlng.latitude));
                hashMap.put("lng", String.valueOf(vendor_latlng.longitude));
                hashMap.put("place_name", Vendor_name);
                hashMap.put("vicinity", vicinity);

                data.add(hashMap);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        ShowNearbyPlaces(data, BitmapDescriptorFactory.fromResource(R.drawable.car_repair_marker));
    }

    public void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(MapActivity.this);
        return requestQueue;
    }


    public LatLng getLatLngFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);


            p1 = new LatLng(location.getLatitude(), location.getLongitude());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return p1;
    }

    public String getPostalfromLatLng(double Latitude, double Longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(Latitude, Longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

//        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//        String city = addresses.get(0).getLocality();
//        String state = addresses.get(0).getAdminArea();
//        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
//        String knownName = addresses.get(0).getFeatureName(); // Only

        return postalCode;
    }

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList, BitmapDescriptor icon) {
        float nearest_vendor = 5000;
        Marker near = null;
        for (int i = 0; i < nearbyPlacesList.size(); i++) {
            Log.d("onPostExecute", "Entered into showing locations");
            final MarkerOptions markerOptions = new MarkerOptions();
            Marker marker;
            HashMap<String, String> googlePlace = nearbyPlacesList.get(i);
            final double lat = Double.parseDouble(googlePlace.get("lat"));
            final double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            final LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            markerOptions.icon(icon);
            marker = mgoogleMap.addMarker(markerOptions);
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//            move map camera
            mgoogleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mgoogleMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            float[] results = new float[1];
            Location.distanceBetween(latitude, longitude, lat, lng, results);
            if (results[0] < nearest_vendor) {
                nearest_vendor = results[0];
                near = marker;
            }
        }
        if (near != null)
            getNearbyVendor(near);
    }

    public void getNearbyVendor(Marker marker) {
        alertDialog.show();

        if (polylineArrayList != null) {

            for (Polyline line : polylineArrayList) {
                line.remove();
            }
            polylineArrayList.clear();
        }

        markerlatlang = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        Log.e("ZOOM_LEVEL", String.valueOf(mgoogleMap.getCameraPosition().zoom));

        isopened = true;

        GetMapsData getDirectionsData = new GetMapsData(MapActivity.this);
        Object[] DataTransfer = new Object[3];
        String url = getDirectionsUrl(latitude, longitude, marker.getPosition().latitude, marker.getPosition().longitude);
        DataTransfer[0] = mgoogleMap;
        DataTransfer[1] = url;
        DataTransfer[2] = 2;
        getDirectionsData.execute(DataTransfer);
        bottom_sheet.setVisibility(View.VISIBLE);
        marker_name.setText(marker.getTitle());
        Toast.makeText(MapActivity.this, "Nearest Vendor to you  " + marker.getTitle(), Toast.LENGTH_LONG).show();
        if (isopened) {
            Animation animation = new TranslateAnimation(0.0f, 0.0f, 0.0f, -120);
//            ObjectAnimator oa = ObjectAnimator.ofFloat(side_layout, "xFraction", 0, 0.25f);
            animation.setFillAfter(true);
            animation.setDuration(100);
            side_layout.setAnimation(animation);
        }
        flag = false;

    }

    @Override
    protected void onResume() {
        super.onResume();

        LocationManager lm = (LocationManager) MapActivity.this.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }

        if (!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(MapActivity.this);
            dialog.setMessage("GPS Location disabled!");
            dialog.setPositiveButton("Enable Location Services!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    MapActivity.this.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }
}
