package id.sch.smktelkom_mlg.projectwork.negosio.helper;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.database.User;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by LittleFireflies on 09-Jan-17.
 */

public class UserHelper {
    private Realm realm;
    private static final String TAG = "userHelper";

    public UserHelper(Realm realm){
        this.realm = realm;
    }

    public void addUser(User obj){
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(obj);
        realm.commitTransaction();
    }

    public ArrayList<User> getUser(){
        ArrayList<User> hasil = new ArrayList<>();
        RealmResults<User> results = realm.where(User.class).findAll();

        if(results.size() > 0){
            for(int i=0; i<results.size(); i++){
                hasil.add(results.get(i));
            }
        }

        return hasil;
    }

    public void logOut(){
        RealmResults<User> results = realm.where(User.class).findAll();

        realm.beginTransaction();
        results.deleteAllFromRealm();
        realm.commitTransaction();
    }
}
