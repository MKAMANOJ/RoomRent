package com.example.android.roomrent.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.android.roomrent.Activity.R;
import com.example.android.roomrent.Rest.PicassoAuthenticationInterceptor;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by mka on 5/12/17.
 */

public class ImagePagerAdapter extends PagerAdapter {

    LayoutInflater inflater;
    private List<String> images;
    Context context;
    private Picasso picasso;

    public ImagePagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        Log.e("Images Pager ADPTER", this.images.toString());
        inflater = LayoutInflater.from(context);
        picasso = PicassoAuthenticationInterceptor.getPicasso();
        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    public int getCount() {
        return images.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        String imageurl = "http://192.168.0.143:81/api/v1/getfile/"+images.get(position);
        View itemView = inflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        picasso.load(imageurl)
                .placeholder(R.drawable.lodge_room)
                .error(R.drawable.ic_logo)
                .into(imageView);

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }


}
