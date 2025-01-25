//import the following (likely added automatically with intelli sense)
import java.awt.*;

//Remember: a class defines what an object is, has and does
public class VectorSprite
{
    //Variables that belong to an instance of this class
    //Everything interactable in our game needs the following properties/attributes
    double xPosition, yPosition;
    double xSpeed, ySpeed, thrust;
    double angle, rotation, size;
    Polygon shape, drawShape;
    Color colour;
    boolean active;
    //twice the size of an integer is a "long" data type
    long counter;

    //Constructor - builds objects of this class type
    public VectorSprite()
    {
        //initialize the polygon shape variable in memory
        shape = new Polygon();
        drawShape = new Polygon();
        //colour is in RGB ranging from 0-255, Color.Green as default
        colour = new Color(0, 255, 0);
        active = true;
        size = 1;
    }

    public void initShape(int[][] points)
    {
        //add points to our ship from the 2d array parameter called points
        for (int i = 0; i < points.length; i++)
        {
            //store the resized x and y points in variables, cast them to int
            int x = (int)Math.round(points[i][0] * size);
            int y = (int)Math.round(points[i][1] * size);
            shape.addPoint(x,y);
            drawShape.addPoint(x,y);
        }
    }

    //Draws this VectorSprite to screen
    public void paint(Graphics g)
    {
        if(active) {
            //set the colour according to what we defined for the vectorsprite
            g.setColor(colour);
            //use the drawPolygon() method to draw the shape we defined in variable above
            g.drawPolygon(drawShape);
        }
    }

    //update position according to speed
    public void update()
    {
        counter++;
        if (active) {
            xPosition += xSpeed;
            yPosition += ySpeed;
            wrapAround();

            double x, y;

            //for loop needs an index, an end point and a step
            for (int index = 0; index < shape.npoints; index++) {
                //using the vector equations to handle rotation with sin and cos
                //x = x * cos(angle) - y * sin(angle)
                //y = x * sin(angle) + y * cos(angle)
                x = Math.round(shape.xpoints[index] * Math.cos(angle) -
                        shape.ypoints[index] * Math.sin(angle));
                y = Math.round(shape.xpoints[index] * Math.sin(angle) +
                        shape.ypoints[index] * Math.cos(angle));


                drawShape.xpoints[index] = (int) x;
                drawShape.ypoints[index] = (int) y;
            }
            drawShape.invalidate();
            //mathematical translate is to move the points of the polygon
            drawShape.translate((int) Math.round(xPosition), (int) Math.round(yPosition));
        }
    }
    public void wrapAround()
    {
        //if we've gone off screen left
        if (xPosition < 0)
        {
            //appear on right
            xPosition = Game.WIDTH;
        }
        //otherwise if we've gone off screen right
        else if (xPosition > Game.WIDTH)
        {
            //appear on left
            xPosition = 0;
        }

        //off screen top
        if (yPosition < 0)
        {
            yPosition = Game.HEIGHT;
        }
        //off screen bottom
        else if (yPosition > Game.HEIGHT)
        {
            yPosition = 0;
        }
    }

    //check does this VectorSprite collide with another VectorSprite?
    public boolean collidesWith(VectorSprite other)
    {
        //Scenario 1: other shape collides with this shape
        //go through each point that makes up the other vectorsprite's drawShape
        for (int i = 0; i < other.drawShape.npoints; i++) {
            //using the Polygon.contains(int x, int y) method, determine if one of the
            //points from other vectorSprite is within this vectorsprite's polygon bounds
            if (this.drawShape.contains(other.drawShape.xpoints[i],
                    other.drawShape.ypoints[i]))
            {
                return true; //return true if shapes overlap
            }
        }
        //Scenario 2: this shape collides with other shape
        for (int i = 0; i < this.drawShape.npoints; i++) {
            //similar to above, but we're checking the opposite case
            if(other.drawShape.contains(this.drawShape.xpoints[i],
                    this.drawShape.ypoints[i]))
            {
                return true;
            }
        }
        return false; //return false if shapes don't overlap
    }
}