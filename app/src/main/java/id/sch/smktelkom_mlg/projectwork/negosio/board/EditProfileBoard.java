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
public class EditProfileBoard extends Fragment implements View.OnClickListener{
    View rootView;
    Context ctx;
    EditText etNama, etPassword, etRePassword, etEmail, etPhone;
    Spinner spCity, spSub;
    Button btnSave;
    String[][] arKota = {{"Klojen", "Blimbing", "Kedungkandang", "Lowokwaru", "Sukun"},
            {},
            {"Batu", "Bumiaji", "Junrejo"}};
    ArrayList<String> listKota = new ArrayList<>();
    ArrayAdapter<String> adapterKota;
    ArrayAdapter<String> spinnerArrayAdapter;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;

    public EditProfileBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_edit_profile_board, container, false);
        ctx = getContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        assignToView();
        setSpinner();
        onSetView();


        return rootView;
    }

    private void onSetView() {
        String[] city = new String[]{
                "Kota Malang",
                "Kabupaten Malang",
                "Kota Batu"
        };
        spinnerArrayAdapter = new ArrayAdapter<String>(ctx, R.layout.spinner_item, city);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(spinnerArrayAdapter);

        dbRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(map.get("username").equals(MainActivity.getUserLogin())){
                        etNama.setText(map.get("name"));
                        etEmail.setText(map.get("email"));
                        etPhone.setText(map.get("phone"));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnSave.setOnClickListener(this);
    }

    private void assignToView() {
        etNama = (EditText) rootView.findViewById(R.id.etName);
        etEmail = (EditText) rootView.findViewById(R.id.etEmail);
        etPhone = (EditText) rootView.findViewById(R.id.etPhone);
        spCity = (Spinner) rootView.findViewById(R.id.spKota);
        spSub = (Spinner) rootView.findViewById(R.id.spKecamatan);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        etRePassword = (EditText) rootView.findViewById(R.id.etRePassword);
        btnSave = (Button) rootView.findViewById(R.id.btnSaveProfile);
    }

    private void setSpinner() {
        adapterKota = new ArrayAdapter<>(ctx, R.layout.spinner_item, listKota);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnSaveProfile:
                editProfile();
                break;
        }
    }

    private void editProfile() {
        if (isValid()) {
            dbRef.child("User").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Map<String, String> map = (Map<String, String>) snapshot.getValue();
                        if(map.get("username").equals(MainActivity.getUserLogin())){
                            String name = etNama.getText().toString();
                            String email = etEmail.getText().toString();
                            String phone = etPhone.getText().toString();
                            String location = spSub.getSelectedItem().toString() + ", " + spCity.getSelectedItem().toString();
                            String password = etPassword.getText().toString();
                            String rePassword = etRePassword.getText().toString();

                            UserRegistration user = new UserRegistration();
                            if(password.equals("")){
                                user.setUsername(MainActivity.getUserLogin());
                                user.setName(name);
                                user.setEmail(email);
                                user.setPhone(phone);
                                user.setLocation(location);
                                user.setPassword(map.get("password"));
                                dbRef.child("User").child(snapshot.getKey()).setValue(user);
                                ((MainActivity)ctx).displayView(R.string.ClassHome);
                            } else {
                                if(password.length() < 6){
                                    etPassword.setError("Password must have at least 6 characters");
                                } else if(rePassword.equals("")){
                                    etRePassword.setError(String.valueOf(R.string.EmptyField));
                                } else if(!password.equals(rePassword)){
                                    etRePassword.setError("Password does not match");
                                } else {
                                    etPassword.setError(null);
                                    etRePassword.setError(null);
                                    user.setUsername(MainActivity.getUserLogin());
                                    user.setName(name);
                                    user.setEmail(email);
                                    user.setPhone(phone);
                                    user.setLocation(location);
                                    user.setPassword(Base64.encodeToString(password.getBytes(), Base64.DEFAULT));
                                    dbRef.child("User").child(snapshot.getKey()).setValue(user);
                                    ((MainActivity)ctx).displayView(R.string.ClassHome);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public boolean isValid() {
        boolean isValid = true;
        String name = etNama.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String rePassword = etRePassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if(name.equals("")){
            etNama.setError(String.valueOf(R.string.EmptyField));
            isValid = false;
        } else {
            etNama.setError(null);
        }

        if(email.equals("")){
            etEmail.setError(String.valueOf(R.string.EmptyField));
            isValid = false;
        } else {
            etEmail.setError(null);
        }

        if(phone.equals("")){
            etPhone.setError(String.valueOf(R.string.EmptyField));
            isValid = false;
        } else {
            etPhone.setError(null);
        }

        return isValid;
    }
}
