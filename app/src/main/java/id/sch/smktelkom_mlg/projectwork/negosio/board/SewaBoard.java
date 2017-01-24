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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import id.sch.smktelkom_mlg.projectwork.negosio.R;


public class SewaBoard extends Fragment implements View.OnClickListener {
    View rootView;
    Context ctx;
    EditText etProduct, etPrice, etDesc;
    Spinner spCategory;
    Button btnAdd;
    private DatabaseReference dbRef;
    private FirebaseAuth firebaseAuth;

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
        firebaseAuth = FirebaseAuth.getInstance();

        etProduct = (EditText) rootView.findViewById(R.id.etProduct);
        etPrice = (EditText) rootView.findViewById(R.id.etPrice);
        etDesc = (EditText) rootView.findViewById(R.id.etDesc);
        btnAdd = (Button) rootView.findViewById(R.id.btnAdd);
    }


    @Override
    public void onClick(View v) {

    }
}
