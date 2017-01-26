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

import java.util.ArrayList;

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
        coverFlow = (FeatureCoverFlow) rootView.findViewById(R.id.coverFlow);
        setDummyData();
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
        listKategori.add(new Kategori("A", "Kamera"));
        listKategori.add(new Kategori("B", "Home"));
        listKategori.add(new Kategori("C", "Technology"));
        listKategori.add(new Kategori("D", "Car"));
        listKategori.add(new Kategori("E", "Daily"));
    }

    @Override
    public void onClick(View view) {

    }

    private void fillData() {
//        Resources resources = getResources();
//        String[] arJudul = resources.getStringArray(R.array.places);
//        String[] arDeskripsi = resources.getStringArray(R.array.place_desc);
//        TypedArray a = resources.obtainTypedArray(R.array.places_picture);
//        Drawable[] arFoto = new Drawable[a.length()];
//        for (int i = 0; i < arFoto.length; i++) {
//            arFoto[i] = a.getDrawable(i);
//        }
//        a.recycle();
//        for (int i = 0; i < arJudul.length; i++) {
//            mList.add(new Hotel(arJudul[i], arDeskripsi[i], arFoto[i]));
//        }
//        mAdapter.notifyDataSetChanged();
    }
}
