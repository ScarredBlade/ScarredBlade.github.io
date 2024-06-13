package com.sdm.mgp_assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import androidx.fragment.app.FragmentActivity;

public class GamePage extends FragmentActivity
{
    public static GamePage Instance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Instance = this;
        setContentView(new GameView(this)); // Surfaceview = GameView
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // WE are hijacking the touch event into our own system
        int x = (int) event.getX();
        int y = (int) event.getY();

        TouchManager.Instance.Update(x, y, event.getAction());

        return true;
    }

    public void changeToGameOverPage()
    {
        Intent intent = new Intent(this, GameOverPage.class);
        StateManager.Instance.ChangeState("GameOverState");
        startActivity(intent);
    }
}