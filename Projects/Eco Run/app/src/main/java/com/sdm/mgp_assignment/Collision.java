package com.sdm.mgp_assignment;

public class Collision
{
    public static boolean SphereToSphere(float x1, float y1, float radius1, float x2, float y2, float radius2)
    {
        float distanceSquared = (x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2);
        float radiusSumSquared = (radius1 + radius2) * (radius1 + radius2);

        return distanceSquared <= radiusSumSquared;
    }

    public static boolean CuboidToSphere(float cuboidX, float cuboidY, float cuboidWidth, float cuboidHeight, float sphereX, float sphereY, float sphereRadius)
    {
        // Find the center of the sphere
        float sphereCenterX = sphereX + sphereRadius;
        float sphereCenterY = sphereY + sphereRadius;

        // Find the closest point on the cuboid to the sphere
        float closestX = Math.max(cuboidX, Math.min(sphereCenterX, cuboidX + cuboidWidth));
        float closestY = Math.max(cuboidY, Math.min(sphereCenterY, cuboidY + cuboidHeight));

        // Calculate the distance between the closest point and the sphere center
        float distanceSquared = (closestX - sphereCenterX) * (closestX - sphereCenterX) + (closestY - sphereCenterY) * (closestY - sphereCenterY);

        // Check if the distance is less than the sphere's radius squared
        return distanceSquared <= (sphereRadius * sphereRadius);
    }
}