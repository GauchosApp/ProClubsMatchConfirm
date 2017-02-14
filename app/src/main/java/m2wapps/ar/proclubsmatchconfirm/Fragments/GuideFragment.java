package m2wapps.ar.proclubsmatchconfirm.Fragments;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import m2wapps.ar.proclubsmatchconfirm.Activities.MainActivity;
import m2wapps.ar.proclubsmatchconfirm.Activities.StartActivity;
import m2wapps.ar.proclubsmatchconfirm.Analytics;
import m2wapps.ar.proclubsmatchconfirm.R;

/**
 * Created by mariano on 04/09/2016.
 */
public class GuideFragment extends Fragment {

    private int bgRes;
    private Tracker mTracker;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bgRes = getArguments().getInt("data");
        Analytics application = (Analytics) getActivity().getApplication();
        mTracker = application.getDefaultTracker();
        mTracker.setScreenName("Inicio~" + "Home");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        imageView.setBackgroundResource(bgRes);
        final View finalView = view;
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bgRes == R.drawable.arg){
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Liga")
                            .setAction("Argentina")
                            .build());
                  //  Toast.makeText(finalView.getContext(),"Proximamente",Toast.LENGTH_SHORT).show();
                    StartActivity.sLigaCache.put(0,0);
                    final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.buton);
                    mp.start();
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(myIntent);
                }else if (bgRes == R.drawable.chile){
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Liga")
                            .setAction("Chile")
                            .build());
                    StartActivity.sLigaCache.put(0,1);
                    final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.buton);
                    mp.start();
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(myIntent);
                }else if(bgRes == R.drawable.uruguay){
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Liga")
                            .setAction("Uruguay")
                            .build());
                    StartActivity.sLigaCache.put(0,2);
                    final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.buton);
                    mp.start();
                    Intent myIntent = new Intent(getActivity(), MainActivity.class);
                    startActivity(myIntent);
                 //   Toast.makeText(finalView.getContext(),"Proximamente",Toast.LENGTH_SHORT).show();
                }else{
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Liga")
                            .setAction("America")
                            .build());
                      StartActivity.sLigaCache.put(0,3);
                      final MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.buton);
                      mp.start();
                      Intent myIntent = new Intent(getActivity(), MainActivity.class);
                      startActivity(myIntent);
                   // Toast.makeText(finalView.getContext(),"Proximamente",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
