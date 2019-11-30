package com.example.livescoreproject.classes;

import android.content.Context;
import android.os.AsyncTask;

import com.example.livescoreproject.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MatchRequest extends AsyncTask<URL, Void, MatchInfo> {
    private Context context;
    private MatchInfo match;

    public MatchRequest(Context context) {
        this.context = context;
    }

    @Override
    protected MatchInfo doInBackground(URL... urls) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss", Locale.getDefault());
            StringBuffer s = new StringBuffer();
            HttpURLConnection conn = (HttpURLConnection) urls[0].openConnection();
            conn.setRequestProperty("X-Auth-Token", context.getResources().getString(R.string.api_key));
            conn.connect();
            if(conn.getResponseCode() == 200) {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
                    s.append(line);
                }
                JSONObject responseObj = new JSONObject(s.toString());
                JSONObject matchObj = responseObj.getJSONObject("match");
                JSONObject h2hObject = responseObj.getJSONObject("head2head");
                String dateString = matchObj.getString("utcDate");
                String year = dateString.substring(0, dateString.indexOf('T'));
                String time = dateString.substring(dateString.indexOf('T') + 1, dateString.length() - 1);
                Date date = sdf.parse(year + " " + time);

                int homeTeamScore = matchObj.getJSONObject("score").getJSONObject("fullTime").isNull("homeTeam")
                        ? 0 : matchObj.getJSONObject("score").getJSONObject("fullTime").getInt("homeTeam");
                int awayTeamScore = matchObj.getJSONObject("score").getJSONObject("fullTime").isNull("awayTeam")
                        ? 0 : matchObj.getJSONObject("score").getJSONObject("fullTime").getInt("awayTeam");

                match = new MatchInfo(
                        matchObj.getJSONObject("competition").getString("name"),
                        date,
                        matchObj.getJSONObject("homeTeam").getString("name"),
                        matchObj.getJSONObject("awayTeam").getString("name"),
                        homeTeamScore + " - " + awayTeamScore,
                        matchObj.getString("status"),
                        h2hObject.getInt("numberOfMatches"),
                        h2hObject.getInt("totalGoals"),
                        h2hObject.getJSONObject("homeTeam").getInt("wins"),
                        h2hObject.getJSONObject("homeTeam").getInt("draws"),
                        h2hObject.getJSONObject("homeTeam").getInt("losses"),
                        h2hObject.getJSONObject("awayTeam").getInt("wins"),
                        h2hObject.getJSONObject("awayTeam").getInt("draws"),
                        h2hObject.getJSONObject("awayTeam").getInt("losses")
                );
            }

            return match == null ? new MatchInfo() : match;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}