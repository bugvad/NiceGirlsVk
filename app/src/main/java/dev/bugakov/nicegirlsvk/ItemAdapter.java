package dev.bugakov.nicegirlsvk;

import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ItemAdapter extends PagedListAdapter<ItemQuestion, ItemAdapter.ItemViewHolder> {

    //адаптер данных для Paging List
    private Context mCtx;

    ItemAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mCtx).inflate(R.layout.list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        ItemQuestion item = getItem(position);

        if (item != null) {
            Picasso.with(mCtx)
                    .load(item.title)
                    .placeholder(R.drawable.ic_ab_app)
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.imageView);
        }
    }

    private static DiffUtil.ItemCallback<ItemQuestion> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<ItemQuestion>() {
                @Override
                public boolean areItemsTheSame(ItemQuestion oldItem, ItemQuestion newItem) {
                    return oldItem.title.equals(newItem.title);
                }

                @Override
                public boolean areContentsTheSame(ItemQuestion oldItem, ItemQuestion newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}