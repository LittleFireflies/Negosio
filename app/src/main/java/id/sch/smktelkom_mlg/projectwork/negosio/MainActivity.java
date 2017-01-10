package id.sch.smktelkom_mlg.projectwork.negosio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

import id.sch.smktelkom_mlg.projectwork.negosio.board.NavigationBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.AppController;

public class MainActivity extends AppCompatActivity implements NavigationBoard.FragmentDrawerListener{

    private Toolbar toolbar;
    private NavigationBoard navigationBoard;
    private DrawerLayout drawerLayout;
    private LinearLayout navigationDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainboard);

//        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        navigationDrawer = (LinearLayout) findViewById(R.id.navigationDrawer);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        navigationBoard = (NavigationBoard) getSupportFragmentManager().findFragmentById(R.id.navigationFragment);
        navigationBoard.setUp(R.id.navigationFragment, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        navigationBoard.setFragmentDrawerListener(this);
        displayView(R.string.ClassHome);
    }

    private void displayView(int titleDrawer) {
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        MenuActionBar menuActionBar = new MenuActionBar(this);
//        return super.onCreateOptionsMenu(menu);
//    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        String title = "sample";
    }

    @Override
    public void onDrawerProfileSelected(View view) {

    }
}
