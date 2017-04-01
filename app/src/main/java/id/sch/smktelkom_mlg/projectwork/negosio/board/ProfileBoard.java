package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import id.sch.smktelkom_mlg.projectwork.negosio.model.UserRegistration;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileBoard extends Fragment {

    public static final int REQUEST_CAMERA = 1;
    public static final int SELECT_FILE = 2;
    DatabaseReference dbRef;
    StorageReference storageRef;
    Context ctx;
    View rootView;
    Dialog dialog, dialogProfilePict;
    ProgressDialog progressDialog;
    TextView tvUsername, tvNama, tvEmail, tvPhone, tvLocation;
    Button btnSewa, btnEditProfile;
    ImageView ivProfile, dialog_ivProfile, dialog_ivBack;
    Uri downloadUri;
    String key;
    private String username = MainActivity.getUserLogin();

    public ProfileBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile_board, container, false);
        ctx = getContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        assignToView();
        onSetView();
        setDialog();
        return rootView;
    }

    private void setDialog() {
        dialogProfilePict = new Dialog(ctx);
        dialogProfilePict.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogProfilePict.setContentView(R.layout.dialog_profile_picture);
        dialogProfilePict.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogProfilePict.setCanceledOnTouchOutside(false);
        dialogProfilePict.onBackPressed();

        dialog_ivProfile = (ImageView) dialogProfilePict.findViewById(R.id.ivProfilePict);
        dialog_ivBack = (ImageView) dialogProfilePict.findViewById(R.id.ivBack);
    }

    private void onSetView() {
        dbRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(username.equals(map.get("username"))){
                        tvUsername.setText(map.get("username"));
                        tvNama.setText(map.get("name"));
                        tvEmail.setText(map.get("email"));
                        tvPhone.setText(map.get("phone"));
                        tvLocation.setText(map.get("location"));
                        PicassoClient.downloadProfilePict(ctx, map.get("pict"), ivProfile);
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
                ((MainActivity)ctx).displayView(R.string.ClassSewa);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)ctx).displayView(R.string.ClassEditProfile);
            }
        });
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String[] items = {"Select Photo", "Take Photo", "Choose from Gallery", "Cancel"};

                AlertDialog.Builder builder = new AlertDialog.Builder((ctx));
                builder.setTitle("Add Attachment");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(items[i].equals("Select Photo")){
                            dialogProfilePict.show();
                            dbRef.child("User").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                        Map<String, String> map = (Map<String, String>) snapshot.getValue();
                                        if(map.get("username").equals(MainActivity.getUserLogin())){
                                            PicassoClient.downloadProfilePict(ctx, map.get("pict"), dialog_ivProfile);
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
                        } else if(items[i].equals("Take Photo")){
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
        });
    }

    private void assignToView() {
        dialog = new Dialog(ctx);
        progressDialog = new ProgressDialog(ctx);

        tvUsername = (TextView) rootView.findViewById(R.id.tvUsername);
        tvNama = (TextView) rootView.findViewById(R.id.tvNama);
        tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);
        tvPhone = (TextView) rootView.findViewById(R.id.tvPhone);
        tvLocation = (TextView) rootView.findViewById(R.id.tvLocation);
        btnSewa = (Button) rootView.findViewById(R.id.btnSewa);
        btnEditProfile = (Button) rootView.findViewById(R.id.btnEditProfile);
        ivProfile = (ImageView) rootView.findViewById(R.id.ivProfile);
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
            StorageReference filePath = storageRef.child("Profile").child(username).child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(ctx, "Change Successfull", Toast.LENGTH_LONG).show();
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
            StorageReference filePath = storageRef.child("Profile").child(username).child(uri.getLastPathSegment());
            filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    downloadUri = taskSnapshot.getDownloadUrl();
                    Toast.makeText(ctx, "Change Successfull", Toast.LENGTH_LONG).show();
                    dbRef.child("User").child(key).child("pict").setValue(String.valueOf(downloadUri));
                    progressDialog.dismiss();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(ctx, String.valueOf(e), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
