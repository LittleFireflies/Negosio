package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingBoard extends Fragment {
    View rootView;
    Context ctx;
    CardView about;

    public SettingBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_setting_board, container, false);
        ctx = getContext();
        about = (CardView) rootView.findViewById(R.id.about);
        onSetView();


        return rootView;
    }

    private void onSetView() {
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ctx.startActivity(new Intent(ctx, AboutBoard.class));
            }
        });
    }

}
