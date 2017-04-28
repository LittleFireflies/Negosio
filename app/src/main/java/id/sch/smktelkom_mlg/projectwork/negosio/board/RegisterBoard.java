package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterBoard extends Fragment implements View.OnClickListener{

    View rootView;
    Context ctx;
    EditText etUsername, etName, etPassword, etRePassword, etEmail, etPhone;
    String[][] arProv = {{"Jakarta Pusat", "Jakarta Utara", "Jakarta Timur", "Jakarta Selatan", "Jakarta Timur"},
                              {"Bandung", "Cimahi", "Depok", "Tasikmalaya", "Sukabumi", "Garut", "Bekasi", "Bogor"},
                              {"Semarang", "Solo", "Salatiga", "Boyolali", "Brebes", "Cilacap", "Demak"},
                              {"Yogyakarta", "Sleman", "Bantul"},
                              {"Surabaya", "Malang", "Kediri", "Blitar", "Tulungagung", "Jombang"}};
    ArrayList<String> listProv = new ArrayList<>();
    ArrayList<String> user = new ArrayList<>();
    ArrayAdapter<String> adapterProv;
    ArrayAdapter<String> spinnerArrayAdapter;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private Spinner spProv, spCity;
    private Button btnRegister;
    private ProgressDialog progressDialog;

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
        adapterProv = new ArrayAdapter<>(ctx, R.layout.spinner_item, listProv);
        adapterProv.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapterProv);

        spProv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                listProv.clear();
                listProv.addAll(Arrays.asList(arProv[pos]));
                adapterProv.notifyDataSetChanged();
                spCity.setSelection(0);
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
        auth = FirebaseAuth.getInstance();

        etUsername = (EditText) rootView.findViewById(R.id.etUsername);
        etName = (EditText) rootView.findViewById(R.id.etName);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        etRePassword = (EditText) rootView.findViewById(R.id.etRePassword);
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        etPhone = (EditText) rootView.findViewById(R.id.etPhone);
        spProv = (Spinner) rootView.findViewById(R.id.spProvinsi) ;
        spCity = (Spinner) rootView.findViewById(R.id.spKota);

        btnRegister = (Button) rootView.findViewById(R.id.btnRegister);
        progressDialog = new ProgressDialog(ctx);

        String[] city = new String[]{
                "DKI Jakarta",
                "Jawa Barat",
                "Jawa Tengah",
                "DI Yogyakarta",
                "Jawa Timur"
        };
        spinnerArrayAdapter = new ArrayAdapter<String>(ctx, R.layout.spinner_item, city);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProv.setAdapter(spinnerArrayAdapter);
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
        if(isValid()){
            final String username = etUsername.getText().toString().trim();
            final String name = etName.getText().toString().trim();
            final String password = etPassword.getText().toString().trim();
            final String rePassword = etRePassword.getText().toString().trim();
            final String email = etEmail.getText().toString().trim();
            final String phone = etPhone.getText().toString().trim();
            final String location = spCity.getSelectedItem().toString() + ", " + spProv.getSelectedItem().toString();

            final UserRegistration userRegistration = new UserRegistration();
            userRegistration.setUsername(username);
            userRegistration.setName(name);
            userRegistration.setPassword(Base64.encodeToString(password.getBytes(), Base64.DEFAULT));
            userRegistration.setEmail(email);
            userRegistration.setPhone(phone);
            userRegistration.setLocation(location);



            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(!task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(ctx, "Registration Failed." + task.getException(), Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        dbRef.child("User").push().setValue(userRegistration);
                        Toast.makeText(ctx, "Register Success", Toast.LENGTH_SHORT).show();
                        ((MainActivity)ctx).displayView(R.string.ClassLogin);
                    }
                }
            });


        } else {
//            Toast.makeText(ctx, "123", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
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
