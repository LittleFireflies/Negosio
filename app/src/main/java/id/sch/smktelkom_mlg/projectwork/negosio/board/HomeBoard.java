package id.sch.smktelkom_mlg.projectwork.negosio.board;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.CoverFlowAdapter;
import id.sch.smktelkom_mlg.projectwork.negosio.database.UserLogin;
import id.sch.smktelkom_mlg.projectwork.negosio.helper.LoginHelper;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Hotel;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Kategori;
import io.realm.Realm;
import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class HomeBoard extends Fragment implements View.OnClickListener{

    View rootView;
    Context ctx;
    Realm realm;
    LoginHelper loginHelper;
    private FeatureCoverFlow coverFlow;
    private CoverFlowAdapter coverFlowAdapter;
    private ArrayList<Kategori> listKategori = new ArrayList<>();
    private DatabaseReference dbRef;

    ArrayList<Hotel> mList = new ArrayList<>();
    private String username = "";
    private TextView tvUsername;
    private int size = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.home_board, container, false);
        ctx = getContext();
        realm = Realm.getDefaultInstance();
        loginHelper = new LoginHelper(realm);
        assignToView();
        getUserData();
        onSetView();
        return rootView;
    }

    private void getUserData() {
        ArrayList<UserLogin> result = loginHelper.getUserLogin();
        size = result.size();
        for(int i=0; i<result.size(); i++){
            username = result.get(i).getUsername();
        }
    }

    private void onSetView() {
//        tvUsername.setText(String.valueOf(size));
    }

    private void assignToView() {
        //tvUsername = (TextView) rootView.findViewById(R.id.tvUser);
        //Script recycle view;

//        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
//        GridLayoutManager layoutManager = new GridLayoutManager(ctx, 2);
//        recyclerView.setLayoutManager(layoutManager);
//        mAdapter = new HotelAdapter(mList);
//        recyclerView.setAdapter(mAdapter);
        dbRef = FirebaseDatabase.getInstance().getReference();
        setDummyData();
            coverFlow = (FeatureCoverFlow) rootView.findViewById(R.id.coverFlow);
            coverFlowAdapter = new CoverFlowAdapter(ctx, listKategori);
            coverFlow.setAdapter(coverFlowAdapter);
            coverFlow.setOnScrollPositionListener(onScrollListener());



        fillData();
        // Akhir
    }

    private FeatureCoverFlow.OnScrollPositionListener onScrollListener() {
        return new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {

            }

            @Override
            public void onScrolling() {

            }
        };
    }

    private void setDummyData() {
//        listKategori.clear();
        listKategori.add(new Kategori("001", "Camera", R.drawable.camera));
        listKategori.add(new Kategori("002", "Transportation", R.drawable.car));
        listKategori.add(new Kategori("003", "Property", R.drawable.property));
        listKategori.add(new Kategori("004", "Event", R.drawable.event));
        listKategori.add(new Kategori("005", "Heavy Equipment", R.drawable.heavy_equipment));
        listKategori.add(new Kategori("006", "Baby Care", R.drawable.baby));
        listKategori.add(new Kategori("007", "Others", R.drawable.placeholder));
//        dbRef.child("Kategori").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
////                listKategori.clear();
//                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
//                    Map<String, String> map = (Map<String, String>) snapshot.getValue();
//                    String id = map.get("id");
//                    String nama = map.get("nama");
//                    String url = map.get("imgUrl");
//
//                    Kategori kategori = new Kategori();
//                    kategori.setId(id);
//                    kategori.setNama(nama);
//                    kategori.setImgUrl(url);
//                    kategoriHelper.setKategori(kategori);
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        if(kategoriHelper.getKategori().size() <=0){
//            setDummyData();
//        }
    }

    @Override
    public void onClick(View view) {

    }

    private void fillData() {
    }
}
