package com.example.mistery_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mistery_app.ApiService.RetrofitClient;
import com.example.mistery_app.modelos.Misterio;
import com.example.mistery_app.modelos.Pregunta;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewMisteryFragment extends Fragment {

    private Uri imagenSeleccionadaUri;
    private ImageView ivPreview;

    private LinearLayout contenedorPreguntas;
    private EditText etDescripcion, etTitulo;
    private ScrollView scrollView;

    private Button btnAgregarPregunta, btnSeleccionarImagen;
    private Button btnGuardarMisterio;

    public List<Misterio> listamisterios = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_mistery, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etDescripcion = view.findViewById(R.id.etDescripcion);
        etTitulo = view.findViewById(R.id.etTitulo);
        contenedorPreguntas = view.findViewById(R.id.contenedorPreguntas);
        btnAgregarPregunta = view.findViewById(R.id.btnAgregarPregunta);
        btnGuardarMisterio = view.findViewById(R.id.btnGuardarMisterio);
        scrollView = view.findViewById(R.id.scrollMisterio);
        ivPreview = view.findViewById(R.id.imgMisterio);
        btnSeleccionarImagen = view.findViewById(R.id.btnSeleccionarImagen);

        btnSeleccionarImagen.setOnClickListener(v -> abrirGaleria());

        btnAgregarPregunta.setOnClickListener(v -> {
            agregarPregunta();
            scrollView.post(() -> scrollView.fullScroll(View.FOCUS_DOWN));
        });

        btnGuardarMisterio.setOnClickListener(v -> guardarMisterio());
    }

    private void abrirGaleria() {
        imagePicker.launch("image/*");
    }

    private final ActivityResultLauncher<String> imagePicker =
            registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) {
                    imagenSeleccionadaUri = uri;
                    ivPreview.setImageURI(uri);
                    Toast.makeText(getContext(), "Imagen seleccionada", Toast.LENGTH_SHORT).show();
                }
            });

    private String getImageString() {
        return (imagenSeleccionadaUri != null) ? imagenSeleccionadaUri.toString() : null;
    }

    private void agregarPregunta() {
        View v = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_pregunta, contenedorPreguntas, false);

        EditText op1 = v.findViewById(R.id.etOpcion1);
        EditText op2 = v.findViewById(R.id.etOpcion2);
        EditText op3 = v.findViewById(R.id.etOpcion3);
        EditText op4 = v.findViewById(R.id.etOpcion4);

        Spinner spinner = v.findViewById(R.id.spRespuestaCorrecta);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new ArrayList<>()
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        TextWatcher watcher = new SimpleTextWatcher(() -> {
            ArrayList<String> opciones = new ArrayList<>();

            if (op1 != null && op1.getText() != null && !op1.getText().toString().trim().isEmpty())
                opciones.add(op1.getText().toString().trim());

            if (op2 != null && op2.getText() != null && !op2.getText().toString().trim().isEmpty())
                opciones.add(op2.getText().toString().trim());

            if (op3 != null && op3.getText() != null && !op3.getText().toString().trim().isEmpty())
                opciones.add(op3.getText().toString().trim());

            if (op4 != null && op4.getText() != null && !op4.getText().toString().trim().isEmpty())
                opciones.add(op4.getText().toString().trim());

            adapter.clear();
            adapter.addAll(opciones);
            adapter.notifyDataSetChanged();
        });

        op1.addTextChangedListener(watcher);
        op2.addTextChangedListener(watcher);
        op3.addTextChangedListener(watcher);
        op4.addTextChangedListener(watcher);

        Button btnEliminar = v.findViewById(R.id.btnEliminarPregunta);
        btnEliminar.setOnClickListener(view -> {
            new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar pregunta")
                    .setMessage("¿Seguro que quieres eliminar esta pregunta?")
                    .setPositiveButton("Sí", (dialog, which) -> contenedorPreguntas.removeView(v))
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        contenedorPreguntas.addView(v);
    }

    public class SimpleTextWatcher implements TextWatcher {
        private Runnable onChange;
        public SimpleTextWatcher(Runnable onChange) { this.onChange = onChange; }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {} // <-- Aquí faltaba el 'int' antes de start

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) { onChange.run(); }

        @Override
        public void afterTextChanged(Editable s) {}
    }

    private void guardarMisterio() {
        String descripcion = etDescripcion.getText().toString().trim();
        if (descripcion.isEmpty()) {
            Toast.makeText(getContext(), "Escribe una descripción", Toast.LENGTH_SHORT).show();
            return;
        }

        String titulo = etTitulo.getText().toString().trim();
        if (titulo.isEmpty()) {
            Toast.makeText(getContext(), "Escribe un título", Toast.LENGTH_SHORT).show();
            return;
        }

        if (contenedorPreguntas.getChildCount() == 0) {
            Toast.makeText(getContext(), "Agrega al menos una pregunta", Toast.LENGTH_SHORT).show();
            return;
        }

        ArrayList<Pregunta> preguntas = new ArrayList<>();

        for (int i = 0; i < contenedorPreguntas.getChildCount(); i++) {
            View v = contenedorPreguntas.getChildAt(i);

            EditText etPregunta = v.findViewById(R.id.etPregunta);
            EditText op1 = v.findViewById(R.id.etOpcion1);
            EditText op2 = v.findViewById(R.id.etOpcion2);
            EditText op3 = v.findViewById(R.id.etOpcion3);
            EditText op4 = v.findViewById(R.id.etOpcion4);

            EditText p1 = v.findViewById(R.id.etPista1);
            EditText p2 = v.findViewById(R.id.etPista2);
            EditText p3 = v.findViewById(R.id.etPista3);

            Spinner spinner = v.findViewById(R.id.spRespuestaCorrecta);

            if (etPregunta.getText().toString().trim().isEmpty()) {
                Toast.makeText(getContext(), "Pregunta vacía en " + (i + 1), Toast.LENGTH_SHORT).show();
                return;
            }

            Pregunta pregunta = new Pregunta();
            pregunta.setPregunta(etPregunta.getText().toString().trim());
            pregunta.setOpcion1(op1.getText().toString().trim());
            pregunta.setOpcion2(op2.getText().toString().trim());
            pregunta.setOpcion3(op3.getText().toString().trim());
            pregunta.setOpcion4(op4.getText().toString().trim());

            pregunta.setRespuestaCorrecta(
                    spinner.getSelectedItem() != null ? spinner.getSelectedItem().toString() : ""
            );

            pregunta.setPista1(p1.getText().toString().trim());
            pregunta.setPista2(p2.getText().toString().trim());
            pregunta.setPista3(p3.getText().toString().trim());

            preguntas.add(pregunta);
        }

        // 🎯 LOGICA PARA RECUPERAR EL IDIOMA Y EL UID DE AUTH DE SHAREDPREFERENCES
        String idiomaUsuario = "es";
        String firebaseUid = "";

        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
            idiomaUsuario = prefs.getString("idioma_seleccionado", "es");

            // Recuperamos el UID de Firebase que guardaste al hacer Login/Registro
            // Asegúrate de usar la misma key que utilizaste para guardarlo en las otras actividades.
            firebaseUid = prefs.getString("firebase_uid", "");
        }

        // Alternativa directa si usas el SDK de Firebase en tu app:
        // if (com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser() != null) {
        //     firebaseUid = com.google.firebase.auth.FirebaseAuth.getInstance().getCurrentUser().getUid();
        // }

        Misterio misterio = new Misterio();
        misterio.setTitulo(titulo);
        misterio.setDescripcion(descripcion);
        misterio.setImagenUri(getImageString());
        misterio.setPreguntas(preguntas);
        misterio.setFirebaseUid(firebaseUid);

        // Enviamos el misterio con los datos actualizados
        RetrofitClient.getApiService()
                .crearMisterio(misterio, idiomaUsuario)
                .enqueue(new Callback<Misterio>() {
                    @Override
                    public void onResponse(Call<Misterio> call, Response<Misterio> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            listamisterios.add(response.body());
                            Toast.makeText(getContext(), "Misterio guardado correctamente", Toast.LENGTH_SHORT).show();
                            limpiarFormulario();
                        } else {
                            try {
                                Log.e("API_ERROR", "Error creando misterio " + response.code() + " -> " + response.errorBody().string());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Misterio> call, Throwable t) {
                        Log.e("API_ERROR", "Error de red: " + t.getMessage());
                    }
                });
    }

    private void limpiarFormulario() {
        etTitulo.setText("");
        etDescripcion.setText("");
        if (ivPreview != null) ivPreview.setImageResource(0);
        imagenSeleccionadaUri = null;
        contenedorPreguntas.removeAllViews();
    }
}