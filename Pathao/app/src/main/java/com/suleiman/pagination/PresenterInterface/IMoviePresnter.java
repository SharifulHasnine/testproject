package com.suleiman.pagination.PresenterInterface;

public interface IMoviePresnter {
     void getMovie(int currentPage);
     void getNowPlayingMovie(int currentPage);
     void getnUpComingMovie(int currentPage);
     void getSearchMovie(String query);

}
