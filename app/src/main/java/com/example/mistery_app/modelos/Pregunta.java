package com.example.mistery_app.modelos;

import com.google.gson.annotations.SerializedName;

public class Pregunta {
    private int id;

    @SerializedName("misterio_id")
    private int misterioId;

    private String pregunta;
    private String opcion1;
    private String opcion2;
    private String opcion3;
    private String opcion4;

    @SerializedName("respuesta_correcta")
    private String respuestaCorrecta;

    private String pista1;
    private String pista2;
    private String pista3;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("pista_actual")
    private String pistaActual;

    @SerializedName("estado_previo")
    private String estadoPrevio;

    public String getEstadoPrevio() {
        return estadoPrevio;
    }

    // Opcional: por si necesitas modificarlo manualmente en Android en algún momento
    public void setEstadoPrevio(String estadoPrevio) {
        this.estadoPrevio = estadoPrevio;
    }

    public String getPistaActual() { return pistaActual; }

    // Constructor
    public Pregunta() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMisterioId() { return misterioId; }
    public void setMisterioId(int misterioId) { this.misterioId = misterioId; }

    public String getPregunta() { return pregunta; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }

    public String getOpcion1() { return opcion1; }
    public void setOpcion1(String opcion1) { this.opcion1 = opcion1; }

    public String getOpcion2() { return opcion2; }
    public void setOpcion2(String opcion2) { this.opcion2 = opcion2; }

    public String getOpcion3() { return opcion3; }
    public void setOpcion3(String opcion3) { this.opcion3 = opcion3; }

    public String getOpcion4() { return opcion4; }
    public void setOpcion4(String opcion4) { this.opcion4 = opcion4; }

    public String getRespuestaCorrecta() { return respuestaCorrecta; }
    public void setRespuestaCorrecta(String respuestaCorrecta) { this.respuestaCorrecta = respuestaCorrecta; }

    public String getPista1() { return pista1; }
    public void setPista1(String pista1) { this.pista1 = pista1; }

    public String getPista2() { return pista2; }
    public void setPista2(String pista2) { this.pista2 = pista2; }

    public String getPista3() { return pista3; }
    public void setPista3(String pista3) { this.pista3 = pista3; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}