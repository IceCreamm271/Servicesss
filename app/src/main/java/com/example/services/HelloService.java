package com.example.services;

import android.media.MediaPlayer;
import android.os.Process;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class HelloService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // Handler that receives messages from the thread
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // Restore interrupt status.
            // Phát nhạc từ nguồn assets

            Log.println(Log.DEBUG, "HelloService", "handleMessage");
            mediaPlayer.start();
            Toast.makeText(getApplicationContext(), "Music playback started", Toast.LENGTH_SHORT).show();
            Log.println(Log.DEBUG, "HelloService", "Music playback started");

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // Dừng phát nhạc khi hoàn tất
                    mediaPlayer.stop();
                    // Hiển thị thông báo
                    Log.println(Log.DEBUG, "HelloService", "Music playback completed");
                    Toast.makeText(getApplicationContext(), "Music playback completed", Toast.LENGTH_SHORT).show();
                }
            });

            Thread.currentThread().interrupt();
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }
    @Override
    public void onCreate() {
        // Start up the thread running the service.  Note that we create a separate thread because the service normally runs in the process's main thread, which we don't want to block.  We also make it background priority so CPU-intensive work will not disrupt our UI.
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        // Get the HandlerThread's Looper and use it for our Handler
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

        mediaPlayer = MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_RINGTONE_URI);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();
        // For each start request, send a message to start a job and deliver the
        // start ID so we know which request we're stopping when we finish the job
        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);
        // If we get killed, after returning from here, restart
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        mServiceLooper.quit();

        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}
