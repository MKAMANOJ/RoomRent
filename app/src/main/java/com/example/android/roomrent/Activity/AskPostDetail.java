package com.example.android.roomrent.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.roomrent.Model.PostDatum;
import com.example.android.roomrent.Model.RoundedTransformation;
import com.example.android.roomrent.Rest.PicassoAuthenticationInterceptor;

/**
 * Created by mka on 5/11/17.
 */

public class AskPostDetail extends AppCompatActivity {

    ImageView userProfilePicture;

    TextView title, description, noOfRoom, price, address, postCreatedOn, name, email , phone;
    PostDatum postDatum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_post_detail);

        // initialize User Data
        Bundle data = getIntent().getExtras();
        postDatum = data.getParcelable("askPost");

        userProfilePicture = (ImageView) findViewById(R.id.profilePicture);
        title = (TextView) findViewById(R.id.ask_title_text);
        description = (TextView) findViewById(R.id.ask_description_text);
        noOfRoom = (TextView) findViewById(R.id.ask_no_of_room_text);
        price = (TextView) findViewById(R.id.ask_price_text);
        address = (TextView) findViewById(R.id.ask_address_text);
        postCreatedOn = (TextView) findViewById(R.id.ask_post_created_text);
        name = (TextView) findViewById(R.id.ask_user_name);
        email = (TextView) findViewById(R.id.ask_user_email);
        phone = (TextView) findViewById(R.id.ask_user_phone);

    }

    @Override
    protected void onPostResume() {
        super.onPostResume();

        PicassoAuthenticationInterceptor.getPicasso().load(RoomRentApplication.imageUrl +
                postDatum.getUser().getProfileImage())
                .placeholder(R.drawable.logo)
                .error(R.drawable.choose_image_icon)
                .transform(new RoundedTransformation(100, 10))
                .resizeDimen(R.dimen.list_detail_image_size_2, R.dimen.list_detail_image_size_2)
                .centerCrop()
                .into(userProfilePicture);

        title.setText(postDatum.getTitle());
        description.setText(postDatum.getDescription());
        noOfRoom.setText(String.valueOf(postDatum.getNoOfRooms()));
        price.setText(String.valueOf(postDatum.getPrice()));
        address.setText(postDatum.getAddress());
        postCreatedOn.setText(postDatum.getCreatedAt());
        name.setText(postDatum.getUser().getName());
        email.setText(postDatum.getUser().getEmail());
        phone.setText(postDatum.getUser().getPhone());
    }
}
