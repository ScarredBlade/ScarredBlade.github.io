package com.sdm.mgp_assignment;

import android.graphics.Canvas;
import android.view.SurfaceView;

public interface StateBase
{
    String GetName();

    void OnEnter(SurfaceView _view);
    void OnExit();
    void Render(Canvas _canvas);
    void Update(float _dt);
}