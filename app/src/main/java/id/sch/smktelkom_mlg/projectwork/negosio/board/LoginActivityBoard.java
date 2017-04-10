package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
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

import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;

import static id.sch.smktelkom_mlg.projectwork.negosio.manager.MyFirebaseInstanceIdService.MY_PREFERENCE;

public class LoginActivityBoard extends AppCompatActivity implements View.OnClickListener{

    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private FirebaseUser user;
    private EditText etEmail, etPassword;
    private TextView tvRegister;
    private Button btnLogin;
    private ProgressDialog progressDialog;
    String token;
    String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        dbRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        SharedPreferences pref = getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);
        token = pref.getString("token", "");
        assignToView();
        onSetView();
    }

    private void onSetView() {
        btnLogin.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
    }

    private void assignToView() {
        progressDialog = new ProgressDialog(LoginActivityBoard.this);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvRegister = (TextView) findViewById(R.id.tvRegister);
        btnLogin = (Button) findViewById(R.id.btnLogin);
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
                startActivity(new Intent(LoginActivityBoard.this, RegisterActivityBoard.class));
                finish();
                break;
        }
    }

    private void doLogin() {
        final String email = etEmail.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        if(isValid(email, password)){
            dbRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
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
                                    auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivityBoard.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if(!task.isSuccessful()){
                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(), "Login Failed : " + task.getException(), Toast.LENGTH_SHORT).show();
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
                                                            finish();
                                                            MainActivity.getInstance().finish();
                                                            startActivity(new Intent(LoginActivityBoard.this, MainActivity.class));
                                                        }
                                                    }
                                                });
                                            }
                                        }
                                    });
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Your username or password does not match", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e){
                                progressDialog.dismiss();
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "Your username or password does not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    if(emailUser == null){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Your username or password does not match", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            progressDialog.dismiss();
        }
    }

    private boolean isValid(String email, String password) {
        boolean isValid = true;

        //validation username
        if(email.equals("")){
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
