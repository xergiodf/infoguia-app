package py.minicubic.info_guia_app.adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import py.minicubic.info_guia_app.ListaClientesActivity;
import py.minicubic.info_guia_app.R;
import py.minicubic.info_guia_app.VistaClienteActivity;
import py.minicubic.info_guia_app.dto.ClienteDTO;

/**
 * Created by hectorvillalba on 3/27/17.
 */

public class ListaClienteRecyclerAdapter extends RecyclerView.Adapter<ListaClienteRecyclerAdapter.MyViewHoldder>{
    private List<ClienteDTO> lista;
    private Activity activity;

    public ListaClienteRecyclerAdapter(Activity activity, List<ClienteDTO> lista){
        this.activity = activity;
        this.lista =lista;

    }

    @Override
    public MyViewHoldder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_clientes,parent,false);

        return new MyViewHoldder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHoldder holder, int position) {
        ClienteDTO clienteDTO = lista.get(position);
        holder.txtDescripcioListaCliente.setText(clienteDTO.getNombre_completo());
        holder.txtAbiertoCerrado.setText("Abierto ahora");
        holder.txtKilometrosDist.setText("A 3km de ti   ");
        holder.imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, VistaClienteActivity.class);
                i.putExtra("lista",(ArrayList<ClienteDTO>) lista);
                activity.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class MyViewHoldder extends RecyclerView.ViewHolder{
        public TextView txtDescripcioListaCliente, txtAbiertoCerrado,txtKilometrosDist;
        public ImageButton imageButtonHome;

        public MyViewHoldder(View itemView) {
            super(itemView);
            txtDescripcioListaCliente = (TextView) itemView.findViewById(R.id.txtDescripcioListaCliente);
            txtAbiertoCerrado = (TextView) itemView.findViewById(R.id.txtAbiertoCerrado);
            txtKilometrosDist = (TextView) itemView.findViewById(R.id.txtKilometrosDist);
            imageButtonHome = (ImageButton) itemView.findViewById(R.id.imageButtonHome);
        }
    }
}
