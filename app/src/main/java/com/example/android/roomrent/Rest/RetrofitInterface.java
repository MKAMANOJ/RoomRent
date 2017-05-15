package com.example.android.roomrent.Rest;


import com.example.android.roomrent.Model.ForgotPasswordRequest;
import com.example.android.roomrent.Model.ForgotPasswordResponse;
import com.example.android.roomrent.Model.LoginRequest;
import com.example.android.roomrent.Model.LoginResponse;
import com.example.android.roomrent.Model.NewPostResponse;
import com.example.android.roomrent.Model.PostStatistics;
import com.example.android.roomrent.Model.SignUpResponse;
import com.example.android.roomrent.Model.UpdateAvatarResponse;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RetrofitInterface {

    @GET("api/v1/posts/{type}")
    Call<PostStatistics> getNewPost(
            @Header("Authorization") String authToken,
            @Path("type") String type,
            @Query("latitude") Double latitude,
            @Query("longitude") Double longitude,
            @Query("offset") Integer offset
    );

    @POST("api/v1/login")
    Call<LoginResponse> login(@Body LoginRequest userCredentials);

    @POST("api/v1/forgotpassword")
    Call<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest userEmail);

    @POST("api/v1/logout")
    Call<ResponseBody> logout(@Header("Authorization") String authToken);

    @Multipart
    @GET("api/v1/getFile")
    Call<ResponseBody> getFile(@Header("Authorization") String authToken,
                               @Part("filename") RequestBody filename);

    //MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));
    @Multipart
    @POST("/api/v1/register")
    Call<SignUpResponse> register (@Part("profile_image\"; filename=\"pp.jpg\" ") RequestBody file ,
                                 @Part("name") RequestBody name,
                                 @Part("username") RequestBody username,
                                 @Part("email") RequestBody email,
                                 @Part("password") RequestBody password,
                                 @Part("phone") RequestBody phone);

    @Multipart
    @POST("/api/v1/user/updateavatar")
    Call<UpdateAvatarResponse> updateAvatar (
            @Header("Authorization") String authToken,
            @Part("profile_image\"; filename=\"pp.jpg\" ") RequestBody file);

    @Multipart
    @POST("api/v1/post/create")
    Call<NewPostResponse> newPost(
            @Header("Authorization") String authToken,
            @Part("address") RequestBody address,
            @Part("description") RequestBody description,
            @Part("latitude") RequestBody latitude,
            @Part("longitude") RequestBody longitude,
            @Part("no_of_rooms") RequestBody noOfRoom,
            @Part("post_type") RequestBody postType,
            @Part("price") RequestBody price,
            @Part("title") RequestBody title,
            @PartMap Map<String, RequestBody> Files
    );

}

