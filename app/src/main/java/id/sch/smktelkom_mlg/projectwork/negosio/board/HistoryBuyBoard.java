package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

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
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.ProductAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Barang;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Booking;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryBuyBoard extends Fragment {

    private RecyclerView rvBuy;
    private BuyAdapter adapter;
    private DatabaseReference dbRef;
    private ArrayList<Booking> listItem = new ArrayList<>();
    private Context ctx;
    private Spinner spFilter;
    View rootView;

    public HistoryBuyBoard() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_history_buy_board, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference();
        spFilter = (Spinner) rootView.findViewById(R.id.spFilter);
        //Config RecyclerView
        rvBuy = (RecyclerView) rootView.findViewById(R.id.rvBuy);
        LinearLayoutManager layoutManager = new LinearLayoutManager(ctx);
        rvBuy.setLayoutManager(layoutManager);
        spFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = spFilter.getSelectedItem().toString();
                initializeData(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        adapter = new BuyAdapter(listItem);
        rvBuy.setAdapter(adapter);
        return rootView;
    }

    private void initializeData(String filter) {
        dbRef.child("Booking").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listItem.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
                    if(map.get("buyer").equals(MainActivity.getUserLogin())){
                        Booking booking = new Booking();
                        booking.setProduct_name(map.get("product_name"));
                        booking.setTotal(map.get("total"));
                        booking.setCategory(map.get("category"));
                        booking.setStart_date(map.get("start_date"));
                        booking.setEnd_date(map.get("end_date"));
                        booking.setPrice(map.get("price"));
                        booking.setTime(map.get("time"));
                        booking.setTgl_booking(map.get("tgl_booking"));
                        booking.setBuyer(map.get("buyer"));
                        booking.setBuyer_phone(map.get("buyer_phone"));
                        booking.setBuyer_location(map.get("buyer_location"));
                        booking.setRenter_token(map.get("renter_token"));
                        booking.setSeller(map.get("seller"));
                        booking.setSeller_phone(map.get("seller_phone"));
                        booking.setSeller_location(map.get("seller_location"));
                        booking.setOwner_token(map.get("owner_token"));
                        booking.setImg(map.get("img"));
                        booking.setStatus(map.get("status"));
                        booking.setReason(map.get("reason"));
                        listItem.add(booking);
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
