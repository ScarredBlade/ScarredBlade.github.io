package com.sdm.mgp_assignment;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.SurfaceView;

public class RenderTextEntity implements EntityBase
{
    private Paint paint = new Paint();
    private boolean isDone = false;
    private boolean isInit = false;
    public static int score = 0;
    protected Typeface myfont;

    public void IncreaseScore()
    {
        score++;
    }

    public static int GetScore()
    {
        return score;
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
        myfont = Typeface.createFromAsset(_view.getContext().getAssets(), "fonts/chewy.ttf");
        isInit = true;
    }

    @Override
    public void Update(float _dt)
    {
    }

    @Override
    public void Render(Canvas _canvas)
    {
        paint.setARGB(255, 255, 255, 255); // White text
        paint.setTypeface(myfont);
        paint.setTextSize(50);
        _canvas.drawText("Score: " + score, 30, 80, paint);
    }

    @Override
    public boolean IsInit()
    {
        return isInit;
    }

    @Override
    public int GetRenderLayer()
    {
        return LayerConstants.RENDERTEXT_LAYER;
    }

    @Override
    public void SetRenderLayer(int _newLayer)
    {
    }

    public static RenderTextEntity Create()
    {
        RenderTextEntity result = new RenderTextEntity();
        EntityManager.Instance.AddEntity(result, ENTITY_TYPE.ENT_TEXT);
        return result;
    }

    @Override
    public ENTITY_TYPE GetEntityType()
    {
        return ENTITY_TYPE.ENT_TEXT;
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