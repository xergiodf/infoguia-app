package py.minicubic.info_guia_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.squareup.picasso.Picasso;

public class SucursalWindowInfoMapAdapterActivity extends AppCompatActivity implements GoogleMap.InfoWindowAdapter  {

    LayoutInflater inflater = null;
    String photo_url;

    public SucursalWindowInfoMapAdapterActivity(LayoutInflater inflater, String photo_url){
        this.inflater = inflater;
        this.photo_url = photo_url;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        View infoWindows = inflater.inflate(R.layout.activity_sucursal_window_info_map_adapter, null);
        ImageView img = (ImageView) infoWindows.findViewById(R.id.badge);

        TextView title = (TextView) infoWindows.findViewById(R.id.title);
        TextView description = (TextView) infoWindows.findViewById(R.id.snippet);

        title.setText(marker.getTitle());
        if(title.getText().equals("")){
            return (infoWindows);
        }
        description.setText(marker.getSnippet());
        Picasso.with(this)
                .load(photo_url)
                .into(img);
        return (infoWindows);
    }
}
