package com.example.android.roomrent.Rest;


import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Configuration of Rest API
 * <p>
 * Visit https://guides.codepath.com/android/Consuming-APIs-with-Retrofit for guidelines
 * Great Samples available at http://www.vogella.com/tutorials/Retrofit/article.html#a
 * uthentication-with-okhttp-interceptors
 */
public class RetrofitService {

    private static final String BASE_URL = "http://192.168.0.143:81/";
    private static Retrofit retrofit = null;
            private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().
                    setLevel(HttpLoggingInterceptor.Level.BODY);
    private static OkHttpClient.Builder builder = new OkHttpClient.Builder().
            addInterceptor(interceptor);
    //  builder.addInterceptor(get)
    private static OkHttpClient httpClient = builder.build();

    private RetrofitService() {
    }

    public static RetrofitInterface getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient)
                    .build();
        }
        return retrofit.create(RetrofitInterface.class);
    }

}


