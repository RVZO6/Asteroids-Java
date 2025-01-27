//bring in or import the functionality of JFrame from a programming library
import javax.swing.*;
import java.awt.*;

public class Window extends JPanel
{
    //the game which we want to draw the objects from
    private Game game;
    //Fonts
    private Font arial18, arial24;
    ///

    //pass the game we want to draw things from as a parameter to build window
    public Window(Game game)
    {
        //the this keyword refers to the variable belonging to the class
        this.game = game;
        //set the default color of this window to be black, and size to match game
        setBackground(Color.black);
        setPreferredSize(new Dimension(Game.WIDTH, Game.HEIGHT));
        //create some fonts of different sizes (hence the numbers in the name)
        arial18 = new Font("Arial", Font.PLAIN, 18);
        arial24 = new Font("Arial", Font.PLAIN, 24);
    }

    //Overriding existing methods allows you to change or add upon what they do
    @Override
    public void paintComponent(Graphics g)
    {
        //super allows us to call upon the existing functionality of paintComponent
        super.paintComponent(g);

        if(game.gameStart)
        {
            drawGame(g);
        }
        drawUI(g);

        //allows changes to be refreshed on the screen
        repaint();
    }

    //Organizing all drawn game elements in one method for future
    private void drawGame(Graphics g)
    {
        //changes the color of the object we’re about to draw
        g.setColor(Color.green);
        //use the drawPolygon() method, an array size is found by array.length

        game.ship.paint(g);
        for (int i = 0; i < game.asteroidList.size(); i++)
        {
            game.asteroidList.get(i).paint(g);
        }
        for (int i = 0; i < game.bulletList.size(); i++)
        {
            game.bulletList.get(i).paint(g);
        }
        for (int i = 0; i < game.debrisList.size(); i++)
        {
            game.debrisList.get(i).paint(g);
        }
        for (int i = 0; i < game.expOrbList.size(); i++)
        {
            game.expOrbList.get(i).paint(g);
        }
    }

    private void drawUI(Graphics g)
    {
        game.experienceBar.setVisible(false);
        //if game has started and not game over, draw gameplay ui
        if(game.gameStart && !game.gameOver && !game.gamePaused)
        {
            //draw gameplay ui
            game.experienceBar.setVisible(true);
            g.setColor(Color.green);
            g.setFont(arial18);
            g.drawString("Lives: " + game.ship.lives, 20, 50);
            g.drawString("Asteroids Left: " + game.asteroidList.size(),
                    Game.WIDTH-150, 50);
            g.drawString("Level: " + game.currentLevel, Game.WIDTH/2, 50);
            g.drawString("Press 6 to Toggle Mouse Aiming", 25, Game.HEIGHT - 25);
            //while asteroids are spawning, let the player know to prepare
            if (game.asteroidList.isEmpty())
            {
                //show how long until asteroids spawn into game = duration - timer value
                g.drawString("Get Ready... " +
                                (game.asteroidSpawnDuration - game.asteroidSpawnTimer),
                        Game.WIDTH/2 - 75, Game.HEIGHT/2 - 150);
                //highlight the spawn area as this is a "safe" area to wait in
                g.drawRoundRect(Game.WIDTH/2 - 75, Game.HEIGHT/2 - 75, 150, 150, 150, 150);
            }
            //if the ship got hit during the game, let player know they will respawn
            else if (!game.ship.active)
            {
                g.drawString("Respawning... ", Game.WIDTH/2-75, Game.HEIGHT/2-150);
                g.drawRoundRect(Game.WIDTH/2 - 75, Game.HEIGHT/2 - 75, 150, 150, 150, 150);
            }
        }
        //if game has not started and not game over, draw title screen
        else if (!game.gameStart && !game.gameOver && !game.gamePaused)
        {
            //draw title screen
            g.setColor(Color.GREEN);
            g.setFont(arial24);
            g.drawString("Asteroids", Game.WIDTH/2-100, Game.HEIGHT/2-100);
            g.setFont(arial18);
            g.drawString("Press Enter to Start", Game.WIDTH/2-100, Game.HEIGHT/2+100);
        }
        //if game is paused but not game over, draw level up menu
        else if (game.gameStart && !game.gameOver && game.gamePaused)
        {
            //draw level up menu
            g.setColor(new Color(0, 255, 0));
            g.setFont(arial24);
            g.drawString("~Game Paused~", Game.WIDTH/2-100, 100);
            g.drawString("Level Up!", Game.WIDTH/2-100, Game.HEIGHT/2-100);
            g.setFont(arial18);
            g.drawString("Choose Ship Upgrade", Game.WIDTH/2-100, Game.HEIGHT/2);
            g.drawString("1: Extra Lives", 50, Game.HEIGHT/2+120);
            g.drawString("2: Fire Rate", 300, Game.HEIGHT/2+120);
            g.drawString("3: Collection Radius", 550, Game.HEIGHT/2+120);
            g.drawString("4: Multishot", 800, Game.HEIGHT/2+120);

        }
        //if game is over, determine if won or lost
        else if (game.gameStart && game.gameOver && !game.gamePaused)
        {
            //if lives are zero on the ship
            if (game.ship.lives <= 0)
            {
                g.setColor(Color.RED);
                g.setFont(arial24);
                g.drawString("GAME OVER", Game.WIDTH/2-100, Game.HEIGHT/2-100);
            }
            //you win if asteroids are no longer on screen / in list, for now
            else if (game.asteroidList.isEmpty() && game.currentLevel == game.maxLevels)
            {
                g.setColor(Color.YELLOW);
                g.setFont(arial24);
                g.drawString("You Win!", Game.WIDTH/2-100, Game.HEIGHT/2-100);
            }
            g.setFont(arial18);
            g.drawString("Press Enter to Restart",
                    Game.WIDTH/2-100, Game.HEIGHT/2+100);
        }
    }

}
