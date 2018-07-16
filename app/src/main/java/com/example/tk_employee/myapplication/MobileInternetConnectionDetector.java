package com.example.tk_employee.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

class MobileInternetConnectionDetector {
    private Context context;
    public MobileInternetConnectionDetector(Context applicationContext) {
        this.context=applicationContext;
    }

    public boolean checkMobileInternetConn() {
        //Create object for ConnectivityManager class which returns network related info
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        //If connectivity object is not null
        if (connectivity != null) {
            //Get network info - Mobile internet access
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                //Look for whether device is currently connected to Mobile internet
                if (info.isConnectedOrConnecting()) {
                    return true;
                }
            }
        }
        return false;
    }
}
