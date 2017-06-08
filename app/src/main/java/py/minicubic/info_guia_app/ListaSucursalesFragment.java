package py.minicubic.info_guia_app;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.List;
import java.util.UUID;

import py.minicubic.info_guia_app.adapters.ListaSucursalesRecyclerAdapter;
import py.minicubic.info_guia_app.adapters.NovedadesAdapter;
import py.minicubic.info_guia_app.dto.PublicacionClienteDTO;
import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.dto.Response;
import py.minicubic.info_guia_app.dto.SucursalClientesDTO;
import py.minicubic.info_guia_app.event.ClienteServiceNovedadesEvent;
import py.minicubic.info_guia_app.event.EventPublish;
import py.minicubic.info_guia_app.event.GetSucursalesEvent;
import py.minicubic.info_guia_app.event.ObtenerDistanciaSucursalesEvent;
import py.minicubic.info_guia_app.util.CacheData;
import py.minicubic.info_guia_app.util.MedirDistanciaDirecciones;
import py.minicubic.info_guia_app.util.SimpleDividerItemDecoration;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListaSucursalesFragment extends Fragment {
    private RecyclerView recyclerView;
    private List<SucursalClientesDTO> sucursalesList;
    private ListaSucursalesRecyclerAdapter adapter;
    private Gson gson = new GsonBuilder().create();
    private ProgressDialog progressDialog;
    private Handler h;
    private boolean checkResponse;
    private Context context;
    private MedirDistanciaDirecciones medirDistanciaDirecciones = MedirDistanciaDirecciones.getInstance();
    private CacheData cacheData = CacheData.getInstance();
    FloatingActionButton buttonMaps;

    public ListaSucursalesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("InfoGuia");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Obteniendo sucursales...");
        progressDialog.show();

        Long id = bundle.getLong("idCliente");
        cargarSucursales(id);
        h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkResponse();
            }
        }, 6000);
    }

    private void cargarSucursales(Long id){
        Request<SucursalClientesDTO> request = new Request<>();
        SucursalClientesDTO sucursalClientesDTO = new SucursalClientesDTO();
        sucursalClientesDTO.setCoordenadas(medirDistanciaDirecciones.getLatitud().toString() + "|" + medirDistanciaDirecciones.getLongitud().toString());
        sucursalClientesDTO.setId_cliente(id);
        request.setData(sucursalClientesDTO);
        request.setType(getString(R.string.request_sucursales)+ cacheData.getImei());
        EventBus.getDefault().post(new EventPublish(request));
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClienteServiceEvent(GetSucursalesEvent event){
        Type listType = new TypeToken<Response<List<SucursalClientesDTO>>>(){}.getType();
        Response<List<SucursalClientesDTO>> response = gson.fromJson(event.getMessage(), listType);
        if (response.getCodigo() == 200){
            checkResponse = true;
            progressDialog.dismiss();
            sucursalesList = response.getData();
            adapter = new ListaSucursalesRecyclerAdapter(getActivity(), sucursalesList);
            recyclerView.setAdapter(adapter);
            //Insertamos los registros traidos en la base de datos local...
            //new InsertarGestionHojaRutas().execute();
        }else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error al traer sucursales", Toast.LENGTH_SHORT).show();
            return;
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_lista_sucursales, container, false);
        context = getActivity();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.listaSucursalesRecycler);
        buttonMaps = (FloatingActionButton) rootView.findViewById(R.id.fabSucursales);
        buttonMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MapsSucursalesActivity.class);
                i.putExtra("idCliente",new Long("0"));
                i.putExtra("sucursal", "0");
                startActivity(i);
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        ///recyclerView.addItemDecoration(new SimpleDividerItemDecoration(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        return rootView;
    }

    private void checkResponse() {
        if (!checkResponse){
            progressDialog.dismiss();
            Toast.makeText(context, "No se pudo traer las sucursales", Toast.LENGTH_SHORT).show();
        }
    }


}
