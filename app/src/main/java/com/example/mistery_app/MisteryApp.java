package com.example.mistery_app;

import android.app.Application;

import com.cloudinary.android.MediaManager;
import java.util.HashMap;
import java.util.Map;

public class MisteryApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Configuración de Cloudinary
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name", "dykmtvqvv");
        MediaManager.init(this, config);
    }
}