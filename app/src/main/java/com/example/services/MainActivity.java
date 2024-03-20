package com.example.services;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

import android.content.Intent;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button start, stop, intentService, /*slideExample ,*/ boundServiceStart, boundServiceStop;
    private BroadcastReceiver broadcastReceiverCompleted, broadcastReceiverStarted;
    private LocalService mLocalService; // Service instance to interact with
    private boolean isServiceConnected; // Flag to check if the service is connected
    private ServiceConnection serviceConnection = new ServiceConnection() { // Service connection callback methods
        @Override
        public void onServiceConnected(ComponentName name, IBinder iBinder) {
            LocalService.LocalBinder binder = (LocalService.LocalBinder) iBinder;
            mLocalService = binder.getService();
            mLocalService.startMusic();
            isServiceConnected = true;
        }

        @Override
        // Gọi khi kết nối với service bị mất đột ngột.
        public void onServiceDisconnected(ComponentName name) {
            isServiceConnected = false;
        }
    };

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Assigning buttons to their respective IDs
        start = (Button) findViewById( R.id.startButton );
        stop = (Button) findViewById( R.id.stopButton );
        intentService = (Button) findViewById( R.id.intentServiceButton);
//        slideExample = (Button) findViewById( R.id.slideExampleButton );
        boundServiceStart = (Button) findViewById( R.id.boundServiceButtonStart );
        boundServiceStop = (Button) findViewById( R.id.boundServiceButtonStop );

        // Setting onClickListener to the buttons
        start.setOnClickListener( this );
        stop.setOnClickListener( this );
        intentService.setOnClickListener( this );
//        slideExample.setOnClickListener( this );
        boundServiceStart.setOnClickListener( this );
        boundServiceStop.setOnClickListener( this );

        // Registering broadcast receivers for the IntentService
        broadcastReceiverStarted = new MyBroadcastReceiver();
        registerReceiver(broadcastReceiverStarted, new IntentFilter(HelloIntentService.TASK_STARTED));
        broadcastReceiverCompleted = new MyBroadcastReceiver();
        registerReceiver(broadcastReceiverCompleted, new IntentFilter(HelloIntentService.TASK_COMPLETED));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiverStarted);
        unregisterReceiver(broadcastReceiverCompleted);
    }

    public void onClick(View view) {
        if(view == start){ // Start the service when the start button is clicked
            startService(new Intent( this, NewService.class ).putExtra("inputExtra", "This line is sent from MainActivity...") );
            Toast.makeText( this, "Service Started", Toast.LENGTH_SHORT ).show();
        }
        else if (view == stop){ // Stop the service when the stop button is clicked
            stopService(new Intent( this, NewService.class ) );
            Toast.makeText( this, "Service Stopped", Toast.LENGTH_SHORT ).show();
        }
        if (view == intentService){ // Start the IntentService when the button is clicked
            startService(new Intent( this, HelloIntentService.class ) );
            //Toast.makeText( this, "Intent Service Started", Toast.LENGTH_SHORT ).show();
        }
//        if (view == slideExample){
//            startService(new Intent( this, HelloService.class ) );
//        }

        if (view == boundServiceStart){ // Start the bound service when the button is clicked
            Intent intent = new Intent( this, LocalService.class );
            // Bind to the service from the activity and start the service if it is not already running (BIND_AUTO_CREATE) flag
            bindService(intent, serviceConnection, BIND_AUTO_CREATE);
        } else if (view == boundServiceStop){ // Stop the bound service when the button is clicked
            if (isServiceConnected){
                unbindService(serviceConnection);
                isServiceConnected = false;
            }
        }
    }
}