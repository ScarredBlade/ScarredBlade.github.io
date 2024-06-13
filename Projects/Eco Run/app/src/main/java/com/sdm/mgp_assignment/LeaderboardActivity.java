package com.sdm.mgp_assignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeaderboardActivity extends Activity implements StateBase
{
    // List to store leaderboard entries
    List<LeaderboardListData> leaderboardEntries = new ArrayList<LeaderboardListData>();

    // Button to go back to the main menu
    private Button btnBackToMainMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard_activity);

        // Set leaderboard data
        getLeaderboardData();

        // Set up the ListView and adapter
        ListView listView = findViewById(R.id.listViewLeaderboard);
        LeaderboardListAdapter leaderboardAdapter = new LeaderboardListAdapter(this, leaderboardEntries);
        listView.setAdapter(leaderboardAdapter);

        // Initialize and set click listener for the back button
        btnBackToMainMenu = findViewById(R.id.btnBackToMainMenu);
        btnBackToMainMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Navigate back to the main menu
                Intent intent = new Intent(LeaderboardActivity.this, MainMenu.class);
                StateManager.Instance.ChangeState("MainMenuState");
                startActivity(intent);
            }
        });
    }

    /**
     * Retrieves and sets the leaderboard data from SharedPreferences.
     */
    private void getLeaderboardData()
    {
        // Clear the existing leaderboard entries
        leaderboardEntries.clear();

        // Access SharedPreferences for leaderboard data
        SharedPreferences sharedPreferences = getSharedPreferences("LeaderboardPrefs", Context.MODE_PRIVATE);

        // Retrieve all entries from SharedPreferences
        Map<String, ?> allEntries = sharedPreferences.getAll();

        // Iterate through entries and add them to the leaderboard
        for (Map.Entry<String, ?> entry : allEntries.entrySet())
        {
            String username = entry.getKey();
            int highScore = (int) entry.getValue();
            leaderboardEntries.add(new LeaderboardListData(username, highScore));
        }
    }

    @Override
    public String GetName()
    {
        return "LeaderboardState";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
    }

    @Override
    public void OnExit()
    {
    }

    @Override
    public void Render(Canvas _canvas)
    {
    }

    @Override
    public void Update(float _dt)
    {
    }
}