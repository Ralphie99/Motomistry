package com.motomistry.motomistry.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.transition.Slide;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.motomistry.motomistry.Activity.DashbordActivity;
import com.motomistry.motomistry.FontChangeCrawler;
import com.motomistry.motomistry.Model.Accessory_item_model;
import com.motomistry.motomistry.Model.Model_Shop_Acessory;
import com.motomistry.motomistry.OtherClass.ConnectionManager;
import com.motomistry.motomistry.OtherClass.Constant;
import com.motomistry.motomistry.OtherClass.CustomDialog;
import com.motomistry.motomistry.R;
import com.motomistry.motomistry.Utility.BaseClass;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class accessories_items_list extends Fragment {

    private View view;
    GridView category_data_list;
    static CustomDialog progressDialog;
    private RequestQueue requestQueue;
    ArrayList<Accessory_item_model> data;
    ArrayList<Accessory_item_model> data_price_sorted;
    LinearLayout sort, change_view;
    int Category_ID;
    ImageView view_icon;
    int count_clicks_view = 0;
    int count_clicks_sort = 0;
    ConnectionManager connectionManager;
    LinearLayout sort_layout;
    String Category_name;
    TextView header_text;
    ImageView back_button, sort_image, filter;
    ImageView find_fab;
    private int mLastFirstVisibleItem;
    private boolean mIsScrollingUp;
    TextView no_item_found;

    public accessories_items_list() {
    }

    @SuppressLint("ValidFragment")
    public accessories_items_list(String text) {
        Category_name = text;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_accessories_items_list, container, false);
        init(view);

        header_text.setText(Category_name);
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((DashbordActivity) getActivity()).bacPOP();
            }
        });
        data = new ArrayList<Accessory_item_model>();

        data_price_sorted = new ArrayList<Accessory_item_model>();

        if (getArguments() != null) {
            Category_ID = getArguments().getInt("Category_ID");
            Log.e("Category_ID", String.valueOf(Category_ID));
        }


        category_data_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                ((DashbordActivity) getActivity()).replace_Fragment(new Accessory(data.get(i)));
            }
        });

        connectionManager = new ConnectionManager(getActivity());
        if (!connectionManager.isConnected()) {
            View no_internet = view.findViewById(R.id.no_internet);
            no_internet.setVisibility(View.VISIBLE);
            this.progressDialog.dismiss();
        } else {
            if(!Constant.isFiltered) {
                GetAccessory_Items(BaseClass.BaseUrl + "get_product_according_to_catogery?catogory_id=" + Category_ID);
            }
        }

        change_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_clicks_view++;
                if (count_clicks_view % 2 == 1) {

                    category_data_list.setAdapter(new CategoryData_Adpater(data, R.layout.accessories_list_item2));
                    view_icon.setImageResource(R.drawable.grid);
                    category_data_list.setNumColumns(1);
                } else {
                    category_data_list.setAdapter(new CategoryData_Adpater(data, R.layout.accessories_list_item));
                    view_icon.setImageResource(R.drawable.list);
                    category_data_list.setNumColumns(2);
                }

            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Slide slide = new Slide(Gravity.TOP);
                slide.setDuration(500);
                Slide slide2 = new Slide(Gravity.BOTTOM);
                slide.setDuration(500);
                filter_layout filter_layout = new filter_layout(accessories_items_list.this);
                filter_layout.setEnterTransition(slide);
                filter_layout.setEnterTransition(slide2);
                ((DashbordActivity) getActivity()).replace_Fragment(filter_layout);
            }
        });

        sort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                count_clicks_sort++;
                if (count_clicks_sort % 2 == 1 && count_clicks_view % 2 == 1) {
                    Collections.sort(data_price_sorted);
                    sort_image.setImageResource(R.drawable.sort);
                    category_data_list.setAdapter(new CategoryData_Adpater(data_price_sorted, R.layout.accessories_list_item2));
                } else if (count_clicks_sort % 2 == 0 && count_clicks_view % 2 == 1) {
                    sort_image.setImageResource(R.drawable.unsort);
                    category_data_list.setAdapter(new CategoryData_Adpater(data, R.layout.accessories_list_item2));
                } else if (count_clicks_sort % 2 == 1 && count_clicks_view % 2 == 0) {
                    Collections.sort(data_price_sorted);
                    sort_image.setImageResource(R.drawable.sort);
                    category_data_list.setAdapter(new CategoryData_Adpater(data_price_sorted, R.layout.accessories_list_item));
                } else {
                    sort_image.setImageResource(R.drawable.unsort);
                    category_data_list.setAdapter(new CategoryData_Adpater(data, R.layout.accessories_list_item));
                }
            }
        });

        category_data_list.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int btn_initPosY = find_fab.getScrollY();

                if (view.getId() == category_data_list.getId()) {
                    final int currentFirstVisibleItem = category_data_list.getFirstVisiblePosition();

                    if (currentFirstVisibleItem > mLastFirstVisibleItem) {
                        mIsScrollingUp = false;
                    } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
                        mIsScrollingUp = true;
                    }

                    mLastFirstVisibleItem = currentFirstVisibleItem;
                }

                if (mIsScrollingUp) {

                    find_fab.animate().cancel();
                    find_fab.animate().translationY(btn_initPosY);
                } else {
                    find_fab.animate().cancel();
                    find_fab.animate().translationYBy(200);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        if(Constant.isFiltered && data.size()==0)
        {
            no_item_found.setVisibility(View.VISIBLE);
        }
        else {
            no_item_found.setVisibility(View.INVISIBLE);
        }


        return view;
    }

    private class CategoryData_Adpater extends BaseAdapter {

        ArrayList<Accessory_item_model> data;
        int layout;

        public CategoryData_Adpater(ArrayList<Accessory_item_model> data, int layout) {
            this.data = data;
            this.layout = layout;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int i) {
            return data.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = LayoutInflater.from(getActivity()).inflate(layout, viewGroup, false);
            }

            Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.null_tofade);
            animation.setDuration(500);
            view.setAnimation(animation);

            ImageView accessory_image;
            TextView accessory_name, accessory_price;

            accessory_image = (ImageView) view.findViewById(R.id.accessory_image);
            accessory_name = (TextView) view.findViewById(R.id.accessory_name);
            accessory_price = (TextView) view.findViewById(R.id.accessory_price);


            Accessory_item_model current_item = data.get(i);

            if (current_item.getImages().length > 0)
                Picasso.get().load(BaseClass.BaseUrl + current_item.getImages()[0]).placeholder(R.drawable.dummy_rectangle_image).into(accessory_image);

            accessory_name.setText(current_item.getAccessory_name());
            accessory_price.setText(getString(R.string.Rs) + " " + current_item.getPrice());

            return view;
        }
    }

    public void init(View view) {

        category_data_list = (GridView) view.findViewById(R.id.category_data_list);

        this.progressDialog = new CustomDialog(getActivity());
        this.progressDialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.circle));
        this.progressDialog.setCanceledOnTouchOutside(false);

        sort = (LinearLayout) view.findViewById(R.id.sort);
        sort_layout = (LinearLayout) view.findViewById(R.id.sort_layout);
        change_view = (LinearLayout) view.findViewById(R.id.change_view);
        view_icon = (ImageView) view.findViewById(R.id.view_icon);
        header_text = (TextView) view.findViewById(R.id.header_text);
        back_button = (ImageView) view.findViewById(R.id.back_button);
        sort_image = (ImageView) view.findViewById(R.id.sort_image);
        filter = (ImageView) view.findViewById(R.id.filter);
        find_fab = (ImageView) view.findViewById(R.id.filter);
        no_item_found = (TextView) view.findViewById(R.id.no_item_found);
    }

    public void GetAccessory_Items(String mURL) {
        ((DashbordActivity)getActivity()).showDialog(true);
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,
                        mURL,
                        null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                ReadJson(response);

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
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


    private void ReadJson(JSONObject json_String) {
        Log.e("DATA", json_String.toString());

        try {

            JSONArray jsonArray = json_String.getJSONArray("product_details");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject currentObject = jsonArray.getJSONObject(i);
                int id = Integer.parseInt(currentObject.getString("product_id"));
                String product_name = currentObject.getString("AName");
                int vehicle_type = currentObject.getInt("vehicle_Type");
                String description = currentObject.getString("description");
                int brand_id = currentObject.getInt("brand_id");
                JSONArray images = currentObject.getJSONArray("images");
                int images_length = images.length();
                String image_paths[] = new String[images_length];
                if (images_length > 0) {
                    for (int j = 0; j < images_length; j++) {
                        image_paths[j] = images.getJSONObject(j).getString("image_path");
                    }
                }

                int price = currentObject.getInt("price");
                int vendor_id = currentObject.getInt("vendor_id");
                String vendor_name = currentObject.getString("vendor_name");
                int stock = currentObject.getInt("stock");

                data.add(new Accessory_item_model(image_paths, product_name, price, description, id, vehicle_type, brand_id, vendor_id, vendor_name, stock));
                data_price_sorted.add(new Accessory_item_model(image_paths, product_name, price, description, id, vehicle_type, brand_id, vendor_id, vendor_name, stock));

            }
            Log.e("DAta",String.valueOf(data.size()));

        } catch (JSONException e) {
            e.printStackTrace();
        }
        ((DashbordActivity)getActivity()).showDialog(false);
        category_data_list.setAdapter(new CategoryData_Adpater(data, R.layout.accessories_list_item));
        if(Constant.isFiltered && data.size()==0)
        {
            no_item_found.setVisibility(View.VISIBLE);
        }
        else {
            no_item_found.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);

        FontChangeCrawler fontChanger = new FontChangeCrawler(getActivity().getAssets(), "font/Main.ttf");
        fontChanger.replaceFonts((ViewGroup) view.findViewById(R.id.accessories_list_main));

    }

    public void setURL(String brand_URL, String model_URL) {
        Constant.isFiltered = true;
        if(!(brand_URL.equals("")))
        {
            GetAccessory_Items(brand_URL);
        }
        if(!(model_URL.equals("")))
        {
            GetAccessory_Items(model_URL);
        }
        if(brand_URL.equals("")&&model_URL.equals(""))
            Constant.isFiltered = false;
    }

}
