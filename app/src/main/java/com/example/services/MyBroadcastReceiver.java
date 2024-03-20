package com.example.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(HelloIntentService.TASK_COMPLETED)) {
            // Update UI here
            Toast.makeText(context, "Task completed in IntentService", Toast.LENGTH_SHORT).show();
            // You can update other UI elements based on your needs
        }
        if (action.equals(HelloIntentService.TASK_STARTED)) {
            // Update UI here
            Toast.makeText(context, "Task started in IntentService", Toast.LENGTH_SHORT).show();
            // You can update other UI elements based on your needs
        }
    }
}

