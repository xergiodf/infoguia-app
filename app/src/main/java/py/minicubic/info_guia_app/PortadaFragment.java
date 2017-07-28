package py.minicubic.info_guia_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PointF;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.SimpleCircleButton;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Util;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import py.minicubic.info_guia_app.adapters.CategoriasAdapter;
import py.minicubic.info_guia_app.adapters.TransformerAdapter;
import py.minicubic.info_guia_app.dto.CategoriaDTO;
import py.minicubic.info_guia_app.dto.ClienteDTO;
import py.minicubic.info_guia_app.dto.PublicacionClienteDTO;
import py.minicubic.info_guia_app.dto.Request;
import py.minicubic.info_guia_app.dto.Response;
import py.minicubic.info_guia_app.event.BuscarClienteEvent;
import py.minicubic.info_guia_app.event.CargarPublicacionPrincipalEvent;
import py.minicubic.info_guia_app.event.CargarPublicacionSecundariaEvent;
import py.minicubic.info_guia_app.event.CategoriaEvent;
import py.minicubic.info_guia_app.event.EventPublish;
import py.minicubic.info_guia_app.event.SubscriberAlready;
import py.minicubic.info_guia_app.rest.HttpRequest;
import py.minicubic.info_guia_app.util.CacheData;
import py.minicubic.info_guia_app.util.MedirDistanciaDirecciones;


public class PortadaFragment extends Fragment implements
        BaseSliderView.OnSliderClickListener,
        ViewPagerEx.OnPageChangeListener {

    private SliderLayout mDemoSlider;
    //private SliderLayout mDemoSlider2;
    private ProgressDialog progressDialog;
    private Handler h;
    private Gson gson = new GsonBuilder().create();
    private boolean checkResponse;
    private boolean online;
    Button btnInformacionesPortada, btnNegociosPortada, btnOcioPortada, btnTurismoPortada, btnServiciosPortada;
    private ImageView imageViewOfertaItau, imageViewOfertaPromoItau, imageViewOfertaDominos, imageViewFavoritoGajas, imageViewFavoritoAuricular, imageViewRecomendadoNotebook,
                    imageViewOfertaBurguer, imageViewOfertaBolson, imageViewOfertaZapato, imageViewFavoritoMochila, imageViewRecomendadoTenis, imageViewRecomendadoHeineken;
    TextView txtMarcaDestacada1, txtMarcaDestacada2, txtMarcaDestacada3,txtMarcaDestacada4;
    private CacheData cacheData = CacheData.getInstance();
    private List<CategoriaDTO> listaCategoriasInformacion = new ArrayList<>();
    private List<CategoriaDTO> listaCategoriasNegocios = new ArrayList<>();
    private List<CategoriaDTO> listaCategoriasOcio = new ArrayList<>();
    private List<CategoriaDTO> listaCategoriasTurismo = new ArrayList<>();
    private List<CategoriaDTO> listaCategoriasServicios = new ArrayList<>();

     BoomMenuButton boomMenuButtonInfo;
    BoomMenuButton boomMenuButtonServicios;
    BoomMenuButton boomMenuButtonNegocios;
    BoomMenuButton boomMenuButtonOcio;
    BoomMenuButton boomMenuButtonTurismo;


    public PortadaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PortadaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static PortadaFragment newInstance(String param1, String param2) {
        PortadaFragment fragment = new PortadaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        View view = inflater.inflate(R.layout.fragment_portada, container, false);

        mDemoSlider =(SliderLayout) view.findViewById(R.id.slider);
        txtMarcaDestacada1 = (TextView) view.findViewById(R.id.txtMarcaDestacada1);
        txtMarcaDestacada2 = (TextView) view.findViewById(R.id.txtMarcaDestacada2);
        txtMarcaDestacada3 = (TextView) view.findViewById(R.id.txtMarcaDestacada3);
        txtMarcaDestacada4 = (TextView) view.findViewById(R.id.txtMarcaDestacada4);
        imageViewOfertaItau = (ImageView) view.findViewById(R.id.imageViewOfertaItau);
        imageViewOfertaItau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.infoguia.com.py"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        imageViewOfertaPromoItau = (ImageView) view.findViewById(R.id.imageViewOfertasPromoItau);
        imageViewOfertaPromoItau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.infoguia.com.py"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        imageViewOfertaDominos = (ImageView) view.findViewById(R.id.imageViewOfertasDominos);
        imageViewOfertaDominos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.infoguia.com.py"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        imageViewOfertaBurguer = (ImageView) view.findViewById(R.id.imageViewOfertasBurguer);
        imageViewOfertaBurguer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://www.infoguia.com.py"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        cargarPublicaciones();
        cargarPublicacionesSecundarias();
        boomMenuButtonInfo = (BoomMenuButton) view.findViewById(R.id.bmbInfo);
        boomMenuButtonServicios = (BoomMenuButton) view.findViewById(R.id.bmbServicios);
        boomMenuButtonOcio = (BoomMenuButton) view.findViewById(R.id.bmbOcio);
        boomMenuButtonNegocios = (BoomMenuButton) view.findViewById(R.id.bmbNegocios);
        boomMenuButtonTurismo = (BoomMenuButton) view.findViewById(R.id.bmbTurismo);
        boomMenuButtonInfo.setInFragment(true);
        boomMenuButtonNegocios.setInFragment(true);
        boomMenuButtonServicios.setInFragment(true);
        boomMenuButtonOcio.setInFragment(true);
        boomMenuButtonTurismo.setInFragment(true);

        return view;
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
        int distancia = 90;
        int fila1 = -120;
        int fila2 = -120;
        int fila3 = -120;
        int fila4 = -120;
        int primeraLinea = -120;
        Type listType = new TypeToken<Response<List<CategoriaDTO>>>(){}.getType();
        Response<List<CategoriaDTO>> response = gson.fromJson(event.getMessage(), listType);
        if (response.getCodigo() == 200){
            if (response.getData().get(0).getCategoria().equalsIgnoreCase("servicios")){
                listaCategoriasServicios = response.getData();
                for (int i = 0; i < listaCategoriasServicios.size(); i++) {
                    if (i<4){
                        if (i != 0)
                            fila1 += distancia;
                        boomMenuButtonServicios.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonServicios.getCustomButtonPlacePositions().add(new PointF((i == 0 ? Util.dp2px(-120) : Util.dp2px(fila1) ), Util.dp2px(-180)));
                    }
                    if ((i>=4 && i <=7)){
                        if (i != 4)
                            fila2 +=  distancia;
                        boomMenuButtonServicios.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonServicios.getCustomButtonPlacePositions().add(new PointF((i == 4 ? Util.dp2px(-120) : Util.dp2px(fila2) ), Util.dp2px(-90)));
                    }

                    if ((i>=8 && i <=11)){
                        if (i != 8)
                            fila3 += distancia;
                        boomMenuButtonServicios.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonServicios.getCustomButtonPlacePositions().add(new PointF((i == 8 ? Util.dp2px(-120) : Util.dp2px(fila3) ), Util.dp2px(0)));
                    }

                    if ((i>=12 && i <=15)){
                        if (i != 12)
                            fila4 += distancia;
                        boomMenuButtonServicios.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonServicios.getCustomButtonPlacePositions().add(new PointF((i == 12 ? Util.dp2px(-120) : Util.dp2px(fila4) ), Util.dp2px(90)));
                    }

                    TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                            .normalImageRes(R.drawable.ic_action_perm_identity)
                            .normalText(listaCategoriasServicios.get(i).getDescripcion())
                            .listener(new OnBMClickListener() {
                                @Override
                                public void onBoomButtonClick(int index) {
                                    CategoriaDTO categoriaDTO = (CategoriaDTO) listaCategoriasServicios.get(index);
                                    Intent i = new Intent(getActivity(), ListaClientesActivity.class);
                                    i.putExtra("parametro", categoriaDTO.getDescripcion());
                                    startActivity(i);
                                }
                            });
                    boomMenuButtonServicios.addBuilder(builder);
                }
            }else if (response.getData().get(0).getCategoria().contains("Turismo")){
                listaCategoriasTurismo = response.getData();
                for (int i = 0; i < listaCategoriasTurismo.size(); i++) {
                    if (i<4){
                        if (i != 0)
                            fila1 += distancia;
                        boomMenuButtonTurismo.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonTurismo.getCustomButtonPlacePositions().add(new PointF((i == 0 ? Util.dp2px(-120) : Util.dp2px(fila1) ), Util.dp2px(-180)));

                        boomMenuButtonOcio.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonOcio.getCustomButtonPlacePositions().add(new PointF((i == 0 ? Util.dp2px(-120) : Util.dp2px(fila1) ), Util.dp2px(-180)));
                    }
                    if ((i>=4 && i <=7)){
                        if (i != 4)
                            fila2 +=  distancia;
                        boomMenuButtonTurismo.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonTurismo.getCustomButtonPlacePositions().add(new PointF((i == 4 ? Util.dp2px(-120) : Util.dp2px(fila2) ), Util.dp2px(-90)));

                        boomMenuButtonOcio.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonOcio.getCustomButtonPlacePositions().add(new PointF((i == 4 ? Util.dp2px(-120) : Util.dp2px(fila2) ), Util.dp2px(-90)));
                    }

                    if ((i>=8 && i <=11)){
                        if (i != 8)
                            fila3 += distancia;
                        boomMenuButtonTurismo.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonTurismo.getCustomButtonPlacePositions().add(new PointF((i == 8 ? Util.dp2px(-120) : Util.dp2px(fila3) ), Util.dp2px(0)));

                        boomMenuButtonOcio.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonOcio.getCustomButtonPlacePositions().add(new PointF((i == 8 ? Util.dp2px(-120) : Util.dp2px(fila3) ), Util.dp2px(0)));
                    }

                    if ((i>=12 && i <=15)){
                        if (i != 12)
                            fila4 += distancia;
                        boomMenuButtonTurismo.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonTurismo.getCustomButtonPlacePositions().add(new PointF((i == 12 ? Util.dp2px(-120) : Util.dp2px(fila4) ), Util.dp2px(90)));

                        boomMenuButtonOcio.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonOcio.getCustomButtonPlacePositions().add(new PointF((i == 12 ? Util.dp2px(-120) : Util.dp2px(fila4) ), Util.dp2px(90)));
                    }

                    TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                            .normalImageRes(R.drawable.ic_action_perm_identity)
                            .normalText(listaCategoriasTurismo.get(i).getDescripcion())
                            .listener(new OnBMClickListener() {
                                @Override
                                public void onBoomButtonClick(int index) {
                                    CategoriaDTO categoriaDTO = (CategoriaDTO) listaCategoriasTurismo.get(index);
                                    Intent i = new Intent(getActivity(), ListaClientesActivity.class);
                                    i.putExtra("parametro", categoriaDTO.getDescripcion());
                                    startActivity(i);
                                }
                            });
                    boomMenuButtonTurismo.addBuilder(builder);
                    boomMenuButtonOcio.addBuilder(builder);
                }
            }else if (response.getData().get(0).getCategoria().contains("Ocio")){
                listaCategoriasOcio = response.getData();
                for (int i = 0; i < listaCategoriasOcio.size(); i++) {
                    if (i<4){
                        if (i != 0)
                            fila1 += distancia;
                        boomMenuButtonOcio.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonOcio.getCustomButtonPlacePositions().add(new PointF((i == 0 ? Util.dp2px(-120) : Util.dp2px(fila1) ), Util.dp2px(-180)));
                    }
                    if ((i>=4 && i <=7)){
                        if (i != 4)
                            fila2 +=  distancia;
                        boomMenuButtonOcio.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonOcio.getCustomButtonPlacePositions().add(new PointF((i == 4 ? Util.dp2px(-120) : Util.dp2px(fila2) ), Util.dp2px(-90)));
                    }

                    if ((i>=8 && i <=11)){
                        if (i != 8)
                            fila3 += distancia;
                        boomMenuButtonOcio.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonOcio.getCustomButtonPlacePositions().add(new PointF((i == 8 ? Util.dp2px(-120) : Util.dp2px(fila3) ), Util.dp2px(0)));
                    }

                    if ((i>=12 && i <=15)){
                        if (i != 12)
                            fila4 += distancia;
                        boomMenuButtonOcio.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonOcio.getCustomButtonPlacePositions().add(new PointF((i == 12 ? Util.dp2px(-120) : Util.dp2px(fila4) ), Util.dp2px(90)));
                    }

                    TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                            .normalImageRes(R.drawable.ic_action_perm_identity)
                            .normalText(listaCategoriasOcio.get(i).getDescripcion())
                            .listener(new OnBMClickListener() {
                                @Override
                                public void onBoomButtonClick(int index) {
                                    CategoriaDTO categoriaDTO = (CategoriaDTO) listaCategoriasOcio.get(index);
                                    Intent i = new Intent(getActivity(), ListaClientesActivity.class);
                                    i.putExtra("parametro", categoriaDTO.getDescripcion());
                                    startActivity(i);
                                }
                            });
                    boomMenuButtonOcio.addBuilder(builder);
                }
            }else if (response.getData().get(0).getCategoria().equalsIgnoreCase("negocios")){
                listaCategoriasNegocios = response.getData();
                for (int i = 0; i < listaCategoriasNegocios.size(); i++) {
                    if (i<4){
                        if (i != 0)
                            fila1 += distancia;
                        boomMenuButtonNegocios.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonNegocios.getCustomButtonPlacePositions().add(new PointF((i == 0 ? Util.dp2px(-120) : Util.dp2px(fila1) ), Util.dp2px(-180)));
                    }
                    if ((i>=4 && i <=7)){
                        if (i != 4)
                            fila2 +=  distancia;
                        boomMenuButtonNegocios.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonNegocios.getCustomButtonPlacePositions().add(new PointF((i == 4 ? Util.dp2px(-120) : Util.dp2px(fila2) ), Util.dp2px(-90)));
                    }

                    if ((i>=8 && i <=11)){
                        if (i != 8)
                            fila3 += distancia;
                        boomMenuButtonNegocios.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonNegocios.getCustomButtonPlacePositions().add(new PointF((i == 8 ? Util.dp2px(-120) : Util.dp2px(fila3) ), Util.dp2px(0)));
                    }

                    if ((i>=12 && i <=15)){
                        if (i != 12)
                            fila4 += distancia;
                        boomMenuButtonNegocios.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                        boomMenuButtonNegocios.getCustomButtonPlacePositions().add(new PointF((i == 12 ? Util.dp2px(-120) : Util.dp2px(fila4) ), Util.dp2px(90)));
                    }

                    TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                            .normalImageRes(R.drawable.ic_action_perm_identity)
                            .normalText(listaCategoriasNegocios.get(i).getDescripcion())
                            .listener(new OnBMClickListener() {
                                @Override
                                public void onBoomButtonClick(int index) {
                                    CategoriaDTO categoriaDTO = (CategoriaDTO) listaCategoriasNegocios.get(index);
                                    Intent i = new Intent(getActivity(), ListaClientesActivity.class);
                                    i.putExtra("parametro", categoriaDTO.getDescripcion());
                                    startActivity(i);
                                }
                            });
                    boomMenuButtonNegocios.addBuilder(builder);
                }
            }else if (response.getData().get(0).getCategoria().equalsIgnoreCase("informaciones")){
                listaCategoriasInformacion = response.getData();
                if (!listaCategoriasInformacion.isEmpty()){
                    for (int i = 0; i < listaCategoriasInformacion.size(); i++) {
                        if (i<4){
                            if (i != 0)
                                fila1 += distancia;
                            boomMenuButtonInfo.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                            boomMenuButtonInfo.getCustomButtonPlacePositions().add(new PointF((i == 0 ? Util.dp2px(-120) : Util.dp2px(fila1) ), Util.dp2px(-180)));
                        }
                        if ((i>=4 && i <=7)){
                            if (i != 4)
                            fila2 +=  distancia;
                            boomMenuButtonInfo.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                            boomMenuButtonInfo.getCustomButtonPlacePositions().add(new PointF((i == 4 ? Util.dp2px(-120) : Util.dp2px(fila2) ), Util.dp2px(-90)));
                        }

                        if ((i>=8 && i <=11)){
                            if (i != 8)
                                fila3 += distancia;
                            boomMenuButtonInfo.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                            boomMenuButtonInfo.getCustomButtonPlacePositions().add(new PointF((i == 8 ? Util.dp2px(-120) : Util.dp2px(fila3) ), Util.dp2px(0)));
                        }

                        if ((i>=12 && i <=15)){
                            if (i != 12)
                                fila4 += distancia;
                            boomMenuButtonInfo.getCustomPiecePlacePositions().add(new PointF(Util.dp2px(+12), Util.dp2px(-12)));
                            boomMenuButtonInfo.getCustomButtonPlacePositions().add(new PointF((i == 12 ? Util.dp2px(-120) : Util.dp2px(fila4) ), Util.dp2px(90)));
                        }

                        TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                                .normalImageRes(R.drawable.ic_action_perm_identity)
                                .normalText(listaCategoriasInformacion.get(i).getDescripcion())
                                .listener(new OnBMClickListener() {
                                    @Override
                                    public void onBoomButtonClick(int index) {
                                        CategoriaDTO categoriaDTO = (CategoriaDTO) listaCategoriasInformacion.get(index);
                                        Intent i = new Intent(getActivity(), ListaClientesActivity.class);
                                        i.putExtra("parametro", categoriaDTO.getDescripcion());
                                        startActivity(i);
                                    }
                                });
                        boomMenuButtonInfo.addBuilder(builder);
                    }
                }
            }
        }else {
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Error al traer Categorias", Toast.LENGTH_SHORT).show();
            return;
        }

    }
    private void cargarPublicaciones(){
        Request<PublicacionClienteDTO> request = new Request<>();
        PublicacionClienteDTO publicacionClienteDTO = new PublicacionClienteDTO();
        publicacionClienteDTO.setTipo_publicaciones_id(3L);
        request.setData(publicacionClienteDTO);
        request.setType("/api/request/android/Publicaciones/ClienteService/getPublicacion/"+ cacheData.getImei());
        EventBus.getDefault().post(new EventPublish(request));
    }

    private void cargarPublicacionesSecundarias(){
        Request<PublicacionClienteDTO> request = new Request<>();
        PublicacionClienteDTO publicacionClienteDTO = new PublicacionClienteDTO();
        publicacionClienteDTO.setTipo_publicaciones_id(4L);
        request.setData(publicacionClienteDTO);
        request.setType("/api/request/android/PublicacionesSecundarias/ClienteService/getPublicacion/"+ cacheData.getImei());
        EventBus.getDefault().post(new EventPublish(request));
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onSubscrbierAlready(SubscriberAlready subscriberAlready){
        cargarPublicaciones();
        cargarPublicacionesSecundarias();
        cargarCategorias("informaciones");
        cargarCategorias("negocios");
        cargarCategorias("ocio");
        cargarCategorias("turismo");
        cargarCategorias("servicios");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onCargarPuclicaciones(CargarPublicacionPrincipalEvent event){
        Type listType = new TypeToken<Response<List<PublicacionClienteDTO>>>(){}.getType();
        Response<List<PublicacionClienteDTO>> response = gson.fromJson(event.getMessage(), listType);
        HashMap<String,String> url_maps = new HashMap<String, String>();
        if (response.getCodigo() == 200){
            for (PublicacionClienteDTO publicacionClienteDTO:  response.getData()){
                url_maps.put(publicacionClienteDTO.getTitulo(), publicacionClienteDTO.getUrl());
            }

            //url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
            //url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
            //url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
            //url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

            HashMap<String,Integer> file_maps = new HashMap<String, Integer>();

            for(String name : url_maps.keySet()){
                TextSliderView textSliderView = new TextSliderView(getActivity());
                // initialize a SliderLayout
                textSliderView
                        .description(name)
                        .image(url_maps.get(name))
                        .setScaleType(BaseSliderView.ScaleType.Fit)
                        .setOnSliderClickListener(this);

                //add your extra information
                textSliderView.bundle(new Bundle());
                textSliderView.getBundle()
                        .putString("extra",name);

                mDemoSlider.addSlider(textSliderView);
            }
            mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
            mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
            mDemoSlider.setCustomAnimation(new DescriptionAnimation());
            mDemoSlider.setDuration(4000);
            mDemoSlider.addOnPageChangeListener(this);
            mDemoSlider.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("http://www.infoguia.com.py"); // missing 'http://' will cause crashed
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

        }else {

        }
    }

    @Subscribe
    public void onCargarPublicacionSecundariaEvent(CargarPublicacionSecundariaEvent event){
        Type listType = new TypeToken<Response<List<PublicacionClienteDTO>>>(){}.getType();
        Response<List<PublicacionClienteDTO>> response = gson.fromJson(event.getMessage(), listType);
        HashMap<String,String> url_maps = new HashMap<String, String>();
        if (response.getCodigo() == 200){

            txtMarcaDestacada1.setText(response.getData().get(0).getTitulo());
            txtMarcaDestacada2.setText(response.getData().get(1).getTitulo());
            txtMarcaDestacada3.setText(response.getData().get(2).getTitulo());
            txtMarcaDestacada4.setText(response.getData().get(3).getTitulo());

            Picasso.with(getActivity())
                    .load(response.getData().get(0).getUrl())
                    .into(imageViewOfertaItau);

            Picasso.with(getActivity())
                    .load(response.getData().get(1).getUrl())
                    .into(imageViewOfertaPromoItau);
            Picasso.with(getActivity())
                    .load(response.getData().get(2).getUrl())
                    .into(imageViewOfertaDominos);
            Picasso.with(getActivity())
                    .load(response.getData().get(3).getUrl())
                    .into(imageViewOfertaBurguer);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBuscarClienteEvent(BuscarClienteEvent evet){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(R.string.progres_bar_title);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Obteniendo clientes...");
        progressDialog.show();
        h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkResponse();
            }
        }, 6000);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void checkResponse() {
        if (!checkResponse){
            progressDialog.dismiss();
            Toast.makeText(getContext(), "No se tuvieron resultados en la busqueda", Toast.LENGTH_SHORT).show();
        }
    }



}
