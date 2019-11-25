package com.example.livescoreproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.SharedPreferencesConfig;
import com.example.livescoreproject.classes.User;

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
            // TODO faza2: request in baza de date pe baza id-ului de user, preluarea informatiilor si crearea unei instante
            // TODO a clasei User
            // deocamdata generam un dummy user doar pentru a demonstra transmiterea obiectului dintr-o activitate in alta
            User user = new User(
                    getString(R.string.temp_username),
                    getString(R.string.temp_email),
                    Integer.parseInt(getString(R.string.temp_userId))
            );
            Intent intent = new Intent(this, MainActivity.class);
            // adaugare user in intent pentru a fi transmis mai departe
            intent.putExtra("user", user);
            startActivity(intent);
            finish();
        }

        Button btnLogin = findViewById(R.id.welcomeActivity_btnLogin);
        TextView tvRegister = findViewById(R.id.welcomeActivity_tvRegister);

        // setare eventuri de click pe butonul de login si textul de register
        btnLogin.setOnClickListener( (v) -> {
            startActivity(new Intent(this, LoginActivity.class));
        });

        tvRegister.setOnClickListener( (v) -> {
            startActivity(new Intent(this, RegisterActivity.class));
        });
    }
}
