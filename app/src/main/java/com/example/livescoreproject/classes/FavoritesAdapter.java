package com.example.livescoreproject.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.livescoreproject.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FavoritesAdapter extends BaseAdapter {
    private Context context;
    private List<FavoriteMatch> matches;

    public FavoritesAdapter(Context context, List<FavoriteMatch> matches) {
        this.context = context;
        this.matches = matches;
    }

    @Override
    public int getCount() {
        return matches.size();
    }

    @Override
    public Object getItem(int position) {
        return matches.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.elem_favorites, parent, false);
            viewHolder.tvTime = view.findViewById(R.id.tvFavMatchTime);
            viewHolder.tvHometeam = view.findViewById(R.id.tvFavHometeam);
            viewHolder.tvAwayteam = view.findViewById(R.id.tvFavAwayteam);
            viewHolder.tvScore = view.findViewById(R.id.tvFavMatchScore);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        FavoriteMatch match = matches.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
        viewHolder.tvTime.setText(sdf.format(match.getDate()));
        viewHolder.tvHometeam.setText(match.getHomeTeam());
        viewHolder.tvAwayteam.setText(match.getAwayTeam());
        viewHolder.tvScore.setText(match.getScore());
        return view;
    }

    static class ViewHolder {
        TextView tvTime, tvHometeam, tvAwayteam, tvScore;
    }
}
