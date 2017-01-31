package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.board.BabyCareBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.CameraBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.EventBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.HeavyEquipmentBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.OthersBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.PropertyBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.board.TransportationBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Kategori;
import io.realm.Realm;

/**
 * Created by LittleFireflies on 26-Jan-17.
 */

public class CoverFlowAdapter extends BaseAdapter{
    private ArrayList<Kategori> kategori;
    private Context context;
    private Realm realm;

    public CoverFlowAdapter(Context context, ArrayList<Kategori> kategori) {
        this.context = context;
        this.kategori = kategori;
    }

    @Override
    public int getCount() {
        return kategori.size();
    }

    @Override
    public Kategori getItem(int position) {
        return kategori.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup patrnt) {
        final ViewHolder viewHolder;
        realm = Realm.getDefaultInstance();

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_category, null, false);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.categoryName.setText(kategori.get(position).getNama());
        viewHolder.ivCategory.setImageResource(kategori.get(position).getPict());

        view.setOnClickListener(onClickListener(viewHolder.categoryName.getText().toString()));
//        viewHolder.btnCategory.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                context.startActivity(new Intent(context, CameraBoard.class));
//            }
//        });
        return view;
    }

    private View.OnClickListener onClickListener(final String name) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (name){
                    case "Camera":
                        context.startActivity(new Intent(context, CameraBoard.class));
                        break;
                    case "Transportation":
                        context.startActivity(new Intent(context, TransportationBoard.class));
                        break;
                    case "Property":
                        context.startActivity(new Intent(context, PropertyBoard.class));
                        break;
                    case "Event":
                        context.startActivity(new Intent(context, EventBoard.class));
                        break;
                    case "Heavy Equipment":
                        context.startActivity(new Intent(context, HeavyEquipmentBoard.class));
                        break;
                    case "Baby Care":
                        context.startActivity(new Intent(context, BabyCareBoard.class));
                        break;
                    case "Others":
                        context.startActivity(new Intent(context, OthersBoard.class));
                        break;
                }
            }
        };
    }

    private static class ViewHolder{
        private TextView categoryName;
        private ImageView ivCategory;
        private ImageButton btnCategory;

        public ViewHolder(View v){
            categoryName = (TextView)v.findViewById(R.id.category_name);
            ivCategory = (ImageView)v.findViewById(R.id.ivCategory);
            btnCategory = (ImageButton)v.findViewById(R.id.btnCategory);
        }
    }
}
