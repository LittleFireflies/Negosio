package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

<<<<<<< HEAD
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

=======
>>>>>>> 44f783b98410290536e588dcec405d5ce34fecaf
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.AppController;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Booking;

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
    private Button dialog_btnSewa;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatabaseReference dbRef;
    private AppController controller;
    private String username = MainActivity.getUserLogin();
    private boolean valid;

    public CameraAdapter(List<Barang> param){
        listCamera = param;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category, parent, false);
        ctx = parent.getContext();
        controller = new AppController();
        dbRef = FirebaseDatabase.getInstance().getReference();
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
        dialog_btnSewa = (Button) dialogDetail.findViewById(R.id.btnSewa);
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

                dialog_btnSewa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isValid(dialog_etFrom.getText().toString(), dialog_etTo.getText().toString())) {
                            int time = calculateTime(dialog_tvType.getText().toString(), dialog_etFrom.getText().toString(), dialog_etTo.getText().toString());
                            double price = controller.currencyTonumber(dialog_tvPrice.getText().toString());
                            double total = time * price;

                            if (username != null) {
                                Booking booking = new Booking();
                                booking.setTgl_booking(controller.getDate("dd MMMM yyyy hh:mm"));
                                booking.setProduct_name(dialog_tvTitle.getText().toString());
                                booking.setPrice("Rp. " + dialog_tvPrice.getText().toString() + " " + dialog_tvType.getText().toString());
                                booking.setStart_date(dialog_etFrom.getText().toString());
                                booking.setEnd_date(dialog_etTo.getText().toString());
                                booking.setTime(String.valueOf(time));
                                booking.setTotal(controller.numberTocurrency(total));
                                booking.setBuyer(username);
                                booking.setSeller(dialog_tvUsername.getText().toString());

                                dbRef.child("Booking").push().setValue(booking);
                            } else {
                                Toast.makeText(ctx, "You haven't login!", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
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
                    }
                });
                dialog_etTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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
                                        dialog_etTo.setText(sdf.format(time.getTime()));
//                                        dialog_etFrom.setText(day + " " + (month+1) + " " + year + " " + hour + ":" + minute);
                                    }
                                }, mHour, mMinute, false);
                                timePickerDialog.show();
                            }
                        }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });

                dialog_etTo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
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
                                        dialog_etTo.setText(sdf.format(time.getTime()));
//                                        dialog_etFrom.setText(day + " " + (month+1) + " " + year + " " + hour + ":" + minute);
                                    }
                                }, mHour, mMinute, false);
                                timePickerDialog.show();
                            }
                        }, mYear, mMonth, mDay);
                        datePickerDialog.show();
                    }
                });
            }

        });
    }

    private int calculateTime(String type, String from, String to) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy hh:mm");
        Date startDate = null;
        Date endDate = null;
        long difference = 0;
        try {
            startDate = format.parse(from);
            endDate = format.parse(to);

            difference = endDate.getTime() - startDate.getTime();
            Log.d("TESTDATE", String.valueOf(difference));
        } catch (Exception e){
            Log.d("TESTDATE", e.getMessage());
        }

        int hours = (int) TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS);
        int days = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
        int months = (int) (TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS) / 30);
        int years = months / 12;
        switch (type){
            case "Per Hour":
                return hours;
            case "Per 3 Hour":
                return hours/3;
            case "Per 12 Hour":
                return hours/12;
            case "Per Day":
                return days;
            case "Per 3 Days":
                return days/3;
            case "Per Month":
                return months;
            case "Per year":
                return years;
        }
        return days;
    }

    @Override
    public int getItemCount() {
        if(listCamera != null){
            return listCamera.size();
        } else {
            return 0;
        }
    }

    public boolean isValid(String from, String to) {
        valid = true;
        if(from.equals("")){
            dialog_etFrom.setError("Please enter the field");
            valid = false;
        } else {
            dialog_etFrom.setError(null);
        }

        if(to.equals("")){
            dialog_etTo.setError("Please enter the field");
            valid = false;
        } else {
            dialog_etTo.setError(null);
        }

        return valid;
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
