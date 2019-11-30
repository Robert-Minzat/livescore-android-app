package com.example.livescoreproject.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.livescoreproject.R;
import com.example.livescoreproject.activities.MatchInfoActivity;
import com.example.livescoreproject.classes.Match;
import com.example.livescoreproject.classes.MatchAdapter;
import com.example.livescoreproject.classes.MatchesRequest;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchesFragment extends Fragment {
    private ListView listView;
    private ImageView ivRefresh;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // populare initiala a listview
        populateListView();

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ivRefresh = getActivity().findViewById(R.id.homeRefreshButton);
        // event de click pe butonul de refresh
        ivRefresh.setOnClickListener(v -> {
            // golire listview
            listView.setEmptyView(null);
            // afisare loader
            getActivity().findViewById(R.id.homeLoader).setVisibility(View.VISIBLE);
            populateListView();
        });

        // setare data curenta
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM", Locale.getDefault());
        TextView tvDate = getActivity().findViewById(R.id.homeCurrentDate);
        tvDate.setText(sdf.format(new Date(System.currentTimeMillis())));
    }

    private void populateListView() {
        try {
            // verific daca exista conexiune la internet
            final String command = "ping -c 1 google.com";
            if (Runtime.getRuntime().exec(command).waitFor() == 0) {
                MatchesRequest request = new MatchesRequest(getContext()) {
                    @Override
                    protected void onPostExecute(List<Match> matches) {
                        super.onPostExecute(matches);
                        // oprire loader
                        getActivity().findViewById(R.id.homeLoader).setVisibility(View.GONE);
                        listView = getActivity().findViewById(R.id.listViewHome);
                        if(matches != null && matches.size() > 0) {
                            // initializare adapted personalizat
                            MatchAdapter adapter = new MatchAdapter(getActivity().getApplicationContext(), matches);
                            // setare adapter listview
                            listView.setAdapter(adapter);

                            // setare event de click pe listview item
                            listView.setOnItemClickListener((parent, view, position, id) -> {
                                Match itemClicked = (Match)parent.getItemAtPosition(position);
                                Intent intent = new Intent(getActivity(), MatchInfoActivity.class);
                                intent.putExtra("matchId", itemClicked.getMatchId());
                                startActivity(intent);
                            });
                        }
                    }
                };
                try {
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    URL url = new URL(getString(R.string.api_matches_link)
                            + "?dateFrom=" + sdf.format(date)
                            + "&dateTo=" + sdf.format(date));
                    request.execute(url);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // oprire loader
                getActivity().findViewById(R.id.homeLoader).setVisibility(View.GONE);
                // afisare mesaj no internet connection
                getActivity().findViewById(R.id.homeNoInternet).setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
