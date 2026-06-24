package com.example.mistery_app.modelos;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Publicacion {
    private int id;

    @SerializedName("usuario_id")
    private int usuarioId;

    @SerializedName("misterio_id")
    private int misterioId;

    private int likes;

    @SerializedName("fecha_publicacion")
    private String fechaPublicacion;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("comentarios_count")
    private int comentariosCount;

    @SerializedName("is_liked")
    private boolean isLiked;

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }
    public int getComentariosCount() {
        return comentariosCount;
    }

    public void setComentariosCount(int comentariosCount) {
        this.comentariosCount = comentariosCount;
    }

    // Constructor
    public Publicacion() {}

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUsuarioId() { return usuarioId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }

    public int getMisterioId() { return misterioId; }
    public void setMisterioId(int misterioId) { this.misterioId = misterioId; }

    public int getLikes() { return likes; }
    public void setLikes(int likes) { this.likes = likes; }

    public String getFechaPublicacion() { return fechaPublicacion; }
    public void setFechaPublicacion(String fechaPublicacion) { this.fechaPublicacion = fechaPublicacion; }

    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}