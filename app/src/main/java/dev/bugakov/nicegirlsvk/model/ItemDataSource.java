package dev.bugakov.nicegirlsvk.model;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import dev.bugakov.nicegirlsvk.network.Repository;

import static dev.bugakov.nicegirlsvk.model.Constant.LOAD_AFTER_ID;
import static dev.bugakov.nicegirlsvk.model.Constant.LOAD_BEFORE_ID;
import static dev.bugakov.nicegirlsvk.model.Constant.LOAD_INITIAL_ID;
import static dev.bugakov.nicegirlsvk.model.Constant.PAGE_SIZE;
import static dev.bugakov.nicegirlsvk.network.Repository.finishFlow1;
import static dev.bugakov.nicegirlsvk.network.Repository.finishFlow2;
import static dev.bugakov.nicegirlsvk.network.Repository.finishFlow3;

public class ItemDataSource extends PageKeyedDataSource<Integer, Item> {


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull final LoadInitialCallback<Integer, Item> callback) {

        finishFlow2(Repository.multiobservable(Repository.generateIdsRequest(PAGE_SIZE,5, LOAD_INITIAL_ID, Constant.getFrom(), Constant.getTo())),
                callback);

    }

    @Override
    public void loadBefore(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {

        finishFlow1(Repository.multiobservable(Repository.generateIdsRequest(PAGE_SIZE, params.key, LOAD_BEFORE_ID, Constant.getFrom(), Constant.getTo())),
                callback, params.key);


    }

    @Override
    public void loadAfter(@NonNull final LoadParams<Integer> params, @NonNull final LoadCallback<Integer, Item> callback) {

        finishFlow3(Repository.multiobservable(Repository.generateIdsRequest(PAGE_SIZE, params.key, LOAD_AFTER_ID, Constant.getFrom(), Constant.getTo())),
                callback, params.key);
    }
}