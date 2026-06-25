package com.example.mistery_app.modelos;

import com.google.gson.annotations.SerializedName;

public class Comentario {
    private int id;

    @SerializedName("publicacion_id")
    private int publicacionId;

    @SerializedName("usuario_id")
    private int usuarioId;

    @SerializedName("usuario_nombre")
    private String usuarioNombre;

    private String comentario;

    @SerializedName("fecha_comentario")
    private String fechaComentario;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Constructor
    public Comentario() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getPublicacionId() { return publicacionId; }
    public void setPublicacionId(int publicacionId) { this.publicacionId = publicacionId; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public String getFechaComentario() { return fechaComentario; }
    public void setFechaComentario(String fechaComentario) { this.fechaComentario = fechaComentario; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}