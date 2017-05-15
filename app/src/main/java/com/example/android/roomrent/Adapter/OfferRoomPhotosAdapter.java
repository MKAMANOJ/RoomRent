package com.example.android.roomrent.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.roomrent.Activity.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

public class OfferRoomPhotosAdapter extends RecyclerView.Adapter<OfferRoomPhotosAdapter.ViewHolder> {

    private static final String TAG = OfferRoomPhotosAdapter.class.getName();
    View.OnClickListener mOnClickListener;
    private Context context;
    private ArrayList<File> roomPictures;


    public OfferRoomPhotosAdapter(Context context, ArrayList<File> roomPictures) {
        this.context = context;
        this.roomPictures = roomPictures;
    }

    public void setClickListener(View.OnClickListener callback) {
        mOnClickListener = callback;
    }

    @Override
    public OfferRoomPhotosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate
                (R.layout.recycler_view_room_photos, parent, false);

        ViewHolder myViewHolder = new ViewHolder(view);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickListener.onClick(view);
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(OfferRoomPhotosAdapter.ViewHolder holder, int position) {
        Picasso.with(context).load(roomPictures.get(position)).into(holder.imageView);
        Log.e(TAG + " onclick", "" + roomPictures.size());
    }

    @Override
    public int getItemCount() {
        return roomPictures == null ? 0 : roomPictures.size();
    }

    // Static inner class to initialize the views of rows
    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;

        private ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.singleRoom);
        }
    }
}
