package com.example.mistery_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mistery_app.Adaptadores.ComentarioAdapter;
import com.example.mistery_app.ApiService.RetrofitClient;
import com.example.mistery_app.modelos.Comentario;
import com.example.mistery_app.modelos.OnComentarioAgregado;
import com.example.mistery_app.modelos.Publicacion;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComentariosFragment extends BottomSheetDialogFragment {

    private Publicacion publicacion;
    private OnComentarioAgregado listener;

    private RecyclerView recyclerView;
    private EditText etComentario;
    private Button btnEnviar;

    private ComentarioAdapter adapter;
    private List<Comentario> listaComentariosLocal = new ArrayList<>();

    // 🎯 Variables globales de control para persistencia local e idioma
    private String idiomaUsuario = "es";
    private String firebaseUid = "";

    public ComentariosFragment(Publicacion publicacion, OnComentarioAgregado listener) {
        this.publicacion = publicacion;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comentarios, container, false);

        // 🎯 1. Recuperar idioma y Firebase UID desde SharedPreferences de forma segura
        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
            idiomaUsuario = prefs.getString("idioma_seleccionado", "en");
            firebaseUid = prefs.getString("firebase_uid", "");
        }

        recyclerView = view.findViewById(R.id.rvComentarios);
        etComentario = view.findViewById(R.id.etComentario);
        btnEnviar = view.findViewById(R.id.btnEnviar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ComentarioAdapter(listaComentariosLocal);
        recyclerView.setAdapter(adapter);

        // 🔄 Cargamos los comentarios pasando el código de idioma detectado
        cargarComentariosDesdeApi();

        // Acción al presionar enviar comentario
        btnEnviar.setOnClickListener(v -> {
            String texto = etComentario.getText().toString().trim();

            if (!TextUtils.isEmpty(texto)) {
                if (TextUtils.isEmpty(firebaseUid)) {
                    Toast.makeText(getContext(), "Error: Sesión no válida", Toast.LENGTH_SHORT).show();
                    return;
                }
                enviarComentarioAApi(texto);
            }
        });

        return view;
    }

    private void cargarComentariosDesdeApi() {
        RetrofitClient.getApiService().getComentariosDePublicacion(publicacion.getId(), idiomaUsuario)
                .enqueue(new Callback<List<Comentario>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Comentario>> call, @NonNull Response<List<Comentario>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Limpiamos e igualamos la lista con los datos frescos traducidos por Laravel
                            listaComentariosLocal.clear();
                            listaComentariosLocal.addAll(response.body());

                            // Notificamos al adaptador que ya llegaron los comentarios reales
                            adapter.notifyDataSetChanged();
                            Log.d("API_COMMENTS", "Comentarios cargados en idioma [" + idiomaUsuario + "]: " + listaComentariosLocal.size());
                        }else {
                            try {
                                Log.e("API_comentarios", "Error " + response.code() + " -> " + response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Comentario>> call, @NonNull Throwable t) {
                        Log.e("API_COMMENTS", "Error de red al consultar tabla de comentarios: " + t.getMessage());
                    }
                });
    }

    private void enviarComentarioAApi(String textoComentario) {
        // Creamos el objeto comentario estructurado para enviar al Backend
        Comentario nuevoComentario = new Comentario();
        nuevoComentario.setPublicacionId(publicacion.getId());
        nuevoComentario.setComentario(textoComentario);

        // 🎯 Enviamos el comentario pasando dinámicamente 'firebaseUid' e 'idiomaUsuario' en los headers
        RetrofitClient.getApiService().crearComentario(nuevoComentario,idiomaUsuario)
                .enqueue(new Callback<Comentario>() {
                    @Override
                    public void onResponse(@NonNull Call<Comentario> call, @NonNull Response<Comentario> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            // Recibimos el comentario registrado (ya contiene su ID autoincremental de Laravel)
                            Comentario comentarioGuardado = response.body();

                            // Lo insertamos dinámicamente en el RecyclerView para mantener la fluidez
                            listaComentariosLocal.add(comentarioGuardado);
                            int posicion = listaComentariosLocal.size() - 1;

                            adapter.notifyItemInserted(posicion);
                            recyclerView.scrollToPosition(posicion);

                            // Avisamos al HomeFragment que incremente el número del contador visual de comentarios
                            if (listener != null) {
                                listener.actualizar();
                            }

                            etComentario.setText("");
                        } else {
                            try {
                                String errorMsg = response.errorBody().string();
                                Log.e("API_ERROR", "Error " + response.code() + " -> " + errorMsg);
                                Toast.makeText(getContext(), "Error servidor: " + errorMsg, Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getContext(), "Error al guardar el comentario", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Comentario> call, @NonNull Throwable t) {
                        Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 🎯 Permite inyectar manualmente el UID de Firebase si se llama de forma externa.
     */
    public void traerusuario(String firebaseUid1) {
        this.firebaseUid = firebaseUid1;
    }
}