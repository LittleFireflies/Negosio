package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.board.HomeBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.RegisterBoard;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by LittleFireflies on 09-Jan-17.
 */

public class AppController extends Application {
    public Fragment changeDisplay(Context context, int titleDrawer){
        Fragment fragment = null;
        String title = context.getString(titleDrawer);
        switch (titleDrawer){
            case R.string.ClassHome:
                fragment = new HomeBoard();
                break;
//            case R.string.ClassLogin:
//                fragment = new LoginBoard();
//                break;
            case R.string.ClassRegister:
                fragment = new RegisterBoard();
                break;
        }

        return fragment;
    }
}
