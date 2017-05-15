package com.example.android.roomrent.Rest;

import android.support.annotation.NonNull;

import com.example.android.roomrent.Activity.RoomRentApplication;
import com.jakewharton.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;


/**
 * Created by mka on 5/3/17.
 */

public class PicassoAuthenticationInterceptor {

    private static Picasso picasso = new Picasso.Builder(RoomRentApplication.getContext())
            .downloader(new OkHttp3Downloader(getClient())).build();

    public PicassoAuthenticationInterceptor() {
    }

    private static OkHttpClient.Builder getBuilder() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().
                setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(interceptor);
        builder.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder builder1 = original.newBuilder()
                        .addHeader("Authorization", "Bearer " + RoomRentApplication.getApiToken());
                Request request = builder1.build();
                return chain.proceed(request);
            }
        });
        return builder;
    }

    @NonNull
    private static OkHttpClient getClient() {
        return getBuilder().build();
    }

    public static Picasso getPicasso() {
        return picasso;
    }
}
