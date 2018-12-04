package com.suleiman.pagination.Presenter;

import android.content.Context;

import com.suleiman.pagination.INetworkInterface.INetWork;
import com.suleiman.pagination.PresenterInterface.IMoviePresnter;
import com.suleiman.pagination.ViewInterface.IMovieView;
import com.suleiman.pagination.ViewInterface.INetworkMethodList;

public class MoviePresenter  implements IMoviePresnter,INetWork{
    Context context;
    INetworkMethodList iNetworkMethodList;
    IMovieView iMovieView;

    MoviePresenter( Context context){
        this.context=context;
    }

    @Override
    public void getMovie() {
        iNetworkMethodList.getMovieList();
    }


    @Override
    public void onSuccess() {
        iMovieView.onSuccess();
    }

    @Override
    public void onFailure() {
        iMovieView.onFailure();
    }
}
