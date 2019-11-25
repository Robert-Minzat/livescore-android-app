package com.example.livescoreproject.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.Match;
import com.example.livescoreproject.classes.MatchAdapter;
import com.example.livescoreproject.classes.Request;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MatchesFragment extends Fragment {
    private ListView listView;
    private TextView tvDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        populateListView();

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM", Locale.getDefault());
        tvDate = getActivity().findViewById(R.id.homeCurrentDate);
        tvDate.setText(sdf.format(new Date(System.currentTimeMillis())));
    }

    private void populateListView() {
        Request request = new Request(getContext()) {
            @Override
            protected void onPostExecute(List<Match> matches) {
                super.onPostExecute(matches);
                // oprire loader
                getActivity().findViewById(R.id.homeLoader).setVisibility(View.GONE);
                listView = getActivity().findViewById(R.id.listViewHome);
                // sortare meciuri dupa data/timp
                Collections.sort(matches, (o1, o2) -> o1.getDate().compareTo(o2.getDate()));
                MatchAdapter adapter = new MatchAdapter(getActivity().getApplicationContext(), matches);
                listView.setAdapter(adapter);
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
    }
}
