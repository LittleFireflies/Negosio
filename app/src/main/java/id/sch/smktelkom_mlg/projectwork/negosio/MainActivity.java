package id.sch.smktelkom_mlg.projectwork.negosio;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import id.sch.smktelkom_mlg.projectwork.negosio.manager.AppController;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private NavigationView navigationBoard;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private LinearLayout navigationDrawer, llProfile;
    private Context ctx;
    private View header;
    private TextView tvUsername, tvEmail;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private FirebaseUser user;

    public static String getUserLogin() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        return user!=null?user.getDisplayName():"";
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
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        navigationBoard = (NavigationView) findViewById(R.id.navigationFragment);
        header = navigationBoard.getHeaderView(0);
        tvUsername = (TextView)header.findViewById(R.id.tvUsername);
        tvEmail = (TextView)header.findViewById(R.id.tvEmail);
        llProfile = (LinearLayout)header.findViewById(R.id.llProfile);

        llProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayView(R.string.ClassProfile);
            }
        });

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
                    case R.id.nav_register:
                        displayView(R.string.ClassRegister);
                        break;
                    case R.id.nav_logout:
                        auth.signOut();
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
        if(user != null){
            nav.findItem(R.id.nav_sewa).setVisible(true);
            nav.findItem(R.id.nav_transaksi).setVisible(true);
            nav.findItem(R.id.nav_login).setVisible(false);
            nav.findItem(R.id.nav_logout).setVisible(true);
            nav.findItem(R.id.nav_myItem).setVisible(true);
            nav.findItem(R.id.nav_register).setVisible(false);
            tvUsername.setText(getUserLogin());
            tvEmail.setText(user.getEmail());
        } else {
            nav.findItem(R.id.nav_sewa).setVisible(false);
            nav.findItem(R.id.nav_transaksi).setVisible(false);
            nav.findItem(R.id.nav_login).setVisible(true);
            nav.findItem(R.id.nav_logout).setVisible(false);
            nav.findItem(R.id.nav_myItem).setVisible(false);
            nav.findItem(R.id.nav_register).setVisible(true);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
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
