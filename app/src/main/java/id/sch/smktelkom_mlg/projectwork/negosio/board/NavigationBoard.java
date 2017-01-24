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
import android.widget.LinearLayout;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.database.UserLogin;
import id.sch.smktelkom_mlg.projectwork.negosio.helper.LoginHelper;
import io.realm.Realm;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavigationBoard extends Fragment implements View.OnClickListener{

    Context ctx;
    LinearLayout logged, unlogged;
    LinearLayout llLogin, llRegister, llSewa;
    LinearLayout llLogout;
    private View containerView;
    private Realm realm;
    private LoginHelper loginHelper;
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
        assignView();
        onSetView();

        return containerView;
    }

    private void onSetView() {
        setNavigationBoard();

        llLogin.setOnClickListener(this);
        llRegister.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        llSewa.setOnClickListener(this);
    }

    private void setNavigationBoard() {
        realm = Realm.getDefaultInstance();
        loginHelper = new LoginHelper(realm);

        ArrayList<UserLogin> login = new ArrayList<>();

        login = loginHelper.getUserLogin();

        if(login.size() == 1){
            logged.setVisibility(View.VISIBLE);
            unlogged.setVisibility(View.GONE);
        } else {
            logged.setVisibility(View.GONE);
            unlogged.setVisibility(View.VISIBLE);
            loginHelper.logOut();
        }
    }

    private void assignView() {
        logged = (LinearLayout) containerView.findViewById(R.id.LOGGED);
        unlogged = (LinearLayout) containerView.findViewById(R.id.UNLOGGED);

        llLogin = (LinearLayout) containerView.findViewById(R.id.llLogin);
        llRegister = (LinearLayout) containerView.findViewById(R.id.llRegister);
        llLogout = (LinearLayout) containerView.findViewById(R.id.llLogout);
        llSewa = (LinearLayout) containerView.findViewById(R.id.llSewa);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.llLogin:
                ((MainActivity)ctx).displayView(R.string.ClassLogin);
                break;
            case R.id.llRegister:
                ((MainActivity)ctx).displayView(R.string.ClassRegister);
                break;
            case R.id.llSewa:
                ((MainActivity) ctx).displayView(R.string.ClassSewa);
                break;
            case R.id.llLogout:
                loginHelper.logOut();
                MainActivity mainActivity = (MainActivity) getActivity();
                if(mainActivity != null){
                    mainActivity.refreshActivity();
                }
                break;
        }
        drawerLayout.closeDrawers();

    }

    public void setUp(int fragmentId, DrawerLayout drawer_layout, Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        drawerLayout = drawer_layout;
        actionBarDrawerToogle = new ActionBarDrawerToggle(getActivity(), drawer_layout, toolbar, R.string.drawer_open, R.string.drawer_close);
    }



    public interface FragmentDrawerListener{
        void onDrawerItemSelected(View view, int position);

        void onDrawerProfileSelected(View view);
    }
}
