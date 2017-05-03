package py.minicubic.info_guia_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import py.minicubic.info_guia_app.adapters.ListElementAdapter;
import py.minicubic.info_guia_app.dto.ClienteDTO;
import py.minicubic.info_guia_app.dto.HorariosDTO;
import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.dto.Response;
import py.minicubic.info_guia_app.dto.SucursalClientesDTO;
import py.minicubic.info_guia_app.event.ClientePerfilEvent;
import py.minicubic.info_guia_app.event.EventPublish;


public class ClientePerfilFragment extends Fragment  implements OnMapReadyCallback{

    private GoogleMap mGoogleMap;
    private MapView mapView;
    private View mView;
    private ListView listViewTelefonos;
    private Gson gson = new GsonBuilder().create();
    private ProgressDialog progressDialog;
    private Handler h;
    private boolean checkResponse;
    private Context context;
    private List<ClienteDTO> listClientes;
    private TextView txtituloPerfilCliente;
    private TextView txtDireccion;
    private ListView listViewHorariosAtencionCliente;
    private ListView listViewTelefonoClientePerfil;
    private double lat;
    private double lon;
    private String descripcion;
    private ImageView imageViewPerfil;
    Long id;
    String titulo;
    String coordenadas;
    String direccion;
    String telefono;
    public ClientePerfilFragment() {
        // Required empty public constructor
    }

    private void cargarCliente(Long id){
        Request<ClienteDTO> request = new Request<>();
        ClienteDTO cliente = new ClienteDTO();
        cliente.setId(id);
        cliente.setNombre_corto("");
        request.setData(cliente);
        request.setType("/api/request/android/ClientePerfil/ClienteService/getClientesPorNombre/"+ UUID.randomUUID().toString());
        EventBus.getDefault().post(new EventPublish(request));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
         id  =  bundle.getLong("idCliente", 0);
        titulo = bundle.getString("titulo");
        coordenadas= bundle.getString("coordenadas");
        direccion = bundle.getString("direccion");
        telefono = bundle.getString("telefono");
        //if (listClientes == null){
        //    cargarCliente(id);
        //}
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Infoguia");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Obteniendo perfil de cliente...");
        progressDialog.show();
        h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 3000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_cliente_perfil, container, false);
        context = getActivity();
        imageViewPerfil = (ImageView) mView.findViewById(R.id.imageViewPerfil);
        listViewHorariosAtencionCliente = (ListView) mView.findViewById(R.id.listViewHorariosAtencionCliente);
        listViewTelefonoClientePerfil = (ListView) mView.findViewById(R.id.listViewTelefonoClientePerfil);
        txtituloPerfilCliente = (TextView) mView.findViewById(R.id.txtTituloPerfilCliente);
        txtDireccion = (TextView) mView.findViewById(R.id.txtDireccion);
        txtituloPerfilCliente.setText(titulo);
        List<String> listaHorarios = new ArrayList<>();
        List<String> listaTelefonos = new ArrayList<>();
        listaTelefonos.add(telefono);

            txtDireccion.setText(direccion);
            lat = Double.parseDouble(coordenadas.split("\\|")[0]);
            lon = Double.parseDouble(coordenadas.split("\\|")[1]);

        listViewHorariosAtencionCliente.setAdapter(new ListElementAdapter(listaHorarios, getActivity()));
        listViewTelefonoClientePerfil.setAdapter( new ListElementAdapter(listaTelefonos, getActivity()));
        //cargarCliente();
        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView = (MapView) mView.findViewById(R.id.mapClientePerfil);
        if(mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lon)).title(titulo).snippet(descripcion));
        CameraPosition cameraPosition = CameraPosition.builder().target(new LatLng(lat,lon)).zoom(16).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    private void traerClientes(){
        checkResponse = true;
        progressDialog.dismiss();
        txtituloPerfilCliente.setText(listClientes.get(0).getDescripcion_corta());
        List<String> listaHorarios = new ArrayList<>();
        List<String> listaTelefonos = new ArrayList<>();
        for (HorariosDTO horariosDTO: listClientes.get(0).getHorarios()){
            listaHorarios.add(horariosDTO.getDias() + ": " + horariosDTO.getHora_desde() + " a "+ horariosDTO.getHora_hasta() +" HS");
        }
        for (SucursalClientesDTO clienteSucursales : listClientes.get(0).getSucursalClientes()){
            listaTelefonos.add(clienteSucursales.getTelefono());
            listaTelefonos.add(clienteSucursales.getTelefono2());
            listaTelefonos.add(clienteSucursales.getTelefono3());
            txtDireccion.setText(clienteSucursales.getDireccion_fisica());
            lat = clienteSucursales.getLat();
            lon = clienteSucursales.getLon();
            titulo = listClientes.get(0).getDescripcion_corta();
            descripcion = clienteSucursales.getNombre_sucursal();
            //mapView.getMapAsync(this);
        }
        listViewHorariosAtencionCliente.setAdapter(new ListElementAdapter(listaHorarios, getActivity()));
        listViewTelefonoClientePerfil.setAdapter( new ListElementAdapter(listaTelefonos, getActivity()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClienteServiceEvent(ClientePerfilEvent event){
        Type listType = new TypeToken<Response<List<ClienteDTO>>>(){}.getType();
        Response<List<ClienteDTO>> response = gson.fromJson(event.getMessage(), listType);
        if (response.getCodigo() == 200){

            checkResponse = true;
            progressDialog.dismiss();
            listClientes = response.getData();
            if (listClientes.get(0).getNombre_corto().equalsIgnoreCase("itau")){
                Picasso.with(getActivity())
                        .load("http://45.79.159.123/itau.jpg")
                        .into(imageViewPerfil);
            }else if (listClientes.get(0).getNombre_corto().equalsIgnoreCase("familiar")){
                Picasso.with(getActivity())
                        .load("http://45.79.159.123/familiar.jpeg")
                        .into(imageViewPerfil);
            }
            txtituloPerfilCliente.setText(listClientes.get(0).getDescripcion_corta());
            List<String> listaHorarios = new ArrayList<>();
            List<String> listaTelefonos = new ArrayList<>();
            for (HorariosDTO horariosDTO: listClientes.get(0).getHorarios()){
                listaHorarios.add(horariosDTO.getDias() + ": " + horariosDTO.getHora_desde() + " a "+ horariosDTO.getHora_hasta() +" HS");
            }
            for (SucursalClientesDTO clienteSucursales : listClientes.get(0).getSucursalClientes()){
                listaTelefonos.add(clienteSucursales.getTelefono());
                listaTelefonos.add(clienteSucursales.getTelefono2());
                listaTelefonos.add(clienteSucursales.getTelefono3());
                txtDireccion.setText(clienteSucursales.getDireccion_fisica());
                lat = clienteSucursales.getLat();
                lon = clienteSucursales.getLon();
                titulo = listClientes.get(0).getDescripcion_corta();
                descripcion = clienteSucursales.getNombre_sucursal();
                mapView.getMapAsync(this);
            }
            listViewHorariosAtencionCliente.setAdapter(new ListElementAdapter(listaHorarios, getActivity()));
            listViewTelefonoClientePerfil.setAdapter( new ListElementAdapter(listaTelefonos, getActivity()));

            //Insertamos los registros traidos en la base de datos local...
            //new InsertarGestionHojaRutas().execute();
        }else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error al traer Cliente", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void checkResponse() {
        if (!checkResponse){
            progressDialog.dismiss();
            Toast.makeText(context, "No se pudo traer Cliente", Toast.LENGTH_SHORT).show();
        }
    }
}
