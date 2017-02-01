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
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.TransportationAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

public class TransportationBoard extends AppCompatActivity {
    private RecyclerView rvTransportation;
    private TransportationAdapter TransportationAdapter;
    private ArrayList<Barang> listTransportation = new ArrayList<>();
    private DatabaseReference dbRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transportation_board);

        dbRef = FirebaseDatabase.getInstance().getReference();
        //Config RecyclerView
        rvTransportation = (RecyclerView) findViewById(R.id.rvTransportation);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvTransportation.setLayoutManager(layoutManager);
        initializeData();
        TransportationAdapter = new TransportationAdapter(listTransportation);
        rvTransportation.setAdapter(TransportationAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeData() {
        dbRef.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listTransportation.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if (map.get("category").equals("Transportation")) {
                        Barang obj = new Barang();
                        obj.setCategory(map.get("category"));
                        obj.setDescription(map.get("description"));
                        obj.setType(map.get("type"));
                        obj.setPrice(map.get("price"));
                        obj.setProductname(map.get("productname"));
                        obj.setUsername(map.get("username"));
                        obj.setDate(map.get("date"));
                        listTransportation.add(obj);
                    }
                }
                TransportationAdapter.notifyDataSetChanged();
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
