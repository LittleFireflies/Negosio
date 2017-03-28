package id.sch.smktelkom_mlg.projectwork.negosio.board;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.FirebaseDatabase;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.adapter.ViewPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryTransactionBoard extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private Context ctx;
    private View rootView;

    public HistoryTransactionBoard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_history_transaction_board, container, false);
        ctx = getContext();
        assignToView();
        onSetView();
        return rootView;
    }

    private void onSetView() {
        setupViewPaget(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void assignToView() {
        viewPager = (ViewPager) rootView.findViewById(R.id.viewPager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
    }

    private void setupViewPaget(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        adapter.addFragment(new HistoryBuyBoard(), "Items In");
        adapter.addFragment(new HistorySellBoard(), "Items Out");
        viewPager.setAdapter(adapter);
    }

}
