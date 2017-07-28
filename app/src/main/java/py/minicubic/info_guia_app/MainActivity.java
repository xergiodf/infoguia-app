package py.minicubic.info_guia_app;

import android.*;
import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import py.minicubic.info_guia_app.dto.ClienteDTO;
import py.minicubic.info_guia_app.dto.NovedadesDTO;
import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.dto.Response;
import py.minicubic.info_guia_app.event.BuscarClienteEvent;
import py.minicubic.info_guia_app.event.ClientePerfilEvent;
import py.minicubic.info_guia_app.event.ClienteServiceNovedadesEvent;
import py.minicubic.info_guia_app.event.EventPublish;
import py.minicubic.info_guia_app.event.ListaClientesEvent;
import py.minicubic.info_guia_app.event.SubscriberAlready;
import py.minicubic.info_guia_app.rest.HttpRequest;
import py.minicubic.info_guia_app.service.LocationService;
import py.minicubic.info_guia_app.service.Service2;
import py.minicubic.info_guia_app.service.Subscriber;
import py.minicubic.info_guia_app.util.CacheData;
import py.minicubic.info_guia_app.util.MedirDistanciaDirecciones;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 5 ;
    private FragmentTabHost tabHost;
    private Gson gson = new GsonBuilder().create();
    private ProgressDialog progressDialog;
    private Handler h;
    private boolean checkResponse;
    private Context context;
    private ArrayList<ClienteDTO> listaClientes = new ArrayList<>();
    private Boolean mCheckFindButton;
    private boolean isCheckCallListaCliente;
    MaterialSearchView searchView;
    Location lastLocationl;
    HttpRequest request = null;
    MedirDistanciaDirecciones medirDistanciaDirecciones = MedirDistanciaDirecciones.getInstance();
    private CacheData cacheData = CacheData.getInstance();
    private static final int REQUEST_LOCATION = 2;
    private GoogleApiClient mGoogleApiClient;
    public static Location mLastLocation;
    private LocationRequest mLocationRequest;
    Double lat, lon;
    private static final int WRITE_EXTERNAL_STORAGE =4  ;
    private static final int READ_PHONE_STATE = 3;
    private SharedPreferences sharedPreferences;
    boolean servicioIniciado = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ///if (AccessToken.getCurrentAccessToken()== null){
        ///    goLoginScreem();
        ///}
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        searchView = (MaterialSearchView) findViewById(R.id.search_viewMain);
        searchView.setVoiceSearch(true);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setFocusable(true);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                buscarCliente(query);
                EventBus.getDefault().post(new BuscarClienteEvent(query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);
        tabHost.addTab(tabHost.newTabSpec("portada").setIndicator("Portada"), PortadaFragment.class, null);
        //tabHost.addTab(tabHost.newTabSpec("novedades").setIndicator("Novedades"), NovedadesFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("promociones").setIndicator("Promociones"), ClientePromocionesFragment.class, null);
        //solicitarPermisosParaIniciarServicio();
        solicitarPermisoReadStatePhone();
        //solicitarPermisoStorage();
        buildGoogleApiClient();
        final LocationManager manager = (LocationManager) getApplicationContext().getSystemService( Context.LOCATION_SERVICE );
        if (!manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            buildAlertMessageNoGps();
        }
    }


    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    private void solicitarPermisoReadStatePhone(){
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_PHONE_STATE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                            }
                        });
                    } else {
                        ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE);
                    }
                }else {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String imei = telephonyManager.getDeviceId();
                    //String imei = sharedPreferences.getString("imei","imei");
                    cacheData.setImei(imei);
                    solicitarPermisoStorage();
                }
    }

    private void solicitarPermisoStorage(){
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                alertBuilder.setCancelable(true);
                alertBuilder.setMessage("Write calendar permission is necessary to write event!!!");
                alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
                    }
                });
            } else {
                ActivityCompat.requestPermissions((Activity)context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE);
            }
        }else {
            sharedPreferences = getSharedPreferences("INFOGUIA_SharedPrefFile", 0);
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            String imei = telephonyManager.getDeviceId();
            SharedPreferences.Editor editorImei = sharedPreferences.edit();
            editorImei.putString("imei", imei);
            editorImei.commit();
            cacheData.setImei(imei);
            if (cacheData.getImei() != null || !cacheData.getImei().equals("")) {
                servicioIniciado = true;
                Intent intent = new Intent(MainActivity.this, Subscriber.class);
                startService(intent);
                Log.i("Arranque", "Servicio Subscriber iniciado...");

            }
        }
    }
    /***
     * @Mandatorio
     * se deben de habilitar estos permimsos obligatoriamente
     */


    private void goLoginScreem() {
        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClienteServiceEvent(ListaClientesEvent event){
        Type listType = new TypeToken<Response<List<ClienteDTO>>>(){}.getType();
        Response<List<ClienteDTO>> response = gson.fromJson(event.getMessage(), listType);
        if (response.getCodigo() == 200){
            if (!response.getData().isEmpty()){
                checkResponse = true;
                listaClientes = (ArrayList<ClienteDTO>) response.getData();
                Intent i = new Intent(this, ListaClientesActivity.class);
                i.putExtra("listaClientes", listaClientes);
                startActivity(i);
            }else {

            }

            //Insertamos los registros traidos en la base de datos local...
            //new InsertarGestionHojaRutas().execute();
        }else {
            progressDialog.dismiss();
            Toast.makeText(this, "Error al traer Clientes", Toast.LENGTH_SHORT).show();
            return;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
       mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void buscarCliente(String nombre){
        Request<ClienteDTO> request = new Request<>();
        ClienteDTO clientesDto = new ClienteDTO();
        clientesDto.setCoordenadas( medirDistanciaDirecciones.getLatitud().toString()+ "|" + medirDistanciaDirecciones.getLongitud().toString());
        clientesDto.setNombre_corto(nombre);
        request.setData(clientesDto);
        request.setType("/api/request/android/ClienteMain/ClienteService/getClientesPorNombre/"+ cacheData.getImei());
        EventBus.getDefault().post(new EventPublish(request));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_perfil) {
            // Handle the camera action
        } else if (id == R.id.nav_lista_deseos) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_ajustes) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.cerrarSesion) {
            LoginManager.getInstance().logOut();
            goLoginScreem();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void checkResponse() {
        if (!checkResponse){
            progressDialog.dismiss();
            Toast.makeText(context, "No se encontraron clientes con esa descripcion", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_LOW_POWER);
        mLocationRequest.setInterval(100); // Update location every second
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                // Check Permissions Now
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        REQUEST_LOCATION);
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = mLastLocation.getLatitude();
            lon = mLastLocation.getLongitude();
            medirDistanciaDirecciones.setLatitud(lat);
            medirDistanciaDirecciones.setLongitud(lon);
        }else {
            if (medirDistanciaDirecciones.getLongitud() == null || medirDistanciaDirecciones.getLatitud() == null){
                UbicacionRequest request = new UbicacionRequest();
                request.execute();
            }
        }
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
                    // We can now safely use the API we requested access to
                    mLastLocation =
                            LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    if (mLastLocation != null) {
                        medirDistanciaDirecciones.setLatitud(mLastLocation.getLatitude());
                        medirDistanciaDirecciones.setLongitud(mLastLocation.getLongitude());
                    }else {
                        if (medirDistanciaDirecciones.getLongitud() == null || medirDistanciaDirecciones.getLatitud() == null){
                            UbicacionRequest request = new UbicacionRequest();
                            request.execute();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            } else {
                if (medirDistanciaDirecciones.getLongitud() == null || medirDistanciaDirecciones.getLatitud() == null){
                    UbicacionRequest request = new UbicacionRequest();
                    request.execute();
                }
            }
        }
        if (requestCode == WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sharedPreferences = getSharedPreferences("INFOGUIA_SharedPrefFile", 0);
                TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String imei = telephonyManager.getDeviceId();
                SharedPreferences.Editor editorImei = sharedPreferences.edit();
                editorImei.putString("imei", imei);
                editorImei.commit();
                cacheData.setImei(imei);
                if (cacheData.getImei() != null || !cacheData.getImei().equals("")) {
                    servicioIniciado = true;
                    Intent intent = new Intent(MainActivity.this, Subscriber.class);
                    startService(intent);
                    Log.i("Arranque", "Servicio Subscriber iniciado...");

                }
            } else {
                Toast.makeText(MainActivity.this, "Permiso denegado para leer estado", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == READ_PHONE_STATE) {
                if (grantResults.length == 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                    String imei = telephonyManager.getDeviceId();
                    cacheData.setImei(imei);
                    Toast.makeText(MainActivity.this, "Permiso condedido para leer estado del telefono", Toast.LENGTH_SHORT).show();
                    solicitarPermisoStorage();
                }else {
                    Toast.makeText(MainActivity.this, "Permiso denegado para leer estado", Toast.LENGTH_SHORT).show();
                }

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    private class UbicacionRequest extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            HttpRequest request = HttpRequest.get("http://ip-api.com/json");
            if (request.code() ==200){
                try {
                    JSONObject jsonArray =  new JSONObject(request.body());
                    lat  = (Double) jsonArray.get("lat");
                    lon = (Double) jsonArray.get("lon");
                    medirDistanciaDirecciones.setLatitud(lat);
                    medirDistanciaDirecciones.setLongitud(lon);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }
            return null;
        }
    }
}
