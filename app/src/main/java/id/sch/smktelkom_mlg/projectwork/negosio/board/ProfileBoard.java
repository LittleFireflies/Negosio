package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileBoard extends Fragment {

    DatabaseReference dbRef;
    Context ctx;
    View rootView;
    TextView tvUsername, tvNama, tvEmail, tvPhone;
    Button btnSewa;
    private String username = MainActivity.getUserLogin();

    public ProfileBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile_board, container, false);
        ctx = getContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        assignToView();
        onSetView();
        return rootView;
    }

    private void onSetView() {
        dbRef.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(username.equals(map.get("username"))){
                        tvUsername.setText(map.get("username"));
                        tvNama.setText(map.get("name"));
                        tvEmail.setText(map.get("email"));
                        tvPhone.setText(map.get("phone"));
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSewa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)ctx).displayView(R.string.ClassSewa);
            }
        });
    }

    private void assignToView() {
        tvUsername = (TextView) rootView.findViewById(R.id.tvUsername);
        tvNama = (TextView) rootView.findViewById(R.id.tvNama);
        tvEmail = (TextView) rootView.findViewById(R.id.tvEmail);
        tvPhone = (TextView) rootView.findViewById(R.id.tvPhone);
        btnSewa = (Button) rootView.findViewById(R.id.btnSewa);
    }

}
