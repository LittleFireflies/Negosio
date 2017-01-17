package id.sch.smktelkom_mlg.projectwork.negosio.helper;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.database.UserLogin;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by LittleFireflies on 09-Jan-17.
 */

public class LoginHelper {
    private Realm realm;
    private static final String TAG = "userHelper";

    public LoginHelper(Realm realm){
        this.realm = realm;
    }

    public void logIn(UserLogin obj){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(obj);
        realm.commitTransaction();
    }

    public ArrayList<UserLogin> getUserLogin(){
        ArrayList<UserLogin> hasil = new ArrayList<>();
        RealmResults<UserLogin> results = realm.where(UserLogin.class).findAll();

        if(results.size() > 0){
            for(int i=0; i<results.size(); i++){
                hasil.add(results.get(i));
            }
        }

        return hasil;
    }

    public void logOut(){
        RealmResults<UserLogin> results = realm.where(UserLogin.class).findAll();

        realm.beginTransaction();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }
}
