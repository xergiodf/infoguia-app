package py.minicubic.info_guia_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import py.minicubic.info_guia_app.adapters.ListaSucursalesRecyclerAdapter;
import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.dto.Response;
import py.minicubic.info_guia_app.dto.SucursalClientesDTO;
import py.minicubic.info_guia_app.event.EventPublish;
import py.minicubic.info_guia_app.event.GetSucursalesEvent;
import py.minicubic.info_guia_app.util.MedirDistanciaDirecciones;
import py.minicubic.info_guia_app.util.MyItem;

public class MapsSucursalesActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<SucursalClientesDTO> sucursalesList;
    private ListaSucursalesRecyclerAdapter adapter;
    private Gson gson = new GsonBuilder().create();
    private ProgressDialog progressDialog;
    private Handler h;
    private boolean checkResponse;
    private Context context;
    private ClusterManager<MyItem> clusterManager;
    private MedirDistanciaDirecciones medirDistanciaDirecciones = MedirDistanciaDirecciones.getInstance();
    String nombreSucursal = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        Long id = getIntent().getLongExtra("idCliente", 0);
         nombreSucursal = getIntent().getStringExtra("sucursal");
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(R.string.progres_bar_title);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Obteniendo sucursales...");
        progressDialog.show();
        cargarSucursales(id);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void cargarSucursales(Long id) {
        Request<SucursalClientesDTO> request = new Request<>();
        medirDistanciaDirecciones.obTenerUbicacion();
        SucursalClientesDTO sucursalClientesDTO = new SucursalClientesDTO();
        sucursalClientesDTO.setCoordenadas(medirDistanciaDirecciones.getLatitud() + "|" + medirDistanciaDirecciones.getLongitud());
        sucursalClientesDTO.setId_cliente(id);
        request.setData(sucursalClientesDTO);
        request.setType(getString(R.string.request_sucursales) + UUID.randomUUID().toString());
        EventBus.getDefault().post(new EventPublish(request));
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClienteServiceEvent(GetSucursalesEvent event) {
        Type listType = new TypeToken<Response<List<SucursalClientesDTO>>>() {
        }.getType();
        Response<List<SucursalClientesDTO>> response = gson.fromJson(event.getMessage(), listType);
        if (response.getCodigo() == 200) {

            checkResponse = true;
            progressDialog.dismiss();
            sucursalesList = response.getData();
            //Insertamos los registros traidos en la base de datos local...
            //new InsertarGestionHojaRutas().execute();
            try {
                LatLng latLng = null;
                mMap.clear();
                LatLng paraguay = new LatLng(-25.456466, -56.057578);
                int imagen = 0;
                clusterManager = new ClusterManager<MyItem>(MapsSucursalesActivity.this, mMap);
                mMap.setOnCameraChangeListener(clusterManager);
                mMap.setOnMarkerClickListener(clusterManager);
                mMap.setTrafficEnabled(true);
                mMap.getUiSettings().setZoomControlsEnabled(true);
                mMap.getUiSettings().setZoomGesturesEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                mMap.setMyLocationEnabled(true);
                for (SucursalClientesDTO sucursalClientesDTO : sucursalesList){
                    double lat = Double.valueOf(sucursalClientesDTO.getCoordenadas().split("\\|")[0]);
                    double lon = Double.valueOf(sucursalClientesDTO.getCoordenadas().split("\\|")[1]);
                    latLng = new LatLng(lat, lon);

                    MyItem myItem = new MyItem(lat,lon,
                            sucursalClientesDTO.getId()+ "-"+ sucursalClientesDTO.getNombre_sucursal(),
                            sucursalClientesDTO.getDireccion_fisica(),
                            BitmapDescriptorFactory.fromResource((nombreSucursal.equals(sucursalClientesDTO.getNombre_sucursal()) ? R.drawable.markeer : R.drawable.officebuilding)));
                    clusterManager.addItem(myItem);
                }

                clusterManager.setRenderer(new OwnIconRendered(MapsSucursalesActivity.this, mMap, clusterManager));
                // mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                CameraPosition cameraPosition;
                cameraPosition = new CameraPosition(paraguay,7,0,0);
                //.target(toledo).zoom(17).bearing(90).tilt(30).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                mMap.setInfoWindowAdapter(new SucursalWindowInfoMapAdapterActivity(getLayoutInflater(), imagen));
                //mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                   // @Override
                    //public void onInfoWindowClick(Marker marker) {
                //        Intent i = new Intent(BuscarPorProductoEmblemaActivity.this, SucursalEmblemas.class);
                //        i.putExtra("descripcion",marker.getTitle());
                //        i.putExtra("ciudad", marker.getSnippet());
                //        startActivity(i);
                //    }
                ///});
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            progressDialog.dismiss();
            Toast.makeText(this, "Error al traer sucursales", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    class OwnIconRendered extends DefaultClusterRenderer<MyItem> {

        public OwnIconRendered(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
            super(context, map, clusterManager);
        }
        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions){
            markerOptions.icon(item.getIcon());
            markerOptions.title(item.getTitle());
            markerOptions.snippet(item.getSnippet());
            super.onBeforeClusterItemRendered(item, markerOptions);
        }
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng paraguay = new LatLng(-25.456466, -56.057578);
        //googleMap.addMarker(new MarkerOptions().position(sydney).title("Paraguay"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(paraguay));
        CameraPosition cameraPosition;
        cameraPosition = new CameraPosition(paraguay,7,0,0);
        //.target(toledo).zoom(17).bearing(90).tilt(30).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //mMap.setInfoWindowAdapter(new UserInfoMapAdapter(getLayoutInflater(), img));
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

            }
        });
    }
}
