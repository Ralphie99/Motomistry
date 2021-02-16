package com.motomistry.motomistry.Fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.RegistrationActivity;


public class AddVehicle_fromOutside extends AppCompatActivity {
    RelativeLayout parent_layout;
    LinearLayout layout_reveal, two_wheeler_block, four_wheeler_block, or_separator;
    private boolean isOpen = false;
    RelativeLayout two_wheeler, four_wheeler;
    TextView question_tag, two_wheeler_text, four_wheeler_text;
    AnimationSet animationSet;
    FrameLayout frameLayout;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_vehicle_from_outside);
        frameLayout = (FrameLayout) findViewById(R.id.container_view_add);
        parent_layout = (RelativeLayout) findViewById(R.id.parent);
        layout_reveal = (LinearLayout) findViewById(R.id.reveal_layout);
        two_wheeler = (RelativeLayout) findViewById(R.id.two_wheeler);
        four_wheeler = (RelativeLayout) findViewById(R.id.four_wheeler);
        four_wheeler_block = (LinearLayout) findViewById(R.id.four_wheeler_block);
        two_wheeler_block = (LinearLayout) findViewById(R.id.two_wheeler_block);
        or_separator = (LinearLayout) findViewById(R.id.or_separator);
        two_wheeler_text = (TextView) findViewById(R.id.two_wheeler_text);
        four_wheeler_text = (TextView) findViewById(R.id.four_wheeler_text);
        question_tag = (TextView) findViewById(R.id.question_tag);
            getWindow().setAllowEnterTransitionOverlap(true);
        two_wheeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.IsTowVechile = true;
                Constant.IsFourVechile = false;
                go_Vehicle(two_wheeler_block, two_wheeler_text, or_separator, question_tag, four_wheeler_block, -700);
                Constant.fromOutside = true;
                getManufacturer manufacturerFragment = new getManufacturer();
                Slide slide2 = new Slide(Gravity.LEFT);
                slide2.setDuration(900);
                manufacturerFragment.setExitTransition(slide2);
                replace_Fragment(manufacturerFragment, R.anim.slide_up, R.anim.slide_down);
            }
        });
        four_wheeler.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constant.IsFourVechile = true;
                Constant.IsTowVechile = false;
                go_Vehicle(four_wheeler_block, four_wheeler_text, or_separator, question_tag, two_wheeler_block, -1000);
                Constant.fromOutside = true;
                getManufacturer manufacturerFragment = new getManufacturer();
                Slide slide2 = new Slide(Gravity.LEFT);
                slide2.setDuration(900);
                manufacturerFragment.setExitTransition(slide2);
                replace_Fragment(manufacturerFragment, R.anim.slide_up, R.anim.slide_down);
            }
        });
    }
    private void go_Vehicle(final LinearLayout main, final TextView main_text, final LinearLayout separator, final TextView question, final LinearLayout other, int up) {

        animationSet = new AnimationSet(true);
        animationSet.setFillAfter(true);

        TranslateAnimation translate_up = new TranslateAnimation(0, 0, 0, up);
        translate_up.setStartOffset(500);
        translate_up.setDuration(500);
        translate_up.setInterpolator(new AccelerateInterpolator());
        animationSet.addAnimation(translate_up);

        TranslateAnimation translate_down = new TranslateAnimation(0, 0, 0, 50);
        translate_down.setStartOffset(200);
        translate_down.setDuration(300);
        animationSet.addAnimation(translate_down);

        ScaleAnimation scale = new ScaleAnimation(1.0f, 1.1f, 1.0f, 1.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale.setDuration(100);
        animationSet.addAnimation(scale);

        ScaleAnimation scale1 = new ScaleAnimation(1.1f, 1.0f, 1.1f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scale1.setStartOffset(100);
        scale1.setDuration(100);
        animationSet.addAnimation(scale1);

        main.startAnimation(animationSet);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.fade_tonull);
        other.startAnimation(anim);
        separator.startAnimation(anim);
        main_text.startAnimation(anim);
        question.startAnimation(anim);
        animationSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                main.clearAnimation();
                other.clearAnimation();
                separator.clearAnimation();
                main_text.clearAnimation();
                question.clearAnimation();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
    public void replace_Fragment(Fragment fragment, int enterAnimation, int exitAnimation) {

        FragmentManager manager = getSupportFragmentManager();

        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();

        transaction.setCustomAnimations(enterAnimation, exitAnimation);
        transaction.replace(R.id.container_view_add, fragment, null);
        transaction.addToBackStack(null);
        transaction.commit();

    }

    public void replace_Fragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container_view_add, fragment, null);
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.commit();
        transaction.addToBackStack(null);
    }
    public void setBackground_null(){
        frameLayout.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    }



    public void setOne(){
        frameLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }


    @Override
    public void setContentView(View view) {
        super.setContentView(view);

            FontChangeCrawler fontChanger = new FontChangeCrawler(getAssets(), "font/Main.ttf");
            fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.add_vehicle_container));

    }
}



