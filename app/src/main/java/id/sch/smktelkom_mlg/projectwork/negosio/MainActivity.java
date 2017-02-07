package id.sch.smktelkom_mlg.projectwork.negosio;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

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

        navigationBoard = (NavigationView) findViewById(R.id.navigationFragment);
        navigationBoard.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    displayView(R.string.ClassHome);
                    // Handle the camera action
                } else if (id == R.id.nav_sewa) {
                    displayView(R.string.ClassSewa);
                } else if (id == R.id.nav_transaksi) {
                    displayView(R.string.ClassLogin);
                } else if (id == R.id.nav_login) {
                    displayView(R.string.ClassLogin);
                } else if (id == R.id.nav_setting) {

                }
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return false;
            }
        });
        displayView(R.string.ClassHome);

        realm = Realm.getDefaultInstance();
        loginHelper = new LoginHelper(realm);
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
//        MenuActionBar menuActionBar = new MenuActionBar(this);
//        return super.onCreateOptionsMenu(menu);
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
}
