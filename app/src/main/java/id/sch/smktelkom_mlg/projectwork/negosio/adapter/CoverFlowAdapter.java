package id.sch.smktelkom_mlg.projectwork.negosio.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;
import id.sch.smktelkom_mlg.projectwork.negosio.board.CameraBoard;
import id.sch.smktelkom_mlg.projectwork.negosio.helper.LoginHelper;
import id.sch.smktelkom_mlg.projectwork.negosio.manager.PicassoClient;
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
        viewHolder.btnCategory.setOnClickListener(onClickListener(viewHolder.categoryName.getText().toString()));
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
                    case "Heavy Equipment":
                        context.startActivity(new Intent(context, CameraBoard.class));
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
