package com.example.android.roomrent.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.android.roomrent.Helper.InputValidatorHelper;
import com.example.android.roomrent.Helper.RequestPermission;
import com.example.android.roomrent.Helper.SaveImage;
import com.example.android.roomrent.Model.SignUpResponse;
import com.example.android.roomrent.Rest.RetrofitCallBack;
import com.example.android.roomrent.Rest.RetrofitInterface;
import com.example.android.roomrent.Rest.RetrofitService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Sign Up Activity
 * <p>
 * User register new Id from here
 */
public class SignUpActivity extends AppCompatActivity
        implements RetrofitCallBack<SignUpResponse> {

    private static final String LOG_TAG = SignUpActivity.class.getName();

    private static final int PICK_IMAGE_ID = 234; // the number doesn't matter
    /**
     * instance of singleton validation class
     * <p>
     * It can be used to validate email, phone number, passwords, Username ,
     * empty string and minimum number of character
     */
    InputValidatorHelper inputValidator = InputValidatorHelper.getInstance();
    // getting fields
    EditText name, userName, email, phoneNumber, password, confirmPassword;
    TextInputLayout textInputLayout;
    ImageView profileImage;
    File profilePicture;

    Bitmap bitmap = null;
    // for photo picker
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    @Override
    protected void onPostResume() {
        super.onPostResume();

        name = (EditText) findViewById(R.id.name);
        userName = (EditText) findViewById(R.id.userName);
        email = (EditText) findViewById(R.id.email);
        phoneNumber = (EditText) findViewById(R.id.phoneNumber);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);
        profileImage = (ImageView) findViewById(R.id.chooseImage);


        inputValidator.compareWithRegex(name, userName, email, phoneNumber, password);

        confirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!inputValidator.confirmPassword(password, confirmPassword)) {
                    confirmPassword.startAnimation(RoomRentApplication.animation);
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent myIntent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    /**
     * Send registration credentials to server
     * <p>
     * Check for errors from server and reply accordingly
     *
     * @param view Create
     */
    public void create(View view) {
        if (inputValidator.checkValidation(name, true) &&
                inputValidator.checkValidation(userName, true) &&
                inputValidator.checkValidation(email, true) &&
                inputValidator.checkValidation(password, true) &&
                inputValidator.confirmPassword(password, confirmPassword) &&
                inputValidator.checkValidation(phoneNumber, true)) {


            // prepare for a progress bar dialog
            final ProgressDialog mProgressDialog = new ProgressDialog(view.getContext());
            mProgressDialog.setTitle(this.getString(R.string.progress));
            mProgressDialog.setMessage(this.getString(R.string.sending_data));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();

            RequestBody rbProfilePicture;
            if (profilePicture != null) {
                rbProfilePicture = RequestBody.create(MediaType.parse("image/jpeg"),
                        profilePicture);
            } else {
                rbProfilePicture = null;
            }

            RequestBody rbName = RequestBody.create(MediaType.parse("text/plain"),
                    name.getText().toString());
            RequestBody rbUserName = RequestBody.create(MediaType.parse("text/plain"),
                    userName.getText().toString());
            RequestBody rbEmail = RequestBody.create(MediaType.parse("text/plain"),
                    email.getText().toString());
            RequestBody rbPassword = RequestBody.create(MediaType.parse("text/plain"),
                    password.getText().toString());
            RequestBody rbPhoneNumber = RequestBody.create(MediaType.parse("text/plain"),
                    phoneNumber.getText().toString());

            RetrofitInterface retrofitService = RetrofitService.getClient();
            Call<SignUpResponse> call = retrofitService.register(
                    rbProfilePicture,rbName,rbUserName,rbEmail,rbPassword,rbPhoneNumber
            );
            call.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(Call<SignUpResponse> call, Response<SignUpResponse> response) {

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
                public void onFailure(Call<SignUpResponse> call, Throwable t) {

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
    public void success(Response<SignUpResponse> response) {

        SignUpResponse signUpResponse = response.body();
        Toast.makeText(this, signUpResponse.getMessage(), Toast.LENGTH_LONG).show();

        if (signUpResponse.getCode().equals("0013")) {
            new AlertDialog.Builder(this)
                    .setTitle(this.getString(R.string.sign_up_verification_required))
                    .setMessage(this.getString(R.string.sign_up_verification_link))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        } else {
            SignUpResponse.Errors errors = signUpResponse.getErrors();
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
                            case "email":
                                textInputLayout = RoomRentApplication.getTextInputLayout(email);

                                for (String string : errors.getEmail()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    email.setText(error);
                                }
                                break;

                            case "name":
                                textInputLayout = RoomRentApplication.getTextInputLayout(name);

                                for (String string : errors.getName()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    email.setText(error);
                                }
                                break;

                            case "password":
                                textInputLayout = RoomRentApplication.getTextInputLayout(password);

                                for (String string : errors.getPassword()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    email.setText(error);
                                }
                                break;

                            case "phone":
                                textInputLayout = RoomRentApplication.getTextInputLayout(phoneNumber);

                                for (String string : errors.getPhone()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    email.setText(error);
                                }
                                break;

                            case "username":
                                textInputLayout = RoomRentApplication.getTextInputLayout(userName);

                                for (String string : errors.getUsername()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    email.setText(error);
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
    /**
     * Check if user want to upload pictures
     *
     * @param view Choose Gallery
     */
    public void chooseGallery(View view) {
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = RequestPermission.checkPermission(SignUpActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case RequestPermission.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext()
                                .getContentResolver(), data.getData());
                        profilePicture = SaveImage.storeImage(bitmap);
                        Toast.makeText(SignUpActivity.this,profilePicture.getAbsolutePath(),
                                Toast.LENGTH_LONG).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            } else if (requestCode == REQUEST_CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
                profilePicture = SaveImage.storeImage(bitmap);
            }
        }

        Picasso.with(SignUpActivity.this).load(profilePicture).into(profileImage);
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

}
