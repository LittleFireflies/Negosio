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
 * Created by Dwi Enggar on 31/01/2017.
 */

public class HvEquipmentAdapter extends RecyclerView.Adapter<HvEquipmentAdapter.ViewHolder> {
    List<Barang> listHvEquipm;
    View layout;
    private Context ctx;

    public HvEquipmentAdapter(List<Barang> param) {
        listHvEquipm = param;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category, parent, false);
        ctx = parent.getContext();

        return new ViewHolder(layout);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTitle.setText(listHvEquipm.get(position).getProductname());
        holder.tvPrice.setText(listHvEquipm.get(position).getPrice());
        holder.tvSeller.setText(listHvEquipm.get(position).getUsername());
        holder.tvDate.setText(listHvEquipm.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        if (listHvEquipm != null) {
            return listHvEquipm.size();
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
