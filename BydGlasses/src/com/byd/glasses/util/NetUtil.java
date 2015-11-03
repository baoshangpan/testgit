
package com.byd.glasses.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtil {

    public NetUtil() {
        // TODO Auto-generated constructor stub
    }

    public static boolean isConnect(Context context) {
        boolean ret = false;

        ConnectivityManager mManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (null != mManager) {
            NetworkInfo info = mManager.getActiveNetworkInfo();
            if (null != info && info.isConnected()) {
                ret = true;
            }
        }
        return ret;
    }
}
