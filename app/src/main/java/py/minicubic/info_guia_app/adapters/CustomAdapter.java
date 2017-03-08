package py.minicubic.info_guia_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import py.minicubic.info_guia_app.R;
import py.minicubic.info_guia_app.dto.NovedadesDTO;
import py.minicubic.info_guia_app.model.Novedades;

/**
 * Created by hectorvillalba on 2/4/17.
 */

public class CustomAdapter extends BaseAdapter {
    Activity activity;
    List<NovedadesDTO> novedadesList;
    private static LayoutInflater layoutInflater;


    public CustomAdapter(Activity a, List<NovedadesDTO> novedadesList  ) {
        activity = a;
        this.novedadesList = novedadesList;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return novedadesList.size();
    }

    @Override
    public Object getItem(int position) {
        return novedadesList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = layoutInflater.inflate(R.layout.novedades_list,  null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
        NovedadesDTO novedades = new NovedadesDTO();
        novedades = novedadesList.get(position);
            Picasso.with(activity)
                    .load(R.drawable.itau)
                    .into(imageView);

        return view;
    }
}
