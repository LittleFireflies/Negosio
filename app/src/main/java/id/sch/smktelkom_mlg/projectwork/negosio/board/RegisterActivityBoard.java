package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.model.UserRegistration;

public class RegisterActivityBoard extends AppCompatActivity implements View.OnClickListener{

    EditText etUsername, etName, etPassword, etRePassword, etEmail, etPhone;
    String[][] arKota = {{"Klojen", "Blimbing", "Kedungkandang", "Lowokwaru", "Sukun"},
            {"Bululawang", "Dampit", "Dau", "Gondanglegi", "Kalipare", "Karangploso", "Kasembon", "Kepanjen", "Lawang", "Ngantang", "Pakis", "Pakisaji", "Pujon", "Sumbermanjing Wetan", "Singosari", "Sumberpucung", "Tumpang", "Turen", "Wonosari"},
            {"Batu", "Bumiaji", "Junrejo"}};
    ArrayList<String> listKota = new ArrayList<>();
    ArrayList<String> user = new ArrayList<>();
    ArrayAdapter<String> adapterKota;
    ArrayAdapter<String> spinnerArrayAdapter;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private Spinner spCity, spSub;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private boolean valid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        assignToView();
        setSpinner();
        getUser();
        onSetView();
    }

    private void onSetView() {
        btnRegister.setOnClickListener(this);
    }

    private void getUser() {
        dbRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    String dbUsername = map.get("username");
                    user.add(dbUsername);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setSpinner() {
        adapterKota = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_item, listKota);
        adapterKota.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSub.setAdapter(adapterKota);

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                listKota.clear();
                listKota.addAll(Arrays.asList(arKota[pos]));
                adapterKota.notifyDataSetChanged();
                spSub.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void assignToView() {
        //Firebase
        dbRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        etUsername = (EditText) findViewById(R.id.etUsername);
        etName = (EditText) findViewById(R.id.etName);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        spCity = (Spinner) findViewById(R.id.spKota) ;
        spSub = (Spinner) findViewById(R.id.spKecamatan);

        btnRegister = (Button) findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(RegisterActivityBoard.this);

        String[] city = new String[]{
                "Kota Malang",
                "Kabupaten Malang",
                "Kota Batu"
        };
        spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, city);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(spinnerArrayAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                progressDialog.setMessage("Please wait...");
                progressDialog.show();
                register();
                break;
        }
    }

    private void register() {
        if (isValid()) {
            final String username = etUsername.getText().toString().trim();
            final String name = etName.getText().toString().trim();
            final String password = etPassword.getText().toString().trim();
            final String rePassword = etRePassword.getText().toString().trim();
            final String email = etEmail.getText().toString().trim();
            final String phone = etPhone.getText().toString().trim();
            final String location = spSub.getSelectedItem().toString() + ", " + spCity.getSelectedItem().toString();

            final UserRegistration userRegistration = new UserRegistration();
            userRegistration.setUsername(username);
            userRegistration.setName(name);
            userRegistration.setPassword(Base64.encodeToString(password.getBytes(), Base64.DEFAULT));
            userRegistration.setEmail(email);
            userRegistration.setPhone(phone);
            userRegistration.setLocation(location);


            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivityBoard.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (!task.isSuccessful()) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Registration Failed." + task.getException(), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        dbRef.child("User").push().setValue(userRegistration);
                        Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivityBoard.this, LoginActivityBoard.class));
                    }
                }
            });


        } else {
            progressDialog.dismiss();
        }
    }

    public boolean isValid() {
        boolean isValid = true;
        String username = etUsername.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String rePassword = etRePassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        //validation username
        if(username.equals("")){
            etUsername.setError(getText(R.string.EmptyField));
            isValid = false;
        } else if(username.length() < 6 || username.length() > 20){
            etUsername.setError("Your username must have 6-20 characters");
            isValid = false;
        } else if(isExist(username)){
            etUsername.setError("Username already exist");
            isValid = false;
        } else {
            etUsername.setError(null);
        }

        //Full Name validation
        if (name.equals("")){
            etName.setError(getText(R.string.EmptyField));
            isValid = false;
        } else {
            etName.setError(null);
        }

        //password validation
        if(password.equals("")){
            etPassword.setError(getText(R.string.EmptyField));
            isValid = false;
        } else if(password.length() <6){
            etPassword.setError("Password must have at least 6 characters");
            isValid = false;
        } else {
            etPassword.setError(null);
        }

        //rePassword validation
        if(rePassword.equals("")){
            etRePassword.setError(getText(R.string.EmptyField));
            isValid = false;
        } else if(!rePassword.equals(password)){
            etRePassword.setError("Please check your password again");
            isValid = false;
        } else {
            etRePassword.setError(null);
        }

        //email validation
        if(email.equals("")){
            etEmail.setError(getText(R.string.EmptyField));
            isValid = false;
        } else {
            etEmail.setError(null);
        }

        //phone validation
        if(phone.equals("")){
            etPhone.setError(getText(R.string.EmptyField));
            isValid = false;
        } else {
            etPhone.setError(null);
        }

        return isValid;
    }

    private boolean isExist(String username) {
        boolean exist = false;

        for (int i =0; i<user.size(); i++){
            if(user.get(i).equals(username)){
                exist = true;
            }
        }

        return exist;
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
