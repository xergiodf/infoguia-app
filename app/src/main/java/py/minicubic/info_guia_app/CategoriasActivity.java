package py.minicubic.info_guia_app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import py.minicubic.info_guia_app.expandable_adapter.ExpandableListAdapter;

public class CategoriasActivity extends AppCompatActivity {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        int categorias = getIntent().getIntExtra("idCategoria", 0);
        // preparing list data
        if (categorias == 1){
            prepareListDataInfo();
        }else if (categorias == 2){
            prepareListDataNegocios();
        }else if (categorias == 3){
            prepareListDataOcio();
        }else if (categorias == 4){
            prepareListDataTurismo();
        }else if (categorias == 5){
            prepareListDataServicios();
        }


        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group click listener
        expListView.setOnGroupClickListener(new OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
    }

    /*
     * Preparing the list data
     */
    private void prepareListDataInfo() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Municipalidades");
        listDataHeader.add("Ministerios");
        listDataHeader.add("Gobernacion");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Muni. Asuncion");
        top250.add("Muni. Fdo. de la Mora");
        top250.add("Muni. Lambare");
        top250.add("Muni. San Lorenzo");
        top250.add("Muni. Luque");
        top250.add("Muni. Asuncion");
        top250.add("Muni. Capiata");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Ministerio de Educacion");
        nowShowing.add("Ministerio de Defensa");
        nowShowing.add("Ministerio de Agricultura");
        nowShowing.add("Ministerio del Interior");
        nowShowing.add("Ministerio de Emergencia");
        nowShowing.add("Ministerio de Trabajo");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Gobernacion de Central");
        comingSoon.add("Gobernacion de Alto Parana");
        comingSoon.add("Gobernacion de Itapua");
        comingSoon.add("Gobernacion de Cordillera");
        comingSoon.add("Gobernacion de Misiones");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    private void prepareListDataNegocios() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Cafes");
        listDataHeader.add("Bancos");
        listDataHeader.add("Restaurantes");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Cafe Paris");
        top250.add("Cafe Martinez");
        top250.add("La Vienesa");
        top250.add("Cafe Consulado");
        top250.add("Juan Valdez");


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Banco Familiar");
        nowShowing.add("Banco Itau");
        nowShowing.add("Banco Regional");
        nowShowing.add("Banco Interfisa");
        nowShowing.add("Banco Amanbay");
        nowShowing.add("Banco Atlas");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Lo de Osvaldo");
        comingSoon.add("La Parisiene");
        comingSoon.add("La Cabrera");
        comingSoon.add("Un toro y 7 vacas");
        comingSoon.add("El Dorado");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    private void prepareListDataOcio() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Shoping");
        listDataHeader.add("Cines");
        listDataHeader.add("Plazas");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Paseo La Galeria");
        top250.add("Moll Excelsior");
        top250.add("Shoping Pinedo");
        top250.add("Mcal Lopez Shoping");
        top250.add("Shoping del Sol");


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Hiperseis Boggiani");
        nowShowing.add("Cines Itau Multiplaza");
        nowShowing.add("Cinemark");
        nowShowing.add("Cinecenter Encarnacion");
        nowShowing.add("Cinecenter Shoping Sanlorenzo");
        nowShowing.add("Cinecenter Villamorra");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Plaza Uruguaya");
        comingSoon.add("Plaza de las Armas");
        comingSoon.add("Plaza Martin de Barua");
        comingSoon.add("Cristo Rey");
        comingSoon.add("Nino Jesus");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }

    private void prepareListDataTurismo() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Agencias de Viajes");
        listDataHeader.add("Lugares");
        listDataHeader.add("Hoteles");
        listDataHeader.add("Acontecimientos");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("DTP");
        top250.add("Intertours");
        top250.add("Viajes de lujo");


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Costanera de Asuncion");
        nowShowing.add("Palacio de Lopez");
        nowShowing.add("Costanera Encarnacion");
        nowShowing.add("Galeria de Ciudad del Este");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Hotel Trinidad");
        comingSoon.add("Hotel Casino Encarnacion");
        comingSoon.add("Hotel de la Costa");

        List<String> acont = new ArrayList<String>();
        acont.add("Dia de los Heroes");
        acont.add("Tanarandy");
        acont.add("Corsos de Encarnacion");


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        listDataChild.put(listDataHeader.get(3), acont);
    }

    private void prepareListDataServicios() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Seguros");
        listDataHeader.add("Hospitales");
        listDataHeader.add("Centros de Salud");
        listDataHeader.add("Emergencia");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("Seguridad Seguros");
        top250.add("Allianz");
        top250.add("MApfre");


        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("Centro de Salud, San Lorenzo");
        nowShowing.add("Centro de Salud, Encarnacion");
        nowShowing.add("Centro de Salud, Asuncion");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("Hospital de Encarnacion");
        comingSoon.add("Hospital de Ciudad del Este");
        comingSoon.add("IPS CENTRAL");

        List<String> acont = new ArrayList<String>();
        acont.add("Cruz Roja");
        acont.add("Cuarte 911");
        acont.add("Policia Nacional");


        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        listDataChild.put(listDataHeader.get(3), acont);
    }
}
