package com.sdm.mgp_assignment;

import android.graphics.Canvas;
import android.graphics.Color;
import android.view.SurfaceHolder;

// Need a dedicated thread to run Surfaceview's update method
public class UpdateThread extends Thread
{
    static final long targetFPS = 60;
    private GameView view = null; //Gameview = Surfaceview
    private SurfaceHolder holder = null;
    private boolean isRunning = false;

    public UpdateThread(GameView _view)
    {
        view = _view;
        holder = _view.getHolder();

		// Manage your managers if there is any classes that deals with view.
        StateManager.Instance.Init(_view);
        EntityManager.Instance.Init(_view);
        GameSystem.Instance.Init(_view);
        ResourceManager.Instance.Init(_view);
        AudioManager.Instance.Init(_view);
    }

    public boolean IsRunning()
    {
        return isRunning;
    }

    public void Initialize()
    {
        isRunning = true;
    }

    public void Terminate()
    {
        isRunning = false;
    }

    //Every game thread will always have a run() method
    @Override
    public void run()
    {
        // This is for frame rate control
        long framePerSecond = 1000 / targetFPS; // 1000 is milliseconds --> 1 second
        long startTime = 0;

        // This is to calculate delta time (more precise)
        long prevTime = System.nanoTime();

        // MainGameSceneState --- level 1
        // MainGame -- State name.
        StateManager.Instance.Start("MainGameState"); // To edit to whichever state to start with.

        // This is the game loop
        while (isRunning && StateManager.Instance.GetCurrentState() != "INVALID")
        {
            startTime = System.currentTimeMillis();
            long currTime = System.nanoTime();
            float deltaTime = (float) ((currTime - prevTime) / 1000000000.0f);
            prevTime = currTime;

            // Update
            StateManager.Instance.Update(deltaTime);

            // Render
            Canvas canvas = holder.lockCanvas(null);
            if (canvas != null)
            {
                // Able to render
                synchronized (holder)
                {
                    // Fill the background color to reset
                    canvas.drawColor(Color.BLACK);

                    StateManager.Instance.Render(canvas);
                }
                holder.unlockCanvasAndPost(canvas);
            }

            // Frame rate controller
            try
            {
                long sleepTime = framePerSecond - (System.currentTimeMillis() - startTime);

                if (sleepTime > 0)
                {
                    sleep(sleepTime);
                }
            }
            catch (InterruptedException e)
            {
                isRunning = false;
                Terminate();
            }

            // End of Loop
        }
    }
}