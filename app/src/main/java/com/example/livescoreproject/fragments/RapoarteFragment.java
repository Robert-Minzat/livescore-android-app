package com.example.livescoreproject.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.livescoreproject.DB.FavoriteMatchDao;
import com.example.livescoreproject.DB.LivescoreDB;
import com.example.livescoreproject.DB.UserDao;
import com.example.livescoreproject.R;
import com.example.livescoreproject.activities.MainActivity;

import java.lang.ref.WeakReference;
import java.util.List;

public class RapoarteFragment extends Fragment {
    TextView tvMatches2018Value, tvMatches2019Value, tvMatches2020Value, tvUsersUnder18Value, tvUsersOver18Value;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rapoarte, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        tvMatches2018Value = getActivity().findViewById(R.id.tvMatches2018Value);
        tvMatches2019Value = getActivity().findViewById(R.id.tvMatches2019Value);
        tvMatches2020Value = getActivity().findViewById(R.id.tvMatches2020Value);
        tvUsersUnder18Value = getActivity().findViewById(R.id.tvUsersUnder18Value);
        tvUsersOver18Value = getActivity().findViewById(R.id.tvUsersOver18Value);

        new GetNoMatchesByYearTask((MainActivity)getActivity()).execute(2018,2019,2020);
        new GetUsersOverAndUnder18Task((MainActivity)getActivity()).execute();
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
                String yearBegin= ints[i] + "-01-01";
                String yearEnd = ints[i] + "-12-31";
                matches[i] = favoriteMatchDao.getNoFavoriteMatchesByYear(yearBegin, yearEnd);
            }
            return matches;
        }

        @Override
        protected void onPostExecute(Integer[] integers) {
            tvMatches2018Value.setText(integers[0].toString());
            tvMatches2019Value.setText(integers[1].toString());
            tvMatches2020Value.setText(integers[2].toString());
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
