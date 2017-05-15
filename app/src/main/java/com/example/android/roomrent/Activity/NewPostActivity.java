package com.example.android.roomrent.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.roomrent.Adapter.OfferRoomPhotosAdapter;
import com.example.android.roomrent.Helper.InputValidatorHelper;
import com.example.android.roomrent.Helper.RequestPermission;
import com.example.android.roomrent.Helper.SaveImage;
import com.example.android.roomrent.Model.NewPostResponse;
import com.example.android.roomrent.Rest.RetrofitCallBack;
import com.example.android.roomrent.Rest.RetrofitInterface;
import com.example.android.roomrent.Rest.RetrofitService;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewPostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener
        , RetrofitCallBack<NewPostResponse> {

    private final static String TAG = NewPostActivity.class.getName();

    private final static int MAX_PHOTO_COUNT = 5;
    private static final int PLACE_PICKER_REQUEST = 1;
    // for photo picker
    private final static int REQUEST_CAMERA = 0, SELECT_FILE = 2;
    /**
     * instance of singleton validation class
     * <p>
     * It can be used to validate email, phone number, passwords, Username ,
     * empty string and minimum number of character
     */
    InputValidatorHelper inputValidator = InputValidatorHelper.getInstance();
    // layout items
    Spinner spinner;
    EditText address, description, noOfRooms, price, title;
    ArrayList<File> roomPictures; // to hold room photos
    // extraData calculate
    int postType;
    double latitude, longitude;
    Bitmap bitmap = null;
    File room; // to hold room photo
    boolean isFromFooterView = false;
    int photoRoomIndex = -1;
    Button footerView;
    LinearLayout roomPhotosLinearLayout;
    RecyclerView roomPhotosrecyclerView;
    OfferRoomPhotosAdapter offerRoomPhotosAdapter;
    TextInputLayout textInputLayout;
    private String userChoosenTask;
    private TextView location; // google map location holder

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);

        address = (EditText) findViewById(R.id.location);
        description = (EditText) findViewById(R.id.description);
        noOfRooms = (EditText) findViewById(R.id.noOfRoom);
        price = (EditText) findViewById(R.id.price);
        title = (EditText) findViewById(R.id.title);
        location = (TextView) findViewById(R.id.location);
        spinner = (Spinner) findViewById(R.id.post_type_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.post_type, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //spinner.setOnItemSelectedListener(this);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        //instatiate  horizontal linear layout to hold room photos
        roomPhotosLinearLayout = (LinearLayout) findViewById(R.id.roomPhotosLinearLayout);

        // houseImages = (LinearLayout) findViewById(R.id.linearHouseImages);
        roomPictures = new ArrayList<File>();

        roomPhotosrecyclerView = (RecyclerView) findViewById(R.id.room_photos_list);
        // for fast scrolling
        roomPhotosrecyclerView.setItemViewCacheSize(20);
        roomPhotosrecyclerView.setDrawingCacheEnabled(true);
        roomPhotosrecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        roomPhotosrecyclerView.setItemAnimator(new DefaultItemAnimator());

        // set the recycler view adapter
        offerRoomPhotosAdapter = new OfferRoomPhotosAdapter(NewPostActivity.this, roomPictures);

        roomPhotosrecyclerView.setAdapter(offerRoomPhotosAdapter);

        offerRoomPhotosAdapter.setClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = roomPhotosrecyclerView.indexOfChild(v);
                isFromFooterView = false;
                photoRoomIndex = pos;
                uploadRoomPhoto();
            }
        });
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!inputValidator.hasText(title)) {
                    title.startAnimation(RoomRentApplication.animation);
                }
            }
        });
        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!inputValidator.hasText(description)) {
                    description.startAnimation(RoomRentApplication.animation);
                }
            }
        });
        noOfRooms.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!inputValidator.hasText(noOfRooms)) {
                    noOfRooms.startAnimation(RoomRentApplication.animation);
                }
            }
        });
        price.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!inputValidator.hasText(price)) {
                    price.startAnimation(RoomRentApplication.animation);
                }
            }
        });
        address.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!inputValidator.hasText(address)) {
                    address.startAnimation(RoomRentApplication.animation);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                Snackbar.make(selectedItemView, String.valueOf(position), Snackbar.LENGTH_LONG).show();
                TextView addHousePhotosTitle = (TextView) findViewById(R.id.addHousePhotosTitle);

                if (position == 1) {

                    addHousePhotosTitle.setVisibility(View.VISIBLE);

                    footerView = new Button(NewPostActivity.this);
                    footerView.setBackgroundResource(R.drawable.ic_add_photos);
                    footerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
                    footerView.setGravity(Gravity.CENTER_VERTICAL);
                    footerView.setPadding(20, 20, 20, 20);
                    roomPhotosLinearLayout.addView(footerView);
                    footerView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (roomPictures.size() < MAX_PHOTO_COUNT) {
                                isFromFooterView = true;
                                uploadRoomPhoto();
                            }
                        }
                    });

                } else if (position == 0) {
                    // houseImages.removeAllViews();
                    addHousePhotosTitle.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //spinner.setTop(parent.getItemAtPosition(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Do nothing for now
    }

    public void post(View view) {
        Snackbar.make(view, "You clicked new post", Snackbar.LENGTH_LONG).show();
        if (inputValidator.hasText(title) &&
                inputValidator.hasText(description) &&
                inputValidator.hasText(noOfRooms) &&
                inputValidator.hasText(price) &&
                inputValidator.hasText(address)) {

            // prepare for a progress bar dialog
            final ProgressDialog mProgressDialog = new ProgressDialog(view.getContext());
            mProgressDialog.setTitle(this.getString(R.string.progress));
            mProgressDialog.setMessage(this.getString(R.string.sending_data));
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.show();
            // Content-Disposition: form-data; name="profile_image"; filename="pp.jpg" "
            Map<String, RequestBody> roomPhotos = new HashMap<String, RequestBody>();

            for (int i = 0; i < roomPictures.size(); i++) {
                roomPhotos.put("images[" + i + "]\"; filename=\"pp.jpg\" ",
                        RequestBody.create(MediaType.parse("image/jpeg"),
                                roomPictures.get(i)));
            }

            DecimalFormat df = new DecimalFormat("#.############");
            df.setRoundingMode(RoundingMode.CEILING);

            Toast.makeText(NewPostActivity.this, address.length() + "", Toast.LENGTH_LONG).show();

            RequestBody rbAddress = RequestBody.create(MediaType.parse("text/plain"),
                    address.getText().toString());
            RequestBody rbDescription = RequestBody.create(MediaType.parse("text/plain"),
                    description.getText().toString());
            RequestBody rbLatitude = RequestBody.create(MediaType.parse("text/plain"),
                    String.valueOf(df.format(latitude)));
            RequestBody rbLongitude = RequestBody.create(MediaType.parse("text/plain"),
                    String.valueOf(df.format(longitude)));
            RequestBody rbNoOfRoom = RequestBody.create(MediaType.parse("text/plain"),
                    noOfRooms.getText().toString());
            RequestBody rbTitle = RequestBody.create(MediaType.parse("text/plain"),
                    title.getText().toString());
            RequestBody rbPrice = RequestBody.create(MediaType.parse("text/plain"),
                    price.getText().toString());

            RequestBody rbPostType;
            if (spinner.getSelectedItemPosition() == 0) {
                rbPostType = RequestBody.create(MediaType.parse("text/plain"),
                        String.valueOf(2));
            } else {
                rbPostType = RequestBody.create(MediaType.parse("text/plain"),
                        String.valueOf(1));
            }

            RetrofitInterface retrofitService = RetrofitService.getClient();
            Call<NewPostResponse> call = retrofitService.newPost(
                    "Bearer " + RoomRentApplication.getApiToken(),
                    rbAddress, rbDescription, rbLatitude, rbLongitude, rbNoOfRoom, rbPostType,
                    rbPrice, rbTitle, roomPhotos
            );
            call.enqueue(new Callback<NewPostResponse>() {
                @Override
                public void onResponse(Call<NewPostResponse> call, Response<NewPostResponse> response) {

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
                public void onFailure(Call<NewPostResponse> call, Throwable t) {

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

    public void addLocation(View view) {
        Snackbar.make(view, "You clicked Add Location", Snackbar.LENGTH_LONG).show();
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();
            Intent intent = intentBuilder.build(NewPostActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException
                | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
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

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {

            final Place place = PlacePicker.getPlace(this, data);
            final CharSequence name = place.getName();
            DecimalFormat df = new DecimalFormat("##.########");
            df.setRoundingMode(RoundingMode.CEILING);
            latitude = place.getLatLng().latitude;
            longitude = place.getLatLng().longitude;
            final CharSequence address = place.getAddress();
            location.setText(name + " " + address);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                if (data != null) {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext()
                                .getContentResolver(), data.getData());
                        room = SaveImage.storeImage(bitmap);

                        if (isFromFooterView) {
                            roomPictures.add(room);
                            isFromFooterView = false;
                        } else {
                            roomPictures.set(photoRoomIndex, room);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            } else if (requestCode == REQUEST_CAMERA) {
                bitmap = (Bitmap) data.getExtras().get("data");
                room = SaveImage.storeImage(bitmap);

                if (isFromFooterView) {
                    roomPictures.add(room);
                    isFromFooterView = false;
                } else {
                    roomPictures.set(photoRoomIndex, room);
                }
            }
            // notify adapter about change of data
            offerRoomPhotosAdapter.notifyDataSetChanged();

            if (roomPictures.size() >= MAX_PHOTO_COUNT) {
                roomPhotosLinearLayout.removeView(footerView);
            }
        }

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

    public void uploadRoomPhoto() {

        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(NewPostActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = RequestPermission.checkPermission(NewPostActivity.this);

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
    public void success(Response<NewPostResponse> response) {
        NewPostResponse newPostResponse = response.body();
        Toast.makeText(this, newPostResponse.getMessage(), Toast.LENGTH_LONG).show();

        if (newPostResponse.getCode().equals("0011")) {
            new AlertDialog.Builder(this)
                    .setTitle(this.getString(R.string.new_post_success_title))
                    .setMessage(this.getString(R.string.new_post_success_msg))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setIcon(android.R.drawable.ic_dialog_alert).show();
        } else {
            NewPostResponse.Errors errors = newPostResponse.getErrors();
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
                            case "title":
                                textInputLayout = RoomRentApplication.getTextInputLayout(title);

                                for (String string : errors.getTitle()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    title.setText(error);
                                }
                                break;

                            case "description":
                                textInputLayout = RoomRentApplication.getTextInputLayout(description);

                                for (String string : errors.getDescription()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    description.setText(error);
                                }
                                break;

                            case "no_of_rooms":
                                textInputLayout = RoomRentApplication.getTextInputLayout(noOfRooms);

                                for (String string : errors.getNoOfRooms()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    noOfRooms.setText(error);
                                }
                                break;

                            case "price":
                                textInputLayout = RoomRentApplication.getTextInputLayout(price);

                                for (String string : errors.getPrice()) {
                                    error += string + "\n";
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    price.setText(error);
                                }
                                break;

                            case "latitude":
                            case "address":
                            case "longitude":
                                textInputLayout = RoomRentApplication.getTextInputLayout(address);


                                if (errors.getAddress() != null) {
                                    for (String string : errors.getAddress()) {
                                        error += string + "\n";
                                    }
                                }
                                if (errors.getLatitude() != null) {
                                    for (String string : errors.getLatitude()) {
                                        error += string + "\n";
                                    }
                                }
                                if (errors.getLongitude() != null) {
                                    for (String string : errors.getLongitude()) {
                                        error += string + "\n";
                                    }
                                }

                                if (textInputLayout != null) {
                                    textInputLayout.setError(error);
                                } else {
                                    address.setText(error);
                                }
                                break;

                            case "images":
                            case "images.1":
                            case "images.2":
                            case "images.3":
                            case "images.4":
                            case "images.5":
                                new AlertDialog.Builder(this)
                                        .setTitle(this.getString(R.string.error))
                                        .setMessage(errors.getImages().toString())
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).setIcon(android.R.drawable.ic_dialog_alert).show();

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