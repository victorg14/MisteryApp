package com.example.mistery_app.ApiService;

import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {

            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS) // Tiempo máximo para establecer conexión
                    .readTimeout(60, TimeUnit.SECONDS)    // Tiempo máximo para esperar la respuesta de Laravel
                    .writeTimeout(60, TimeUnit.SECONDS)   // Tiempo máximo para subir datos/imágenes


                    .addInterceptor(chain -> {
                        Request original = chain.request();
                        String idiomaDispositivo = Locale.getDefault().getLanguage();

                        Request request = original.newBuilder()
                                .header("Accept", "application/json")
                                .header("Accept-Language", idiomaDispositivo)
                                .method(original.method(), original.body())
                                .build();
                        return chain.proceed(request);
                    })

                    .addInterceptor(new FirebaseUidInterceptor())
                    .build();

            // Construcción de Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://192.168.1.13/apiconsumer/public/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}