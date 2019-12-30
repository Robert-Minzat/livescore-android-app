package com.example.livescoreproject.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.livescoreproject.DB.LivescoreDB;
import com.example.livescoreproject.DB.UserDao;
import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.User;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private Button btnRegister;
    private EditText etUsername, etPassword, etConfirmPassword, etEmail, etDateOfBirth;
    private RadioGroup rgSex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnRegister = findViewById(R.id.registerActivity_btnRegister);
        etUsername = findViewById(R.id.registerActivity_etUsername);
        etPassword = findViewById(R.id.registerActivity_etPassword);
        etConfirmPassword = findViewById(R.id.registerActivity_etConfirmPassword);
        etEmail = findViewById(R.id.registerActivity_etEmail);
        rgSex = findViewById(R.id.registerRadioGroup);
        etDateOfBirth = findViewById(R.id.registerActivity_etDateOfBirth);

        // Afisare logo in toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.logo);
            actionBar.setTitle(" " + getString(R.string.livescore));
        }

        // click listener pe edit text pentru ziua de nastere
        etDateOfBirth.setOnClickListener(v -> showDatePickerDialog());

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        btnRegister.setOnClickListener(v -> {
            if (etUsername.getText().toString().trim().length() < 3) {
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
            } else if (etDateOfBirth.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Please select your birth date!", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton radioButton = findViewById(rgSex.getCheckedRadioButtonId());
            User user = new User(
                    etUsername.getText().toString().trim(),
                    etEmail.getText().toString().trim(),
                    etPassword.getText().toString().trim(),
                    radioButton.getText().toString(),
                    etDateOfBirth.getText().toString().trim());

            // adaugare user creat in baza de date
            UserDao userDao = LivescoreDB.getInstance(getApplicationContext()).getUserDao();
            userDao.addUser(user);

            Toast.makeText(this, "Account created! You can now log in!", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "/" + (month + 1) + "/" + dayOfMonth;
        etDateOfBirth.setText(date);
    }
}
