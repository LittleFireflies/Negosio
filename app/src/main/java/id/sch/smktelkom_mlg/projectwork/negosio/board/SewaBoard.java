package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.database.UserLogin;
import id.sch.smktelkom_mlg.projectwork.negosio.helper.LoginHelper;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;
import io.realm.Realm;


public class SewaBoard extends Fragment implements View.OnClickListener {
    View rootView;
    Context ctx;
    EditText etProduct, etPrice, etDesc;
    Spinner spCategory, spType;
    Button btnAdd;
    private DatabaseReference dbRef;
    private Realm realm;
    private LoginHelper loginHelper;
    //TextView hasil;

    public SewaBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sewa, container, false);
        ctx = getContext();
        assignToView();
        onSetView();
        return rootView;
    }

    private void onSetView() {
        btnAdd.setOnClickListener(this);
    }

    private void assignToView() {
        //Firebase
        dbRef = FirebaseDatabase.getInstance().getReference();

        realm = Realm.getDefaultInstance();
        loginHelper = new LoginHelper(realm);

        etProduct = (EditText) rootView.findViewById(R.id.etProduct);
        etPrice = (EditText) rootView.findViewById(R.id.etPrice);
        etDesc = (EditText) rootView.findViewById(R.id.etDesc);
        spCategory = (Spinner) rootView.findViewById(R.id.spCategory);
        spType = (Spinner) rootView.findViewById(R.id.sptype);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);

        //hasil = (TextView) rootView.findViewById(R.id.textView5);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                add();
                break;
        }
    }

    private void add1() {
        //hasil.setText(etProduct.getText().toString() + " " + etPrice.getText().toString() + " " + etDesc.getText().toString() + " " + spCategory.getSelectedItemPosition());
    }

    private void add() {
        if (etProduct.getText().toString().equals("")
                || etPrice.getText().toString().equals("")
                || etDesc.getText().toString().equals("")) {
            Toast.makeText(ctx, "Field Vacant", Toast.LENGTH_SHORT).show();
        } else {
            final String productname = etProduct.getText().toString().trim();
            final int price = Integer.parseInt(etPrice.getText().toString());
            final String description = etDesc.getText().toString().trim();
            int category = 0;
            int type = 0;
            String username = "";
            type = spType.getSelectedItemPosition();
            category = spCategory.getSelectedItemPosition();

            try {
                ArrayList<UserLogin> login = new ArrayList<>();
                login = loginHelper.getUserLogin();
                for (int i = 0; i < login.size(); i++) {
                    username = login.get(i).getUsername();
                }

                Barang barang = new Barang();
                barang.setProductname(productname);
                barang.setPrice(price);
                barang.setDescription(description);
                barang.setCategory(category);
                barang.setType(type);
                barang.setUsername(username);

                dbRef.child("Barang").push().setValue(barang);
                Toast.makeText(ctx, "Add Success", Toast.LENGTH_SHORT).show();
                ((MainActivity) ctx).displayView(R.string.ClassHome);
            } catch (Exception e) {

            }
        }
    }
}
