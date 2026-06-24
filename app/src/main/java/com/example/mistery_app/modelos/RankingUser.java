package com.example.mistery_app.modelos;

import com.google.gson.annotations.SerializedName;

public class RankingUser {
    @SerializedName("usuario_id") private int usuarioId;
    private String nombre;

    @SerializedName("misterios_resueltos") private int misteriosResueltos;

    // Cambiamos a String temporalmente para recibirlo de manera segura sin importar el formato de Laravel
    @SerializedName("promedio_eficiencia") private String promedioEficiencia;

    public int getUsuarioId() { return usuarioId; }
    public String getNombre() { return nombre; }
    public int getMisteriosResueltos() { return misteriosResueltos; }

    // Convertimos de manera segura a double al solicitarlo en el Adaptador
    public double getPromedioEficiencia() {
        try {
            return promedioEficiencia != null ? Double.parseDouble(promedioEficiencia) : 0.0;
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }
}