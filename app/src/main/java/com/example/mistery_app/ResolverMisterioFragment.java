package com.example.mistery_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.mistery_app.ApiService.RetrofitClient;
import com.example.mistery_app.modelos.EstadoMisterioResponse;
import com.example.mistery_app.modelos.Pregunta;
import com.example.mistery_app.modelos.RespuestaSubmit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResolverMisterioFragment extends Fragment {

    private ArrayList<String> respuestasUsuario = new ArrayList<>();
    private TextView txtNumeroPregunta, txtPregunta, txtPista;

    private Button btnOp1, btnOp2, btnOp3, btnOp4;
    private Button btnAnterior, btnSiguiente, btnVerMisterio;

    private int misterioId;
    private int indiceActual = 0;
    private List<Pregunta> preguntas = new ArrayList<>();

    private String respuestaSeleccionada = "";

    private String idiomaUsuario = "es";
    private String firebaseUid = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            indiceActual = savedInstanceState.getInt("KEY_INDICE_ACTUAL", 0);
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resolver_misterio, container, false);

        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
            idiomaUsuario = prefs.getString("idioma_seleccionado", Locale.getDefault().getLanguage());
            firebaseUid = prefs.getString("firebase_uid", "");
        }

        txtNumeroPregunta = view.findViewById(R.id.txtProgreso);
        txtPregunta = view.findViewById(R.id.txtPregunta);
        txtPista = view.findViewById(R.id.txtPista);

        btnOp1 = view.findViewById(R.id.btnOpcion1);
        btnOp2 = view.findViewById(R.id.btnOpcion2);
        btnOp3 = view.findViewById(R.id.btnOpcion3);
        btnOp4 = view.findViewById(R.id.btnOpcion4);

        btnAnterior = view.findViewById(R.id.btnAnterior);
        btnSiguiente = view.findViewById(R.id.btnSiguiente);
        btnVerMisterio = view.findViewById(R.id.btnVerMisterio);

        btnVerMisterio.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("misterio_id", misterioId);
            bundle.putBoolean("ocultar_iniciar", true);

            ContenidoFragment fragment = new ContenidoFragment();
            fragment.setArguments(bundle);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .setCustomAnimations(
                            android.R.anim.slide_in_left,

                            android.R.anim.fade_out,

                            android.R.anim.fade_in,

                            android.R.anim.slide_out_right
                    )
                    .replace(R.id.containerMain, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        if (getArguments() != null) {
            misterioId = getArguments().getInt("misterio_id");
        }

        btnOp1.setOnClickListener(v -> seleccionarBotonOpcion(btnOp1));
        btnOp2.setOnClickListener(v -> seleccionarBotonOpcion(btnOp2));
        btnOp3.setOnClickListener(v -> seleccionarBotonOpcion(btnOp3));
        btnOp4.setOnClickListener(v -> seleccionarBotonOpcion(btnOp4));

        btnAnterior.setOnClickListener(v -> anteriorPregunta());
        btnSiguiente.setOnClickListener(v -> {
            if (respuestaSeleccionada.isEmpty()) {
                Toast.makeText(getContext(), R.string.error_seleccionar_opcion, Toast.LENGTH_SHORT).show();
                return;
            }

            if (indiceActual == preguntas.size() - 1) {
                guardarRespuestaActual();

                if (TextUtils.isEmpty(firebaseUid)) {
                    Toast.makeText(getContext(), R.string.error_sesion_invalida, Toast.LENGTH_SHORT).show();
                    return;
                }
                enviarResultadosAlServidor();
            } else {
                siguientePregunta();
            }
        });

        if (preguntas != null && !preguntas.isEmpty()) {
            mostrarPregunta(indiceActual);
        } else {
            if (!TextUtils.isEmpty(firebaseUid)) {
                recuperarProgresoDeLaravel();
            } else {
                Toast.makeText(getContext(), R.string.error_sesion_invalida, Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("KEY_INDICE_ACTUAL", indiceActual);
    }

    private void recuperarProgresoDeLaravel() {
        Log.d("DEBUG_SINCRO", "UID que se va a enviar: '" + firebaseUid + "'");
        Log.d("DEBUG_SINCRO", "Idioma que se va a enviar: '" + idiomaUsuario + "'");
        RetrofitClient.getApiService().obtenerEstadoInicial(idiomaUsuario, misterioId)
                .enqueue(new Callback<EstadoMisterioResponse>() {
                    @Override
                    public void onResponse(Call<EstadoMisterioResponse> call, Response<EstadoMisterioResponse> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<Pregunta> todasLasPreguntas = response.body().getPreguntas();
                            preguntas = new ArrayList<>();

                            for (Pregunta p : todasLasPreguntas) {
                                if (p.getEstadoPrevio() == null || !p.getEstadoPrevio().equalsIgnoreCase("correcta")) {
                                    preguntas.add(p);
                                }
                            }

                            respuestasUsuario.clear();
                            for (int i = 0; i < preguntas.size(); i++) {
                                respuestasUsuario.add("");
                            }

                            if (preguntas.isEmpty()) {
                                Toast.makeText(getContext(), R.string.misterio_completado, Toast.LENGTH_SHORT).show();
                                requireActivity().getSupportFragmentManager().popBackStack();
                                return;
                            }

                            if (indiceActual >= preguntas.size()) {
                                indiceActual = 0;
                            }
                            mostrarPregunta(indiceActual);
                        } else {
                            try {
                                Log.e("API_ERROR", "Error inicializando estado: " + response.code() + " -> " + response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<EstadoMisterioResponse> call, Throwable t) {
                        Toast.makeText(getContext(), R.string.error_red, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void mostrarPregunta(int posicion) {
        if (preguntas == null || preguntas.isEmpty() || posicion >= preguntas.size()) return;

        Pregunta pregunta = preguntas.get(posicion);

        if (isAdded() && getContext() != null) {
            String formatoProgreso = getString(R.string.formato_progreso_pregunta);
            txtNumeroPregunta.setText(String.format(formatoProgreso, (posicion + 1), preguntas.size()));
        }

        txtPregunta.setText(pregunta.getPregunta());

        btnOp1.setText(pregunta.getOpcion1());
        btnOp2.setText(pregunta.getOpcion2());
        btnOp3.setText(pregunta.getOpcion3());
        btnOp4.setText(pregunta.getOpcion4());


        String pistaTraduccion = pregunta.getPistaActual();

        // Comprobación de seguridad en caso de que en tu modelo 'Pregunta' existan métodos específicos de traducción:
        // Si tu JSON entrega la traducción según métodos alternativos, descomenta o adapta las líneas de abajo:
        /*
        if (idiomaUsuario.equalsIgnoreCase("en")) {
            pistaTraduccion = pregunta.getPistaEn(); // O el método que equivalga a la pista en inglés
        }
        */

        if (pistaTraduccion != null && !pistaTraduccion.isEmpty()) {
            txtPista.setVisibility(View.VISIBLE);
            if (isAdded() && getContext() != null) {
                String formatoPista = getString(R.string.formato_pista);
                txtPista.setText(String.format(formatoPista, pistaTraduccion));
            }
        } else {
            txtPista.setVisibility(View.GONE);
        }

        restablecerColoresBotones();

        respuestaSeleccionada = respuestasUsuario.get(posicion);
        if (!respuestaSeleccionada.isEmpty()) {
            if (respuestaSeleccionada.equals(btnOp1.getText().toString())) marcarVisualmente(btnOp1);
            else if (respuestaSeleccionada.equals(btnOp2.getText().toString())) marcarVisualmente(btnOp2);
            else if (respuestaSeleccionada.equals(btnOp3.getText().toString())) marcarVisualmente(btnOp3);
            else if (respuestaSeleccionada.equals(btnOp4.getText().toString())) marcarVisualmente(btnOp4);
        }

        btnAnterior.setEnabled(posicion != 0);

        if (isAdded() && getContext() != null) {
            btnSiguiente.setText(posicion == preguntas.size() - 1 ? getString(R.string.finalizar) : getString(R.string.siguiente));
        }
    }

    private void seleccionarBotonOpcion(Button botonSeleccionado) {
        restablecerColoresBotones();
        marcarVisualmente(botonSeleccionado);
        respuestaSeleccionada = botonSeleccionado.getText().toString();
    }

    private void marcarVisualmente(Button btn) {
        btn.setBackgroundColor(Color.parseColor("#2E7D32"));
        btn.setTextColor(Color.WHITE);
    }

    private void restablecerColoresBotones() {
        String colorNeutro = "#E0E0E0";
        btnOp1.setBackgroundColor(Color.parseColor(colorNeutro));
        btnOp1.setTextColor(Color.BLACK);
        btnOp2.setBackgroundColor(Color.parseColor(colorNeutro));
        btnOp2.setTextColor(Color.BLACK);
        btnOp3.setBackgroundColor(Color.parseColor(colorNeutro));
        btnOp3.setTextColor(Color.BLACK);
        btnOp4.setBackgroundColor(Color.parseColor(colorNeutro));
        btnOp4.setTextColor(Color.BLACK);
        respuestaSeleccionada = "";
    }

    private void siguientePregunta() {
        guardarRespuestaActual();
        if (indiceActual < preguntas.size() - 1) {
            indiceActual++;
            mostrarPregunta(indiceActual);
        }
    }

    private void anteriorPregunta() {
        if (indiceActual > 0) {
            indiceActual--;
            mostrarPregunta(indiceActual);
        }
    }

    private void guardarRespuestaActual() {
        if (indiceActual < respuestasUsuario.size()) {
            respuestasUsuario.set(indiceActual, respuestaSeleccionada);
        }
    }

    private void enviarResultadosAlServidor() {
        List<RespuestaSubmit> listaAEnviar = new ArrayList<>();

        for (int i = 0; i < preguntas.size(); i++) {
            Pregunta p = preguntas.get(i);
            String ansUser = respuestasUsuario.get(i);

            boolean isCorrect = ansUser.trim().equalsIgnoreCase(p.getRespuestaCorrecta().trim());
            listaAEnviar.add(new RespuestaSubmit(p.getId(), isCorrect));
        }

        Map<String, Object> body = new HashMap<>();
        body.put("misterio_id", misterioId);
        body.put("respuestas", listaAEnviar);

        RetrofitClient.getApiService().guardarProgreso(idiomaUsuario, body)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getContext(), R.string.sincronizacion_exitosa, Toast.LENGTH_LONG).show();
                            requireActivity().getSupportFragmentManager().popBackStack();
                        } else {
                            try {
                                Log.e("API_ERROR", "Error guardando progreso -> " + response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(getContext(), R.string.error_guardar_progreso, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}