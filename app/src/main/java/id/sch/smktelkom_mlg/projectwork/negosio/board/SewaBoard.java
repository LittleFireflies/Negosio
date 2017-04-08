package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.AppController;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.NumberTextWatcher;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

public class SewaBoard extends AppCompatActivity implements View.OnClickListener{
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    AppController controller;
    Context ctx;
    EditText etProduct, etPrice, etDesc;
    Spinner spCategory, spType;
    Button btnAdd, btnAttach;
    ImageView ivAttachment;
    TextView tvDelete;
    Dialog dialog;
    LinearLayout llAttachment;
    private DatabaseReference dbRef;
    private StorageReference storageRef;
    private String username  = MainActivity.getUserLogin();
    private String location;
    private String phone;
    private ProgressDialog progressDialog;
    Uri downloadUri;
    private boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sewa);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Add Product");
        ctx = getApplicationContext();
        assignToView();
        onSetView();
    }

    private void onSetView() {
        btnAdd.setOnClickListener(this);
        btnAttach.setOnClickListener(this);
        tvDelete.setOnClickListener(this);
        dbRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(map.get("username").equals(username)){
                        location = map.get("location");
                        phone = map.get("phone");
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void assignToView() {
        dbRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();

        controller = new AppController();

        etProduct = (EditText) findViewById(R.id.etProduct);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etDesc = (EditText) findViewById(R.id.etDesc);
        spCategory = (Spinner) findViewById(R.id.spCategory);
        spType = (Spinner) findViewById(R.id.spType);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAttach = (Button) findViewById(R.id.btnAttach);
        ivAttachment = (ImageView) findViewById(R.id.ivAttachment);
        tvDelete = (TextView) findViewById(R.id.tvDelete);
        llAttachment = (LinearLayout) findViewById(R.id.llAttachment);

        etPrice.addTextChangedListener(new NumberTextWatcher(etPrice, "#,###", "currency", null));

        dialog = new Dialog(ctx);
        progressDialog = new ProgressDialog(SewaBoard.this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                add();
                break;
            case R.id.btnAttach:
                uploadImage();
                break;
            case R.id.tvDelete:
                deleteImage();
                break;
        }
    }

    private void deleteImage() {
        llAttachment.setVisibility(View.GONE);
        btnAttach.setEnabled(true);
    }

    private void uploadImage() {
        final String[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SewaBoard.this);
        builder.setTitle("Add Attachment");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                boolean result = Utility.checkPermission(ctx);

                if(items[i].equals("Take Photo")){
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if(items[i].equals("Choose from Gallery")){
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
                } else if(items[i].equals("Cancel")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void add() {
        if (isValid()) {
            final String productname = etProduct.getText().toString().trim();
            final String price = etPrice.getText().toString().trim();
            final String description = etDesc.getText().toString().trim();
            String type = spType.getSelectedItem().toString().trim();
            String category = spCategory.getSelectedItem().toString().trim();
//            String username = "";

            try {
                Barang barang = new Barang();
                barang.setProductname(productname);
                barang.setPrice(price);
                barang.setDescription(description);
                barang.setCategory(category);
                barang.setType(type);
                barang.setUsername(username);
                barang.setDate(controller.getDate("dd MMMM yyyy"));
                barang.setImg(String.valueOf(downloadUri));
                barang.setLocation(location);
                barang.setPhone(phone);


                dbRef.child("Barang").push().setValue(barang);
                Toast.makeText(ctx, "Add Success", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {

            }
        }
    }

    public boolean isValid() {
        valid = true;
        String product_name = etProduct.getText().toString();
        String price = etPrice.getText().toString();
        String description = etDesc.getText().toString();

        if(product_name.equals("")){
            etProduct.setError(getText(R.string.EmptyField));
            valid = false;
        } else {
            etProduct.setError(null);
        }

        if(price.equals("")){
            etPrice.setError(getText(R.string.EmptyField));
            valid = false;
        } else {
            etPrice.setError(null);
        }

        if(description.equals("")){
            etDesc.setError(getText(R.string.EmptyField));
            valid = false;
        } else if(description.length() > 250){
            etDesc.setError("Description Max. 250 characters");
            valid = false;
        } else {
            etDesc.setError(null);
        }

        return valid;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if(requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onCaptureImageResult(Intent data) {
        if(data != null){
            progressDialog.setMessage("Uploading. Please wait...");
            progressDialog.show();

            Uri uri = data.getData();
            StorageReference filePath = storageRef.child("Photos").child(username).child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(ctx, "Upload Done", Toast.LENGTH_LONG).show();
                    btnAttach.setEnabled(false);
                    progressDialog.dismiss();
                    llAttachment.setVisibility(View.VISIBLE);
                    PicassoClient.downloadImage(ctx, String.valueOf(downloadUri), ivAttachment);
                }
            });
        }
    }

    private void onSelectFromGalleryResult(Intent data) {
        if(data != null){
            progressDialog.setMessage("Uploading. Please wait...");
            progressDialog.show();

            Uri uri = data.getData();
            StorageReference filePath = storageRef.child("Photos").child(username).child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(ctx, "Upload Done", Toast.LENGTH_LONG).show();
                    btnAttach.setEnabled(false);
                    progressDialog.dismiss();
                    llAttachment.setVisibility(View.VISIBLE);
                    PicassoClient.downloadImage(ctx, String.valueOf(downloadUri), ivAttachment);
                }
            });
        }
    }
}
