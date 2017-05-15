package com.example.android.roomrent.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.roomrent.Activity.AskPostDetail;
import com.example.android.roomrent.Activity.R;
import com.example.android.roomrent.Activity.RoomRentApplication;
import com.example.android.roomrent.Adapter.OfferPostDatumAdapter;
import com.example.android.roomrent.Helper.CustomViewPager;
import com.example.android.roomrent.Helper.ItemClickListener;
import com.example.android.roomrent.Helper.PaginationAdapterCallback;
import com.example.android.roomrent.Helper.PaginationScrollListener;
import com.example.android.roomrent.Model.PostDatum;
import com.example.android.roomrent.Model.PostStatistics;
import com.example.android.roomrent.Rest.RetrofitInterface;
import com.example.android.roomrent.Rest.RetrofitService;

import java.util.List;
import java.util.concurrent.TimeoutException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OfferFragment extends Fragment implements PaginationAdapterCallback,ItemClickListener {

    private final static String TAG = OfferFragment.class.getName();


    OfferPostDatumAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    //private List<PostDatum> postList = new ArrayList<>();
    private RecyclerView recyclerView;

    CustomViewPager customViewPager;

    ProgressBar progressBar;
    LinearLayout errorLayout;
    Button btnRetry;
    TextView txtError;

    RetrofitInterface postService;

    private static final int COUNT_START = 0;

    private boolean isLoading = false;
    private boolean isLastCount = false;

    private int TOTAL_COUNT;
    private int currentCount = COUNT_START;

    public OfferFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // set the main recyclerview view in the layout
        View rootView = inflater.inflate(R.layout.fragment_ask_or_offer, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        progressBar = (ProgressBar) rootView.findViewById(R.id.main_progress);
        errorLayout = (LinearLayout) rootView.findViewById(R.id.error_layout);
        btnRetry = (Button) rootView.findViewById(R.id.error_btn_retry);
        txtError = (TextView) rootView.findViewById(R.id.error_txt_cause);

        adapter = new OfferPostDatumAdapter(rootView.getContext(),this);

        linearLayoutManager = new LinearLayoutManager(rootView.getContext(),
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setAdapter(adapter);

        adapter.setClickListener(this);

        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if (!isLastCount) {
                    loadNextPage();
                }
            }

            @Override
            public int getTotalCount() {
                return TOTAL_COUNT;
            }

            @Override
            public boolean isLastCount() {
                return isLastCount;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });

        postService = RetrofitService.getClient();
        loadFirstPage();

        btnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadFirstPage();
            }
        });

        return rootView;
    }

    private void loadFirstPage() {
        Log.d(TAG, "loadFirstPage: ");

        // To ensure list is visible when retry button in error view is clicked
        hideErrorView();

        callPostApi().enqueue(new Callback<PostStatistics>() {
            @Override
            public void onResponse(Call<PostStatistics> call, Response<PostStatistics> response) {
                // Got data. Send it to adapter

                hideErrorView();
                TOTAL_COUNT = response.body().getTotal();
                currentCount = response.body().getOffset();
                isLastCount = response.body().getLastPage();

                List<PostDatum> results = fetchResults(response);
                progressBar.setVisibility(View.GONE);
                adapter.addAll(results);

                if (currentCount <= TOTAL_COUNT) adapter.addLoadingFooter();
                else isLastCount = true;
            }

            @Override
            public void onFailure(Call<PostStatistics> call, Throwable t) {
                t.printStackTrace();
                showErrorView(t);
            }
        });
    }

    /**
     * @param response extracts List<{@link PostDatum>} from response
     * @return
     */
    private List<PostDatum> fetchResults(Response<PostStatistics> response) {
        PostStatistics postStatistics = response.body();
        return postStatistics.getData();
    }

    private void showErrorView(Throwable t) {
        if (errorLayout.getVisibility() == View.GONE) {
            errorLayout.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            txtError.setText(fetchErrorMessage(t));
        }
    }

    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentCount} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next count.
     */
    private Call<PostStatistics> callPostApi() {
        return postService.getNewPost(
                "Bearer " + RoomRentApplication.getApiToken(),
                "offers",
                null,
                null,
               currentCount);
    }

    @Override
    public void retryPageLoad() {
        loadNextPage();
    }

    private void loadNextPage() {

        Log.d(TAG, "loadNextCount: " + currentCount);

        callPostApi().enqueue(new Callback<PostStatistics>() {
            @Override
            public void onResponse(Call<PostStatistics> call, Response<PostStatistics> response) {
                adapter.removeLoadingFooter();
                isLoading = false;


                TOTAL_COUNT = response.body().getTotal();
                currentCount = response.body().getOffset();
                isLastCount = response.body().getLastPage();

                List<PostDatum> results = fetchResults(response);
                adapter.addAll(results);

                if (currentCount != TOTAL_COUNT) adapter.addLoadingFooter();
                else isLastCount = true;
            }

            @Override
            public void onFailure(Call<PostStatistics> call, Throwable t) {
                t.printStackTrace();
                adapter.showRetry(true, fetchErrorMessage(t));
            }
        });
    }

    /**
     * @param t to identify the type of error
     * @return appropriate error message
     */
    private String fetchErrorMessage(Throwable t) {

        String errorMsg = getResources().getString(R.string.error_msg_unknown);

        if (!isNetworkConnected()) {
            errorMsg = getResources().getString(R.string.no_internet_connection_message);
        } else if (t instanceof TimeoutException) {
            errorMsg = getResources().getString(R.string.error_msg_timeout);
        }

        return errorMsg;
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    private void hideErrorView() {
        if (errorLayout.getVisibility() == View.VISIBLE) {
            errorLayout.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Remember to add android.permission.ACCESS_NETWORK_STATE permission.
     *
     * @return
     */
    private boolean isNetworkConnected() {
        return RoomRentApplication.isNetworkConnected();
    }

    @Override
    public void onClick(View view, int position) {
        Intent intent = new Intent(RoomRentApplication.getContext(), AskPostDetail.class);
        intent.putExtra("askPost", adapter.getItem(position));
        startActivity(intent);
    }
}
