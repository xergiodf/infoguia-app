package py.minicubic.info_guia_app.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import py.minicubic.info_guia_app.R;
import py.minicubic.info_guia_app.dto.CategoriaDTO;
import py.minicubic.info_guia_app.dto.PublicacionClienteDTO;

/**
 * Created by hectorvillalba on 5/8/17.
 */

public class CategoriasAdapter extends BaseAdapter {
    Activity activity;
    List<CategoriaDTO> categoriaDTOs;
    private static LayoutInflater layoutInflater;


    public CategoriasAdapter(Activity activity, List<CategoriaDTO> lista){
        this.activity = activity;
        this.categoriaDTOs = lista;
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return categoriaDTOs.size();
    }

    @Override
    public CategoriaDTO getItem(int position) {
        return categoriaDTOs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null)
            view = layoutInflater.inflate(R.layout.item_categorias, null);
        TextView textView = (TextView) view.findViewById(R.id.txtCategoriaList);
        CategoriaDTO categoriaDTO = categoriaDTOs.get(position);
        textView.setText(categoriaDTO.getDescripcion());
        return view;
    }
}
