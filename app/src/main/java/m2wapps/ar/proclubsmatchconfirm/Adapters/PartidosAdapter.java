package m2wapps.ar.proclubsmatchconfirm.Adapters;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import m2wapps.ar.proclubsmatchconfirm.Activities.ListaActivity;
import m2wapps.ar.proclubsmatchconfirm.Activities.MainActivity;
import m2wapps.ar.proclubsmatchconfirm.Activities.PartidoActivity;
import m2wapps.ar.proclubsmatchconfirm.Partido;
import m2wapps.ar.proclubsmatchconfirm.R;

/**
 * Created by mariano on 19/08/2016.
 */
public class PartidosAdapter extends RecyclerView.Adapter<PartidosAdapter.MyViewHolder> {

    private List<Partido> partidosList;
    private ListaActivity m;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView hora;
        public ImageView equipo1, equipo2;
        public Button entrar;
        public MyViewHolder(View view) {
            super(view);
            hora = (TextView) view.findViewById(R.id.hora);
            equipo1 = (ImageView) view.findViewById(R.id.equipo1);
            equipo2 = (ImageView) view.findViewById(R.id.equipo2);
            entrar = (Button) view.findViewById(R.id.button);
        }
    }


    public PartidosAdapter(List<Partido> moviesList, ListaActivity m) {
        this.partidosList = moviesList;
        this.m = m;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.partidos_list_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Partido partido = partidosList.get(position);
        holder.hora.setText(partido.getYear());
        holder.equipo1.setImageResource(partido.getEquipo1());
        holder.equipo2.setImageResource(partido.getEquipo2());
        holder.entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(m, PartidoActivity.class);
                final MediaPlayer mp = MediaPlayer.create(m, R.raw.buton);
                mp.start();
                MainActivity.sIdCache.put(0,position);
                MainActivity.sEquipo1ImgCache.put(0,partido.getEquipo1());
                MainActivity.sEquipo2ImgCache.put(0,partido.getEquipo2());
                MainActivity.sHoraCache.put(0,partido.getYear());
                MainActivity.sEstado1Cache.put(0,m.auxEstado1.get(position));
                MainActivity.sEstado2Cache.put(0,m.auxEstado2.get(position));
                MainActivity.sEquipo1Cache.put(0,m.auxEquipo1.get(position));
                MainActivity.sEquipo2Cache.put(0,m.auxEquipo2.get(position));
                m.startActivity(myIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return partidosList.size();
    }
}
