package m2wapps.ar.proclubsmatchconfirm.Activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.View;


import com.google.common.collect.Lists;

import java.util.List;

import github.chenupt.multiplemodel.viewpager.ModelPagerAdapter;
import github.chenupt.multiplemodel.viewpager.PagerModelManager;
import github.chenupt.springindicator.SpringIndicator;
import github.chenupt.springindicator.viewpager.ScrollerViewPager;
import m2wapps.ar.proclubsmatchconfirm.Fragments.GuideFragment;
import m2wapps.ar.proclubsmatchconfirm.Presentacion;
import m2wapps.ar.proclubsmatchconfirm.R;
import su.levenetc.android.textsurface.TextSurface;


/**
 * Created by mariano on 31/08/2016.
 */
public class StartActivity extends AppCompatActivity{

    public static SparseArray<Integer> sLigaCache = new SparseArray<>(1);
    private TextSurface textSurface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        textSurface = (TextSurface) findViewById(R.id.text_surface);
        textSurface.postDelayed(new Runnable() {
            @Override public void run() {
                show();
            }
        }, 1000);
        ScrollerViewPager viewPager = (ScrollerViewPager) findViewById(R.id.view_pager);
        SpringIndicator springIndicator = (SpringIndicator) findViewById(R.id.indicator);

    

        PagerModelManager manager = new PagerModelManager();
        manager.addCommonFragment(GuideFragment.class, getBgRes(), getTitles());
        ModelPagerAdapter adapter = new ModelPagerAdapter(getSupportFragmentManager(), manager);
        assert viewPager != null;
        viewPager.setAdapter(adapter);
        viewPager.fixScrollSpeed();
        // just set viewPager
        assert springIndicator != null;
        springIndicator.setViewPager(viewPager);
    }
    private List<String> getTitles(){
        return Lists.newArrayList("Argentina", "Chile", "Uruguay", "America");
    }

    private List<Integer> getBgRes(){
        return Lists.newArrayList(R.drawable.arg, R.drawable.chile, R.drawable.uruguay, R.drawable.america);
    }
    private void show() {
        textSurface.reset();
       Presentacion.play(textSurface, getAssets());
        getPartidos();
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
                textSurface.setVisibility(View.GONE);
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return "";
            }
        }
        GetPartidos  gi = new GetPartidos ();
        gi.execute();
    }
}
