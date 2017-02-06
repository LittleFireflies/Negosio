package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

/**
 * Created by LittleFireflies on 28-Jan-17.
 */

public class CameraAdapter extends RecyclerView.Adapter<CameraAdapter.ViewHolder>{
    List<Barang> listCamera;
    View layout;
    private Context ctx;
    private Dialog dialogDetail;
    private TextView dialog_tvUsername, dialog_tvTitle, dialog_tvDesc, dialog_tvDate, dialog_tvPrice, dialog_tvType;
    private ImageView dialog_ivImage;
    private EditText dialog_etFrom, dialog_etTo;
    private int mYear, mMonth, mDay, mHour, mMinute;

    public CameraAdapter(List<Barang> param){
        listCamera = param;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category, parent, false);
        ctx = parent.getContext();
        setDialog();

        return new ViewHolder(layout);
    }

    private void setDialog() {
        dialogDetail = new Dialog(ctx);
        dialogDetail.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogDetail.setContentView(R.layout.detail_item);
        dialogDetail.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogDetail.setCanceledOnTouchOutside(false);
        dialogDetail.onBackPressed();

        dialog_tvUsername = (TextView) dialogDetail.findViewById(R.id.tvUsername);
        dialog_ivImage = (ImageView) dialogDetail.findViewById(R.id.ivDetail);
        dialog_tvDate = (TextView) dialogDetail.findViewById(R.id.tvDate);
        dialog_tvTitle = (TextView) dialogDetail.findViewById(R.id.tvTitle);
        dialog_tvDesc = (TextView) dialogDetail.findViewById(R.id.tvDesc);
        dialog_tvPrice = (TextView) dialogDetail.findViewById(R.id.tvPrice);
        dialog_tvType = (TextView) dialogDetail.findViewById(R.id.tvType);
        dialog_etFrom = (EditText) dialogDetail.findViewById(R.id.etDateFrom);
        dialog_etTo = (EditText) dialogDetail.findViewById(R.id.etDateTo);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTitle.setText(listCamera.get(position).getProductname());
        holder.tvPrice.setText(listCamera.get(position).getPrice());
        holder.tvSeller.setText(listCamera.get(position).getUsername());
        holder.tvDate.setText(listCamera.get(position).getDate());
        PicassoClient.downloadImage(ctx, listCamera.get(position).getImg(), holder.ivImage);

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDetail.show();
                dialog_tvUsername.setText(listCamera.get(position).getUsername());
                dialog_tvDate.setText(listCamera.get(position).getDate());
                dialog_tvTitle.setText(listCamera.get(position).getProductname());
                dialog_tvDesc.setText(listCamera.get(position).getDescription());
                dialog_tvPrice.setText(listCamera.get(position).getPrice());
                dialog_tvType.setText(listCamera.get(position).getType());
                dialog_etFrom.setText("");
                dialog_etTo.setText("");
                PicassoClient.downloadImage(ctx, listCamera.get(position).getImg(), dialog_ivImage);

                dialog_etFrom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        final String[] dateFrom = {""};
                        Calendar calendar = Calendar.getInstance();
                        mYear = calendar.get(Calendar.YEAR);
                        mMonth = calendar.get(Calendar.MONTH);
                        mDay = calendar.get(Calendar.DAY_OF_MONTH);

                        mHour = calendar.get(Calendar.HOUR_OF_DAY);
                        mMinute = calendar.get(Calendar.MINUTE);



                        DatePickerDialog datePickerDialog = new DatePickerDialog(ctx, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, final int year, final int month, final int day) {
                                TimePickerDialog timePickerDialog = new TimePickerDialog(ctx, new TimePickerDialog.OnTimeSetListener() {
                                    @Override
                                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                                        Calendar time = Calendar.getInstance();
                                        time.set(Calendar.DAY_OF_MONTH, day);
                                        time.set(Calendar.MONTH, month);
                                        time.set(Calendar.YEAR, year);
                                        time.set(Calendar.HOUR_OF_DAY, hour);
                                        time.set(Calendar.MINUTE, minute);
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy hh:mm");
                                        dialog_etFrom.setText(sdf.format(time.getTime()));
//                                        dialog_etFrom.setText(day + " " + (month+1) + " " + year + " " + hour + ":" + minute);
                                    }
                                }, mHour, mMinute, false);
                                timePickerDialog.show();
                            }
                        }, mYear, mMonth, mDay);
                        datePickerDialog.show();


//                        dialog_etFrom.setText(dateFrom[0]);
                    }
                });
            }
        });
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
