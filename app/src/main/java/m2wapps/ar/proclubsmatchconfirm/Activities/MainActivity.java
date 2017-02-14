package m2wapps.ar.proclubsmatchconfirm.Activities;

import android.content.Intent;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.common.collect.Lists;

import java.util.List;

import m2wapps.ar.proclubsmatchconfirm.R;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {


    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private int ligue;
    public static final String MY_PREFS_NAME = "Email";
    public static SparseBooleanArray sAorBCache = new SparseBooleanArray(1);

    public static SparseIntArray sEquipo1ImgCache = new SparseIntArray(1);
    public static SparseIntArray sEquipo2ImgCache = new SparseIntArray(1);
    public static SparseIntArray sIdCache = new SparseIntArray(1);
    public static SparseIntArray sColorCache = new SparseIntArray(1);

    public static SparseArray<String> sEstado1Cache = new SparseArray<>(1);
    public static SparseArray<String> sEstado2Cache = new SparseArray<>(1);
    public static SparseArray<String> sHoraCache = new SparseArray<>(1);
    public static SparseArray<String> sEmailCache = new SparseArray<>(1);
    public static SparseArray<String> sEquipo1Cache = new SparseArray<>(1);
    public static SparseArray<String> sEquipo2Cache = new SparseArray<>(1);

    public static final String URL_GET_CAPITANES_B = "http://lolblacklist.bugs3.com/lolblacklistdb/getCapitanesB.php";
    public static final String URL_GET_CAPITANES_A = "http://lolblacklist.bugs3.com/lolblacklistdb/getCapitanesA.php";
    public static final String URL_UPDATE_ESTADO1_B = "http://lolblacklist.bugs3.com/lolblacklistdb/update_Estado1.php";
    public static final String URL_UPDATE_ESTADO2_B = "http://lolblacklist.bugs3.com/lolblacklistdb/update_Estado2.php";
    public static final String URL_UPDATE_ESTADO1_A = "http://lolblacklist.bugs3.com/lolblacklistdb/update_EstadA1.php";
    public static final String URL_UPDATE_ESTADO2_A = "http://lolblacklist.bugs3.com/lolblacklistdb/update_EstadA2.php";


    private List<Integer> ligas = Lists.newArrayList(R.drawable.arg, R.drawable.chile, R.drawable.uruguay, R.drawable.america);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        sColorCache.put(0,getResources().getColor(R.color.fondo));
        if (StartActivity.sLigaCache.get(0) != null) {
            ligue = StartActivity.sLigaCache.get(0);
        }
        SharedPreferences prefs = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE);
        String email = prefs.getString("email", null);
        if (email != null) {
            sEmailCache.put(0,email);
            Toast.makeText(this,"Logueado correctamente",Toast.LENGTH_SHORT).show();
        }
        Button coordinar = (Button) findViewById(R.id.coordinar);
        Button resultados = (Button) findViewById(R.id.resultados);
        Button posiciones = (Button) findViewById(R.id.posiciones);
        Button transfers = (Button) findViewById(R.id.transfers);
        Button rankings = (Button) findViewById(R.id.rankings);
        ImageView liga = (ImageView) findViewById(R.id.liga);
        SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        if (ligue == 0) {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)

                    .build();
            assert signInButton != null;
            signInButton.setScopes(gso.getScopeArray());
            signInButton.setOnClickListener(this);
            assert coordinar != null;
          //  coordinar.setBackgroundColor(getResources().getColor(R.color.boton));
          //  coordinar.setTextColor(Color.WHITE);
            coordinar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  //  final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.buton);
                 //   mp.start();
                 //   Intent myIntent = new Intent(MainActivity.this, ListaActivity.class);
                 //   startActivity(myIntent);
                    Toast.makeText(MainActivity.this,"Proximamente",Toast.LENGTH_SHORT).show();
                }
            });
            assert transfers != null;
            transfers.setBackgroundColor(getResources().getColor(R.color.boton));
            transfers.setTextColor(Color.WHITE);
            transfers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.buton);
                    mp.start();
                }
            });
        }
        for (int i = 1; i < ligas.size(); i++){
            if(ligue == i){
                assert signInButton != null;
                signInButton.setVisibility(View.GONE);
                assert liga != null;
                liga.setImageResource(ligas.get(i));
            }
        }
      /*  if (ligue == 1){

        }else if (ligue == 2){
            assert signInButton != null;
            signInButton.setVisibility(View.GONE);
            assert liga != null;
            liga.setImageResource(R.drawable.uruguay);
        }*/
        assert resultados != null;
        resultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.buton);
                mp.start();
                Intent myIntent = new Intent(MainActivity.this, ResultadosActivity.class);
                startActivity(myIntent);
            }
        });
        assert posiciones != null;
        posiciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.buton);
                mp.start();
                Intent myIntent = new Intent(MainActivity.this, PosicionesActivity.class);
                startActivity(myIntent);
            }
        });
        assert rankings != null;
        rankings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final MediaPlayer mp = MediaPlayer.create(MainActivity.this, R.raw.buton);
                mp.start();
                Intent myIntent = new Intent(MainActivity.this, RankingsActivity.class);
                startActivity(myIntent);
            }
        });
    }



    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d("asd", "handleSignInResult:" + result.getStatus());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.

            GoogleSignInAccount acct = result.getSignInAccount();
            assert acct != null;
            sEmailCache.put(0,acct.getEmail());
            SharedPreferences.Editor editor = getSharedPreferences(MY_PREFS_NAME, MODE_PRIVATE).edit();
            editor.putString("email", acct.getEmail());
            editor.apply();
            Toast.makeText(this,"Logueado correctamente",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Error en login "+ result.getStatus(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        signIn();
    }
}
