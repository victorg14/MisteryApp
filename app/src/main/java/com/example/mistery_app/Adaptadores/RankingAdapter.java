package com.example.mistery_app.Adaptadores;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide; // 👈 Asegúrate de tener la dependencia de Glide
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

        //Asignar el puesto en el ranking
        holder.txtPuesto.setText(String.format(Locale.getDefault(), "#%d", puesto));

        //Asignar nombre del usuario
        holder.txtNombre.setText(user.getNombre());

        // Asignar cantidad de misterios resueltos
        holder.txtResueltos.setText(String.format(Locale.getDefault(), "Misterios Completos: %d", user.getMisteriosResueltos()));

        //Asignar el promedio de eficiencia formateado a dos decimales
        holder.txtPorcentaje.setText(String.format(Locale.getDefault(), "%.2f%%", user.getPromedioEficiencia()));

        //CARGAR IMAGEN DE PERFIL ASÍNCRONA CON GLIDE

        String photoUrl = user.getFoto();

        Glide.with(holder.itemView.getContext())
                .load(photoUrl != null && !photoUrl.isEmpty() ? photoUrl : R.drawable.img1) // Si no hay URL, usa la de por defecto
                .circleCrop()
                .placeholder(R.drawable.img1)
                .error(R.drawable.img1)
                .into(holder.imgPerfil);

        //Detalle estético: Resaltar los colores del podio de forma segura
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
            txtNombre = itemView.findViewById(R.id.txtNombreRanking);
            txtResueltos = itemView.findViewById(R.id.txtResueltosRanking);
            txtPorcentaje = itemView.findViewById(R.id.txtPorcentajeRanking);
            imgPerfil = itemView.findViewById(R.id.imgPerfilRanking);
        }
    }
}