package com.example.mistery_app.modelos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Misterio {

    private int id;

    private String titulo;
    private String descripcion;
    private String categoria;

    @SerializedName("imagen_uri")
    private String imagenUri;

    @SerializedName("usuario_id")
    private int id_usuario;

    @SerializedName("porcentaje_real")
    private int porcentajeReal;

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public int getPorcentajeReal() { return porcentajeReal; }
    public void setPorcentajeReal(int porcentajeReal) { this.porcentajeReal = porcentajeReal; }
    private List<Pregunta> preguntas;

    private String firebaseUid;

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public Misterio() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenUri() { return imagenUri; }
    public void setImagenUri(String imagenUri) { this.imagenUri = imagenUri; }

    public List<Pregunta> getPreguntas() { return preguntas; }
    public void setPreguntas(List<Pregunta> preguntas) { this.preguntas = preguntas; }
}