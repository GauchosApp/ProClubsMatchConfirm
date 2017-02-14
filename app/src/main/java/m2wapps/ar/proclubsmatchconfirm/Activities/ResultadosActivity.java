package m2wapps.ar.proclubsmatchconfirm.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import m2wapps.ar.proclubsmatchconfirm.R;
import m2wapps.ar.proclubsmatchconfirm.Adapters.ResultadosAdapter;


/**
 * Created by mariano on 01/09/2016.
 */
public class ResultadosActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ResultadosAdapter mAdapter;
    private ArrayList<String> jornada, equipo1, equipo2, resultado;
    private ArrayList<String> escudo1, escudo2;
    private static String argA ="http://www.vpsleague.com/campeonatos/primera-division-argentina/";
    private static String argB ="http://www.vpsleague.com/campeonatos/segunda-division-argentina/";
    private static String uruA ="http://www.vpsleague.com/campeonatos/apertura-uruguay-fifa172/";
    private static String chiA ="http://www.vpsleague.com/campeonatos/liga-de-chile/";
    private static String ameA ="http://www.vpsleague.com/campeonatos/liga-de-america/";
    private Context mContext;
    private Spinner spinner2;
    private boolean first, AorB = true;
    private ArrayAdapter<String> adapter;
    private int ligue;
    private ProgressBar loading;
    private static String imagenUrl ="http://www.vpsleague.com";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultados);
        ligue = StartActivity.sLigaCache.get(0);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        mContext = this.getApplicationContext();
        loading = (ProgressBar) findViewById(R.id.activity_movies_progress);
        assert loading != null;
        loading.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), android.graphics.PorterDuff.Mode.MULTIPLY);
        ImageView ico = (ImageView) findViewById(R.id.ico);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view2);
        Spinner spinner = (Spinner) findViewById(R.id.spinner2);
        spinner2 = (Spinner) findViewById(R.id.spinner3);
        jornada = new ArrayList<>();
        equipo1 = new ArrayList<>();
        equipo2 = new ArrayList<>();
        resultado = new ArrayList<>();
        escudo1 = new ArrayList<>();
        escudo2 = new ArrayList<>();
        String[] arraySpinner = null;
        if (ligue == 2 || ligue == 1){
            assert ico != null;
            if(ligue == 1){
                ico.setImageResource(R.drawable.chile);
            }else {
                ico.setImageResource(R.drawable.uruguay);
            }
            arraySpinner = new String[]{
                    "Primera A"
            };
        }else if(ligue == 0){
                arraySpinner = new String[]{
                        "Primera A", "Primera B"
                };
        }else if(ligue == 3){
            ico.setImageResource(R.drawable.america);
            arraySpinner = new String[]{
                    "Grupo A"
            };
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        assert spinner != null;
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    AorB = true;
                    getPartidos(true);
                } else {
                    AorB = false;
                    getPartidos(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mAdapter = new ResultadosAdapter(resultado, escudo1, escudo2, position, AorB);
                first = true;
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        getPartidos(true);

    }

    private void opcion2(boolean AorB) {
        jornada = new ArrayList<>();
        equipo1 = new ArrayList<>();
        equipo2 = new ArrayList<>();
        resultado = new ArrayList<>();
        escudo1 = new ArrayList<>();
        escudo2 = new ArrayList<>();
        String url;
        String primeraA= "", primeraB = "";
        if (ligue == 0) {
            primeraA = argA;
            primeraB = argB;
        }else if (ligue == 1){
            primeraA = chiA;
            primeraB = primeraA;
        }else if (ligue == 2){
            primeraA = uruA;
            primeraB =  primeraA;
        }else if (ligue == 3){
            primeraA = ameA;
            primeraB =  primeraA;
        }
        if (AorB) {
            url = primeraA;
        } else {
            url = primeraB;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("div#matches_tab");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            String aux;
            for (int i = 0; i < tableRowElements.size(); i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                for (int j = 0; j < rowItems.size(); j++) {
                    if (!rowItems.get(j).text().contains("Jornada")) {
                        if (j == 0) {
                           equipo1.add(rowItems.get(j).text());
                            if (jornada.size() < 1) {
                                jornada.add(rowItems.get(j).text());
                            }
                           getIdEscudo(rowItems.get(j).text(), true, AorB);
                        } else if (j == 2) {
                            equipo2.add(rowItems.get(j).text());
                            getIdEscudo(rowItems.get(j).text(), false, AorB);
                            escudo1.add(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                        } else if (j == 3) {
                            String aux0 = rowItems.get(j).text();
                            Character aux2;
                            Character aux3;
                            if (aux0.length() > 0) {
                                aux2 = aux0.charAt(0);
                                aux3 = aux0.charAt(2);
                                resultado.add(aux2+ " - "+aux3);
                            }else {
                                resultado.add(" - ");
                            }
                        }else if (j == 4){
                            escudo2.add(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                        }
                    } else {
                        aux = rowItems.get(j).text().substring(0, 10);
                        aux = aux.replaceAll(",", " ");
                        jornada.add(aux);
                    }
                }
            }
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, llenarSpinner());


            if (!first) {
                mAdapter = new ResultadosAdapter(resultado, escudo1, escudo2, 0, AorB);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getIdEscudo(final String name, final boolean unoODos, final boolean AorB) {
        class GetPartidos extends AsyncTask<Void, Void, String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                hideLoading();
                spinner2.setAdapter(adapter);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            protected String doInBackground(Void... params) {
                int aux;
                String [] primera;
                String [] primeraFix;
                if (AorB){
                    if (ligue == 0) {
                        primera = mContext.getResources().getStringArray(R.array.primeraA);
                        primeraFix = mContext.getResources().getStringArray(R.array.primeraAFix);
                    }else if (ligue == 1 ){
                        primera = mContext.getResources().getStringArray(R.array.americaA);
                        primeraFix = mContext.getResources().getStringArray(R.array.americaAFix);
                    }else {
                        primera = mContext.getResources().getStringArray(R.array.uruA);
                        primeraFix = mContext.getResources().getStringArray(R.array.uruAFix);
                    }
                }else {
                    if (ligue == 0) {
                        primera = mContext.getResources().getStringArray(R.array.primeraB);
                        primeraFix = mContext.getResources().getStringArray(R.array.primeraBFix);
                    }else if (ligue == 1 ){
                        primera = mContext.getResources().getStringArray(R.array.americaB);
                        primeraFix = mContext.getResources().getStringArray(R.array.americaBFix);
                    }else {
                        primera = mContext.getResources().getStringArray(R.array.uruB);
                        primeraFix = mContext.getResources().getStringArray(R.array.uruBFix);
                    }
                }
                for (int i = 0; i < primera.length; i++) {
                    if (name.equals(primera[i])) {
                        aux = ResultadosActivity.this.getResources().getIdentifier(primeraFix[i], "drawable", ResultadosActivity.this.getPackageName());
                        if (unoODos) {
                      //      escudo1.add(aux);
                        } else {
                      //      escudo2.add(aux);
                        }
                    }
                }
                return "";
            }
        }
        GetPartidos gi = new GetPartidos();
        gi.execute();
    }
    private String[] llenarSpinner(){
        String[] aux = new String[jornada.size()];
        return jornada.toArray(aux);
    }
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
            }

            @Override
            protected String doInBackground(Void... params) {
                opcion2(AorB);
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    public void showLoading() {
        loading.setVisibility(View.VISIBLE);
    }
    public void hideLoading() {
        loading.setVisibility(View.GONE);
    }
}
