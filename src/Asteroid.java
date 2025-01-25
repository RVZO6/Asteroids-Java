import java.awt.*;
import java.util.*;

public class Asteroid extends VectorSprite
{
    //create a 2D array to store all the x,y coordinates of the polygon vertices
    public static int[][] defaultAsteroidShape =
            { {15, 3}, {5, 17}, {-12, 5}, {-8, -8}, {10, -17} };
    //constructor
    public Asteroid()
    {
        //create asteroid using VectorSprite's properties
        super();
        //colour for RGB ranging from 0-255, feel free to change
        colour = new Color(0, 255, 0);
        //size must be set before the asteroid shape is initialized
        size = 3;
        //initialize the polygon of the asteroid with points above
        initShape(Asteroid.defaultAsteroidShape);
        randomizeAsteroid();

    }

    //second constructor - make asteroid at certain x & y location
    public Asteroid(double x, double y, double size)
    {
        super();
        colour = new Color(0, 255, 0);
        this.size = size;

        initShape(Asteroid.defaultAsteroidShape);
        randomizeAsteroid();
        xPosition = x;
        yPosition = y;
    }
    public void randomizeAsteroid()
    {

        //set the hypotenuse and theta angle for randomized speed and direction
        double hypotenuse, theta;
        //use this formula for better randomization: Math.random() * maxValue + minimumValue
        hypotenuse = Math.random() * 1 + 1;
        theta = Math.random()* (2 * Math.PI);
        xSpeed = Math.cos(theta) * hypotenuse;
        ySpeed = Math.sin(theta) * hypotenuse;

        rotation = Math.random() * 0.25 + 0;

        //set the position of asteroid to be far from player (min 100 pixels away)
        //set from the center of the screen using static WIDTH & HEIGHT from Game
        //math.random() * maxValue + minValue
        hypotenuse = Math.random() * Game.WIDTH/2 + 100;
        theta = Math.random() * (2 * Math.PI);
        xPosition = Math.cos(theta) * hypotenuse + Game.WIDTH/2;
        yPosition = Math.sin(theta) * hypotenuse + Game.HEIGHT/2;
    }

    public void update()
    {
        angle += rotation;
        //call the existing update method from VectorSprite (parent/super class)
        super.update();
    }

    //destroy asteroids into smaller pieces
    public void destroy(ArrayList<Asteroid> asteroidList)
    {
        //only split if our size is greater than 1
        if (size > 1)
        {
            //currently adds two new asteroids to the asteroid list
            //place new asteroids away from one another to simulate splitting apart & reduce size
            asteroidList.add(new Asteroid(xPosition + 10, yPosition, size - 1));
            asteroidList.add(new Asteroid(xPosition - 10, yPosition, size - 1));
        }
    }
}
