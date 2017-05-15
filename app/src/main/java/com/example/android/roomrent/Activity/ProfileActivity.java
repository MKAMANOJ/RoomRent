package com.example.android.roomrent.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.android.roomrent.Model.User;
import com.example.android.roomrent.Rest.RetrofitCallBack;
import com.example.android.roomrent.Rest.RetrofitInterface;
import com.example.android.roomrent.Rest.RetrofitService;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity implements
        RetrofitCallBack<ResponseBody> {

    public static final String LOG_TAG = ProfileActivity.class.getSimpleName();
    // getting fields
    TextView name, userName, email, phoneNumber;
    User user;

    public ProfileActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        name = (TextView) findViewById(R.id.name);
        userName = (TextView) findViewById(R.id.userName);
        email = (TextView) findViewById(R.id.email);
        phoneNumber = (TextView) findViewById(R.id.phoneNumber);

        // in ProcessDataActivity retrieve User
        Intent intent = getIntent();
        user = intent.getParcelableExtra("user");

        name.setText(user.getName());
        userName.setText(user.getUsername());
        email.setText(user.getEmail());
        phoneNumber.setText(user.getPhone());
    }

    public void logOut(View view) {

        // prepare for a progress bar dialog
        final ProgressDialog mProgressDialog = new ProgressDialog(ProfileActivity.this);
        mProgressDialog.setTitle(this.getString(R.string.progress));
        mProgressDialog.setMessage(this.getString(R.string.sending_data));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        RetrofitInterface retrofitService = RetrofitService.getClient();
        Call<ResponseBody> call = retrofitService.logout("Bearer " + RoomRentApplication.getApiToken());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

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
            public void onFailure(Call<ResponseBody> call, Throwable t) {

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


    @Override
    public void success(Response<ResponseBody> response) {
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
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


