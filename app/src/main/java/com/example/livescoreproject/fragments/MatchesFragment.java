package com.example.livescoreproject.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.livescoreproject.R;
import com.example.livescoreproject.classes.MatchesRequest;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MatchesFragment extends Fragment implements DatePickerDialog.OnDateSetListener {
    private ListView listView;
    private ImageView ivRefresh;
    private TextView tvDate;
    private Date requestDate;
    private static SimpleDateFormat dateDisplayFormat = new SimpleDateFormat("EEEE, d MMM", Locale.getDefault());
    private static SimpleDateFormat dateRequestFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        requestDate = new Date(System.currentTimeMillis());

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = getActivity().findViewById(R.id.listViewHome);
        ivRefresh = getActivity().findViewById(R.id.homeRefreshButton);
        tvDate = getActivity().findViewById(R.id.tvHomeDate);

        // populare initiala a listview
        populateListView(requestDate);

        // event de click pe butonul de refresh
        ivRefresh.setOnClickListener(v -> populateListView(requestDate));

        // event de click pe textview cu data
        getActivity().findViewById(R.id.tvHomeDate).setOnClickListener(v -> showDatePickerDialog());

        // setare data curenta
        tvDate.setText(dateDisplayFormat.format(requestDate));
    }

    private void populateListView(Date requestDate) {
        // golire listview
        listView.setEmptyView(null);
        // afisare loader
        getActivity().findViewById(R.id.homeLoader).setVisibility(View.VISIBLE);

        try {
            URL url = new URL(getString(R.string.api_matches_link)
                    + "?dateFrom=" + dateRequestFormat.format(requestDate)
                    + "&dateTo=" + dateRequestFormat.format(requestDate));
            new MatchesRequest(this).execute(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showDatePickerDialog() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(requestDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                this,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
        // golire listview
        listView.setEmptyView(null);
        // afisare loader
        getActivity().findViewById(R.id.homeLoader).setVisibility(View.VISIBLE);

        try {
            requestDate = dateRequestFormat.parse(date);
            // update textview cu data
            tvDate.setText(dateDisplayFormat.format(requestDate));
            // populare listview
            populateListView(requestDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
