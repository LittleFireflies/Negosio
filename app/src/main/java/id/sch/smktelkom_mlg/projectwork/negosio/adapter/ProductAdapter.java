package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.board.CategoryDetailBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.AppController;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.NumberTextWatcher;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Booking;

/**
 * Created by LittleFireflies on 28-Jan-17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    public static final String DIRECTION = "Direction";
    public static final int CALL_CODE = 24;
    List<Barang> listProduct;
    View layout;
    private static Context ctx;
    private Dialog dialogDetail, dialogUnlogged, dialogConfirm, dialogSuccess, dialogEdit;
    private TextView dialog_tvUsername, dialog_tvTitle, dialog_tvDesc, dialog_tvLocation, dialog_tvDate, dialog_tvPrice, dialog_tvCategory, dialog_tvType, dialog_btnBack, dialog_tvEditUsername, dialog_tvEditDate;
    private EditText dialog_etTitle, dialog_etPrice, dialog_etDesc;
    private Spinner dialog_spType, dialog_spCategory;
    private ImageView dialog_ivImage, dialog_ivBack, dialog_ivEditImage, dialog_IvEditBack, dialog_ivChangePict;
    private EditText dialog_etFrom, dialog_etTo;
    private Button dialog_btnSewa, dialog_btnEdit, dialog_btnCall, dialog_back, dialog_btnYes, dialog_btnOk, dialog_btnSaveEdit;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private DatabaseReference dbRef;
    private static StorageReference storageRef;
    private AppController controller;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private boolean valid;
    private static ProgressDialog progressDialog;
    private String ownerToken, ownerLocation, ownerPhone;
    private String renterPhone, renterLocation, renterToken;

    public ProductAdapter(List<Barang> param) {
        listProduct = param;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_category, parent, false);
        ctx = parent.getContext();
        controller = new AppController();
        progressDialog = new ProgressDialog(ctx);
        storageRef = FirebaseStorage.getInstance().getReference();
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

        dialogUnlogged = new Dialog(ctx);
        dialogUnlogged.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUnlogged.setContentView(R.layout.dialog_not_login);
        dialogUnlogged.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogUnlogged.setCanceledOnTouchOutside(false);
        dialogUnlogged.onBackPressed();

        dialogConfirm = new Dialog(ctx);
        dialogConfirm.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogConfirm.setContentView(R.layout.dialog_confirm);
        dialogConfirm.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogConfirm.setCanceledOnTouchOutside(false);
        dialogConfirm.onBackPressed();

        dialogSuccess = new Dialog(ctx);
        dialogSuccess.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogSuccess.setContentView(R.layout.dialog_success);
        dialogSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogSuccess.setCanceledOnTouchOutside(false);
        dialogSuccess.onBackPressed();

        dialogEdit = new Dialog(ctx);
        dialogEdit.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogEdit.setContentView(R.layout.detail_edit_item);
        dialogEdit.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogEdit.setCanceledOnTouchOutside(false);
        dialogEdit.onBackPressed();

        dialog_tvUsername = (TextView) dialogDetail.findViewById(R.id.tvUsername);
        dialog_ivImage = (ImageView) dialogDetail.findViewById(R.id.ivDetail);
        dialog_tvDate = (TextView) dialogDetail.findViewById(R.id.tvDate);
        dialog_tvTitle = (TextView) dialogDetail.findViewById(R.id.tvTitle);
        dialog_tvDesc = (TextView) dialogDetail.findViewById(R.id.tvDesc);
        dialog_tvLocation = (TextView) dialogDetail.findViewById(R.id.tvLocation);
        dialog_tvPrice = (TextView) dialogDetail.findViewById(R.id.tvPrice);
        dialog_tvCategory = (TextView) dialogDetail.findViewById(R.id.tvCategory);
        dialog_tvType = (TextView) dialogDetail.findViewById(R.id.tvType);
        dialog_etFrom = (EditText) dialogDetail.findViewById(R.id.etDateFrom);
        dialog_etTo = (EditText) dialogDetail.findViewById(R.id.etDateTo);
        dialog_btnSewa = (Button) dialogDetail.findViewById(R.id.btnSewa);
        dialog_btnEdit = (Button) dialogDetail.findViewById(R.id.btnEdit);
        dialog_btnCall = (Button) dialogDetail.findViewById(R.id.btnCall);
        dialog_ivBack = (ImageView) dialogDetail.findViewById(R.id.ivBack);

        dialog_etTitle = (EditText) dialogEdit.findViewById(R.id.etTitle);
        dialog_etPrice = (EditText) dialogEdit.findViewById(R.id.etPrice);
        dialog_spType = (Spinner) dialogEdit.findViewById(R.id.spType);
        dialog_spCategory = (Spinner) dialogEdit.findViewById(R.id.spCategory);
        dialog_etDesc = (EditText) dialogEdit.findViewById(R.id.etDesc);
        dialog_btnSaveEdit = (Button) dialogEdit.findViewById(R.id.btnSaveEdit);
        dialog_IvEditBack = (ImageView) dialogEdit.findViewById(R.id.ivBack);
        dialog_ivChangePict = (ImageView) dialogEdit.findViewById(R.id.btnChangeImage);
        dialog_tvEditDate = (TextView) dialogEdit.findViewById(R.id.tvEditDate);
        dialog_tvEditUsername = (TextView) dialogEdit.findViewById(R.id.tvEditUsername);
        dialog_ivEditImage = (ImageView) dialogEdit.findViewById(R.id.ivEditDetail);

        dialog_back = (Button) dialogUnlogged.findViewById(R.id.btnBack);
        dialog_btnYes = (Button) dialogConfirm.findViewById(R.id.btnYes);
        dialog_btnBack = (TextView) dialogConfirm.findViewById(R.id.tvBack);
        dialog_btnOk = (Button) dialogSuccess.findViewById(R.id.btnOK);

        dialog_etPrice.addTextChangedListener(new NumberTextWatcher(dialog_etPrice, "#,###", "currency", null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.tvTitle.setText(listProduct.get(position).getProductname());
        final String type = listProduct.get(position).getType().substring(4);
        holder.tvPrice.setText("Rp " + listProduct.get(position).getPrice() + "/" + type);
        holder.tvSeller.setText(listProduct.get(position).getUsername());
        holder.tvDate.setText(listProduct.get(position).getDate());
        PicassoClient.downloadImage(ctx, listProduct.get(position).getImg(), holder.ivImage);

        holder.ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDetail.show();
                dialog_tvUsername.setText(listProduct.get(position).getUsername());
                dialog_tvDate.setText(listProduct.get(position).getDate());
                dialog_tvTitle.setText(listProduct.get(position).getProductname());
                dialog_tvDesc.setText(listProduct.get(position).getDescription());
                dialog_tvPrice.setText(listProduct.get(position).getPrice());
                dialog_tvLocation.setText(listProduct.get(position).getLocation());
                dialog_tvCategory.setText(listProduct.get(position).getCategory());
                dialog_tvType.setText(listProduct.get(position).getType());
                dialog_etFrom.setText("");
                dialog_etTo.setText("");
                getSellerToken(listProduct.get(position).getUsername());
                getBuyerData();

                if (user != null && user.getDisplayName().equals(dialog_tvUsername.getText().toString())) {
                    dialog_btnSewa.setVisibility(View.GONE);
                    dialog_btnEdit.setVisibility(View.VISIBLE);
                    dialog_btnCall.setVisibility(View.GONE);
                } else {
                    dialog_btnSewa.setVisibility(View.VISIBLE);
                    dialog_btnEdit.setVisibility(View.GONE);
                    dialog_btnCall.setVisibility(View.VISIBLE);
                }
                PicassoClient.downloadImage(ctx, listProduct.get(position).getImg(), dialog_ivImage);

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
                        intent.setData(Uri.parse("tel:" + listProduct.get(position).getPhone()));
                        if(ContextCompat.checkSelfPermission(ctx, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions((Activity) ctx, new String[]{Manifest.permission.CALL_PHONE}, CALL_CODE);
                        } else {
                            ctx.startActivity(intent);
                        }

                    }
                });
                dialog_btnSewa.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isValid(dialog_etFrom.getText().toString(), dialog_etTo.getText().toString())) {
                            final int time = calculateTime(dialog_tvType.getText().toString(), dialog_etFrom.getText().toString(), dialog_etTo.getText().toString());
                            double price = controller.currencyTonumber(dialog_tvPrice.getText().toString());
                            final double total = time * price;

                            if (user != null) {
                                dialogConfirm.show();
                                dialog_btnYes.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        try {
                                            Booking booking = new Booking();
                                            booking.setTgl_booking(controller.getDate("dd MMMM yyyy HH:mm"));
                                            booking.setProduct_name(dialog_tvTitle.getText().toString());
                                            booking.setCategory(dialog_tvCategory.getText().toString());
                                            booking.setPrice("Rp. " + dialog_tvPrice.getText().toString() + " " + dialog_tvType.getText().toString());
                                            booking.setStart_date(dialog_etFrom.getText().toString());
                                            booking.setEnd_date(dialog_etTo.getText().toString());
                                            booking.setTime(String.valueOf(time));
                                            booking.setTotal(controller.numberTocurrency(total));
                                            booking.setBuyer(user.getDisplayName());
                                            booking.setSeller(dialog_tvUsername.getText().toString());
                                            booking.setImg(listProduct.get(position).getImg());
                                            booking.setOwner_token(ownerToken);
                                            booking.setRenter_token(renterToken);
                                            booking.setBuyer_phone(renterPhone);
                                            booking.setBuyer_location(renterLocation);
                                            booking.setSeller_phone(ownerPhone);
                                            booking.setSeller_location(ownerLocation);
                                            booking.setStatus("Waiting for Confirmation");

                                            dbRef.child("Booking").push().setValue(booking);
                                            dialogConfirm.dismiss();

                                            dialogSuccess.show();
                                            dialog_btnOk.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View view) {
                                                    dialogSuccess.dismiss();
                                                    dialog_etFrom.setText("");
                                                    dialog_etTo.setText("");
                                                }
                                            });
                                        } catch (Exception e) {
                                            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                                dialog_btnBack.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogConfirm.dismiss();
                                    }
                                });
                            } else {
//                                Toast.makeText(ctx, "You haven't login!", Toast.LENGTH_LONG).show();
                                dialogUnlogged.show();
                                dialog_back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        dialogUnlogged.dismiss();
//                                        Intent intent = new Intent(ctx, MainActivity.class);
//                                        intent.putExtra(DIRECTION, R.string.ClassHome);
//                                        ctx.startActivity(intent);
                                    }
                                });
                            }
                        }
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
                        dialog_spType.setSelection(getIndex(dialog_spType, dialog_tvType.getText().toString()));
                        dialog_spCategory.setSelection(getIndex(dialog_spCategory, dialog_tvCategory.getText().toString()));
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
                        dialog_IvEditBack.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialogEdit.dismiss();
                            }
                        });
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
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
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
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy HH:mm");
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

    private int getIndex(Spinner spinner, String value) {
        int index = 0;

        for(int i=0; i<spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).equals(value)){
                index = i;
            }
        }
        return index;
    }

    private void getBuyerData() {
        dbRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(map.get("username").equals(MainActivity.getUserLogin())){
                        renterPhone = map.get("phone");
                        renterLocation = map.get("location");
                        renterToken = map.get("token");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private int calculateTime(String type, String from, String to) {
        SimpleDateFormat format = new SimpleDateFormat("dd MMMM yyyy HH:mm");
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

//        int hours = (int) TimeUnit.HOURS.convert(difference, TimeUnit.MILLISECONDS);
//        int days = (int) TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS);
//        int months = (int) (TimeUnit.DAYS.convert(difference, TimeUnit.MILLISECONDS) / 30);
//        int years = months / 12;
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) (difference / (1000 * 60 * 60));
        int months = days/30;
        int years = months/12;
        switch (type){
            case "Per Hour":
                return hours!=0?hours:1;
            case "Per 3 Hour":
                return (hours/3)!=0?(hours/3):1;
            case "Per 12 Hour":
                return (hours/12)!=0?(hours/12):1;
            case "Per Day":
                return days!=0?days:1;
            case "Per 3 Days":
                return (days/3)!=0?(days/3):1;
            case "Per Month":
                return months!=0?months:1;
            case "Per year":
                return years!=0?years:1;
        }
        return days;
    }

    @Override
    public int getItemCount() {
        if(listProduct != null){
            return listProduct.size();
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

    public void getSellerToken(final String username) {
        dbRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(map.get("username").equals(username)){
                        ownerToken = map.get("token");
                        ownerPhone = map.get("phone");
                        ownerLocation = map.get("location");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
