package com.motomistry.motomistry.Adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.motomistry.motomistry.Model.ImageModel;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Amit on 2/02/2018.
 */
public class SlidingImage_Adapter extends PagerAdapter {
    private ArrayList<ImageModel> imageModelArrayList;
    private LayoutInflater inflater;
    private Context context;
    public SlidingImage_Adapter(Context context, ArrayList<ImageModel> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
        try {
            inflater = LayoutInflater.from(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }
    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.slidingimages_layout, view, false);
        assert imageLayout != null;
        final ImageView imageView = (ImageView) imageLayout
                .findViewById(R.id.image);
       // imageView.setImageResource(imageModelArrayList.get(position).getImage_drawable());
        Picasso.get().load(Constant.Base_url+imageModelArrayList.get(position).getImage_drawable()).into(imageView);
        view.addView(imageLayout, 0);
        return imageLayout;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }
    @Override
    public Parcelable saveState() {
        return null;
    }
}