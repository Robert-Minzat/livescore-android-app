package com.example.livescoreproject.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livescoreproject.DB.LivescoreDB;
import com.example.livescoreproject.DB.UserDao;
import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.SharedPreferencesConfig;
import com.example.livescoreproject.classes.User;

import java.lang.ref.WeakReference;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferencesConfig sharedPreferences;
    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvRegister;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // folosit pentru resize/scroll atunci cand se deschide tastatura
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        // Afisare logo in toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.logo);
            actionBar.setTitle(" " + getString(R.string.livescore));
        }

        // get sharedpreferences
        sharedPreferences = new SharedPreferencesConfig(getApplicationContext());

        etUsername = findViewById(R.id.loginActivity_etUsername);
        etPassword = findViewById(R.id.loginActivity_etPassword);
        btnLogin = findViewById(R.id.loginActivity_btnLogin);
        tvRegister = findViewById(R.id.loginActivity_tvRegister);

        // event de click pe buton login
        btnLogin.setOnClickListener(v -> loginUser());

        // event de click buton register
        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(this, RegisterActivity.class));
            finish();
        });
    }

    private void loginUser() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        new UserTask(this).execute(username, password);
    }

    private static class UserTask extends AsyncTask<String, Void, User> {
        private WeakReference<LoginActivity> loginActivityWeakReference;

        UserTask(LoginActivity context) {
            loginActivityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPostExecute(User user) {
            LoginActivity activity = loginActivityWeakReference.get();
            if(activity == null || activity.isFinishing())
                return;
            if(user != null) {
                // se stocheaza id-ul userului gasit pentru a tine minte ca este logat
                activity.sharedPreferences.writeLoginId(user.getUserId());
                Intent intent = new Intent(activity, MainActivity.class);
                // adaugare user in intent pentru a fi transmis mai departe
                intent.putExtra("user", user);
                activity.startActivity(intent);
                activity.finish();
            } else {
                activity.etUsername.setText("");
                activity.etPassword.setText("");

                Toast.makeText(activity, "Login failed.. Try again!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected User doInBackground(String... strings) {
            String username = strings[0];
            String password = strings[1];
            UserDao userDao = LivescoreDB.getInstance(loginActivityWeakReference.get().getApplicationContext()).getUserDao();
            User user = userDao.checkUsernameAndPass(username, password);
            return user;
        }
    }
}
