package py.minicubic.info_guia_app.adapters;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import py.minicubic.info_guia_app.MapsSucursalesActivity;
import py.minicubic.info_guia_app.R;
import py.minicubic.info_guia_app.VistaClienteActivity;
import py.minicubic.info_guia_app.dto.ClienteDTO;
import py.minicubic.info_guia_app.rest.HttpRequest;
import py.minicubic.info_guia_app.service.LocationService;
import py.minicubic.info_guia_app.util.MedirDistanciaDirecciones;

/**
 * Created by hectorvillalba on 3/27/17.
 */

public class ListaClienteRecyclerAdapter extends RecyclerView.Adapter<ListaClienteRecyclerAdapter.MyViewHoldder>{
    private List<ClienteDTO> lista;
    private Activity activity;
    MedirDistanciaDirecciones distanciaDirecciones;
    private static  Location lastLocationl;
    MyViewHoldder holder;
    HttpRequest request = null;
    Double lat = null;
    Double lon = null;
    String body ="";
    String distancia;
    String duracion;

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
        this.holder = holder;
        final ClienteDTO clienteDTO = lista.get(position);
        holder.txtDescripcioListaCliente.setText(clienteDTO.getNombre_completo());
        holder.txtAbiertoCerrado.setText("Abierto ahora");
        holder.txtKilometrosDist.setText("A " + clienteDTO.getDistancia() + " de ti   ");
        holder.txtDuracionDist.setText(clienteDTO.getDuracion());
        holder.txtNombreSucursalListaCliente.setText(clienteDTO.getNombre_sucursal());
        holder.imageButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(activity, VistaClienteActivity.class);
                i.putExtra("idCliente",clienteDTO.getId());
                i.putExtra("titulo", clienteDTO.getNombre_completo() + (clienteDTO.getCant_sucursales() >1 ? " (" + clienteDTO.getNombre_sucursal() +")" : "") );
                i.putExtra("coordenadas", clienteDTO.getCoordenadas());
                i.putExtra("telefonos", clienteDTO.getTelefonos());
                i.putExtra("direccion", clienteDTO.getDireccion_fisica());
                i.putExtra("photo_url", clienteDTO.getPhoto_url());
                i.putExtra("horario_atencion", clienteDTO.getHorario_atencion());
                i.putExtra("emails", clienteDTO.getEmails());
                i.putExtra("sitio_web", clienteDTO.getSitio_web());
                i.putExtra("cant_sucursales", clienteDTO.getCant_sucursales());
                activity.startActivity(i);
            }
        });
        holder.imageButtonLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent i = new Intent(activity, MapsSucursalesActivity.class);
                //i.putExtra("idCliente",clienteDTO.getId());
                //i.putExtra("sucursal", clienteDTO.getNombre_sucursal());
                //i.putExtra("photo_url", clienteDTO.getPhoto_url());
                //activity.startActivity(i);
                //Uri gmmIntentUri = Uri.parse("geo:" + clienteDTO.getCoordenadas().split("\\|")[0].toString() + "," +clienteDTO.getCoordenadas().split("\\|")[1].toString());
                //Uri gmmIntentUri = Uri.parse("geo:0,0?q="+ clienteDTO.getCoordenadas().split("\\|")[0].toString()
                //        + "," +clienteDTO.getCoordenadas().split("\\|")[1].toString()+"("+clienteDTO.getNombre_corto()+"+"+ clienteDTO.getNombre_sucursal() + ")");
                //Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                //mapIntent.setPackage("com.google.android.apps.maps");
                //activity.startActivity(mapIntent);
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+ clienteDTO.getCoordenadas().split("\\|")[0].toString()
                              + "," +clienteDTO.getCoordenadas().split("\\|")[1].toString());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                activity.startActivity(mapIntent);
            }
        });
        Picasso.with(activity)
                .load(clienteDTO.getPhoto_url())
                .into(holder.imageViewListaClientes);
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class MyViewHoldder extends RecyclerView.ViewHolder{
        public TextView txtDescripcioListaCliente, txtAbiertoCerrado,txtKilometrosDist, txtDuracionDist, txtNombreSucursalListaCliente;
        public ImageButton imageButtonHome;
        public ImageButton imageButtonLocation;
        public ImageView imageViewListaClientes;

        public MyViewHoldder(View itemView) {
            super(itemView);
            txtDescripcioListaCliente = (TextView) itemView.findViewById(R.id.txtDescripcioListaCliente);
            txtAbiertoCerrado = (TextView) itemView.findViewById(R.id.txtAbiertoCerrado);
            txtKilometrosDist = (TextView) itemView.findViewById(R.id.txtKilometrosDist);
            txtDuracionDist = (TextView) itemView.findViewById(R.id.txtDuracionDist);
            txtNombreSucursalListaCliente = (TextView) itemView.findViewById(R.id.txtNombreSucursalListaCliente);
            imageButtonHome = (ImageButton) itemView.findViewById(R.id.imageButtonHome);
            imageButtonLocation = (ImageButton) itemView.findViewById(R.id.imageButtonLocation);
            imageViewListaClientes = (ImageView) itemView.findViewById(R.id.imageViewListaClientes);
        }
    }


    public void updateUI(final String distancia, final String duracion){
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                holder.txtKilometrosDist.setText("A " + distancia + " de ti");
                holder.txtDuracionDist.setText(duracion);
            }
        });

    }
    private class ObtenerLocation extends AsyncTask<Void, Void, Integer>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
           holder.txtDuracionDist.setText(duracion);
           holder.txtKilometrosDist.setText(distancia);
        }

        @Override
        protected Integer doInBackground(Void... params) {
            Double latitud = null;
            Double longitud = null;
            lastLocationl = LocationService.mLastLocation;
            if (lastLocationl == null){
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
            String url = activity.getString(R.string.url_json_google_maps)  + "origins=" + latitud.toString() +","+ longitud.toString() + "&destinations="
                    + lat.toString() +"," + lon.toString() + "&key=" + activity.getString(R.string.key);
            HttpRequest request2 = HttpRequest.get(url );
            if (request2.code() == 200){
                try {
                    JSONObject body2 = new JSONObject(request2.body());
                        JSONArray array2 = body2.getJSONArray("rows");
                        for (int i = 0;i<array2.length(); i++){
                            JSONObject dist = array2.getJSONObject(i);
                            JSONArray dis2 = dist.getJSONArray("elements");
                            for (int j = 0;j<dis2.length(); j++){
                                JSONObject object = dis2.getJSONObject(j);
                                JSONObject dur = object.getJSONObject("duration");
                                duracion = dur.getString("text");
                                JSONObject di = object.getJSONObject("distance");
                                distancia = di.getString("text");
                            }

                        }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}
