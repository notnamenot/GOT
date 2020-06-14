package pl.edu.agh.wtm.got;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.widget.Toast;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) { // intent contains action
        if (Intent.ACTION_POWER_CONNECTED.equals(intent.getAction())) {
            Toast.makeText(context,"Ładowanie", Toast.LENGTH_LONG).show();
        }
//        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
//            Toast.makeText(context,"Connectivity changed", Toast.LENGTH_SHORT).show();
//        }
    }
}

//TODO dodać  liste punktów z wycieczki
//TODO odmiana punkty!
// TODO poprawa zapisywania stanu w search activity
