package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.CoverFlowAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.ProductAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

public class SearchBoard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private ArrayList<Barang> listBarang = new ArrayList<>();
    private DatabaseReference dbRef;
    private EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_board);
        setTitle("Search");

        dbRef = FirebaseDatabase.getInstance().getReference();
        //Config RecyclerView
        etSearch = (EditText) findViewById(R.id.etSearch);
        recyclerView = (RecyclerView) findViewById(R.id.rvSearch);
        final GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
//        recyclerView.setLayoutManager(layoutManager);
        initializeData();
//        adapter = new ProductAdapter(listBarang);
//        recyclerView.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence query, int start, int before, int count) {
                query = query.toString().toLowerCase();

                ArrayList<Barang> searchList = new ArrayList<>();

                searchList.clear();
                for(int i=0; i < listBarang.size(); i++){
                    String product = listBarang.get(i).getProductname().toLowerCase();
                    String owner = listBarang.get(i).getUsername().toLowerCase();
                    if(product.contains(query) || owner.contains(query)){
                        Barang obj = new Barang();
                        obj.setCategory(listBarang.get(i).getCategory());
                        obj.setDescription(listBarang.get(i).getDescription());
                        obj.setType(listBarang.get(i).getType());
                        obj.setPrice(listBarang.get(i).getPrice());
                        obj.setProductname(listBarang.get(i).getProductname());
                        obj.setUsername(listBarang.get(i).getUsername());
                        obj.setDate(listBarang.get(i).getDate());
                        obj.setImg(listBarang.get(i).getImg());
                        obj.setPhone(listBarang.get(i).getPhone());
                        obj.setLocation(listBarang.get(i).getLocation());
                        searchList.add(obj);
                    }

                    recyclerView.setLayoutManager(layoutManager);
                    adapter = new ProductAdapter(searchList);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void initializeData() {
        dbRef.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listBarang.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                        Barang obj = new Barang();
                        obj.setCategory(map.get("category"));
                        obj.setDescription(map.get("description"));
                        obj.setType(map.get("type"));
                        obj.setPrice(map.get("price"));
                        obj.setProductname(map.get("productname"));
                        obj.setUsername(map.get("username"));
                        obj.setDate(map.get("date"));
                        obj.setImg(map.get("img"));
                        obj.setPhone(map.get("phone"));
                        obj.setLocation(map.get("location"));
                        listBarang.add(obj);
                }
                adapter = new ProductAdapter(listBarang);
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
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
