package com.bacnk.lovedays.common.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.bacnk.lovedays.main.units.InfoApp;
import com.bacnk.lovedays.main.database.DatabaseService;

import java.io.IOException;

public class SoundService extends Service {
    private static int POSITION_PAUSE_MUSIC = 0;
    public DatabaseService databaseService;
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        // Open database
        databaseService = new DatabaseService(getApplicationContext());
        databaseService.open();


        InfoApp infoApp = databaseService.getInforApp();
        if (infoApp.getMusicPathBackGround() != null && !infoApp.getMusicPathBackGround().isEmpty()) {
            Uri uri = Uri.parse(infoApp.getMusicPathBackGround());
            mediaPlayer = new MediaPlayer();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mediaPlayer.setAudioAttributes(
                        new AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .setUsage(AudioAttributes.USAGE_MEDIA)
                                .build()
                );
            }
            try {
                mediaPlayer.setDataSource(getApplicationContext(), uri);
                mediaPlayer.prepare();
                mediaPlayer.setLooping(true); // Set looping
                mediaPlayer.setVolume(50, 50);
                mediaPlayer.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
        return Service.START_NOT_STICKY;
    }
    public void onStart(Intent intent, int startId) {
    }

    public void onContinue() {
        if (mediaPlayer != null) {
            mediaPlayer.seekTo(POSITION_PAUSE_MUSIC);
            mediaPlayer.start();
        }
    }

    public void onPause() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            POSITION_PAUSE_MUSIC = mediaPlayer.getCurrentPosition();
        }
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }
    @Override
    public void onLowMemory() {
    }
}
