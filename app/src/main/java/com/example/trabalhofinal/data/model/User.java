package com.example.trabalhofinal.data.model;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.security.NoSuchAlgorithmException;

public class User implements Serializable {

    private String username;
    private String password;
    private String email;

    public User() {

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public User(String username, String password, String email) {
        this.username = username;
        this.email = email;
        try {
            this.password = Hash.cryptoMessage(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public User(String password, String email) {
        this.email = email;
        try {
            this.password = Hash.cryptoMessage(password);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
