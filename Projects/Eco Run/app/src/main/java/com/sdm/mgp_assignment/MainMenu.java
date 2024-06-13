package com.sdm.mgp_assignment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.SurfaceView;
import android.view.View.OnClickListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.SplashScreen;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.SeekBar;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import java.util.Arrays;

public class MainMenu extends Activity implements View.OnClickListener, StateBase
{
//    // CallbackManager and ShareDialog for Facebook
//    private CallbackManager callbackManager;
//    private ShareDialog shareDialog;

    // Buttons in the main menu
    private Button btn_start;
    private Button btn_settings;
    private Button btn_leaderboard;
    private Button btn_share;

    // Variables to track SeekBar interactions
    private boolean isSeekBarMusicTouched = false, isSeekBarSFXTouched = false;
    private final Handler handlerMusic = new Handler(), handlerSFX = new Handler();

    /**
     * Called when the activity is first created.
     * Initializes the main menu layout, buttons, and other elements.
     * Loads saved audio volume preferences and starts playing background music.
     *
     * @param savedInstanceState The saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_menu);

//        // Initialize Facebook SDK
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        callbackManager = CallbackManager.Factory.create();
//        shareDialog = new ShareDialog(this);

        // Initialize buttons and set click listeners
        btn_start = findViewById(R.id.btn_start);
        btn_settings = findViewById(R.id.btn_settings);
        btn_leaderboard = findViewById(R.id.btn_leaderboard);
        btn_share = findViewById(R.id.btn_share);

        btn_start.setOnClickListener(this);
        btn_settings.setOnClickListener(this);
        btn_leaderboard.setOnClickListener(this);
        btn_share.setOnClickListener(this);

        // Customize the font for the title
        TextView titleTextView = findViewById(R.id.title);
        Typeface myfont = Typeface.createFromAsset(getAssets(), "fonts/chewy.ttf");
        titleTextView.setTypeface(myfont);

        // Load saved audio volume preferences
        SharedPreferences prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
        int savedMusicVol = prefs.getInt("music_volume", 50);
        int savedSFXVol = prefs.getInt("sfx_volume", 50);
        AudioManager.Instance.SetMusicVolume(savedMusicVol);
        AudioManager.Instance.SetSFXVolume(savedSFXVol);

        // Start playing background music
        AudioManager.Instance.PlayMusic(this, R.raw.bg_music);
    }

    /**
     * Handles button clicks in the main menu.
     * Performs actions based on the clicked button.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent();
        if (v == btn_start)
        {
            // Start the game when the "Start" button is clicked
            intent.setClass(this, GamePage.class);
            StateManager.Instance.ChangeState("MainGameState");
        }
        else if (v == btn_settings)
        {
            // Show audio settings dialog when the "Settings" button is clicked
            showSettings();
            return;
        }
        else if (v == btn_leaderboard)
        {
            // Navigate to the leaderboard when the "Leaderboard" button is clicked
            intent.setClass(this, LeaderboardActivity.class);
            StateManager.Instance.ChangeState("LeaderboardState");
        }
        else if (v == btn_share)
        {
            // Share the provided link when the "Share" button is clicked
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, "https://about.google/products/?tab=rh");
            intent.setType("text/plain");

            if (intent.resolveActivity(getPackageManager()) != null)
            {
                startActivity(intent);
            }
        }
        startActivity(intent);
    }

    /**
     * Displays the audio settings dialog.
     * Allows the user to adjust music and SFX volumes using SeekBars.
     * Updates volume preferences and UI elements accordingly.
     */
    private void showSettings()
    {
        // Create a dialog for audio settings
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.audio_settings);

        // Find SeekBars and the close button in the audio settings dialog
        SeekBar seekBarMusic = dialog.findViewById(R.id.seekBarMusic);
        SeekBar seekBarSFX = dialog.findViewById(R.id.seekBarSFX);
        Button btnClose = dialog.findViewById(R.id.btnClose);

        // Create TextViews to display volume values above the SeekBars
        final TextView tvMusicValue = new TextView(this);
        tvMusicValue.setTextColor(getResources().getColor(android.R.color.white));
        tvMusicValue.setTextSize(16);
        tvMusicValue.setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.addContentView(tvMusicValue, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        final TextView tvSFXValue = new TextView(this);
        tvSFXValue.setTextColor(getResources().getColor(android.R.color.white));
        tvSFXValue.setTextSize(16);
        tvSFXValue.setGravity(Gravity.CENTER_HORIZONTAL);
        dialog.addContentView(tvSFXValue, new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        // Set initial SeekBar positions based on saved volume preferences
        seekBarMusic.setProgress(AudioManager.Instance.GetMusicVolume());
        seekBarSFX.setProgress(AudioManager.Instance.GetSFXVolume());

        // Set listeners for SeekBars to update volumes and UI elements
        seekBarMusic.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                AudioManager.Instance.SetMusicVolume(progress);

                // Calculate position of the volume TextView above the SeekBar thumb
                int thumbX = (int) ((float) seekBar.getWidth() * progress / seekBar.getMax());
                int thumbAdjustment = (seekBar.getThumb().getIntrinsicWidth() * progress) / seekBar.getMax() / 2;

                // Update the position of the volume TextView
                tvMusicValue.setText(String.valueOf(progress));
                tvMusicValue.setX(seekBar.getX() + thumbX - tvMusicValue.getWidth() / 2 - thumbAdjustment);
                tvMusicValue.setY(seekBar.getY() - tvMusicValue.getHeight());

                if (fromUser)
                {
                    // If SeekBar is touched by the user, show the TextView and schedule it to fade away
                    isSeekBarMusicTouched = true;
                    tvMusicValue.setVisibility(View.VISIBLE);

                    handlerMusic.removeCallbacksAndMessages(null);
                    handlerMusic.postDelayed(() ->
                    {
                        if (isSeekBarMusicTouched)
                        {
                            tvMusicValue.setVisibility(View.INVISIBLE);
                            isSeekBarMusicTouched = false;
                        }
                    }, 1000);
                }

                // Save the music volume to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("music_volume", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        seekBarSFX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                AudioManager.Instance.SetSFXVolume(progress);

                // Calculate position of the volume TextView above the SeekBar thumb
                int thumbX = (int) ((float) seekBar.getWidth() * progress / seekBar.getMax());
                int thumbAdjustment = (seekBar.getThumb().getIntrinsicWidth() * progress) / seekBar.getMax() / 2;

                // Update the position of the volume TextView
                tvSFXValue.setText(String.valueOf(progress));
                tvSFXValue.setX(seekBar.getX() + thumbX - tvSFXValue.getWidth() / 2 - thumbAdjustment);
                tvSFXValue.setY(seekBar.getY() - tvSFXValue.getHeight());

                if (fromUser)
                {
                    // If SeekBar is touched by the user, show the TextView and schedule it to fade away
                    isSeekBarSFXTouched = true;
                    tvSFXValue.setVisibility(View.VISIBLE);

                    handlerSFX.removeCallbacksAndMessages(null);
                    handlerSFX.postDelayed(() ->
                    {
                        if (isSeekBarSFXTouched)
                        {
                            tvSFXValue.setVisibility(View.INVISIBLE);
                            isSeekBarSFXTouched = false;
                        }
                    }, 1000);
                }

                // Save the SFX volume to SharedPreferences
                SharedPreferences prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("sfx_volume", progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
            }
        });

        // Set listener for the close button to dismiss the dialog
        btnClose.setOnClickListener(v -> dialog.dismiss());

        // Display the audio settings dialog
        dialog.show();
    }

    /**
     * Called when the activity is paused.
     * Pauses the background music.
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        AudioManager.Instance.PauseMusic();
    }

    /**
     * Called when the activity is resumed.
     * Resumes the background music.
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        AudioManager.Instance.ResumeMusic();
    }

    @Override
    protected void onStop()
    {
        super.onStop();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public String GetName()
    {
        return "MainMenuState";
    }

    @Override
    public void OnEnter(SurfaceView _view)
    {
    }

    @Override
    public void OnExit()
    {
    }

    @Override
    public void Render(Canvas _canvas)
    {
    }

    @Override
    public void Update(float _dt)
    {
    }

//    private void shareOnFacebook(String message)
//    {
//        // Check if the ShareDialog can show a ShareLinkContent
//        if (ShareDialog.canShow(ShareLinkContent.class))
//        {
//            ShareLinkContent linkContent = new ShareLinkContent.Builder()
//                    .setContentUrl(Uri.parse("https://www.youtube.com/watch?v=I-9jEnBOD7w&ab_channel=TryHardNinja"))  // Replace with your content URL
//                    .setQuote(message)  // Add the message here
//                    .build();
//
//            shareDialog.show(linkContent);
//        }
//        else
//        {
//            Toast.makeText(this, "Unable to share on Facebook", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void showShareDialog()
//    {
//        final Dialog shareDialog = new Dialog(this);
//        shareDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        shareDialog.setContentView(R.layout.share_settings);
//
//        Button btnShareOnFacebook = shareDialog.findViewById(R.id.btnShareOnFacebook);
//
//        // Add listener to the "Share on Facebook" button
//        Button ShareOnFacebook = shareDialog.findViewById(R.id.btnShareOnFacebook);
//        ShareOnFacebook.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v)
//            {
//                // Check if the user is logged in to Facebook
//                if (AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken().isExpired())
//                {
//                    // User is logged in, proceed with sharing
//                    // Provide a method to get the user's input message, or use a default message
//                    String userMessage = "Check out this link!";
//                    shareOnFacebook(userMessage);
//                }
//                else
//                {
//                    // User is not logged in, show Facebook login button
//                    LoginManager.getInstance().logInWithReadPermissions(MainMenu.this, Arrays.asList("public_profile", "email"));
//                }
//            }
//        });
//        shareDialog.show();
//    }
}