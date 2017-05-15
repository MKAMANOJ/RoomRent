package com.example.android.roomrent.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.android.roomrent.Helper.InputValidatorHelper;
import com.example.android.roomrent.Model.ForgotPasswordRequest;
import com.example.android.roomrent.Model.ForgotPasswordResponse;
import com.example.android.roomrent.Rest.RetrofitCallBack;
import com.example.android.roomrent.Rest.RetrofitInterface;
import com.example.android.roomrent.Rest.RetrofitService;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity implements
        RetrofitCallBack<ForgotPasswordResponse> {

    private static final String LOG_TAG = ForgetPasswordActivity.class.getName();

    /**
     * instance of singleton validation class
     * <p>
     * It can be used to validate email, phone number, passwords, Username ,
     * empty string and minimum number of character
     */
    InputValidatorHelper inputValidator = InputValidatorHelper.getInstance();
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

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
    protected void onPostResume() {
        super.onPostResume();

        email = (EditText) findViewById(R.id.email);
        inputValidator.compareWithRegex(email);
    }

    /**
     * Sends Password to Given Email in EditText above
     *
     * @param view Send Password
     */
    public void sendPassword(View view) {

        if (inputValidator.checkValidation(email, true)) {
            ForgotPasswordRequest forgotPasswordRequest = new ForgotPasswordRequest(
                    email.getText().toString()
            );

            // prepare for a progress bar dialog
            final ProgressDialog mProgressDialog = new ProgressDialog(view.getContext());
            mProgressDialog.setTitle(this.getString(R.string.progress));
            mProgressDialog.setMessage(this.getString(R.string.sending_data));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();

            RetrofitInterface retrofitService = RetrofitService.getClient();
            Call<ForgotPasswordResponse> call = retrofitService.forgotPassword(forgotPasswordRequest);
            call.enqueue(new Callback<ForgotPasswordResponse>() {
                @Override
                public void onResponse(Call<ForgotPasswordResponse> call, Response<ForgotPasswordResponse> response) {

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
                public void onFailure(Call<ForgotPasswordResponse> call, Throwable t) {

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

    @Override
    public void success(Response<ForgotPasswordResponse> response) {

        ForgotPasswordResponse forgotPasswordResponse = response.body();
        if (!forgotPasswordResponse.getCode().equals("0023")) {
            displayDialog(this.getString(R.string.error),
                    forgotPasswordResponse.getMessage() + "\n" + this.getString(R.string.try_again));
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(this.getString(R.string.reset_password_link))
                    .setMessage(forgotPasswordResponse.getMessage())
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startActivity(new Intent(ForgetPasswordActivity.this, MainActivity.class));
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
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