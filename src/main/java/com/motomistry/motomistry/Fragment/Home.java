package com.motomistry.motomistry.Fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.vision.text.Line;
import com.merhold.extensiblepageindicator.ExtensiblePageIndicator;
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.Adapter.SlidingImage_Adapter;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.ImageModel;
import com.motomistry.motomistry.Model.ServicesModel;
import com.motomistry.motomistry.New.Fragment.FragmentVenderList;
import com.motomistry.motomistry.New.Fragment.Learn_Driving;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.OtherClass.FontAwesomeIcon.FontManager;
import com.motomistry.motomistry.OtherClass.Session;
import com.motomistry.motomistry.OtherClass.ViewPagerCustomDuration;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.target.SimpleTarget;
import com.takusemba.spotlight.target.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class Home extends Fragment {
    String TAG = "Home";
    private View view;
    private LinearLayout insurance_block;

    Toolbar toolbar;

    private RecyclerView services_recycler,bike_services_recycler;
    private RelativeLayout go_shopping;
    private ViewPagerCustomDuration mPager;
    int currentPage = 0;
    Timer timer;
    final long DELAY_MS = 2000;//delay in milliseconds before task is to be executed
    final long PERIOD_MS = 12000;
    ImageView my_cart,toolbar_back;
    private static int NUM_PAGES;
    ConnectionManager connectionManager;
    ArrayList<ServicesModel> data = new ArrayList<ServicesModel>();
    ArrayList<ServicesModel> data2 = new ArrayList<ServicesModel>();
    private RequestQueue requestQueue;
    private ArrayList<ImageModel> imageModelArrayList;
    private ArrayList<String> myImageList = new ArrayList<>();
    private LinearLayout driving_school_block;
    private CustomDialog progressDialog;
    ExtensiblePageIndicator extensiblePageIndicator;
    TextView our_car_services,go_shopping_text;
    Session session;
    ScrollView scrollView;
    int showcase_count = 0;

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        addTargets(view.findViewById(R.id.my_cart),"Our Services","Book them any time you want!");
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ((DashbordActivity) getActivity()).selectIcon(1);

        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, container, false);
            overrideFonts(getActivity(),view);
            session = new Session(getActivity());
            findView();
            init();
            progressDialog.show();
            Fatch();
            my_cart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((DashbordActivity)getActivity()).replace_Fragment(new My_Cart());
                    Constant.clickedFromHome = true;
                }
            });
            toolbar_back.setVisibility(View.INVISIBLE);
        }
        return view;
    }

//    ArrayList<Target> targets = new ArrayList<>();
//
//    private void addTargets(View view,String title,String description){
//        int[] viewLocation = new int[2];
//        view.getLocationOnScreen(viewLocation);
//        float oneX = view.getX();
//        float oneY = view.getY();
//
//        SimpleTarget Target = new SimpleTarget.Builder(getActivity()).setPoint(oneX, oneY)
//                .setShape(new Circle(100f))
//                .setTitle(title)
//                .setDescription(description)
//                .build();
//
//        targets.add(Target);
//        Spotlight.with(getActivity())
//                .setOverlayColor(R.color.colorAccent)
//                .setDuration(1000L)
//                .setAnimation(new DecelerateInterpolator(2f))
//                .setTargets(targets)
//                .setClosedOnTouchedOutside(true)
//                .start();
//    }

    private void Fatch() {
        connectionManager = new ConnectionManager(getActivity());
        if (!connectionManager.isConnected()) {
            View no_internet = view.findViewById(R.id.no_internet);
            no_internet.setVisibility(View.VISIBLE);
            progressDialog.dismiss();
        } else {
            GetResult(BaseClass.getGet_service_according_to_vehicle_type_URL(0),0);
            GetResult(BaseClass.getGet_service_according_to_vehicle_type_URL(1),1);
            GetResultImage(Constant.GET_SLIDER_IMAGE_URL);
        }
    }

    private void findView() {

        our_car_services = (TextView) view.findViewById(R.id.our_car_services);

        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        my_cart = (ImageView) view.findViewById(R.id.my_cart);
        toolbar_back = (ImageView) view.findViewById(R.id.toolbar_back);
        go_shopping_text = (TextView) view.findViewById(R.id.go_shopping_text);
        scrollView = (ScrollView) view.findViewById(R.id.home_scrollView);
        services_recycler = (RecyclerView) view.findViewById(R.id.services_recycler);
        bike_services_recycler = (RecyclerView) view.findViewById(R.id.bike_services_recycler);
        insurance_block = (LinearLayout) view.findViewById(R.id.insurance_block);
        driving_school_block = (LinearLayout) view.findViewById(R.id.driving_school_block);
        go_shopping = (RelativeLayout) view.findViewById(R.id.go_shopping);
        go_shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Shop shop = new Shop();
                Slide slide = new Slide(Gravity.RIGHT);
                slide.setDuration(500);
                shop.setEnterTransition(slide);
                ((DashbordActivity) getActivity()).replace_Fragment2(shop);
            }
        });

        insurance_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DashbordActivity) getActivity()).replace_Fragment(new inusrance());
            }
        });
        driving_school_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((DashbordActivity) getActivity()).replace_Fragment(new Learn_Driving());
            }
        });
    }

    private void createCarServices(JSONArray object,int type) {
        switch (type){
            case 0:
                data.clear();
                break;
            case 1:
                data2.clear();
                break;
        }

        try {
            for (int i = 0; i < object.length(); i++) {
                JSONObject obj = object.getJSONObject(i);
                int id = obj.getInt("ID");
                String name = obj.getString("NAME");
                String DISCREPTION = obj.getString("DISCREPTION");
                ServicesModel servicesModel = new ServicesModel(id, name, DISCREPTION);
                switch (type){
                    case 0:
                        data.add(servicesModel);
                        break;
                    case 1:
                        data2.add(servicesModel);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        switch (type){
            case 0:
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                services_recycler.setLayoutManager(linearLayoutManager);
                services_recycler.setAdapter(new horizontalRecyclerAdapter(data2,1));
                break;
            case 1:
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
                bike_services_recycler.setLayoutManager(linearLayoutManager2);
                bike_services_recycler.setAdapter(new horizontalRecyclerAdapter(data,2));
                break;
        }


    }

    private class horizontalRecyclerAdapter extends RecyclerView.Adapter<horizontalRecyclerAdapter.ServicesViewHolder> {
        ArrayList<ServicesModel> data;
        private Typeface iconFont;
        int flag;
        public horizontalRecyclerAdapter(ArrayList<ServicesModel> data,int flag) {
            this.data = data;
            this.flag = flag;
        }
        @Override
        public ServicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View services_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.services_layout, parent, false);
            ServicesViewHolder servicesViewHolder = new ServicesViewHolder(services_view);
            iconFont = FontManager.getTypeface(getContext(), FontManager.RALEWAY_SEMI_BOLD);
            AnimationSet animationSet = new AnimationSet(true);
            animationSet.addAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.popup_scale_1));
            animationSet.addAnimation(AnimationUtils.loadAnimation(getActivity(),R.anim.popup_scale_2));
            services_view.setAnimation(animationSet);
            return servicesViewHolder;
        }
        @Override
        public void onBindViewHolder(ServicesViewHolder holder, final int position) {
            holder.services_text.setText(data.get(position).getText());
            holder.services_text.setTypeface(iconFont);
            if(position == 1) {
                holder.services_image.setImageResource(R.drawable.car_services_icon);
            }
            else if(position == 2)
                holder.services_image.setImageResource(R.drawable.car_wash_icon);
            else
                holder.services_image.setImageResource(R.drawable.services_center_icon);
            holder.services_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(flag==1)
                    {
                        BaseClass.HOME_VEHICLE_TYPE = 1;
                        BaseClass.Service_ID_Selected = data.get(position).getID();
                    }
                    else
                    {
                        BaseClass.HOME_VEHICLE_TYPE = 0;
                        BaseClass.Service_ID_Selected = data.get(position).getID();
                    }

                    ((DashbordActivity) getActivity()).replace_Fragment(new FragmentVenderList(data.get(position).getID()));
                }
            });
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        public class ServicesViewHolder extends RecyclerView.ViewHolder {

            ImageView services_image;
            TextView services_text;

            public ServicesViewHolder(View itemView) {
                super(itemView);
                services_image = (ImageView) itemView.findViewById(R.id.services_image);
                services_text = (TextView) itemView.findViewById(R.id.services_text);
                services_text.setTypeface(iconFont);
            }
        }
    }

    public void GetResult(String mURL, final int type) {
        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                progressDialog.dismiss();
                                Log.e("Services_URL", response.toString());
                                if (response.toString() != null) {

                                    try {
                                        JSONArray jsonObject = response.getJSONArray("Result");
                                                createCarServices(jsonObject,type);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                               // aleart.Error("");
                                progressDialog.dismiss();
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

//                    @Override
//                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
//                        try {
//                            Cache.Entry cacheEntry = HttpHeaderParser.parseCacheHeaders(response);
//                            if (cacheEntry == null) {
//                                cacheEntry = new Cache.Entry();
//                            }
//                            final long cacheHitButRefreshed = 3 * 60 * 1000; // in 3 minutes cache will be hit, but also refreshed on background
//                            final long cacheExpired = 24 * 60 * 60 * 1000; // in 24 hours this cache entry expires completely
//                            long now = System.currentTimeMillis();
//                            final long softExpire = now + cacheHitButRefreshed;
//                            final long ttl = now + cacheExpired;
//                            cacheEntry.data = response.data;
//                            cacheEntry.softTtl = softExpire;
//                            cacheEntry.ttl = ttl;
//                            String headerValue;
//                            headerValue = response.headers.get("Date");
//                            if (headerValue != null) {
//                                cacheEntry.serverDate = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                            }
//                            headerValue = response.headers.get("Last-Modified");
//                            if (headerValue != null) {
//                                cacheEntry.lastModified = HttpHeaderParser.parseDateAsEpoch(headerValue);
//                            }
//                            cacheEntry.responseHeaders = response.headers;
//                            final String jsonString = new String(response.data,
//                                    HttpHeaderParser.parseCharset(response.headers));
//                            return Response.success(new JSONObject(jsonString), cacheEntry);
//                        } catch (UnsupportedEncodingException e) {
//                            return Response.error(new ParseError(e));
//                        } catch (JSONException e) {
//                            return Response.error(new ParseError(e));
//                        }
//                    }
                };
        addToRequestQueue(jsonObjectRequest);
    }
    public void GetResultImage(String mURL) {
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.d(TAG, response.toString());
                                progressDialog.dismiss();
                                if (response.toString() != null) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = new JSONObject(response.toString());
                                        ReadJsonImageGet(jsonObject);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
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

    public void addToRequestQueue(Request request) {
        getRequestQueue().add(request);
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null)
            requestQueue = Volley.newRequestQueue(getActivity());
        return requestQueue;
    }

    private void init() {

        imageModelArrayList = populateList();

        int images[] = {R.drawable.vector,R.drawable.car_repair1_slider,R.drawable.insurance_slider,R.drawable.driving_slider,R.drawable.doorstep_slider};

        mPager = (ViewPagerCustomDuration) view.findViewById(R.id.home_slider);
        extensiblePageIndicator = (ExtensiblePageIndicator) view.findViewById(R.id.flexibleIndicator);


//        mPager.setClipToPadding(false);
//        if(mPager.getChildCount() == 1){
//            mPager.setPadding(0,0,50,0);
//        }
//        else if(mPager.getChildCount() == images.length){
//            mPager.setPadding(50,0,0,0);
//        }
//        else
//            mPager.setPadding(50,0,50,0);
//
//        mPager.setPageMargin(-10);
//        mPager.setOffscreenPageLimit(3);
//        mPager.setCurrentItem(2,false);

        mPager.setScrollDurationFactor(4.5);

//        NUM_PAGES = imageModelArrayList.size();
        NUM_PAGES = images.length;
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);

            }
        };
        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() { // task to be scheduled

            @Override
            public void run() {
                handler.post(Update);
            }
        }, DELAY_MS, PERIOD_MS);

        mPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()){

                    case MotionEvent.ACTION_MOVE:
                        mPager.setScrollDurationFactor(1);
                        currentPage = mPager.getCurrentItem();
                        mPager.setCurrentItem(currentPage);
                        break;

                    case MotionEvent.ACTION_DOWN:

                        timer.cancel();

                        break;

                    case MotionEvent.ACTION_UP:

                        mPager.setScrollDurationFactor(4.5);

                        timer = new Timer();
                        timer.schedule(new TimerTask() { // task to be scheduled

                            @Override
                            public void run() {
                                handler.post(Update);
                            }
                        }, DELAY_MS, PERIOD_MS);

                        break;


                    default:
                        break;
                }
                return false;
            }
        });

//        mPager.setAdapter(new SlidingImage_Adapter(getActivity(), imageModelArrayList));
        mPager.setAdapter(new pagerAdapter(images));
        mPager.setPageTransformer(true,new ZoomOutSlideTransformer());
        extensiblePageIndicator.initViewPager(mPager);

        progressDialog = new CustomDialog(getActivity());
        progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        progressDialog.setCanceledOnTouchOutside(false);


    }


    private class pagerAdapter extends PagerAdapter {

        int images[];

        public pagerAdapter(int[] images) {
            this.images = images;
        }

        @Override
        public int getCount() {
            return images.length;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view.equals(object);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View imageLayout = LayoutInflater.from(getActivity()).inflate(R.layout.slidingimages_layout,container, false);

            ImageView imageView = (ImageView) imageLayout
                    .findViewById(R.id.image);

            imageView.setImageResource(images[position]);
            container.addView(imageLayout);
            return imageLayout;
        }
    }

    private ArrayList<ImageModel> populateList() {
        ArrayList<ImageModel> list = new ArrayList<>();
        for (int i = 0; i < myImageList.size(); i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList.get(i));
            list.add(imageModel);
        }
        return list;
    }
    private void ReadJsonImageGet(JSONObject jsonObj1) {
            try {
                JSONArray details = jsonObj1.getJSONArray("Image");
                for (int i = 0; i < details.length(); i++) {
                    JSONObject c = details.getJSONObject(i);
                    String pic_url = c.getString("image");
                    myImageList.add(pic_url);
                    myImageList.add(pic_url);
                    myImageList.add(pic_url);
                }
                init();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    public class ZoomOutSlideTransformer implements ViewPager.PageTransformer {

        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            if (position >= -1 || position <= 1) {
                // Modify the default slide transition to shrink the page as well
                final float height = view.getHeight();
                final float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                final float vertMargin = height * (1 - scaleFactor) / 2;
                final float horzMargin = view.getWidth() * (1 - scaleFactor) / 2;

                // Center vertically
                view.setPivotY(0.5f * height);

                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));
            }
        }

    }

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "Montserrat_Regular.ttf"));
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.home_main));

    }


}
