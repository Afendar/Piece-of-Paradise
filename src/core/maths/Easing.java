package core.maths;

public class Easing
{
    public static int easeOut(float t, float b, float c, float d)
    {
        if((t /= d) < (1 / 2.75f))
            return (int)((c * 7.5625f * t * t) + b);
        else if(t < (2 / 2.75f))
            return (int)(c * (7.5625f * (t -= (1.5f / 2.75f)) * t + .75f) + b);
        else if(t < (2.5 / 2.75))
            return (int)(c * (7.5625f * (t -= (2.25f / 2.75f)) * t + .9375f) + b);
        else
            return (int)(c * (7.5625f * (t -= (2.625f / 2.75f)) * t + .984375f) + b);
    }
    
    public static int cubicEaseIn (float t,float b , float c, float d)
    {
        return (int)(c * (t /= d) * t * t + b);
    }
    
    public static int circEaseOut(float t,float b , float c, float d)
    {
        return (int)(c * Math.sqrt(1 - ( t = t / d - 1) * t) + b);
    }
}
