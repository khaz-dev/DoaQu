package com.khazasid.android.doaqu;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String EVENT_NOTIF_CHANNEL_ID = "eventNotification";

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("newToken", s);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fb", s).apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        remoteMessage.getFrom();

        if(remoteMessage.getData().size() > 0){
            remoteMessage.getData();
        }

        if(remoteMessage.getNotification() != null){
            remoteMessage.getNotification().getBody();
        }

        // notification title
        String title = remoteMessage.getData().get("title");

        // notification message
        String message = remoteMessage.getData().get("message");

        // row_id needed when want to open specific doa
        String rowID = remoteMessage.getData().get("row_id");

        //imageUri will contain URL of the image to be displayed with Notification
        //To get a Bitmap image from the URL received
        String image = remoteMessage.getData().get("image");

        //sendNotification(message, bitmap, TrueOrFlase);
        sendNotification(title, rowID, message, image);

    }

    /**
     * Create and show a simple notification containing the received FCM message.
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void sendNotification(String title, @Nullable String rowID, String message, String image) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, notificationIntent,
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, EVENT_NOTIF_CHANNEL_ID)
                .setAutoCancel(true)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_stat_timeline)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_launcher))/*Notification icon image*/
                .setContentTitle(title)
                .setContentIntent(pendingIntent);

        if(rowID != null && !rowID.isEmpty()){
            Intent detailIntent = new Intent(this, DoaDetailActivity.class);
            detailIntent.putExtra(DetailFragment.ROW_ID_KEY, Short.valueOf(rowID));

            // Create the TaskStackBuilder and add the intent, which inflates the back stack
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
            stackBuilder.addNextIntentWithParentStack(detailIntent);

            PendingIntent detailPendingIntent =
                    stackBuilder.getPendingIntent(0, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Action actionDoa = new NotificationCompat.Action.Builder(0,
                    getResources().getString(R.string.read_doa),detailPendingIntent).build();

            notificationBuilder.addAction(actionDoa);
        }

        if(image != null && !image.isEmpty()){
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(getBitmapfromUrl(image)).setSummaryText(message));
        } else{
            notificationBuilder.setStyle(new NotificationCompat.BigTextStyle()
                    .bigText(message));/*Notification with Image*/
        }

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(getResources().getInteger(R.integer.EVENT_NOTIF_ID) /* ID of notification */, notificationBuilder.build());
    }

    /*
     *To get a Bitmap image from the URL received
     * */
    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);

        } catch (Exception e) {
            // Auto-generated catch block
            e.printStackTrace();
            return null;
        }
    }

    public static void getToken(Context context) {
        context.getSharedPreferences("_", MODE_PRIVATE).getString("fb", "empty");
    }
}
