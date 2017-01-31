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
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.HvEquipmentAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

public class HeavyEquipmentBoard extends AppCompatActivity {
    private RecyclerView rvHvEquipm;
    private HvEquipmentAdapter hvEquipmentAdapter;
    private ArrayList<Barang> listHvEquipm = new ArrayList<>();
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heavy_equipment_board);

        dbRef = FirebaseDatabase.getInstance().getReference();
        //Config RecyclerView
        rvHvEquipm = (RecyclerView) findViewById(R.id.rvhvequipm);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvHvEquipm.setLayoutManager(layoutManager);
        initializeData();
        hvEquipmentAdapter = new HvEquipmentAdapter(listHvEquipm);
        rvHvEquipm.setAdapter(hvEquipmentAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeData() {
        dbRef.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listHvEquipm.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if (map.get("category").equals("Heavy Equipment")) {
                        Barang obj = new Barang();
                        obj.setCategory(map.get("category"));
                        obj.setDescription(map.get("description"));
                        obj.setType(map.get("type"));
                        obj.setPrice(map.get("price"));
                        obj.setProductname(map.get("productname"));
                        obj.setUsername(map.get("username"));
                        obj.setDate(map.get("date"));
                        listHvEquipm.add(obj);
                    }
                }
                hvEquipmentAdapter.notifyDataSetChanged();
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
