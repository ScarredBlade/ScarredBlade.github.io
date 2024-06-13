package com.sdm.mgp_assignment;

public interface Collidable
{
    String GetType();

    float GetPosX();
    float GetPosY();
    float GetRadius();
    float GetWidth();
    float GetHeight();

    void OnHit(Collidable _other);
}