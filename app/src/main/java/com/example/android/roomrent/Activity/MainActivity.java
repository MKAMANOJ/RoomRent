package com.example.android.roomrent.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.android.roomrent.Helper.InputValidatorHelper;
import com.example.android.roomrent.Model.LoginRequest;
import com.example.android.roomrent.Model.LoginResponse;
import com.example.android.roomrent.Rest.RetrofitCallBack;
import com.example.android.roomrent.Rest.RetrofitInterface;
import com.example.android.roomrent.Rest.RetrofitService;

import java.io.IOException;
import java.lang.reflect.Field;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * This is Home Screen With the LoginResponse Section
 */
public class MainActivity extends AppCompatActivity implements RetrofitCallBack<LoginResponse> {

    private static final String TAG = MainActivity.class.getName();

    /**
     * instance of singleton validation class
     *
     * It can be used to validate email, phone number, passwords, Username ,
     * empty string and minimum number of character
     */
    InputValidatorHelper inputValidator  = InputValidatorHelper.getInstance();

    // getting global firlds
    EditText email, password;
    TextInputLayout textInputLayout;
    RetrofitInterface retrofitInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        password = (EditText) findViewById(R.id.password);
        email = (EditText) findViewById(R.id.email);

        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!inputValidator.hasText(email)) {
                    email.startAnimation(RoomRentApplication.animation);
                }
            }
        });

        password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!inputValidator.hasText(password)) {
                    password.startAnimation(RoomRentApplication.animation);
                }
            }
        });
    }

    /**
     * Function to direct to user to rest his/her password     *
     * @param view Forgetpassword
     */
    public void forgetPassword(View view){
        Intent intent = new Intent(MainActivity.this,ForgetPasswordActivity.class);
        startActivity(intent);
    }

    /**
     * When Sign In is clicked, api call goes here     *
     * @param view Sign In
     */
    public void signIn(View view){
        /*
         * send login credentials0
         * https://github.com/codepath/android_guides/wiki/Consuming-APIs-with-Retrofit
         * send device token here
         */
        if (inputValidator.hasText(email) &&
                inputValidator.hasText(password)) {

            LoginRequest loginRequest = new LoginRequest(
                    "MKA jhnjknjkn",
                    "2",
                    email.getText().toString(),
                    password.getText().toString()
            );

            // prepare for a progress bar dialog
            final ProgressDialog mProgressDialog = new ProgressDialog(view.getContext());
            mProgressDialog.setTitle(this.getString(R.string.progress));
            mProgressDialog.setMessage(this.getString(R.string.sending_data));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();

            RetrofitInterface retrofitService = RetrofitService.getClient();
            Call<LoginResponse> call = retrofitService.login(loginRequest);
            call.enqueue(new Callback<LoginResponse>() {
                @Override
                public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    int code = response.code();
                    if (code >= 200 && code < 300) {
                        success(response);
                    } else if (code == 401) {
                        unauthenticated(response);
                    } else if (code >= 400 && code < 500) {
                        clientError(response);
                    } else if (code >= 500 && code < 600) {
                        serverError(response);
                    } else {
                        unexpectedError(new RuntimeException("Unexpected response " + response));
                    }
                }

                @Override
                public void onFailure(Call<LoginResponse> call, Throwable t) {

                    if (mProgressDialog.isShowing())
                        mProgressDialog.dismiss();

                    if (t instanceof IOException) {
                        networkError((IOException) t);
                    } else {
                        unexpectedError(t);
                    }
                    t.printStackTrace();
                }
            });
        }
    }

    /**
     * Redirect to sign up activity
     * @param view Sign Up
     */
    public void signUp(View view){
        Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void success(Response<LoginResponse> response) {

        LoginResponse loginResponse = response.body();
        //Toast.makeText(this, loginResponse.getMsg(), Toast.LENGTH_LONG).show();

        if (loginResponse.getCode().equals("0011")) {
            RoomRentApplication.setApiToken(loginResponse.getApiToken());
            Intent intent = new Intent(this, HomeScreen.class);
            intent.putExtra("user", loginResponse.getUser());
            startActivity(intent);
        } else {
            LoginResponse.LoginErrors errors = loginResponse.getLoginErrors();
            if (errors != null) {
                for (Field field : errors.getClass().getDeclaredFields()) {
                    Object errorPoints = null;
                    try {
                        errorPoints = field.get(errors);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    if (errorPoints != null) {
                        String error = "";
                        switch (field.getName()) {
                            case "identity":
                                textInputLayout = RoomRentApplication.getTextInputLayout(email);

                                for (String string : errors.getIdentity()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    email.setText(error);
                                }
                                break;

                            case "deviceType":

                                for (String string : errors.getDeviceType()) {
                                    error += string + "\n";
                                }

                                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                                break;

                            case "deviceToken":

                                for (String string : errors.getDeviceToken()) {
                                    error += string + "\n";
                                }

                                Toast.makeText(this, error, Toast.LENGTH_LONG).show();
                                break;

                            case "password":
                                textInputLayout = RoomRentApplication.getTextInputLayout(password);

                                for (String string : errors.getPassword()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    password.setText(error);
                                }
                                break;

                            default:
                                break;
                        }
                    }
                }
            }
        }
    }

    @Override
    public void unauthenticated(Response<?> response) {
        displayDialog(this.getString(R.string.unauthorized_error_tile), response.message());
    }

    @Override
    public void clientError(Response<?> response) {
        displayDialog(this.getString(R.string.client_error_title), response.message());
    }

    @Override
    public void serverError(Response<?> response) {
        displayDialog(this.getString(R.string.server_error_title), response.message());
    }

    @Override
    public void networkError(IOException e) {
        if (!RoomRentApplication.isNetworkConnected()) {
            displayDialog(this.getString(R.string.no_internet_connection_title),
                    this.getString(R.string.no_internet_connection_message));
        } else {
            displayDialog(this.getString(R.string.network_error_title), e.getLocalizedMessage());
        }
    }

    @Override
    public void unexpectedError(Throwable t) {
        displayDialog(this.getString(R.string.fatal_error_title), t.getLocalizedMessage());
    }

    public void displayDialog(String title, String message) {
        AlertDialog alertDialog =
                new AlertDialog.Builder(this)
                        .setTitle(title)
                        .setMessage(message)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}