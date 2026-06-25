package com.example.mistery_app.ApiService;

import com.example.mistery_app.modelos.Comentario;
import com.example.mistery_app.modelos.EstadoMisterioResponse;
import com.example.mistery_app.modelos.Insultos;
import com.example.mistery_app.modelos.Misterio;
import com.example.mistery_app.modelos.Publicacion;
import com.example.mistery_app.modelos.RankingUser;
import com.example.mistery_app.modelos.Usuario;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {


    @GET("misterios")
    Call<List<Misterio>> getMisterios(@Query("lang") String idioma);


    @POST("misterios")
    Call<Misterio> crearMisterio(
            @Body Misterio misterio,
            @Header("Accept-Language") String idioma // 🎯 Agrega esto
    );


    @FormUrlEncoded
    @POST("publicaciones/{id}/like")
    Call<ResponseBody> toggleLike(
            @Path("id") int publicacionId,
            @Field("usuario_id") String usuarioId
    );


    @GET("publicaciones/{id}/comentarios")
    Call<List<Comentario>> getComentariosDePublicacion(
            @Path("id") int pubId,
            @Header("Accept-Language") String idioma
    );

    @POST("comentarios")
    Call<Comentario> crearComentario(
            @Body Comentario comentario,
            @Header("Accept-Language") String idioma
    );

    @GET("publicaciones")
    Call<List<Publicacion>> getPublicaciones(@Query("lang") String idioma);


    @GET("misterios/ranking")
    Call<List<RankingUser>> obtenerRankingTop10();


    @GET("misterios/{id}")
    Call<Misterio> getMisterioById(
            @Path("id") int misterioId,
            @Header("X-Firebase-Uid") String firebaseUid
    );
    @POST("usuarios")
    Call<Usuario> registrarUsuario(@Body Usuario usuario);
    @POST("usuarios/perfil")
    Call<Usuario> actualizarPerfil(@Body Usuario usuario);

    // En tu ApiService.java
    @GET("misterios/estado-inicial")
    Call<EstadoMisterioResponse> obtenerEstadoInicial(
            @Query("lang") String idioma,
            @Query("misterio_id") int misterioId
    );

    @POST("misterios/guardar-progreso")
    Call<ResponseBody> guardarProgreso(
            @Header("Accept-Language") String idioma,
            @Body Map<String, Object> body
    );


    @GET("insultos")
    Call<Insultos> getInsultosPorIdioma(@Query("lang") String idioma);
}