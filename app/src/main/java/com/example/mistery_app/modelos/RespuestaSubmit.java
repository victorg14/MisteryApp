package com.example.mistery_app.modelos;

import com.google.gson.annotations.SerializedName;

public class RespuestaSubmit {
    @SerializedName("pregunta_id")
    private int preguntaId;
    @SerializedName("is_correcta")
    private boolean isCorrecta;

    public RespuestaSubmit(int preguntaId, boolean isCorrecta) {
        this.preguntaId = preguntaId;
        this.isCorrecta = isCorrecta;
    }
}