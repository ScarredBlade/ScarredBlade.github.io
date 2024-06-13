package com.sdm.mgp_assignment;

public class ScoreManager
{
    public static int score = 0;

    public static void setScore()
    {
        score = RenderTextEntity.GetScore();
    }

    public static int getScore()
    {
        return RenderTextEntity.GetScore();
    }
}