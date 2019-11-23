package com.example.livescoreproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.SharedPreferencesConfig;

public class WelcomeActivity extends AppCompatActivity {
    SharedPreferencesConfig sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // verific daca userul a fost logat anterior
        // daca da, deschid direct activitatea principala si inchid activitatea de welcome
        sharedPreferences = new SharedPreferencesConfig(this);
        if(sharedPreferences.readLoginId() > 0) {
            startActivity( new Intent(this, MainActivity.class));
            finish();
        }

        Button btnLogin = findViewById(R.id.welcomeActivity_btnLogin);
        TextView tvRegister = findViewById(R.id.welcomeActivity_tvRegister);
        Toast toast = new Toast(this);

        // setare eventuri de click pe butonul de login si textul de register
        btnLogin.setOnClickListener( (v) -> {
            toast.makeText(this, "Click pe login", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
        });

        tvRegister.setOnClickListener( (v) -> {
            toast.makeText(this, "Click pe register", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
