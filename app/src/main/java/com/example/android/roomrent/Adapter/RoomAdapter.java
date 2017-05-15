package com.example.android.roomrent.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.roomrent.Activity.R;
import com.example.android.roomrent.Model.Room;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {

    private List<Room> roomList;
    private Context context;

    public RoomAdapter(Context context, List<Room> roomList) {
        this.context = context;
        this.roomList = roomList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ask_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Room room = roomList.get(position);
        holder.place.setText(room.getPlace());
        holder.noOfRoom.setText(room.getNoOfRoom());
        holder.image.setImageResource(R.drawable.choose_image_icon);
    }

    @Override
    public int getItemCount() {
        return roomList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView place, noOfRoom;
        private ImageView image;

        private MyViewHolder(View view) {
            super(view);
            place = (TextView) view.findViewById(R.id.address);
            noOfRoom = (TextView) view.findViewById(R.id.noOfRoom);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
}

