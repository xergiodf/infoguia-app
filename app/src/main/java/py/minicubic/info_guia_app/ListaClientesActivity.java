package py.minicubic.info_guia_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

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

import py.minicubic.info_guia_app.adapters.ListaClienteRecyclerAdapter;
import py.minicubic.info_guia_app.dto.ClienteDTO;
import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.dto.Response;
import py.minicubic.info_guia_app.event.ClienteCategoriasEvent;
import py.minicubic.info_guia_app.event.EventPublish;
import py.minicubic.info_guia_app.event.ObtenerDistanciaEvent;
import py.minicubic.info_guia_app.rest.HttpRequest;
import py.minicubic.info_guia_app.service.LocationService;
import py.minicubic.info_guia_app.util.CacheData;
import py.minicubic.info_guia_app.util.MedirDistanciaDirecciones;
import py.minicubic.info_guia_app.util.SimpleDividerItemDecoration;

public class ListaClientesActivity extends AppCompatActivity {

    private List<ClienteDTO> lista = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListaClienteRecyclerAdapter adapter;
    private ImageButton imageButtonHome;
    MedirDistanciaDirecciones medirDistanciaDirecciones = MedirDistanciaDirecciones.getInstance();
    private Gson gson = new GsonBuilder().create();
    private ProgressDialog progressDialog;
    private Handler h;
    private boolean checkResponse;
    private CacheData cacheData = CacheData.getInstance();
    FloatingActionButton buttonMaps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        String parametro = getIntent().getStringExtra("parametro");
        recyclerView = (RecyclerView) findViewById(R.id.listClientesRV);
        buttonMaps = (FloatingActionButton) findViewById(R.id.fab);
        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ListaClientesActivity.this, MapsSucursalesActivity.class);
                i.putExtra("idCliente",new Long("0"));
                i.putExtra("sucursal", "0");
                startActivity(i);
            }
        });
        if (parametro != null){
            cargarLista(parametro);
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Cargando clientes...");
            progressDialog.setTitle("Infoguia");
            progressDialog.show();
        }else {
            lista = (ArrayList<ClienteDTO>) getIntent().getSerializableExtra("listaClientes");
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMessage("Cargando clientes...");
            progressDialog.setTitle("Infoguia");
            progressDialog.show();

            adapter = new ListaClienteRecyclerAdapter(this,lista);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            progressDialog.dismiss();
        }

    }

    private void cargarLista(String parametro){
            Request<ClienteDTO> request = new Request<>();
            ClienteDTO clientesDto = new ClienteDTO();
            clientesDto.setCoordenadas( medirDistanciaDirecciones.getLatitud().toString()+ "|" + medirDistanciaDirecciones.getLongitud().toString());
            clientesDto.setNombre_corto(parametro);
            request.setData(clientesDto);
            request.setType("/api/request/android/ClienteCategorias/ClienteService/getClientesPorNombre/"+ cacheData.getImei());
            EventBus.getDefault().post(new EventPublish(request));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClienteCategorias(ClienteCategoriasEvent event){
        Type listType = new TypeToken<Response<List<ClienteDTO>>>(){}.getType();
        Response<List<ClienteDTO>> response = gson.fromJson(event.getMessage(), listType);
        if (response.getCodigo() == 200){
            if (!response.getData().isEmpty()){
                progressDialog.dismiss();
                checkResponse = true;
                lista = (List<ClienteDTO>) response.getData();
                adapter = new ListaClienteRecyclerAdapter(this,lista);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(adapter);
            }else {
                Toast.makeText(this, "No se pudo cargar los clientes", Toast.LENGTH_SHORT).show();
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
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void obtenerDistanciaEvent(ObtenerDistanciaEvent event){
     adapter.updateUI(event.getDistancia(), event.getDuracion());
    }
}
