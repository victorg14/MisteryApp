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
import com.example.mistery_app.modelos.Insultos; // POJO importado
import com.example.mistery_app.modelos.OnComentarioAgregado;
import com.example.mistery_app.modelos.Publicacion;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    // 🎯 NUEVO: Lista donde guardaremos localmente los insultos prohibidos traídos de la API
    private List<String> palabrasProhibidas = new ArrayList<>();

    private String idiomaUsuario = "es";
    private String firebaseUid = "";
    private String nombreUsuarioLogueado = "";

    private String textoCambiado;

    public ComentariosFragment(Publicacion publicacion, OnComentarioAgregado listener) {
        this.publicacion = publicacion;
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comentarios, container, false);

        com.google.firebase.auth.FirebaseUser usuarioFirebase = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser();
        if (usuarioFirebase != null) {
            firebaseUid = usuarioFirebase.getUid();
        }

        // Conservamos las SharedPreferences solo para el idioma y el nombre
        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("ConfiguracionApp", Context.MODE_PRIVATE);
            idiomaUsuario = prefs.getString("idioma_seleccionado", "es");
            nombreUsuarioLogueado = prefs.getString("usuario_nombre", "Yo");
        }

        recyclerView = view.findViewById(R.id.rvComentarios);
        etComentario = view.findViewById(R.id.etComentario);
        btnEnviar = view.findViewById(R.id.btnEnviar);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new ComentarioAdapter(listaComentariosLocal);
        recyclerView.setAdapter(adapter);


        cargarInsultosDesdeApi();
        cargarComentariosDesdeApi();

        btnEnviar.setOnClickListener(v -> {
            String texto = etComentario.getText().toString().trim();

            if (!TextUtils.isEmpty(texto)) {
                if (TextUtils.isEmpty(firebaseUid)) {
                    Toast.makeText(getContext(), "Error: Sesión no válida", Toast.LENGTH_SHORT).show();
                    return;
                }


                btnEnviar.setEnabled(false);
                btnEnviar.setText("Publicando...");

                String textoFiltrado = censurarTextoRiguroso(texto);

                enviarComentarioAApi(textoFiltrado);
            }
        });

        return view;
    }


    // 🎯 Reemplaza estos métodos en tu ComentariosFragment.java

    private void cargarInsultosDesdeApi() {
        RetrofitClient.getApiService().getInsultosPorIdioma(idiomaUsuario)
                .enqueue(new Callback<Insultos>() {
                    @Override
                    public void onResponse(@NonNull Call<Insultos> call, @NonNull Response<Insultos> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Insultos datos = response.body();
                            if (datos.getInsultos() != null) {
                                palabrasProhibidas.clear();
                                // Pasamos a minúsculas preventivamente para evitar fallos de case-sensitivity
                                for (String insulto : datos.getInsultos()) {
                                    palabrasProhibidas.add(insulto.trim().toLowerCase());
                                }
                                for (String insulto : palabrasProhibidas) {
                                    Log.d("ANTI_PROFANIDAD", "Insulto cargado: " + insulto);
                                }
                                Log.d("ANTI_PROFANIDAD", "DICCIONARIO DE INSULTOS CARGADO: " + palabrasProhibidas.size() + " palabras.");
                            }
                        } else {
                            try {
                                Log.e("ANTI_PROFANIDAD", "Error " + response.code() + " -> " + response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Insultos> call, @NonNull Throwable t) {
                        Log.e("ANTI_PROFANIDAD", "Falló la conexión al traer insultos: " + t.getMessage());
                    }
                });
    }

    private String censurarTextoRiguroso(String textoOriginal) {
        if (palabrasProhibidas == null || palabrasProhibidas.isEmpty()) {
            Log.d("ANTI_PROFANIDAD", "Alerta: El filtro se ejecutó vacío. No hay palabras cargadas.");
            return textoOriginal;
        }

        String textoProcesado = textoOriginal;

        // Mapa robusto de equivalencias Leetspeak
        Map<Character, String> mutaciones = new HashMap<>();
        mutaciones.put('a', "[a44@áÀâä]");
        mutaciones.put('e', "[e33éÈêë]");
        mutaciones.put('i', "[i11!|íÌîï]");
        mutaciones.put('o', "[o00óÒôö]");
        mutaciones.put('u', "[uovvúÙûü]");
        mutaciones.put('c', "[cKk]");
        mutaciones.put('s', "[s5$zZ]");
        mutaciones.put('t', "[t77]");
        mutaciones.put('b', "[b88]");

        for (String insulto : palabrasProhibidas) {
            if (insulto.isEmpty()) continue;

            StringBuilder regexBuilder = new StringBuilder();

            // Usamos modificador (?i) para ignorar mayúsculas/minúsculas de forma nativa
            regexBuilder.append("(?i)");

            for (int i = 0; i < insulto.length(); i++) {
                char letra = insulto.charAt(i);
                if (mutaciones.containsKey(letra)) {
                    regexBuilder.append(mutaciones.get(letra));
                } else {
                    regexBuilder.append(Pattern.quote(String.valueOf(letra)));
                }

                // [\\s_]* permite espacios, guiones bajos o saltos de línea intermedios entre caracteres
                if (i < insulto.length() - 1) {
                    regexBuilder.append("[\\s_]*");
                }
            }

            try {
                Pattern pattern = Pattern.compile(regexBuilder.toString());
                Matcher matcher = pattern.matcher(textoProcesado);

                StringBuffer sb = new StringBuffer();
                while (matcher.find()) {
                    String coincidencia = matcher.group();

                    // Generar dinámicamente el bloque de asteriscos exacto
                    StringBuilder asteriscos = new StringBuilder();
                    for (int j = 0; j < coincidencia.length(); j++) {
                        asteriscos.append("*");
                    }
                    matcher.appendReplacement(sb, asteriscos.toString());
                }
                matcher.appendTail(sb);
                textoProcesado = sb.toString();

                textoCambiado = textoProcesado;

                Log.d("ANTI_PROFANIDAD", "Texto procesado: " + textoCambiado);


            } catch (Exception e) {
                Log.e("ANTI_PROFANIDAD", "Error procesando RegEx para: " + insulto, e);
            }
        }

        Log.d("ANTI_PROFANIDAD", "Texto procesado: " + textoCambiado);
        return textoProcesado;
    }

    private void cargarComentariosDesdeApi() {
        RetrofitClient.getApiService().getComentariosDePublicacion(publicacion.getId(), idiomaUsuario)
                .enqueue(new Callback<List<Comentario>>() {
                    @Override
                    public void onResponse(@NonNull Call<List<Comentario>> call, @NonNull Response<List<Comentario>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            listaComentariosLocal.clear();
                            listaComentariosLocal.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<List<Comentario>> call, @NonNull Throwable t) {
                        Log.e("API_COMMENTS", "Error de red: " + t.getMessage());
                    }
                });
    }

    private void enviarComentarioAApi(String textoComentario) {
        Comentario nuevoComentario = new Comentario();
        nuevoComentario.setPublicacionId(publicacion.getId());
        nuevoComentario.setComentario(textoComentario);

        RetrofitClient.getApiService().crearComentario(nuevoComentario, idiomaUsuario)
                .enqueue(new Callback<Comentario>() {
                    @Override
                    public void onResponse(@NonNull Call<Comentario> call, @NonNull Response<Comentario> response) {
                        btnEnviar.setEnabled(true);
                        btnEnviar.setText("Enviar");

                        if (response.isSuccessful() && response.body() != null) {
                            Comentario comentarioGuardado = response.body();

                            if (comentarioGuardado.getUsuarioNombre() == null || comentarioGuardado.getUsuarioNombre().isEmpty()) {
                                comentarioGuardado.setUsuarioNombre(nombreUsuarioLogueado);
                            }

                            listaComentariosLocal.add(comentarioGuardado);
                            int posicion = listaComentariosLocal.size() - 1;

                            adapter.notifyItemInserted(posicion);
                            recyclerView.scrollToPosition(posicion);

                            if (listener != null) {
                                listener.actualizar();
                            }

                            etComentario.setText("");
                        } else {
                            Toast.makeText(getContext(), "Error al enviar el comentario", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<Comentario> call, @NonNull Throwable t) {
                        btnEnviar.setEnabled(true);
                        btnEnviar.setText("Enviar");
                        Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}