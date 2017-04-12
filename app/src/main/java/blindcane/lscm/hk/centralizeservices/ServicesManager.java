package blindcane.lscm.hk.centralizeservices;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.PrintWriter;

import blindcane.lscm.hk.centralizeservices.service.TTSService;

import static blindcane.lscm.hk.centralizeservices.util.RequestCode.BLE_SERVICE;
import static blindcane.lscm.hk.centralizeservices.util.RequestCode.BLUETOOTH_CONNECT_SERVICE;
import static blindcane.lscm.hk.centralizeservices.util.RequestCode.DATABASE_SERVICE;
import static blindcane.lscm.hk.centralizeservices.util.RequestCode.HTTP_SERVICE;
import static blindcane.lscm.hk.centralizeservices.util.RequestCode.PATH_SEARCHING_SERVICE;
import static blindcane.lscm.hk.centralizeservices.util.RequestCode.TTS_SERVICE;

/**
 * Created by tauyeung on 4/3/2017.
 */

public class ServicesManager extends Service {
    private boolean mBound = false;
    private volatile int requestCodeHistory = 0;
    private Intent intentTTS = new Intent("TTS_REQUEST");

    public ServicesManager() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("lscm", "TTS Service Start");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent != null) {
            int requestCode = intent.getIntExtra("REQUEST_CODE", 0);
            requestCodeHistory = requestCode;
            if (requestCode == BLUETOOTH_CONNECT_SERVICE)
                ;
            else if (requestCode == BLE_SERVICE)
                ;
            else if (requestCode == TTS_SERVICE) {
                Intent i = new Intent(this, TTSService.class);
                bindService(i, mConnection, BIND_AUTO_CREATE);
            } else if (requestCode == HTTP_SERVICE)
                ;
            else if (requestCode == DATABASE_SERVICE)
                ;
            else if (requestCode == PATH_SEARCHING_SERVICE)
                ;

        }
        return null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
    }

    @Override
    protected void dump(FileDescriptor fd, PrintWriter writer, String[] args) {
        super.dump(fd, writer, args);
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            mBound = true;


        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


}
