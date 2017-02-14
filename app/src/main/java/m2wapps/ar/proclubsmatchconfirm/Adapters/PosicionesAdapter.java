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
 * Created by mariano on 04/09/2016.
 */
public class PosicionesAdapter extends RecyclerView.Adapter<PosicionesAdapter.MyViewHolder> {

    private List<Tabla> posicionesList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView posicion, puntaje, ganados, empatados, perdidos, goles, partidos;
        public LinearLayout linear;
        public ImageView equipo;
        Context ctx;
        MyViewHolder(View view) {
            super(view);

            equipo = (ImageView) view.findViewById(R.id.equipo);
            posicion = (TextView) view.findViewById(R.id.pos);
            puntaje = (TextView) view.findViewById(R.id.puntaje);
            ganados = (TextView) view.findViewById(R.id.ganados);
            empatados = (TextView) view.findViewById(R.id.empatados);
            perdidos = (TextView) view.findViewById(R.id.perdidos);
            partidos = (TextView) view.findViewById(R.id.partidos);
            linear = (LinearLayout) view.findViewById(R.id.linear);
            goles = (TextView) view.findViewById(R.id.goles);
            ctx = view.getContext();
        }
    }
    public PosicionesAdapter(List<Tabla> posicionesList) {
        this.posicionesList = posicionesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.posiciones_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        Tabla tabla = posicionesList.get(position);
            if (tabla.isColor()) {
                holder.linear.setBackgroundColor(MainActivity.sColorCache.get(0));
            }
            holder.posicion.setText(String.valueOf(tabla.getPosicion()));
            holder.puntaje.setText(String.valueOf(tabla.getPuntaje()));
            holder.ganados.setText(String.valueOf(tabla.getGanados()));
            holder.empatados.setText(String.valueOf(tabla.getEmpatados()));
            holder.perdidos.setText(String.valueOf(tabla.getPerdidos()));
            holder.partidos.setText(String.valueOf(tabla.getPartidos()));
            if (tabla.getGoles() > 0) {
                holder.goles.setText(String.valueOf("+" + tabla.getGoles()));
            } else {
                holder.goles.setText(String.valueOf(tabla.getGoles()));
            }
            Picasso.with(holder.ctx)
                .load(tabla.getEquipo())
                .fit().centerInside()
                .into(holder.equipo, null);

    }

    @Override
    public int getItemCount() {
        return posicionesList.size();
    }
}

