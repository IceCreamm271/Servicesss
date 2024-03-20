package com.example.services;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class HelloIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public HelloIntentService() {
        super("HelloIntentService");
    }
    public static final String TASK_COMPLETED = "com.example.services.task_completed";
    public static final String TASK_STARTED = "com.example.services.task_started";
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("ExampleIntentService", "Task in progress");
        Intent broadcastIntentStarted = new Intent(TASK_STARTED);
        sendBroadcast(broadcastIntentStarted);

        // Simulate a long running task by
        // sleeping the thread for 5 seconds
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // Print stack trace if an
            // InterruptedException occurs
            e.printStackTrace();
        }
        Log.d("ExampleIntentService", "Task completed");
        // Send broadcast to notify Activity
        Intent broadcastIntentCompleted = new Intent(TASK_COMPLETED);
        sendBroadcast(broadcastIntentCompleted);
    }

}

