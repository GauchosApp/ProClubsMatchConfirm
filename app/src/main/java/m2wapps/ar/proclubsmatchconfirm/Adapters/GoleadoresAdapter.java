package m2wapps.ar.proclubsmatchconfirm.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import m2wapps.ar.proclubsmatchconfirm.Activities.MainActivity;
import m2wapps.ar.proclubsmatchconfirm.R;
import m2wapps.ar.proclubsmatchconfirm.Tabla;

/**
 * Created by mariano on 07/09/2016.
 */
public class GoleadoresAdapter extends RecyclerView.Adapter<GoleadoresAdapter.MyViewHolder> {

    private List<Tabla> goleadoresList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView posicion, jugador, goles;
        public LinearLayout linear;
        public ImageView equipo;
        public Context ctx;
        public MyViewHolder(View view) {
            super(view);
            linear = (LinearLayout) view.findViewById(R.id.linear);
            equipo = (ImageView) view.findViewById(R.id.equipo);
            posicion = (TextView) view.findViewById(R.id.pos);
            jugador = (TextView) view.findViewById(R.id.puntaje);
            goles = (TextView) view.findViewById(R.id.goles);
            ctx = view.getContext();
        }
    }
    public GoleadoresAdapter(List<Tabla> goleadoresList) {
        this.goleadoresList = goleadoresList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.goleadores_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Tabla tabla = goleadoresList.get(position);
        if (position % 2 == 0){
            holder.linear.setBackgroundColor(MainActivity.sColorCache.get(0));
        }
        holder.posicion.setText(String.valueOf(tabla.getPosicion()));
        holder.jugador.setText(tabla.getJugador());
        Picasso.with(holder.ctx)
                .load(tabla.getEquipo())
                .fit().centerInside()
                .into(holder.equipo, null);
        holder.goles.setText(String.valueOf(tabla.getGoles()));
    }

    @Override
    public int getItemCount() {
        return goleadoresList.size();
    }
}
