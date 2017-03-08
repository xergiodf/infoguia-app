package py.minicubic.info_guia_app;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import py.minicubic.info_guia_app.service.Subscriber;

/**
 * Created by hectorvillalba on 3/1/17.
 */

public class Arranque extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Intent intent = new Intent(this, Subscriber.class);
        startService(intent);
        Log.i("Arranque", "Servicio Subscriber iniciado...");
    }
}
