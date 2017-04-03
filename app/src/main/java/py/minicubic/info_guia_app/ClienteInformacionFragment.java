package py.minicubic.info_guia_app;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ClienteInformacionFragment extends Fragment {

    private Button btnPublicar;
    private RatingBar ratingBar;
    private ImageView imageViewClienteInfo1, imageViewClienteInfo2,imageViewClienteInfo3,imageViewClienteInfo4,
            imageViewClienteInfo5,imageViewClienteInfo6,imageViewClienteInfo7,imageViewClienteInfo8;
    public ClienteInformacionFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cliente_informacion, container, false);
        imageViewClienteInfo1 = (ImageView) view.findViewById(R.id.imageViewClienteInfo1);
        imageViewClienteInfo2 = (ImageView) view.findViewById(R.id.imageViewClienteInfo2);
        imageViewClienteInfo3 = (ImageView) view.findViewById(R.id.imageViewClienteInfo3);
        imageViewClienteInfo4 = (ImageView) view.findViewById(R.id.imageViewClienteInfo4);
        imageViewClienteInfo5 = (ImageView) view.findViewById(R.id.imageViewClienteInfo5);
        imageViewClienteInfo6 = (ImageView) view.findViewById(R.id.imageViewClienteInfo6);
        imageViewClienteInfo7 = (ImageView) view.findViewById(R.id.imageViewClienteInfo7);
        imageViewClienteInfo8 = (ImageView) view.findViewById(R.id.imageViewClienteInfo8);
        Picasso.with(getActivity())
                .load("http://45.79.159.123/bolson.jpg")
                .into(imageViewClienteInfo1);

        Picasso.with(getActivity())
                .load("http://45.79.159.123/zapato.jpg")
                .into(imageViewClienteInfo2);

        Picasso.with(getActivity())
                .load("http://45.79.159.123/gajas.jpeg")
                .into(imageViewClienteInfo3);

        Picasso.with(getActivity())
                .load("http://45.79.159.123/mochila.jpg")
                .into(imageViewClienteInfo4);

        Picasso.with(getActivity())
                .load("http://45.79.159.123/auricular.jpg")
                .into(imageViewClienteInfo5);

        Picasso.with(getActivity())
                .load("http://45.79.159.123/tenis.jpg")
                .into(imageViewClienteInfo6);

        Picasso.with(getActivity())
                .load("http://45.79.159.123/notebook.jpeg")
                .into(imageViewClienteInfo7);

        Picasso.with(getActivity())
                .load("http://45.79.159.123/heineken.jpg")
                .into(imageViewClienteInfo8);

        btnPublicar = (Button) view.findViewById(R.id.btnPublicar);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        btnPublicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Se publico el comentario", Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

}
