package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

/**
 * Created by Dwi Enggar on 31/01/2017.
 */

public class BabyCareAdapter extends RecyclerView.Adapter<BabyCareAdapter.ViewHolder> {
    List<Barang> listBabyCare;
    View layout;
    private Context ctx;

    public BabyCareAdapter(List<Barang> param) {
        listBabyCare = param;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category, parent, false);
        ctx = parent.getContext();

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(listBabyCare.get(position).getProductname());
        holder.tvPrice.setText(listBabyCare.get(position).getPrice());
        holder.tvSeller.setText(listBabyCare.get(position).getUsername());
        holder.tvDate.setText(listBabyCare.get(position).getDate());
        PicassoClient.downloadImage(ctx, listBabyCare.get(position).getImg(), holder.ivImage);

    }

    @Override
    public int getItemCount() {
        if (listBabyCare != null) {
            return listBabyCare.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTitle, tvPrice, tvSeller;
        ImageView ivImage;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvSeller = (TextView) itemView.findViewById(R.id.tvSeller);
            ivImage = (ImageView) itemView.findViewById(R.id.ivImage);
        }
    }
}