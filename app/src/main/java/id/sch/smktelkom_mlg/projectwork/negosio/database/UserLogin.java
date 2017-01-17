package id.sch.smktelkom_mlg.projectwork.negosio.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by LittleFireflies on 12-Jan-17.
 */

public class UserLogin extends RealmObject{
    @PrimaryKey
    private String username;
    private String password;

    public UserLogin() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
