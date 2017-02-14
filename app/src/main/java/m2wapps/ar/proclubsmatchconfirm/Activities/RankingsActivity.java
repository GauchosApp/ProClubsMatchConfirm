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

import m2wapps.ar.proclubsmatchconfirm.Adapters.RankingAdapter;
import m2wapps.ar.proclubsmatchconfirm.Fragments.RankingArqFragment;
import m2wapps.ar.proclubsmatchconfirm.Fragments.RankingDefFragment;
import m2wapps.ar.proclubsmatchconfirm.Fragments.RankingDelFragment;
import m2wapps.ar.proclubsmatchconfirm.Fragments.RankingVolFragment;
import m2wapps.ar.proclubsmatchconfirm.R;
import m2wapps.ar.proclubsmatchconfirm.Tabla;

/**
 * Created by mariano on 03/11/2016.
 */

public class RankingsActivity extends AppCompatActivity {
    private List<Tabla> goleadoresAList = new ArrayList<>();
    private List<Tabla> asistidoresAList = new ArrayList<>();
    private List<Tabla> defensoresAList = new ArrayList<>();
    private List<Tabla> arquerosAList = new ArrayList<>();


    private static String imagenUrl ="http://www.vpsleague.com";
    private RankingAdapter mGAdapter2, mASAdapter2, mDAdapter2, mArqAdapter2;


    private int ligue;
    public int position2;

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
                .add("Arqueros", RankingArqFragment.class)
                .add("Defensores", RankingDefFragment.class)
                .add("Volantes",RankingVolFragment.class)
                .add("Atacantes", RankingDelFragment.class)
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
                setAdapter(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        viewPagerTab.setViewPager(viewPager);
        ligue = StartActivity.sLigaCache.get(0);
        Spinner spinner = (Spinner) findViewById(R.id.spinner4);

        ImageView ico = (ImageView) findViewById(R.id.ico);
        mGAdapter2 = new RankingAdapter(goleadoresAList);
        mASAdapter2 = new RankingAdapter(asistidoresAList);
        mDAdapter2 = new RankingAdapter(defensoresAList);
        mArqAdapter2 = new RankingAdapter(arquerosAList);
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
                    ""
            };
        }else if (ligue == 0){
            //argentina
            arraySpinner = new String[]{
                    ""
            };
        }else{
            //america
            assert ico != null;
            ico.setImageResource(R.drawable.america);
            arraySpinner = new String[]{
                    ""
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
                    getArqueros();
                    getDefensores();
                    getVolantes();
                    getAtacantes();
                } else {
                    getArqueros();
                    getDefensores();
                    getVolantes();
                    getAtacantes();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
    private void getArqueros(){
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
                setAdapter(0);
            }

            @Override
            protected String doInBackground(Void... params) {
                arqueros();
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void arqueros() {
        String url;
        String primeraA;
        if (ligue == 0){
            primeraA = "http://www.vpsleague.com/communities/vpsl-argentina-pc/";
        }else if (ligue == 1){
            primeraA = "http://www.vpsleague.com/communities/vpsl-chile-pc/";
        }
        else if (ligue == 2){
            primeraA ="http://www.vpsleague.com/communities/vpsl-uruguay-pc/";
        }else {
            primeraA = "http://www.vpsleague.com/communities/vpsl-interamericana-pc/";
        }
        url = primeraA;

        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("div#gks_tab");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            Tabla tabla;
            for (int i = 1; i < tableRowElements.size()-1; i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                tabla = new Tabla();
                for (int j = 0; j < rowItems.size(); j++) {
                    if (j == 10) {
                        tabla.setPosicion(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 2) {
                        tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                    } else if (j == 1) {
                        String arr[] = rowItems.get(j).text().split(" ", 2);
                        String firstWord = arr[0];
                        tabla.setJugador(firstWord);
                    }
                    else if (j == 3) {
                        tabla.setPj(Integer.parseInt(rowItems.get(j).text()));
                    }
                }
                    if (arquerosAList.size() < 21) {
                        arquerosAList.add(tabla);
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getDefensores(){
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
                setAdapter(1);
            }

            @Override
            protected String doInBackground(Void... params) {
                defensores();
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void defensores() {
        String url;
        String primeraA;
        if (ligue == 0){
            primeraA = "http://www.vpsleague.com/communities/vpsl-argentina-pc/";
        }else if (ligue == 1){
            primeraA = "http://www.vpsleague.com/communities/vpsl-chile-pc/";
        }
        else if (ligue == 2){
            primeraA ="http://www.vpsleague.com/communities/vpsl-uruguay-pc/";
        }else {
            primeraA = "http://www.vpsleague.com/communities/vpsl-interamericana-pc/";
        }
         url = primeraA;

        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("div#defs_tab");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            Tabla tabla;
            for (int i = 1; i < tableRowElements.size()-1; i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                tabla = new Tabla();
                for (int j = 0; j < rowItems.size(); j++) {
                    if (j == 10) {
                        tabla.setPosicion(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 2) {
                        tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                    } else if (j == 1) {
                        String arr[] = rowItems.get(j).text().split(" ", 2);
                        String firstWord = arr[0];
                        tabla.setJugador(firstWord);
                    }   else if (j == 3) {
                            tabla.setPj(Integer.parseInt(rowItems.get(j).text()));
                        }
                }
                    if (defensoresAList.size() < 21) {
                        defensoresAList.add(tabla);
                    }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getVolantes(){
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
                setAdapter(2);
            }

            @Override
            protected String doInBackground(Void... params) {
                volantes();
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void volantes() {
        String url;
        String primeraA;
        if (ligue == 0){
            primeraA = "http://www.vpsleague.com/communities/vpsl-argentina-pc/";
        }else if (ligue == 1){
            primeraA = "http://www.vpsleague.com/communities/vpsl-chile-pc/";
        }
        else if (ligue == 2){
            primeraA ="http://www.vpsleague.com/communities/vpsl-uruguay-pc/";
        }else {
            primeraA = "http://www.vpsleague.com/communities/vpsl-interamericana-pc/";
        }
            url = primeraA;

        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("div#mids_tab");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            Tabla tabla;
            for (int i = 1; i < tableRowElements.size()-1; i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                tabla = new Tabla();
                for (int j = 0; j < rowItems.size(); j++) {
                    if (j == 10) {
                        tabla.setPosicion(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 2) {
                        tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                    } else if (j == 1) {
                        String arr[] = rowItems.get(j).text().split(" ", 2);
                        String firstWord = arr[0];
                        tabla.setJugador(firstWord);
                    } else if (j == 3) {
                        tabla.setPj(Integer.parseInt(rowItems.get(j).text()));
                    }
                }
                    if (asistidoresAList.size() < 21) {
                        asistidoresAList.add(tabla);
                    }
                }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void getAtacantes(){
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
                setAdapter(3);
            }

            @Override
            protected String doInBackground(Void... params) {
                atacantes();
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void atacantes() {
        String url;
        String primeraA;
        if (ligue == 0){
            primeraA = "http://www.vpsleague.com/communities/vpsl-argentina-pc/";
        }else if (ligue == 1){
            primeraA = "http://www.vpsleague.com/communities/vpsl-chile-pc/";
        }
        else if (ligue == 2){
            primeraA ="http://www.vpsleague.com/communities/vpsl-uruguay-pc/";
        }else {
            primeraA = "http://www.vpsleague.com/communities/vpsl-interamericana-pc/";
        }
        url = primeraA;
        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("div#atks_tab");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            Tabla tabla;
            for (int i = 1; i < tableRowElements.size()-1; i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                tabla = new Tabla();
                for (int j = 0; j < rowItems.size(); j++) {
                    if (j == 10) {
                        tabla.setPosicion(Integer.parseInt(rowItems.get(j).text()));
                    } else if (j == 2) {
                        tabla.setEquipo(imagenUrl + rowItems.get(j).select("img[src]").attr("src"));
                    } else if (j == 1) {
                        String arr[] = rowItems.get(j).text().split(" ", 2);
                        String firstWord = arr[0];
                        tabla.setJugador(firstWord);
                    }   else if (j == 3) {
                        tabla.setPj(Integer.parseInt(rowItems.get(j).text()));
                    }
                }
                    if (goleadoresAList.size() < 21) {
                        goleadoresAList.add(tabla);
                    }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void showLoading() {
        if (position2 == 0) {
            if (RankingArqFragment.loading != null) {
                RankingArqFragment.loading.setVisibility(View.VISIBLE);
            }
        }
        else if (position2 == 1) {
            if (RankingDefFragment.loading != null) {
                RankingDefFragment.loading.setVisibility(View.VISIBLE);
            }
        }
        else if (position2 == 2) {
            if (RankingVolFragment.loading != null) {
                RankingVolFragment.loading.setVisibility(View.VISIBLE);
            }

        }
        else if (position2 == 3) {
            if (RankingDelFragment.loading != null) {
                RankingDelFragment.loading.setVisibility(View.VISIBLE);
            }
        }
    }
    public void hideLoading() {
        if (position2 == 0) {
            if (RankingArqFragment.loading != null) {
                RankingArqFragment.loading.setVisibility(View.GONE);
            }
        }
        else if (position2 == 1) {
            if (RankingDefFragment.loading != null) {
                RankingDefFragment.loading.setVisibility(View.GONE);
            }
        }
        else if (position2 == 2) {
            if (RankingVolFragment.loading != null) {
                RankingVolFragment.loading.setVisibility(View.GONE);
            }

        }
        else if (position2 == 3) {
            if (RankingDelFragment.loading != null) {
                RankingDelFragment.loading.setVisibility(View.GONE);
            }
        }
    }
    public void setAdapter(int position){
    if (position == 3){
            if (position2 == 3) {
                    if (RankingDelFragment.recyclerView.getAdapter() != mGAdapter2) {
                        RankingDelFragment.recyclerView.setAdapter(mGAdapter2);
                    }
                    mGAdapter2.notifyDataSetChanged();
            }
        }else if(position == 2){
            if (position2 == 2) {
                    if(RankingVolFragment.recyclerView.getAdapter() != mASAdapter2) {
                        RankingVolFragment.recyclerView.setAdapter(mASAdapter2);
                    }
                    mASAdapter2.notifyDataSetChanged();

            }
        }else if (position == 1){
            if (position2 == 1) {
                    RankingDefFragment.recyclerView.setAdapter(mDAdapter2);
                    mDAdapter2.notifyDataSetChanged();

            }
        }else if (position == 0){
            if (position2 == 0) {
                    RankingArqFragment.recyclerView.setAdapter(mArqAdapter2);
                    mArqAdapter2.notifyDataSetChanged();
            }
        }
    }
}
