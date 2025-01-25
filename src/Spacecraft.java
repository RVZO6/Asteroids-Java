import java.awt.*;
import java.util.*;

//Inheritance in java is with the "extends" keyword
public class Spacecraft extends VectorSprite
{
    //set a fireRate for a standard ship (in clock cycles)
    public int fireRate = 25;
    public int lives = 3;

    //Constructor
    public Spacecraft()
    {
        //building our spacecraft using the info from the super/parent class
        super();
        thrust = 0.33;
        rotation = 0.1;
        //colour for RGB ranging from 0-255, Color.Green as default
        colour = new Color(0, 255, 0);
        //initialize the polygon of the vectorsprite
        initShape(Game.defaultShipShape);

        xPosition = Game.WIDTH/2;
        yPosition = Game.HEIGHT/2;
    }

    //move player forward at an angle
    public void accelerate()
    {
        xSpeed += Math.cos(angle) * thrust;
        ySpeed += Math.sin(angle) * thrust;
    }

    //slow player's speed down
    public void decelerate()
    {
        //divide using / to slow the speed of the ship by a fraction per frame
        xSpeed /= 1.1;
        ySpeed /= 1.1;
    }

    //rotate the player clockwise (1) or counter-clockwise (-1)
    public void rotate(int direction)
    {
        angle += rotation * direction;
    }

    //fires a bullet from the polygon shape of the ship
    //we pass through the bulletList as a parameter so the bullet gets kept track of
    public void fireBullet(ArrayList<Bullet> bulletList)
    {
        //if the ship is active in the game AND the counter has reached the fireRate cycle
        if (active && counter >= fireRate)
        {
            counter = 0; //reset counter
            //fire a new bullet from the first vertex on thex polygon shape
            Bullet b = new Bullet(drawShape.xpoints[0],drawShape.ypoints[0], angle);
            //we add this new bullet to the bulletList
            bulletList.add(b);
        }
    }

    //for now, this doesn't do much more than set the spacecraft inactive
    public void hit()
    {
        active = false;
        counter = 0; //reset counter to time when to respawn
        lives--;
    }

    //this method will reset the spacecraft to a new x, y position
    //made this way to help customization in future
    public void reset(double x, double y)
    {
        xPosition = x;
        yPosition = y;
        xSpeed = 0;
        ySpeed = 0;
        angle = 0;
        active = true;
    }
}