package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.Executor;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.model.UserRegistration;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterBoard extends Fragment implements View.OnClickListener{

    View rootView;
    Context ctx;
    private DatabaseReference dbRef;
    private FirebaseAuth firebaseAuth;
    private EditText etUsername, etPassword;
    private Button btnRegister;

    public RegisterBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.register_board, container, false);
        ctx = getContext();
        assignToView();
        onSetView();
        return rootView;
    }

    private void onSetView() {
        btnRegister.setOnClickListener(this);
    }

    private void assignToView() {
        //Firebase
        dbRef = FirebaseDatabase.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        etUsername = (EditText) rootView.findViewById(R.id.etUsername);
        etPassword = (EditText) rootView.findViewById(R.id.etPassword);
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
        final String username = etUsername.getText().toString().trim();
        final String password = etPassword.getText().toString().trim();

        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setUsername(username);
        userRegistration.setPassword(password);

        dbRef.child("User").child(username).setValue(userRegistration);
        Toast.makeText(ctx, "Register Success", Toast.LENGTH_SHORT).show();


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
