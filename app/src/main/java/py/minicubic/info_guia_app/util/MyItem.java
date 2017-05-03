package py.minicubic.info_guia_app.util;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by hectorvillalba on 4/11/17.
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;
    private String title;
    private String snippet;
    private BitmapDescriptor icon;

    public MyItem(Double lat, Double lng,String title,String snippet, BitmapDescriptor icon){
        mPosition = new LatLng(lat, lng);
        this.title = title;
        this.snippet = snippet;
        this.icon = icon;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public BitmapDescriptor getIcon() {
        return icon;
    }

    public void setIcon(BitmapDescriptor icon) {
        this.icon = icon;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }
}

