package py.minicubic.info_guia_app;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import py.minicubic.info_guia_app.dto.ClienteDTO;
import py.minicubic.info_guia_app.model.Novedades;

public class VistaClienteActivity extends AppCompatActivity {


    private FragmentTabHost tabHost;
    private List<ClienteDTO> lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_cliente);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCliente);
        //setSupportActionBar(toolbar);
        tabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);
        Long id =  getIntent().getLongExtra("idCliente",0);
        String titulo = getIntent().getStringExtra("titulo");
        String coordenadas =getIntent().getStringExtra("coordenadas");
        String telefono = getIntent().getStringExtra("telefonos");
        String direccion = getIntent().getStringExtra("direccion");
        String photo_url = getIntent().getStringExtra("photo_url");
        Bundle bundle = new Bundle();
        bundle.putSerializable("idCliente", id);
        bundle.putString("titulo", titulo);
        bundle.putString("coordenadas", coordenadas);
        bundle.putString("telefonos", telefono);
        bundle.putString("direccion", direccion);
        bundle.putString("photo_url", photo_url);
        bundle.putString("horario_atencion", getIntent().getStringExtra("horario_atencion"));
        bundle.putString("emails", getIntent().getStringExtra("emails"));
        bundle.putString("sitio_web", getIntent().getStringExtra("sitio_web"));
        bundle.putInt("cant_sucursales",getIntent().getIntExtra("cant_sucursales", 0));

        ClientePerfilFragment perfilFragment = new ClientePerfilFragment();
        perfilFragment.setArguments(bundle);

        tabHost.addTab(tabHost.newTabSpec("perfil").setIndicator(getTabIndicator(tabHost.getContext(),"Perfil", null)), perfilFragment.getClass(), bundle);

        ListaSucursalesFragment sucursalesFragment = new ListaSucursalesFragment();
        bundle = new Bundle();
        bundle.putLong("idCliente", id);

        sucursalesFragment.setArguments(bundle);

        ClientePromocionesFragment promocionesFragment = new ClientePromocionesFragment();
        promocionesFragment.setArguments(bundle);

        //tabHost.addTab(tabHost.newTabSpec("informacion").setIndicator(getTabIndicator(tabHost.getContext(),"Informacion", null)), ClienteInformacionFragment.class, null);
        if (getIntent().getIntExtra("cant_sucursales", 0) > 1){
            tabHost.addTab(tabHost.newTabSpec("sucursales").setIndicator(getTabIndicator(tabHost.getContext(),"Sucursales", null)), sucursalesFragment.getClass(), bundle);
        }

        tabHost.addTab(tabHost.newTabSpec("promociones").setIndicator(getTabIndicator(tabHost.getContext(),"Promociones", null)), promocionesFragment.getClass(), bundle);

    }

    private View getTabIndicator(Context context, String title, Integer icon) {
        View view = LayoutInflater.from(context).inflate(R.layout.tab_layout, null);
        TextView tv = (TextView) view.findViewById(R.id.textViewTab);
        tv.setText(title);
        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
