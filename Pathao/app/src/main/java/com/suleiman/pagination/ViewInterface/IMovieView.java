package com.suleiman.pagination.ViewInterface;

import com.suleiman.pagination.models.GetMoviesResponseObject;

import retrofit2.Response;

public interface IMovieView {
     void onSuccessOfTrending(Response<GetMoviesResponseObject> response);
     void onFailureOfTrending();

     void onSuccessOfNowPlaying(Response<GetMoviesResponseObject> response);
     void onFailureOfNowPlaying();

     void onSuccessOfUpComing(Response<GetMoviesResponseObject> response);
     void onFailureOfUpComing();


     void onSuccessOfSearch(Response<GetMoviesResponseObject> response);
     void onFailureOfSearch();

}
