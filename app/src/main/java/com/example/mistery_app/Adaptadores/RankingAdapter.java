package com.example.mistery_app.Adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mistery_app.R;
import com.example.mistery_app.modelos.RankingUser;
import java.util.Locale;
import java.util.List;

public class RankingAdapter extends RecyclerView.Adapter<RankingAdapter.ViewHolder> {

    private List<RankingUser> listaRanking;

    public RankingAdapter(List<RankingUser> listaRanking) {
        this.listaRanking = listaRanking;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RankingUser user = listaRanking.get(position);
        int puesto = position + 1;

        // 1. Asignar el puesto en el ranking
        holder.txtPuesto.setText(String.format(Locale.getDefault(), "#%d", puesto));

        // 2. Asignar nombre del usuario
        holder.txtNombre.setText(user.getNombre());

        // 3. Asignar cantidad de misterios resueltos (basado en el alias de Laravel)
        holder.txtResueltos.setText(String.format(Locale.getDefault(), "Misterios Completos: %d", user.getMisteriosResueltos()));

        // 4. Asignar el promedio de eficiencia formateado a dos decimales
        holder.txtPorcentaje.setText(String.format(Locale.getDefault(), "%.2f%%", user.getPromedioEficiencia()));

        // Imagen por defecto por ahora
        holder.imgPerfil.setImageResource(R.drawable.img1);

        // 5. Detalle estético: Resaltar los colores del podio de forma segura
        switch (puesto) {
            case 1:
                holder.txtPuesto.setTextColor(Color.parseColor("#FFD700")); // Oro
                break;
            case 2:
                holder.txtPuesto.setTextColor(Color.parseColor("#C0C0C0")); // Plata
                break;
            case 3:
                holder.txtPuesto.setTextColor(Color.parseColor("#CD7F32")); // Bronce
                break;
            default:
                holder.txtPuesto.setTextColor(Color.GRAY); // Un color neutro para el resto
                break;
        }
    }

    @Override
    public int getItemCount() {
        return listaRanking != null ? listaRanking.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtPuesto, txtNombre, txtResueltos, txtPorcentaje;
        ImageView imgPerfil;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPuesto = itemView.findViewById(R.id.txtPuesto);

            // Asegúrate de que estos IDs existan exactamente así en tu archivo layout 'item_ranking.xml'
            txtNombre = itemView.findViewById(R.id.txtNombreRanking);
            txtResueltos = itemView.findViewById(R.id.txtResueltosRanking);
            txtPorcentaje = itemView.findViewById(R.id.txtPorcentajeRanking);
            imgPerfil = itemView.findViewById(R.id.imgPerfilRanking);
        }
    }
}