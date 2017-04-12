package blindcane.lscm.hk.centralizeservices.adapter;

import android.content.Context;
import android.preference.PreferenceActivity;
import android.preference.PreferenceActivity.Header;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

import blindcane.lscm.hk.centralizeservices.HeadServiceEnabler;
import blindcane.lscm.hk.centralizeservices.R;

/**
 * Created by LSCM on 2017/4/10.
 */

public class ServicePreferenceHeaderAdapter extends ArrayAdapter<PreferenceActivity.Header> {

    static final int HEADER_TYPE_CATEGORY = 0;
    static final int HEADER_TYPE_NORMAL = 1;
    static final int HEADER_TYPE_SWITCH = 2;

    private LayoutInflater inflater;
    private HeadServiceEnabler enabler;

    public ServicePreferenceHeaderAdapter(Context context, List<PreferenceActivity.Header> objects) {
        super(context, 0, objects);

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        enabler = new HeadServiceEnabler(context, new Switch(context));

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Header header = getItem(position);
        int headerType = getHeaderType(header);

        View view = null;

        switch (headerType) {
            case HEADER_TYPE_SWITCH:
                view = inflater.inflate(R.layout.preferences_header_switch, parent, false);

                ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                imageView.setBackgroundResource(header.iconRes);

                TextView textViewTitle = (TextView) view.findViewById(R.id.title);
                textViewTitle.setText(header.getTitle(getContext().getResources()));

                TextView textViewSummary = (TextView) view.findViewById(R.id.summary);
                textViewSummary.setText(header.getSummary(getContext().getResources()));

                break;
        }
        return view;

    }

    public static int getHeaderType(Header header) {

        if (header.fragment == null && header.intent == null) {
            return HEADER_TYPE_CATEGORY;
        } else if (header.id == R.id.service_setting) {
            return HEADER_TYPE_SWITCH;
        } else {
            return HEADER_TYPE_NORMAL;
        }
    }

    public void resume() {
        enabler.resume();
    }

    public void pause() {
        enabler.pause();
    }
}
