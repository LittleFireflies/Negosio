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

import id.sch.smktelkom_mlg.projectwork.negosio.R;

public class HomeBoard extends Fragment implements View.OnClickListener{

    View rootView;
    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_board, container, false);
        ctx = getContext();
        assignToView();
        onSetView();

        return rootView;
    }

    private void onSetView() {
    }

    private void assignToView() {
    }

    @Override
    public void onClick(View view) {

    }
}
