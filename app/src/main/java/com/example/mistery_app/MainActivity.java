package com.example.mistery_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mistery_app.PerfilFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageView imgPerfil;
    private FrameLayout userPanelContainer;
    private View overlay;

    private BottomNavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String firebaseUid = "";

    // Constantes para la configuración del idioma persistente
    private static final String PREF_NAME = "ConfiguracionApp";
    private static final String KEY_IDIOMA = "idioma_seleccionado";

    @Override
    protected void attachBaseContext(Context newBase) {
        // 🛠️ Aplica el idioma guardado antes de que se infle la Activity para evitar parpadeos
        SharedPreferences prefs = newBase.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String idioma = prefs.getString(KEY_IDIOMA, Locale.getDefault().getLanguage());

        Locale locale = new Locale(idioma);
        Locale.setDefault(locale);

        Configuration config = new Configuration();
        config.setLocale(locale);

        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Inicializaciones de Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Enlace de vistas
        imgPerfil = findViewById(R.id.imgPerfil);
        userPanelContainer = findViewById(R.id.userPanelContainer);
        overlay = findViewById(R.id.viewOverlay);

        // Carga inicial de datos
        loadProfileImage();

        // Configuración de Navegación Inferior
        navigationView = findViewById(R.id.menuBotton);
        loadFragment(new HomeFragment());

        navigationView.setOnItemSelectedListener(item -> {
            item.setCheckable(true);
            if (item.getItemId() == R.id.home) {
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                loadFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.Agregar) {
                Toast.makeText(this, "agregar", Toast.LENGTH_SHORT).show();
                loadFragment(new NewMisteryFragment());
                return true;
            }else if (item.getItemId() == R.id.Clasificacion) {
//                Toast.makeText(this, "Tops", Toast.LENGTH_SHORT).show();
                loadFragment(new ClasificacionesFragment());
                return true;
            }
            return false;
        });

        // 🎯 Evento Click en la Imagen de Perfil (Despliega el menú lateral fusionado)
        imgPerfil.setOnClickListener(v -> {
            overlay.setVisibility(View.VISIBLE);
            userPanelContainer.setVisibility(View.VISIBLE);

            // Cargamos el PerfilFragment fusionado pasándole el callback para cerrar el menú lateral
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userPanelContainer, new PerfilFragment(this::cerrarPanelUsuario))
                    .commit();

            // Animación lateral de entrada (Slide-In)
            userPanelContainer.post(() -> {
                userPanelContainer.setTranslationX(userPanelContainer.getWidth());
                userPanelContainer.animate()
                        .translationX(0)
                        .setDuration(300)
                        .start();
            });
        });

        // Fondo oscuro para cerrar el panel
        overlay.setOnClickListener(v -> cerrarPanelUsuario());

        // Control de barras de sistema (Edge-To-Edge padding)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Recargar la foto por si cambió tras volver de otra vista
        loadProfileImage();
    }

    private void loadProfileImage() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .addSnapshotListener((documentSnapshot, e) -> {
                        if (e != null) {
                            Log.e("PROFILE_DEBUG", "Error al escuchar cambios", e);
                            return;
                        }

                        if (!isDestroyed() && documentSnapshot != null && documentSnapshot.exists()) {
                            String photoUrl = documentSnapshot.getString("photoUrl");

                            if (photoUrl != null && !photoUrl.isEmpty()) {
                                Log.d("PROFILE_DEBUG", "Actualizando foto en Home: " + photoUrl);
                                Glide.with(MainActivity.this)
                                        .load(photoUrl)
                                        .circleCrop()
                                        .placeholder(R.drawable.img1)
                                        .error(R.drawable.img1)
                                        .into(imgPerfil);
                            }
                        }
                    });
        }
    }

    private void cerrarPanelUsuario() {
        // Animación lateral de salida (Slide-Out)
        userPanelContainer.animate()
                .translationX(userPanelContainer.getWidth())
                .setDuration(300)
                .withEndAction(() -> {
                    userPanelContainer.setVisibility(View.GONE);
                    overlay.setVisibility(View.GONE);
                })
                .start();
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.containerMain, fragment)
                .commit();
    }
}