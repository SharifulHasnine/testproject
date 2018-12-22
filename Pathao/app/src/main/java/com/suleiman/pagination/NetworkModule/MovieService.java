package com.suleiman.pagination.NetworkModule;


import com.suleiman.pagination.models.GetMoviesResponseObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;



public interface MovieService {

    @GET("movie/top_rated")
    Call<GetMoviesResponseObject> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
                @Query("page") int pageIndex
    );

    @GET("movie/now_playing")
    Call<GetMoviesResponseObject> getNowPlayingMovie(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int pageIndex
    );

    @GET("movie/upcoming")
    Call<GetMoviesResponseObject> getUpcomingMovie(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int pageIndex
    );

    @GET("movie/upcoming")
    Call<GetMoviesResponseObject> getMovieFromSearchString (
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("query") String queryString
    );

}
