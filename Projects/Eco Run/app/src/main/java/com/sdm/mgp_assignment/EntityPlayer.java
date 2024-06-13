package com.sdm.mgp_assignment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

public class EntityPlayer implements EntityBase, Collidable
{
    private Bitmap bmp = null;
    private boolean isDone = false;
    private boolean isInit = false;
    private float xPos, yPos, xDir, yDir;
    private boolean hasTouched = false;
    private Sprite spritesheet = null;
    private static final int RUNNING_START_FRAME = 7;
    private static final int RUNNING_END_FRAME = 14;
    private static final int SLIDING_START_FRAME = 87;
    private static final int SLIDING_END_FRAME = 91;
    private static final int JUMPING_START_FRAME = 42;
    private static final int JUMPING_END_FRAME = 49;
    private float screenWidth, screenHeight;
    private float hitboxX, hitboxY, hitboxWidth, hitboxHeight;
    private AnimationState currentAnimationState = AnimationState.RUNNING;
    private boolean isJumping = false;
    private boolean isSliding = false;

    private enum AnimationState
    {
        RUNNING,
        JUMPING,
        SLIDING
    }

    public static EntityPlayer Create()
    {
        EntityPlayer result = new EntityPlayer();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_PLAYER);
        return result;
    }

    public static EntityPlayer Create(int _layer)
    {
        EntityPlayer result = Create();
        result.SetRenderLayer(_layer);
        return result;
    }

    public void HandleAnimationState()
    {
        // Check and handle animation state
        if (currentAnimationState == AnimationState.JUMPING)
        {
            // Calculate the jump motion using a half sine wave
            float jumpHeight = -500; // Adjust the jump height as needed
            float jumpDuration = 1.5f; // Adjust the jump duration as needed

            float jumpProgress = (spritesheet.GetCurrentFrame() - JUMPING_START_FRAME) / (float)(JUMPING_END_FRAME - JUMPING_START_FRAME);
            float yOffset = jumpHeight * (float) Math.sin(Math.PI * jumpProgress);

            // Move smoothly between jumping and landing
            if (jumpProgress < 0.5f)
            {
                // If still ascending, apply the half sine wave motion
                yPos = screenHeight * 0.6f + yOffset;
            }
            else
            {
                // If descending, smoothly transition back to the original height
                float landingProgress = (jumpProgress - 0.5f) / 0.5f; // Normalize landing progress
                yPos = screenHeight * 0.6f + yOffset * (1.0f - landingProgress);
            }

            // Check if the jump animation has completed
            if (spritesheet.GetCurrentFrame() == spritesheet.GetEndFrame() - 1)
            {
                isJumping = false;
                // Switch back to running animation
                currentAnimationState = AnimationState.RUNNING;
                spritesheet.SetAnimationFrames(RUNNING_START_FRAME, RUNNING_END_FRAME);
            }
        }
        else if (currentAnimationState == AnimationState.SLIDING)
        {
            // Check if the sliding animation has completed
            if (spritesheet.GetCurrentFrame() == spritesheet.GetEndFrame() - 1)
            {
                isSliding = false;
                // Switch back to running animation
                currentAnimationState = AnimationState.RUNNING;
                spritesheet.SetAnimationFrames(RUNNING_START_FRAME, RUNNING_END_FRAME);

                // Adjust hitbox height back to normal when sliding animation is complete
                hitboxHeight = spritesheet.GetHeight() + 100;
                // Adjust hitbox position relative to the entity back to normal when sliding animation is complete
                hitboxY = 0.0f;
            }
            else
            {
                // While sliding, reduce the hitbox height
                hitboxHeight = spritesheet.GetHeight() * 1.2f;
                // While sliding, move the hitbox down
                hitboxY = 80.0f;
            }
        }
    }

    // COLLIDABLE INTERFACE FUNCTIONS
    @Override
    public String GetType()
    {
        return "PlayerEntity";
    }

    @Override
    public float GetPosX()
    {
        return xPos + hitboxX;
    }

    @Override
    public float GetPosY()
    {
        return yPos + hitboxY;
    }

    @Override
    public float GetRadius()
    {
        return 0;
    }

    @Override
    public float GetWidth()
    {
        return hitboxWidth;
    }

    @Override
    public float GetHeight()
    {
        return hitboxHeight;
    }

    @Override
    public void OnHit(Collidable _other)
    {
        if (_other.GetType() == "ObstacleEntity")
        {
            System.out.println("hit!");
            EntityObstacle.hasCollided = true;
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
        // New method using our own resource manager: Returns pre-loaded one if exists
        // 2. Loading spritesheet
        bmp = BitmapFactory.decodeResource(_view.getResources(), R.drawable.player_sprite);
        spritesheet = new Sprite(bmp, 17, 6, 1.2f);

        // Start with the running animation
        spritesheet.SetAnimationFrames(RUNNING_START_FRAME, RUNNING_END_FRAME);

        screenWidth = _view.getWidth();
        screenHeight = _view.getHeight();

        // Set initial hitbox dimensions and position relative to the entity
        hitboxX = -50.0f; // Adjust as needed
        hitboxY = 0.0f; // Adjust as needed
        hitboxWidth = spritesheet.GetWidth();
        hitboxHeight = spritesheet.GetHeight() + 100;

        // Set the initial position to the bottom left of the screen
        xPos = screenWidth * 0.1f;
        yPos = screenHeight * 0.55f;  // Adjust to start at the middle left

        xDir = 0.0f;  // No initial horizontal movement
        yDir = 0.0f;  // No initial vertical movement

        isInit = true;
    }

    @Override
    public void Update(float _dt)
    {
        // Update if game is not paused
        if (!GameSystem.Instance.GetIsPaused())
        {
            // 4. Update spritesheet
            spritesheet.Update(_dt);

            // 5. Deal with the touch on screen for interaction of the image using collision check
            if (TouchManager.Instance.HasTouch() && !isJumping && !isSliding)
            {
                // Collided!
                hasTouched = true;

                // Swipe up
                if (TouchManager.Instance.IsSwipeUp() && currentAnimationState != AnimationState.JUMPING && !isSliding)
                {
                    AudioManager.Instance.PlaySFX(R.raw.jump);
                    spritesheet.SetAnimationFrames(JUMPING_START_FRAME, JUMPING_END_FRAME);
                    currentAnimationState = AnimationState.JUMPING;
                    isJumping = true;
                }
                // Swipe down
                else if (TouchManager.Instance.IsSwipeDown() && currentAnimationState != AnimationState.SLIDING && !isJumping)
                {
                    AudioManager.Instance.PlaySFX(R.raw.slide);
                    spritesheet.SetAnimationFrames(SLIDING_START_FRAME, SLIDING_END_FRAME);
                    currentAnimationState = AnimationState.SLIDING;
                    isSliding = true;
                }
            }

            HandleAnimationState();
        }
    }

    @Override
    public void Render(Canvas _canvas)
    {
        // This is for our sprite animation!
        spritesheet.Render(_canvas, (int)xPos, (int)yPos, 3.0f);
    }

    @Override
    public boolean IsInit()
    {
        return isInit;
    }

    @Override
    public int GetRenderLayer()
    {
        return LayerConstants.PLAYER_LAYER;
    }

    @Override
    public void SetRenderLayer(int _newLayer)
    {
    }

    @Override
    public ENTITY_TYPE GetEntityType()
    {
        return ENTITY_TYPE.ENT_PLAYER;
    }

    @Override
    public void RenderRadius(Canvas _canvas)
    {
    }

    @Override
    public void RenderCuboid(Canvas _canvas)
    {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.RED);
        paint.setStrokeWidth(2);

        _canvas.drawRect(GetPosX(), GetPosY(), GetPosX() + GetWidth(), GetPosY() + GetHeight(), paint);
    }
}