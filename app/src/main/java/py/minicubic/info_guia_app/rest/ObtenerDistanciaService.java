package py.minicubic.info_guia_app.rest;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.Nullable;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import py.minicubic.info_guia_app.R;
import py.minicubic.info_guia_app.event.ObtenerDistanciaEvent;
import py.minicubic.info_guia_app.event.ObtenerDistanciaSucursalesEvent;
import py.minicubic.info_guia_app.service.LocationService;

/**
 * Created by hectorvillalba on 4/26/17.
 */

public class ObtenerDistanciaService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    HttpRequest request = null;
    Double lat = null;
    Double lon = null;
    String body ="";
    String distancia;
    String duracion;
    Double value;
    private static Location lastLocationl;
    private String from;


    public ObtenerDistanciaService() {
        super("ObtenerDistanciaService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        lat = intent.getDoubleExtra("lat", 1.0);
        lon = intent.getDoubleExtra("lon", 1.0);
        from = intent.getStringExtra("from");
        request = HttpRequest.get("http://ip-api.com/json");
        Double latitud = null;
        Double longitud = null;
        lastLocationl = LocationService.mLastLocation;
        if (lastLocationl == null){
            if (request.code() ==200){
                try {
                    JSONObject jsonArray =  new JSONObject(request.body());
                    latitud  = (Double) jsonArray.get("lat");
                    longitud = (Double) jsonArray.get("lon");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }else {
            latitud = lastLocationl.getLatitude();
            longitud = lastLocationl.getLongitude();
        }
        String url = getApplicationContext().getString(R.string.url_json_google_maps)  + "origins=" + latitud.toString() +","+ longitud.toString() + "&destinations="
                + lat.toString() +"," + lon.toString() + "&key=" + getApplicationContext().getString(R.string.key);
        HttpRequest request2 = HttpRequest.get(url );
        if (request2.code() == 200){
            try {
                JSONObject body2 = new JSONObject(request2.body());
                JSONArray array2 = body2.getJSONArray("rows");
                for (int i = 0;i<array2.length(); i++){
                    JSONObject dist = array2.getJSONObject(i);
                    JSONArray dis2 = dist.getJSONArray("elements");
                    for (int j = 0;j<dis2.length(); j++){
                        JSONObject object = dis2.getJSONObject(j);
                        JSONObject dur = object.getJSONObject("duration");
                        duracion = dur.getString("text");
                        JSONObject di = object.getJSONObject("distance");
                        distancia = di.getString("text");
                        value = di.getDouble("value");
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (from.equalsIgnoreCase("clientes")){
            EventBus.getDefault().post(new ObtenerDistanciaEvent(distancia, duracion, value));
        }else {
            EventBus.getDefault().post(new ObtenerDistanciaSucursalesEvent(distancia, duracion, value));
        }
        Log.i("ObtenerDistancia", "Entregado");
    }
}
