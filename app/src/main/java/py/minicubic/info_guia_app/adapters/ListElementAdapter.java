package py.minicubic.info_guia_app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import py.minicubic.info_guia_app.R;

/**
 * Created by hectorvillalba on 3/19/17.
 */

public class ListElementAdapter extends BaseAdapter {

    List<String> data;
    Context context;
    LayoutInflater layoutInflater;


    public ListElementAdapter(List<String> data, Context context) {
        super();
        this.data = data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView= layoutInflater.inflate(R.layout.item, null);

        TextView txt=(TextView)convertView.findViewById(R.id.list_item_text);

        txt.setText(data.get(position));
        return convertView;
    }

}