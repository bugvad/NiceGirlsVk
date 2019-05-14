package dev.bugakov.nicegirlsvk.model;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import dev.bugakov.nicegirlsvk.network.Repository;

import static dev.bugakov.nicegirlsvk.network.Repository.finishFlow1;
import static dev.bugakov.nicegirlsvk.network.Repository.finishFlow2;
import static dev.bugakov.nicegirlsvk.network.Repository.finishFlow3;

public class ItemDataSource extends PageKeyedDataSource<Integer, Item> {

    public static final int PAGE_SIZE = 5;
    private static final int FIRST_PAGE = 1;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Item> callback) {
        int methodCode = 0;

        Log.i("bs: ", "error");
        finishFlow2(Repository.multiobservable(Repository.generateIdsRequest(PAGE_SIZE,5, methodCode)),
                callback);


    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {

        Log.i("bs: ", "inside loadBefore");

        int methodCode = -1;

        finishFlow1(Repository.multiobservable(Repository.generateIdsRequest(PAGE_SIZE, params.key, methodCode)),
                callback, params.key);


    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {
        int methodCode = 1;

        finishFlow3(Repository.multiobservable(Repository.generateIdsRequest(PAGE_SIZE, params.key, methodCode)),
                callback, params.key);
    }
}