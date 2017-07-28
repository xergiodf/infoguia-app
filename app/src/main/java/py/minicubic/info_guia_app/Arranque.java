package py.minicubic.info_guia_app;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.multidex.MultiDex;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.util.Log;

import py.minicubic.info_guia_app.service.LocationService;
import py.minicubic.info_guia_app.service.Subscriber;
import py.minicubic.info_guia_app.util.CacheData;

/**
 * Created by hectorvillalba on 3/1/17.
 */

public class Arranque extends Application {
    private SharedPreferences sharedPreferences;
    private CacheData cacheData = CacheData.getInstance();
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
