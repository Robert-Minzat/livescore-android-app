package com.example.livescoreproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.livescoreproject.fragments.AboutFragment;
import com.example.livescoreproject.fragments.FavoritesFragment;
import com.example.livescoreproject.fragments.HomeFragment;
import com.example.livescoreproject.fragments.MatchesFragment;
import com.example.livescoreproject.fragments.StandingsFragment;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // configurare navigation view
        navigationView = findViewById(R.id.nav_view);
        configNavigation();

        // setare fragment home ca fragment inital
        // fiind in onCreate, verificam daca exista savedInstanceState ca sa nu inlocuiasca alt fragment la
        // schimbarea orientarii ecranului, cand se apeleaza din nou onCreate
        if( savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        // Afisare logo in toolbar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setIcon(R.drawable.logo);
            actionBar.setTitle(" " + getString(R.string.livescore));
        }
    }

    private void configNavigation() {
        // setare toolbar navigation drawer
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // adaugare hamburger icon pentru a deschide meniul drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawerLayout.addDrawerListener(toggle);

        // adaugare event de selectare item din navigation view
        navigationView.setNavigationItemSelectedListener((item) -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new HomeFragment()).commit();
                    break;
                case R.id.nav_matches:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new MatchesFragment()).commit();
                    break;
                case R.id.nav_favorites:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new FavoritesFragment()).commit();
                    break;
                case R.id.nav_standings:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new StandingsFragment()).commit();
                    break;
                case R.id.nav_about:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            new AboutFragment()).commit();
                    break;
                case R.id.nav_logout:
                    finish();
                    break;
            }

            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // sincronizare stare toggle cu drawer
        toggle.syncState();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public void onBackPressed() {
        // daca navigation view e deschis, il inchidem
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            // altfel, comportament default
            super.onBackPressed();
        }
    }
}
