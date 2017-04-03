package py.minicubic.info_guia_app;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import py.minicubic.info_guia_app.adapters.TransformerAdapter;
import py.minicubic.info_guia_app.event.BuscarClienteEvent;


public class PortadaFragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{

    private SliderLayout mDemoSlider;
    private SliderLayout mDemoSlider2;
    private ProgressDialog progressDialog;
    private Handler h;
    private boolean checkResponse;
    private ImageView imageViewOfertaItau, imageViewOfertaPromoItau, imageViewOfertaDominos, imageViewFavoritoGajas, imageViewFavoritoAuricular, imageViewRecomendadoNotebook,
                    imageViewOfertaBurguer, imageViewOfertaBolson, imageViewOfertaZapato, imageViewFavoritoMochila, imageViewRecomendadoTenis, imageViewRecomendadoHeineken;

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
        if (getArguments() != null) {

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
        View view = inflater.inflate(R.layout.fragment_portada, container, false);
        mDemoSlider = (SliderLayout) view.findViewById(R.id.slider);
        mDemoSlider2 = (SliderLayout) view.findViewById(R.id.slider2);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

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


        HashMap<String,String> file_maps2 = new HashMap<String, String>();
        file_maps2.put("Itau","http://45.79.159.123/promo_itau.jpg");
        file_maps2.put("Dominos Pizza","http://45.79.159.123/domino.jpeg");
        file_maps2.put("Casa rica","http://45.79.159.123/casarica.jpeg");
        file_maps2.put("Cerveza Pilsen", "http://45.79.159.123/pilsen.jpeg");

        for(String name : file_maps2.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps2.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider2.addSlider(textSliderView);
        }
        mDemoSlider2.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider2.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider2.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider2.setDuration(4000);
        mDemoSlider2.addOnPageChangeListener(this);

        imageViewOfertaItau = (ImageView) view.findViewById(R.id.imageViewOfertaItau);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/itau.jpg")
                .into(imageViewOfertaItau);

        imageViewOfertaPromoItau = (ImageView) view.findViewById(R.id.imageViewOfertasPromoItau);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/promo_itau.jpg")
                .into(imageViewOfertaPromoItau);

        imageViewOfertaDominos = (ImageView) view.findViewById(R.id.imageViewOfertasDominos);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/domino.jpeg")
                .into(imageViewOfertaDominos);

        imageViewOfertaBurguer = (ImageView) view.findViewById(R.id.imageViewOfertasBurguer);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/domino.jpeg")
                .into(imageViewOfertaBurguer);


        imageViewOfertaBolson = (ImageView) view.findViewById(R.id.imageViewOfertasBolson);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/bolson.jpg")
                .into(imageViewOfertaBolson);

        imageViewOfertaZapato = (ImageView) view.findViewById(R.id.imageViewOfertasZapato);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/zapato.jpg")
                .into(imageViewOfertaZapato);


        imageViewFavoritoGajas = (ImageView) view.findViewById(R.id.imageViewFavoritosGajas);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/gajas.jpeg")
                .into(imageViewFavoritoGajas);

        imageViewFavoritoMochila = (ImageView) view.findViewById(R.id.imageViewFavoritosMochila);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/mochila.jpg")
                .into(imageViewFavoritoMochila);

        imageViewFavoritoAuricular = (ImageView) view.findViewById(R.id.imageViewFavoritosAuricular);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/auricular.jpg")
                .into(imageViewFavoritoAuricular);

        imageViewRecomendadoTenis = (ImageView) view.findViewById(R.id.imageViewRecomendadosTenis);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/tenis.jpg")
                .into(imageViewRecomendadoTenis);

        imageViewRecomendadoNotebook = (ImageView) view.findViewById(R.id.imageViewRecomendadosNotebook);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/notebook.jpeg")
                .into(imageViewRecomendadoNotebook);

        imageViewRecomendadoHeineken = (ImageView) view.findViewById(R.id.imageViewRecomendadosHeineken);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/heineken.jpg")
                .into(imageViewRecomendadoHeineken);
        return view;
    }

    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onBuscarClienteEvent(BuscarClienteEvent evet){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Infoguia");
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
