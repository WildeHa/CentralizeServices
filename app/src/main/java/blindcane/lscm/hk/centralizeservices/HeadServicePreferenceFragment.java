package blindcane.lscm.hk.centralizeservices;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.widget.Switch;

import blindcane.lscm.hk.centralizeservices.service.HeadService;
import blindcane.lscm.hk.centralizeservices.util.PermissionChecker;
import blindcane.lscm.hk.centralizeservices.util.RequestCode;

/**
 * Created by LSCM on 2017/4/12.
 * <p>
 * List of checkbox that represent all service would be contained
 */

public class HeadServicePreferenceFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private HeadServiceEnabler headServiceEnabler;

    private PermissionChecker permissionChecker;

    private boolean isHeadServicePermitted;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        PreferenceManager.getDefaultSharedPreferences(getActivity())
                .registerOnSharedPreferenceChangeListener(this);

        addPreferencesFromResource(R.xml.setting);

        Activity activity = getActivity();
//        ActionBar actionBar =  activity.getSupportActionBar();
        Switch actionBarSwitch = new Switch(activity);

//        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
//                ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionBar.setCustomView(actionBarSwitch, new ActionBar.LayoutParams(
//                ActionBar.LayoutParams.WRAP_CONTENT,
//                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL
//                | Gravity.RIGHT));
//
//        actionBar.setTitle("Sounds options");

        headServiceEnabler = new HeadServiceEnabler(getActivity(), actionBarSwitch);
        updateSettings();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionChecker = new PermissionChecker(getActivity());


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(RequestCode.HEAD_SERVICE_ENABLE_KEY)) {

            updateSettings();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        headServiceEnabler.resume();
        updateSettings();
    }

    @Override
    public void onPause() {
        super.onPause();
        headServiceEnabler.pause();
    }

    private void updateSettings() {

        boolean available = headServiceEnabler.isSwitchOn();

        int count = getPreferenceScreen().getPreferenceCount();

        if (!permissionChecker.isRequiredPermissionGranted()) {
            Intent intent = permissionChecker.createRequiredPermissionIntent();
            startActivityForResult(intent, PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE);

        } else {

            enableHeadServiceSwitch(true);

            if (available) {
                startHeadService();
            } else {
                stopHeadService();
            }

        }

        for (int i = 0; i < count; ++i) {
            Preference pref = getPreferenceScreen().getPreference(i);
            pref.setEnabled(available);
        }

    }

    private void startHeadService() {
        Context context = getActivity();
        context.startService(new Intent(context, HeadService.class));
    }

    private void stopHeadService() {
        Context context = getActivity();
        context.stopService(new Intent(context, HeadService.class));
    }

    private void enableHeadServiceSwitch(boolean enabled) {
        getPreferenceScreen().findPreference(RequestCode.HEAD_SERVICE_ENABLE_KEY).setEnabled(enabled);
    }
}
