package py.minicubic.info_guia_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
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
import py.minicubic.info_guia_app.adapters.NovedadesAdapter;
import py.minicubic.info_guia_app.dto.NovedadesDTO;
import py.minicubic.info_guia_app.dto.PublicacionClienteDTO;
import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.dto.Response;
import py.minicubic.info_guia_app.event.ClienteServiceNovedadesEvent;
import py.minicubic.info_guia_app.event.EventPublish;
import py.minicubic.info_guia_app.util.CacheData;


public class NovedadesFragment extends Fragment {

    private ListView listView;
    private List<PublicacionClienteDTO> novedadesList;
    private NovedadesAdapter adapter;
    private Gson gson = new GsonBuilder().create();
    private ProgressDialog progressDialog;
    private Handler h;
    private boolean checkResponse;
    private Context context;
    private CacheData cacheData = CacheData.getInstance();

    public NovedadesFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static NovedadesFragment newInstance(String param1, String param2) {
        NovedadesFragment fragment = new NovedadesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void cargarNovedades(){
        Request<PublicacionClienteDTO> request = new Request<>();
        PublicacionClienteDTO publicacionClienteDTO = new PublicacionClienteDTO();
        publicacionClienteDTO.setTipo_publicaciones_id(2L);
        request.setData(publicacionClienteDTO);
        request.setType(getString(R.string.request_novedades)+ cacheData.getImei());
        EventBus.getDefault().post(new EventPublish(request));
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClienteServiceEvent(ClienteServiceNovedadesEvent event){
        Type listType = new TypeToken<Response<List<PublicacionClienteDTO>>>(){}.getType();
        Response<List<PublicacionClienteDTO>> response = gson.fromJson(event.getMessage(), listType);
        if (response.getCodigo() == 200){
            checkResponse = true;
            progressDialog.dismiss();
            novedadesList = response.getData();
            adapter = new NovedadesAdapter(getActivity(), novedadesList);
            listView.setAdapter(adapter);
            //Insertamos los registros traidos en la base de datos local...
            //new InsertarGestionHojaRutas().execute();
        }else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error al traer Novedades", Toast.LENGTH_SHORT).show();
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
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_novedades, container, false);
        context = getActivity();
        listView = (ListView) rootView.findViewById(R.id.listViewNovedades);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Infoguia app");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Obteniendo novedades...");
        progressDialog.show();
        cargarNovedades();
        h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkResponse();
            }
        }, 6000);
        return rootView;
    }

    private void checkResponse() {
        if (!checkResponse){
            progressDialog.dismiss();
            Toast.makeText(context, "No se pudo traer las hojas de rutas", Toast.LENGTH_SHORT).show();
        }
    }

}
