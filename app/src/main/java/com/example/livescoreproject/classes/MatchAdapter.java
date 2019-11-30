package com.example.livescoreproject.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.livescoreproject.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MatchAdapter extends BaseAdapter {
    private Context context;
    private List<Match> matches;

    public MatchAdapter(Context context, List<Match> matches) {
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
        if(view == null) {
           viewHolder = new ViewHolder();
           view = LayoutInflater.from(context).inflate(R.layout.elem_matches, parent, false);
           viewHolder.tvTime = view.findViewById(R.id.tvHomeMatchTime);
           viewHolder.tvHometeam = view.findViewById(R.id.tvHomeHometeam);
           viewHolder.tvAwayteam = view.findViewById(R.id.tvHomeAwayteam);
           viewHolder.tvScore = view.findViewById(R.id.tvHomeMatchScore);

           view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        Match  match = matches.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        viewHolder.tvTime.setText(sdf.format(match.getDate()));
        viewHolder.tvHometeam.setText(match.getHomeTeam());
        viewHolder.tvAwayteam.setText(match.getAwayTeam());
        viewHolder.tvScore.setText(match.getHomeTeamScore() + " - " + match.getAwayTeamScore());

        return view;
    }

    static class ViewHolder {
        TextView tvTime, tvHometeam, tvAwayteam, tvScore;
    }
}
