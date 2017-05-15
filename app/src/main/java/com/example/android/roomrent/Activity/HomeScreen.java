package com.example.android.roomrent.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.roomrent.Adapter.CategoryAdapter;
import com.example.android.roomrent.Helper.RequestPermission;
import com.example.android.roomrent.Helper.SaveImage;
import com.example.android.roomrent.Model.RoundedTransformation;
import com.example.android.roomrent.Model.UpdateAvatarResponse;
import com.example.android.roomrent.Model.User;
import com.example.android.roomrent.Rest.PicassoAuthenticationInterceptor;
import com.example.android.roomrent.Rest.RetrofitCallBack;
import com.example.android.roomrent.Rest.RetrofitInterface;
import com.example.android.roomrent.Rest.RetrofitService;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.R.string.ok;

public class HomeScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RetrofitCallBack<ResponseBody> {
    private static final String TAG = HomeScreen.class.getName();
    public User user;

    ImageView profilePictureHolder;
    TextView emailHolder, usernameHolder;
    File profilePicture;

    Bitmap bitmap = null;
    // for photo picker
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;

    View navigationHeaderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationHeaderView = navigationView.getHeaderView(0);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter adapter = new CategoryAdapter(getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        // initialize User Data
        Bundle data = getIntent().getExtras();
        user = data.getParcelable("user");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        emailHolder = (TextView) navigationHeaderView.findViewById(R.id.emailHolder);
        usernameHolder = (TextView) navigationHeaderView.findViewById(R.id.userNameHolder);
        profilePictureHolder = (ImageView) navigationHeaderView.findViewById(R.id.profilePictureHolder);

        emailHolder.setText(user.getEmail());
        usernameHolder.setText(user.getUsername());

        //String imageurl = "http://192.168.0.143:81/api/v1/getfile/" + user.getProfileImage();
        Picasso.with(HomeScreen.this).setLoggingEnabled(true);
        PicassoAuthenticationInterceptor.getPicasso().load(RoomRentApplication.imageUrl +
        user.getProfileImage())
                .fit()
                .placeholder(R.drawable.logo)
                .error(R.drawable.choose_image_icon)
                .transform(new RoundedTransformation(50, 4))
                .centerCrop()
                .into(profilePictureHolder);

        profilePictureHolder.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View view) {
                final CharSequence[] items = {"Take Photo", "Choose from Library",
                        "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeScreen.this);
                builder.setTitle("Update Avatar");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        boolean result = RequestPermission.checkPermission(HomeScreen.this);

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
                return true;
            }
        });
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
                        Toast.makeText(HomeScreen.this,profilePicture.getAbsolutePath(),
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

        RequestBody rbProfilePicture;
        if (profilePicture != null) {
            rbProfilePicture = RequestBody.create(MediaType.parse("image/jpeg"),
                    profilePicture);
        } else {
            rbProfilePicture = null;
        }
         // Todo: change with update to server
       // Picasso.with(HomeScreen.this).load(profilePicture).into(profilePictureHolder);
        // prepare for a progress bar dialog
        final ProgressDialog mProgressDialog = new ProgressDialog(HomeScreen.this);
        mProgressDialog.setTitle(this.getString(R.string.progress));
        mProgressDialog.setMessage(this.getString(R.string.sending_data));
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.show();

        RetrofitInterface retrofitService = RetrofitService.getClient();
        Call<UpdateAvatarResponse> call = retrofitService.updateAvatar("Bearer " + RoomRentApplication.getApiToken(),
                rbProfilePicture);
        call.enqueue(new Callback<UpdateAvatarResponse>() {
            @Override
            public void onResponse(Call<UpdateAvatarResponse> call, Response<UpdateAvatarResponse> response) {

                if (mProgressDialog.isShowing())
                    mProgressDialog.dismiss();

                int code = response.code();
                if (code >= 200 && code < 300) {
                    Picasso.with(HomeScreen.this).setLoggingEnabled(true);
                    PicassoAuthenticationInterceptor.getPicasso().load(
                           RoomRentApplication.imageUrl + response.body().getData().getProfileImage())
                            .fit()
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.choose_image_icon)
                            .transform(new RoundedTransformation(50, 4))
                            .centerCrop()
                            .into(profilePictureHolder);
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
            public void onFailure(Call<UpdateAvatarResponse> call, Throwable t) {

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            startActivity(new Intent(HomeScreen.this, NewPostActivity.class));
            return true;
        } else if (id == R.id.profile) {
            Toast.makeText(HomeScreen.this, "clicked profile", Toast.LENGTH_LONG).show();
            return true;
        } else if (id == R.id.action_filter) {
            Toast.makeText(HomeScreen.this, "clicked filter", Toast.LENGTH_LONG).show();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            // Handle the profile
            Intent intent = new Intent(HomeScreen.this, ProfileActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (id == R.id.logout) {
            // prepare for a progress bar dialog
            final ProgressDialog mProgressDialog = new ProgressDialog(HomeScreen.this);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void success(Response<ResponseBody> response) {
        startActivity(new Intent(HomeScreen.this, MainActivity.class));
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
                        .setPositiveButton(ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setIcon(android.R.drawable.ic_dialog_alert).show();
    }
}
