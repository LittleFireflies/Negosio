package id.sch.smktelkom_mlg.projectwork.negosio;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.board.NavigationBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.database.UserLogin;
import id.sch.smktelkom_mlg.projectwork.negosio.helper.LoginHelper;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.AppController;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity implements NavigationBoard.FragmentDrawerListener{

    static ArrayList<UserLogin> login = new ArrayList<>();
    private static LoginHelper loginHelper;
    private static String userLogin;
    private Toolbar toolbar;
    private NavigationView navigationBoard;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout navigationDrawer;
    private Realm realm;
    private Context ctx;
    private View header;
    private TextView tvUsername, tvEmail;

    public static String getUserLogin() {
        login = loginHelper.getUserLogin();
        for (int i = 0; i < login.size(); i++) {
            userLogin = login.get(i).getUsername();
        }
        return userLogin;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainboard);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        ctx = getApplicationContext();
        realm = Realm.getDefaultInstance();
        loginHelper = new LoginHelper(realm);
        navigationBoard = (NavigationView) findViewById(R.id.navigationFragment);
        header = navigationBoard.getHeaderView(0);
        tvUsername = (TextView)header.findViewById(R.id.tvUsername);
        tvEmail = (TextView)header.findViewById(R.id.tvEmail);

        setNavigatonMenu();
        navigationBoard.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch (id){
                    case R.id.nav_home:
                        displayView(R.string.ClassHome);
                        break;
                    case R.id.nav_sewa:
                        displayView(R.string.ClassSewa);
                        break;
                    case R.id.nav_transaksi:
                        displayView(R.string.ClassTransaction);
                        break;
                    case R.id.nav_myItem:
                        displayView(R.string.ClassMyItem);
                        break;
                    case R.id.nav_login:
                        displayView(R.string.ClassLogin);
                        break;
                    case R.id.nav_logout:
                        loginHelper.logOut();
                        finish();
                        startActivity(getIntent());
                        break;
                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        displayView(R.string.ClassHome);
    }

    private void setNavigatonMenu() {
        Menu nav = navigationBoard.getMenu();
        if(loginHelper.getUserLogin().size() == 1){
            nav.findItem(R.id.nav_sewa).setVisible(true);
            nav.findItem(R.id.nav_transaksi).setVisible(true);
            nav.findItem(R.id.nav_login).setVisible(false);
            nav.findItem(R.id.nav_logout).setVisible(true);
            tvUsername.setText("User");
            tvEmail.setText("Email User");
        } else {
            nav.findItem(R.id.nav_sewa).setVisible(false);
            nav.findItem(R.id.nav_transaksi).setVisible(false);
            nav.findItem(R.id.nav_login).setVisible(true);
            nav.findItem(R.id.nav_logout).setVisible(false);
        }
    }

    public void displayView(int titleDrawer) {
        Fragment fragment = null;
        String title = getApplicationContext().getString(titleDrawer);
        AppController moveClass = new AppController();
        fragment = moveClass.changeDisplay(getApplicationContext(), titleDrawer);
        if(fragment!=null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            //set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuItem search = menu.findItem(R.id.action_search);
//
//        if(search != null) {
//            SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
//            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
//                @Override
//                public boolean onClose() {
//                    return false;
//                }
//            });
//            searchView.setOnSearchClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                }
//            });
//        }
//
//        return true;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        String title = "sample";
    }

    @Override
    public void onDrawerProfileSelected(View view) {

    }

    public void refreshActivity() {
        finish();
        startActivity(getIntent());
    }

    private boolean backPressedToExitOnce = false;

    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
//            super.onBackPressed();
//            logout();
            finish();
        } else {
            this.backPressedToExitOnce = true;
            Toast.makeText(this,"Press again to Exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 2000);

        }
    }
}
