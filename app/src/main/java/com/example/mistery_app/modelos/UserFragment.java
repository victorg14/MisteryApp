package com.example.mistery_app.modelos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.mistery_app.R;

public class UserFragment extends Fragment {

    private Runnable onClose;

    public UserFragment(Runnable onClose) {
        this.onClose = onClose;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_perfil,
                container,
                false);

        TextView btnCerrar =
                view.findViewById(R.id.btnCerrar);

        btnCerrar.setOnClickListener(v -> {

            if(onClose != null){
                onClose.run();
            }

        });

        return view;
    }
}
