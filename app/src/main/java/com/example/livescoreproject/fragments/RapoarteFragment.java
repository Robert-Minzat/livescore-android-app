package com.example.livescoreproject.fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.livescoreproject.DB.FavoriteMatchDao;
import com.example.livescoreproject.DB.LivescoreDB;
import com.example.livescoreproject.DB.UserDao;
import com.example.livescoreproject.R;
import com.example.livescoreproject.activities.MainActivity;
import com.example.livescoreproject.classes.RaportView;
import com.example.livescoreproject.classes.SharedPreferencesConfig;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;

public class RapoarteFragment extends Fragment {
    private TextView tvMatches2018Value, tvMatches2019Value, tvMatches2020Value, tvUsersUnder18Value, tvUsersOver18Value;
    private SharedPreferencesConfig sharedPreferences;
    private Button btnSaveReport;
    private RaportView raportView;
    private int count2018 = 0, count2019 = 0, count2020 = 0, sum = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        sharedPreferences = new SharedPreferencesConfig(getActivity());
        return inflater.inflate(R.layout.fragment_rapoarte, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvMatches2018Value = getActivity().findViewById(R.id.tvMatches2018Value);
        tvMatches2019Value = getActivity().findViewById(R.id.tvMatches2019Value);
        tvMatches2020Value = getActivity().findViewById(R.id.tvMatches2020Value);
        tvUsersUnder18Value = getActivity().findViewById(R.id.tvUsersUnder18Value);
        tvUsersOver18Value = getActivity().findViewById(R.id.tvUsersOver18Value);
        btnSaveReport = getActivity().findViewById(R.id.btnSaveReport);
        raportView = getActivity().findViewById(R.id.raportPieChart);

        btnSaveReport.setOnClickListener(v -> {
            FileOutputStream fos = null;
            try {
                fos = getActivity().openFileOutput("report.txt", Context.MODE_PRIVATE);
                String text =
                        getActivity().getString(R.string.number_of_favorite_matches_from_2018) + " " + tvMatches2018Value.getText() + "\n" +
                                getActivity().getString(R.string.number_of_favorite_matches_from_2018) + " " + tvMatches2019Value.getText() + "\n" +
                                getActivity().getString(R.string.number_of_favorite_matches_from_2018) + " " + tvMatches2020Value.getText() + "\n" +
                                "\n" +
                                getActivity().getString(R.string.users_under_18) + " " + tvUsersUnder18Value.getText() + "\n" +
                                getActivity().getString(R.string.users_over_18) + " " + tvUsersOver18Value.getText() + "\n";
                fos.write(text.getBytes());
                Toast.makeText(getActivity(), "Saved to " + getActivity().getFilesDir() + "/report.txt",
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        new GetNoMatchesByYearTask((MainActivity) getActivity()).execute(2018, 2019, 2020);
        new GetUsersOverAndUnder18Task((MainActivity) getActivity()).execute();
    }

    private class GetNoMatchesByYearTask extends AsyncTask<Integer, Integer, Integer[]> {
        private WeakReference<MainActivity> mainActivityWeakReference;

        GetNoMatchesByYearTask(MainActivity context) {
            mainActivityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Integer[] doInBackground(Integer... ints) {
            FavoriteMatchDao favoriteMatchDao = LivescoreDB.getInstance(mainActivityWeakReference.get().getApplicationContext()).getFavoriteMatchDao();
            Integer[] matches = new Integer[3];
            for (int i = 0; i < 3; i++) {
                String yearBegin = ints[i] + "-01-01";
                String yearEnd = ints[i] + "-12-31";
                matches[i] = favoriteMatchDao.getNoFavoriteMatchesByYear(yearBegin, yearEnd, sharedPreferences.readLoginId());
            }
            return matches;
        }

        @Override
        protected void onPostExecute(Integer[] integers) {
            count2018 = integers[0];
            count2019 = integers[1];
            count2020 = integers[2];
            sum = count2018 + count2019 + count2020;
            tvMatches2018Value.setText(integers[0].toString());
            tvMatches2019Value.setText(integers[1].toString());
            tvMatches2020Value.setText(integers[2].toString());
            if (sum == 0) {
                getActivity().findViewById(R.id.tvNoData).setVisibility(View.VISIBLE);
            }
            raportView.setDegrees_value(new float[]{360f / sum * count2018, 360f / sum * count2019, 360f / sum * count2020});
        }
    }

    private class GetUsersOverAndUnder18Task extends AsyncTask<Void, Integer[], Integer[]> {
        private WeakReference<MainActivity> mainActivityWeakReference;

        GetUsersOverAndUnder18Task(MainActivity context) {
            mainActivityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Integer[] doInBackground(Void... voids) {
            UserDao userDao = LivescoreDB.getInstance(mainActivityWeakReference.get().getApplicationContext()).getUserDao();
            Integer[] results = new Integer[2];
            results[0] = userDao.getUsersUnder18();
            results[1] = userDao.getUsersOver18();
            return results;
        }

        @Override
        protected void onPostExecute(Integer[] results) {
            tvUsersUnder18Value.setText(results[0].toString());
            tvUsersOver18Value.setText(results[1].toString());
        }
    }
}
