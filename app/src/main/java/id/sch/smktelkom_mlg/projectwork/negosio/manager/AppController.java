package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import java.text.SimpleDateFormat;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.board.HomeBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.LoginBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.RegisterBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.SewaBoard;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by LittleFireflies on 09-Jan-17.
 */

public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .name("negosio")
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);
    }

    public Fragment changeDisplay(Context context, int titleDrawer){
        Fragment fragment = null;
        String title = context.getString(titleDrawer);
        switch (titleDrawer){
            case R.string.ClassHome:
                fragment = new HomeBoard();
                break;
            case R.string.ClassLogin:
                fragment = new LoginBoard();
                break;
            case R.string.ClassRegister:
                fragment = new RegisterBoard();
                break;
            case R.string.ClassSewa:
                fragment = new SewaBoard();
                break;
        }

        return fragment;
    }

    public String getDate(String format){
        String _setDate;
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        _setDate = sdf.format(date);
        return _setDate;
    }
}
