package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

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

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterBoard extends Fragment implements View.OnClickListener{

    View rootView;
    Context ctx;
    EditText etUsername, etName, etPassword, etRePassword, etEmail, etPhone;
    String[][] arKota = {{"Klojen", "Blimbing", "Kedungkandang", "Lowokwaru", "Sukun"},
                              {},
                              {"Batu", "Bumiaji", "Junrejo"}};
    ArrayList<String> listKota = new ArrayList<>();
    ArrayList<String> user = new ArrayList<>();
    ArrayAdapter<String> adapterKota;
    private DatabaseReference dbRef;
    private Spinner spCity, spSub;
    private Button btnRegister;

    public RegisterBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        ctx = getContext();
        assignToView();
        setSpinner();
        getUser();
        onSetView();
        return rootView;
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
        adapterKota = new ArrayAdapter<>(ctx, android.R.layout.simple_spinner_item, listKota);
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

    private void onSetView() {
        btnRegister.setOnClickListener(this);
    }

    private void assignToView() {
        //Firebase
        dbRef = FirebaseDatabase.getInstance().getReference();

        etUsername = (EditText) rootView.findViewById(R.id.etUsername);
        etName = (EditText) rootView.findViewById(R.id.etName);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        etRePassword = (EditText) rootView.findViewById(R.id.etRePassword);
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        etPhone = (EditText) rootView.findViewById(R.id.etPhone);
        spCity = (Spinner) rootView.findViewById(R.id.spKota) ;
        spSub = (Spinner) rootView.findViewById(R.id.spKecamatan);

        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnRegister:
                register();
                break;
        }
    }

    private void register() {
        if(isValid()){
            final String username = etUsername.getText().toString().trim();
            final String name = etName.getText().toString().trim();
            final String password = etPassword.getText().toString().trim();
            final String rePassword = etRePassword.getText().toString().trim();
            final String email = etEmail.getText().toString().trim();
            final String phone = etPhone.getText().toString().trim();
            final String location = spSub.getSelectedItem().toString() + ", " + spCity.getSelectedItem().toString();

            UserRegistration userRegistration = new UserRegistration();
            userRegistration.setUsername(username);
            userRegistration.setName(name);
            userRegistration.setPassword(Base64.encodeToString(password.getBytes(), Base64.DEFAULT));
            userRegistration.setEmail(email);
            userRegistration.setPhone(phone);
            userRegistration.setLocation(location);

            dbRef.child("User").child(username).setValue(userRegistration);
            Toast.makeText(ctx, "Register Success", Toast.LENGTH_SHORT).show();
            ((MainActivity)ctx).displayView(R.string.ClassLogin);
        }
    }

    private boolean isValid() {
        boolean isValid = true;
        String username = etUsername.getText().toString().trim();
        String name = etName.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String rePassword = etRePassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        //validation username
        if(username.equals("")){
            etUsername.setError(String.valueOf(R.string.EmptyField));
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
            etName.setError(String.valueOf(R.string.EmptyField));
            isValid = false;
        } else {
            etName.setError(null);
        }

        //password validation
        if(password.equals("")){
            etPassword.setError(String.valueOf(R.string.EmptyField));
            isValid = false;
        } else if(password.length() <6){
            etPassword.setError("Password must have at least 6 characters");
            isValid = false;
        } else {
            etPassword.setError(null);
        }

        //rePassword validation
        if(rePassword.equals("")){
            etRePassword.setError(String.valueOf(R.string.EmptyField));
            isValid = false;
        } else if(!rePassword.equals(password)){
            etRePassword.setError("Please check your password again");
            isValid = false;
        } else {
            etRePassword.setError(null);
        }

        //email validation
        if(email.equals("")){
            etEmail.setError(String.valueOf(R.string.EmptyField));
            isValid = false;
        } else {
            etEmail.setError(null);
        }

        //phone validation
        if(phone.equals("")){
            etPhone.setError(String.valueOf(R.string.EmptyField));
            isValid = false;
        } else {
            etPhone.setError(null);
        }

        return isValid;
    }



    private boolean isExist(final String username) {
        boolean exist = false;

        for (int i =0; i<user.size(); i++){
            if(user.get(i).equals(username)){
                exist = true;
            }
        }

        return exist;
    }
}
