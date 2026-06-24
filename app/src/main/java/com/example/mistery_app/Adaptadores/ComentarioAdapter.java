package com.example.mistery_app.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mistery_app.R;
import com.example.mistery_app.modelos.Comentario;

import java.util.List;

public class ComentarioAdapter extends RecyclerView.Adapter<ComentarioAdapter.ViewHolder> {

    private List<Comentario> comentarios;

    public ComentarioAdapter(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    // Método útil para actualizar la lista de comentarios desde el Fragment
    public void setLista(List<Comentario> nuevosComentarios) {
        this.comentarios = nuevosComentarios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comentario, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // 🎯 Descomentamos y obtenemos el comentario de la posición actual
        Comentario comentarioActual = comentarios.get(position);

        // Pasamos el texto del comentario al TextView correspondiente
        holder.comentario.setText(comentarioActual.getComentario());

        holder.usuario.setText("Usuario ID: " + comentarioActual.getUsuarioId());
    }

    @Override
    public int getItemCount() {
        return comentarios == null ? 0 : comentarios.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView usuario;
        TextView comentario;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            usuario = itemView.findViewById(R.id.txtUsuario);
            comentario = itemView.findViewById(R.id.txtComentario);
        }
    }
}