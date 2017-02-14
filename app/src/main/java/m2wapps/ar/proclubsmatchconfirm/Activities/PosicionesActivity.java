package m2wapps.ar.proclubsmatchconfirm.Activities;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;




import m2wapps.ar.proclubsmatchconfirm.Adapters.GoleadoresAdapter;
import m2wapps.ar.proclubsmatchconfirm.Adapters.PosicionesAdapter;
import m2wapps.ar.proclubsmatchconfirm.Fragments.ArquerosFragment;
import m2wapps.ar.proclubsmatchconfirm.Fragments.AsistidoresFragment;
import m2wapps.ar.proclubsmatchconfirm.Fragments.DefensoresFragment;
import m2wapps.ar.proclubsmatchconfirm.Fragments.GoleadoresFragment;
import m2wapps.ar.proclubsmatchconfirm.Fragments.MvpFragment;
import m2wapps.ar.proclubsmatchconfirm.Fragments.PosicionesFragment;
import m2wapps.ar.proclubsmatchconfirm.R;
import m2wapps.ar.proclubsmatchconfirm.Tabla;

/**
 * Created by mariano on 04/09/2016.
 */
public class PosicionesActivity extends AppCompatActivity {
    private List<Tabla> posicionesBList = new ArrayList<>();
    private List<Tabla> posicionesAList = new ArrayList<>();
    private List<Tabla> goleadoresBList = new ArrayList<>();
    private List<Tabla> goleadoresAList = new ArrayList<>();
    private List<Tabla> asistidoresBList = new ArrayList<>();
    private List<Tabla> asistidoresAList = new ArrayList<>();
    private List<Tabla> mvpAList = new ArrayList<>();
    private List<Tabla> mvpBList = new ArrayList<>();
    private List<Tabla> defensoresAList = new ArrayList<>();
    private List<Tabla> defensoresBList = new ArrayList<>();
    private List<Tabla> arquerosAList = new ArrayList<>();
    private List<Tabla> arquerosBList = new ArrayList<>();
    private static String argA ="http://www.vpsleague.com/campeonatos/primera-division-argentina/";
    private static String argB ="http://www.vpsleague.com/campeonatos/segunda-division-argentina/";
    private static String uruA ="http://www.vpsleague.com/campeonatos/apertura-uruguay-fifa172/";
    private static String chiA ="http://www.vpsleague.com/campeonatos/liga-de-chile/";
    private static String ameA ="http://www.vpsleague.com/campeonatos/liga-de-america/";
    private static String imagenUrl ="http://www.vpsleague.com";
    private Context mContext;
    private PosicionesAdapter mAdapter, mAdapter2;
    private GoleadoresAdapter mGAdapter, mGAdapter2, mASAdapter, mASAdapter2, mMvpAdapter, mMvpAdapter2, mDAdapter, mDAdapter2, mArqAdapter, mArqAdapter2;

    private int ligue;
    public int position2;
    private boolean division;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posiciones);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        position2= 0;
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Posiciones", PosicionesFragment.class)
                .add("MVPs", MvpFragment.class)
                .add("Goleadores", GoleadoresFragment.class)
                .add("Asistidores", AsistidoresFragment.class)
                .add("Defensores", DefensoresFragment.class)
                .add("Arqueros", ArquerosFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        assert viewPager != null;
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
        assert viewPagerTab != null;
        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                position2 = position;
                setAdapter(division,position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        viewPagerTab.setViewPager(viewPager);
        this.mContext = this.getApplicationContext();
        ligue = StartActivity.sLigaCache.get(0);
        Spinner spinner = (Spinner) findViewById(R.id.spinner4);

        ImageView ico = (ImageView) findViewById(R.id.ico);
        mAdapter = new PosicionesAdapter(posicionesBList);
        mAdapter2 = new PosicionesAdapter(posicionesAList);
        mGAdapter = new GoleadoresAdapter(goleadoresBList);
        mGAdapter2 = new GoleadoresAdapter(goleadoresAList);
        mASAdapter = new GoleadoresAdapter(asistidoresBList);
        mASAdapter2 = new GoleadoresAdapter(asistidoresAList);
        mMvpAdapter = new GoleadoresAdapter(mvpBList);
        mMvpAdapter2 = new GoleadoresAdapter(mvpAList);
        mDAdapter = new GoleadoresAdapter(defensoresBList);
        mDAdapter2 = new GoleadoresAdapter(defensoresAList);
        mArqAdapter = new GoleadoresAdapter(arquerosBList);
        mArqAdapter2 = new GoleadoresAdapter(arquerosAList);
        String[] arraySpinner;
        if (ligue == 2 || ligue == 1){
            //chile y uruguay
            assert ico != null;
            if(ligue == 1){
                ico.setImageResource(R.drawable.chile);
            }else {
                ico.setImageResource(R.drawable.uruguay);
            }
            arraySpinner = new String[]{
                    "Primera A"
            };
        }else if (ligue == 0){
            //argentina
                arraySpinner = new String[]{
                        "Primera A", "Primera B"
                };
        }else{
            //america
            assert ico != null;
                ico.setImageResource(R.drawable.america);
            arraySpinner = new String[]{
                    "Grupo A"
            };
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, arraySpinner);
        assert spinner != null;
        spinner.setAdapter(adapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getPosiciones(true);
                    getMvps(true);
                    getGoleadores(true);
                    getAsistidores(true);
                    getDefensores(true);
                    getArqueros(true);
                } else {
                    getPosiciones(false);
                    getMvps(false);
                    getGoleadores(false);
                    getAsistidores(false);
                    getDefensores(false);
                    getArqueros(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void getPosiciones(final boolean AorB){
        class GetPartidos extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                hideLoading();
                division = AorB;
                setAdapter(AorB,0);
            }

            @Override
            protected String doInBackground(Void... params) {
                posiciones(AorB);
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void posiciones(boolean AorB) {
        String url;
        String primeraA;
        String primeraB;
        if (ligue == 0){
            primeraA = argA;
            primeraB = argB;
        }else if (ligue == 1){
            primeraA = chiA;
            primeraB = primeraA;
        }
        else if (ligue == 2){
            primeraA = uruA;
            primeraB = primeraA;
        }else {
            primeraA = ameA;
            primeraB = primeraA;
        }
        if (AorB) {
            url = primeraA;
        } else {
            url = primeraB;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements;
            Elements tableRowElements;
            int max=0, min=0;

            if (ligue == 3) {
                tableElements = doc.getElementsByClass("table-responsive");
                tableRowElements = tableElements.select(":not(thead) tr");
                max = 16;
                min = 1;
            }else {
                tableElements = doc.getElementsByClass("table-md");
                tableRowElements = tableElements.select(":not(thead) tr");
                max = tableRowElements.size();
            }
            Tabla tabla;
            for (int i = min; i < max; i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                tabla = new Tabla();
                if (i % 2 == 0){
                    tabla.setColor(true);
                }
                for (int j = 0; j < rowItems.size(); j++) {
                    if (j == 0) {
                        String rank = rowItems.get(j).text();
                        rank = rank.replaceAll("\u00A0", "");
                        tabla.setPosicion(Integer.parseInt(rank));
                    } else if (j == 1) {
                        tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                      //  getIdEscudo(tabla, rowItems.get(j).text(), AorB);
                    } else if (j == 2) {
                        tabla.setPuntaje(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 3) {
                        tabla.setPartidos(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 4) {
                        tabla.setGanados(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 5) {
                        tabla.setEmpatados(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 6) {
                        tabla.setPerdidos(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 9) {
                        tabla.setGoles(Integer.parseInt(rowItems.get(j).text()));
                    }
                }
                if (AorB) {
                    if (posicionesAList.size() < tableRowElements.size()) {
                        posicionesAList.add(tabla);
                    }
                } else {
                    if (posicionesBList.size() < tableRowElements.size()) {
                        posicionesBList.add(tabla);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getMvps(final boolean AorB){
        class GetPartidos extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                hideLoading();
             //   division = AorB;
                setAdapter(AorB,1);
            }

            @Override
            protected String doInBackground(Void... params) {
                mvps(AorB);
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void mvps(boolean AorB) {
        String url;
        String primeraA;
        String primeraB;
        if (ligue == 0){
            primeraA = argA;
            primeraB = argB;
        }else if (ligue == 1){
            primeraA = chiA;
            primeraB = primeraA;
        }
        else if (ligue == 2){
            primeraA = uruA;
            primeraB = primeraA;
        }else {
            primeraA = ameA;
            primeraB = primeraA;
        }
        if (AorB) {
            url = primeraA;
        } else {
            url = primeraB;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("div#top_mvps_tab");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            Tabla tabla;
            for (int i = 1; i < tableRowElements.size()-1; i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                tabla = new Tabla();
                for (int j = 0; j < rowItems.size(); j++) {
                    if (j == 0) {
                        tabla.setPosicion(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 1) {
                        tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                    } else if (j == 2) {
                        String arr[] = rowItems.get(j).text().split(" ", 2);
                        String firstWord = arr[0];
                        tabla.setJugador(firstWord);
                    } else if (j == 4) {
                        tabla.setGoles(Integer.parseInt(rowItems.get(j).text()));
                    }
                }
                if (AorB) {
                    if (mvpAList.size() < 5) {
                        mvpAList.add(tabla);
                    }
                } else {
                    if (mvpBList.size() < 5) {
                        mvpBList.add(tabla);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getGoleadores(final boolean AorB){
        class GetPartidos extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                hideLoading();
                division = AorB;
                setAdapter(AorB,2);
            }

            @Override
            protected String doInBackground(Void... params) {
                goleadores(AorB);
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void goleadores(boolean AorB) {
        String url;
        String primeraA;
        String primeraB;
        if (ligue == 0){
            primeraA = argA;
            primeraB = argB;
        }else if (ligue == 1){
            primeraA = chiA;
            primeraB = primeraA;
        }
        else if (ligue == 2){
            primeraA = uruA;
            primeraB = primeraA;
        }else {
            primeraA = ameA;
            primeraB = primeraA;
        }
        if (AorB) {
            url = primeraA;
        } else {
            url = primeraB;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("div#top_scorers_tab");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            Tabla tabla;
                for (int i = 1; i < tableRowElements.size()-1; i++) {
                    Element row = tableRowElements.get(i);
                    Elements rowItems = row.select("td");
                    tabla = new Tabla();
                    for (int j = 0; j < rowItems.size(); j++) {
                        if (j == 0) {
                            tabla.setPosicion(Integer.parseInt(rowItems.get(j).text()));
                        } else if (j == 1) {
                            tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                        } else if (j == 2) {
                            String arr[] = rowItems.get(j).text().split(" ", 2);
                            String firstWord = arr[0];
                            tabla.setJugador(firstWord);
                        } else if (j == 5) {
                            tabla.setGoles(Integer.parseInt(rowItems.get(j).text()));
                        }
                    }
                    if (AorB) {
                        if (goleadoresAList.size() < 5) {
                            goleadoresAList.add(tabla);
                        }
                    } else {
                        if (goleadoresBList.size() < 5) {
                            goleadoresBList.add(tabla);
                        }
                    }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getAsistidores(final boolean AorB){
        class GetPartidos extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                hideLoading();
                division = AorB;
                setAdapter(AorB,3);
            }

            @Override
            protected String doInBackground(Void... params) {
                asistidores(AorB);
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void asistidores(boolean AorB) {
        String url;
        String primeraA;
        String primeraB;
        if (ligue == 0){
            primeraA = argA;
            primeraB = argB;
        }else if (ligue == 1){
            primeraA = chiA;
            primeraB = primeraA;
        }
        else if (ligue == 2){
            primeraA = uruA;
            primeraB = primeraA;
        }else {
            primeraA = ameA;
            primeraB = primeraA;
        }
        if (AorB) {
            url = primeraA;
        } else {
            url = primeraB;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("div#top_assisters_tab");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            Tabla tabla;
             for (int i = 1; i < tableRowElements.size()-1; i++) {
                    Element row = tableRowElements.get(i);
                    Elements rowItems = row.select("td");
                    tabla = new Tabla();
                    for (int j = 0; j < rowItems.size(); j++) {
                        if (j == 0) {
                            tabla.setPosicion(Integer.parseInt(rowItems.get(j).text()));
                        } else if (j == 1) {
                            tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                        } else if (j == 2) {
                            String arr[] = rowItems.get(j).text().split(" ", 2);
                            String firstWord = arr[0];
                            //  String theRest = arr[1];
                            tabla.setJugador(firstWord);
                            //    tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                            //  getIdEscudo(tabla,theRest,AorB);
                        } else if (j == 5) {
                            tabla.setGoles(Integer.parseInt(rowItems.get(j).text()));
                        }
                    }
                    if (AorB) {
                        if (asistidoresAList.size() < 5) {
                            asistidoresAList.add(tabla);
                        }
                    } else {
                        if (asistidoresBList.size() < 5) {
                            asistidoresBList.add(tabla);
                        }
                    }
              }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getDefensores(final boolean AorB){
        class GetPartidos extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                hideLoading();
                division = AorB;
                setAdapter(AorB,4);
            }

            @Override
            protected String doInBackground(Void... params) {
                defensores(AorB);
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void defensores(boolean AorB) {
        String url;
        String primeraA;
        String primeraB;
        if (ligue == 0){
            primeraA = argA;
            primeraB = argB;
        }else if (ligue == 1){
            primeraA = chiA;
            primeraB = primeraA;
        }
        else if (ligue == 2){
            primeraA = uruA;
            primeraB = primeraA;
        }else {
            primeraA = ameA;
            primeraB = primeraA;
        }
        if (AorB) {
            url = primeraA;
        } else {
            url = primeraB;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("div#top_defenders_tab");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            Tabla tabla;
            for (int i = 1; i < tableRowElements.size()-1; i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                tabla = new Tabla();
                for (int j = 0; j < rowItems.size(); j++) {
                    if (j == 0) {
                        tabla.setPosicion(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 1) {
                        tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                    } else if (j == 2) {
                        String arr[] = rowItems.get(j).text().split(" ", 2);
                        String firstWord = arr[0];
                        //  String theRest = arr[1];
                        tabla.setJugador(firstWord);
                        //    tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                        //  getIdEscudo(tabla,theRest,AorB);
                    } else if (j == 5) {
                        tabla.setGoles(Integer.parseInt(rowItems.get(j).text()));
                    }
                }
                if (AorB) {
                    if (defensoresAList.size() < 5) {
                        defensoresAList.add(tabla);
                    }
                } else {
                    if (defensoresBList.size() < 5) {
                        defensoresBList.add(tabla);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getArqueros(final boolean AorB){
        class GetPartidos extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showLoading();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                hideLoading();
                division = AorB;
                setAdapter(AorB,5);
            }

            @Override
            protected String doInBackground(Void... params) {
                arqueros(AorB);
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void arqueros(boolean AorB) {
        String url;
        String primeraA;
        String primeraB;
        if (ligue == 0){
            primeraA = argA;
            primeraB = argB;
        }else if (ligue == 1){
            primeraA = chiA;
            primeraB = primeraA;
        }
        else if (ligue == 2){
            primeraA = uruA;
            primeraB = primeraA;
        }else {
            primeraA = ameA;
            primeraB = primeraA;
        }
        if (AorB) {
            url = primeraA;
        } else {
            url = primeraB;
        }
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("div#top_goalkeepers_tab");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            Tabla tabla;
            for (int i = 1; i < tableRowElements.size()-1; i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                tabla = new Tabla();
                for (int j = 0; j < rowItems.size(); j++) {
                    if (j == 0) {
                        tabla.setPosicion(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 1) {
                        tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                    } else if (j == 2) {
                        String arr[] = rowItems.get(j).text().split(" ", 2);
                        String firstWord = arr[0];
                        //  String theRest = arr[1];
                        tabla.setJugador(firstWord);
                        //    tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                        //  getIdEscudo(tabla,theRest,AorB);
                    } else if (j == 5) {
                        tabla.setGoles(Integer.parseInt(rowItems.get(j).text()));
                    }
                }
                if (AorB) {
                    if (arquerosAList.size() < 5) {
                        arquerosAList.add(tabla);
                    }
                } else {
                    if (arquerosBList.size() < 5) {
                        arquerosBList.add(tabla);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showLoading() {
        if (position2 == 0) {
            PosicionesFragment.loading.setVisibility(View.VISIBLE);
        }
        else if (position2 == 1) {
            MvpFragment.loading.setVisibility(View.VISIBLE);
        }
        else if (position2 == 2) {
            if (GoleadoresFragment.loading != null) {
                GoleadoresFragment.loading.setVisibility(View.VISIBLE);
            }
        }
        else if (position2 == 3) {
            if (AsistidoresFragment.loading != null) {
                AsistidoresFragment.loading.setVisibility(View.VISIBLE);
            }
        }else if (position2 == 4) {
            if (DefensoresFragment.loading != null) {
                DefensoresFragment.loading.setVisibility(View.VISIBLE);
            }
        }else {
            if (ArquerosFragment.loading != null) {
                ArquerosFragment.loading.setVisibility(View.VISIBLE);
            }
        }
    }
    public void hideLoading() {
        if (position2 == 0) {
            if (PosicionesFragment.loading != null) {
                PosicionesFragment.loading.setVisibility(View.GONE);
            }
        }
        else if (position2 == 1) {
            MvpFragment.loading.setVisibility(View.GONE);
        }
        else if (position2 == 2) {
            if (GoleadoresFragment.loading != null) {
                GoleadoresFragment.loading.setVisibility(View.GONE);
            }
        }
        else if (position2 == 3) {
            if (AsistidoresFragment.loading != null) {
                AsistidoresFragment.loading.setVisibility(View.GONE);
            }
        }else if (position2 == 4) {
            if (DefensoresFragment.loading != null) {
                DefensoresFragment.loading.setVisibility(View.GONE);
            }
        }else {
            if (ArquerosFragment.loading != null) {
                ArquerosFragment.loading.setVisibility(View.GONE);
            }
        }
    }
    public void setAdapter(boolean AorB, int position){
        if (position == 0) {
            if (AorB) {
                if ( PosicionesFragment.recyclerView.getAdapter() != mAdapter2) {
                    PosicionesFragment.recyclerView.setAdapter(mAdapter2);
                }
                mAdapter2.notifyDataSetChanged();
            } else {
                if ( PosicionesFragment.recyclerView.getAdapter() != mAdapter) {
                    PosicionesFragment.recyclerView.setAdapter(mAdapter);
                }
                mAdapter.notifyDataSetChanged();
            }

        }
        else if (position == 1){
            if (AorB) {
                if (MvpFragment.recyclerView.getAdapter() != mMvpAdapter2) {
                    MvpFragment.recyclerView.setAdapter(mMvpAdapter2);
                }
                mMvpAdapter2.notifyDataSetChanged();
            } else {
                if (MvpFragment.recyclerView.getAdapter() != mMvpAdapter) {
                    MvpFragment.recyclerView.setAdapter(mMvpAdapter);
                }
                mMvpAdapter.notifyDataSetChanged();
            }
        }
        else if (position == 2){
            if (position2 == 2) {
                if (AorB) {
                    if (GoleadoresFragment.recyclerView.getAdapter() != mGAdapter2) {
                        GoleadoresFragment.recyclerView.setAdapter(mGAdapter2);
                    }
                    mGAdapter2.notifyDataSetChanged();
                } else {
                    if (GoleadoresFragment.recyclerView.getAdapter() != mGAdapter) {
                        GoleadoresFragment.recyclerView.setAdapter(mGAdapter);
                    }
                    mGAdapter.notifyDataSetChanged();
                }
            }
        }else if(position == 3){
            if (position2 == 3) {
                if (AorB) {
                    if(AsistidoresFragment.recyclerView.getAdapter() != mASAdapter2) {
                        AsistidoresFragment.recyclerView.setAdapter(mASAdapter2);
                    }
                    mASAdapter2.notifyDataSetChanged();
                } else {
                    if (AsistidoresFragment.recyclerView.getAdapter() != mASAdapter) {
                        AsistidoresFragment.recyclerView.setAdapter(mASAdapter);
                    }
                    mASAdapter.notifyDataSetChanged();
                }
            }
        }else if (position == 4){
            if (position2 == 4) {
                if (AorB) {
                    DefensoresFragment.recyclerView.setAdapter(mDAdapter2);
                    mDAdapter2.notifyDataSetChanged();
                } else {
                    DefensoresFragment.recyclerView.setAdapter(mDAdapter);
                    mDAdapter.notifyDataSetChanged();
                }
            }
        }else {
            if (position2 == 5) {
                if (AorB) {
                    ArquerosFragment.recyclerView.setAdapter(mArqAdapter2);
                    mArqAdapter2.notifyDataSetChanged();
                } else {
                    ArquerosFragment.recyclerView.setAdapter(mArqAdapter);
                    mArqAdapter.notifyDataSetChanged();
                }
            }
        }
    }
    private void getIdEscudo(final Tabla t , final String name, final boolean AorB) {
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
                        aux = PosicionesActivity.this.getResources().getIdentifier(primeraFix[i], "drawable", PosicionesActivity.this.getPackageName());
                  //      t.setEquipo(aux);
                    }
                }
    }
}

