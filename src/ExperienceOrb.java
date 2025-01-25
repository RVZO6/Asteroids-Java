import java.awt.*;
public class ExperienceOrb extends VectorSprite
{
    //the shape of the orb, modify if desired
    public static int [][] defaultOrbShape = new int[][]
            { {0, 5}, {3, 3}, {5, 0}, {3, -3}, {0, -5}, {-3, -3}, {-5, 0}, {-3, 3} };

    //the experience points value of this orb
    int expValue;

    //constructor
    public ExperienceOrb(double x, double y, double size)
    {
        super();
        this.size = size;
        xPosition = x;
        yPosition = y;
        initShape(ExperienceOrb.defaultOrbShape);
        //value of the orb is based on size of asteroid destroyed and random value
        expValue = (int) Math.round(size * 10f + (Math.random() * 10));
        colour = Color.WHITE;
        thrust = 5;
    }
}