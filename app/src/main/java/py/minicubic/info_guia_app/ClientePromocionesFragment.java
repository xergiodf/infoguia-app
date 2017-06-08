package py.minicubic.info_guia_app;

import android.app.ProgressDialog;
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

import py.minicubic.info_guia_app.adapters.PromocionesAdapter;
import py.minicubic.info_guia_app.dto.PublicacionClienteDTO;
import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.dto.Response;
import py.minicubic.info_guia_app.event.ClientePromocionesEvent;
import py.minicubic.info_guia_app.event.EventPublish;
import py.minicubic.info_guia_app.util.CacheData;


public class ClientePromocionesFragment extends Fragment {

    private Gson gson = new GsonBuilder().create();
    private ProgressDialog progressDialog;
    private Handler h;
    private boolean checkResponse;
    private ListView listView;
    private List<PublicacionClienteDTO> publicacionClienteDTO;
    private PromocionesAdapter mAdapter;
    private UUID uuid;
    private ListView listViewPromociones;
    Long id = null ;
    CacheData cacheData = CacheData.getInstance();
    public ClientePromocionesFragment() {
        // Required empty public constructor
    }



    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    public static ClientePromocionesFragment newInstance(String param1, String param2) {
        ClientePromocionesFragment fragment = new ClientePromocionesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    private void cargarNovedades(Long idCliente){
        Request<PublicacionClienteDTO> request = new Request<>();
        PublicacionClienteDTO publicacionClienteDTO = new PublicacionClienteDTO();
        publicacionClienteDTO.setTipo_publicaciones_id(1L);
        publicacionClienteDTO.setId_cliente(idCliente);
        request.setData(publicacionClienteDTO);
        request.setType("/api/request/android/Promociones/ClienteService/getPublicacion/"+ cacheData.getImei());
        EventBus.getDefault().post(new EventPublish(request));
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onClienteServiceEvent(ClientePromocionesEvent event){
        Type listType = new TypeToken<Response<List<PublicacionClienteDTO>>>(){}.getType();
        Response<List<PublicacionClienteDTO>> response = gson.fromJson(event.getMessage(), listType);
        if (response.getCodigo() == 200){

            checkResponse = true;
            progressDialog.dismiss();
            publicacionClienteDTO = response.getData();
            // specify an adapter (see also next example)
            mAdapter = new PromocionesAdapter(getActivity(),publicacionClienteDTO);
            listViewPromociones.setAdapter(mAdapter);
            //Insertamos los registros traidos en la base de datos local...
            //new InsertarGestionHojaRutas().execute();
        }else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error al traer hojas de rutas", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null){
            id = bundle.getLong("idCliente");
        }

        cargarNovedades(id);
        h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkResponse();
            }
        }, 6000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_cliente_promociones, container, false);
        listViewPromociones = (ListView) mView.findViewById(R.id.listViewPromociones);
        if (publicacionClienteDTO != null){
            mAdapter = new PromocionesAdapter(getActivity(),publicacionClienteDTO);
            listViewPromociones.setAdapter(mAdapter);
        }else {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle(R.string.progres_bar_title);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Obteniendo promociones...");
            progressDialog.show();
        }

        return mView;
    }

    private void checkResponse() {
        if (!checkResponse){
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "No se pudo traer las promociones", Toast.LENGTH_SHORT).show();
        }
    }

}
