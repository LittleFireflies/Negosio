package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

/**
 * Created by LittleFireflies on 28-Jan-17.
 */

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder>{
    List<Barang> listCamera;
    View layout;
    private Context ctx;

    public CameraAdapter(List<Barang> param){
        listCamera = param;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category, parent, false);
        ctx = parent.getContext();

        return new ViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(listCamera.get(position).getProductname());
        holder.tvPrice.setText(listCamera.get(position).getPrice());
        holder.tvSeller.setText(listCamera.get(position).getUsername());
        holder.tvDate.setText(listCamera.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        if(listCamera != null){
            return listCamera.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTitle, tvPrice, tvSeller;

        public ViewHolder(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvSeller = (TextView) itemView.findViewById(R.id.tvSeller);
        }
    }
}
