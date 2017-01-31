package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.CameraAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

public class CameraBoard extends AppCompatActivity {
    private RecyclerView rvCamera;
    private CameraAdapter cameraAdapter;
    private ArrayList<Barang> listCamera = new ArrayList<>();
    private DatabaseReference dbRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_board);

        dbRef = FirebaseDatabase.getInstance().getReference();
        //Config RecyclerView
        rvCamera = (RecyclerView) findViewById(R.id.rvCamera);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvCamera.setLayoutManager(layoutManager);
        initializeData();
        cameraAdapter = new CameraAdapter(listCamera);
        rvCamera.setAdapter(cameraAdapter);
    }

    private void initializeData() {
        dbRef.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listCamera.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(map.get("category").equals("Camera")){
                        Barang obj = new Barang();
                        obj.setCategory(map.get("category"));
                        obj.setDescription(map.get("description"));
                        obj.setType(map.get("type"));
                        obj.setPrice(map.get("price"));
                        obj.setProductname(map.get("productname"));
                        obj.setUsername(map.get("username"));
                        obj.setDate(map.get("date"));
                        obj.setImg(map.get("img"));
                        listCamera.add(obj);
                    }
                }
                cameraAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
