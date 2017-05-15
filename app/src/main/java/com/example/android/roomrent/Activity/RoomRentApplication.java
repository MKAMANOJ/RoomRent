package com.example.android.roomrent.Activity;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;

/**
 * Class to take Application Context and animation
 *
 */
public class RoomRentApplication extends Application {

    public static Animation animation;
    private static Context mContext;
    private static String apiToken;

    public static final String imageUrl = "http://192.168.0.143:81/api/v1/getfile/";

    public static String getApiToken() {
        return apiToken;
    }

    public static void setApiToken(String apiToken) {
        RoomRentApplication.apiToken = apiToken;
    }

    public static Context getContext() {
        return mContext;
    }

    public static boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) RoomRentApplication.getContext().
                getSystemService(Context.CONNECTIVITY_SERVICE); // 1
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo(); // 2
        return networkInfo != null && networkInfo.isConnected(); // 3
    }

    /*
*Return parent TextInputLayout for the edittext
*Return null if edittext has no parent TextInputLayout
 */
    @Nullable
    public static TextInputLayout getTextInputLayout(@NonNull EditText editText) {
        View currentView = editText;
        for (int i = 0; i < 2; i++) {
            ViewParent parent = currentView.getParent();
            if (parent instanceof TextInputLayout) {
                return (TextInputLayout) parent;
            } else {
                currentView = (View) parent;
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();

        animation = AnimationUtils.
                loadAnimation(RoomRentApplication.getContext(), R.anim.shake);
    }

}