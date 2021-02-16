package com.motomistry.motomistry.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.Accessory_item_model;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Accessory extends Fragment{

    private View view;
    ImageView image_0,image_1,image_2,image_3,product_image;
    TextView accessory_name,accessory_price,accessory_desc,shop_count,add_to_cart_text;
    ImageView my_cart;
    LinearLayout product_details,add_to_cart,buy_now;
    RelativeLayout more_images;
    Accessory_item_model data;
    Session session;
    private CustomDialog progressDialog;
    ImageView toolbar_back;
    boolean accessory_already_incart = false;
    int count=0;
    public Accessory() {
    }


    @SuppressLint("ValidFragment")
    public Accessory(Accessory_item_model accessory_item_model) {
        this.data = accessory_item_model;


    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_accessory,container,false);

        toolbar_back = (ImageView) view.findViewById(R.id.toolbar_back);

        toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity)getActivity()).bacPOP();
            }
        });

//        product_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        session = new Session(getActivity());
        findView();
        setData();

        if(My_Cart.data.size() == 0){
            shop_count.setVisibility(View.GONE);
        }
        else {
            shop_count.setText("+"+My_Cart.data.size());
        }

        JSONArray jsonArray = session.getCartData();
        for(int i=0;i<jsonArray.length();i++){
            try {
                if(Integer.parseInt(jsonArray.getJSONObject(i).getString("product_id")) == data.getAccessory_id()){
                    accessory_already_incart = true;
                    add_to_cart_text.setText("Go To Cart");
                    break;
                }
                else accessory_already_incart = false;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        product_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Accessory_Images accessory_images = new Accessory_Images();
                Bundle bundle = new Bundle();
                bundle.putStringArray("images_data",data.getImages());
                accessory_images.setArguments(bundle);
                ((DashbordActivity)getActivity()).replace_Fragment(accessory_images);
            }
        });

//        more_images.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Accessory_Images accessory_images = new Accessory_Images();
//                Bundle bundle = new Bundle();
//                String[] images = data.getImages();
//                bundle.putStringArray("images_data",data.getImages());
//                accessory_images.setArguments(bundle);
//                ((DashbordActivity)getActivity()).replace_Fragment(accessory_images);
//            }
//        });
        image_0.setBackground(getResources().getDrawable(R.drawable.product_image_selected));
        image_1.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
        image_2.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
        image_3.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));

        image_0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(BaseClass.BaseUrl+data.getImages()[0]).placeholder(R.drawable.dummy_accessory).into(product_image);
                image_0.setBackground(getResources().getDrawable(R.drawable.product_image_selected));
                image_1.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
                image_2.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
                image_3.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));

            }
        });

        image_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(BaseClass.BaseUrl+data.getImages()[1]).placeholder(R.drawable.dummy_accessory).into(product_image);
                image_1.setBackground(getResources().getDrawable(R.drawable.product_image_selected));
                image_0.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
                image_2.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
                image_3.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));

            }
        });

        image_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(BaseClass.BaseUrl+data.getImages()[2]).placeholder(R.drawable.dummy_accessory).into(product_image);
                image_2.setBackground(getResources().getDrawable(R.drawable.product_image_selected));
                image_0.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
                image_1.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
                image_3.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
            }
        });

        image_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Picasso.get().load(BaseClass.BaseUrl+data.getImages()[3]).placeholder(R.drawable.dummy_accessory).into(product_image);
                image_3.setBackground(getResources().getDrawable(R.drawable.product_image_selected));
                image_0.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
                image_1.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
                image_2.setBackground(getResources().getDrawable(R.drawable.product_image_unselected));
            }
        });

        my_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constant.isFromFrequent)
                    Constant.clickedFromHome = true;
                else
                Constant.clickedFromHome = false;

                ((DashbordActivity)getActivity()).replace_Fragment(new My_Cart());
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray jsonArray = session.getCartData();
                for(int i=0;i<jsonArray.length();i++){
                    try {
                        if(Integer.parseInt(jsonArray.getJSONObject(i).getString("product_id")) == data.getAccessory_id()){
                            accessory_already_incart = true;
                            add_to_cart_text.setText("Go To Cart");
                            break;
                        }
                        else accessory_already_incart = false;
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                AnimationSet animationSet = new AnimationSet(true);
                Animation animation1 = new ScaleAnimation(1.0f,1.1f,1.0f,1.1f);
                animation1.setDuration(200);
                animationSet.addAnimation(animation1);
                Animation animation2 = new ScaleAnimation(1.1f,1.0f,1.1f,1.0f);
                animation2.setStartOffset(200);
                animation2.setDuration(100);
                animationSet.addAnimation(animation2);
                shop_count.setVisibility(View.VISIBLE);
                shop_count.setAnimation(animationSet);
                add_to_cart_text.setText("Go To Cart");
                count++;
                if(count>=2 || accessory_already_incart){
                    if(Constant.isFromFrequent)
                        Constant.clickedFromHome = true;
                    else
                        Constant.clickedFromHome = false;

                    ((DashbordActivity)getActivity()).replace_Fragment(new My_Cart());
                }
                else{ My_Cart.data.add(data);
                    if(session.getCartData().length()==0) {
                        session.setCartData(My_Cart.createJSON());
                    }
                    else{
                        JSONObject jsonObject = new JSONObject();
                        try {
                            if(data.getImages().length > 0) {
                                jsonObject.put("cart_image", data.getImages()[0]);
                            }
                            else
                                jsonObject.put("cart_image", "");

                            jsonObject.put("accessory_name",data.getAccessory_name());
                            jsonObject.put("vendor_name",data.getVendor_name());
                            jsonObject.put("price",data.getPrice());
                            jsonObject.put("product_id",data.getAccessory_id());
                            jsonObject.put("vendor_id",data.getVendor_id());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        session.updateCartData(jsonObject);
                    }
                    shop_count.setText("+ "+session.getCartData().length());
                    accessory_already_incart = true;}
            }
        });

        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!accessory_already_incart) {
                    My_Cart.data.add(data);
                }
                ((DashbordActivity)getActivity()).replace_Fragment(new My_Cart());
            }
        });

        return view;
    }

    private void findView(){
        image_0 = (ImageView) view.findViewById(R.id.image_0);
        image_1 = (ImageView) view.findViewById(R.id.image_1);
        image_2 = (ImageView) view.findViewById(R.id.image_2);
        image_3 = (ImageView) view.findViewById(R.id.image_3);
        product_image = (ImageView) view.findViewById(R.id.product_image);
//        more_images = (RelativeLayout) view.findViewById(R.id.more_images);
        add_to_cart = (LinearLayout) view.findViewById(R.id.add_to_cart);
        add_to_cart_text = (TextView) view.findViewById(R.id.add_to_cart_text);
        buy_now = (LinearLayout) view.findViewById(R.id.buy_now);
        shop_count = (TextView) view.findViewById(R.id.shop_count);

        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);

//        product_details = (LinearLayout) view.findViewById(R.id.product_details);
        accessory_name = (TextView) view.findViewById(R.id.accessory_name);
        accessory_price = (TextView) view.findViewById(R.id.accessory_price);
        accessory_desc = (TextView) view.findViewById(R.id.accessory_desc);
        my_cart = (ImageView) view.findViewById(R.id.my_cart_float);
    }


    private void setData(){

        Log.e("DATA",data.toString());
        int flag = 0;
        if(data!=null){
            int image_length = data.getImages().length;
            progressDialog.show();
            switch (image_length){
                case 4:
                    Picasso.get().load(BaseClass.BaseUrl+data.getImages()[3]).placeholder(R.drawable.dummy_accessory).into(image_3);
                    flag=4;
                case 3:
                    Picasso.get().load(BaseClass.BaseUrl+data.getImages()[2]).placeholder(R.drawable.dummy_accessory).into(image_2);
                    if(!(flag==4))
                    image_3.setVisibility(View.GONE);
                    flag=3;
                case 2:
                    Picasso.get().load(BaseClass.BaseUrl + data.getImages()[1]).placeholder(R.drawable.dummy_accessory).into(image_1);
                    if(!(flag==3)) {
                        image_2.setVisibility(View.GONE);
                        image_3.setVisibility(View.GONE);
                    }
                    flag=2;
                case 1:
                    Log.e("Accessory_Image",data.getImages()[0]);
                    Picasso.get().load(BaseClass.BaseUrl + data.getImages()[0]).placeholder(R.drawable.no_image_available).into(product_image);
                    Picasso.get().load(BaseClass.BaseUrl + data.getImages()[0]).placeholder(R.drawable.no_image_available).into(image_0);
                    if(!(flag==2)) {
                        image_1.setVisibility(View.GONE);
                        image_2.setVisibility(View.GONE);
                        image_3.setVisibility(View.GONE);
                    }
                    flag=0;
                    break;

                default:
                    image_1.setVisibility(View.GONE);
                    image_2.setVisibility(View.GONE);
                    image_3.setVisibility(View.GONE);
                    image_0.setVisibility(View.GONE);
                    product_image.setImageResource(R.drawable.no_image_available);
//                    more_images.setVisibility(View.GONE);
                    break;
            }

            accessory_name.setText(data.getAccessory_name());
            accessory_price.setText(getString(R.string.Rs)+" "+data.getPrice());
            accessory_desc.setText(data.getDescription());
        }
        progressDialog.dismiss();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.accessory_main));

    }
}
