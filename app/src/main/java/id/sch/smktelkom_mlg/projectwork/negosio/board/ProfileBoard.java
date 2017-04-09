package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
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
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;

public class ProfileBoard extends AppCompatActivity {

    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    DatabaseReference dbRef;
    StorageReference storageRef;
    TextView tvNama, tvEmail, tvPhone, tvLocation;
    ImageView ivProfile;
    Button btnSewa, btnEditProfile;
    FloatingActionButton editPict;
    Dialog dialog, dialogProfilePict;
    ImageView dialog_ivProfile, dialog_ivBack;
    ProgressDialog progressDialog;
    Uri downloadUri;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_board);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(MainActivity.getUserLogin());

        dbRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        assignToView();
        setDialog();

        dbRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(map.get("username").equals(MainActivity.getUserLogin())){
                        setTitle(map.get("username"));
                        tvNama.setText(map.get("name"));
                        tvEmail.setText(map.get("email"));
                        tvPhone.setText(map.get("phone"));
                        tvLocation.setText(map.get("location"));
                        PicassoClient.downloadProfilePict(getApplicationContext(), map.get("pict"), ivProfile);
                        key = snapshot.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnSewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileBoard.this, SewaBoard.class));
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(ProfileBoard.this, EditProfileBoard.class));
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogProfilePict.show();
                dbRef.child("User").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                            if(map.get("username").equals(MainActivity.getUserLogin())){
                                PicassoClient.downloadProfilePict(ProfileBoard.this, map.get("pict"), dialog_ivProfile);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                dialog_ivBack.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogProfilePict.dismiss();
                    }
                });
            }
        });
        editPict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = {"Take Photo", "Choose from Gallery", "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileBoard.this);
                builder.setTitle("Add Attachment");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(items[i].equals("Take Photo")){
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (ActivityCompat.checkSelfPermission(ProfileBoard.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
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
        });
    }

    private void setDialog() {
        dialogProfilePict = new Dialog(ProfileBoard.this);
        dialogProfilePict.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProfilePict.setContentView(R.layout.dialog_profile_picture);
        dialogProfilePict.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogProfilePict.setCanceledOnTouchOutside(false);
        dialogProfilePict.onBackPressed();

        dialog_ivProfile = (ImageView) dialogProfilePict.findViewById(R.id.ivProfilePict);
        dialog_ivBack = (ImageView) dialogProfilePict.findViewById(R.id.ivBack);
    }

    private void assignToView() {
        dialog = new Dialog(ProfileBoard.this);
        progressDialog = new ProgressDialog(ProfileBoard.this);

        tvNama = (TextView) findViewById(R.id.tvNama);
        tvEmail = (TextView) findViewById(R.id.tvEmail);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        ivProfile = (ImageView) findViewById(R.id.ivProfile);
        btnSewa = (Button) findViewById(R.id.btnSewa);
        btnEditProfile = (Button) findViewById(R.id.btnEditProfile);
        editPict = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
            StorageReference filePath = storageRef.child("Profile").child(MainActivity.getUserLogin()).child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(ProfileBoard.this, "Change Successfull", Toast.LENGTH_LONG).show();
                    dbRef.child("User").child(key).child("pict").setValue(String.valueOf(downloadUri));
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
            StorageReference filePath = storageRef.child("Profile").child(MainActivity.getUserLogin()).child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(ProfileBoard.this, "Change Successfull", Toast.LENGTH_LONG).show();
                    dbRef.child("User").child(key).child("pict").setValue(String.valueOf(downloadUri));
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ProfileBoard.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            });
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
}
