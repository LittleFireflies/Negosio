package id.sch.smktelkom_mlg.projectwork.negosio.model;

/**
 * Created by LittleFireflies on 26-Jan-17.
 */

public class Kategori {
    private String id;
    private String nama;
    private String imgUrl;

    public Kategori(String id, String nama) {
        this.id = id;
        this.nama = nama;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}
