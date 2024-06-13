package com.sdm.mgp_assignment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;

public class SplashPage extends Activity
{
    private boolean _active = true;
    private int _splashTime = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_page);

        // Thread for displaying the Splash Screen
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try
                {
                    int waited = 0;
                    while (_active && (waited < _splashTime))
                    {
                        sleep(100);
                        if (_active)
                        {
                            waited += 100;
                        }
                    }
                }
                catch (InterruptedException e)
                {
                    // do nothing
                }
                finally
                {
                    // Create new activity based on an intent with CurrentActivity
                    Intent intent = new Intent(SplashPage.this, LoadingScreen.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        splashThread.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            //_active = false;
        }
        return true;
    }
}