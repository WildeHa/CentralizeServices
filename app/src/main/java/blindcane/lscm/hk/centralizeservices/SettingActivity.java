package blindcane.lscm.hk.centralizeservices;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

import blindcane.lscm.hk.centralizeservices.adapter.ServicePreferenceHeaderAdapter;

/**
 * Created by LSCM on 2017/4/7.
 */

public class SettingActivity extends PreferenceActivity {

    private List<PreferenceActivity.Header> headers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SettingsFragment settingsFragment = new SettingsFragment();
//        getFragmentManager().beginTransaction().replace(android.R.id.content, settingsFragment).commit();

    }

    @Override
    protected boolean isValidFragment(String fragmentName) {
        return HeadServicePreferenceFragment.class.getName().equals(fragmentName);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getListAdapter() instanceof ServicePreferenceHeaderAdapter) {
            ((ServicePreferenceHeaderAdapter) getListAdapter()).resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (getListAdapter() instanceof ServicePreferenceHeaderAdapter) {
            ((ServicePreferenceHeaderAdapter) getListAdapter()).pause();
        }
    }

    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.preference_headers, target);
        headers = target;
    }

    public void setListAdapter(ListAdapter adapter) {
        int count;
        if (headers == null) {
            headers = new ArrayList<>();

            count = adapter.getCount();

            for (int i = 0; i < count; i++) {
                headers.add((PreferenceActivity.Header) adapter.getItem(i));
            }
        }

        super.setListAdapter(new ServicePreferenceHeaderAdapter(this, headers));
    }


}
