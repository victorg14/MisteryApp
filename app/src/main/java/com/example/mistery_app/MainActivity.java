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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private ImageView imgPerfil;
    private FrameLayout userPanelContainer;
    private View overlay;
    private BottomNavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private TextInputLayout layoutFiltro;
    private AutoCompleteTextView spinnerFiltroCategoria;
    String[] opcionesFiltro = {"Todos los Casos","Crimen", "Lógica", "Terror", "Historia", "Ciencia Ficcion"};

    private static final String PREF_NAME = "ConfiguracionApp";
    private static final String KEY_IDIOMA = "idioma_seleccionado";

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        imgPerfil = findViewById(R.id.imgPerfil);
        userPanelContainer = findViewById(R.id.userPanelContainer);
        overlay = findViewById(R.id.viewOverlay);

        layoutFiltro = findViewById(R.id.layoutFiltro);
        spinnerFiltroCategoria = findViewById(R.id.spinnerFiltroCategoria);
        configurarFiltroDeCategorias();

        loadProfileImage();

        navigationView = findViewById(R.id.menuBotton);
        loadFragment(new HomeFragment());

        navigationView.setOnItemSelectedListener(item -> {
            item.setCheckable(true);
            if (item.getItemId() == R.id.home) {
                reiniciarFiltroCategoria();
                layoutFiltro.setVisibility(View.VISIBLE);
                loadFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.Agregar) {
                layoutFiltro.setVisibility(View.GONE);
                loadFragment(new NewMisteryFragment());
                return true;
            } else if (item.getItemId() == R.id.Clasificacion) {
                layoutFiltro.setVisibility(View.GONE);
                loadFragment(new ClasificacionesFragment());
                return true;
            }
            return false;
        });


        imgPerfil.setOnClickListener(v -> {

            overlay.setVisibility(View.VISIBLE);
            userPanelContainer.setVisibility(View.VISIBLE);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.userPanelContainer, new PerfilFragment(this::cerrarPanelUsuario))
                    .commit();
            userPanelContainer.post(() -> {
                userPanelContainer.setTranslationX(userPanelContainer.getWidth());
                userPanelContainer.animate()
                        .translationX(0)
                        .setDuration(300)
                        .start();
            });

        });

        overlay.setOnClickListener(v -> cerrarPanelUsuario());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void configurarFiltroDeCategorias() {

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                opcionesFiltro
        );
        spinnerFiltroCategoria.setAdapter(adapter);

        spinnerFiltroCategoria.setText(opcionesFiltro[0], false);

        spinnerFiltroCategoria.setOnItemClickListener((parent, view, position, id) -> {
            String categoriaSeleccionada = (String) parent.getItemAtPosition(position);

            Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.containerMain);
            if (currentFragment instanceof HomeFragment) {

                ((HomeFragment) currentFragment).filtrarMisteriosPorCategoria(categoriaSeleccionada);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
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

    public void reiniciarFiltroCategoria() {
        if (spinnerFiltroCategoria != null) {
            spinnerFiltroCategoria.setText("Todos los Casos", false);
            spinnerFiltroCategoria.setSelection(0);
        }
    }

    public void ocultarfiltro(boolean BOOL){

        if (BOOL){
            layoutFiltro.setVisibility(View.GONE);
        }else {
            layoutFiltro.setVisibility(View.VISIBLE);
        }


    }
}