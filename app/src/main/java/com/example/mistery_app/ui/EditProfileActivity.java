package com.example.mistery_app.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.example.mistery_app.ApiService.RetrofitClient;
import com.example.mistery_app.R;
import com.example.mistery_app.modelos.Usuario;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    
    private ImageView ivEditProfilePic;
    private EditText etEditName;
    private Button btnSaveProfile;
    private ProgressBar pbEditProfile;
    
    private Uri imageUri;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String currentPhotoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        ivEditProfilePic = findViewById(R.id.ivEditProfilePic);
        etEditName = findViewById(R.id.etEditName);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        pbEditProfile = findViewById(R.id.pbEditProfile);

        loadCurrentData();

        findViewById(R.id.cardImage).setOnClickListener(v -> openFileChooser());

        btnSaveProfile.setOnClickListener(v -> saveProfile());
    }

    private void loadCurrentData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("name");
                            currentPhotoUrl = documentSnapshot.getString("photoUrl");
                            etEditName.setText(name);
                            if (currentPhotoUrl != null && !currentPhotoUrl.isEmpty()) {
                                Glide.with(this).load(currentPhotoUrl).into(ivEditProfilePic);
                            }
                        }
                    });
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK 
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivEditProfilePic.setImageURI(imageUri);
        }
    }

    private void saveProfile() {
        String name = etEditName.getText().toString().trim();
        if (name.isEmpty()) {
            etEditName.setError("Nombre requerido");
            return;
        }

        pbEditProfile.setVisibility(View.VISIBLE);
        btnSaveProfile.setEnabled(false);

        if (imageUri != null) {
            uploadImageToCloudinary(name);
        } else {
            updateFirestore(name, currentPhotoUrl);
        }
    }

    private void uploadImageToCloudinary(String name) {
        MediaManager.get().upload(imageUri)
                .unsigned("detective_digital") // Preset de la captura
                .callback(new UploadCallback() {
                    @Override
                    public void onStart(String requestId) {}

                    @Override
                    public void onProgress(String requestId, long bytes, long totalBytes) {}

                    @Override
                    public void onSuccess(String requestId, Map resultData) {
                        String imageUrl = (String) resultData.get("secure_url");
                        updateFirestore(name, imageUrl);
                    }

                    @Override
                    public void onError(String requestId, ErrorInfo error) {
                        pbEditProfile.setVisibility(View.GONE);
                        btnSaveProfile.setEnabled(true);
                        Toast.makeText(EditProfileActivity.this, "Error al subir imagen: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onReschedule(String requestId, ErrorInfo error) {}
                }).dispatch();
    }

    private void updateFirestore(String name, String photoUrl) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", name);
            if (photoUrl != null) {
                userData.put("photoUrl", photoUrl);
            }

            db.collection("users").document(user.getUid())
                    .set(userData, com.google.firebase.firestore.SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        // 🎯 NUEVO: Sincronizar con Laravel/MySQL
                        syncWithLaravel(name, photoUrl);
                    })
                    .addOnFailureListener(e -> {
                        pbEditProfile.setVisibility(View.GONE);
                        btnSaveProfile.setEnabled(true);
                        Toast.makeText(EditProfileActivity.this, "Error al guardar: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void syncWithLaravel(String name, String photoUrl) {
        Usuario usuario = new Usuario();
        usuario.setNombre(name);
        usuario.setFoto(photoUrl);
        usuario.setFirebaseUid(mAuth.getCurrentUser().getUid());

        RetrofitClient.getApiService().actualizarPerfil(usuario).enqueue(new Callback<Usuario>() {
            @Override
            public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                pbEditProfile.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Perfil sincronizado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    btnSaveProfile.setEnabled(true);
                    Toast.makeText(EditProfileActivity.this, "Error al sincronizar con MySQL", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Usuario> call, Throwable t) {
                pbEditProfile.setVisibility(View.GONE);
                btnSaveProfile.setEnabled(true);
                Toast.makeText(EditProfileActivity.this, "Fallo de red con servidor local", Toast.LENGTH_SHORT).show();
            }
        });
    }
}