package py.minicubic.info_guia_app.util;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.DoubleBuffer;
import java.text.SimpleDateFormat;
import java.util.List;

import py.minicubic.info_guia_app.R;
import py.minicubic.info_guia_app.rest.HttpRequest;
import py.minicubic.info_guia_app.service.LocationService;

/**
 * Created by hectorvillalba on 4/25/17.
 */

public class MedirDistanciaDirecciones {
    private static MedirDistanciaDirecciones medirDistanciaDirecciones  = new MedirDistanciaDirecciones();
    private static  Location lastLocationl;
    private HttpRequest request;
    private Double longitud;
    private Double latitud;

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    private MedirDistanciaDirecciones(){

    }

    public static MedirDistanciaDirecciones getInstance(){
        return  medirDistanciaDirecciones;
    }

    public void obTenerUbicacion(){
        Location lastLocationl = LocationService.mLastLocation;
        if (lastLocationl == null){
            HttpRequest request = HttpRequest.get("http://ip-api.com/json");
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

    }
}
