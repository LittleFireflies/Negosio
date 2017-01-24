package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;

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
    ArrayAdapter<String> adapterKota;
    private DatabaseReference dbRef;
    private FirebaseAuth firebaseAuth;
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
        onSetView();
        return rootView;
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
        firebaseAuth = FirebaseAuth.getInstance();

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

        if(etUsername.getText().toString().equals("")
                || etName.getText().toString().equals("")
                || etPassword.getText().toString().equals("")
                || etRePassword.getText().toString().equals("")
                || etEmail.getText().toString().equals("")
                || etPhone.getText().toString().equals("")){
            Toast.makeText(ctx, "Field Vacant", Toast.LENGTH_SHORT).show();
        } else if(!etPassword.getText().toString().equals(etRePassword.getText().toString())){
            Toast.makeText(ctx, "Please check your password", Toast.LENGTH_SHORT).show();
            etRePassword.setError("Password doesn't match");
        } else {
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
            userRegistration.setPassword(password);
            userRegistration.setEmail(email);
            userRegistration.setPhone(phone);
            userRegistration.setLocation(location);

            dbRef.child("User").child(username).setValue(userRegistration);
            Toast.makeText(ctx, "Register Success", Toast.LENGTH_SHORT).show();
            ((MainActivity)ctx).displayView(R.string.ClassLogin);
        }




//        firebaseAuth.createUserWithEmailAndPassword(username, password)
//                .addOnCompleteListener((Activity) ctx, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            UserRegistration userRegistration = new UserRegistration();
//                            userRegistration.setUsername(username);
//                            userRegistration.setPassword(password);
//
//                            dbRef.child("User").push().setValue(userRegistration);
//
////                            ((MainActivity)ctx).displayView(R.string.ClassLogin);
//                            Toast.makeText(ctx, "Register Successfull", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(ctx, "Register Failed", Toast.LENGTH_SHORT).show();
//
//                        }
//                    }
//                });
    }
}
