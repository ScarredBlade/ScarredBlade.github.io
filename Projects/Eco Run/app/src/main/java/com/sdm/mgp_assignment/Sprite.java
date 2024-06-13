package com.sdm.mgp_assignment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Sprite
{
    private int row = 0;
    private int col = 0;
    private int width = 0;
    private int height = 0;
    private Bitmap bmp = null;

    private int currentFrame = 0;
    private int startFrame = 0;
    private int endFrame = 0;

    private float timePerFrame = 0.0f;
    private float fps = 0.0f;
    private float timeAcc = 0.0f;

    public Sprite(Bitmap _bmp, int _row, int _col, float _fps)
    {
        bmp = _bmp;
        row = _row;
        col = _col;

        // Calculate the width and height of each individual frame in the sprite sheet
        width = bmp.getWidth() / _col;
        height = bmp.getHeight() / _row;

        // Calculate the time per frame based on the specified frames per second (_fps)
        fps = 1.0f / _fps;

        // Calculate the total number of frames in the sprite sheet
        endFrame = _col * _row;
    }

    public void Update(float _dt)
    {
        timeAcc += _dt;
        if (timeAcc > timePerFrame)
        {
            ++currentFrame;
            if (currentFrame == endFrame)
            {
                currentFrame = startFrame;
            }
            timeAcc = 0.0f;
        }
    }

    public void Render(Canvas _canvas, int _x, int _y, float scale)
    {
        // Calculate the source rectangle based on the current frame
        int frameX = currentFrame % col;
        int frameY = currentFrame / col;
        int srcX = frameX * width;
        int srcY = frameY * height;

        // Calculate the new width and height based on the scale factor
        int scaledWidth = (int) (width * scale);
        int scaledHeight = (int) (height * scale);

        // Center the sprite at the specified (_x, _y) position after the size change
        _x -= 0.5f * (scaledWidth - width);
        _y -= 0.5f * (scaledHeight - height);

        // Create source and destination rectangles for drawing the scaled sprite
        Rect src = new Rect(srcX, srcY, srcX + width, srcY + height);
        Rect dst = new Rect(_x, _y, _x + scaledWidth, _y + scaledHeight);

        // Draw the scaled sprite on the canvas
        _canvas.drawBitmap(bmp, src, dst, null);
    }

    public void SetAnimationFrames(int _start, int _end)
    {
        timeAcc = 0.0f;
        currentFrame = _start;
        startFrame = _start;
        endFrame = _end;

        // Calculate the time per frame based on the number of frames in the animation
        int framesInAnimation = endFrame - startFrame;
        if (framesInAnimation > 0)
        {
            CalculateTimePerFrame(framesInAnimation);
        }
    }

    private void CalculateTimePerFrame(int framesInAnimation)
    {
        if (framesInAnimation > 0)
        {
            timePerFrame = 1.0f / (float)framesInAnimation * fps;
            System.out.println("time per frame: " + timePerFrame);
        }
    }

    public int GetCurrentFrame()
    {
        return currentFrame;
    }

    public int GetEndFrame()
    {
        return endFrame;
    }

    public int GetHeight()
    {
        return height;
    }

    public int GetWidth()
    {
        return width;
    }
}