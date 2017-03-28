package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

/**
 * Created by LittleFireflies on 12-Feb-17.
 */

public class MyItemAdapter extends RecyclerView.Adapter<MyItemAdapter.ViewHolder>{
    private View layout;
    private Context ctx;
    private DatabaseReference dbRef;
    private ArrayList<Barang> listItem;
    private Dialog dialogDetail;
    private TextView dialog_tvTitle, dialog_tvPrice, dialog_tvType, dialog_tvCategory, dialog_tvDesc, dialog_tvDate, dialog_tvUsername;
    private ImageView dialog_ivItem, dialog_ivBack;

    public MyItemAdapter(ArrayList<Barang> param){
        listItem = param;
    }

    @Override
    public MyItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_item, parent, false);
        ctx = parent.getContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        setDialog();
        return new MyItemAdapter.ViewHolder(layout);
    }

    private void setDialog() {
        dialogDetail = new Dialog(ctx);
        dialogDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDetail.setContentView(R.layout.detail_my_item);
        dialogDetail.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDetail.setCanceledOnTouchOutside(false);
        dialogDetail.onBackPressed();

        dialog_ivBack = (ImageView) dialogDetail.findViewById(R.id.ivBack);
        dialog_tvTitle = (TextView) dialogDetail.findViewById(R.id.tvTitle);
        dialog_tvPrice = (TextView) dialogDetail.findViewById(R.id.tvPrice);
        dialog_tvType = (TextView) dialogDetail.findViewById(R.id.tvType);
        dialog_tvCategory = (TextView) dialogDetail.findViewById(R.id.tvCategory);
        dialog_tvDesc = (TextView) dialogDetail.findViewById(R.id.tvDesc);
        dialog_tvUsername = (TextView) dialogDetail.findViewById(R.id.tvUsername);
        dialog_tvDate = (TextView) dialogDetail.findViewById(R.id.tvDate);
        dialog_ivItem = (ImageView) dialogDetail.findViewById(R.id.ivDetail);
    }

    @Override
    public void onBindViewHolder(MyItemAdapter.ViewHolder holder, final int position) {
        holder.tvTitle.setText(listItem.get(position).getProductname());
        holder.tvPrice.setText(listItem.get(position).getPrice());
        holder.tvType.setText(listItem.get(position).getType());
        holder.tvCategory.setText(listItem.get(position).getCategory());
        holder.tvDate.setText(listItem.get(position).getDate());
        PicassoClient.downloadImage(ctx, listItem.get(position).getImg(), holder.ivItem);
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDetail.show();
                dialog_tvTitle.setText(listItem.get(position).getProductname());
                dialog_tvPrice.setText(listItem.get(position).getPrice());
                dialog_tvType.setText(listItem.get(position).getType());
                dialog_tvCategory.setText(listItem.get(position).getCategory());
                dialog_tvDesc.setText(listItem.get(position).getDescription());
                dialog_tvDate.setText(listItem.get(position).getDate());
                dialog_tvUsername.setText(listItem.get(position).getUsername());
                PicassoClient.downloadImage(ctx, listItem.get(position).getImg(), dialog_ivItem);
                dialog_ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDetail.dismiss();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        if(listItem != null){
            return listItem.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvPrice, tvType, tvCategory, tvDate;
        ImageView ivItem;
        LinearLayout llItem;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
        }
    }
}
