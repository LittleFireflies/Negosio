package id.sch.smktelkom_mlg.projectwork.negosio.manager;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import id.sch.smktelkom_mlg.projectwork.negosio.MainActivity;
import id.sch.smktelkom_mlg.projectwork.negosio.R;

/**
 * Created by LittleFireflies on 27-Mar-17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size() > 0){
            Map<String, String> payload = remoteMessage.getData();

            showNotification(payload);
        }
    }

    private void showNotification(Map<String, String> payload) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(payload.get("username"));
        builder.setContentText(payload.get("email"));

        Intent resultIntent = new Intent(this, MainActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(resultPendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
    }
}
