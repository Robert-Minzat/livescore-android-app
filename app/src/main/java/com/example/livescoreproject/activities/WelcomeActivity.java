package com.example.livescoreproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.livescoreproject.DB.LivescoreDB;
import com.example.livescoreproject.DB.UserDao;
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
            UserDao userDao = LivescoreDB.getInstance(getApplicationContext()).getUserDao();

            // preluare user dupa id
//            new AsyncTask<Integer, Void, User>() {
//                @Override
//                protected User doInBackground(Integer... integers) {
//
//                    return user;
//                }
//            }.execute();
            User user = userDao.getUser(sharedPreferences.readLoginId());

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
