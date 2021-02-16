package com.motomistry.motomistry.OtherClass;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Nikhil on 30-Mar-18.
 */

public class ConnectionManager {

    Context context;

    public ConnectionManager(Context context) {
        this.context = context;
    }

    public boolean isConnected() {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();


        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            return true;

        } else
            return false;

    }

}
