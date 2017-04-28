package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Booking;

/**
 * Created by LittleFireflies on 11-Feb-17.
 */

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.ViewHolder>{

    public static final int CALL_CODE = 24;
    private View layout;
    private Context ctx;
    private DatabaseReference dbRef;
    private ArrayList<Booking> listItem;
    private Dialog dialogDetail, dialogComplete;
    private ImageView dialog_ivItem, dialog_ivBack;
    private TextView dialog_tvTitle, dialog_tvPrice, dialog_tvCategory, dialog_tvBuyer, dialog_tvSellerPhone, dialog_tvSeller, dialog_tvBookDate, dialog_tvStartDate, dialog_tvEndDate, dialog_tvTotal, dialog_tvStatus, dialog_tvReason;
    private Button dialog_btnCall, dialog_btnConfirm, dialog_btnComplete, dialog_btnYes;
    private LinearLayout dialog_llReason;
    private TextView dialog_tvCancel;
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

        dialogComplete = new Dialog(ctx);
        dialogComplete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogComplete.setContentView(R.layout.dialog_complete);
        dialogComplete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogComplete.setCanceledOnTouchOutside(false);
        dialogComplete.onBackPressed();

        dialog_ivBack = (ImageView) dialogDetail.findViewById(R.id.ivBack);
        dialog_tvTitle = (TextView) dialogDetail.findViewById(R.id.tvTitle);
        dialog_tvStatus = (TextView) dialogDetail.findViewById(R.id.tvStatus);
        dialog_tvReason = (TextView) dialogDetail.findViewById(R.id.tvReason);
        dialog_tvPrice = (TextView) dialogDetail.findViewById(R.id.tvPrice);
        dialog_tvCategory = (TextView) dialogDetail.findViewById(R.id.tvCategory);
        dialog_tvBuyer = (TextView) dialogDetail.findViewById(R.id.tvBuyer);
        dialog_tvSeller = (TextView) dialogDetail.findViewById(R.id.tvSeller);
        dialog_tvSellerPhone = (TextView) dialogDetail.findViewById(R.id.tvSellerPhone);
        dialog_tvStartDate = (TextView) dialogDetail.findViewById(R.id.tvStartDate);
        dialog_tvEndDate = (TextView) dialogDetail.findViewById(R.id.tvEndDate);
        dialog_tvBookDate = (TextView) dialogDetail.findViewById(R.id.tvDate);
        dialog_tvTotal = (TextView) dialogDetail.findViewById(R.id.tvTotalPrice);
        dialog_ivItem = (ImageView) dialogDetail.findViewById(R.id.ivDetail);
        dialog_ivBack = (ImageView) dialogDetail.findViewById(R.id.ivBack);
        dialog_btnCall = (Button) dialogDetail.findViewById(R.id.btnCall);
        dialog_btnConfirm = (Button) dialogDetail.findViewById(R.id.btnConfirm);
        dialog_btnComplete = (Button) dialogDetail.findViewById(R.id.btnComplete);
        dialog_llReason = (LinearLayout) dialogDetail.findViewById(R.id.llReason);

        dialog_btnYes = (Button) dialogComplete.findViewById(R.id.btnYes);
        dialog_tvCancel = (TextView) dialogComplete.findViewById(R.id.tvCancel);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tvTitle.setText(listItem.get(position).getProduct_name());
        holder.tvPrice.setText(listItem.get(position).getTotal());
        holder.tvCategory.setText(listItem.get(position).getCategory());
        holder.tvDate.setText(listItem.get(position).getTgl_booking());
        holder.tvSeller.setText(listItem.get(position).getSeller());
        holder.tvStatus.setText(listItem.get(position).getStatus());
        PicassoClient.downloadImage(ctx, listItem.get(position).getImg(), holder.ivItem);
        holder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDetail.show();
                dialog_tvBookDate.setText(listItem.get(position).getTgl_booking());
                dialog_tvTitle.setText(listItem.get(position).getProduct_name());
                dialog_tvStatus.setText(listItem.get(position).getStatus());
                if(listItem.get(position).getReason() != null){
                    dialog_llReason.setVisibility(View.VISIBLE);
                    dialog_tvReason.setText(listItem.get(position).getReason());
                } else {
                    dialog_llReason.setVisibility(View.GONE);
                }
                dialog_tvPrice.setText(listItem.get(position).getPrice());
                dialog_tvCategory.setText(listItem.get(position).getCategory());
                dialog_tvBuyer.setText(listItem.get(position).getBuyer());
                dialog_tvSeller.setText(listItem.get(position).getSeller());
                dialog_tvSellerPhone.setText(listItem.get(position).getSeller_phone());
                dialog_tvStartDate.setText(listItem.get(position).getStart_date());
                dialog_tvEndDate.setText(listItem.get(position).getEnd_date());
                dialog_tvTotal.setText(listItem.get(position).getTotal());
                PicassoClient.downloadImage(ctx, listItem.get(position).getImg(), dialog_ivItem);
                if(listItem.get(position).getStatus().equals("On Loan")){
                    dialog_btnConfirm.setVisibility(View.GONE);
                    dialog_btnComplete.setVisibility(View.VISIBLE);
                } else {
                    dialog_btnConfirm.setVisibility(View.GONE);
                    dialog_btnComplete.setVisibility(View.GONE);
                }
                dialog_ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDetail.dismiss();
                    }
                });
                dialog_btnCall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse("tel:" + listItem.get(position).getSeller_phone()));
                        if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions((Activity) ctx, new String[]{Manifest.permission.CALL_PHONE}, CALL_CODE);
                        } else {
                            ctx.startActivity(intent);
                        }
                    }
                });
                dialog_btnComplete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogComplete.show();
                        dialog_tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogComplete.dismiss();
                            }
                        });
                        dialog_btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dbRef.child("Booking").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                                            if(map.get("seller").equals(dialog_tvSeller.getText().toString())
                                                    && map.get("buyer").equals(dialog_tvBuyer.getText().toString())
                                                    && map.get("product_name").equals(dialog_tvTitle.getText().toString())
                                                    && map.get("tgl_booking").equals(dialog_tvBookDate.getText().toString())){

                                                Booking booking = new Booking();
                                                booking.setProduct_name(map.get("product_name"));
                                                booking.setTotal(map.get("total"));
                                                booking.setCategory(map.get("category"));
                                                booking.setStart_date(map.get("start_date"));
                                                booking.setEnd_date(map.get("end_date"));
                                                booking.setPrice(map.get("price"));
                                                booking.setTime(map.get("time"));
                                                booking.setTgl_booking(map.get("tgl_booking"));
                                                booking.setBuyer(map.get("buyer"));
                                                booking.setBuyer_phone(map.get("buyer_phone"));
                                                booking.setBuyer_location(map.get("buyer_location"));
                                                booking.setRenter_token(map.get("renter_token"));
                                                booking.setSeller(map.get("seller"));
                                                booking.setSeller_phone(map.get("seller_phone"));
                                                booking.setSeller_location(map.get("seller_location"));
                                                booking.setOwner_token(map.get("owner_token"));
                                                booking.setImg(map.get("img"));
                                                booking.setStatus("Waiting for Completion");
                                                booking.setReason(map.get("reason"));
                                                listItem.add(booking);
                                                dbRef.child("Booking").child(snapshot.getKey()).setValue(booking);
                                                dialogComplete.dismiss();
                                                dialogDetail.dismiss();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
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
        TextView tvTitle, tvPrice, tvType, tvCategory, tvDate, tvSeller, tvStatus;
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
            tvStatus = (TextView) itemView.findViewById(R.id.tvStatus);
            ivItem = (ImageView) itemView.findViewById(R.id.ivItem);
            llItem = (LinearLayout) itemView.findViewById(R.id.llItem);
        }
    }
}
