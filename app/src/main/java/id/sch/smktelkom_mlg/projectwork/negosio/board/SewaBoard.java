package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.helper.LoginHelper;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.AppController;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.NumberTextWatcher;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;
import io.realm.Realm;

import static android.app.Activity.RESULT_OK;


public class SewaBoard extends Fragment implements View.OnClickListener {
    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    View rootView;
    AppController controller;
    Context ctx;
    EditText etProduct, etPrice, etDesc;
    Spinner spCategory, spType;
    Button btnAdd, btnAttach;
    Dialog dialog;
    private DatabaseReference dbRef;
    private StorageReference storageRef;
    private Realm realm;
    private LoginHelper loginHelper;
    private String username  = MainActivity.getUserLogin();
    private ProgressDialog progressDialog;
    Uri downloadUri;
    private boolean valid;

    public SewaBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sewa, container, false);
        ctx = getContext();
        assignToView();
        onSetView();
        return rootView;
    }

    private void
    onSetView() {
        btnAdd.setOnClickListener(this);
        btnAttach.setOnClickListener(this);
    }

    private void assignToView() {
        //Firebase
        dbRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();

        controller = new AppController();
        realm = Realm.getDefaultInstance();
        loginHelper = new LoginHelper(realm);

        etProduct = (EditText) rootView.findViewById(R.id.etProduct);
        etPrice = (EditText) rootView.findViewById(R.id.etPrice);
        etDesc = (EditText) rootView.findViewById(R.id.etDesc);
        spCategory = (Spinner) rootView.findViewById(R.id.spCategory);
        spType = (Spinner) rootView.findViewById(R.id.sptype);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
        btnAttach = (Button) rootView.findViewById(R.id.btnAttach);

        etPrice.addTextChangedListener(new NumberTextWatcher(etPrice, "#,###", "currency", null));

        dialog = new Dialog(ctx);
        progressDialog = new ProgressDialog(ctx);
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
        }
    }

    private void uploadImage() {
        final String[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder((ctx));
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
                }
            });
        }
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

                dbRef.child("Barang").push().setValue(barang);
                Toast.makeText(ctx, "Add Success", Toast.LENGTH_SHORT).show();
                ((MainActivity) ctx).displayView(R.string.ClassHome);
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
            etProduct.setError(String.valueOf(R.string.EmptyField));
            valid = false;
        } else {
            etProduct.setError(null);
        }

        if(price.equals("")){
            etPrice.setError(String.valueOf(R.string.EmptyField));
            valid = false;
        } else {
            etPrice.setError(null);
        }

        if(description.equals("")){
            etDesc.setError(String.valueOf(R.string.EmptyField));
            valid = false;
        } else if(description.length() > 250){
            etDesc.setError("Description Max. 250 characters");
            valid = false;
        } else {
            etDesc.setError(null);
        }

        return valid;
    }
}
