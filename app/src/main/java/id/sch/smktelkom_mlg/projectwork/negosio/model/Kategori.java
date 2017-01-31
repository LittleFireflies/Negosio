package id.sch.smktelkom_mlg.projectwork.negosio.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by LittleFireflies on 26-Jan-17.
 */

public class Kategori{
//    @PrimaryKey
    private String id;
    private String nama;
    private int pict;

    public Kategori(String id, String nama, int pict) {
        this.id = id;
        this.nama = nama;
        this.pict = pict;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public int getPict() {
        return pict;
    }

    public void setPict(int pict) {
        this.pict = pict;
    }
}
