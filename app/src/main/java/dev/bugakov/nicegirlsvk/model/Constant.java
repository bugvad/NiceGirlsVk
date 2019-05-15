package dev.bugakov.nicegirlsvk.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Constant {

    public static final int PAGE_SIZE = 5;
    public static final int LOAD_INITIAL_ID = 0;
    public static final int LOAD_BEFORE_ID = -1;
    public static final int LOAD_AFTER_ID = 1;
    public static int from = 18;
    public static int to = 25;

    public static int getFrom() {
        return from;
    }
    public static int getTo() {
        return to;
    }
    public static void setFrom(int from) {
        Constant.from = from;
    }
    public static void setTo(int to) {
        Constant.to = to;
    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo anInfo : info) {
                    if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
