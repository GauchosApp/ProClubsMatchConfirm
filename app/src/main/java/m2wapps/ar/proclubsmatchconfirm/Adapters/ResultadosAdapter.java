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
import m2wapps.ar.proclubsmatchconfirm.Activities.StartActivity;
import m2wapps.ar.proclubsmatchconfirm.R;

/**
 * Created by mariano on 02/09/2016.
 */
public class ResultadosAdapter extends RecyclerView.Adapter<ResultadosAdapter.MyViewHolder> {

    private final List<String> escudo1, escudo2;
    private final int aux;
    private List<String> resultadoList;
    private int numero;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView resultado;
        public ImageView equipo1, equipo2;
        public LinearLayout linear;
        public Context ctx;
        public MyViewHolder(View view) {
            super(view);
            linear = (LinearLayout) view.findViewById(R.id.linear);
            equipo1 = (ImageView) view.findViewById(R.id.equip1);
            equipo2 = (ImageView) view.findViewById(R.id.equip2);
            resultado = (TextView) view.findViewById(R.id.result);
            ctx = view.getContext();
        }
    }


    public ResultadosAdapter(List<String> resultadoList, List<String> escudo1, List<String> escudo2, int jornada, boolean AorB) {
        this.resultadoList = resultadoList;
        this.escudo1 = escudo1;
        this.escudo2 = escudo2;
        int ligue = StartActivity.sLigaCache.get(0);
        if (ligue == 0 ){
            numero = 5;
        }else if (ligue == 1){
            if (AorB) {
                numero = 6;
            }else {
                numero = 5;
            }
        }else {
            numero = 3;
        }
        aux = numero * jornada;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.resultados_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (position % 2 == 0){
            holder.linear.setBackgroundColor(MainActivity.sColorCache.get(0));
        }
        Picasso.with(holder.ctx)
                .load(escudo1.get(position))
                .fit().centerInside()
                .into(holder.equipo1, null);
        Picasso.with(holder.ctx)
                .load(escudo2.get(position))
                .fit().centerInside()
                .into(holder.equipo2, null);
//        holder.equipo1.setImageResource(escudo1.get(position+aux));
   //     holder.equipo2.setImageResource(escudo2.get(position+aux));
        holder.resultado.setText(resultadoList.get(position+aux));
       }

    @Override
    public int getItemCount() {
       return resultadoList.size();
    }
}
