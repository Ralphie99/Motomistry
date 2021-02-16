package com.motomistry.motomistry.New.Fragment;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.Activity.MapActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.R;

public class FragmentTrackContact extends Fragment implements OnMapReadyCallback,View.OnClickListener{

    private View view;
    private GoogleMap mgoogleMap;
    private RelativeLayout loc1,loc2,loc3;
    private double latitude1=23.148627,longitude1=79.904745,latitude2=23.259933,longitude2=77.412615,latitude3=22.719569,longitude3=75.857726;

    public FragmentTrackContact() {
        // Required empty public constructor
    }
    public static FragmentTrackContact newInstance(String param1, String param2) {
        FragmentTrackContact fragment = new FragmentTrackContact();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view==null){
            view= inflater.inflate(R.layout.fragment_track_contact, container, false);
            ((DashbordActivity)getActivity()).getSupportActionBar().setTitle("Track Contact");
            ((DashbordActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            loc1=(RelativeLayout)view.findViewById(R.id.loc1);
            loc2=(RelativeLayout)view.findViewById(R.id.loc2);
            loc3=(RelativeLayout)view.findViewById(R.id.loc3);
            loc1.setOnClickListener(this);
            loc2.setOnClickListener(this);
            loc3.setOnClickListener(this);
            initMap();
        }
        return view;
    }
    private void initMap() {
        MapFragment mapFragment = (MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap mMap) {
        mgoogleMap=mMap;
        LatLng location = new LatLng(21, 57);
        mgoogleMap.addMarker(new
                MarkerOptions().position(location).title("USer name"));
        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLng(location));


    }

    @Override
    public void onClick(View v) {
        if (v==loc1){
            mgoogleMap.clear();
            LatLng ll = new LatLng(latitude1, longitude1);
            mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
            mgoogleMap.addMarker(new
                    MarkerOptions().position(ll).title("USer name"));
        }else if (v==loc2){
            mgoogleMap.clear();
            LatLng ll = new LatLng(latitude2, longitude2);
            mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
            mgoogleMap.addMarker(new
                    MarkerOptions().position(ll).title("USer name"));
        }else if (v==loc3){
            mgoogleMap.clear();
            LatLng ll = new LatLng(latitude3, longitude3);
            mgoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 15));
            mgoogleMap.addMarker(new
                    MarkerOptions().position(ll).title("USer name"));
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.parent));

    }
}
