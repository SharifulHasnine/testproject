package com.suleiman.pagination.Presenter;

import android.content.Context;
import android.util.Log;

import com.suleiman.pagination.NetworkModule.MovieApi;
import com.suleiman.pagination.NetworkModule.MovieService;
import com.suleiman.pagination.PresenterInterface.IMoviePresnter;
import com.suleiman.pagination.ViewInterface.IMovieView;
import com.suleiman.pagination.models.GetMoviesResponseObject;
import com.suleiman.pagination.utils.PaginationScrollListener;

import java.io.Console;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviePresenter  implements IMoviePresnter {
    Context context;
    private MovieService movieService = MovieApi.getClient().create(MovieService.class);
    ;
    IMovieView iMovieView;
    String api_key = "4bc53930f5725a4838bf943d02af6aa9";
    int currentPage;

    public MoviePresenter(Context context, IMovieView iMovieView) {
        this.context = context;
        this.iMovieView = iMovieView;
    }

    @Override
    public void getMovie(int currentPage) {
        System.out.println("getMovie");
        this.currentPage = currentPage;
        callTopRatedMoviesApi().enqueue(new Callback<GetMoviesResponseObject>() {
            @Override
            public void onResponse(Call<GetMoviesResponseObject> call, Response<GetMoviesResponseObject> response) {
                // Got data. Send it to adapter

                iMovieView.onSuccessOfTrending(response);

            }

            @Override
            public void onFailure(Call<GetMoviesResponseObject> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
                iMovieView.onFailureOfTrending();
            }
        });
    }


    @Override
    public void getNowPlayingMovie(int currentPage) {
        System.out.println("getNowPlayingMovie");
        this.currentPage = currentPage;
        callNowPlayingMoviesApi().enqueue(new Callback<GetMoviesResponseObject>() {
            @Override
            public void onResponse(Call<GetMoviesResponseObject> call, Response<GetMoviesResponseObject> response) {
                // Got data. Send it to adapter
                iMovieView.onSuccessOfNowPlaying(response);

            }

            @Override
            public void onFailure(Call<GetMoviesResponseObject> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
                iMovieView.onFailureOfNowPlaying();
            }
        });
    }

    @Override
    public void getnUpComingMovie(int currentPage) {
        System.out.println("getnUpComingMovie");
        this.currentPage = currentPage;
        callUpcComingMoviesApi().enqueue(new Callback<GetMoviesResponseObject>() {
            @Override
            public void onResponse(Call<GetMoviesResponseObject> call, Response<GetMoviesResponseObject> response) {
                // Got data. Send it to adapter
                iMovieView.onSuccessOfUpComing(response);

            }

            @Override
            public void onFailure(Call<GetMoviesResponseObject> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
                iMovieView.onFailureOfUpComing();
            }
        });
    }


    @Override
    public void getSearchMovie(String query) {
        this.currentPage = currentPage;
        callSearchMoviesApi(query).enqueue(new Callback<GetMoviesResponseObject>() {
            @Override
            public void onResponse(Call<GetMoviesResponseObject> call, Response<GetMoviesResponseObject> response) {
                // Got data. Send it to adapter
                iMovieView.onSuccessOfSearch(response);

            }

            @Override
            public void onFailure(Call<GetMoviesResponseObject> call, Throwable t) {
                t.printStackTrace();
                // TODO: 08/11/16 handle failure
                iMovieView.onFailureOfSearch();
            }
        });
    }


    /**
     * Performs a Retrofit call to the top rated movies API.
     * Same API call for Pagination.
     * As {@link #currentPage} will be incremented automatically
     * by @{@link PaginationScrollListener} to load next page.
     */
    private Call<GetMoviesResponseObject> callTopRatedMoviesApi() {
        return movieService.getTopRatedMovies(api_key,
                "en_US",
                currentPage
        );
    }

    private Call<GetMoviesResponseObject> callNowPlayingMoviesApi() {
        return movieService.getNowPlayingMovie(api_key,
                "en_US",
                currentPage);

    }


    private Call<GetMoviesResponseObject> callUpcComingMoviesApi() {
        return movieService.getUpcomingMovie(api_key,
                "en_US",
                currentPage);
    }

    private Call<GetMoviesResponseObject> callSearchMoviesApi(String query) {
        return movieService.getMovieFromSearchString(api_key,
                "en_US",
                query);
    }
}

