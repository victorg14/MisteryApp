package com.example.mistery_app.ApiService;

import java.util.Locale;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {

            // Configuración del OkHttpClient con ambos interceptores
            OkHttpClient client = new OkHttpClient.Builder()
                    // 1. Interceptor para el Idioma y Headers básicos (Tu código actual)
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
                    // 2. Agregamos tu nuevo interceptor para el UID de Firebase
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