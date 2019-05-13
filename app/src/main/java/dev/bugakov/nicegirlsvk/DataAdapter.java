package dev.bugakov.nicegirlsvk;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private LayoutInflater inflater;
    private List<Item> itemsList;

    Context context;

    DataAdapter(Context context, List<Item> itemsList) {
        this.itemsList = itemsList;
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DataAdapter.ViewHolder holder, int position) {
        Item item = itemsList.get(position);

        Picasso.with(context)
                .load(item.getName())
                .placeholder(R.drawable.ic_ab_app)
                .error(R.drawable.ic_launcher_background)
                .into(holder.nameView);
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        final ImageView nameView;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.image);
        }
    }
}