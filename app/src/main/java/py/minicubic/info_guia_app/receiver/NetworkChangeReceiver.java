package py.minicubic.info_guia_app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;

import py.minicubic.info_guia_app.event.NetworkStateChangeEvent;

public class NetworkChangeReceiver extends BroadcastReceiver {
    public NetworkChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null){
            if (networkInfo.isConnected()){
                Log.i("NetWorkChange", "Hay coneccion...");
                EventBus.getDefault().post(new NetworkStateChangeEvent(true));
            }
        }else {
            Log.i("NetWorkChange", "Se perdio la conexion...");
            EventBus.getDefault().post(new NetworkStateChangeEvent(false));
        }

    }
}
