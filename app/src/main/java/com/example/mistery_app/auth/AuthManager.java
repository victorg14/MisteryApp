package com.example.mistery_app.auth;

import android.content.Context;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthManager {
    private final FirebaseAuth mAuth;

    public AuthManager() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public AuthManager(Context context) {
        // The Google Services plugin usually handles initialization via ContentProvider.
        // We just get the instance here.
        this.mAuth = FirebaseAuth.getInstance();
    }

    public boolean isLoggedIn() {
        return mAuth.getCurrentUser() != null;
    }

    public void logout() {
        mAuth.signOut();
    }

    public FirebaseUser getCurrentUser() {
        return mAuth.getCurrentUser();
    }

    public String getCurrentUid() {
        FirebaseUser user = mAuth.getCurrentUser();
        return user != null ? user.getUid() : null;
    }
}