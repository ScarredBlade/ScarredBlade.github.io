package com.sdm.mgp_assignment;

import android.graphics.Canvas;
import android.view.SurfaceView;

public interface EntityBase
{
    enum ENTITY_TYPE
    {
        ENT_PLAYER,
        ENT_OBSTACLE,
        ENT_PAUSE,
        ENT_TEXT,
        //ENT_NEXT,
        ENT_DEFAULT, // Background entity
    }

    boolean IsDone();
    void SetIsDone(boolean _isDone);
    void Init(SurfaceView _view);
    void Update(float _dt);
    void Render(Canvas _canvas);
    boolean IsInit();
    int GetRenderLayer();
    void SetRenderLayer(int _newLayer);
	ENTITY_TYPE GetEntityType();
    void RenderRadius(Canvas _canvas);
    void RenderCuboid(Canvas _canvas);
}