package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.board.EditProfileBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.HistoryTransactionBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.HomeBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.LoginBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.MyItemBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.ProfileBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.RegisterBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.SettingBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.SewaBoard;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by LittleFireflies on 09-Jan-17.
 */

public class AppController extends Application {
    public static String FLAG = "It the start";
    @Override
    public void onCreate() {
        super.onCreate();

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
//            case R.string.ClassSewa:
//                fragment = new SewaBoard();
//                break;
            case R.string.ClassTransaction:
                fragment = new HistoryTransactionBoard();
                break;
            case R.string.ClassMyItem:
                fragment = new MyItemBoard();
                break;
//            case R.string.ClassProfile:
//                fragment = new ProfileBoard();
//                break;
//            case R.string.ClassEditProfile:
//                fragment = new EditProfileBoard();
//                break;
            case R.string.ClassSetting:
                fragment = new SettingBoard();
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

    public String numberTocurrency(Double nilai)
    {
//        double x =20000;
        String currencyUI;
        DecimalFormat format = new DecimalFormat("###,###,###,###,###");
        currencyUI = format.format(nilai);
        currencyUI = currencyUI.replace(",", ".");
        return currencyUI;
    }

    public Double currencyTonumber(String nilai)
    {
        Double currencyDB;
        currencyDB = Double.valueOf(nilai.replace(".",""));
        return currencyDB;
    }
}
