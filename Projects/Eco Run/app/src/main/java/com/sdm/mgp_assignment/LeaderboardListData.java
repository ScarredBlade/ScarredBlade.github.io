package com.sdm.mgp_assignment;

// Class representing data for a single entry in the leaderboard
public class LeaderboardListData
{
    public String name;  // Username associated with the entry
    public int score;     // High score associated with the entry

    // Constructor to initialize the LeaderboardListData with a username and high score
    public LeaderboardListData(String username, int highscore)
    {
        this.name = username;
        this.score = highscore;
    }
}