package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LittleFireflies on 09-Jan-17.
 */

public class AppPrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;

    int PRIVATE_MODE = 0;
    String PREF_NAME = "Negosio";
    String KEY_IS_LOGGED_IN = "IsLoggedIn";
    String KEY_USERNAME = "username";

    public AppPrefManager(Context context){
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setIsLoggedIn(Boolean isLoggedIn){
        editor.putBoolean(KEY_IS_LOGGED_IN, isLoggedIn);
        editor.commit();
    }

    public Boolean getIsLoggedIn(){
        return pref.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public void setUsername(String username){
        editor.putString(KEY_USERNAME, username);
        editor.commit();
    }

    public String getUsername(){
        return pref.getString(KEY_IS_LOGGED_IN, "");
    }
}
