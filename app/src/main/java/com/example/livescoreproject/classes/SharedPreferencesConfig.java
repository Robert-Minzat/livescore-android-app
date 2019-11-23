package com.example.livescoreproject.classes;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.livescoreproject.R;

public class SharedPreferencesConfig {
    private SharedPreferences sharedPreferences;
    private Context context;

    public SharedPreferencesConfig(Context context) {
        // initializare campuri prin constuctor
        this.context = context;
        // preluare sharedpreferences din fisierul specificat in strings.xml cu id login_preference
        // daca nu exista, va fi creat
        // context.MODE_PRIVATE permite doar aplicatiei mele sa citeasca fisierul de preferinte
        sharedPreferences = context.getSharedPreferences(context.getResources().getString(R.string.login_preference), Context.MODE_PRIVATE);
    }

    public void writeLoginId(int id) {
        // instantiere editor pentru a putea adauga campuri in sharedpreferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // adauga camp pt user id
        editor.putInt(context.getResources().getString(R.string.login_userid_preference), id);
        // salvare campuri adaugate
        editor.apply();
    }

    public int readLoginId() {
        return sharedPreferences.getInt(context.getResources().getString(R.string.login_userid_preference),  -1);
    }
}
