package id.sch.smktelkom_mlg.projectwork.negosio.model;

import android.graphics.drawable.Drawable;

/**
 * Created by Fadhil Ikhsan on 18/01/2017.
 */

public class Hotel {
    public String judul;
    public String deskripsi;
    public Drawable foto;

    public Hotel(String judul, String deskripsi, Drawable foto) {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.foto = foto;
    }
}
