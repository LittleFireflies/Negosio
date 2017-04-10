package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.content.Context;
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

public class EditProfileBoard extends AppCompatActivity implements View.OnClickListener{

    Context ctx;
    EditText etNama, etPassword, etRePassword, etEmail, etPhone;
    Spinner spCity, spSub;
    Button btnSave;

    String[][] arKota = {{"Klojen", "Blimbing", "Kedungkandang", "Lowokwaru", "Sukun"},
            {"Bululawang", "Dampit", "Dau", "Gondanglegi", "Kalipare", "Karangploso", "Kasembon", "Kepanjen", "Lawang", "Ngantang", "Pakis", "Pakisaji", "Pujon", "Sumbermanjing Wetan", "Singosari", "Sumberpucung", "Tumpang", "Turen", "Wonosari"},
            {"Batu", "Bumiaji", "Junrejo"}};
    ArrayList<String> listKota = new ArrayList<>();
    ArrayAdapter<String> adapterKota;
    ArrayAdapter<String> spinnerArrayAdapter;
    private DatabaseReference dbRef;
    private FirebaseAuth auth;
    private boolean valid;
    private String[] location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_profile_board);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ctx = getApplicationContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        assignToView();
        setSpinner();
        onSetView();
    }

    private void onSetView() {
        String[] city = new String[]{
                "Kota Malang",
                "Kabupaten Malang",
                "Kota Batu"
        };
        spinnerArrayAdapter = new ArrayAdapter<String>(EditProfileBoard.this, R.layout.spinner_item, city);
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
                        location = map.get("location").split(", ");
                        spCity.setSelection(getIndex(spCity, location[1]));
                        spSub.setSelection(getIndex(spSub, location[0]));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        btnSave.setOnClickListener(this);
    }

    private int getIndex(Spinner spinner, String value) {
        int index = 0;

        for(int i=0; i<spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).equals(value)){
                index = i;
            }
        }
        return index;
    }

    private void setSpinner() {
        adapterKota = new ArrayAdapter<>(EditProfileBoard.this, R.layout.spinner_item, listKota);
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
        etNama = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        spCity = (Spinner) findViewById(R.id.spKota);
        spSub = (Spinner) findViewById(R.id.spKecamatan);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRePassword = (EditText) findViewById(R.id.etRePassword);
        btnSave = (Button) findViewById(R.id.btnSaveProfile);
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
            dbRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Map<String, String> map = (Map<String, String>) snapshot.getValue();
                        if (map.get("username").equals(MainActivity.getUserLogin())) {
                            String name = etNama.getText().toString();
                            String email = etEmail.getText().toString();
                            String phone = etPhone.getText().toString();
                            String location = spSub.getSelectedItem().toString() + ", " + spCity.getSelectedItem().toString();
                            String password = etPassword.getText().toString();
                            String rePassword = etRePassword.getText().toString();

                            UserRegistration user = new UserRegistration();
                            if (password.equals("")) {
                                user.setUsername(MainActivity.getUserLogin());
                                user.setName(name);
                                user.setEmail(email);
                                user.setPhone(phone);
                                user.setLocation(location);
                                user.setPassword(map.get("password"));
                                user.setPict(map.get("pict"));
                                user.setToken(map.get("token"));
                                dbRef.child("User").child(snapshot.getKey()).setValue(user);
                                finish();
                            } else {
                                if (password.length() < 6) {
                                    etPassword.setError("Password must have at least 6 characters");
                                } else if (rePassword.equals("")) {
                                    etRePassword.setError(getText(R.string.EmptyField));
                                } else if (!password.equals(rePassword)) {
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
                                    user.setPict(map.get("pict"));
                                    user.setToken(map.get("token"));
                                    dbRef.child("User").child(snapshot.getKey()).setValue(user);
                                    finish();
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
        valid = true;
        String name = etNama.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String rePassword = etRePassword.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();

        if(name.equals("")){
            etNama.setError(getText(R.string.EmptyField));
            valid = false;
        } else {
            etNama.setError(null);
        }

        if(email.equals("")){
            etEmail.setError(getText(R.string.EmptyField));
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if(phone.equals("")){
            etPhone.setError(getText(R.string.EmptyField));
            valid = false;
        } else {
            etPhone.setError(null);
        }

        return valid;
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
