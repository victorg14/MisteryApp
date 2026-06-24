package com.example.mistery_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.mistery_app.Adaptadores.publicacionAdapter;
import com.example.mistery_app.ApiService.ApiService;
import com.example.mistery_app.ApiService.RetrofitClient;
import com.example.mistery_app.modelos.Misterio;
import com.example.mistery_app.modelos.Publicacion;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private publicacionAdapter adapter;
    private List<Publicacion> listaPublicaciones = new ArrayList<>();
    private List<Misterio> dataMisterio = new ArrayList<>();
    private boolean publicacionesCargadas = false;
    private boolean misteriosCargados = false;

    // 🎯 LOGIC REFACTOR: Se elimina 'idUsuarioLogueado = 1'.
    // Ahora el interceptor de Retrofit adjunta el header 'X-Firebase-Uid' automáticamente de forma dinámica.

    public HomeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = view.findViewById(R.id.rvPublicaciones);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new publicacionAdapter(listaPublicaciones, dataMisterio);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        obtenerPublicacionesDesdeLaravel();
    }

    private void actualizarRecycler() {
        if (publicacionesCargadas && misteriosCargados) {
            adapter.setLista(listaPublicaciones, dataMisterio);
        }
    }

    private void obtenerPublicacionesDesdeLaravel() {
        // Obtener el idioma guardado por el Spinner en SharedPreferences (por defecto "es")
        SharedPreferences prefs = requireActivity().getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
        String idiomaUsuario = prefs.getString("idioma_seleccionado", "es");

        ApiService apiService = RetrofitClient.getApiService();


        apiService.getPublicaciones(idiomaUsuario).enqueue(new Callback<List<Publicacion>>() {
            @Override
            public void onResponse(@NonNull Call<List<Publicacion>> call, @NonNull Response<List<Publicacion>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPublicaciones = response.body();
                    publicacionesCargadas = true;
                    actualizarRecycler();
                } else {
                    try {
                        Log.e("API_ERROR", "Error Publicaciones " + response.code() + " -> " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Publicacion>> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", "Fallo Publicaciones: " + t.getMessage(), t);
            }
        });


        apiService.getMisterios(idiomaUsuario).enqueue(new Callback<List<Misterio>>() {
            @Override
            public void onResponse(@NonNull Call<List<Misterio>> call, @NonNull Response<List<Misterio>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    dataMisterio = response.body();
                    misteriosCargados = true;
                    actualizarRecycler();
                } else {
                    try {
                        Log.e("API_ERROR", "Error Misterios " + response.code() + " -> " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Misterio>> call, @NonNull Throwable t) {
                Log.e("API_FAILURE", "Fallo Misterios: " + t.getMessage(), t);
            }
        });
    }
}