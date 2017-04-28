package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.content.Context;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Booking;
import id.sch.smktelkom_mlg.projectwork.negosio.model.UserRegistration;

public class EditProfileBoard extends AppCompatActivity implements View.OnClickListener{

    Context ctx;
    EditText etNama, etPassword, etRePassword, etEmail, etPhone;
    Spinner spProv, spCity;
    Button btnSave;

    String[][] arProv = {{"Jakarta Pusat", "Jakarta Utara", "Jakarta Timur", "Jakarta Selatan", "Jakarta Timur"},
            {"Bandung", "Cimahi", "Depok", "Tasikmalaya", "Sukabumi", "Garut", "Bekasi", "Bogor"},
            {"Semarang", "Solo", "Salatiga", "Boyolali", "Brebes", "Cilacap", "Demak"},
            {"Yogyakarta", "Sleman", "Bantul"},
            {"Surabaya", "Malang", "Kediri", "Blitar", "Tulungagung", "Jombang"}};
    ArrayList<String> listProv = new ArrayList<>();
    ArrayAdapter<String> adapterProv;
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
                "DKI Jakarta",
                "Jawa Barat",
                "Jawa Tengah",
                "DI Yogyakarta",
                "Jawa Timur"
        };
        spinnerArrayAdapter = new ArrayAdapter<String>(EditProfileBoard.this, R.layout.spinner_item, city);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spProv.setAdapter(spinnerArrayAdapter);

        dbRef.child("User").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(map.get("username").equals(MainActivity.getUserLogin())){
                        etNama.setText(map.get("name"));
                        etEmail.setText(map.get("email"));
                        etPhone.setText(map.get("phone"));
                        location = map.get("location").split(", ");
                        spProv.setSelection(getIndex(spProv, location[1]));
                        spCity.setSelection(getIndex(spCity, location[0]));
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
        adapterProv = new ArrayAdapter<>(EditProfileBoard.this, R.layout.spinner_item, listProv);
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

    private void assignToView() {
        etNama = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPhone = (EditText) findViewById(R.id.etPhone);
        spProv = (Spinner) findViewById(R.id.spProvinsi);
        spCity = (Spinner) findViewById(R.id.spKota);
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
                    for (final DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Map<String, String> map = (Map<String, String>) snapshot.getValue();
                        if (map.get("username").equals(MainActivity.getUserLogin())) {
                            String name = etNama.getText().toString();
                            String email = etEmail.getText().toString();
                            final String phone = etPhone.getText().toString();
                            final String location = spCity.getSelectedItem().toString() + ", " + spProv.getSelectedItem().toString();
                            String password = etPassword.getText().toString();
                            String rePassword = etRePassword.getText().toString();

                            final UserRegistration user = new UserRegistration();
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
                                dbRef.child("Barang").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                                            if(map.get("username").equals(MainActivity.getUserLogin())){
                                                Barang barang = new Barang();
                                                barang.setUsername(map.get("username"));
                                                barang.setProductname(map.get("productname"));
                                                barang.setPrice(map.get("price"));
                                                barang.setDescription(map.get("description"));
                                                barang.setImg(map.get("img"));
                                                barang.setPhone(phone);
                                                barang.setLocation(location);
                                                barang.setCategory(map.get("category"));
                                                barang.setType(map.get("type"));
                                                barang.setDate(map.get("date"));
                                                dbRef.child("Barang").child(snapshot.getKey()).setValue(barang);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                dbRef.child("Booking").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                                            if(map.get("buyer").equals(MainActivity.getUserLogin())){
                                                Booking booking = new Booking();
                                                booking.setProduct_name(map.get("product_name"));
                                                booking.setTotal(map.get("total"));
                                                booking.setCategory(map.get("category"));
                                                booking.setStart_date(map.get("start_date"));
                                                booking.setEnd_date(map.get("end_date"));
                                                booking.setPrice(map.get("price"));
                                                booking.setTime(map.get("time"));
                                                booking.setTgl_booking(map.get("tgl_booking"));
                                                booking.setBuyer(map.get("buyer"));
                                                booking.setBuyer_phone(phone);
                                                booking.setBuyer_location(location);
                                                booking.setRenter_token(map.get("renter_token"));
                                                booking.setSeller(map.get("seller"));
                                                booking.setSeller_phone(map.get("seller_phone"));
                                                booking.setSeller_location(map.get("seller_location"));
                                                booking.setOwner_token(map.get("owner_token"));
                                                booking.setImg(map.get("img"));
                                                booking.setStatus(map.get("status"));
                                                booking.setReason(map.get("reason"));
                                                dbRef.child("Booking").child(snapshot.getKey()).setValue(booking);
                                            }
                                            if(map.get("seller").equals(MainActivity.getUserLogin())){
                                                Booking booking = new Booking();
                                                booking.setProduct_name(map.get("product_name"));
                                                booking.setTotal(map.get("total"));
                                                booking.setCategory(map.get("category"));
                                                booking.setStart_date(map.get("start_date"));
                                                booking.setEnd_date(map.get("end_date"));
                                                booking.setPrice(map.get("price"));
                                                booking.setTime(map.get("time"));
                                                booking.setTgl_booking(map.get("tgl_booking"));
                                                booking.setBuyer(map.get("buyer"));
                                                booking.setBuyer_phone(map.get("buyer_phone"));
                                                booking.setBuyer_location(map.get("buyer_location"));
                                                booking.setRenter_token(map.get("renter_token"));
                                                booking.setSeller(map.get("seller"));
                                                booking.setSeller_phone(phone);
                                                booking.setSeller_location(location);
                                                booking.setOwner_token(map.get("owner_token"));
                                                booking.setImg(map.get("img"));
                                                booking.setStatus(map.get("status"));
                                                booking.setReason(map.get("reason"));
                                                dbRef.child("Booking").child(snapshot.getKey()).setValue(booking);
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
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
                                    FirebaseUser userAuth = FirebaseAuth.getInstance().getCurrentUser();
                                    userAuth.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                dbRef.child("User").child(snapshot.getKey()).setValue(user);
                                                dbRef.child("Barang").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                                                            if(map.get("username").equals(MainActivity.getUserLogin())){
                                                                Barang barang = new Barang();
                                                                barang.setUsername(map.get("username"));
                                                                barang.setProductname(map.get("productname"));
                                                                barang.setPrice(map.get("price"));
                                                                barang.setDescription(map.get("description"));
                                                                barang.setImg(map.get("img"));
                                                                barang.setPhone(phone);
                                                                barang.setLocation(location);
                                                                barang.setCategory(map.get("category"));
                                                                barang.setType(map.get("type"));
                                                                barang.setDate(map.get("date"));
                                                                dbRef.child("Barang").child(snapshot.getKey()).setValue(barang);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                dbRef.child("Booking").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                        for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                                                            Map<String, String> map = (Map<String, String>) snapshot.getValue();
                                                            if(map.get("buyer").equals(MainActivity.getUserLogin())){
                                                                Booking booking = new Booking();
                                                                booking.setProduct_name(map.get("product_name"));
                                                                booking.setTotal(map.get("total"));
                                                                booking.setCategory(map.get("category"));
                                                                booking.setStart_date(map.get("start_date"));
                                                                booking.setEnd_date(map.get("end_date"));
                                                                booking.setPrice(map.get("price"));
                                                                booking.setTime(map.get("time"));
                                                                booking.setTgl_booking(map.get("tgl_booking"));
                                                                booking.setBuyer(map.get("buyer"));
                                                                booking.setBuyer_phone(phone);
                                                                booking.setBuyer_location(location);
                                                                booking.setRenter_token(map.get("renter_token"));
                                                                booking.setSeller(map.get("seller"));
                                                                booking.setSeller_phone(map.get("seller_phone"));
                                                                booking.setSeller_location(map.get("seller_location"));
                                                                booking.setOwner_token(map.get("owner_token"));
                                                                booking.setImg(map.get("img"));
                                                                booking.setStatus(map.get("status"));
                                                                booking.setReason(map.get("reason"));
                                                                dbRef.child("Booking").child(snapshot.getKey()).setValue(booking);
                                                            }
                                                            if(map.get("seller").equals(MainActivity.getUserLogin())){
                                                                Booking booking = new Booking();
                                                                booking.setProduct_name(map.get("product_name"));
                                                                booking.setTotal(map.get("total"));
                                                                booking.setCategory(map.get("category"));
                                                                booking.setStart_date(map.get("start_date"));
                                                                booking.setEnd_date(map.get("end_date"));
                                                                booking.setPrice(map.get("price"));
                                                                booking.setTime(map.get("time"));
                                                                booking.setTgl_booking(map.get("tgl_booking"));
                                                                booking.setBuyer(map.get("buyer"));
                                                                booking.setBuyer_phone(map.get("buyer_phone"));
                                                                booking.setBuyer_location(map.get("buyer_location"));
                                                                booking.setRenter_token(map.get("renter_token"));
                                                                booking.setSeller(map.get("seller"));
                                                                booking.setSeller_phone(phone);
                                                                booking.setSeller_location(location);
                                                                booking.setOwner_token(map.get("owner_token"));
                                                                booking.setImg(map.get("img"));
                                                                booking.setStatus(map.get("status"));
                                                                booking.setReason(map.get("reason"));
                                                                dbRef.child("Booking").child(snapshot.getKey()).setValue(booking);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(DatabaseError databaseError) {

                                                    }
                                                });
                                                finish();
                                            } else {
                                                Toast.makeText(EditProfileBoard.this, "Failed to update password: " + task.getException() , Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

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
