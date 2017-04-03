package py.minicubic.info_guia_app;

import android.app.ActionBar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ManuPrincipalActivity extends FragmentActivity {

    private FragmentTabHost tabHost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manu_principal);
        tabHost = (FragmentTabHost) findViewById(R.id.tabhost);
        tabHost.setup(this, getSupportFragmentManager(),android.R.id.tabcontent);

        tabHost.addTab(tabHost.newTabSpec("novedades").setIndicator("Novedades"), NovedadesFragment.class,null);
        tabHost.addTab(tabHost.newTabSpec("lista").setIndicator("Lista"), ListaFragment.class, null);

        //qué queremos que aparezca en las pestañas//definimos el id de cada Tab (pestaña)


    }

}
