package com.example.services;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.Binder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import java.util.Random;

public class LocalService extends Service {
    private LocalBinder mBinder = new LocalBinder(); // Binder given to clients to allow them to communicate with the service.
    private MediaPlayer player; // Media player to play music in the background

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public class LocalBinder extends Binder { // Class used for the client Binder to interact with the service (LocalService)
        LocalService getService() {
            // Return this instance of LocalService so clients can call public methods.
            return LocalService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) { // Called when all clients have disconnected from a particular interface published by the service.
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() { // Called by the system to notify a Service that it is no longer used and is being removed.
        super.onDestroy();
        if(player != null) {
            player.stop();
            player.release();
            player = null;
            Log.println(Log.DEBUG, "LocalService", "Service Stopped");
            Toast.makeText(this, "Music stopped", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to start the music playback
    public void startMusic() {
        if (player == null) {
            player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
            player.setLooping(true);
            player.start();
            Log.println(Log.DEBUG, "LocalService", "Service Started");
            Toast.makeText(this, "Music started", Toast.LENGTH_SHORT).show();
        }
    }
}
