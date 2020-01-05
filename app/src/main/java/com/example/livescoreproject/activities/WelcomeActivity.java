package com.example.livescoreproject.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.livescoreproject.DB.LivescoreDB;
import com.example.livescoreproject.DB.UserDao;
import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.SharedPreferencesConfig;
import com.example.livescoreproject.classes.User;

import java.lang.ref.WeakReference;

public class WelcomeActivity extends AppCompatActivity {
    private SharedPreferencesConfig sharedPreferences;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // verific daca userul a fost logat anterior
        // daca da, deschid direct activitatea principala si inchid activitatea de welcome
        sharedPreferences = new SharedPreferencesConfig(this);
        if(sharedPreferences.readLoginId() > 0) {
            // pornire loader
            findViewById(R.id.welcomeLoader).setVisibility(View.VISIBLE);
            new UserTask(this).execute(sharedPreferences.readLoginId());
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

    private static class UserTask extends AsyncTask<Integer, Void, User> {
        private WeakReference<WelcomeActivity> welcomeActivityWeakReference;

        UserTask(WelcomeActivity context) {
            welcomeActivityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPostExecute(User user) {
            WelcomeActivity activity = welcomeActivityWeakReference.get();
            if(activity == null || activity.isFinishing())
                return;
            activity.user = user;

            Intent intent = new Intent(activity, MainActivity.class);
            // adaugare user in intent pentru a fi transmis mai departe
            intent.putExtra("user", user);
            //oprire loader
            activity.findViewById(R.id.welcomeLoader).setVisibility(View.INVISIBLE);
            activity.startActivity(intent);
            activity.finish();
        }

        @Override
        protected User doInBackground(Integer... ids) {
            UserDao userDao = LivescoreDB.getInstance(welcomeActivityWeakReference.get().getApplicationContext()).getUserDao();
            User user = userDao.getUser(ids[0]);
            return user;
        }
    }
}
