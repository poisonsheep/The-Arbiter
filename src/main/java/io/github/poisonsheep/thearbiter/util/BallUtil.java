package io.github.poisonsheep.thearbiter.util;


public class BallUtil {
    private static double radius;
    private static int divide;
    public static int max;
    private static double divdAngle;
    public void update_radius(double pR, int pDivd) {
        radius = pR;
        if (pR < 0)
        {
            radius = 1;
        }
        if (pDivd < 4)
        {
            divide = 4;
        }
        else if (pDivd % 4 != 0)
        {
            divide = pDivd + 4 - pDivd % 4;
        }
        else
        {
            divide = pDivd;
        }
        max = divide * (divide - 3) + 2;
        divdAngle = Math.PI/(divide/2);
    }
    public static Position getPosition(int i){
        Position position = new Position();
        double tR,tL;
        int group,pos;
        if( i < 0 || i > max-1) {
            i = 0;
        }
        if(i == 0)  {   // 球顶
            position.x = 0;
            position.z = 0;
            position.y = radius;
        } else if (i == (max - 1)) { // 球底
            position.x = 0;
            position.z = 0;
            position.y = -radius;
        } else {    // 序列号除球顶和球底以外，每次切一个圆，分成divd份
            group = ((i-1) / divide) + 1;
            pos = i % divide;
            tR = radius * Math.cos(divdAngle * group);
            tL = radius * Math.sin(divdAngle * group);
            position.y = tR;
            position.x = tL * Math.cos(divdAngle * pos);
            position.z = tL * Math.sin(divdAngle * pos);
        }
        return position;
    }
    public static class Position{
        public double x;
        public double y;
        public double z;
    }
}

