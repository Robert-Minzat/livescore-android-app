package com.example.livescoreproject.fragments;

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
import com.example.livescoreproject.classes.MatchesRequest;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
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
        listView = getActivity().findViewById(R.id.listViewHome);
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
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            URL url = new URL(getString(R.string.api_matches_link)
                    + "?dateFrom=" + sdf.format(date)
                    + "&dateTo=" + sdf.format(date));
            new MatchesRequest(this).execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
