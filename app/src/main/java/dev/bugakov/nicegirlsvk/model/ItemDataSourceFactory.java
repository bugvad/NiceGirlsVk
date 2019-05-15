package dev.bugakov.nicegirlsvk.model;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;

public class ItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, Item>> itemLiveDataSource = new MutableLiveData<>();

    public static ItemDataSource getItemDataSource() {
        return itemDataSource;
    }

    private static ItemDataSource itemDataSource;

    @Override
    public DataSource<Integer, Item> create() {
        itemDataSource = new ItemDataSource();

        itemLiveDataSource.postValue(itemDataSource);

        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, Item>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}