package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.OthersAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

public class OthersBoard extends AppCompatActivity {
    private RecyclerView rvOthers;
    private OthersAdapter othersAdapter;
    private ArrayList<Barang> listOthers = new ArrayList<>();
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_board);

        dbRef = FirebaseDatabase.getInstance().getReference();
        //Config RecyclerView
        rvOthers = (RecyclerView) findViewById(R.id.rvOthers);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvOthers.setLayoutManager(layoutManager);
        initializeData();
        othersAdapter = new OthersAdapter(listOthers);
        rvOthers.setAdapter(othersAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeData() {
        dbRef.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOthers.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if (map.get("category").equals("Others")) {
                        Barang obj = new Barang();
                        obj.setCategory(map.get("category"));
                        obj.setDescription(map.get("description"));
                        obj.setType(map.get("type"));
                        obj.setPrice(map.get("price"));
                        obj.setProductname(map.get("productname"));
                        obj.setUsername(map.get("username"));
                        obj.setDate(map.get("date"));
                        obj.setImg(map.get("img"));
                        listOthers.add(obj);
                    }
                }
                othersAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
