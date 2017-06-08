package py.minicubic.info_guia_app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.google.android.gms.location.LocationServices;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import py.minicubic.info_guia_app.event.SubscriberAlready;
import py.minicubic.info_guia_app.rest.HttpRequest;
import py.minicubic.info_guia_app.service.Service2;
import py.minicubic.info_guia_app.service.Subscriber;
import py.minicubic.info_guia_app.util.CacheData;
import py.minicubic.info_guia_app.util.MedirDistanciaDirecciones;

public class SplashScreemActivity extends AppCompatActivity {
    private static final int WRITE_EXTERNAL_STORAGE =1  ;
    private static final int READ_PHONE_STATE = 2;
    MedirDistanciaDirecciones medirDistanciaDirecciones = MedirDistanciaDirecciones.getInstance();
    boolean servicioIniciado = false;
    private SharedPreferences sharedPreferences;
    private CacheData cacheData = CacheData.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screem);
    }


    @Override
    protected void onResume() {
        super.onResume();
        UbicacionRequest ubicacionRequest = new UbicacionRequest();
        ubicacionRequest.execute();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SplashScreemActivity.this.finish();
                Intent mainIntent = new Intent(SplashScreemActivity.this, MainActivity.class);
                SplashScreemActivity.this.startActivity(mainIntent);
            }
        },5000);
    }


    private class UbicacionRequest extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            HttpRequest request = HttpRequest.get("http://ip-api.com/json");
            if (request.code() ==200){
                try {
                    JSONObject jsonArray =  new JSONObject(request.body());
                    medirDistanciaDirecciones.setLatitud((Double) jsonArray.get("lat"));
                    medirDistanciaDirecciones.setLongitud((Double) jsonArray.get("lon"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
            return null;
        }
    }
}
