package com.example.android.roomrent.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.roomrent.Activity.R;
import com.example.android.roomrent.Activity.RoomRentApplication;
import com.example.android.roomrent.Helper.ItemClickListener;
import com.example.android.roomrent.Helper.PaginationAdapterCallback;
import com.example.android.roomrent.Model.PostDatum;
import com.example.android.roomrent.Model.RoundedTransformation;
import com.example.android.roomrent.Rest.PicassoAuthenticationInterceptor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.roomrent.Activity.R.id.profilePictureHolder;


/**
 * Created by mka on 5/4/17.
 */

public class AskPostDatumAdapter extends RecyclerView.Adapter<AskPostDatumAdapter.PostDatumViewHolder> {


    private static final int ITEM = 0;
    private static final int LOADING = 1;

    private List<PostDatum> postList;
    private Context context;

    private boolean isLoadingAdded = false;
    private boolean retryPageLoad = false;

    private PaginationAdapterCallback mCallback;

    private String errorMsg;

    private ItemClickListener clickListener;

    public AskPostDatumAdapter(Context context, PaginationAdapterCallback callback) {
        this.context = context;
        this.mCallback = callback;
        postList = new ArrayList<>();
    }

    public List<PostDatum> getPostList() {
        return postList;
    }

    public void setPostList(List<PostDatum> postList) {
        this.postList = postList;
    }

    @Override
    public AskPostDatumAdapter.PostDatumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AskPostDatumAdapter.PostDatumViewHolder viewHolder = new PostDatumViewHolder(new View(RoomRentApplication.getContext()));
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());



        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;
            case LOADING:
                View v2 = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private AskPostDatumAdapter.PostDatumViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        AskPostDatumAdapter.PostDatumViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.ask_item, parent, false);
        viewHolder = new PostDatumViewHolder(v1);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PostDatumViewHolder holder, final int position) {

        PostDatum postDatum = postList.get(position);

        switch (getItemViewType(position)) {

            case ITEM:
                    if (position % 2 == 0) {
                        holder.rootView.setBackgroundResource(R.color.recycler_even);
                    } else {
                        holder.rootView.setBackgroundResource(R.color.recycler_odd);
                    }

                    holder.v.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (clickListener != null) clickListener.onClick(v, position);

                        }
                    });
                    holder.askedBy.setText(postDatum.getUser().getName());
                    holder.title.setText(postDatum.getTitle());
                    holder.address.setText(postDatum.getAddress());
                    holder.noOfRoom.setText(postDatum.getId() + "");

                    String imageurl = "http://192.168.0.143:81/api/v1/getfile/" + postDatum.getUser().getProfileImage();

                    Picasso.with(context).setLoggingEnabled(true);
                    PicassoAuthenticationInterceptor.getPicasso().load(imageurl)
                            .placeholder(R.drawable.logo)
                            .error(R.drawable.choose_image_icon)
                            .transform(new RoundedTransformation(50, 4))
                            .resizeDimen(R.dimen.list_detail_image_size, R.dimen.list_detail_image_size)
                            .centerCrop()
                            .into(holder.profilePicture);
                break;

            case LOADING:
                LoadingVH loadingVH = (LoadingVH) holder;

                if (retryPageLoad) {
                    loadingVH.mErrorLayout.setVisibility(View.VISIBLE);
                    loadingVH.mProgressBar.setVisibility(View.VISIBLE);

                    loadingVH.mErrorTxt.setText(
                            errorMsg != null ?
                                    errorMsg :
                                    context.getString(R.string.error_msg_unknown));

                } else {
                    loadingVH.mErrorLayout.setVisibility(View.GONE);
                    loadingVH.mProgressBar.setVisibility(View.GONE);
                }

                break;
        }

    }

    @Override
    public int getItemCount() {
        return postList == null ? 0 : postList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == postList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }
     /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(PostDatum r) {
        postList.add(r);
        notifyItemInserted(postList.size() - 1);
    }

    public void addAll(List<PostDatum> movePostDatums) {
        for (PostDatum PostDatum : movePostDatums) {
            add(PostDatum);
        }
    }

    public void remove(PostDatum r) {
        int position = postList.indexOf(r);
        if (position > -1) {
            postList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new PostDatum());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = postList.size() - 1;
        PostDatum PostDatum = getItem(position);

        if (PostDatum != null) {
            postList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public PostDatum getItem(int position) {
        return postList.get(position);
    }

    /**
     * Displays Pagination retry footer view along with appropriate errorMsg
     *
     * @param show
     * @param errorMsg to display if page load fails
     */
    public void showRetry(boolean show, @Nullable String errorMsg) {
        retryPageLoad = show;
        notifyItemChanged(postList.size() - 1);

        if (errorMsg != null) this.errorMsg = errorMsg;
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }


   /*
   View Holders
   _________________________________________________________________________________________________
    */

    public class PostDatumViewHolder extends RecyclerView.ViewHolder{
        public LinearLayout rootView;//newly added field
        private TextView askedBy, title, address, noOfRoom;
        private ImageView profilePicture;
        View v;


        private PostDatumViewHolder(View view) {
            super(view);

            rootView = (LinearLayout) view.findViewById(R.id.rootView);
            askedBy = (TextView) view.findViewById(R.id.askedBy);
            title = (TextView) view.findViewById(R.id.title);
            address = (TextView) view.findViewById(R.id.address);
            noOfRoom = (TextView) view.findViewById(R.id.noOfRoom);
            profilePicture = (ImageView) view.findViewById(profilePictureHolder);
            v = view;

        }
    }


    protected class LoadingVH extends AskPostDatumAdapter.PostDatumViewHolder implements View.OnClickListener {
        private ProgressBar mProgressBar;
        private ImageButton mRetryBtn;
        private TextView mErrorTxt;
        private LinearLayout mErrorLayout;

        public LoadingVH(View itemView) {
            super(itemView);

            mProgressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);
            mRetryBtn = (ImageButton) itemView.findViewById(R.id.loadmore_retry);
            mErrorTxt = (TextView) itemView.findViewById(R.id.loadmore_errortxt);
            mErrorLayout = (LinearLayout) itemView.findViewById(R.id.loadmore_errorlayout);

            mRetryBtn.setOnClickListener(this);
            mErrorLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loadmore_retry:
                case R.id.loadmore_errorlayout:

                    showRetry(false, null);
                    mCallback.retryPageLoad();

                    break;
            }
        }
    }
}