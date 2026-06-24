package com.example.mistery_app.modelos;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class EstadoMisterioResponse {
    @SerializedName("indice_arranque")
    private int indiceArranque;
    private List<Pregunta> preguntas;

    public int getIndiceArranque() { return indiceArranque; }
    public List<Pregunta> getPreguntas() { return preguntas; }
}