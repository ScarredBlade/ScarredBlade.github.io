package com.sdm.mgp_assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LeaderboardListAdapter extends ArrayAdapter<LeaderboardListData>
{
    private Context context;

    // Constructor for LeaderboardListAdapter
    public LeaderboardListAdapter(Context context, List<LeaderboardListData> dataArrayList)
    {
        super(context, 0, dataArrayList);
        // Sort and allocate ranks when constructing the adapter
        sortAllocateRanks(dataArrayList);
        this.context = context;
    }

    // Override the getView method to customize the appearance of each item in the ListView
    @Override
    public View getView(int position, View view, ViewGroup parent)
    {
        // Get the data for the current position
        LeaderboardListData listData = getItem(position);

        // Inflate the layout if the view is null
        if (view == null)
        {
            view = LayoutInflater.from(getContext()).inflate(R.layout.leaderboard_item, parent, false);
        }

        // Get references to TextViews in the layout
        TextView listRank = view.findViewById(R.id.rankTextView);
        TextView listName = view.findViewById(R.id.usernameTextView);
        TextView listScore = view.findViewById(R.id.highScoreTextView);

        // Set background color
        view.setBackgroundColor(android.graphics.Color.parseColor("#FFFACD")); // Cream color

        // Set text color
        listRank.setTextColor(context.getResources().getColor(android.R.color.black));
        listName.setTextColor(context.getResources().getColor(android.R.color.black));
        listScore.setTextColor(context.getResources().getColor(android.R.color.black));

        // Set text and other properties
        listRank.setText(String.valueOf(position + 1));  // Display rank starting from 1
        listName.setText(listData.name);  // Display username
        listScore.setText(String.valueOf(listData.score));  // Display high score

        return view;
    }

    // Method to sort the leaderboard entries based on high scores and reallocate ranks
    public List<LeaderboardListData> sortAllocateRanks(List<LeaderboardListData> _users)
    {
        List<LeaderboardListData> users = _users;

        // Sort the list based on high scores (descending order)
        Collections.sort(users, new Comparator<LeaderboardListData>()
        {
            @Override
            public int compare(LeaderboardListData LeaderboardListData1, LeaderboardListData LeaderboardListData2)
            {
                return Integer.compare(LeaderboardListData2.score, LeaderboardListData1.score);
            }
        });

        // Notify the adapter that the data set has changed
        notifyDataSetChanged();

        return users;
    }
}