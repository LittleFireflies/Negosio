package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import android.app.Application;
import android.content.Context;
import android.support.v4.app.Fragment;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.board.HistoryTransactionBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.HomeBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.LoginBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.MyItemBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.RegisterBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.SewaBoard;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.App42BadParameterException;
import com.shephertz.app42.paas.sdk.android.App42NotFoundException;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.user.User;
import com.shephertz.app42.paas.sdk.android.user.User.Profile;
import com.shephertz.app42.paas.sdk.android.user.User.UserGender;
import com.shephertz.app42.paas.sdk.android.user.UserService;

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

        App42API.initialize(getApplicationContext(),"db71841700b2023d5e2a1755ce66cb0fd4e5a76109e21ee895445b23ba94d717","9c0f1def3c7d9126c81d4d320f3f192e26e50f28fe96efaf3ffe03e46d2cbda8");

        //APPHQ
        String username = "widdyjp";
        String password = "rahasia";
        String emailId = "widyjp10gmail.com";
        UserService userService  = App42API.buildUserService();
        userService.createUser(username, password, emailId, new App42CallBack() {
            @Override
            public void onSuccess(Object response) {
                User user = (User) response;
                System.out.println("Username is " + user.getUserName());
                System.out.println("Email is " + user.getEmail());
            }

            @Override
            public void onException(Exception e) {
                System.out.println("Exception Message"+e.getMessage());
            }
        });
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
            case R.string.ClassTransaction:
                fragment = new HistoryTransactionBoard();
                break;
            case R.string.ClassMyItem:
                fragment = new MyItemBoard();
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
