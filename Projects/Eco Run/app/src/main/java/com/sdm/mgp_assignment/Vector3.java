package com.sdm.mgp_assignment;

public class Vector3
{
    public float x,y,z;
    public static Vector3 Zero = new Vector3(0,0,0);

    public Vector3()
    {
    }

    public Vector3(float xValue, float yValue, float zValue)
    {
        set(xValue, yValue, zValue);
    }

    public Vector3(Vector3 other)
    {
        set(other);
    }

    public void add(Vector3 other)
    {
        x += other.x;
        y += other.y;
        z += other.z;
    }

    public void add(float otherX, float otherY, float otherZ)
    {
        x += otherX;
        y += otherY;
        z += otherZ;
    }

    public void subtract(Vector3 other)
    {
        x -= other.x;
        y -= other.y;
        z -= other.z;
    }

    public Vector3 subtractReturn(Vector3 other)
    {
        Vector3 temp = new Vector3();
        temp.set(x - other.x, y - other.y, z-other.z);
        return temp;
    }

    public void multiply(float magnitude)
    {
        x *= magnitude;
        y *= magnitude;
        z *= magnitude;
    }

    public void multiply(Vector3 other)
    {
        x *= other.x;
        y *= other.y;
        z *= other.z;
    }

    public Vector3 multiplyReturn(float magnitude)
    {
        Vector3 temp = new Vector3();
        temp.set(x*magnitude,y*magnitude,z*magnitude);
        return temp;
    }

    public Vector3 multiplyReturn(Vector3 other)
    {
        Vector3 temp = new Vector3();
        temp.set(x*other.x,y*other.y,z*other.z);
        return temp;
    }

    public Vector3 crossReturn(Vector3 other)
    {
        Vector3 temp = new Vector3();
        temp.x = (y * other.z) - (z * other.y);
        temp.y = (x * other.z) - (z * other.x);
        temp.z = (x * other.y) - (y * other.x);

        return temp;
    }

    public void divide(float magnitude)
    {
        if (magnitude != 0.0f)
        {
            x /= magnitude;
            y /= magnitude;
            z /= magnitude;
        }
    }

    public Vector3 divideReturn(float magnitude)
    {
        Vector3 temp = new Vector3();
        temp.set(x/magnitude,y/magnitude,z/magnitude);
        return temp;
    }

    public void set(Vector3 other)
    {
        x = other.x;
        y = other.y;
        z = other.z;
    }

    public void set(float xValue, float yValue, float zValue)
    {
        x = xValue;
        y = yValue;
        z = zValue;
    }

    public float dot(Vector3 other)
    {
        return (x * other.x) + (y * other.y) + (z * other.z);
    }

    public float length()
    {
        return (float) Math.sqrt(length2());
    }

    public float length2()
    {
        return (x * x) + (y * y) + (z * z);
    }

    public float distance2(Vector3 other)
    {
        float dx = x - other.x;
        float dy = y - other.y;
        float dz = z - other.z;
        return (dx * dx) + (dy * dy) + (dz * dz);
    }

    public float normalize()
    {
        float magnitude = length();
        if (magnitude != 0.0f)
        {
            x /= magnitude;
            y /= magnitude;
            z /= magnitude;
        }
        return magnitude;
    }

    public void setZero()
    {
        set(0.0f, 0.0f, 0.0f);
    }

    public boolean isZero()
    {
        if(x == 0.f && y == 0.f && z == 0.f)
        {
            return true;
        }
        return false;
    }
}