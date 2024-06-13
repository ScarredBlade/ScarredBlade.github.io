package com.sdm.mgp_assignment;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.SurfaceView;

public class GameSystem
{
    public final static GameSystem Instance = new GameSystem();

    // Game stuff
    private boolean isPaused = false;

    // Singleton Pattern : Blocks others from creating
    private GameSystem()
    {
    }

    public void Update(float _deltaTime)
    {
    }

    public void Init(SurfaceView _view)
    {
        // 2. We will add all of our states into the state manager here!
        StateManager.Instance.AddState(new MainMenu());

        // Plese add state, MainGameSceneState.
        StateManager.Instance.AddState(new MainGameSceneState());

        // add state, GameOverPage.
        StateManager.Instance.AddState(new GameOverPage());

        // add state, LeaderboardActivity.
        StateManager.Instance.AddState(new LeaderboardActivity());
    }

    public void SetIsPaused(boolean _newIsPaused)
    {
        isPaused = _newIsPaused;
    }

    public boolean GetIsPaused()
    {
        return isPaused;
    }
}