package py.minicubic.info_guia_app.adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import py.minicubic.info_guia_app.MapsSucursalesActivity;
import py.minicubic.info_guia_app.R;
import py.minicubic.info_guia_app.VistaClienteActivity;
import py.minicubic.info_guia_app.dto.ClienteDTO;
import py.minicubic.info_guia_app.dto.SucursalClientesDTO;
import py.minicubic.info_guia_app.rest.ObtenerDistanciaService;

/**
 * Created by hectorvillalba on 4/10/17.
 */

public class ListaSucursalesRecyclerAdapter extends RecyclerView.Adapter<ListaSucursalesRecyclerAdapter.MyViewHoldder>
{

    private List<SucursalClientesDTO> listaSucursales;
    private Activity activity;
    private Double lat;
    private Double lon;
    private MyViewHoldder holder;

    public ListaSucursalesRecyclerAdapter(Activity activity, List<SucursalClientesDTO> listaSucursales){
        this.activity = activity;
        this.listaSucursales = listaSucursales;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private String mItem;
        private TextView mTextView;

        public ViewHolder(View view) {
            super(view);
            view.setOnClickListener(this);
            mTextView = (TextView) view;
        }

        public void setItem(String item) {
            mItem = item;
            mTextView.setText(item);
        }

        @Override
        public void onClick(View view) {

        }
    }

    @Override
    public ListaSucursalesRecyclerAdapter.MyViewHoldder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_sucursales,parent,false);

        return new ListaSucursalesRecyclerAdapter.MyViewHoldder(itemView);
    }

    @Override
    public void onBindViewHolder(ListaSucursalesRecyclerAdapter.MyViewHoldder holder, int position) {
        final SucursalClientesDTO sucursalClientesDTO = listaSucursales.get(position);
        holder.txtSucursalDistancia.setText("A " + sucursalClientesDTO.getDistancia() + " de ti ");
        holder.txtSucursalDuracion.setText(sucursalClientesDTO.getDuracion());
        holder.txtNombreClienteSucursal.setText(sucursalClientesDTO.getNombre_sucursal());
        holder.txtSucursalDireccion.setText(sucursalClientesDTO.getDireccion_fisica());
        holder.txtTelefonoSucursal.setText(sucursalClientesDTO.getTelefonos());
        Picasso.with(activity)
                .load(sucursalClientesDTO.getPhoto_url())
                .into(holder.imageViewListaSucursales);
        holder.imageButtonHomeSucursales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, VistaClienteActivity.class);
                i.putExtra("idCliente",sucursalClientesDTO.getId_cliente());
                i.putExtra("titulo", sucursalClientesDTO.getNombre_sucursal());
                i.putExtra("coordenadas", sucursalClientesDTO.getCoordenadas());
                i.putExtra("telefonos", sucursalClientesDTO.getTelefonos());
                i.putExtra("direccion", sucursalClientesDTO.getDireccion_fisica());
                i.putExtra("photo_url", sucursalClientesDTO.getPhoto_url());
                activity.startActivity(i);
            }
        });
        holder.imageButtonLocationSucursales.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(activity, MapsSucursalesActivity.class);
                //i.putExtra("idCliente",sucursalClientesDTO.getId_cliente());
                //i.putExtra("sucursal", sucursalClientesDTO.getNombre_sucursal());
                //i.putExtra("photo_url", sucursalClientesDTO.getPhoto_url());
                //activity.startActivity(i);
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ sucursalClientesDTO.getCoordenadas().split("\\|")[0].toString()
                        + "," +sucursalClientesDTO.getCoordenadas().split("\\|")[1].toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return listaSucursales.size();
    }

    public class MyViewHoldder extends RecyclerView.ViewHolder{
        public TextView txtNombreClienteSucursal, txtSucursalDireccion,txtSucursalDistancia, txtSucursalDuracion, txtTelefonoSucursal;
        public ImageView imageViewListaSucursales;
        public ImageButton imageButtonHomeSucursales, imageButtonLocationSucursales;

        public MyViewHoldder(View itemView) {
            super(itemView);
            txtNombreClienteSucursal = (TextView) itemView.findViewById(R.id.txtNombreClienteSucursal);
            txtSucursalDireccion = (TextView) itemView.findViewById(R.id.txtSucursalDireccion);
            txtSucursalDistancia = (TextView) itemView.findViewById(R.id.txtSucursalDistancia);
            txtSucursalDuracion = (TextView) itemView.findViewById(R.id.txtSucursalDuracion);
            imageViewListaSucursales = (ImageView) itemView.findViewById(R.id.imageViewListaSucursales);
            imageButtonHomeSucursales = (ImageButton) itemView.findViewById(R.id.imageButtonHomeSucursales);
            imageButtonLocationSucursales = (ImageButton) itemView.findViewById(R.id.imageButtonLocationSucursales);
            txtTelefonoSucursal = (TextView) itemView.findViewById(R.id.txtSucursalTelefono);
         }
    }


    public void updateUI(final String distancia, final String duracion){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                holder.txtSucursalDuracion.setText(duracion);
                holder.txtSucursalDistancia.setText("A " + distancia + " de ti");
            }
        });
    }


}
