package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import android.content.SharedPreferences;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;

/**
 * Created by LittleFireflies on 27-Mar-17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    public static final String MY_PREFERENCE = "TOKEN DEVICE";
    SharedPreferences sharedPreferences;

    @Override
    public void onTokenRefresh() {
        final String refreshToken = FirebaseInstanceId.getInstance().getToken();
        sharedPreferences = getSharedPreferences(MY_PREFERENCE, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", refreshToken);
        editor.commit();
    }
}
