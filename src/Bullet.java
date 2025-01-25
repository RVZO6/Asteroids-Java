public class Bullet extends VectorSprite
{
    //the square shape of a default bullet
    public static int[][] defaultBulletShape = new int[][]
            { {-1, 1}, {1, 1}, {1, -1}, {-1, -1} };

    public int duration = 90; //how long bullets last on screen

    //constructor
    public Bullet(double startX, double startY, double angle)
    {
        super();
        initShape(Bullet.defaultBulletShape);
        //the this keyword represents the current instance of bullet
        this.xPosition = startX;
        this.yPosition = startY;
        this.angle = angle;
        //provide some extra speed with the thrust variable
        thrust = 10;

        //calculate xSpeed & ySpeed using Trigonometry (sine and cosine)
        this.xSpeed = Math.cos(angle) * thrust;
        this.ySpeed = Math.sin(angle) * thrust;
    }

    public void update()
    {
        super.update();
        //check if the counter reaches the set duration we want bullets on screen for
        if (counter >= duration)
        {
            //set the bullet inactive
            active = false;
        }
    }
}
