package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.ProductAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.CoverFlowAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

import static id.sch.smktelkom_mlg.projectwork.negosio.board.SewaBoard.REQUEST_CAMERA;
import static id.sch.smktelkom_mlg.projectwork.negosio.board.SewaBoard.SELECT_FILE;

public class CategoryDetailBoard extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayout llEmpty;
    private Button btnRefresh;
    private ProductAdapter adapter;
    private ArrayList<Barang> listBarang = new ArrayList<>();
    private DatabaseReference dbRef;
    private ProgressDialog progressDialog;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_detail);
        setTitle(getIntent().getStringExtra(CoverFlowAdapter.CATEGORY));

        progressDialog = new ProgressDialog(getApplicationContext());
        dbRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        //Config RecyclerView
        llEmpty = (LinearLayout) findViewById(R.id.llEmpty);
        btnRefresh = (Button) findViewById(R.id.btnRefresh);
        btnRefresh.setVisibility(View.GONE);
        recyclerView = (RecyclerView) findViewById(R.id.rvCamera);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        dbRef.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                btnRefresh.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        initializeData();
        adapter = new ProductAdapter(listBarang);
        recyclerView.setAdapter(adapter);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeData();
//                startActivity(getIntent());
            }
        });

    }

    private void initializeData() {
        dbRef.child("Barang").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listBarang.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(map.get("category").equals(getIntent().getStringExtra(CoverFlowAdapter.CATEGORY))){
                        llEmpty.setVisibility(View.GONE);
                        Barang obj = new Barang();
                        obj.setCategory(map.get("category"));
                        obj.setDescription(map.get("description"));
                        obj.setType(map.get("type"));
                        obj.setPrice(map.get("price"));
                        obj.setProductname(map.get("productname"));
                        obj.setUsername(map.get("username"));
                        obj.setDate(map.get("date"));
                        obj.setImg(map.get("img"));
                        obj.setLocation(map.get("location"));
                        obj.setPhone(map.get("phone"));
                        listBarang.add(obj);
                    }
                }
                adapter.notifyDataSetChanged();
                btnRefresh.setVisibility(View.GONE);
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
