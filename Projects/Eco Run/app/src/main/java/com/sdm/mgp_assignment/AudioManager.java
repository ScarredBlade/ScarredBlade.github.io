package com.sdm.mgp_assignment;

import android.content.Context;
import android.media.MediaPlayer;
import android.view.SurfaceView;

import java.util.HashMap;

public class AudioManager
{
    // Singleton instance of the AudioManager
    public final static AudioManager Instance = new AudioManager();

    // SurfaceView used for creating MediaPlayer instances
    private SurfaceView view = null;

    // HashMap to store MediaPlayer instances for sound effects
    private HashMap<Integer, MediaPlayer> audioMap = new HashMap<Integer, MediaPlayer>();

    // MediaPlayer instances for background music and sound effects
    private MediaPlayer bgMusicPlayer, sfxAudioPlayer;

    // Bools to track the paused state of background music and sound effects
    private boolean isBgMusicPaused = false, isSfxMusicPaused = false;

    // Initial volume levels for background music and sound effects
    private int musicVolume = 50, sfxVolume = 50;

    /**
     * Initializes the AudioManager with the specified SurfaceView.
     *
     * @param _view The SurfaceView used for creating MediaPlayer instances.
     */
    public void Init(SurfaceView _view)
    {
        view = _view;
        Release(); // Clears the audioMap
    }

    /**
     * Plays background music with volume control.
     *
     * @param context The context of the calling activity or service.
     * @param _id     The resource ID of the background music.
     */
    public void PlayMusic(Context context, int _id)
    {
        if (bgMusicPlayer == null)
        {
            // Create and start a new MediaPlayer instance for background music
            bgMusicPlayer = MediaPlayer.create(context, _id);
            bgMusicPlayer.setLooping(true);
            bgMusicPlayer.setVolume(musicVolume / 100f, musicVolume / 100f);
            bgMusicPlayer.start();
        }
        else if (!bgMusicPlayer.isPlaying())
        {
            // Resume playback if the background music is paused
            bgMusicPlayer.setVolume(musicVolume / 100f, musicVolume / 100f);
            bgMusicPlayer.start();
            isBgMusicPaused = false;
        }
    }

    /**
     * Sets the volume level for background music.
     *
     * @param volume The desired volume level (0 to 100).
     */
    public void SetMusicVolume(int volume)
    {
        musicVolume = volume;
        if (bgMusicPlayer != null)
        {
            bgMusicPlayer.setVolume(musicVolume / 100f, musicVolume / 100f);
        }
    }

    /**
     * Retrieves the current volume level for background music.
     *
     * @return The current volume level.
     */
    public int GetMusicVolume()
    {
        return musicVolume;
    }

    /**
     * Pauses background music if it is currently playing.
     */
    public void PauseMusic()
    {
        if (bgMusicPlayer != null && bgMusicPlayer.isPlaying())
        {
            bgMusicPlayer.pause();
            isBgMusicPaused = true;
        }
    }

    /**
     * Resumes background music if it is paused.
     */
    public void ResumeMusic()
    {
        if (bgMusicPlayer != null && isBgMusicPaused)
        {
            bgMusicPlayer.start();
            isBgMusicPaused = false;
        }
    }

    /**
     * Plays a sound effect with volume control.
     *
     * @param _id The resource ID of the sound effect.
     */
    public void PlaySFX(int _id)
    {
        if (audioMap.containsKey(_id))
        {
            // Use the existing MediaPlayer instance for the sound effect
            sfxAudioPlayer = audioMap.get(_id);
            sfxAudioPlayer.seekTo(0);
            sfxAudioPlayer.setVolume(sfxVolume / 100f, sfxVolume / 100f);
            sfxAudioPlayer.start();
        }
        else
        {
            // Create and start a new MediaPlayer instance for the sound effect
            sfxAudioPlayer = MediaPlayer.create(view.getContext(), _id);
            sfxAudioPlayer.setVolume(sfxVolume / 100f, sfxVolume / 100f);
            sfxAudioPlayer.start();
        }
    }

    /**
     * Sets the volume level for sound effects.
     *
     * @param volume The desired volume level (0 to 100).
     */
    public void SetSFXVolume(int volume)
    {
        sfxVolume = volume;
        if (sfxAudioPlayer != null)
        {
            sfxAudioPlayer.setVolume(sfxVolume / 100f, sfxVolume / 100f);
        }
    }

    /**
     * Retrieves the current volume level for sound effects.
     *
     * @return The current volume level.
     */
    public int GetSFXVolume()
    {
        return sfxVolume;
    }

    /**
     * Pauses a sound effect if it is currently playing.
     */
    public void PauseSFX()
    {
        if (sfxAudioPlayer != null && sfxAudioPlayer.isPlaying())
        {
            sfxAudioPlayer.pause();
            isSfxMusicPaused = true;
        }
    }

    /**
     * Resumes a paused sound effect.
     */
    public void ResumeSFX()
    {
        if (sfxAudioPlayer != null && isSfxMusicPaused)
        {
            sfxAudioPlayer.start();
            isBgMusicPaused = false;
        }
    }

    /**
     * Stops a specific sound effect.
     *
     * @param _id The resource ID of the sound effect to be stopped.
     */
    public void StopSFX(int _id)
    {
        sfxAudioPlayer = audioMap.get(_id);
        sfxAudioPlayer.stop();
    }

    /**
     * Releases all resources, stopping and clearing all audio playback instances.
     */
    public void Release()
    {
        for (HashMap.Entry<Integer, MediaPlayer> entry : audioMap.entrySet())
        {
            entry.getValue().stop();
            entry.getValue().reset();
            entry.getValue().release();
        }

        audioMap.clear();
    }

    /**
     * Retrieves or creates a MediaPlayer instance for a specific sound effect.
     *
     * @param _id The resource ID of the sound effect.
     * @return The MediaPlayer instance associated with the specified sound effect.
     */
    private MediaPlayer GetAudio(int _id)
    {
        if (audioMap.containsKey(_id))
        {
            return audioMap.get(_id);
        }

        MediaPlayer result = MediaPlayer.create(view.getContext(), _id);
        audioMap.put(_id, result);
        return result;
    }
}