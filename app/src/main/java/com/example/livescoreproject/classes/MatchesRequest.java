package com.example.livescoreproject.classes;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.livescoreproject.R;
import com.example.livescoreproject.activities.MatchInfoActivity;
import com.example.livescoreproject.fragments.MatchesFragment;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchesRequest extends AsyncTask<URL, Void, List<Match>> {
    private WeakReference<MatchesFragment> matchesFragmentWeakReference;
    private ArrayList<Match> raspuns = new ArrayList<>();

    public MatchesRequest(MatchesFragment matchesFragment) {
        matchesFragmentWeakReference = new WeakReference<>(matchesFragment);
    }

    @Override
    protected List<Match> doInBackground(URL... urls) {
        try {
            MatchesFragment matchesFragment = matchesFragmentWeakReference.get();
            StringBuilder s;
            JSONObject responseObj, matchObj;
            Match matchToAdd;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());
            for (URL url : urls) {
                s = new StringBuilder();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("X-Auth-Token", matchesFragment.getResources().getString(R.string.api_key));
                conn.connect();
                if (conn.getResponseCode() == 200) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                        s.append(line);
                    }
                    responseObj = new JSONObject(s.toString());
                    JSONArray matchesArray = responseObj.getJSONArray("matches");
                    for (int i = 0; i < matchesArray.length(); i++) {
                        matchObj = matchesArray.getJSONObject(i);
                        String dateString = matchObj.getString("utcDate");
                        String year = dateString.substring(0, dateString.indexOf('T'));
                        String time = dateString.substring(dateString.indexOf('T') + 1, dateString.length() - 1);
                        Date date = sdf.parse(year + " " + time);
                        int homeTeamScore, awayTeamScore;

                        homeTeamScore = matchObj.getJSONObject("score").getJSONObject("fullTime").isNull("homeTeam")
                                ? 0 : matchObj.getJSONObject("score").getJSONObject("fullTime").getInt("homeTeam");
                        awayTeamScore = matchObj.getJSONObject("score").getJSONObject("fullTime").isNull("awayTeam")
                                ? 0 : matchObj.getJSONObject("score").getJSONObject("fullTime").getInt("awayTeam");

                        matchToAdd = new Match(
                                matchObj.getInt("id"),
                                matchObj.getJSONObject("homeTeam").getString("name"),
                                matchObj.getJSONObject("homeTeam").getInt("id"),
                                matchObj.getJSONObject("awayTeam").getString("name"),
                                matchObj.getJSONObject("awayTeam").getInt("id"),
                                homeTeamScore,
                                awayTeamScore,
                                matchObj.getJSONObject("competition").getInt("id"),
                                date
                        );

                        raspuns.add(matchToAdd);
                    }
                } else {
                    return null;
                }
            }
            return raspuns;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Match> matches) {
        super.onPostExecute(matches);
        MatchesFragment matchesFragment = matchesFragmentWeakReference.get();

        // ascundere loader
        matchesFragment.getActivity().findViewById(R.id.homeLoader).setVisibility(View.GONE);

        // ascundere mesaj unavailable network
        matchesFragment.getActivity().findViewById(R.id.homeUnavailableNetwork).setVisibility(View.GONE);

        ListView listView = matchesFragment.getActivity().findViewById(R.id.listViewHome);
        if (matches != null) {
            // initializare adapter personalizat
            MatchAdapter adapter = new MatchAdapter(matchesFragment.getActivity().getApplicationContext(), matches);
            // setare adapter listview
            listView.setAdapter(adapter);

            // setare event de click pe listview item
            listView.setOnItemClickListener((parent, view, position, id) -> {
                Match itemClicked = (Match) parent.getItemAtPosition(position);
                Intent intent = new Intent(matchesFragment.getActivity(), MatchInfoActivity.class);
                intent.putExtra("matchId", itemClicked.getMatchId());
                matchesFragment.startActivity(intent);
            });
        } else {
            // afisare mesaj unavailable network
            matchesFragment.getActivity().findViewById(R.id.homeUnavailableNetwork).setVisibility(View.VISIBLE);
        }
    }
}
