package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.board.CategoryDetailBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.model.Kategori;
import io.realm.Realm;

/**
 * Created by LittleFireflies on 26-Jan-17.
 */

public class CoverFlowAdapter extends BaseAdapter{
    public static final int REQUEST_CODE = 123;
    public static final String CATEGORY = "Category";
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

        if(view == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_category, null, false);

            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        viewHolder.categoryName.setText(kategori.get(position).getNama());
        viewHolder.categoryTitle.setText(kategori.get(position).getNama());
        viewHolder.ivCategory.setImageResource(kategori.get(position).getPict());

        view.setOnClickListener(onClickListener(viewHolder.categoryName.getText().toString()));
        viewHolder.ivCategory.setOnClickListener(onClickListener(viewHolder.categoryName.getText().toString()));

        return view;
    }

    private View.OnClickListener onClickListener(final String name) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CategoryDetailBoard.class);
                intent.putExtra(CATEGORY, name);
                context.startActivity(intent);
            }
        };
    }

    private static class ViewHolder{
        private TextView categoryName, categoryTitle;
        private ImageView ivCategory;
//        private ImageButton btnCategory;

        public ViewHolder(View v){
            categoryTitle = (TextView)v.findViewById(R.id.category_title);
            categoryName = (TextView)v.findViewById(R.id.category_name);
            ivCategory = (ImageView)v.findViewById(R.id.ivCategory);
        }
    }
}
