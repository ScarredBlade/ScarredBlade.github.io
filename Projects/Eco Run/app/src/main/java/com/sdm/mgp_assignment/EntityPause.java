package com.sdm.mgp_assignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;

public class EntityPause implements EntityBase
{
    private Bitmap bmpP, bmpP1;  // Bitmaps for the pause button in normal and pressed states
    private Bitmap ScaledbmpP= null, ScaledbmpP1 = null;  // Scaled bitmaps based on screen dimensions
    private float xPos, yPos;  // Position of the pause button
    private float buttonDelay;  // Delay between consecutive button presses
    int ScreenWidth, ScreenHeight;
    private boolean isDone = false;
    private boolean isInit = false;
    private boolean isPaused = false;  // Bool indicating whether the game is paused

    public static EntityPause Instance = null;

    /**
     * Creates and returns an instance of EntityPause.
     * Adds the instance to the entity manager.
     *
     * @return An instance of EntityPause.
     */
    public static EntityPause Create()
    {
        EntityPause result = new EntityPause();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_PAUSE);
        return result;
    }

    @Override
    public boolean IsDone()
    {
        return isDone;
    }

    @Override
    public void SetIsDone(boolean _isDone)
    {
        isDone = _isDone;
    }

    @Override
    public void Init(SurfaceView _view)
    {
        // Load bitmaps for the pause button
        bmpP = ResourceManager.Instance.GetBitmap(R.drawable.pause);
        bmpP1 = ResourceManager.Instance.GetBitmap(R.drawable.pause1);

        // Get screen dimensions
        DisplayMetrics metrics = _view.getResources().getDisplayMetrics();
        ScreenWidth = metrics.widthPixels;
        ScreenHeight = metrics.heightPixels;

        // Scale bitmaps based on screen dimensions
        ScaledbmpP = Bitmap.createScaledBitmap(bmpP, ScreenWidth/14, ScreenHeight/7, true);
        ScaledbmpP1 = Bitmap.createScaledBitmap(bmpP1, ScreenWidth/14, ScreenHeight/7, true);

        // Position the pause button in the top right corner of the screen
        xPos = ScreenWidth - 150;
        yPos = 150;

        isInit = true;
    }

    @Override
    public void Update(float _dt)
    {
        // Update button delay for touch input
        buttonDelay += _dt;

        // Check for touch input
        if(TouchManager.Instance.HasTouch())
        {
            if(TouchManager.Instance.IsDown() && !isPaused)
            {
                // Calculate the radius of the pause button
                float imgRadius = ScaledbmpP.getHeight() * 0.5f;

                // Check for collision with touch input and activate the pause button
                if(Collision.SphereToSphere((TouchManager.Instance.GetPosX()), TouchManager.Instance.GetPosY(), 0.0f, xPos, yPos, imgRadius) && buttonDelay >= 0.25f)
                {
                    isPaused = true;

                    // Check if the pause confirmation dialog is already shown
                    if(PauseConfirmDialogFragment.IsShown)
                    {
                        return;
                    }

                    // Show the pause confirmation dialog
                    PauseConfirmDialogFragment newPause = new PauseConfirmDialogFragment();
                    newPause.show(GamePage.Instance.getSupportFragmentManager(), "PauseConfirm");
                }
                buttonDelay = 0;
            }
        }
        else
        {
            isPaused = false;
        }
    }

    @Override
    public void Render(Canvas _canvas)
    {
        // Render the appropriate pause button based on the game's pause state
        if (isPaused == false)
        {
            _canvas.drawBitmap(ScaledbmpP, xPos - ScaledbmpP.getWidth() * 0.5f, yPos - ScaledbmpP.getHeight() * 0.5f, null);
        }
        else
        {
            _canvas.drawBitmap(ScaledbmpP1, xPos - ScaledbmpP1.getWidth() * 0.5f, yPos - ScaledbmpP1.getHeight() * 0.5f, null);
        }
    }

    @Override
    public boolean IsInit()
    {
        return isInit;
    }

    @Override
    public int GetRenderLayer()
    {
        return LayerConstants.UI_LAYER;
    }

    @Override
    public void SetRenderLayer(int _newLayer)
    {
    }

    @Override
    public ENTITY_TYPE GetEntityType()
    {
        return ENTITY_TYPE.ENT_PAUSE;
    }

    @Override
    public void RenderRadius(Canvas _canvas)
    {
    }

    @Override
    public void RenderCuboid(Canvas _canvas)
    {
    }
}