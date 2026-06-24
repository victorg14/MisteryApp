package com.example.mistery_app; // O el paquete com.example.mistery_app si lo prefieres

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mistery_app.MainActivity;
import com.example.mistery_app.R;
import com.example.mistery_app.auth.AuthManager; // Si usas tu wrapper de AuthManager
import java.util.Locale;

import com.example.mistery_app.ui.EditProfileActivity;
import com.example.mistery_app.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class PerfilFragment extends Fragment {

    // Variables de control y callbacks
    private Runnable onClose;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "ConfiguracionApp";
    private static final String KEY_IDIOMA = "idioma_seleccionado";

    // Componentes de la interfaz (UI)
    private ImageView ivProfilePic;
    private TextView tvUserName, tvUserEmail;
    private Spinner spinnerIdiomas;

    // Servicios de Firebase
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // Constructores obligatorios
    public PerfilFragment() {
        // Constructor vacío requerido por Android para recrear el fragment al girar pantalla
    }

    public PerfilFragment(Runnable onClose) {
        this.onClose = onClose;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el Layout común "fragment_perfil"
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        // Inicializamos Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // Enlazamos las vistas comunes y de usuario
        ivProfilePic = view.findViewById(R.id.ivProfilePic);
        tvUserName = view.findViewById(R.id.tvUserName);
        tvUserEmail = view.findViewById(R.id.tvUserEmail);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Enlazamos las vistas restantes
        spinnerIdiomas = view.findViewById(R.id.spinnerIdiomas);
        TextView btnCerrar = view.findViewById(R.id.btnCerrar);
        Button btnLogout = view.findViewById(R.id.btnLogout);
        Button btnEditProfile = view.findViewById(R.id.btnEditProfile);

        // Cargar datos de Firebase Firestore
        loadUserData();

        // --- Configuración de los Listeners ---

        if (btnCerrar != null) {
            btnCerrar.setOnClickListener(v -> {
                if (onClose != null) {
                    onClose.run();
                }
            });
        }

        if (btnEditProfile != null) {
            btnEditProfile.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), EditProfileActivity.class);
                startActivity(intent);
            });
        }

        if (btnLogout != null) {
            btnLogout.setOnClickListener(v -> {
                mAuth.signOut();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            });
        }

        // --- Lógica de SharedPreferences y Cambio de Idioma ---

        sharedPreferences = requireActivity().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        String[] nombresIdiomas = {"Español", "English", "Français", "Português"};
        String[] codigosIdiomas = {"es", "en", "fr", "pt"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_spinner_item, nombresIdiomas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerIdiomas.setAdapter(adapter);

        Log.d("SPINNER", "Items de idioma: " + adapter.getCount());

        // Pre-seleccionar el idioma guardado actual
        String idiomaActual = sharedPreferences.getString(KEY_IDIOMA, Locale.getDefault().getLanguage());
        int posicion = 0;
        for (int i = 0; i < codigosIdiomas.length; i++) {
            if (codigosIdiomas[i].equals(idiomaActual)) {
                posicion = i;
                break;
            }
        }
        spinnerIdiomas.setSelection(posicion, false);

        // Listener para la interacción con el Spinner
        spinnerIdiomas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String nuevoCodigo = codigosIdiomas[position];
                String guardado = sharedPreferences.getString(KEY_IDIOMA, "");

                if (!nuevoCodigo.equals(guardado)) {
                    cambiarIdioma(nuevoCodigo);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
    }

    private void loadUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (tvUserEmail != null) {
                tvUserEmail.setText(user.getEmail());
            }

            db.collection("users").document(user.getUid())
                    .addSnapshotListener((documentSnapshot, e) -> {
                        if (e != null) {
                            Log.e("FIRESTORE", "Error cargando perfil", e);
                            return;
                        }
                        // Validamos que el fragmento siga adjunto antes de actualizar la UI
                        if (isAdded() && documentSnapshot != null && documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            String photoUrl = documentSnapshot.getString("photoUrl");

                            if (tvUserName != null) {
                                tvUserName.setText(name != null ? name : "Sin nombre");
                            }

                            if (photoUrl != null && !photoUrl.isEmpty() && ivProfilePic != null) {
                                Glide.with(this)
                                        .load(photoUrl)
                                        .placeholder(R.drawable.img2)
                                        .into(ivProfilePic);
                            }
                        }
                    });
        }
    }

    private void cambiarIdioma(String codigo) {
        // Guardar la elección en almacenamiento local
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_IDIOMA, codigo);
        editor.apply();

        // Actualizar la localización del sistema en la App
        Locale locale = new Locale(codigo);
        Locale.setDefault(locale);
        Resources res = getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        res.updateConfiguration(config, res.getDisplayMetrics());

        // Reiniciar la actividad para re-renderizar los textos en el nuevo idioma
        requireActivity().recreate();
    }
}