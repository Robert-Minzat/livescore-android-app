package com.example.livescoreproject.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.livescoreproject.R;
import com.example.livescoreproject.activities.MainActivity;

import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

public class AboutFragment extends Fragment {
    private ImageView ivLogoAse;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ivLogoAse = getActivity().findViewById(R.id.ivLogoAse);

        try {
            new LogoTask((MainActivity) getActivity()).execute(new URL(getActivity().getString(R.string.logo_ase)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class LogoTask extends AsyncTask<URL, Void, Bitmap> {
        private WeakReference<MainActivity> mainActivityWeakReference;

        LogoTask(MainActivity context) {
            mainActivityWeakReference = new WeakReference<>(context);
        }

        @Override
        protected Bitmap doInBackground(URL... urls) {
            try {
                URLConnection conn = urls[0].openConnection();
                return BitmapFactory.decodeStream(conn.getInputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ivLogoAse.setImageBitmap(bitmap);
            super.onPostExecute(bitmap);
        }
    }
}
