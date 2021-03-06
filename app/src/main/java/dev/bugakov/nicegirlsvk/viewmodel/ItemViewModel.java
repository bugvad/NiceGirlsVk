package dev.bugakov.nicegirlsvk.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import dev.bugakov.nicegirlsvk.model.ItemDataSource;
import dev.bugakov.nicegirlsvk.model.ItemDataSourceFactory;
import dev.bugakov.nicegirlsvk.model.Item;

import static dev.bugakov.nicegirlsvk.model.Constant.PAGE_SIZE;

public class ItemViewModel extends ViewModel {

    public LiveData<PagedList<Item>> getItemPagedList() {
        return itemPagedList;
    }

    public LiveData<PageKeyedDataSource<Integer, Item>> getLiveDataSource() {
        return liveDataSource;
    }

    private LiveData<PagedList<Item>> itemPagedList;
    private LiveData<PageKeyedDataSource<Integer, Item>> liveDataSource;

    public ItemViewModel() {
        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory();

        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(PAGE_SIZE).build();

        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, pagedListConfig))
                .build();
    }
}
