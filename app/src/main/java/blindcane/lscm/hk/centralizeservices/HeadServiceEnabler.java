package blindcane.lscm.hk.centralizeservices;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.Switch;
import blindcane.lscm.hk.centralizeservices.util.RequestCode;

/**
 * Created by LSCM on 2017/4/10.
 */

public class HeadServiceEnabler implements CompoundButton.OnCheckedChangeListener {

    protected final Context context;
    private Switch mSwitch;

    public HeadServiceEnabler(Context context, Switch mSwitch) {

        this.context = context;
        setSwitch(mSwitch);

    }

    public void setSwitch(Switch mSwitch) {
        if (this.mSwitch == mSwitch) {
            return;
        }
        if (this.mSwitch != null) {

            this.mSwitch.setOnCheckedChangeListener(null);
        }

        this.mSwitch = mSwitch;
        this.mSwitch.setOnCheckedChangeListener(this);
        this.mSwitch.setChecked(isSwitchOn());
    }


    public boolean isSwitchOn() {
        SharedPreferences prefs;
        prefs = PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getBoolean(RequestCode.HEAD_SERVICE_ENABLE_KEY, true);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(RequestCode.HEAD_SERVICE_ENABLE_KEY, isChecked);

        editor.commit();
    }

    public void resume() {
        mSwitch.setOnCheckedChangeListener(this);
        mSwitch.setChecked(isSwitchOn());
    }

    public void pause() {
        mSwitch.setOnCheckedChangeListener(null);
    }
}
