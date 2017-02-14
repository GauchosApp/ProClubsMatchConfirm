package m2wapps.ar.proclubsmatchconfirm.Activities;

import android.os.AsyncTask;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONException;


import java.util.ArrayList;
import java.util.List;

import m2wapps.ar.proclubsmatchconfirm.Partido;
import m2wapps.ar.proclubsmatchconfirm.Adapters.PartidosAdapter;
import m2wapps.ar.proclubsmatchconfirm.R;
import m2wapps.ar.proclubsmatchconfirm.Handlers.RequestHandler;

/**
 * Created by mariano on 01/09/2016.
 */
public class ListaActivity extends AppCompatActivity {
    //La VISTA que es la lista de partidos
    private RecyclerView recyclerView;
    //Adaptadores para llenar la lista
    private PartidosAdapter mAdapter, mAdapter2;
    //Arrays con los partidos
    private List<Partido> partidosBList = new ArrayList<>();
    private List<Partido> partidosAList = new ArrayList<>();
    //Auxiliares
    public ArrayList<String> auxEstado1;
    public ArrayList<String> auxEstado2;
    public ArrayList<String> auxEquipo1;
    public ArrayList<String> auxEquipo2;

    //Mi server donde agarro los estados en la parte de coordinar
    public static final String URL_GET_PARTIDOS_B = "http://lolblacklist.bugs3.com/lolblacklistdb/getPartidosB.php";
    public static final String URL_GET_PARTIDOS_A = "http://lolblacklist.bugs3.com/lolblacklistdb/getPartidosA.php";
    //Logo de cargando
    private ProgressBar loading;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //La actividad necesita una interface que se define asi:
        setContentView(R.layout.activity_lista);
        //agarro el progressbar e la interface y lo guardo en una variable para utilizarla
        loading = (ProgressBar) findViewById(R.id.activity_movies_progress);
        //si loading no es null le asigno color
        assert loading != null;
        loading.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        //defino la vista de la lista
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        //spinner (donde elegis primera a primera b) defino
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        //Creo los adaptadores para llenar
        mAdapter = new PartidosAdapter(partidosBList,ListaActivity.this);
        mAdapter2 = new PartidosAdapter(partidosAList,ListaActivity.this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //Seteo el adaptador de primera a
        recyclerView.setAdapter(mAdapter2);
        auxEstado1 = new ArrayList<>();
        auxEstado2 = new ArrayList<>();
        auxEquipo1 = new ArrayList<>();
        auxEquipo2 = new ArrayList<>();
        String[] arraySpinner = new String[]{
                "Primera A", "Primera B"
        };
        //adaptador para el spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        assert spinner != null;
        spinner.setAdapter(adapter);
        //creo un listener para si hay algun cambio si eligen primera a o primera b cambia
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
          @Override
          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
              if (position == 0 ){
                  MainActivity.sAorBCache.put(0,true);
                  getPartidos(true);
              }
              if (position == 1 ){
                  MainActivity.sAorBCache.put(0,false);
                  getPartidos(false);
              }
          }
          @Override
          public void onNothingSelected(AdapterView<?> parent) {

          }
      });


    }
    //Agarro el json de los partidos
    private void getPartidos(final boolean AorB){
        class GetPartidos extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
              getPartidos2(AorB, s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s;
                if (AorB) {
                    s = rh.sendGetRequest(URL_GET_PARTIDOS_A);
                }else {
                    s = rh.sendGetRequest(URL_GET_PARTIDOS_B);
                }
                return s;
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void getPartidos2(final boolean AorB , final String s){
        class GetPartidos extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                hideLoading();
                if (AorB){
                    recyclerView.setAdapter(mAdapter2);
                    mAdapter2.notifyDataSetChanged();
                }else {
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }

            }

            @Override
            protected String doInBackground(Void... params) {

                if (AorB) {
                    showPartidosA(s);
                }else {
                    showPartidosB(s);
                }
                return s;
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void showPartidosB(String json){
        try {
            JSONArray result = new JSONArray(json);
            Partido partido;
            int auxEquipo1;
            int auxEquipo2;
            this.auxEstado1 = new ArrayList<>();
            this.auxEstado2 = new ArrayList<>();
            this.auxEquipo1 = new ArrayList<>();
            this.auxEquipo2 = new ArrayList<>();
            for (int i = 0; i < result.length(); i++){
                JSONArray match = result.getJSONArray(i);
                auxEquipo1 = this.getResources().getIdentifier(match.getString(1), "drawable", this.getPackageName());
                auxEquipo2 = this.getResources().getIdentifier(match.getString(2), "drawable", this.getPackageName());
                this.auxEquipo1.add(match.getString(1));
                this.auxEquipo2.add(match.getString(2));
                auxEstado1.add(match.getString(4));
                auxEstado2.add(match.getString(5));
                partido = new Partido(auxEquipo1, auxEquipo2, match.getString(3));
                if (partidosBList.size() < result.length()) {
                    partidosBList.add(partido);
                }
            }


        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    private void showPartidosA(String json){
        try {
            JSONArray result = new JSONArray(json);
            Partido partido;
            int auxEquipo1;
            int auxEquipo2;
            this.auxEstado1 = new ArrayList<>();
            this.auxEstado2 = new ArrayList<>();
            this.auxEquipo1 = new ArrayList<>();
            this.auxEquipo2 = new ArrayList<>();
            for (int i = 0; i < result.length(); i++){
                JSONArray match = result.getJSONArray(i);
                auxEquipo1 = this.getResources().getIdentifier(match.getString(1), "drawable", this.getPackageName());
                auxEquipo2 = this.getResources().getIdentifier(match.getString(2), "drawable", this.getPackageName());
                this.auxEquipo1.add(match.getString(1));
                this.auxEquipo2.add(match.getString(2));
                auxEstado1.add(match.getString(4));
                auxEstado2.add(match.getString(5));
                partido = new Partido(auxEquipo1, auxEquipo2, match.getString(3));
                if (partidosAList.size() < result.length()) {
                    partidosAList.add(partido);
                }
            }


        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }
    public void hideLoading() {
       loading.setVisibility(View.GONE);
    }
}
