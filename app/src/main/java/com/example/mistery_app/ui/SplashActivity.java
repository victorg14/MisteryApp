package com.example.mistery_app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mistery_app.MainActivity;
import com.example.mistery_app.R;
import com.example.mistery_app.auth.AuthManager;
import com.google.firebase.FirebaseApp;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 1. Inicializar Firebase de inmediato
        FirebaseApp.initializeApp(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // 2. 🎯 SOLUCIÓN AQUÍ: Instanciar AuthManager en el hilo principal inmediatamente
        // mientras la Activity está 100% activa.
        AuthManager authManager1 = new AuthManager();
        boolean usuarioLogueado = authManager1.isLoggedIn();

        ImageView logo = findViewById(R.id.ivSplashLogo);
        TextView title = findViewById(R.id.tvSplashTitle);

        // Animación de entrada: Combinación de Zoom y Fade
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        Animation fadeIn = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);

        logo.startAnimation(zoomIn);
        title.startAnimation(fadeIn);

        // Retraso de 2 segundos para mostrar la pantalla de carga
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent;

            // 3. Usamos el valor booleano extraído de forma segura previamente
            if (usuarioLogueado) {
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            startActivity(intent);
            finish();
        }, 2000);
    }
}