package io.caly.calyandroid.Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

import net.jspiner.prefer.Prefer;

import java.util.Map;

import io.caly.calyandroid.Activity.EventListActivity;
import io.caly.calyandroid.Activity.SplashActivity;
import io.caly.calyandroid.CalyApplication;
import io.caly.calyandroid.Model.DataModel.TestModel;
import io.caly.calyandroid.Model.Event.AccountListRefreshEvent;
import io.caly.calyandroid.Model.Event.GoogleSyncDoneEvent;
import io.caly.calyandroid.Model.Event.RecoReadyEvent;
import io.caly.calyandroid.R;
import io.caly.calyandroid.Util.BusProvider;
import io.caly.calyandroid.Util.EventListener.AppLifecycleListener;

/**
 * Copyright 2017 JSpiner. All rights reserved.
 *
 * @author jspiner (jspiner@naver.com)
 * @project CalyAndroid
 * @since 17. 2. 13
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {

    //로그에 쓰일 tag
    private static final String TAG = CalyApplication.class.getSimpleName() + "/" + FirebaseMessagingService.class.getSimpleName();

    // JSON 규격
    /*
    {
        "type" : "type",
        "action" : "action"
    }
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(!Prefer.get("isPushReceive", true)){
            return;
        }

        Log.i(TAG, "onMessageReceived");
        Log.d(TAG, "push massage : " + remoteMessage.getData().toString());

        Map<String, String> pushData = remoteMessage.getData();

        String pushType = pushData.get("type");
        String pushAction = pushData.get("action");

        Log.d(TAG, "activity count : " + AppLifecycleListener.getActiveActivityCount());
        switch (pushType){
            case "sync":
                if(AppLifecycleListener.getActiveActivityCount()==0){
                    //app is background
                    sendNotification(getString(R.string.notification_msg_google_sync_done), getString(R.string.notification_msg_google_sync_done));
                }
                else{
                    //app is foreground
                    BusProvider.getInstance().post(new GoogleSyncDoneEvent());
                }
                break;
            case "noti":
                sendNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
                break;
            case "caldavForwardSyncEnd":
                BusProvider.getInstance().post(new AccountListRefreshEvent());
                break;
            case "googleForwardSyncEnd":
                BusProvider.getInstance().post(new AccountListRefreshEvent());
                break;
            case "reco":
                if(AppLifecycleListener.getActiveActivityCount()==0) {
                    sendNotification("추천이 준비되었습니다.", "추천이 준비되었습니다.");
                }
                else{
                    BusProvider.getInstance().post(new RecoReadyEvent());
                }
                break;
        }

    }


    private void sendNotification(String title, String message) {
        Log.i(TAG, "sendNotification("+title+")");

        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.app_icon)
                .setTicker(title)
                .setWhen(0)
                .setContentTitle(title)
                .setStyle(new NotificationCompat.InboxStyle())
                .setContentText(message)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.app_icon))
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }

}
