package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;

import static android.content.Context.MODE_PRIVATE;
import static id.sch.smktelkom_mlg.projectwork.negosio.manager.MyFirebaseInstanceIdService.MY_PREFERENCE;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginBoard extends Fragment implements View.OnClickListener{

    View rootView;
    Context ctx;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private EditText etEmail, etPassword;
    private TextView tvRegister;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    String token;
    String emailUser;

    public LoginBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ctx = getContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        SharedPreferences pref = ctx.getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);
        token = pref.getString("token", "");
        assignToView();
        onSetView();
        return rootView;
    }

    private void onSetView() {
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    private void assignToView() {
        progressDialog = new ProgressDialog(ctx);
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        tvRegister = (TextView) rootView.findViewById(R.id.tvRegister);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnLogin:
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                doLogin();
                break;
            case R.id.tvRegister:
                ((MainActivity)ctx).displayView(R.string.ClassRegister);
                break;
        }
    }

    private void doLogin() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if(isValid(email, password)){
            dbRef.child("User").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(final DataSnapshot snapshot : dataSnapshot.getChildren()){
                        final Map<String, String> dataUser = (Map<String, String>) snapshot.getValue();
                        if(email.equals(dataUser.get("email"))){
                            emailUser = dataUser.get("email");
                            try {
                                final String dbUsername = dataUser.get("username");
                                final String dbPassword = new String(Base64.decode(dataUser.get("password"), Base64.DEFAULT));
                                final String dbEmail = dataUser.get("email");

                                if(dbEmail.equals(email) && dbPassword.equals(password)){
                                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(!task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(ctx, "Login Failed : " + task.getException(), Toast.LENGTH_SHORT).show();
                                            } else {
                                                progressDialog.dismiss();
                                                user = auth.getCurrentUser();
                                                UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                                        .setDisplayName(dbUsername)
                                                        .build();
                                                user.updateProfile(profileUpdate).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful()){
                                                            dbRef.child("User").child(snapshot.getKey()).child("token").setValue(token);
                                                            MainActivity mainActivity = (MainActivity) getActivity();
                                                            mainActivity.refreshActivity();
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(ctx, "Your username or password does not match", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e){
                                progressDialog.dismiss();
                                e.printStackTrace();
                                Toast.makeText(ctx, "Your username or password does not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    if(emailUser == null){
                        progressDialog.dismiss();
                        Toast.makeText(ctx, "Your username or password does not match", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private boolean isValid(String username, String password) {
        boolean isValid = true;

        //validation username
        if(username.equals("")){
            etEmail.setError("Please enter your email");
            isValid = false;
        } else {
            etEmail.setError(null);
        }

        //validation password
        if(password.equals("")){
            etPassword.setError("Please enter your password");
            isValid = false;
        } else {
            etPassword.setError(null);
        }

        return isValid;
    }
}
