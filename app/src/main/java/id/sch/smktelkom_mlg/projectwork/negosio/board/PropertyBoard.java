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
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.PropertyAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

public class PropertyBoard extends AppCompatActivity {
    private RecyclerView rvProperty;
    private PropertyAdapter propertyAdapter;
    private ArrayList<Barang> listProperty = new ArrayList<>();
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_board);

        dbRef = FirebaseDatabase.getInstance().getReference();
        //Config RecyclerView
        rvProperty = (RecyclerView) findViewById(R.id.rvProperty);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProperty.setLayoutManager(layoutManager);
        initializeData();
        propertyAdapter = new PropertyAdapter(listProperty);
        rvProperty.setAdapter(propertyAdapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initializeData() {
        dbRef.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listProperty.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if (map.get("category").equals("Property")) {
                        Barang obj = new Barang();
                        obj.setCategory(map.get("category"));
                        obj.setDescription(map.get("description"));
                        obj.setType(map.get("type"));
                        obj.setPrice(map.get("price"));
                        obj.setProductname(map.get("productname"));
                        obj.setUsername(map.get("username"));
                        obj.setDate(map.get("date"));
                        listProperty.add(obj);
                    }
                }
                propertyAdapter.notifyDataSetChanged();
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
