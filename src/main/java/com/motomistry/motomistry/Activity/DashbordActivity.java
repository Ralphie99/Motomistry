package com.motomistry.motomistry.Activity;


import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.transition.TransitionInflater;
import android.transition.TransitionSet;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Fragment.Home;
import com.motomistry.motomistry.Fragment.Profile;
import com.motomistry.motomistry.Fragment.Shop;
import com.motomistry.motomistry.New.Fragment.DrivingSchoolHistory;
import com.motomistry.motomistry.New.Fragment.FragmentBookingServiceHistry;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.R;


public class DashbordActivity extends AppCompatActivity implements View.OnClickListener {
    ImageView home, shop, progress, profile, near_location;
    private ConnectionManager connectionManager;
    private boolean double_back = false;
    static CustomDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findView();
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

//        FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "font/Montserrat_Regular.ttf");
//        fontChanger.replaceFonts((ViewGroup)this.findViewById(android.R.id.content));
    }

    private void findView() {

        this.progressDialog = new CustomDialog(this);
        this.progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        this.progressDialog.setCanceledOnTouchOutside(false);

        home = (ImageView) findViewById(R.id.home);
        shop = (ImageView) findViewById(R.id.shop);
        progress = (ImageView) findViewById(R.id.progress);
        profile = (ImageView) findViewById(R.id.profile);
        near_location = (ImageView) findViewById(R.id.near_location);
        profile.setOnClickListener(this);
        home.setOnClickListener(this);
        shop.setOnClickListener(this);
        near_location.setOnClickListener(this);
        progress.setOnClickListener(this);
        connectionManager = new ConnectionManager(this);
        if (!connectionManager.isConnected()) {
            final Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No Internet Connection!", Snackbar.LENGTH_SHORT);
            snackbar.setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    snackbar.dismiss();
                }
            });
            snackbar.show();

        }
        String menuFragment = getIntent().getStringExtra("menuFragment");
        if (menuFragment != null) {
            NotificationOnClick(menuFragment);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_view2, new Home()).commit();
        }
    }

    private void NotificationOnClick(String menuFragment) {
        switch (menuFragment) {
            case "DRIVING_HISTORY":
                getSupportFragmentManager().beginTransaction().replace(R.id.container_view2, new DrivingSchoolHistory()).commit();
                break;
            case "bookingHistory":
                getSupportFragmentManager().beginTransaction().replace(R.id.container_view2, new FragmentBookingServiceHistry()).commit();
                break;
            default:
                getSupportFragmentManager().beginTransaction().replace(R.id.container_view2, new Home()).commit();
                break;

        }
    }

//    public void replace_Fragment(Fragment fragment, int enterAnimation, int exitAnimation) {
//        FragmentManager manager = getSupportFragmentManager();
//        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
//        transaction.replace(R.id.container_view, fragment, null);
//        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }

    public void replace_Fragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container_view, fragment, null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
        transaction.addToBackStack("Fragment");
    }

    public void replace_Fragment_filter(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.filter_list_container, fragment, null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
        transaction.addToBackStack("Fragment");
    }

    public void replace_Fragment2(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container_view2, fragment, null);
        transaction.addToBackStack("Fragment");
        transaction.commit();
    }

    public void bacPOP() {
        super.onBackPressed();
    }

    public void showDialog(boolean flag){
        if(flag)
            progressDialog.show();
        else
            progressDialog.dismiss();
    }

    public void replace_Fragment(Fragment fragment, View sharedElement, String transitionName) {

        FragmentManager manager = getSupportFragmentManager();

        TransitionSet enterTransitionSet = new TransitionSet();
        enterTransitionSet.addTransition(TransitionInflater.from(this).inflateTransition(android.R.transition.move));
        fragment.setSharedElementEnterTransition(enterTransitionSet);

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.addSharedElement(sharedElement, transitionName);
        transaction.replace(R.id.container_view, fragment, null);

        transaction.addToBackStack("Fragment");
        transaction.commit();

    }

    public void selectIcon(int view) {

        switch (view) {

            case 1:
                home.setColorFilter(getResources().getColor(R.color.colorAccent));
                home.startAnimation(AnimationUtils.loadAnimation(DashbordActivity.this, R.anim.scale_up));
                shop.setColorFilter(getResources().getColor(R.color.White));
                shop.clearAnimation();
                profile.setColorFilter(getResources().getColor(R.color.White));
                profile.clearAnimation();
                progress.setColorFilter(getResources().getColor(R.color.White));
                progress.clearAnimation();

                break;

            case 2:
                home.setColorFilter(getResources().getColor(R.color.White));
                home.clearAnimation();
                shop.setColorFilter(getResources().getColor(R.color.colorAccent));
                shop.startAnimation(AnimationUtils.loadAnimation(DashbordActivity.this, R.anim.scale_up));
                profile.setColorFilter(getResources().getColor(R.color.White));
                profile.clearAnimation();
                progress.setColorFilter(getResources().getColor(R.color.White));
                progress.clearAnimation();
                break;

            case 3:
                home.setColorFilter(getResources().getColor(R.color.White));
                home.clearAnimation();
                shop.setColorFilter(getResources().getColor(R.color.White));
                shop.clearAnimation();
                profile.setColorFilter(getResources().getColor(R.color.White));
                profile.clearAnimation();
                progress.setColorFilter(getResources().getColor(R.color.colorAccent));
                progress.startAnimation(AnimationUtils.loadAnimation(DashbordActivity.this, R.anim.scale_up));
                break;


            case 4:
                home.setColorFilter(getResources().getColor(R.color.White));
                home.clearAnimation();
                shop.setColorFilter(getResources().getColor(R.color.White));
                shop.clearAnimation();
                profile.setColorFilter(getResources().getColor(R.color.colorAccent));
                profile.startAnimation(AnimationUtils.loadAnimation(DashbordActivity.this, R.anim.scale_up));
                progress.setColorFilter(getResources().getColor(R.color.White));
                progress.clearAnimation();
                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View v) {
        if (v == profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_view2, new Profile()).commit();
        } else if (v == home) {
//            Slide slide = new Slide(Gravity.BOTTOM);
//            slide.setDuration(500);
//            Home home = new Home();
//            home.setEnterTransition(slide);
//            getSupportFragmentManager().beginTransaction().replace(R.id.container_view2, home).commit();
            startActivity(new Intent(this,DashbordActivity.class));
            this.finish();
        } else if (v == shop) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_view2, new Shop()).commit();
        } else if (v == progress) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container_view2, new FragmentBookingServiceHistry()).commit();
        } else if (v == near_location) {
            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(DashbordActivity.this);
            Intent i = new Intent(DashbordActivity.this, MapActivity.class);
            startActivity(i, options.toBundle());
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.add_contact:
//                replace_Fragment(new FragmentAddContact());
//                break;
//                case R.id.track_contact:
//                    replace_Fragment(new FragmentTrackContact());
//                    break;
//            case android.R.id.home:
//                super.onBackPressed();
//                if (getFragmentManager().getBackStackEntryCount()==0){
//                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//                    getSupportActionBar().setTitle("Dashboard");
//                }
//                break;
//
//            // case blocks for other MenuItems (if any)
//        }
//        return false;
//    }

    @Override
    public void onBackPressed() {
        Log.e("Stack_Count",String.valueOf(getSupportFragmentManager().getBackStackEntryCount()));
        if ((getSupportFragmentManager().getBackStackEntryCount() >= 1)) {
            bacPOP();
        }
        else{
            if(double_back){
                System.exit(0);
            }
            else{
                double_back = true;
                Toast.makeText(this,"Press back once again to exit!",Toast.LENGTH_LONG).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        double_back = false;
                    }
                },2000);
            }

        }
    }


}
