package py.minicubic.info_guia_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import py.minicubic.info_guia_app.adapters.ListaClienteRecyclerAdapter;
import py.minicubic.info_guia_app.dto.ClienteDTO;
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
    private void cargarClientes(){

    }


}
