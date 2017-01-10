package id.sch.smktelkom_mlg.projectwork.negosio.database;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by LittleFireflies on 09-Jan-17.
 */

public class User extends RealmObject {
    @PrimaryKey
    private String username;
    private String nama;
    private String password;
    private String email;
    private String lokasi;
    private String hp;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }
}
