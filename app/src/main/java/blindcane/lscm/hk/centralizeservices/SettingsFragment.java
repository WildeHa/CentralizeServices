//package blindcane.lscm.hk.centralizeservices;
//
//import android.annotation.TargetApi;
//import android.app.ActionBar;
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.os.Build;
//import android.os.Bundle;
//import android.preference.Preference;
//import android.preference.PreferenceFragment;
//import android.preference.PreferenceManager;
//import android.support.annotation.Nullable;
//import android.view.Gravity;
//import android.widget.Switch;
//import android.widget.Toast;
//
///**
// * Created by LSCM on 2017/4/5.
// */
//
//public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
//
//    private PermissionChecker permissionChecker;
//    private HeadServiceEnabler headServiceEnabler;
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        PreferenceManager.getDefaultSharedPreferences(getActivity()).registerOnSharedPreferenceChangeListener(this);
//        addPreferencesFromResource(R.xml.setting);
//
//        Activity activity = getActivity();
//        ActionBar actionbar = activity.getActionBar();
//        Switch actionBarSwitch = new Switch(activity);
//
//        actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
//                ActionBar.DISPLAY_SHOW_CUSTOM);
//        actionbar.setCustomView(actionBarSwitch, new ActionBar.LayoutParams(
//                ActionBar.LayoutParams.WRAP_CONTENT,
//                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.CENTER_VERTICAL
//                | Gravity.RIGHT));
//
//        actionbar.setTitle("Sounds options");
//
//        headServiceEnabler = new HeadServiceEnabler(getActivity(), actionBarSwitch);
//        updateSettings();
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//
//        permissionChecker = new PermissionChecker(getActivity());
//        enableHeadServiceCheckbox(false);
//
//        permissionChecker = new PermissionChecker(getActivity());
//
//        if (!permissionChecker.isRequiredPermissionGranted()) {
//
//            enableHeadServiceCheckbox(false);
//            Intent intent = permissionChecker.createRequiredPermissionIntent();
//            startActivityForResult(intent, PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE);
//
//        } else {
//
//            enableHeadServiceCheckbox(true);
//
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.M)
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == PermissionChecker.REQUIRED_PERMISSION_REQUEST_CODE) {
//            if (!permissionChecker.isRequiredPermissionGranted()) {
//                Toast.makeText(getActivity(), "Required permission is not granted. Please restart the app and grant required permission.", Toast.LENGTH_LONG).show();
//            } else {
//                enableHeadServiceCheckbox(true);
//                getPreferenceScreen().findPreference(RequestCode.PreferenceKey.SERVICE_ENABLE_KEY).setEnabled(false);
//            }
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
//        headServiceEnabler.resume();
//        updateSettings();
//    }
//
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
//        headServiceEnabler.pause();
//
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
//    }
//
//    @Override
//    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
//
////        if (RequestCode.PreferenceKey.SERVICE_ENABLE_KEY.equals(key)) {
////            boolean enabled = sharedPreferences.getBoolean(key, false);
////            if (enabled) {
////                startHeadService();
////            } else {
////                stopHeadService();
////            }
////        }
//
//        if (key.equals(RequestCode.PreferenceKey.TTS_SERVICE_ENABLE_KEY)) {
//            updateSettings();
//        }
//    }
//
//    protected void updateSettings() {
//
//        boolean available = headServiceEnabler.isSwitchOn();
//        int count = getPreferenceScreen().getPreferenceCount();
//
//        for (int i = 0; i < count; i++) {
//
//            Preference preference = getPreferenceScreen().getPreference(i);
//            preference.setEnabled(available);
//
//        }
//    }
//;
//    private void enableHeadServiceCheckbox(boolean enabled) {
//        getPreferenceScreen().findPreference(RequestCode.PreferenceKey.SERVICE_ENABLE_KEY).setEnabled(enabled);
//    }
//
//    private void startHeadService() {
//        Context context = getActivity();
//        context.startService(new Intent(context, HeadService.class));
//    }
//
//    private void stopHeadService() {
//        Context context = getActivity();
//        context.stopService(new Intent(context, HeadService.class));
//    }
//}
