package com.motomistry.motomistry.Fragment;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.Adapter.SlidingImage_Adapter;
import com.motomistry.motomistry.Fatch.Fatch;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.ImageModel;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.EmailFetcher;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.RegistrationActivity;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserDetailsSenterFragment extends Fragment {
    private View view;
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;
    private int[] myImageList = new int[]{R.drawable.banner1, R.drawable.banner2,
            R.drawable.banner3,R.drawable.banner4
            ,R.drawable.banner5,R.drawable.banner5};
    private View jabalpur,mumbai,delhi,alpha1,alpha2,alpha3;
    private Button sign_in;
    private Animation animation1,animation2;
    private boolean isAnim1,isAnim2,isAnim3=false;
    private int IsCity;
    private EditText email,full_name;
    TextView city_error;

    public UserDetailsSenterFragment() {
        // Required empty public constructor
    }

    public static UserDetailsSenterFragment newInstance() {
        UserDetailsSenterFragment fragment = new UserDetailsSenterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_actions, menu);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view==null){
        view=inflater.inflate(R.layout.userdetailsenter, container, false);
            jabalpur = view.findViewById(R.id.jabalpur);
            findView();
            onClick();
    }return view;}

    private void onClick() {
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Validation()) {
                    new Fatch(getActivity(), "PROFILE_SUBMIT", Constant.getClintRegistrationUrl(Constant.MOBILE_NUMBER,
                            full_name.getText().toString(), email.getText().toString(), IsCity));
                }
            }
        });

        jabalpur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartAnimation(alpha3,alpha1,alpha2,isAnim3);
                isAnim3=!isAnim3;
                isAnim1=false;
                isAnim2=false;
                IsCity=isAnim3?1:0;
                city_error.setVisibility(View.INVISIBLE);
                sign_in.setEnabled(true);
                sign_in.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.colorAccent));
            }
        });

        mumbai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartAnimation(alpha1,alpha2,alpha3,isAnim1);
                isAnim1=!isAnim1;
                isAnim2=false;
                isAnim3=false;
                IsCity=isAnim1?2:0;
                city_error.setVisibility(View.VISIBLE);
                city_error.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.jiggle_animation));
                sign_in.setEnabled(false);
                sign_in.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.GrayDark));
            }
        });


        delhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onStartAnimation(alpha2,alpha1,alpha3,isAnim2);
                isAnim2=!isAnim2;
                isAnim1=false;
                isAnim3=false;
                IsCity=isAnim2?3:0;
                city_error.setVisibility(View.VISIBLE);
                city_error.setAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.jiggle_animation));
                sign_in.setEnabled(false);
                sign_in.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.GrayDark));
            }
        });
    }

    private void findView() {
        city_error = view.findViewById(R.id.city_error);
        mumbai = view.findViewById(R.id.mumbai);
        delhi = view.findViewById(R.id.delhi);
        sign_in = view.findViewById(R.id.btn_signin);
        ImageView imageView = mumbai.findViewById(R.id.city);
        imageView.setImageResource(R.drawable.mumbai);
        TextView city_name = (TextView) mumbai.findViewById(R.id.city_name);
        city_name.setText("MUMBAI");
        alpha1 = mumbai.findViewById(R.id.alpha);

        imageView = delhi.findViewById(R.id.city);
        imageView.setImageResource(R.drawable.delhi);
        city_name = (TextView) delhi.findViewById(R.id.city_name);
        city_name.setText("DELHI");
        alpha2 = delhi.findViewById(R.id.alpha);

        imageView = jabalpur.findViewById(R.id.city);
        imageView.setImageResource(R.drawable.jabalpur);
        city_name = (TextView) jabalpur.findViewById(R.id.city_name);
        city_name.setText("JABALPUR");
        alpha3 = jabalpur.findViewById(R.id.alpha);
        email=(EditText)view.findViewById(R.id.email);
        full_name=(EditText)view.findViewById(R.id.full_name);
    }

    private void onStartAnimation(final View alpha,final View alpha1,final View alpha2,boolean isSelected) {
        if (isSelected){
            animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadein);
        }else {
            animation1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fadeout);
        }
        alpha.startAnimation(animation1);
        alpha1.clearAnimation();
        alpha2.clearAnimation();
        alpha1.setAlpha((float) 0.6);
        alpha2.setAlpha((float) 0.6);
    }
    private boolean Validation(){
        return (full_name.getText().length()>0)?(email.getText().length()>0)?IsCity!=0?true:Tost("Please select cityt first"):Tost("Please Enter email first"):Tost("Please Enter Name first");
    }
    private boolean Tost(String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_SHORT).show();
        return false;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.user_details_main));

    }
}