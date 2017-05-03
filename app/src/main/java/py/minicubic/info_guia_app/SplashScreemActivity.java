package py.minicubic.info_guia_app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import py.minicubic.info_guia_app.rest.HttpRequest;
import py.minicubic.info_guia_app.util.MedirDistanciaDirecciones;

public class SplashScreemActivity extends AppCompatActivity {
    MedirDistanciaDirecciones medirDistanciaDirecciones = MedirDistanciaDirecciones.getInstance();
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
        },3000);
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
