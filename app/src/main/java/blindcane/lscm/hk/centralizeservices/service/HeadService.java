package blindcane.lscm.hk.centralizeservices.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import blindcane.lscm.hk.centralizeservices.HeadLayer;
import blindcane.lscm.hk.centralizeservices.MainActivity;
import blindcane.lscm.hk.centralizeservices.R;

/**
 * Created by LSCM on 2017/4/5.
 */

public class HeadService extends Service {

    private HeadLayer headLayer;

    private final static int FORGROUND_ID = 9999;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        logServiceStarted();

        initializeHeadLayer();

        PendingIntent pendingIntent = createPendingIntent();

        Notification notification = createNotification(pendingIntent);

        startForeground(FORGROUND_ID, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        destroyHeadLayer();
        stopForeground(true);
    }

    private void initializeHeadLayer() {
        headLayer = new HeadLayer(this);
    }

    private void destroyHeadLayer() {
        headLayer.destroy();
        headLayer = null;
    }

    private PendingIntent createPendingIntent() {
        Intent intent = new Intent(this, MainActivity.class);
        return PendingIntent.getActivity(this, 0, intent, 0);
    }

    private Notification createNotification(PendingIntent intent) {
        return new Notification.Builder(this).setContentTitle("ABC")
                .setContentText("DEF")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(intent).build();
    }

    private void logServiceStarted() {

    }

    private void logServiceEnded() {
    }

}
