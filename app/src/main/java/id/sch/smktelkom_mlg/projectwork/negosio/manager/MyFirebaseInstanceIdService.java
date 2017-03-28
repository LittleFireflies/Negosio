package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by LittleFireflies on 27-Mar-17.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Notifications");
        dbRef.child("token").setValue(refreshToken);
    }
}
