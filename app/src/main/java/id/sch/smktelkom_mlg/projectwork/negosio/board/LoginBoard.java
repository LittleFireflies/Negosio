package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.database.UserLogin;
import id.sch.smktelkom_mlg.projectwork.negosio.helper.LoginHelper;
import id.sch.smktelkom_mlg.projectwork.negosio.model.UserRegistration;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginBoard extends Fragment implements View.OnClickListener{

    View rootView;
    Context ctx;
    private DatabaseReference dbRef;
    private LoginHelper loginHelper;
    private Realm realm;
    private EditText etUsername, etPassword;
    private TextView etCoba;
    private Button btnLogin;

    ArrayList<UserRegistration> listUser = new ArrayList<>();

    public LoginBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ctx = getContext();
        Firebase.setAndroidContext(ctx);
        dbRef = FirebaseDatabase.getInstance().getReference();
        realm = Realm.getDefaultInstance();
        loginHelper = new LoginHelper(realm);
        assignToView();
        onSetView();

        return rootView;
    }

    private void assignToView() {
        etUsername = (EditText) rootView.findViewById(R.id.etUsername);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
        etCoba = (TextView) rootView.findViewById(R.id.etCoba);
        btnLogin = (Button) rootView.findViewById(R.id.btnLogin);
    }

    private void onSetView() {
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();
        final String[] tampil = {""};

        if(username.equals("")){
            etUsername.setError("Field Vacant");
        } else if(password.equals("")){
            etPassword.setError("Field Vacant");
        } else {
            dbRef.child("User").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshots : dataSnapshot.getChildren()){
                        try{
                            Map<String, String> map = (Map<String, String>) dataSnapshots.getValue();
                            String dbUsername = map.get("username");
                            String dbPassword = map.get("password");

                            if(dbUsername.equals(username) && dbPassword.equals(password)){
                                UserLogin obj = new UserLogin();
                                obj.setUsername(username);
                                obj.setPassword(password);
                                loginHelper.logIn(obj);

                                Toast.makeText(ctx, "Login Successfull", Toast.LENGTH_SHORT).show();
//                                ((MainActivity)ctx).displayView(R.string.ClassHome);

                                MainActivity mainActivity = (MainActivity) getActivity();
                                if(mainActivity != null){
                                    mainActivity.refreshActivity();
                                }
                            }
                        } catch (Exception e){
                            Toast.makeText(ctx, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    Toast.makeText(ctx, "Login Failed", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

}
