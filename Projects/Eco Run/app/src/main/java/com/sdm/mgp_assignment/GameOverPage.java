package com.sdm.mgp_assignment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameOverPage extends Activity implements View.OnClickListener, StateBase
{
    private int score = 0;
    public static GameOverPage Instance = null;

    TextView tvGameOver;
    TextView tvHighScore;
    Button btnRestart;
    Button btnMainMenu;

    SharedPreferences sharedPreferences;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Instance = this;
        setContentView(R.layout.game_over);

        // Initialize UI elements
        tvGameOver = findViewById(R.id.tvGameOver);
        tvHighScore = findViewById(R.id.tvHighScore);
        btnRestart = findViewById(R.id.btnRestart);
        btnMainMenu = findViewById(R.id.btnMainMenu);

        btnRestart.setOnClickListener(this);
        btnMainMenu.setOnClickListener(this);

        // Display the high score
        tvHighScore.setText("High Score: " + ScoreManager.getScore());

        // Initialize SharedPreferences for leaderboard
        sharedPreferences = getSharedPreferences("LeaderboardPrefs", Context.MODE_PRIVATE);

        // Show dialog to input username
        showUsernameInputDialog();
    }

    @Override
    public void onClick(View v)
    {
        // Handle button clicks
        if (v == btnRestart)
        {
            // Restart the game
            Intent intent = new Intent(this, GamePage.class);
            StateManager.Instance.ChangeState("MainGameState");
            startActivity(intent);

            // Reset scores
            ScoreManager.score = 0;
            RenderTextEntity.score = 0;
        }
        else if (v == btnMainMenu)
        {
            // Return to the main menu
            Intent intent = new Intent(this, MainMenu.class);
            StateManager.Instance.ChangeState("MainMenuState");
            startActivity(intent);

            // Reset scores
            ScoreManager.score = 0;
            RenderTextEntity.score = 0;
        }
    }

    @Override
    public String GetName()
    {
        return "GameOverState";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
    }

    @Override
    public void OnExit()
    {
        // Finish the GameOverPage activity
        GameOverPage.Instance.finish();
    }

    @Override
    public void Render(Canvas _canvas)
    {
    }

    @Override
    public void Update(float _dt)
    {
    }

    /**
     * Display a dialog to input the username.
     */
    private void showUsernameInputDialog()
    {
        final Dialog usernameDialog = new Dialog(this);
        usernameDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        usernameDialog.setContentView(R.layout.username_alert);

        final EditText etUsername = usernameDialog.findViewById(R.id.etUsername);
        Button btnSaveUsername = usernameDialog.findViewById(R.id.btnSaveUsername);

        btnSaveUsername.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Save the username and score to the leaderboard
                username = etUsername.getText().toString().trim();
                saveScore(username);
                usernameDialog.dismiss();
            }
        });

        // Show the username input dialog
        usernameDialog.show();
    }

    /**
     * Save the username to SharedPreferences.
     * @param username The username to be saved.
     */
    private void saveUsername(String username)
    {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username", username);
        editor.apply();
    }

    /**
     * Save the score to the leaderboard in SharedPreferences.
     * @param username The username associated with the score.
     */
    private void saveScore(String username)
    {
        SharedPreferences sharedPreferences = getSharedPreferences("LeaderboardPrefs", Context.MODE_PRIVATE);

        // Check if the username already exists in the leaderboard
        if (sharedPreferences.contains(username))
        {
            // Username exists, compare the scores
            int existingScore = sharedPreferences.getInt(username, 0);
            int newScore = ScoreManager.getScore();

            if (newScore > existingScore)
            {
                // Update the score if the new score is higher
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(username, newScore);
                editor.apply();
            }
        }
        else
        {
            // Username doesn't exist, save the new score
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(username, ScoreManager.getScore());
            editor.apply();
        }
    }
}