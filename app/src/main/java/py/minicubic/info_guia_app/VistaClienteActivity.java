package py.minicubic.info_guia_app;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarCliente);
        setSupportActionBar(toolbar);
        tabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);
        lista = (ArrayList<ClienteDTO>) getIntent().getSerializableExtra("lista");
        Bundle bundle = new Bundle();
        bundle.putSerializable("lista", (Serializable) lista);
        ClientePerfilFragment perfilFragment = new ClientePerfilFragment();
        perfilFragment.setArguments(bundle);
        tabHost.addTab(tabHost.newTabSpec("perfil").setIndicator("Perfil"), perfilFragment.getClass(), bundle);
        tabHost.addTab(tabHost.newTabSpec("informacion").setIndicator("Informacion"), ClienteInformacionFragment.class, null);
        tabHost.addTab(tabHost.newTabSpec("promociones").setIndicator("Promociones"), ClientePromocionesFragment.class, null);
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