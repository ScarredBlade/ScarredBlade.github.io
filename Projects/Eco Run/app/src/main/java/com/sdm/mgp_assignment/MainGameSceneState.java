package com.sdm.mgp_assignment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.SurfaceView;

public class MainGameSceneState extends Activity implements StateBase
{
    private float timer = 0.0f;
    public static MainGameSceneState Instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public String GetName()
    {
        return "MainGameState";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
        // 3. Create Background
        RenderBackground.Create();

        // Render Pause Button
        EntityPause.Create();

        // Render player sprite
        EntityPlayer.Create();

        // Render obstacle sprite
        EntityObstacle.Create();

        // Resume background music when the app is resumed
        AudioManager.Instance.ResumeMusic();
    }

    @Override
    public void OnExit()
    {
        // 4. Clear any instance instantiated via EntityManager.
        EntityManager.Instance.Clean();

        // 5. Clear or end any instance instantiated via GamePage.
        GamePage.Instance.finish();

        // Pause background music when the app is paused
        AudioManager.Instance.PauseMusic();

        Instance = this;
    }

    @Override
    public void Render(Canvas _canvas)
    {
        EntityManager.Instance.Render(_canvas);
    }

    @Override
    public void Update(float _dt)
    {
        // Update all entities
        EntityManager.Instance.Update(_dt);

        if (TouchManager.Instance.IsDown())
        {
            //6. Example of touch on screen in the main game to trigger back to Main menu
            //StateManager.Instance.ChangeState("MainMenuState");
        }

        if (EntityObstacle.getRenderObstacle() == false)
        {
            ScoreManager.setScore();
            GamePage.Instance.changeToGameOverPage();
        }
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        // Pause background music when the app is paused
        AudioManager.Instance.PauseMusic();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        // Resume background music when the app is resumed
        AudioManager.Instance.ResumeMusic();
    }
}