package blindcane.lscm.hk.centralizeservices.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

/**
 * Created by LSCM on 2017/4/5.
 */

public class PermissionChecker {

    public final static int REQUIRED_PERMISSION_REQUEST_CODE = 1111;

    private Context context;

    public PermissionChecker(Context context) {
        this.context = context;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean isRequiredPermissionGranted() {
        if (isMarshMallowOrHigher()) {
            return Settings.canDrawOverlays(context);
        }
        return true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    public Intent createRequiredPermissionIntent() {
        if (isMarshMallowOrHigher()) {
            return new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + context.getPackageName()));
        }

        return null;
    }

    private boolean isMarshMallowOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.M;
    }
}
