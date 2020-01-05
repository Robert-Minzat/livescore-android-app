package com.example.livescoreproject.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.livescoreproject.DB.FavoriteMatchDao;
import com.example.livescoreproject.DB.LivescoreDB;
import com.example.livescoreproject.R;
import com.example.livescoreproject.activities.MainActivity;
import com.example.livescoreproject.classes.FavoriteMatch;
import com.example.livescoreproject.classes.FavoritesAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

public class FavoritesFragment extends Fragment {
    private ListView listView;
    private int userId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userId = getArguments().getInt("userId");
        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        listView = getActivity().findViewById(R.id.listViewFavorites);

        new FavoriteMatchesTask((MainActivity) getActivity()).execute(userId);
    }

    private class FavoriteMatchesTask extends AsyncTask<Integer, Void, List<FavoriteMatch>> {
        private WeakReference<MainActivity> mainActivityWeakReference;

        FavoriteMatchesTask(MainActivity context) {
            mainActivityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected void onPostExecute(List<FavoriteMatch> favoriteMatches) {
            MainActivity activity = mainActivityWeakReference.get();

            if (activity == null || activity.isFinishing())
                return;

            FavoritesAdapter favoritesAdapter = new FavoritesAdapter(getActivity().getApplicationContext(), favoriteMatches);
            listView.setAdapter(favoritesAdapter);

            listView.setOnItemLongClickListener((parent, view, position, id) -> {
                FavoriteMatch match = (FavoriteMatch)parent.getItemAtPosition(position);
                favoriteMatches.remove(match);

                new DeleteTask(activity, favoritesAdapter).execute(match);
                return true;
            });
        }

        @Override
        protected List<FavoriteMatch> doInBackground(Integer... ints) {
            FavoriteMatchDao favoriteMatchDao = LivescoreDB.getInstance(mainActivityWeakReference.get().getApplicationContext()).getFavoriteMatchDao();
            List<FavoriteMatch> favoriteMatchList = favoriteMatchDao.getFavoriteMatchesbyUserId(ints[0]);
            return favoriteMatchList;
        }

        private class DeleteTask extends AsyncTask<FavoriteMatch, Void, Void> {
            private WeakReference<MainActivity> mainActivityWeakReference;
            private FavoritesAdapter favoritesAdapter;

            DeleteTask(MainActivity context, FavoritesAdapter favoritesAdapter) {
                mainActivityWeakReference = new WeakReference<>(context);
                this.favoritesAdapter = favoritesAdapter;
            }

            @Override
            protected Void doInBackground(FavoriteMatch... matches) {
                FavoriteMatchDao favoriteMatchDao = LivescoreDB.getInstance(mainActivityWeakReference.get().getApplicationContext()).getFavoriteMatchDao();
                favoriteMatchDao.delete(matches[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                favoritesAdapter.notifyDataSetChanged();
                Toast.makeText(mainActivityWeakReference.get(), "Match removed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
