package com.example.livescoreproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.livescoreproject.R;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegister;
    private EditText etUsername, etPassword, etConfirmPassword, etEmail;
    private RadioGroup rgSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.registerActivity_btnRegister);
        etUsername = findViewById(R.id.registerActivity_etUsername);
        etPassword  = findViewById(R.id.registerActivity_etPassword);
        etConfirmPassword = findViewById(R.id.registerActivity_etConfirmPassword);
        etEmail = findViewById(R.id.registerActivity_etEmail);
        rgSex = findViewById(R.id.registerRadioGroup);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        btnRegister.setOnClickListener(v -> {
            if(etUsername.getText().toString().trim().length() < 3) {
                Toast.makeText(this, "Username is too short!", Toast.LENGTH_SHORT).show();
                return;
            } else if (!etEmail.getText().toString().trim().matches(emailPattern)) {
                Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show();
                return;
            } else if (etPassword.getText().toString().trim().length() < 3) {
                Toast.makeText(this, "Password is too short!", Toast.LENGTH_SHORT).show();
                return;
            } else if (!etPassword.getText().toString().trim().equals(etConfirmPassword.getText().toString().trim())) {
                Toast.makeText(this, "Passwords do no match!", Toast.LENGTH_SHORT).show();
                return;
            } else if (rgSex.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Please choose your sex!", Toast.LENGTH_SHORT).show();
                return;
            } // TODO validare date of birth

            // TODO creare user cu campurile de mai sus si salvare in baza de date
            Toast.makeText(this, "Account created! You can now log in!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

    }
}
