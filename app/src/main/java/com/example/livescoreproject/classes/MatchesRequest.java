package com.example.livescoreproject.classes;

import android.content.Context;
import android.os.AsyncTask;

import com.example.livescoreproject.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchesRequest extends AsyncTask<URL, Void, List<Match>> {
    private ArrayList<Match> raspuns = new ArrayList<>();
    private Context context;

    public MatchesRequest(Context context) {
        this.context = context;
    }

    @Override
    protected List<Match> doInBackground(URL... urls) {
        try {
            StringBuffer s;
            JSONObject responseObj, matchObj;
            Match matchToAdd;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());
            for (URL url : urls) {
                s = new StringBuffer();
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("X-Auth-Token", context.getResources().getString(R.string.api_key));
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
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return raspuns;
    }
}
