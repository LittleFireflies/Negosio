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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Booking;

/**
 * Created by LittleFireflies on 11-Feb-17.
 */

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.ViewHolder>{

    private View layout;
    private Context ctx;
    private DatabaseReference dbRef;
    private ArrayList<Booking> listItem;
    private Dialog dialogDetail;
    private ImageView dialog_ivItem, dialog_ivBack;
    private TextView dialog_tvTitle, dialog_tvPrice, dialog_tvCategory, dialog_tvBuyer, dialog_tvSeller, dialog_tvBookDate, dialog_tvStartDate, dialog_tvEndDate, dialog_tvTotal;
    private FirebaseUser user;

    public BuyAdapter(ArrayList<Booking> param){
        listItem = param;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history_buy, parent, false);
        ctx = parent.getContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        setDialog();
        return new ViewHolder(layout);
    }

    private void setDialog() {
        dialogDetail = new Dialog(ctx);
        dialogDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDetail.setContentView(R.layout.detail_buy_transaction);
        dialogDetail.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDetail.setCanceledOnTouchOutside(false);
        dialogDetail.onBackPressed();

        dialog_ivBack = (ImageView) dialogDetail.findViewById(R.id.ivBack);
        dialog_tvTitle = (TextView) dialogDetail.findViewById(R.id.tvTitle);
        dialog_tvPrice = (TextView) dialogDetail.findViewById(R.id.tvPrice);
        dialog_tvCategory = (TextView) dialogDetail.findViewById(R.id.tvCategory);
        dialog_tvBuyer = (TextView) dialogDetail.findViewById(R.id.tvBuyer);
        dialog_tvSeller = (TextView) dialogDetail.findViewById(R.id.tvSeller);
        dialog_tvStartDate = (TextView) dialogDetail.findViewById(R.id.tvStartDate);
        dialog_tvEndDate = (TextView) dialogDetail.findViewById(R.id.tvEndDate);
        dialog_tvBookDate = (TextView) dialogDetail.findViewById(R.id.tvDate);
        dialog_tvTotal = (TextView) dialogDetail.findViewById(R.id.tvTotalPrice);
        dialog_ivItem = (ImageView) dialogDetail.findViewById(R.id.ivDetail);
        dialog_ivBack = (ImageView) dialogDetail.findViewById(R.id.ivBack);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTitle.setText(listItem.get(position).getProduct_name());
        holder.tvPrice.setText(listItem.get(position).getTotal());
        holder.tvCategory.setText(listItem.get(position).getCategory());
        holder.tvDate.setText(listItem.get(position).getTgl_booking());
        holder.tvSeller.setText(listItem.get(position).getSeller());
        PicassoClient.downloadImage(ctx, listItem.get(position).getImg(), holder.ivItem);
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDetail.show();
                dialog_tvBookDate.setText(listItem.get(position).getTgl_booking());
                dialog_tvTitle.setText(listItem.get(position).getProduct_name());
                dialog_tvPrice.setText(listItem.get(position).getPrice());
                dialog_tvCategory.setText(listItem.get(position).getCategory());
                dialog_tvBuyer.setText(listItem.get(position).getBuyer());
                dialog_tvSeller.setText(listItem.get(position).getSeller());
                dialog_tvStartDate.setText(listItem.get(position).getStart_date());
                dialog_tvEndDate.setText(listItem.get(position).getEnd_date());
                dialog_tvTotal.setText(listItem.get(position).getTotal());
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
        TextView tvTitle, tvPrice, tvType, tvCategory, tvDate, tvSeller;
        ImageView ivItem;
        LinearLayout llItem;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvType = (TextView) itemView.findViewById(R.id.tvType);
            tvCategory = (TextView) itemView.findViewById(R.id.tvCategory);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);
            tvSeller = (TextView) itemView.findViewById(R.id.tvSeller);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
        }
    }
}
