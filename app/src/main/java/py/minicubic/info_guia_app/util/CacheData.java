package py.minicubic.info_guia_app.util;

import android.net.ConnectivityManager;

/**
 * Created by hectorvillalba on 5/9/17.
 */

public class CacheData {


    public static CacheData instance = new CacheData();
    private String imei;

    public static CacheData getInstance() {
        return instance;
    }

    public static void setInstance(CacheData instance) {
        CacheData.instance = instance;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    private CacheData(){

    }

}
