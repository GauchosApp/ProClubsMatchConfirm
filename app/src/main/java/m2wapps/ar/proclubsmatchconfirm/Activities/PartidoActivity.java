package m2wapps.ar.proclubsmatchconfirm.Activities;

import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import m2wapps.ar.proclubsmatchconfirm.R;
import m2wapps.ar.proclubsmatchconfirm.Handlers.RequestHandler;
import m2wapps.ar.proclubsmatchconfirm.Tabla;

/**
 * Created by mariano on 19/08/2016.
 */
public class PartidoActivity extends AppCompatActivity {
    private Button listo,preparandose,pedir,equipo1,equipo2;
    private TextView hora;
    private String estado1, estado2, horaAux;
    private boolean running = true;
    private boolean admin1 = false;
    private ImageView info1;
    private ArrayList<String> caps1, caps2;
    private ImageView info2;


    @Override
    protected void onPause() {
        super.onPause();
        try {
            pauseT();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
         resumeT();
        if (MainActivity.sEmailCache.get(0) != null) {
            checkAdmin();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partido);
        if (MainActivity.sEmailCache.get(0) != null) {
            checkAdmin();
        }
        hora = (TextView) findViewById(R.id.hora);
        ImageView equipoEscudo1 = (ImageView) findViewById(R.id.escudo1);
        ImageView equipoEscudo2 = (ImageView) findViewById(R.id.escudo2);
        listo = (Button)findViewById(R.id.listo);
        preparandose = (Button)findViewById(R.id.preparandose);
        pedir = (Button)findViewById(R.id.pedir);
        equipo1 = (Button)findViewById(R.id.equipo1);
        equipo2 = (Button)findViewById(R.id.equipo2);
        info1 = (ImageView)findViewById(R.id.capitanes1);
        info2 = (ImageView)findViewById(R.id.capitanes2);
        getCapitanes(true);
        getCapitanes(false);
        info1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux="";
                for (String a : caps1){
                    aux = aux + a+"\n";
                }
                new AlertDialog.Builder(PartidoActivity.this)
                        .setTitle("Capitanes de "+MainActivity.sEquipo1Cache.get(0))
                        .setMessage(aux)
                        .setPositiveButton(android.R.string.yes, null)
                        .setIcon(R.mipmap.ic_info_outline_black_36dp)
                        .show();
            }
        });
        info2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String aux="";
                for (String a : caps2){
                    aux = aux + a+"\n";
                }
                new AlertDialog.Builder(PartidoActivity.this)
                        .setTitle("Capitanes de "+MainActivity.sEquipo2Cache.get(0))
                        .setMessage(aux)
                        .setPositiveButton(android.R.string.yes, null)
                        .setIcon(R.mipmap.ic_info_outline_black_36dp)
                        .show();
            }
        });
        equipo1.setText(MainActivity.sEstado1Cache.get(0));
        equipo2.setText(MainActivity.sEstado2Cache.get(0));
        if (MainActivity.sEstado1Cache.get(0).equals("LISTO")){
            equipo1.setBackgroundColor(getResources().getColor(R.color.listo));
        }else if (MainActivity.sEstado1Cache.get(0).equals("PREPARANDOSE")){
            equipo1.setBackgroundColor(getResources().getColor(R.color.preparandose));
        }else if (MainActivity.sEstado1Cache.get(0).equals("PIDE LOS 10")){
            equipo1.setBackgroundColor(getResources().getColor(R.color.pedir));
        }

        if (MainActivity.sEstado2Cache.get(0).equals("LISTO")){
            equipo2.setBackgroundColor(getResources().getColor(R.color.listo));
        }else if (MainActivity.sEstado2Cache.get(0).equals("PREPARANDOSE")){
            equipo2.setBackgroundColor(getResources().getColor(R.color.preparandose));
        }else if (MainActivity.sEstado2Cache.get(0).equals("PIDE LOS 10")){
            equipo2.setBackgroundColor(getResources().getColor(R.color.pedir));
        }
        hora.setText(MainActivity.sHoraCache.get(0));
        assert equipoEscudo1 != null;
        equipoEscudo1.setImageResource(MainActivity.sEquipo1ImgCache.get(0));
        assert equipoEscudo2 != null;
        equipoEscudo2.setImageResource(MainActivity.sEquipo2ImgCache.get(0));

        listo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(PartidoActivity.this, R.raw.buton);
                mp.start();
                if (admin1) {
                    equipo1.setText(R.string.listo);
                    estado1 = "LISTO";
                    horaAux = (String) hora.getText();
                    hiloUpdate(true);
                    equipo1.setBackgroundColor(getResources().getColor(R.color.listo));
                }else {
                    equipo2.setText(R.string.listo);
                    estado2 = "LISTO";
                    horaAux = (String) hora.getText();
                    hiloUpdate(false);
                    equipo2.setBackgroundColor(getResources().getColor(R.color.listo));
                }
            }
        });
        preparandose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(PartidoActivity.this, R.raw.buton);
                mp.start();
                if (admin1) {
                    equipo1.setText(R.string.preparandose);
                    estado1 = "PREPARANDOSE";
                    horaAux = (String) hora.getText();
                    hiloUpdate(true);
                    equipo1.setBackgroundColor(getResources().getColor(R.color.preparandose));
                }else {
                    equipo2.setText(R.string.preparandose);
                    estado2 = "PREPARANDOSE";
                    horaAux = (String) hora.getText();
                    hiloUpdate(false);
                    equipo2.setBackgroundColor(getResources().getColor(R.color.preparandose));
                }
            }
        });
        pedir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(PartidoActivity.this, R.raw.buton);
                mp.start();
                if (admin1) {
                    equipo1.setText(R.string.pedir_los_10);
                    estado1 = "PIDE LOS 10";
                    String aux = (String) hora.getText();
                    aux = aux.replaceAll("\\D+", "");
                    int x = Integer.decode(aux);
                    if (x == 2300) {
                        hora.setText(R.string.hora10);
                    } else if (x == 2330) {
                        hora.setText(R.string.hora20);
                    }
                    horaAux = (String) hora.getText();
                    hiloUpdate(true);
                    equipo1.setBackgroundColor(getResources().getColor(R.color.pedir));
                }else {
                    equipo2.setText(R.string.pedir_los_10);
                    estado2 = "PIDE LOS 10";
                    String aux = (String) hora.getText();
                    aux = aux.replaceAll("\\D+", "");
                    int x = Integer.decode(aux);
                    if (x == 2300) {
                        hora.setText(R.string.hora10);
                    } else if (x == 2330) {
                        hora.setText(R.string.hora20);
                    }
                    horaAux = (String) hora.getText();
                    hiloUpdate(false);
                    equipo2.setBackgroundColor(getResources().getColor(R.color.pedir));
                }
            }
        });
        checkStatuses();
    }
    private void hiloUpdate(final boolean x){
        Thread hiloUpdater = new Thread(new Runnable() {
            @Override
            public void run() {
                if (x) {
                    updateEstado1();
                } else {
                    updateEstado2();
                }
            }
        });
        hiloUpdater.start();
    }
    private void updateEstado1(){

        class UpdateEstado1 extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
              //  resumeT();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("estado",estado1);
                hashMap.put("hora",horaAux);
                hashMap.put("equipa",MainActivity.sEquipo1Cache.get(0));
                hashMap.put("equipb",MainActivity.sEquipo2Cache.get(0));
                RequestHandler rh = new RequestHandler();
                String s;
                if (MainActivity.sAorBCache.get(0)) {
                    s = rh.sendPostRequest(MainActivity.URL_UPDATE_ESTADO1_A, hashMap);
                }else {
                    s = rh.sendPostRequest(MainActivity.URL_UPDATE_ESTADO1_B, hashMap);
                }

                return s;
            }
        }

        UpdateEstado1 ui = new UpdateEstado1();
        ui.execute();
    }
    private void updateEstado2(){

        class UpdateEstado2 extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //  resumeT();
            }

            @Override
            protected String doInBackground(Void... params) {
                HashMap<String,String> hashMap = new HashMap<>();
                hashMap.put("estado",estado2);
                hashMap.put("hora",horaAux);
                hashMap.put("equipa",MainActivity.sEquipo1Cache.get(0));
                hashMap.put("equipb",MainActivity.sEquipo2Cache.get(0));
                RequestHandler rh = new RequestHandler();
                String s;
                if (MainActivity.sAorBCache.get(0)) {
                    s = rh.sendPostRequest(MainActivity.URL_UPDATE_ESTADO2_A, hashMap);
                }else {
                    s = rh.sendPostRequest(MainActivity.URL_UPDATE_ESTADO2_B, hashMap);
                }

                return s;
            }
        }

        UpdateEstado2 ui = new UpdateEstado2();
        ui.execute();
    }
    private void checkStatuses(){
        Thread hiloChecker = new Thread(new Runnable() {
            @Override
            public void run() {
                while (running) {
                    getPartidos();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        hiloChecker.start();
    }
    private void getPartidos(){
        class GetPartidos extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                showPartidos(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s;
                if (MainActivity.sAorBCache.get(0)) {
                    s = rh.sendGetRequest(ListaActivity.URL_GET_PARTIDOS_A);
                }else {
                    s = rh.sendGetRequest(ListaActivity.URL_GET_PARTIDOS_B);
                }
                return s;
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
    private void showPartidos(String json){
        try {
            JSONArray result = new JSONArray(json);
            JSONArray match = result.getJSONArray(MainActivity.sIdCache.get(0));
                if (!match.getString(4).equals(equipo1.getText())) {
                    equipo1.setText(match.getString(4));
                    if (match.getString(4).equals("LISTO")) {
                           final MediaPlayer mp = MediaPlayer.create(PartidoActivity.this, R.raw.buton);
                            mp.start();
                        equipo1.setBackgroundColor(getResources().getColor(R.color.listo));
                    } else if (match.getString(4).equals("PREPARANDOSE")) {
                           final MediaPlayer mp = MediaPlayer.create(PartidoActivity.this, R.raw.buton);
                           mp.start();
                        equipo1.setBackgroundColor(getResources().getColor(R.color.preparandose));
                    } else if (match.getString(4).equals("PIDE LOS 10")) {
                           final MediaPlayer mp = MediaPlayer.create(PartidoActivity.this, R.raw.buton);
                            mp.start();
                        equipo1.setBackgroundColor(getResources().getColor(R.color.pedir));
                    }
                }
            if (!match.getString(5).equals(equipo2.getText())) {
                equipo2.setText(match.getString(5));
                if (match.getString(5).equals("LISTO")) {
                       final MediaPlayer mp = MediaPlayer.create(PartidoActivity.this, R.raw.buton);
                       mp.start();
                    equipo2.setBackgroundColor(getResources().getColor(R.color.listo));
                } else if (match.getString(5).equals("PREPARANDOSE")) {
                      final MediaPlayer mp = MediaPlayer.create(PartidoActivity.this, R.raw.buton);
                      mp.start();
                    equipo2.setBackgroundColor(getResources().getColor(R.color.preparandose));
                } else if (match.getString(5).equals("PIDE LOS 10")) {
                      final MediaPlayer mp = MediaPlayer.create(PartidoActivity.this, R.raw.buton);
                     mp.start();
                    equipo2.setBackgroundColor(getResources().getColor(R.color.pedir));
                }
            }


        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    private void checkAdmin(){
        class GetCapitanes extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                checkCapitanes(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                String s;
                if (MainActivity.sAorBCache.get(0)) {
                    s = rh.sendGetRequest(MainActivity.URL_GET_CAPITANES_A);
                }else {
                    s = rh.sendGetRequest(MainActivity.URL_GET_CAPITANES_B);
                }
                return s;
            }
        }
        GetCapitanes  gc = new GetCapitanes ();
        gc.execute();
    }
    private void checkCapitanes(String json){
        try {
            JSONArray result = new JSONArray(json);
            for (int i = 0; i < result.length(); i++){
                JSONArray match = result.getJSONArray(i);
                if (MainActivity.sEmailCache.get(0).equals(match.getString(0))){
                    if (MainActivity.sEquipo1Cache.get(0).equals(match.getString(1))){
                        admin1 = true;
                        listo.setVisibility(View.VISIBLE);
                        preparandose.setVisibility(View.VISIBLE);
                        pedir.setVisibility(View.VISIBLE);
                        info1.setVisibility(View.VISIBLE);
                        info2.setVisibility(View.VISIBLE);
                        break;
                    }else if (MainActivity.sEquipo2Cache.get(0).equals(match.getString(1))){
                        listo.setVisibility(View.VISIBLE);
                        preparandose.setVisibility(View.VISIBLE);
                        pedir.setVisibility(View.VISIBLE);
                        info1.setVisibility(View.VISIBLE);
                        info2.setVisibility(View.VISIBLE);
                        break;
                    }else {
                        admin1 = false;
                    }
                }else{
                    admin1 = false;
                }
            }


        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    private void getCapitanes(final boolean unoODos){
        class GetCapitanes extends AsyncTask<Void,Void,String> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                caps1 = new ArrayList<>();
                caps2 = new ArrayList<>();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

            }
            @Override
            protected String doInBackground(Void... params) {
                String s;
                int aux;
                if (unoODos) {
                    aux = PartidoActivity.this.getResources().getIdentifier(MainActivity.sEquipo1Cache.get(0), "string", PartidoActivity.this.getPackageName());
                }else {
                    aux = PartidoActivity.this.getResources().getIdentifier(MainActivity.sEquipo2Cache.get(0), "string", PartidoActivity.this.getPackageName());
                }
                 s = PartidoActivity.this.getResources().getString(aux);
                getCapis(s,unoODos);
                return s;
            }
        }
        GetCapitanes  gc = new GetCapitanes ();
        gc.execute();
    }
    private void getCapis(String s, boolean unoODos) {
        String url = s;

        try {
            Document doc = Jsoup.connect(url).get();
            Elements tableElements = doc.select("table");
            Elements tableRowElements = tableElements.select(":not(thead) tr");
            for (int i = 0; i < tableRowElements.size(); i++) {
                Element row = tableRowElements.get(i);
                Elements rowItems = row.select("td");
                for (int j = 0; j < rowItems.size(); j++) {
                    if (i == 10){
                    if (unoODos){
                        caps1.add(rowItems.get(j).text());
                    }else {
                        caps2.add(rowItems.get(j).text());
                    }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void pauseT() throws InterruptedException
    {
        running = false;
    }

    public void resumeT()
    {
        running = true;
    }
}

