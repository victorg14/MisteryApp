package com.example.mistery_app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.mistery_app.ApiService.RetrofitClient;
import com.example.mistery_app.modelos.Misterio;
import com.example.mistery_app.MainActivity;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContenidoFragment extends Fragment {

    private ImageView imgContenido;
    private TextView txtDescripcion, txtTitulo;
    private MaterialButton btnRegresar, btnComenzar;
    private int misterio_id, publicacion_id;

    private MainActivity mainActivity;

    private String firebaseUid = "";

    public ContenidoFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contenido, container, false);

        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences("Configuracion", Context.MODE_PRIVATE);
            firebaseUid = prefs.getString("firebase_uid", "");
        }

        imgContenido = view.findViewById(R.id.imgContenido);
        txtDescripcion = view.findViewById(R.id.txtDescripcion);
        txtTitulo = view.findViewById(R.id.txtTitulo);
        btnRegresar = view.findViewById(R.id.btnRegresar);
        btnComenzar = view.findViewById(R.id.btnComenzar);

        btnRegresar.setOnClickListener(v -> {
            if (getActivity() instanceof MainActivity) {
                ((MainActivity) getActivity()).ocultarfiltro(false);
            }
            requireActivity().getSupportFragmentManager().popBackStack();
        });

        btnComenzar.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putInt("misterio_id", misterio_id);
            bundle.putInt("publicacion_id", publicacion_id);

            ResolverMisterioFragment fragment = new ResolverMisterioFragment();
            fragment.setArguments(bundle);

            requireActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.containerMain, fragment)
                    .addToBackStack(null)
                    .commit();
        });

        Bundle args = getArguments();
        if (args != null) {
            misterio_id = args.getInt("misterio_id", -1);
            publicacion_id = args.getInt("publicacion_id", -1);

            boolean ocultarIniciar = args.getBoolean("ocultar_iniciar", false);
            if (ocultarIniciar) {
                btnComenzar.setVisibility(View.GONE);
            } else {
                btnComenzar.setVisibility(View.VISIBLE);
            }

            if (!TextUtils.isEmpty(firebaseUid)) {
                cargarMisterio(misterio_id);
            } else {
                Toast.makeText(getContext(), "Error: Sesión no válida", Toast.LENGTH_SHORT).show();
            }
        }

        return view;
    }

    private void cargarMisterio(int id) {
        RetrofitClient.getApiService()
                .getMisterioById(id, firebaseUid)
                .enqueue(new Callback<Misterio>() {
                    @Override
                    public void onResponse(Call<Misterio> call, Response<Misterio> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Misterio m = response.body();

                            txtDescripcion.setText(m.getDescripcion());
                            txtTitulo.setText(m.getTitulo());

                            String imagePath = m.getImagenUri();
                            if (imagePath != null) {
                                if (imagePath.startsWith("http")) {
                                    Glide.with(requireContext())
                                            .load(imagePath)
                                            .placeholder(R.drawable.img1)
                                            .error(R.drawable.img1)
                                            .into(imgContenido);
                                } else {
                                    int resId = getResources().getIdentifier(imagePath, "drawable", requireContext().getPackageName());
                                    if (resId != 0) {
                                        imgContenido.setImageResource(resId);
                                    } else {
                                        imgContenido.setImageResource(R.drawable.img1);
                                    }
                                }
                            }

                            if (m.getPorcentajeReal() >= 100) {
                                btnComenzar.setEnabled(false);
                                btnComenzar.setText("Misterio Resuelto • 100%");
                                btnComenzar.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#C19A6B")));
                                btnComenzar.setTextColor(Color.parseColor("#121212"));
                                btnComenzar.setIcon(ContextCompat.getDrawable(getContext(), android.R.drawable.checkbox_on_background));
                                btnComenzar.setIconTint(ColorStateList.valueOf(Color.parseColor("#121212")));

                                btnComenzar.setOnClickListener(v -> {
                                    v.performHapticFeedback(android.view.HapticFeedbackConstants.LONG_PRESS);
                                });
                            }
                        } else {
                            Toast.makeText(getContext(), "Error al obtener detalles del misterio", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Misterio> call, Throwable t) {
                        Toast.makeText(getContext(), "Error cargando misterio", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}