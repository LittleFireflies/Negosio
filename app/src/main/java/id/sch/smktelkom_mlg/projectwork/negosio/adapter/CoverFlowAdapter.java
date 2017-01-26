package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Kategori;

/**
 * Created by LittleFireflies on 26-Jan-17.
 */

public class CoverFlowAdapter extends BaseAdapter{
    private ArrayList<Kategori> kategori;
    private Context context;

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
        ViewHolder viewHolder;

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_category, null, false);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.categoryName.setText(kategori.get(position).getNama());

        view.setOnClickListener(onClickListener(position));
        return view;
    }

    private View.OnClickListener onClickListener(int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        };
    }

    private static class ViewHolder{
        private TextView categoryName;

        public ViewHolder(View v){
            categoryName = (TextView)v.findViewById(R.id.category_name);
        }
    }
}
