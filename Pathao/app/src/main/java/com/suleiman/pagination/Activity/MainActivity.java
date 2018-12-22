package com.suleiman.pagination.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.suleiman.pagination.Adaptar.NowPlayingAdapter;
import com.suleiman.pagination.Adaptar.PaginationAdapter;
import com.suleiman.pagination.Adaptar.TrendingPaginationAdapter;
import com.suleiman.pagination.Adaptar.UpcomingMovieAdapter;
import com.suleiman.pagination.Presenter.MoviePresenter;
import com.suleiman.pagination.R;
import com.suleiman.pagination.ViewInterface.IMovieView;
import com.suleiman.pagination.models.MovieObject;
import com.suleiman.pagination.models.GetMoviesResponseObject;
import com.suleiman.pagination.utils.PaginationScrollListener;

import java.io.Console;
import java.util.List;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements IMovieView {

    private static final String TAG = "MainActivity";

    PaginationAdapter adapter;
    TrendingPaginationAdapter trendingPaginationAdapter;
    NowPlayingAdapter nowPlayingAdapter;
    UpcomingMovieAdapter upcomingMovieAdapter;
    LinearLayoutManager linearLayoutManager;
    public CardView cardView1;
    public CardView cardView2;
    public CardView cardView3;


    LinearLayoutManager linearLayoutManagerTrending;
    LinearLayoutManager linearLayoutManagerNowPlaying;
    LinearLayoutManager linearLayoutManagerUpcoming;

    MoviePresenter moviePresenter;

    RecyclerView searchList;
    RecyclerView trendingRecylerView;
    RecyclerView nowPlaying;
    RecyclerView upcoming;




    ProgressBar progressBar;

    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPageTrending = false;
    private boolean isLastPageNowPlaying = false;
    private boolean isLastPageUpcoming = false;
    private boolean isLastPage = false;

    // limiting to 5 for this tutorial, since total pages in actual API is very large. Feel free to modify.
    private int TOTAL_PAGES = 5;
    private int currentPage= PAGE_START;
    private int currentPageUpComing = PAGE_START;
    private int currentPagenowPlayiing = PAGE_START;
    private int currentPageTrending = PAGE_START;
    private SearchView searchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        trendingRecylerView =(RecyclerView) findViewById(R.id.trendingList);
        nowPlaying=(RecyclerView) findViewById(R.id.nowshowing);
        upcoming =(RecyclerView) findViewById(R.id.upcoming);
        searchList = (RecyclerView) findViewById(R.id.main_recycler);


        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        moviePresenter=new MoviePresenter(this,this);
        adapter = new PaginationAdapter(this);
        trendingPaginationAdapter = new TrendingPaginationAdapter(this);
        nowPlayingAdapter = new NowPlayingAdapter(this);
        upcomingMovieAdapter = new UpcomingMovieAdapter(this);
        cardView1=(CardView)findViewById(R.id.cardView1);
        cardView2=(CardView)findViewById(R.id.cardView2);
        cardView3=(CardView)findViewById(R.id.cardView3);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        linearLayoutManagerTrending = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerNowPlaying = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        linearLayoutManagerUpcoming = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);


        searchList.setLayoutManager(linearLayoutManager);

        searchList.setItemAnimator(new DefaultItemAnimator());

        searchList.setAdapter(adapter);



        trendingRecylerView.setLayoutManager(linearLayoutManagerTrending);

        trendingRecylerView.setItemAnimator(new DefaultItemAnimator());

        trendingRecylerView.setAdapter(trendingPaginationAdapter);

        trendingRecylerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManagerTrending) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPageTrending += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPageOfTrending();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageTrending;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });


        nowPlaying.setLayoutManager(linearLayoutManagerNowPlaying);

        nowPlaying.setItemAnimator(new DefaultItemAnimator());

        nowPlaying.setAdapter(nowPlayingAdapter);

        nowPlaying.addOnScrollListener(new PaginationScrollListener(linearLayoutManagerNowPlaying) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPagenowPlayiing += 1;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPageOfNowPlaying();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageNowPlaying;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });



        upcoming.setLayoutManager(linearLayoutManagerUpcoming);

        upcoming.setItemAnimator(new DefaultItemAnimator());

        upcoming.setAdapter(upcomingMovieAdapter);

        upcoming.addOnScrollListener(new PaginationScrollListener(linearLayoutManagerUpcoming) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPageUpComing += 1;

                // mocking network delay for API call
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadNextPageOfUpComing();
                    }
                }, 1000);
            }

            @Override
            public int getTotalPageCount() {
                return TOTAL_PAGES;
            }

            @Override
            public boolean isLastPage() {
                return isLastPageUpcoming;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });



        //init service and load data

        loadFirstPage();

    }

    private void loadNextPageOfUpComing() {
        Log.d(TAG, "loadNextPage: " + currentPageUpComing);
        moviePresenter.getnUpComingMovie(currentPageUpComing);
    }

    private void loadNextPageOfNowPlaying() {
        Log.d(TAG, "loadNextPage: " + currentPagenowPlayiing);
        moviePresenter.getNowPlayingMovie(currentPagenowPlayiing);
    }

    private void loadNextPageOfTrending() {
        Log.d(TAG, "loadNextPage: " + currentPageTrending);
        moviePresenter.getMovie(currentPageTrending);
    }




    private void loadFirstPage() {
        moviePresenter.getMovie(currentPageTrending);
        moviePresenter.getNowPlayingMovie(currentPagenowPlayiing);
        moviePresenter.getnUpComingMovie(currentPageUpComing);

    }

    /**
     * @param response extracts List<{@link MovieObject >} from response
     * @return
     */
    private List<MovieObject> fetchResults(Response<GetMoviesResponseObject> response) {
        GetMoviesResponseObject getMoviesResponseObject = response.body();
        return getMoviesResponseObject.getResults();
    }



    @Override
    public void onSuccessOfTrending(Response<GetMoviesResponseObject> response) {
        if(currentPageTrending==1){
            List<MovieObject> results = fetchResults(response);
            System.out.println(results.size()+"trending");
            progressBar.setVisibility(View.GONE);
            trendingPaginationAdapter.addAll(results);

            if (currentPageTrending <= TOTAL_PAGES) trendingPaginationAdapter.addLoadingFooter();
            else isLastPageTrending = true;
        }else {
            trendingPaginationAdapter.removeLoadingFooter();
            isLoading = false;

            List<MovieObject> results = fetchResults(response);
            trendingPaginationAdapter.addAll(results);

            if (currentPageTrending != TOTAL_PAGES) trendingPaginationAdapter.addLoadingFooter();
            else isLastPageTrending = true;
        }
    }

    @Override
    public void onFailureOfTrending() {
        Toast.makeText(this,"Movie List get fail",Toast.LENGTH_SHORT);
    }

    @Override
    public void onSuccessOfNowPlaying(Response<GetMoviesResponseObject> response) {
        if(currentPagenowPlayiing==1){
            List<MovieObject> results = fetchResults(response);
            System.out.println(results.size()+"now Play");
            progressBar.setVisibility(View.GONE);
            nowPlayingAdapter.addAll(results);

            if (currentPagenowPlayiing <= TOTAL_PAGES) nowPlayingAdapter.addLoadingFooter();
            else isLastPageNowPlaying = true;
        }else {
            nowPlayingAdapter.removeLoadingFooter();
            isLoading = false;

            List<MovieObject> results = fetchResults(response);
            nowPlayingAdapter.addAll(results);

            if (currentPagenowPlayiing != TOTAL_PAGES) nowPlayingAdapter.addLoadingFooter();
            else isLastPageNowPlaying = true;
        }
    }

    @Override
    public void onFailureOfNowPlaying() {
        Toast.makeText(this,"Movie List get fail",Toast.LENGTH_SHORT);
    }

    @Override
    public void onSuccessOfUpComing(Response<GetMoviesResponseObject> response) {
        if(currentPageUpComing==1){
            List<MovieObject> results = fetchResults(response);
            System.out.println(results.size()+"currentPageUpComing");
            progressBar.setVisibility(View.GONE);
            upcomingMovieAdapter.addAll(results);

            if (currentPageUpComing <= TOTAL_PAGES) upcomingMovieAdapter.addLoadingFooter();
            else isLastPageUpcoming = true;
        }else {
            upcomingMovieAdapter.removeLoadingFooter();
            isLoading = false;

            List<MovieObject> results = fetchResults(response);
            upcomingMovieAdapter.addAll(results);

            if (currentPageUpComing != TOTAL_PAGES) upcomingMovieAdapter.addLoadingFooter();
            else isLastPageUpcoming = true;
        }
    }

    @Override
    public void onFailureOfUpComing() {
        Toast.makeText(this,"Movie List get fail",Toast.LENGTH_SHORT);
    }

    @Override
    public void onSuccessOfSearch(Response<GetMoviesResponseObject> response) {
        if(currentPage==1){
            List<MovieObject> results = fetchResults(response);
            progressBar.setVisibility(View.GONE);
            adapter.addAll(results);

            if (currentPage <= TOTAL_PAGES) adapter.addLoadingFooter();
            else isLastPage = true;
        }else {
            adapter.removeLoadingFooter();
            isLoading = false;

            List<MovieObject> results = fetchResults(response);
            adapter.addAll(results);

            if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
            else isLastPage = true;
        }
    }

    @Override
    public void onFailureOfSearch() {
        Toast.makeText(this,"Movie List get fail",Toast.LENGTH_SHORT);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {


            @Override
            public boolean onQueryTextSubmit(String query) {
                searchList.setVisibility(View.VISIBLE);
                moviePresenter.getSearchMovie(query);
                cardView1.setVisibility(View.GONE);
                cardView2.setVisibility(View.GONE);
                cardView3.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if(query==null || query.equals("") || query.length()==0){
                    searchList.setVisibility(View.GONE);

                    cardView1.setVisibility(View.VISIBLE);
                    cardView2.setVisibility(View.VISIBLE);
                    cardView3.setVisibility(View.VISIBLE);
                }

                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        cardView1.setVisibility(View.VISIBLE);
        cardView2.setVisibility(View.VISIBLE);
        cardView3.setVisibility(View.VISIBLE);
        searchList.setVisibility(View.GONE);
        if (!searchView.isIconified()) {

            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
}
