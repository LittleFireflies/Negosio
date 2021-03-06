package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import id.sch.smktelkom_mlg.projectwork.negosio.R;

/**
 * Created by LittleFireflies on 27-Jan-17.
 */

public class PicassoClient {
    public static void downloadImage(Context ctx, String url, ImageView imageView){
        if(url != null && url.length()>0){
            Picasso.with(ctx).load(url).fit().centerCrop().placeholder(R.drawable.placeholder).into(imageView);
        } else {
            Picasso.with(ctx).load(R.drawable.placeholder).into(imageView);
        }
    }

    public static void downloadProfilePict(Context ctx, String url, ImageView imageView){
        if(url != null && url.length()>0){
            Picasso.with(ctx).load(url).fit().centerCrop().placeholder(R.drawable.no_profile).into(imageView);
        } else {
            Picasso.with(ctx).load(R.drawable.no_profile).into(imageView);
        }
    }
}
