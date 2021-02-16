package com.motomistry.motomistry.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.squareup.picasso.Picasso;

public class Accessory_Images extends Fragment{

    private View view;
    ViewPager product_images_slider;
    RecyclerView bottom_slider_list;
    FrameLayout bottom_panel;
    boolean flag_open = true;
    String[] image_data;
    Animation bottom_up,bottom_down,scale;
    private ScaleGestureDetector scaleGestureDetector;
    private float ScaleFactor = 1.0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        view = inflater.inflate(R.layout.fragment_accessory__images,container,false);
        product_images_slider = (ViewPager) view.findViewById(R.id.product_images_pager);
        bottom_slider_list = (RecyclerView) view.findViewById(R.id.bottom_slider_list);
        bottom_panel = (FrameLayout) view.findViewById(R.id.bottom_panel);

        bottom_up = AnimationUtils.loadAnimation(getActivity(),R.anim.bottom_up);
        bottom_down = AnimationUtils.loadAnimation(getActivity(),R.anim.bottom_down);
        scale = AnimationUtils.loadAnimation(getActivity(),R.anim.scale_up);

        if(getArguments()!=null){
            image_data = getArguments().getStringArray("images_data");
        }


        product_images_slider.setAdapter(new CustomPagerAdapter(getActivity(),image_data));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),LinearLayout.HORIZONTAL,false);
        bottom_slider_list.setLayoutManager(linearLayoutManager);

        bottom_slider_list.setAdapter(new Bottom_Panel_Adapter(image_data));

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                scaleGestureDetector.onTouchEvent(event);

                return true;
            }
        });

        return view;
    }


    private class Bottom_Panel_Adapter extends RecyclerView.Adapter<Bottom_Panel_Adapter.CustomView_Holder>{

        String[] list;

        Bottom_Panel_Adapter(String[] list) {
            this.list = list;
        }

        @NonNull
        @Override
        public CustomView_Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View categories_view = LayoutInflater.from(getActivity()).inflate(R.layout.bottom_slider_item,parent,false);
            return new CustomView_Holder(categories_view);
        }

        @Override
        public void onBindViewHolder(@NonNull final CustomView_Holder holder, final int position) {

            Picasso.get().load(BaseClass.BaseUrl + list[position]).placeholder(R.drawable.no_image_available).into(holder.image);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    holder.itemView.clearAnimation();
                    holder.itemView.startAnimation(scale);
                    holder.image.setBackground(getResources().getDrawable(R.drawable.corner_rectangle_border));
                    bottom_slider_list.scrollToPosition(position);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.length;
        }

        class CustomView_Holder extends RecyclerView.ViewHolder{

            ImageView image;

            CustomView_Holder(View view){
                super(view);
                image = view.findViewById(R.id.bottom_pager_image);
            }

        }


    }

    public class CustomPagerAdapter extends PagerAdapter {

        private String images[];
        private Context context;

         CustomPagerAdapter(Context context,String images[]) {

            this.images = images;
            this.context = context;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((FrameLayout)object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {

            View inflate_view;

                    inflate_view = LayoutInflater.from(context).inflate(R.layout.accessory_image_item, container, false);
                    ImageView imageView = (ImageView) inflate_view.findViewById(R.id.pager_image);
                    Picasso.get().load(BaseClass.BaseUrl+images[position]).placeholder(R.drawable.dummy_accessory).into(imageView);
                    scaleGestureDetector = new ScaleGestureDetector(getActivity(), new ScaleListener(imageView));

                    inflate_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Log.e("FLAG",String.valueOf(flag_open));

                            if(flag_open) {
                                bottom_panel.startAnimation(bottom_up);
                                bottom_panel.setVisibility(View.GONE);
                                flag_open = false;

                                Log.e("FLAG1",String.valueOf(flag_open));
                            }
                            else {

                                Log.e("FLAG2",String.valueOf(flag_open));
                                bottom_panel.startAnimation(bottom_down);
                                bottom_panel.setVisibility(View.VISIBLE);
                                flag_open = true;
                            }

                        }
                    });

            container.addView(inflate_view);

            return inflate_view;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((FrameLayout)object);
        }
    }




    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener{

        ImageView imageView;

        public ScaleListener(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            ScaleFactor *= scaleGestureDetector.getScaleFactor();
            ScaleFactor = Math.max(0.1f,
                    Math.min(ScaleFactor, 10.0f));
            imageView.setScaleX(ScaleFactor);
            imageView.setScaleY(ScaleFactor);
            return true;
        }


    }




}
