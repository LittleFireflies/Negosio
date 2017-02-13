package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.BuyAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.MyItemAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyItemBoard extends Fragment {

    private View rootView;
    private Context ctx;
    private RecyclerView rvMyItem;
    private MyItemAdapter adapter;
    private DatabaseReference dbRef;
    private ArrayList<Barang> listItem = new ArrayList<>();

    public MyItemBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my_item_board, container, false);
        dbRef = FirebaseDatabase.getInstance().getReference();
        rvMyItem = (RecyclerView) rootView.findViewById(R.id.rvMyItem);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
        rvMyItem.setLayoutManager(layoutManager);
        initializeData();
        adapter = new MyItemAdapter(listItem);
        rvMyItem.setAdapter(adapter);
        return rootView;
    }

    private void initializeData() {
        dbRef.child("Barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItem.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(map.get("username").equals(MainActivity.getUserLogin())){
                        Barang barang = new Barang();
                        barang.setPrice(map.get("price"));
                        barang.setProductname(map.get("productname"));
                        barang.setUsername(map.get("username"));
                        barang.setDate(map.get("date"));
                        barang.setCategory(map.get("category"));
                        barang.setType(map.get("type"));
                        barang.setDescription(map.get("description"));
                        barang.setImg(map.get("img"));
                        listItem.add(barang);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
