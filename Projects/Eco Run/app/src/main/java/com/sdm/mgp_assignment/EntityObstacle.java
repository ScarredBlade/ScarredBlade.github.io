package com.sdm.mgp_assignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.VibrationEffect;
import android.view.SurfaceView;
import android.os.Vibrator;

import java.util.Random;

public class EntityObstacle implements EntityBase, Collidable
{
    private Bitmap bmp = null;
    private boolean isDone = false;
    private boolean isInit = false;
    private float xPos, yPos;
    private float screenWidth, screenHeight;
    private float obstacleSpeed = 20.0f;
    public static boolean hasCollided = false;
    public static boolean renderObstacle = true;
    Vibrator _vibrator;

    public static EntityObstacle Create()
    {
        EntityObstacle result = new EntityObstacle();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_OBSTACLE);
        return result;
    }

    private void ResetObstaclePosition()
    {
        renderObstacle = true;
        float randomOffset = new Random().nextFloat() * 200; // Adjust the offset as needed
        boolean generateAbove = new Random().nextBoolean();
        if (generateAbove)
        {
            yPos = screenHeight * 0.55f - randomOffset;
        }
        else
        {
            yPos = screenHeight * 0.55f + randomOffset;
        }
    }

    public static boolean getRenderObstacle()
    {
        return renderObstacle;
    }

    public void startVibrate()
    {
        if (Build.VERSION.SDK_INT >= 26)
        {
            _vibrator.vibrate(VibrationEffect.createOneShot(150, 100));
        }
        else
        {
            long pattern[] = {0, 50, 0};
            _vibrator.vibrate(pattern, -1);
        }
    }

    // COLLIDABLE INTERFACE FUNCTIONS
    @Override
    public String GetType()
    {
        return "ObstacleEntity";
    }

    @Override
    public float GetPosX()
    {
        return xPos;
    }

    @Override
    public float GetPosY()
    {
        return yPos;
    }

    @Override
    public float GetRadius()
    {
        return 40;
    }

    @Override
    public float GetWidth()
    {
        return bmp.getWidth();
    }

    @Override
    public float GetHeight()
    {
        return bmp.getHeight();
    }

    @Override
    public void OnHit(Collidable _other)
    {
        if (_other.GetType() == ("PlayerEntity"))
        {
            System.out.println("hit!");
            renderObstacle = false;
        }
    }

    // ENTITY BASE INTERFACE FUNCTIONS
    @Override
    public boolean IsDone()
    {
        return isDone;
    }

    @Override
    public void SetIsDone(boolean _isDone)
    {
        isDone  = _isDone;
    }

    @Override
    public void Init(SurfaceView _view)
    {
        // Load the obstacle image (garbage.png) and scale it to a smaller size
        Bitmap originalBitmap = BitmapFactory.decodeResource(_view.getResources(), R.drawable.garbage);
        float scale = 0.04f; // Adjust the scale factor as needed
        bmp = Bitmap.createScaledBitmap(originalBitmap, (int) (originalBitmap.getWidth() * scale), (int) (originalBitmap.getHeight() * scale), true);

        screenWidth = _view.getWidth();
        screenHeight = _view.getHeight();

        // Set the initial position outside the right of the screen
        xPos = screenWidth + screenWidth * 0.1f;

        // Set the initial position either slightly above or below the player
        ResetObstaclePosition();

        hasCollided = false;
        _vibrator = (Vibrator)_view.getContext().getSystemService (_view.getContext().VIBRATOR_SERVICE);

        isInit = true;
    }

    @Override
    public void Update(float _dt)
    {
        // Update if game is not paused
        if (!GameSystem.Instance.GetIsPaused())
        {
            // Move the obstacle from right to left
            xPos -= obstacleSpeed;

            // Reset the obstacle position if it goes off the left side of the screen
            if (xPos + bmp.getWidth() < 0)
            {
                renderObstacle = false;

                // Randomize the position again
                ResetObstaclePosition();
                xPos = screenWidth + screenWidth * 0.1f;

                // Check if the obstacle exited without collision and update the score
                if (!hasCollided)
                {
                    EntityManager.Instance.GetScoreEntity().IncreaseScore();
                    startVibrate();
                }
                else
                {
                    // Reset the collision bool
                    hasCollided = false;
                }
            }
        }
    }

    @Override
    public void Render(Canvas _canvas)
    {
        if (renderObstacle == true)
        {
            // Render the obstacle on the canvas
            _canvas.drawBitmap(bmp, xPos, yPos, null);
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
        return LayerConstants.GAMEOBJECTS_LAYER;
    }

    @Override
    public void SetRenderLayer(int _newLayer)
    {
    }

    @Override
    public ENTITY_TYPE GetEntityType()
    {
        return ENTITY_TYPE.ENT_OBSTACLE;
    }

    @Override
    public void RenderRadius(Canvas _canvas)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);

        // Calculate the center position based on the obstacle's position and size
        float centerX = xPos + 0.5f * bmp.getWidth();
        float centerY = yPos + 0.5f * bmp.getHeight();

        _canvas.drawCircle(centerX, centerY, GetRadius(), paint);
    }

    @Override
    public void RenderCuboid(Canvas _canvas)
    {
    }
}