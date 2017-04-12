package blindcane.lscm.hk.centralizeservices.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import blindcane.lscm.hk.centralizeservices.SoundMessage;

import static blindcane.lscm.hk.centralizeservices.MainActivity.speakSentenceCount;

public class TTSService extends Service implements TextToSpeech.OnInitListener {

    private TextToSpeech textToSpeech;
    private BroadcastReceiver commandReceiver;
    private static LinkedHashMap<Integer, List<SoundMessage>> messageQueue;
    private HashMap<String, String> myHashAlarm;
    private static MediaPlayer mediaPlayer;
    private static volatile boolean isMediaPlayerRelease = false;
    private static volatile int playbackPosition = 0;
    private Intent intentTTSRespond = new Intent("TTS_RESPOND");

    public TTSService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(this.getBaseContext(), this);
            messageQueue = new LinkedHashMap<>();
        }

        commandReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean pause = false, resume = false;
                if (intent != null) {
                    pause = intent.getBooleanExtra("PAUSE", false);
                    resume = intent.getBooleanExtra("RESUME", false);
                    if (!(!pause && !resume)) {
                        if (pause) {
                            if (textToSpeech.isSpeaking())
                                textToSpeech.stop();
                            if (mediaPlayer != null && !isMediaPlayerRelease && mediaPlayer.isPlaying()) {
                                playbackPosition = mediaPlayer.getCurrentPosition();
                                mediaPlayer.pause();
                            }
                            sendBroadcast(intentTTSRespond);
                        } else if (resume) {
                            mediaPlayer.seekTo(playbackPosition);
                            mediaPlayer.start();
                        }
                    } else if (intent.getStringExtra("MESSAGE") != null) {
                        String content = intent.getStringExtra("MESSAGE");
                        String url = intent.getStringExtra("URL");
                        String fileName = intent.getStringExtra("FILENAME");
                        int priority = intent.getIntExtra("PRIORITY", 1);
                        boolean urgent = intent.getBooleanExtra("URGENT", false);
                        boolean isMP3 = intent.getBooleanExtra("IS_MP3", false);
                        boolean isFileExist = intent.getBooleanExtra("IS_FILE_EXIST", false);

                        Log.d("lscm", "Message : " + content + " URL : " + url + " FileName : " + fileName + " Urgent : " + urgent + "  isMp3 : " + isMP3 + " isFileExist :" + isFileExist + " Priority : " + priority + " Urgent : " + urgent);
                        SoundMessage currentMessage = new SoundMessage(content, url, fileName, urgent, isMP3, isFileExist, priority);
                        if (urgent) {
                            textToSpeech.stop();
                            textToSpeech.speak(content, TextToSpeech.QUEUE_FLUSH, myHashAlarm);
                        } else {
                            if (messageQueue.get(priority) == null)
                                messageQueue.put(priority, new ArrayList<SoundMessage>());
                            messageQueue.get(priority).add(currentMessage);

                            if ((mediaPlayer != null && !isMediaPlayerRelease && !mediaPlayer.isPlaying()) || !textToSpeech.isSpeaking()) {
                                SortedSet<Integer> keys = new TreeSet<>(messageQueue.keySet());
                                if (keys != null && keys.size() != 0 && messageQueue.get(keys.last()) != null && messageQueue.get(keys.last()).size() != 0) {
                                    Log.d("lscm", "Keys Last : " + keys.last());
                                    if (!(messageQueue.get(keys.last()).get(0).isMP3())) {
                                        textToSpeech.speak(messageQueue.get(keys.last()).get(0).getContent(), TextToSpeech.QUEUE_ADD, myHashAlarm);
                                    } else {
                                        if (messageQueue.get(keys.last()).get(0).isFileExist()) {
                                            mediaPlayer = MediaPlayer.create(TTSService.this, Uri.parse(messageQueue.get(keys.last()).get(0).getFileName()));
                                            mediaPlayer.start();
                                        } else {
                                            try {
                                                isMediaPlayerRelease = false;
                                                mediaPlayer.setDataSource(messageQueue.get(keys.last()).get(0).getUrl());
                                                mediaPlayer.prepare();
                                                mediaPlayer.start();
                                            } catch (Exception e) {
                                                Log.e("lscm", e.toString());
                                            }
                                        }
                                    }
                                    messageQueue.get(keys.last()).remove(0);
                                    if (messageQueue.get(keys.last()).size() == 0)
                                        messageQueue.remove(keys.last());
                                }
                            }
                        }

                    }
                }
            }
        };
        IntentFilter ttsCommandFilter = new IntentFilter();
        ttsCommandFilter.addAction("TTS_REQUEST");
        registerReceiver(commandReceiver, ttsCommandFilter);

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        myHashAlarm = new HashMap<>();
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_MUSIC));
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "CONTENT MESSAGE");
        if (mediaPlayer == null)
            mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                onCompleteSoundTrack();
                intentTTSRespond.putExtra("MP3_DONE", true);
                intentTTSRespond.putExtra("TTS_DONE", false);
            }
        });
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        textToSpeech.stop();
        releaseMedia();
        if (messageQueue != null)
            messageQueue.clear();
        unregisterReceiver(commandReceiver);
        //unregisterReceiver(mediaReceiver);

    }

    @Override
    public boolean onUnbind(Intent intent) {
        textToSpeech.stop();
        releaseMedia();
        if (messageQueue != null)
            messageQueue.clear();
        unregisterReceiver(commandReceiver);
        //unregisterReceiver(mediaReceiver);
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }

    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
//            textToSpeech.speak("Initialize Success", TextToSpeech.QUEUE_ADD, myHashAlarm);
            textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                @Override
                public void onStart(String s) {

                }

                @Override
                public void onDone(String utteranceId) {
                    speakSentenceCount++;
                    Log.d("lscm", "Speak Sentence Count : " + speakSentenceCount);
                    if (utteranceId.equals("CONTENT MESSAGE") && messageQueue != null && messageQueue.size() != 0) {
                        onCompleteSoundTrack();
                        intentTTSRespond.putExtra("MP3_DONE", false);
                        intentTTSRespond.putExtra("TTS_DONE", true);
                    }
                }

                @Override
                public void onError(String s) {

                }
            });
            Log.d("lscm", "Initialize Success");
        }
    }

    private static void releaseMedia() {
        try {
            if (mediaPlayer != null && !isMediaPlayerRelease) {
                mediaPlayer.release();
                playbackPosition = 0;
                isMediaPlayerRelease = true;
            }
        } catch (Exception e) {
            Log.e("lscm", e.toString());
        }
    }

    private void onCompleteSoundTrack() {
        Log.d("lscm", "Message Queue size : " + messageQueue.size());
        sendBroadcast(intentTTSRespond);
        SortedSet<Integer> keys = new TreeSet<Integer>(messageQueue.keySet());
        if (keys != null && keys.size() != 0 && messageQueue.get(keys.last()) != null && messageQueue.get(keys.last()).size() != 0) {
            Log.d("lscm", "Keys Last : " + keys.last());
            if (!(messageQueue.get(keys.last()).get(0).isMP3())) {
                textToSpeech.speak(messageQueue.get(keys.last()).get(0).getContent(), TextToSpeech.QUEUE_ADD, myHashAlarm);
            } else {
                if (messageQueue.get(keys.last()).get(0).isFileExist()) {
                    mediaPlayer = MediaPlayer.create(TTSService.this, Uri.parse(messageQueue.get(keys.last()).get(0).getFileName()));
                    mediaPlayer.start();
                } else {
                    try {
                        isMediaPlayerRelease = false;
                        mediaPlayer.setDataSource(messageQueue.get(keys.last()).get(0).getUrl());
                        mediaPlayer.prepare();
                        mediaPlayer.start();
                    } catch (Exception e) {
                        Log.e("lscm", e.toString());
                    }
                }
            }
            messageQueue.get(keys.last()).remove(0);
            if (messageQueue.get(keys.last()).size() == 0)
                messageQueue.remove(keys.last());
        }
    }
}
