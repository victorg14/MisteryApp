package com.example.mistery_app.ApiService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class FirebaseUidInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Request.Builder builder = original.newBuilder();

        if (user != null) {
            builder.addHeader("X-Firebase-Uid", user.getUid());
        }

        return chain.proceed(builder.build());
    }
}
