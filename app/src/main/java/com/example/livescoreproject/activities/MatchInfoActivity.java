package com.example.livescoreproject.activities;

import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.livescoreproject.DB.FavoriteMatchDao;
import com.example.livescoreproject.DB.LivescoreDB;
import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.FavoriteMatch;
import com.example.livescoreproject.classes.MatchInfo;
import com.example.livescoreproject.classes.MatchRequest;
import com.example.livescoreproject.classes.SharedPreferencesConfig;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MatchInfoActivity extends AppCompatActivity {
    private SharedPreferencesConfig sharedPreferences;
    private int matchId;
    private TextView tvCompetition, tvMatchTime, tvMatchScore, tvMatchStatus, tvHometeam, tvAwayteam, tvNumberOfMatches, tvTotalGoals,
            tvHometeamWins, tvHometeamDraws, tvHometeamLosses, tvAwayteamWins, tvAwayteamDraws, tvAwayteamLosses;
    private Button btnAddFavorite, btnAddFirebase;
    private FavoriteMatch match;
    private ImageView ivHometeam, ivAwayteam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);

        matchId = getIntent().getIntExtra("matchId", 0);
        sharedPreferences = new SharedPreferencesConfig(this);

        ActionBar actionBar = getSupportActionBar();
        // Afisare logo  si button de back in toolbar
        if (actionBar != null) {
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setIcon(R.drawable.logo);
            actionBar.setTitle(" " + getString(R.string.livescore));
        }

        // get textviews
        tvCompetition = findViewById(R.id.tvCompetitionName);
        tvMatchTime = findViewById(R.id.tvInfoMatchtime);
        tvMatchScore = findViewById(R.id.tvInfoMatchscore);
        tvMatchStatus = findViewById(R.id.tvMatchStatus);
        tvHometeam = findViewById(R.id.tvInfoHometeam);
        tvAwayteam = findViewById(R.id.tvInfoAwayteam);
        tvNumberOfMatches = findViewById(R.id.tvInfoNumberOfMatches);
        tvTotalGoals = findViewById(R.id.tvInfoTotalGoals);
        tvHometeamWins = findViewById(R.id.tvInfoHtWins);
        tvHometeamDraws = findViewById(R.id.tvInfoHtDraws);
        tvHometeamLosses = findViewById(R.id.tvInfoHtLosses);
        tvAwayteamWins = findViewById(R.id.tvInfoAtWins);
        tvAwayteamDraws = findViewById(R.id.tvInfoAtDraws);
        tvAwayteamLosses = findViewById(R.id.tvInfoAtLosses);
        ivHometeam = findViewById(R.id.ivHometeam);
        ivAwayteam = findViewById(R.id.ivAwayTeam);

        // get buttons
        btnAddFavorite = findViewById(R.id.btnInfoFavorite);
        btnAddFirebase = findViewById(R.id.btnInfoFirebase);

        btnAddFavorite.setOnClickListener(v -> {
            new FavoriteMatchTask(this).execute(match);
        });


        btnAddFirebase.setOnClickListener(v -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference(String.valueOf(sharedPreferences.readLoginId()));
            DatabaseReference pushPostRef = ref.push();
            String postId = pushPostRef.getKey();
            ref.child(postId).setValue(match);
            Toast.makeText(this, "Match added to firebase!", Toast.LENGTH_SHORT).show();
        });

        //request match
        try {
            URL url = new URL(getString(R.string.api_matches_link) + "/" + matchId);
            new MatchRequest(this).execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void populateFields(MatchInfo matchInfo) {
        match = new FavoriteMatch(matchInfo.getHometeam(), matchInfo.getAwayteam(), matchInfo.getScore(), matchInfo.getMatchTime(), sharedPreferences.readLoginId());
        tvCompetition.setText(matchInfo.getCompetitionName());
        SimpleDateFormat sdf2 = new SimpleDateFormat("EEE, d MMM HH:mm", Locale.getDefault());
        tvMatchTime.setText(sdf2.format(matchInfo.getMatchTime()));
        tvMatchScore.setText(matchInfo.getScore());
        tvMatchStatus.setText(matchInfo.getStatus());
        tvHometeam.setText(matchInfo.getHometeam());
        tvAwayteam.setText(matchInfo.getAwayteam());
        tvNumberOfMatches.setText(Integer.toString(matchInfo.getNumberOfMatches()));
        tvTotalGoals.setText(Integer.toString(matchInfo.getTotalGoals()));
        tvHometeamWins.setText(Integer.toString(matchInfo.getHometeamWins()));
        tvHometeamDraws.setText(Integer.toString(matchInfo.getHometeamDraws()));
        tvHometeamLosses.setText(Integer.toString(matchInfo.getHometeamLosses()));
        tvAwayteamWins.setText(Integer.toString(matchInfo.getAwayteamWins()));
        tvAwayteamDraws.setText(Integer.toString(matchInfo.getAwayteamDraws()));
        tvAwayteamLosses.setText(Integer.toString(matchInfo.getAwayteamLosses()));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private static class FavoriteMatchTask extends AsyncTask<FavoriteMatch, Void, Void> {
        private WeakReference<MatchInfoActivity> matchInfoActivityWeakReference;

        FavoriteMatchTask(MatchInfoActivity context) {
            matchInfoActivityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Void doInBackground(FavoriteMatch... matches) {
            FavoriteMatchDao favoriteMatchDao = LivescoreDB.getInstance(matchInfoActivityWeakReference.get().getApplicationContext()).getFavoriteMatchDao();
            favoriteMatchDao.insertFavoriteMatch(matches[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(matchInfoActivityWeakReference.get(), matchInfoActivityWeakReference.get().getString(R.string.match_added_to_favs), Toast.LENGTH_SHORT).show();
        }
    }
}
