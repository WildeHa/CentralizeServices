package blindcane.lscm.hk.centralizeservices.util;

import android.app.Application;

/**
 * Created by tauyeung on 4/3/2017.
 */

public class RequestCode extends Application {
    public static final int BLUETOOTH_CONNECT_SERVICE = 1;
    public static final int BLE_SERVICE = 2;
    public static final int TTS_SERVICE = 3;
    public static final int HTTP_SERVICE = 4;
    public static final int DATABASE_SERVICE = 5;
    public static final int PATH_SEARCHING_SERVICE = 6;

    public final static String HEAD_SERVICE_ENABLE_KEY = "HeadServiceEnableKey";
    public final static String TTS_SERVICE_ENABLE_KEY = "TTSServiceEnableKey";
    public final static String BLE_ENABLE_KEY = "BleServiceEnableKey";

}
