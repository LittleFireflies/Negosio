package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationBoard extends Fragment implements View.OnClickListener{

    private View containerView;
    Context ctx;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToogle;
    private FragmentDrawerListener fragmentDrawerListener;

    public NavigationBoard() {
        // Required empty public constructor
    }

    public void setFragmentDrawerListener(FragmentDrawerListener listener){
        this.fragmentDrawerListener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerView = inflater.inflate(R.layout.navigationboard, container, false);
        ctx = getContext();

        return containerView;
    }

    @Override
    public void onClick(View view) {

    }

    public void setUp(int fragmentId, DrawerLayout drawer_layout, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        drawerLayout = drawer_layout;
        actionBarDrawerToogle = new ActionBarDrawerToggle(getActivity(), drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }



    public interface FragmentDrawerListener{
        public void onDrawerItemSelected(View view, int position);
        public void onDrawerProfileSelected(View view);
    }
}
