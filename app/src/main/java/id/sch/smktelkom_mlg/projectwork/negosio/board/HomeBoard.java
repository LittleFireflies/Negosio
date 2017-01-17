package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.database.UserLogin;
import id.sch.smktelkom_mlg.projectwork.negosio.helper.LoginHelper;
import io.realm.Realm;

public class HomeBoard extends Fragment implements View.OnClickListener{

    View rootView;
    Context ctx;
    Realm realm;
    LoginHelper loginHelper;
    private String username = "";
    private TextView tvUsername;
    private int size = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_board, container, false);
        ctx = getContext();
        realm = Realm.getDefaultInstance();
        loginHelper = new LoginHelper(realm);
        assignToView();
        getUserData();
        onSetView();

        return rootView;
    }

    private void getUserData() {
        ArrayList<UserLogin> result = loginHelper.getUserLogin();
        size = result.size();
        for(int i=0; i<result.size(); i++){
            username = result.get(i).getUsername();
        }
    }

    private void onSetView() {
        tvUsername.setText(String.valueOf(size));
    }

    private void assignToView() {
        tvUsername = (TextView) rootView.findViewById(R.id.tvUser);
    }

    @Override
    public void onClick(View view) {

    }
}
