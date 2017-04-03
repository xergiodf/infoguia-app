package py.minicubic.info_guia_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
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
import py.minicubic.info_guia_app.dto.PublicacionClienteDTO;
import py.minicubic.info_guia_app.model.Novedades;

import static py.minicubic.info_guia_app.R.layout.item;

/**
 * Created by hectorvillalba on 3/20/17.
 */

public class PromocionesAdapter extends BaseAdapter {
    Activity activity;
    List<PublicacionClienteDTO> publicacionClienteDTOList;
    private static LayoutInflater layoutInflater;


    public PromocionesAdapter(Activity a, List<PublicacionClienteDTO> publicacionClienteDTOList  ) {
        activity = a;
        this.publicacionClienteDTOList = publicacionClienteDTOList;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return publicacionClienteDTOList.size();
    }

    @Override
    public PublicacionClienteDTO getItem(int position) {
        return publicacionClienteDTOList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = layoutInflater.inflate(R.layout.item_promociones,  null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageViewPromociones);
        PublicacionClienteDTO publicacionClienteDTO = new PublicacionClienteDTO();
        publicacionClienteDTO = publicacionClienteDTOList.get(position);
        Picasso.with(activity)
                    .load(publicacionClienteDTO.getDir_imagen())
                    .into(imageView);
        return view;
    }
}



