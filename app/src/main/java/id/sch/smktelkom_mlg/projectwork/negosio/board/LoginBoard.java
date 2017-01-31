package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    ArrayList<UserRegistration> listUser = new ArrayList<>();
    private DatabaseReference dbRef;
    private LoginHelper loginHelper;
    private Realm realm;
    private EditText etUsername, etPassword;
    private Button btnLogin;

    public LoginBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        ctx = getContext();
        dbRef = FirebaseDatabase.getInstance().getReference();
        realm = Realm.getDefaultInstance();
        loginHelper = new LoginHelper(realm);
        assignToView();
        onSetView();

//        rootView.findViewById(R.id.etPassword).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                etPassword.setText("");
//                etPassword.setHint("Password");
//                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//
//
//            }
//        });
        return rootView;
    }

    private void assignToView() {
        etUsername = (EditText) rootView.findViewById(R.id.etUsername);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
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

        if(isValid(username, password)){
            dbRef.child("User").child(username).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        Map<String, String> map = (Map<String, String>) dataSnapshot.getValue();
                        String dbUsername = map.get("username");
                        String dbPassword = new String(Base64.decode(map.get("password"), Base64.DEFAULT));

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
                        } else {
                            Toast.makeText(ctx, "Your username or password does not match", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e){
                        Toast.makeText(ctx, "Your username or password does not match", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private boolean isValid(String username, String password) {
        boolean isValid = true;

        //validation username
        if(username.equals("")){
            etUsername.setError("Please enter your username");
            isValid = false;
        } else {
            etUsername.setError(null);
        }

        //validation password
        if(password.equals("")){
            etPassword.setError("Please enter your password");
            isValid = false;
        } else {
            etPassword.setError(null);
        }

        return isValid;
    }

}
