package py.minicubic.info_guia_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import py.minicubic.info_guia_app.adapters.CategoriasAdapter;
import py.minicubic.info_guia_app.adapters.NovedadesAdapter;
import py.minicubic.info_guia_app.dto.CategoriaDTO;
import py.minicubic.info_guia_app.dto.PublicacionClienteDTO;
import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.dto.Response;
import py.minicubic.info_guia_app.event.CategoriaEvent;
import py.minicubic.info_guia_app.event.EventPublish;
import py.minicubic.info_guia_app.expandable_adapter.ExpandableListAdapter;
import py.minicubic.info_guia_app.util.CacheData;

public class CategoriasActivity extends AppCompatActivity {

    CategoriasAdapter adapter;
    ListView listView;
    List<CategoriaDTO> listCategorias;
    private ProgressDialog progressDialog;
    private Handler h;
    private Gson gson = new GsonBuilder().create();
    private boolean checkResponse;
    private Context context;
    private CacheData cacheData = CacheData.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);
        progressDialog = new ProgressDialog(this);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Cargando categorias...");
        progressDialog.setTitle("Infoguia");
        progressDialog.show();
        listView = (ListView) findViewById(R.id.lvExp);
        String parametro = getIntent().getStringExtra("parametro");
        cargarCategorias(parametro);
        h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkResponse();
            }
        }, 6000);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter = (CategoriasAdapter) listView.getAdapter();
                CategoriaDTO categoriaDTO = (CategoriaDTO) adapter.getItem(position);
                Intent i = new Intent(CategoriasActivity.this, ListaClientesActivity.class);
                i.putExtra("parametro", categoriaDTO.getDescripcion());
                startActivity(i);
            }
        });
        // get the listview

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

    private void cargarCategorias(String parametro){
        Request<CategoriaDTO> request = new Request<>();
        CategoriaDTO categoriaDTO = new CategoriaDTO();
        categoriaDTO.setDescripcion(parametro);
        request.setData(categoriaDTO);
        request.setType("/api/request/android/Categoria/ClienteService/getCategoria/"+ cacheData.getImei());
        EventBus.getDefault().post(new EventPublish(request));
    }

    @Subscribe(threadMode=ThreadMode.MAIN)
    public void onCategoriaEvent(CategoriaEvent event){
        Type listType = new TypeToken<Response<List<CategoriaDTO>>>(){}.getType();
        Response<List<CategoriaDTO>> response = gson.fromJson(event.getMessage(), listType);
        if (response.getCodigo() == 200){

            checkResponse = true;
            progressDialog.dismiss();
            listCategorias = response.getData();
            adapter = new CategoriasAdapter(CategoriasActivity.this, listCategorias);
            listView.setAdapter(adapter);
            //Insertamos los registros traidos en la base de datos local...
            //new InsertarGestionHojaRutas().execute();
        }else {
            progressDialog.dismiss();
            Toast.makeText(this, "Error al traer Novedades", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void checkResponse() {
        if (!checkResponse){
            progressDialog.dismiss();
            Toast.makeText(this, "No se pudo traer las categorias", Toast.LENGTH_SHORT).show();
        }
    }
}
