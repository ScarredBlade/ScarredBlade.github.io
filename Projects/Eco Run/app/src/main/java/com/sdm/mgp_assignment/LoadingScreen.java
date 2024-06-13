package com.sdm.mgp_assignment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class LoadingScreen extends Activity
{
    private ProgressBar loadingBar;
    private TextView loadingPercentage;
    private int progressStatus = 0;
    private boolean _active = true;

    private long lastFrameTime = 0;

    // Array of recycling facts
    private String[] recyclingFacts = {
            "Recycling one ton of paper can save 17 trees!",
            "Plastic bottles can take up to 450 years to decompose.",
            "Glass is 100% recyclable and can be recycled endlessly without loss in quality.",
    };

    // Random object to select a random recycling fact
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);

        // Initialize ProgressBar and TextView
        loadingBar = findViewById(R.id.loadingBar);
        loadingPercentage = findViewById(R.id.loadingPercentage);

        // Find the TextView for recycling fact
        TextView recyclingFactText = findViewById(R.id.recyclingFactText);

        // Load custom font
        Typeface myfont = Typeface.createFromAsset(getAssets(), "fonts/chewy.ttf");

        // Set font for the TextView
        recyclingFactText.setTypeface(myfont);

        // Set a random recycling fact during the loading period
        final String randomFact = getRandomRecyclingFact();
        recyclingFactText.setText(randomFact);

        // Thread for displaying the Splash Screen with loading progress
        Thread splashThread = new Thread()
        {
            @Override
            public void run()
            {
                try
                {
                    while (_active && progressStatus < 100)
                    {
                        long currentTime = System.nanoTime();
                        long frameTime = currentTime - lastFrameTime;

                        // Calculate frames per second (fps)
                        double fps = 1e9 / frameTime;

                        // Calculate remaining progress needed to reach 100%
                        int remainingProgress = 100 - progressStatus;

                        // Adjust progress increment based on fps, considering remaining progress
                        int progressIncrement = Math.min((int) (2 * fps / 60), remainingProgress); // 60 frames per second

                        progressStatus += progressIncrement;

                        // Update the progress bar and percentage on the UI thread
                        handler.sendEmptyMessage(0);

                        // Sleep for approximately 60 fps
                        sleep(16);

                        lastFrameTime = currentTime;

                        if (!_active)
                        {
                            break;
                        }
                    }

                    // Finish the activity and start the new one
                    finish();
                    Intent intent = new Intent(LoadingScreen.this, MainMenu.class);
                    startActivity(intent);

                }
                catch (InterruptedException e)
                {
                }
            }
        };
        splashThread.start();
    }

    // Handler to update UI elements on the main thread
    Handler handler = new Handler(new Handler.Callback()
    {
        @Override
        public boolean handleMessage(Message msg)
        {
            loadingBar.setProgress(progressStatus);
            loadingPercentage.setText(progressStatus + "%");
            return true;
        }
    });

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == MotionEvent.ACTION_DOWN)
        {
            //_active = false;
        }
        return true;
    }

    // Method to get a random recycling fact from the array
    private String getRandomRecyclingFact()
    {
        int randomIndex = random.nextInt(recyclingFacts.length);
        return recyclingFacts[randomIndex];
    }
}