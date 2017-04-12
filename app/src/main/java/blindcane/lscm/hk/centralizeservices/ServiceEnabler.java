package blindcane.lscm.hk.centralizeservices;

import android.content.Context;
import android.widget.CompoundButton;
import android.widget.Switch;

/**
 * Created by LSCM on 2017/4/10.
 */

public abstract class ServiceEnabler implements CompoundButton.OnCheckedChangeListener {

    private final Context context;
    private Switch mSwitch;

    public ServiceEnabler(Context context, Switch mSwitch) {
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

    public void resume() {

        mSwitch.setOnCheckedChangeListener(this);
        mSwitch.setChecked(isSwitchOn());

    }

    public void pause() {
        mSwitch.setOnCheckedChangeListener(null);
    }

    public abstract boolean isSwitchOn();

}
