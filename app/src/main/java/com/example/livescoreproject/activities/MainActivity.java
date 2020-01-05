package com.example.livescoreproject.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.SharedPreferencesConfig;
import com.example.livescoreproject.classes.User;
import com.example.livescoreproject.fragments.AboutFragment;
import com.example.livescoreproject.fragments.FavoritesFragment;
import com.example.livescoreproject.fragments.MatchesFragment;
import com.example.livescoreproject.fragments.RapoarteFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private SharedPreferencesConfig sharedPreferences;
    private int userId;
    private long backPressedTime;
    private Toast backToast;

    private TextView tvUsername, tvEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get sharedpreferences
        sharedPreferences = new SharedPreferencesConfig(this);

        // get intent
        Intent intent = getIntent();

        // get obiect user primit prin intent
        User user = intent.getParcelableExtra("user");
        if(user == null) {
            sharedPreferences.writeLoginId(-1);
            finish();
            startActivity(new Intent(this, WelcomeActivity.class));
            return;
        }

        // configurare navigation view
        navigationView = findViewById(R.id.nav_view);
        configNavigation(user);


        // setare fragment matches ca fragment inital
        // fiind in onCreate, verificam daca exista savedInstanceState ca sa nu inlocuiasca alt fragment la
        // schimbarea orientarii ecranului, cand se apeleaza din nou onCreate
        if( savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MatchesFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_matches);
        }
    }

    private void configNavigation(User user) {
        // setare toolbar navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // adaugare hamburger icon pentru a deschide meniul drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        // setare nume si email pentru userul logat
        tvUsername = navigationView.getHeaderView(0).findViewById(R.id.nav_header_username);
        tvEmail = navigationView.getHeaderView(0).findViewById(R.id.nav_header_email);
        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());

        // adaugare event de selectare item din navigation view
        navigationView.setNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.nav_matches:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MatchesFragment()).commit();
                    break;
                case R.id.nav_favorites:
                    Bundle bundle = new Bundle();
                    bundle.putInt("userId", sharedPreferences.readLoginId());
                    FavoritesFragment favoritesFragment = new FavoritesFragment();
                    favoritesFragment.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            favoritesFragment).commit();
                    break;
                case R.id.nav_rapoarte_grafic:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new RapoarteFragment()).commit();
                    break;
                case R.id.nav_about:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AboutFragment()).commit();
                    break;
                case R.id.nav_logout:
                    sharedPreferences.writeLoginId(-1);
                    startActivity(new Intent(this, WelcomeActivity.class));
                    finish();
                    break;
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Afisare logo in toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.logo);
            actionBar.setTitle(" " + getString(R.string.livescore));
        }

        // sincronizare stare toggle cu drawer
        toggle.syncState();

    }

    @Override
    public void onBackPressed() {
        // daca navigation view e deschis, il inchidem
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // daca utilizatorul apasa back de 2 ori consecutiv(intr-un interval de 2 secunde), iesire din aplicatie
            if(backPressedTime + 2000 > System.currentTimeMillis()) {
                backToast.cancel();
                finishAffinity();
            } else {
                backToast = Toast.makeText(getBaseContext(), "Press back again to exit", Toast.LENGTH_SHORT);
                backToast.show();
            }

            backPressedTime = System.currentTimeMillis();
        }
    }


}
