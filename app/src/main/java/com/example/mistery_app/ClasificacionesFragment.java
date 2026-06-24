package com.example.mistery_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mistery_app.Adaptadores.RankingAdapter;
import com.example.mistery_app.ApiService.RetrofitClient;
import com.example.mistery_app.modelos.RankingUser;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClasificacionesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RankingAdapter adapter;
    private List<RankingUser> listaRanking = new ArrayList<>();

    public ClasificacionesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clasificaciones, container, false);

        recyclerView = view.findViewById(R.id.rvRanking);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RankingAdapter(listaRanking);
        recyclerView.setAdapter(adapter);
        Toast.makeText(getContext(), "En fragment Clasificaciones", Toast.LENGTH_SHORT).show();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cargarTop10DesdeServidor();
    }

    private void cargarTop10DesdeServidor() {
        RetrofitClient.getApiService().obtenerRankingTop10().enqueue(new Callback<List<RankingUser>>() {
            @Override
            public void onResponse(Call<List<RankingUser>> call, Response<List<RankingUser>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listaRanking.clear();
                                listaRanking.addAll(response.body());
                                adapter.notifyDataSetChanged();
                                Log.d("API_CLASIFICACIONES", "Datos cargados: " + listaRanking.size());
                            }
                        });
                    }

                } else {
                    try {
                        Log.e("API_CLASIFICACIONES",
                                "Error " + response.code() +
                                        " -> " + response.errorBody().string());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RankingUser>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}