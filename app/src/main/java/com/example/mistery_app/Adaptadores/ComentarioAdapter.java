package com.example.mistery_app.Adaptadores;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
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
        Comentario comentarioActual = comentarios.get(position);
        Context context = holder.itemView.getContext();

        holder.comentario.setText(comentarioActual.getComentario());

        // Recuperar datos del usuario logueado desde SharedPreferences
        SharedPreferences prefs = context.getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        int miIdMysql = prefs.getInt("usuario_id_mysql", -1);
        String miNombre = prefs.getString("usuario_nombre", "");

        String nombreEnComentario = comentarioActual.getUsuarioNombre();

        // 🔍 LOG DE DEPURACIÓN PARA TI
        Log.d("DEBUG_COMENTARIO", "Comparando -> ID Comentario: " + comentarioActual.getUsuarioId() + " | Mi ID guardado: " + miIdMysql + " | Mi Nombre: " + miNombre);

        if (nombreEnComentario != null && !nombreEnComentario.isEmpty()) {
            // Si el servidor ya nos mandó el nombre, lo usamos
            holder.usuario.setText(nombreEnComentario);
        } else if (comentarioActual.getUsuarioId() == miIdMysql && !miNombre.isEmpty()) {
            // Si el ID coincide con el mío, ponemos mi nombre guardado
            holder.usuario.setText(miNombre);
        } else {
            // Si no, mostramos el ID como último recurso
            holder.usuario.setText("Usuario: " + comentarioActual.getUsuarioId());
        }
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