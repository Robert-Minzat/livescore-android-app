package com.example.livescoreproject.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.MatchInfo;
import com.example.livescoreproject.classes.MatchRequest;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class MatchInfoActivity extends AppCompatActivity {
    private int matchId;
    private TextView tvCompetition, tvMatchTime, tvMatchScore, tvMatchStatus, tvHometeam, tvAwayteam, tvNumberOfMatches, tvTotalGoals,
        tvHometeamWins, tvHometeamDraws, tvHometeamLosses, tvAwayteamWins, tvAwayteamDraws, tvAwayteamLosses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_info);

        matchId = getIntent().getIntExtra("matchId", 0);

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

        //request match
        MatchRequest request = new MatchRequest(this) {
            @Override
            protected void onPostExecute(MatchInfo matchInfo) {
                super.onPostExecute(matchInfo);
                if(matchInfo != null) {
                    populateFields(matchInfo);
                }
            }
        };
        try {
            URL url = new URL(getString(R.string.api_matches_link) + "/" + matchId);
            request.execute(url);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void populateFields(MatchInfo matchInfo) {
        tvCompetition.setText(matchInfo.getCompetitionName());
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM HH:mm", Locale.getDefault());
        tvMatchTime.setText(sdf.format(matchInfo.getMatchTime()));
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
}
