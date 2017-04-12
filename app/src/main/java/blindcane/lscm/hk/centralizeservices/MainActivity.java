package blindcane.lscm.hk.centralizeservices;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import static blindcane.lscm.hk.centralizeservices.util.RequestCode.TTS_SERVICE;

public class MainActivity extends AppCompatActivity {
    private Intent intentTTS = new Intent("TTS_REQUEST");
    private EditText messageContent, priority;
    public static TextView btnCount, speakCount;
    private CheckBox urgent;
    public static int messangeBTNPressCount = 0, speakSentenceCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
            }
        });

        messageContent = (EditText) findViewById(R.id.message);
        priority = (EditText) findViewById(R.id.priority);
        urgent = (CheckBox) findViewById(R.id.urgent);
        btnCount = (TextView) findViewById(R.id.btn_count);
        speakCount = (TextView) findViewById(R.id.speak_count);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        unbindService(mConnection);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lscm", "Binding the Service Manager");
        Intent intent = new Intent(MainActivity.this, ServicesManager.class);
        intent.putExtra("REQUEST_CODE", TTS_SERVICE);
        bindService(intent, mConnection, BIND_AUTO_CREATE);
        btnCount.setText("0");
        speakCount.setText("0");
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            Log.d("lscm", " Bind Service Manager Success.");
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            Log.d("lscm", " Bind Service Manager Fail.");
        }
    };

    public void btnsendMessage(View v) {
        int priorityInt = 1;
        try {
            priorityInt = Integer.parseInt(priority.getText().toString());
        } catch (Exception e) {
            Log.e("lscm", e.toString());
            priorityInt = 1;
        }
        //-----Send Message to Service-----
        intentTTS.putExtra("MESSAGE", messageContent.getText().toString());
        intentTTS.putExtra("PRIORITY", priorityInt);
        intentTTS.putExtra("IS_URGENT", urgent.isChecked());
        sendBroadcast(intentTTS);
        //---------------------------------
        messangeBTNPressCount++;
        btnCount.setText(String.valueOf(messangeBTNPressCount));
    }
}
