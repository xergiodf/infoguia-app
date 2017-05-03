package py.minicubic.info_guia_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.os.AsyncTask;
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

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import py.minicubic.info_guia_app.adapters.ListaClienteRecyclerAdapter;
import py.minicubic.info_guia_app.dto.ClienteDTO;
import py.minicubic.info_guia_app.event.ObtenerDistanciaEvent;
import py.minicubic.info_guia_app.rest.HttpRequest;
import py.minicubic.info_guia_app.service.LocationService;
import py.minicubic.info_guia_app.util.MedirDistanciaDirecciones;
import py.minicubic.info_guia_app.util.SimpleDividerItemDecoration;

public class ListaClientesActivity extends AppCompatActivity {

    private List<ClienteDTO> lista = new ArrayList<>();
    private RecyclerView recyclerView;
    private ListaClienteRecyclerAdapter adapter;
    private ProgressDialog progressDialog;
    private ImageButton imageButtonHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_clientes);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setMessage("Cargando clientes...");
        progressDialog.setTitle("Infoguia");
        progressDialog.show();
        lista = (ArrayList<ClienteDTO>) getIntent().getSerializableExtra("listaClientes");
        recyclerView = (RecyclerView) findViewById(R.id.listClientesRV);
        adapter = new ListaClienteRecyclerAdapter(this,lista);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        progressDialog.dismiss();

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
