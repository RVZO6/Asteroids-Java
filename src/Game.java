//bring in or import the functionality of JFrame from a programming library
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.plaf.basic.*;


public class Game extends JFrame implements KeyListener, ActionListener
{
    //These variables will be universal (static) to all Game objects we make
    //they are also unchangeable (final) and therefore we capitalize the names
    public static final int WIDTH = 1080, HEIGHT = 720;
    //create the window that will be attached to the game
    public Window panel;
    //Create the Timer variable for the class that will tick every so often
    public javax.swing.Timer gameTimer;
    //useful to make it easier to modify starting number in future
    public int numberOfAsteroids = 6;
    public ArrayList<Asteroid> asteroidList;
    public ArrayList<Bullet> bulletList;
    public ArrayList<Debris> debrisList;
    public ArrayList<ExperienceOrb> expOrbList;
    public Spacecraft ship;
    public int shipLevel;
    public int shipRespawnTime = 50, respawnSafeRadius = 100;
    public int shipCollectionRadius = 100;
    public JProgressBar experienceBar;
    //determine number of levels and which one we're currently on
    public int currentLevel, maxLevels = 10;
    //a timer for how long before more asteroids spawn in and the duration to wait
    public int asteroidSpawnTimer, asteroidSpawnDuration = 500;
    //Sound Effects
    public Sound laserSfx, shipHitSfx, shipMoveSfx;
    public Sound asteroidHitSfx, collectSfx;
    ///

    //Game States
    public boolean gameStart, gameOver, gamePaused;

    //fix something known as key Locking, when only one key is recognized at a time
    public boolean leftKey, rightKey, forwardKey, backwardKey, shootKey;

    //the 3 points that make up our default ship, static as we can use for multiple ships in future
    //notice the use of double [] and the { } brackets indicating the arrays within the bigger array
    // { {x1, y1}, {x2, y2}, {x3, y3} }
    public static int[][] defaultShipShape = new int[][] { {15, 0}, {-10, 10}, {-10, -10} };

    //Constructor - builds the object from a recipe
    public Game()
    {
        //make it appear to the screen
        setVisible(true);
        //set the size using the variables made above
        setSize(new Dimension(WIDTH, HEIGHT));
        //this line is so we can use the normal close option provided by window
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //prevent resizing the window
        setResizable(false);
        setTitle("Asteroids - By RP4K");
        //create the window instance object for this game (this is the game!)
        panel = new Window(this);
        //add this window panel to game's Jframe (null is for window constraints)
        add(panel, null);
        //add the ability for our game window to listen in for key presses
        addKeyListener(this);
        //create the game timer that will cycle every 20 ms
        gameTimer = new javax.swing.Timer(20, this);
        asteroidList = new ArrayList<>();
        bulletList = new ArrayList<>();
        debrisList = new ArrayList<>();
        expOrbList = new ArrayList<>();
        //create exp bar with minimum and maximum values
        experienceBar = new JProgressBar(0, 1000);
        experienceBar.setVisible(false);

        //sounds
        laserSfx = new Sound("./src/Sounds/laser79.wav");
        shipHitSfx = new Sound("./src/Sounds/explode0.wav");
        shipMoveSfx = new Sound ("./src/Sounds/thruster.wav");
        asteroidHitSfx = new Sound("./src/Sounds/explode1.wav");
        collectSfx = new Sound("./src/Sounds/collect.wav");
        //fit every component we add into the game JFrame
        pack();
        gameTimer.start();
    }

    //initialize or start the game!
    public void init()
    {
        currentLevel = 0;
        asteroidSpawnTimer = 0;
        gamePaused = false;
        gameOver = false;
        ship = new Spacecraft();
        shipLevel = 0;
        //clear removes any leftover asteroids from a previous game
        asteroidList.clear();
        bulletList.clear();
        debrisList.clear();
        expOrbList.clear();

        initExperienceBar();
        gameTimer.restart();
    }

    public void spawnAsteroids()
    {
        asteroidSpawnTimer++;
        //if our asteroid spawn timer reaches the duration we set, spawn in asteroids
        if (asteroidSpawnTimer >= asteroidSpawnDuration)
        {
            for (int i = 0; i < numberOfAsteroids + currentLevel; i++)
            {
                asteroidList.add(new Asteroid());
            }
            asteroidSpawnTimer = 0;
            currentLevel++;
        }
    }

    //initialize experience bar for use in gameplay
    public void initExperienceBar()
    {
        experienceBar.setValue(0);
        //set the width and height dimensions of the exp bar
        experienceBar.setPreferredSize(new Dimension(1000, 20));
        experienceBar.setUI(new BasicProgressBarUI());
        //display the percentage and what colour the fill of the bar is
        experienceBar.setForeground(Color.green);
        experienceBar.setStringPainted(true);
        //add the experience bar to the window
        panel.add(experienceBar);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        controls();
        //if the game has started, the game is not over and not paused
        if (gameStart && !gameOver && !gamePaused) {
            respawnShip(); //we call this before ship updates in case the ship is not active
            ship.update();
            collectExpOrbs();
            for (int i = 0; i < asteroidList.size(); i++) {
                asteroidList.get(i).update();
            }
            for (int i = 0; i < bulletList.size(); i++) {
                bulletList.get(i).update();
            }
            for (int i = 0; i < debrisList.size(); i++) {
                debrisList.get(i).update();
            }
            for (int i = 0; i < expOrbList.size(); i++) {
                expOrbList.get(i).update();
            }

            checkCollisions();
            //call the removeObjects method we created below
            removeObjects();
            //temporary stop game if no asteroids are in the list
            if(asteroidList.isEmpty())
            {
                if(currentLevel < maxLevels)
                {
                    spawnAsteroids();
                }
                else
                {
                    gameOver = true;
                }

            }
            //level up and pause game if experience bar fills to maximum
            if (experienceBar.getValue() == experienceBar.getMaximum())
            {
                gamePaused = true;
            }
        }
    }

    public void collectExpOrbs() {
        //temporary variables to define x, y components
        //hypotenuse (distance between orb and ship) and angle
        double x, y, hypotenuse, angle;
        //new type of for loop: for each ExperienceOrb in expOrbList,
        //store one in a temporary variable called "orb"
        for (ExperienceOrb orb : expOrbList) {
            //x and y values based on position differences
            x = orb.xPosition - ship.xPosition;
            y = orb.yPosition - ship.yPosition;

            //using Pythagorean's Theorem c^2 = a^2 + b^2, calculate
            //the hypotenuse distance between orb and ship
            hypotenuse = (int) Math.round(Math.sqrt((x * x) + (y * y)));

            //using inverse of tan(angle) = opposite / adjacent to calculate angle
            //convert to degrees from radians
            angle = Math.atan2(y, x) * (2 * Math.PI);

            //if both ship and orb are active and the
            //distance between them is less than collection radius
            if (ship.active && hypotenuse < shipCollectionRadius) {
                //set x and y speeds according to angle
                orb.xSpeed = Math.cos(angle) * orb.thrust;
                orb.ySpeed = Math.sin(angle) * orb.thrust;
            } else {
                //otherwise if outside of collection radius, stop moving
                orb.xSpeed = 0;
                orb.ySpeed = 0;
            }
        }
    }


    public void checkCollisions()
    {
        //go through all asteroids
        for (int i = 0; i < asteroidList.size(); i++)
        {
            //get one asteroid at a time and temporarily call it "rock"
            Asteroid rock = asteroidList.get(i);
            // see if ship collides with any of them while both ship and "rock" are active
            if (ship.active && rock.active && ship.collidesWith(rock))
            {
                ship.hit();
                makeDebris(ship.xPosition, ship.yPosition);
                shipHitSfx.stop();
                shipHitSfx.play();

                if (ship.lives == 0)
                {
                    gameOver = true;
                }
            }
            //NOTE we use j instead of i as that letter is used in the above for loop
            //go through all bullets in bulletList
            for (int j = 0; j < bulletList.size(); j++)
            {
                //get one bullet from the list and temporarily call it "b"
                Bullet b = bulletList.get(j);
                if(b.active && rock.active && b.collidesWith(rock))
                {
                    b.active = false; //both bullet and rock are no longer active in game
                    rock.active = false;
                    //destroy rock into smaller asteroids, they get added to list
                    rock.destroy(asteroidList);
                    makeDebris(rock.xPosition, rock.yPosition);
                    asteroidHitSfx.stop();
                    asteroidHitSfx.play();
                    //spawn an experience orb from the asteroid to collect
                    expOrbList.add(new ExperienceOrb
                            (rock.xPosition, rock.yPosition, rock.size));

                }
            }
        }

        //go through all experience orbs and see if they collide with player ship
        for (int i = 0; i < expOrbList.size(); i++)
        {
            ExperienceOrb orb = expOrbList.get(i);
            if (ship.active && orb.active && orb.collidesWith(ship))
            {
                orb.active = false;
                //set value of exp bar to increase depending on orb value
                experienceBar.setValue(experienceBar.getValue() + orb.expValue);
                collectSfx.stop();
                collectSfx.play();
            }
        }
    }


    public void makeDebris(double x, double y)
    {
        //randomize the amount of debris made
        double randomNumber = Math.random() * 10 + 1;
        //add randomNumber amount of debris to the list at the x & y position above
        for (double i = 0; i < randomNumber; i++)
        {
            debrisList.add(new Debris(x, y));
        }
    }

    public void controls()
    {
        if(ship.active) {
            if (leftKey) {
                ship.rotate(-1); //CounterClockwise
            } else if (rightKey) {
                ship.rotate(1); //Clockwise
            }
            if (forwardKey) {
                ship.accelerate();
                shipMoveSfx.loop();
            }
            else {
                shipMoveSfx.stop();
            }
            if (backwardKey) {
                ship.decelerate();
            }
            if (shootKey) {
                laserSfx.loop();
                ship.fireBullet(bulletList);
            }
            else { //fixes a bug that happens when you speed right into an asteroid
                laserSfx.stop();
            }
        }
        else { //fixes a bug that happens when you speed right into an asteroid
            shipMoveSfx.stop();
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        //choose which key set off the event - using WASD keys and compare equal to ==
        if (e.getKeyCode() == KeyEvent.VK_A)
        {
            leftKey = true;
        }
        if (e.getKeyCode() == KeyEvent.VK_D)
        {
            rightKey = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_W)
        {
            forwardKey = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_S)
        {
            backwardKey = true;

        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            shootKey = true;
        }


        if(gamePaused)
        {
            if(e.getKeyCode() == KeyEvent.VK_1) //1 for extra lives
            {
                ship.lives += 3;
                gamePaused = false;
                experienceBar.setValue(0);
            }
            else if (e.getKeyCode() == KeyEvent.VK_2) //2 for increased fire rate
            {
                ship.fireRate -= 5;
                if (ship.fireRate <= 0) //negative fire rate makes no sense
                {
                    ship.fireRate = 0;
                }
                gamePaused = false;
                experienceBar.setValue(0);
            }
            else if (e.getKeyCode() == KeyEvent.VK_3) //3 to collect orbs easier
            {
                shipCollectionRadius += 100;
                gamePaused = false;
                experienceBar.setValue(0);
            }
        }



        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            //if the game has not started yet or we've lost, then have it start
            if(!gameStart || gameOver)
            {
                gameStart = true;
                init();
            }
        }
        //exit game upon hitting the escape key
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            System.exit(0);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //choose which key set off the event - using WASD keys and compare equal to ==
        if (e.getKeyCode() == KeyEvent.VK_A)
        {
            leftKey = false;
        }
        //CHALLENGE STUDENTS TO GET THE REST!
        if (e.getKeyCode() == KeyEvent.VK_D)
        {
            rightKey = false;

        }
        if (e.getKeyCode() == KeyEvent.VK_W)
        {
            forwardKey = false;

        }
        if (e.getKeyCode() == KeyEvent.VK_S)
        {
            backwardKey = false ;

        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE)
        {
            shootKey = false;
        }
    }

    public void respawnShip()
    {
        //if ship is not active in game, reset position, angle, speed and active
        if (ship.active == false && ship.counter > shipRespawnTime && isRespawnSafe())
        {
            ship.reset(Game.WIDTH/2, Game.HEIGHT/2);
        }
    }
    public boolean isRespawnSafe()
    {
        double xComponent, yComponent, hypotenuse;
        for(int i = 0; i < asteroidList.size(); i++)
        {
            //get x, y distances between respawn an asteroid
            xComponent = asteroidList.get(i).xPosition - Game.WIDTH/2;
            yComponent = asteroidList.get(i).yPosition - Game.HEIGHT/2;
            //calculate distance between asteroid and center of screen
            //hypotenuse/distance uses Pythagorean's Theorem (a^2 + b^2 = c^2)
            hypotenuse =  Math.sqrt( (xComponent * xComponent) +
                    (yComponent * yComponent));

            //if distance is less than our set spawn radius, respawning is not safe!
            if(hypotenuse < respawnSafeRadius) {
                return false;
            }
        }
        //after all asteroids have been checked, none are near the spawn!
        return true;
    }
    public void removeObjects()
    {
        //go through every asteroid
        for (int i = 0; i < asteroidList.size(); i++)
        {
            //check if asteroid is not active in game
            if (asteroidList.get(i).active == false)
            {
                //remove from list and therefore memory
                asteroidList.remove(i);
            }
        }

        //remove inactive bullets from list
        for (int i = 0; i < bulletList.size(); i++)
        {
            //check if bullet is not active in game
            if (bulletList.get(i).active == false)
            {
                //remove from list and therefore memory
                bulletList.remove(i);
            }
        }


        //remove inactive debris from list
        for (int i = 0; i < debrisList.size(); i++)
        {
            //check if debris is not active in game
            if (debrisList.get(i).active == false)
            {
                //remove from list and therefore memory
                debrisList.remove(i);
            }
        }


        //remove inactive expOrbs from list
        for (int i = 0; i < expOrbList.size(); i++)
        {
            //check if debris is not active in game
            if (expOrbList.get(i).active == false)
            {
                //remove from list and therefore memory
                expOrbList.remove(i);
            }
        }
    }

}
