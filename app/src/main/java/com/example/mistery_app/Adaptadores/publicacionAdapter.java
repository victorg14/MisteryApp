package com.example.mistery_app.Adaptadores;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast; // Importado para avisar si no hay sesión

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mistery_app.ApiService.RetrofitClient;
import com.example.mistery_app.ComentariosFragment;
import com.example.mistery_app.ContenidoFragment;
import com.example.mistery_app.R;
import com.example.mistery_app.modelos.Misterio;
import com.example.mistery_app.modelos.OnComentarioAgregado;
import com.example.mistery_app.modelos.Publicacion;
import com.google.firebase.auth.FirebaseAuth; // 🔥 Importación de Firebase Auth
import com.google.firebase.auth.FirebaseUser; // 🔥 Importación de Firebase User

import java.util.List;

public class publicacionAdapter extends RecyclerView.Adapter<publicacionAdapter.publicacionesViewHolder> {

    private List<Publicacion> data;
    private List<Misterio> dataMisterio;
    private boolean isLikedLocal = false;

    public publicacionAdapter(List<Publicacion> data, List<Misterio> dataMisterio) {
        this.data = data;
        this.dataMisterio = dataMisterio;
    }

    public void setLista(List<Publicacion> nuevaLista, List<Misterio> dataMisterio) {
        data = nuevaLista;
        this.dataMisterio = dataMisterio;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public publicacionesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_publicacion, parent, false);
        return new publicacionesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull publicacionesViewHolder holder, int position) {
        final Publicacion publi = data.get(position);
        final Misterio publiMist = buscarMisterioPorId(publi.getMisterioId());

        holder.progressMisterio.setProgress(publiMist.getPorcentajeReal());
        holder.txtPorcentajeProgreso.setText("Progreso: " + publiMist.getPorcentajeReal() + "%");
        holder.like.setText(String.valueOf(publi.getLikes()));
        holder.coment.setText(String.valueOf(publi.getComentariosCount()));

        holder.layoutcomment.setOnClickListener(v -> {
            ComentariosFragment fragment = new ComentariosFragment(publi, new OnComentarioAgregado() {
                @Override
                public void actualizar() {
                    publi.setComentariosCount(publi.getComentariosCount() + 1);
                    notifyItemChanged(holder.getAdapterPosition());
                }
            });
            fragment.show(((AppCompatActivity) v.getContext()).getSupportFragmentManager(), "comentarios");
        });

        holder.corazon.setText(isLikedLocal ? "❤️" : "🤍");

        if (publiMist != null) {
            String nombreImagen = publiMist.getImagenUri();
            int imageResId = holder.itemView.getContext()
                    .getResources()
                    .getIdentifier(nombreImagen, "drawable", holder.itemView.getContext().getPackageName());

            if (imageResId != 0) {
                holder.img.setImageResource(imageResId);
            } else {
                holder.img.setImageResource(R.drawable.img1);
            }
        } else {
            Log.e("ADAPTER", "No se encontró misterio con ID: " + publi.getMisterioId());
            holder.img.setImageResource(R.drawable.img1);
        }

        holder.img.setOnClickListener(v -> {
            Animation zoomIn = AnimationUtils.loadAnimation(v.getContext(), R.anim.zoom_in);
            v.startAnimation(zoomIn);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                Bundle bundle = new Bundle();
                bundle.putInt("misterio_id", publiMist.getId());
                bundle.putInt("publicacion_id", publi.getId());

                ContenidoFragment fragment = new ContenidoFragment();
                fragment.setArguments(bundle);

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.containerMain, fragment)
                        .addToBackStack(null)
                        .commit();
            }, 180);
        });

        if (publi.isLiked()) {
            holder.corazon.setText("❤️");
        } else {
            holder.corazon.setText("🤍");
        }

        holder.like.setText(String.valueOf(publi.getLikes()));

        // 🔘 Click en el contenedor de likes modificado
        holder.layoutLikes.setOnClickListener(view -> {

            // 🔥 1. Obtener el usuario actual de Firebase
            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            if (firebaseUser == null) {
                // Si no hay sesión iniciada, evitamos que interactúe y mande errores a la API
                Toast.makeText(view.getContext(), "Debes iniciar sesión para dar like", Toast.LENGTH_SHORT).show();
                return;
            }

            // 🔥 2. Extraer el UID (Es un String alfanumérico largo)
            String firebaseUid = firebaseUser.getUid();

            // Invertir estado actual en la UI de inmediato (Optimistic UI)
            boolean nuevoEstadoLike = !publi.isLiked();
            publi.setLiked(nuevoEstadoLike);

            int likesActuales = publi.getLikes();
            String accionApi;

            if (nuevoEstadoLike) {
                publi.setLikes(likesActuales + 1);
                holder.corazon.setText("❤️");
                accionApi = "guardar";
            } else {
                publi.setLikes(Math.max(0, likesActuales - 1));
                holder.corazon.setText("🤍");
                accionApi = "eliminar";
            }

            holder.like.setText(String.valueOf(publi.getLikes()));

            // Enviar a Retrofit enviando el String `firebaseUid`
            RetrofitClient.getApiService()
                    .toggleLike(
                            publi.getId(),
                            firebaseUid
                    )
                    .enqueue(new retrofit2.Callback<okhttp3.ResponseBody>() {
                        @Override
                        public void onResponse(
                                retrofit2.Call<okhttp3.ResponseBody> call,
                                retrofit2.Response<okhttp3.ResponseBody> response) {

                            if (response.isSuccessful()) {
                                Log.d("API_LIKE", "Like sincronizado. Acción: " + accionApi + " para usuario: " + firebaseUid);
                            } else {
                                try {
                                    Log.e("API_LIKE", "Error " + response.code() + " -> " + response.errorBody().string());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(retrofit2.Call<okhttp3.ResponseBody> call, Throwable t) {
                            Log.e("API_LIKE", "Fallo de conexión: " + t.getMessage());
                        }
                    });
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public static class publicacionesViewHolder extends RecyclerView.ViewHolder {
        TextView like, coment, corazon, txtPorcentajeProgreso;
        ImageView img;
        LinearLayout layoutcomment, layoutLikes;
        ProgressBar progressMisterio;

        public publicacionesViewHolder(@NonNull View itemView) {
            super(itemView);
            like = itemView.findViewById(R.id.txtLikes);
            coment = itemView.findViewById(R.id.txtComentarios);
            img = itemView.findViewById(R.id.imgPublicacion);
            layoutcomment = itemView.findViewById(R.id.layoutComentarios);
            corazon = itemView.findViewById(R.id.txtCorazon);
            layoutLikes = itemView.findViewById(R.id.LayoutLike);
            txtPorcentajeProgreso = itemView.findViewById(R.id.txtPorcentajeProgreso);
            progressMisterio = itemView.findViewById(R.id.progressMisterio);
        }
    }

    private Misterio buscarMisterioPorId(int misterioId) {
        if (dataMisterio == null) return null;
        for (Misterio misterio : dataMisterio) {
            if (misterio.getId() == misterioId) {
                return misterio;
            }
        }
        return null;
    }
}