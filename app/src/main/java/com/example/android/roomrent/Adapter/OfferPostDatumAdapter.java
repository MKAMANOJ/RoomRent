package com.example.android.roomrent.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.android.roomrent.Activity.R;
import com.example.android.roomrent.Helper.CustomViewPager;
import com.example.android.roomrent.Helper.ItemClickListener;
import com.example.android.roomrent.Helper.PaginationAdapterCallback;
import com.example.android.roomrent.Model.PostDatum;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by mka on 5/4/17.
 */

public class OfferPostDatumAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    static class OfferViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rootView;//newly added field
        TextView offerTitle;
        CustomViewPager viewPager;
        TextView description;
        TextView address;
        TextView price;
        TextView noOfRooms;
        View v;

        OfferViewHolder(View itemView) {
            super(itemView);

            rootView = (RelativeLayout) itemView.findViewById(R.id.rootView);
            offerTitle = (TextView) itemView.findViewById(R.id.offer_title);
            viewPager = (CustomViewPager) itemView.findViewById(R.id.offer_room_images);

            description = (TextView) itemView.findViewById(R.id.offer_description);
            address = (TextView) itemView.findViewById(R.id.offer_address);
            price = (TextView) itemView.findViewById(R.id.offer_price);
            noOfRooms = (TextView) itemView.findViewById(R.id.offer_no_of_rooms);
            v = itemView;
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ProgressBar mProgressBar;
        private ImageButton mRetryButton;
        private TextView mErrorText;
        private LinearLayout mErrorLayout;

        LoadingViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryButton = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorText = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryButton.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:
                    showRetry(false, null);
                    mCallback.retryPageLoad();
                    break;
            }
        }
    }


    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;
    private String errorMsg;

    private List<PostDatum> offerDetailsList;
    private Context context;

    private ItemClickListener clickListener;

    public OfferPostDatumAdapter( Context context, PaginationAdapterCallback callback) {
        this.offerDetailsList = new ArrayList<>();
        this.context = context;
        this.mCallback = callback;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View loading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(loading);
                break;
        }
        return viewHolder;
    }

    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View offerView = inflater.inflate(R.layout.offer_item, parent, false);
        viewHolder = new OfferViewHolder(offerView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        PostDatum offer = offerDetailsList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                OfferViewHolder offerHolder = (OfferViewHolder) holder;
//
//                if (position % 2 == 0) {
//                    offerHolder.rootView.setBackgroundResource(R.color.recycler_even);
//                } else {
//                    offerHolder.rootView.setBackgroundResource(R.color.recycler_odd);
//                }

                offerHolder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (clickListener != null) clickListener.onClick(v, position);

                    }
                });
                offerHolder.offerTitle.setText(offer.getTitle());
                offerHolder.description.setText(offer.getDescription());
                if(offer.getImages()!=null){
                    offerHolder.viewPager.setAdapter(new ImagePagerAdapter(context, offer.getImages()));
                }
                offerHolder.address.setText(offer.getAddress());
                offerHolder.price.setText(Integer.toString(offer.getPrice()));
                offerHolder.noOfRooms.setText(Integer.toString(offer.getNoOfRooms()));
                break;
            case LOADING:
                LoadingViewHolder loading = (LoadingViewHolder) holder;
                if (retryPageLoad) {
                    loading.mErrorLayout.setVisibility(View.VISIBLE);
                    loading.mProgressBar.setVisibility(View.GONE);
                    loading.mErrorText.setText(errorMsg != null ?
                            errorMsg : context.getString(R.string.error_msg));
                } else {
                    loading.mErrorLayout.setVisibility(View.GONE);
                    loading.mProgressBar.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void showRetry(boolean show, @Nullable String errorMessage) {
        retryPageLoad = show;
        notifyItemChanged(offerDetailsList.size() - 1);

        if (errorMessage != null) this.errorMsg = errorMessage;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == offerDetailsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    @Override
    public int getItemCount() {
        return offerDetailsList == null ? 0 : offerDetailsList.size();
    }

    public void add(PostDatum offer) {
        offerDetailsList.add(offer);
        notifyItemInserted(offerDetailsList.size() - 1);
    }

    public void addAll(List<PostDatum> offerList) {
        for (PostDatum offer : offerList) {
            add(offer);
        }
    }

        public void remove(PostDatum offer) {
        int position = offerDetailsList.indexOf(offer);
        if (position > -1) {
            offerDetailsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new PostDatum());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = offerDetailsList.size() - 1;
        PostDatum offer = getItem(position);

        if (offer != null) {
            offerDetailsList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public int getSize() {
        return offerDetailsList.size();
    }

    public PostDatum getItem(int position) {
        return offerDetailsList.get(position);
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }
}