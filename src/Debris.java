public class Debris extends VectorSprite
{
    public static int[][] defaultDebrisShape = new int[][]
            { {0, 2}, {2, 0}, {0, -2}, {-2, 0} };
    int debrisLifetime = 25;
    //Constructor
    public Debris(double x, double y)
    {
        //Remember: call the super class to have VectorSprite do a lot of the work
        super();
        initShape(Debris.defaultDebrisShape);
        xPosition = x;
        yPosition = y;
        thrust = 7;
        //using random and trigonometry to determine speed, angle and rotation
        angle = Math.random() * (2 * Math.PI);
        rotation = Math.random() / 4;
        xSpeed = Math.cos(angle) * thrust;
        ySpeed = Math.sin(angle) * thrust;
    }

    public void update()
    {
        super.update();
        if (counter > debrisLifetime)
        {
            active = false;
        }
    }
}