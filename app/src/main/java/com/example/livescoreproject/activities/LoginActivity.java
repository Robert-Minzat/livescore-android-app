package com.example.livescoreproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.SharedPreferencesConfig;
import com.example.livescoreproject.classes.User;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferencesConfig sharedPreferences;
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // folosit pentru resize/scroll atunci cand se deschide tastatura
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // get sharedpreferences
        sharedPreferences = new SharedPreferencesConfig(getApplicationContext());

        etUsername = findViewById(R.id.loginActivity_etUsername);
        etPassword = findViewById(R.id.loginActivity_etPassword);
        btnLogin = findViewById(R.id.loginActivity_btnLogin);
        tvRegister = findViewById(R.id.loginActivity_tvRegister);

        // event de click pe buton login
        btnLogin.setOnClickListener(v -> {
            loginUser();
        });

        // event de click buton register
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        // verificarea se face deocamdata cu valori hardcodate, in urmatoarea etapa se vor verifica folosind userii stocati in baza de date
        // se va stoca in sharedpreferences id-ul userului gasit
        if(username.equals(getString(R.string.temp_username)) && password.equals(getString(R.string.temp_password))) {
            // se stocheaza o valoare temporara
            sharedPreferences.writeLoginId(Integer.parseInt(getString(R.string.temp_userId)));
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            etUsername.setText("");
            etPassword.setText("");

            Toast.makeText(this, "Login failed.. Try again!", Toast.LENGTH_LONG).show();
        }
    }
}
