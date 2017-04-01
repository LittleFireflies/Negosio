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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.NumberTextWatcher;
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
    private Dialog dialogDetail, dialogDelete, dialogEdit;
    private TextView dialog_tvTitle, dialog_tvPrice, dialog_tvType, dialog_tvCategory, dialog_tvDesc, dialog_tvDate, dialog_tvUsername, dialog_tvBack, dialog_tvEditUsername, dialog_tvEditDate;
    private ImageView dialog_ivItem, dialog_ivBack, dialog_ivEditImage;
    private Button dialog_btnEdit, dialog_btnDelete, dialog_btnYes, dialog_btnSaveEdit;
    private EditText dialog_etTitle, dialog_etPrice, dialog_etDesc;
    private Spinner dialog_spType, dialog_spCategory;

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

        dialogDelete = new Dialog(ctx);
        dialogDelete.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDelete.setContentView(R.layout.dialog_delete);
        dialogDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDelete.setCanceledOnTouchOutside(false);
        dialogDelete.onBackPressed();

        dialogEdit = new Dialog(ctx);
        dialogEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEdit.setContentView(R.layout.detail_edit_item);
        dialogEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEdit.setCanceledOnTouchOutside(false);
        dialogEdit.onBackPressed();

        dialog_ivBack = (ImageView) dialogDetail.findViewById(R.id.ivBack);
        dialog_tvTitle = (TextView) dialogDetail.findViewById(R.id.tvTitle);
        dialog_tvPrice = (TextView) dialogDetail.findViewById(R.id.tvPrice);
        dialog_tvType = (TextView) dialogDetail.findViewById(R.id.tvType);
        dialog_tvCategory = (TextView) dialogDetail.findViewById(R.id.tvCategory);
        dialog_tvDesc = (TextView) dialogDetail.findViewById(R.id.tvDesc);
        dialog_tvUsername = (TextView) dialogDetail.findViewById(R.id.tvUsername);
        dialog_tvDate = (TextView) dialogDetail.findViewById(R.id.tvDate);
        dialog_ivItem = (ImageView) dialogDetail.findViewById(R.id.ivDetail);
        dialog_btnEdit = (Button) dialogDetail.findViewById(R.id.btnEdit);
        dialog_btnDelete = (Button) dialogDetail.findViewById(R.id.btnDelete);
        dialog_btnYes = (Button) dialogDelete.findViewById(R.id.btnYes);
        dialog_tvBack = (TextView) dialogDelete.findViewById(R.id.tvBack);

        dialog_etTitle = (EditText) dialogEdit.findViewById(R.id.etTitle);
        dialog_etPrice = (EditText) dialogEdit.findViewById(R.id.etPrice);
        dialog_spType = (Spinner) dialogEdit.findViewById(R.id.spType);
        dialog_spCategory = (Spinner) dialogEdit.findViewById(R.id.spCategory);
        dialog_etDesc = (EditText) dialogEdit.findViewById(R.id.etDesc);
        dialog_btnSaveEdit = (Button) dialogEdit.findViewById(R.id.btnSaveEdit);
        dialog_ivBack = (ImageView) dialogEdit.findViewById(R.id.ivBack);
        dialog_tvEditDate = (TextView) dialogEdit.findViewById(R.id.tvEditDate);
        dialog_tvEditUsername = (TextView) dialogEdit.findViewById(R.id.tvEditUsername);
        dialog_ivEditImage = (ImageView) dialogEdit.findViewById(R.id.ivEditDetail);

        dialog_etPrice.addTextChangedListener(new NumberTextWatcher(dialog_etPrice, "#,###", "currency", null));
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
                dialog_btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogDelete.show();
                        dialog_btnYes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dbRef.child("Barang").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                                            if(map.get("productname").equals(dialog_tvTitle.getText().toString())
                                                    && map.get("username").equals(dialog_tvUsername.getText().toString())
                                                    && map.get("price").equals(dialog_tvPrice.getText().toString())
                                                    && map.get("date").equals(dialog_tvDate.getText().toString())){
                                                dbRef.child("Barang").child(snapshot.getKey()).setValue(null);
                                                dialogDelete.dismiss();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        });
                        dialog_tvBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogDelete.dismiss();
                            }
                        });
                    }
                });
                dialog_btnEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogEdit.show();
                        dialogDetail.dismiss();
                        dialog_etTitle.setText(dialog_tvTitle.getText().toString());
                        dialog_etPrice.setText(dialog_tvPrice.getText().toString());
                        dialog_etDesc.setText(dialog_tvDesc.getText().toString());
                        dialog_tvEditUsername.setText(dialog_tvUsername.getText().toString());
                        dialog_tvEditDate.setText(dialog_tvDate.getText().toString());
                        dialog_btnSaveEdit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dbRef.child("Barang").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            Map<String, String> map = (Map<String, String>) snapshot.getValue();

                                            if (map.get("username").equals(MainActivity.getUserLogin())
                                                    && map.get("productname").equals(dialog_tvTitle.getText().toString())
                                                    && map.get("date").equals(dialog_tvEditDate.getText().toString())) {
                                                Barang obj = new Barang();
                                                obj.setProductname(dialog_etTitle.getText().toString());
                                                obj.setPrice(dialog_etPrice.getText().toString());
                                                obj.setType(dialog_spType.getSelectedItem().toString());
                                                obj.setCategory(dialog_spCategory.getSelectedItem().toString());
                                                obj.setDescription(dialog_etDesc.getText().toString());
                                                obj.setDate(map.get("date"));
                                                obj.setUsername(map.get("username"));
                                                obj.setImg(map.get("img"));
                                                dbRef.child("Barang").child(snapshot.getKey()).setValue(obj);
                                                dialogEdit.dismiss();
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
