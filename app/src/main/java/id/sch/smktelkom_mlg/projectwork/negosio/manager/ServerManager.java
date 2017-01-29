package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.model.Kategori;
import io.realm.Realm;

/**
 * Created by LittleFireflies on 27-Jan-17.
 */

public class ServerManager {
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    Realm realm = Realm.getDefaultInstance();
//    KategoriHelper kategoriHelper = new KategoriHelper(realm);

    public void insertKategori(){
        dbRef.child("Kategori").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    String id = map.get("id");
                    String nama = map.get("nama");
                    String url = map.get("imgUrl");

//                    Kategori kategori = new Kategori();
//                    kategori.setId(id);
//                    kategori.setNama(nama);
//                    kategori.setImgUrl(url);
//                    kategoriHelper.setKategori(kategori);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
