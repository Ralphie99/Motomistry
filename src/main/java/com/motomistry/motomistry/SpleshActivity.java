package com.motomistry.motomistry;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.motomistry.motomistry.OtherClass.Session;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_CONTACTS;

public class SpleshActivity extends AppCompatActivity {
    private Handler handler;

    private static final int PERMISSION_REQUEST_CODE = 200;
    private View view,splash_border;
    private Session session;
    ImageView splash_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splesh);
        handler =new Handler();
        session=new Session(this);

        splash_logo = (ImageView) findViewById(R.id.splash_logo);
        splash_border = findViewById(R.id.splash_border);

        AnimationSet animationSet = new AnimationSet(true);
        Animation animation= AnimationUtils.loadAnimation(this,R.anim.splash_1);
        animation.setDuration(1500);
        animation.setInterpolator(new DecelerateInterpolator());
        animationSet.addAnimation(animation);
        animation = AnimationUtils.loadAnimation(this,R.anim.null_tofade);
        animation.setStartOffset(700);
        animation.setDuration(500);
        animationSet.addAnimation(animation);
        AnimationSet animationSet1 = new AnimationSet(true);
        Animation scale_up_border  = AnimationUtils.loadAnimation(this,R.anim.scale_up_border);
        Animation scale_down_border  = AnimationUtils.loadAnimation(this,R.anim.scale_down_border);
        scale_up_border.setStartOffset(1200);
        scale_up_border.setDuration(800);
        animationSet1.addAnimation(scale_up_border);
        scale_down_border.setStartOffset(2400);
        scale_down_border.setDuration(800);
        animationSet1.addAnimation(scale_down_border);

        splash_border.setAnimation(animationSet1);
        splash_border.setVisibility(View.INVISIBLE);

        splash_logo.setAnimation(animationSet);


        if (checkPermission()) {
            Handlers();
        }else {
            requestPermission();
        }
    }
    private void Handlers(){
        handler.postDelayed(new Runnable() {
            public void run() {
                session.checkLogin();
                finish();

            }
        }, 4000);
    }
    public void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SpleshActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);

        return result == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean phoneAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (phoneAccepted)
                        Handlers();
                    else {
                        requestPermission();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_PHONE_STATE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{READ_PHONE_STATE},
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
        new AlertDialog.Builder(SpleshActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
