package com.sdm.mgp_assignment;

import android.view.MotionEvent;

// Manages the touch events
public class TouchManager
{
    public final static TouchManager Instance = new TouchManager();

    private TouchManager()
    {
    }

    public enum TouchState
    {
        NONE,
        DOWN,
        MOVE
    }

    private int posX, posY;
    private int newPosX, newPosY;  // Added variables to track new touch position
    private TouchState status = TouchState.NONE;

    public boolean HasTouch()
    {
        return status == TouchState.DOWN || status == TouchState.MOVE;
    }

    public boolean IsDown()
    {
        return status == TouchState.DOWN;
    }

    public int GetPosX()
    {
        return posX;
    }

    public int GetPosY()
    {
        return posY;
    }

    public boolean IsSwipeUp()
    {
        return posY < newPosY;  // Check if the new Y position is above the initial Y position
    }

    public boolean IsSwipeDown()
    {
        return posY > newPosY;  // Check if the new Y position is below the initial Y position
    }

    public void Update(int _posX, int _posY, int _motionEventStatus)
    {
        switch (_motionEventStatus)
        {
            case MotionEvent.ACTION_DOWN:
                status = TouchState.DOWN;
                newPosX = _posX;
                newPosY = _posY;
                break;

            case MotionEvent.ACTION_MOVE:
                status = TouchState.MOVE;
                break;

            case MotionEvent.ACTION_UP:
                status = TouchState.NONE;
                break;
        }

        posX = _posX;
        posY = _posY;
    }
}