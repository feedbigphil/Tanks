package Tanks;

import org.checkerframework.checker.units.qual.A;
import org.w3c.dom.css.RGBColor;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONArray;
import processing.data.JSONObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import processing.event.KeyEvent;
import processing.event.MouseEvent;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
/**
 * The App class represents the main application class for the Tanks game.
 * It manages the setup, drawing, and user input handling for the game.
 */
public class App extends PApplet {
    //Constants defining game parameters
    public static final int CELLSIZE = 32; 
    public static final int CELLHEIGHT = 32;
    public static final int CELLAVG = 32;
    public static final int TOPBAR = 0;
    public static int WIDTH = 864; 
    public static int HEIGHT = 640;
    public static final int BOARD_WIDTH = WIDTH/CELLSIZE;
    public static final int BOARD_HEIGHT = 20;
    public static final int INITIAL_PARACHUTES = 1;
    public static final int FPS = 100;

    private configReader configReader;
	//Images and visual elements
    private PImage Background; //backgorund, need to adapt this to the json file
    private PImage windRight;
    private PImage windLeft;
    private static int wind;
    private PImage windDirection;
    private PImage fuelImage;
    private PImage parachuteImage;

    //Games Objects
    private Terrain terrain;
    private Tree trees;
    private Tank tank; 
    private Projectile projectile;
    private ArrayList<Projectile> activeProjectiles = new ArrayList<>();
    private Collisions collision;

    //Player related variables
    private int currentPlayerIndex = 0;

    //Storing all the players scores 
    public static ArrayList<Integer> playerScores = new ArrayList<>();
    private ArrayList<String> abcd = new ArrayList<>(4);

    //Keyboard Input codes
    int rightArrow = 39;
    int leftArrow = 37;
    int upArrow = 38;
    int downArrow = 40;
    int spaceBar = 32;
    int wKey = 87;
    int sKey = 83;
    int rKey = 82;
    int eKey = 69;
    int fKey = 70;
    int pKey = 80;

    //Booleans to track states
    private static boolean rightMove = false;
    private static boolean leftMove = false;
    private static boolean upMove = false;
    private static boolean downMove = false;
    private static boolean spaceShoot = false;
    private static boolean increasePower = false;
    private static boolean decreasePower = false;
    private boolean gameIsRunning = true;
    private boolean LVLover = false;
    private boolean restart = false;
    private int LVLcount = 0;


    private Tank currTank;
    private int currRed;
    private int currBlue;
    private int currGreen;
    
    private Random random = new Random();
	
    /**
     * Constructs an instance of the App class.
     */
    public App() { 
        for (int i = 0; i < 4; i++) {
            playerScores.add(0);
        }
        abcd.add("A");
        abcd.add("B");
        abcd.add("C");
        abcd.add("D");
        
    }
    /**
     * Sets the size of the application window.
     */
	@Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    /**
     * Load all resources such as images. Initialise the elements such as the player and map elements.
     */
	@Override
    public void setup() {
        frameRate(FPS);
        this.configReader = new configReader(this, "/Users/philippefreisler/Documents/USYD/Computer Science/tanks_scaffold/config.json");

        this.Background = this.loadImage(configReader.getBackground(this.LVLcount));

        this.windLeft = this.loadImage("src/main/resources/Tanks/wind-1.png");
        this.windLeft.resize(45, 45);
        this.windRight = this.loadImage("src/main/resources/Tanks/wind.png");
        this.windRight.resize(45, 45);
        wind();
        this.fuelImage = this.loadImage("src/main/resources/Tanks/fuel.png");
        this.fuelImage.resize(40, 40);
        this.parachuteImage = this.loadImage("src/main/resources/Tanks/parachute.png");
        this.parachuteImage.resize(40, 40);

        this.terrain = new Terrain(this, configReader.getLayout(this.LVLcount)); 

        if (Terrain.treeList.size() > 1) {
            this.trees = new Tree(this, configReader.getTrees(this.LVLcount));
        }  
    }
    /**
     * Retrieves the boolean indicating whether the right arrow key is pressed.
     * @return true if the right arrow key is pressed, false otherwise.
     */
    public static boolean isRightMove() {
        return rightMove;
    }
    /**
     * Retrieves the boolean indicating whether the left arrow key is pressed.
     * @return true if the left arrow key is pressed, false otherwise.
     */
    public static boolean isLeftMove() {
        return leftMove;
    }
    /**
     * Retrieves the boolean indicating whether the up arrow key is pressed.
     * @return true if the up arrow key is pressed, false otherwise.
     */
    public static boolean isUpMove() {
        return upMove;
    }
    /**
     * Retrieves the boolean indicating whether the down arrow key is pressed.
     * @return true if the down arrow key is pressed, false otherwise.
     */

    public static boolean isDownMove() {
        return downMove;
    }
    /**
     * Retrieves the boolean indicating whether the spacebar key is pressed.
     * @return true if the spacebar key is pressed, false otherwise.
     */
    public static boolean isSpaceShoot() {
        return spaceShoot;
    }
    /**
     * Retrieves the boolean indicating whether the key for increasing power is pressed.
     * @return true if the key for increasing power is pressed, false otherwise.
     */
    public static boolean isIncreasePower() {
        return increasePower;
    }
    /**
     * Retrieves the boolean indicating whether the key for decreasing power is pressed.
     * @return true if the key for decreasing power is pressed, false otherwise.
     */
    public static boolean isDecreasePower() {
        return decreasePower;
    }
    /**
     * Retrieves the current wind velocity.
     * @return The wind velocity as an integer value.
     */
    public static int getWind() {
        return wind;
    }
    /**
     * Receive key pressed signal from the keyboard.
     */
	@Override
    public void keyPressed(KeyEvent event){
        if (event.getKeyCode() == rightArrow) { 
            rightMove = true; 
        }
        if (event.getKeyCode() == leftArrow) {
            leftMove = true;
        }
        if (event.getKeyCode() == upArrow) {
            upMove = true;
        }
        if (event.getKeyCode() == downArrow) {
            downMove = true;
        }
        if (event.getKeyCode() == spaceBar) {
            
        }
        if (event.getKeyCode() == wKey) {
            increasePower = true;
        }
        if (event.getKeyCode() == sKey) {
            decreasePower = true;
        }
        if (event.getKeyCode() == rKey) {
            restart = true;
        }
        //Repair Kit press e, cost 20
        if (event.getKeyCode() == eKey) {
            int i = findScoreIndex(currTank.getChar());
            int score = playerScores.get(i);
            playerScores.set(i, score - 20);
            int health = currTank.getHealth();
            currTank.setHealth(health + 20);
        }
        //Extra Fuel press f, cost 10
        if (event.getKeyCode() == fKey) {
            int i = findScoreIndex(currTank.getChar());
            int score = playerScores.get(i);
            playerScores.set(i, score - 10);
            currTank.addFuel(200);
        }
        //Add Parachute press p, cost 15 extension
        if (event.getKeyCode() == pKey) {
            int i = findScoreIndex(currTank.getChar());
            int score = playerScores.get(i);
            playerScores.set(i, score - 15);
            currTank.addParachute();
        }
    }
    /**
     * Receive key released signal from the keyboard.
     */
	@Override
    public void keyReleased(KeyEvent event){
        if (event.getKeyCode() == rightArrow) { 
            rightMove = false; 
        }
        if (event.getKeyCode() == leftArrow) {
            leftMove = false;
        }
        if (event.getKeyCode() == upArrow) {
            upMove = false;
        }
        if (event.getKeyCode() == downArrow) {
            downMove = false;
        }
        if (event.getKeyCode() == spaceBar) {
            spaceShoot = true;
            Projectile projectile = new Projectile(this, currTank.getX(), currTank.getY(), currTank.getProVel(), currTank.getVX(), currTank.getVY(), currTank.getChar());
            activeProjectiles.add(projectile);
        }
        if (event.getKeyCode() == wKey) {
            increasePower = false;
        }
        if (event.getKeyCode() == sKey) {
            decreasePower = false;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //TODO - powerups, like repair and extra fuel and teleport
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }
    /**
     * Generates a random wind velocity within the range [-35, 35] and updates the wind direction accordingly.
     */
    public void wind() {
        this.wind = random.nextInt(71) - 35;
        if (wind > 0) {
            this.windDirection = this.windRight;
        } else {
            this.windDirection = this.windLeft;
        }
    }
    public Integer findScoreIndex(String letter) {
        if (letter == "A") {
            return 0;
        }
        if (letter == "B") {
            return 1;
        }
        if (letter == "C") {
            return 2;
        } else {
            return 3;
        }
    }
    /**
     * Draw all elements in the game by current frame.
     */
	@Override
    public void draw() {
        if (gameIsRunning) { 
            this.background(this.Background); 
            this.terrain.draw(configReader.getForegroundColour(this.LVLcount)); 

            int yPositionScore = 105; 
            for(Tank player:Terrain.playerList) {
                int[] colours = configReader.getPlayerColour(player.getChar());
                int red = colours[0];
                int green = colours[1];
                int blue = colours[2];
                player.draw(colours);

                if (player == currTank) {
                    currRed = red;
                    currGreen = green;
                    currBlue = blue;
                }
                //constantly updating tank height to be on the ground & fall
                //Parachutes 
                int tankX = player.getX();
                int tankY = player.getY();
                int terrainY = Terrain.pixelHeights.get(tankX);

                if (tankY < terrainY) {
                    if (player.usingParachute() ) {
                        this.image(this.parachuteImage, tankX - 20, tankY - 40);
                        tankY++; //this makes it a slow fall
                        player.setY(tankY);
                        if (terrainY == tankY) {
                            player.usedParachute();
                        }
                    }
                    if(player.hasParachute()) { 
                        player.useParachute();
                        
                    } else {
                        player.setY(terrainY);
                    }
                }
            }
            //Trees
            if (Terrain.treeList.size() > 1) {
                this.trees.draw();
            }
            Iterator<Projectile> proIterator = activeProjectiles.iterator();
            while (proIterator.hasNext()) {
                Projectile projectile = proIterator.next();
                projectile.shoot();

                // Check for collision or out of bounds
                if (projectile.isCollided()) {
                    proIterator.remove();
                }
            }

            //To remove the dead tanks 
            if (Collisions.tanksTOremove != null ) {
                for(Tank tank: Collisions.tanksTOremove) {
                    Terrain.playerList.remove(tank);
                }
                Collisions.tanksTOremove.clear(); // Clear the list after removing tanks
            }
            
            //Change turns
            if (isSpaceShoot()) {
                currentPlayerIndex++;
                this.wind =  wind + (int)random(11) - 5;
                if (currentPlayerIndex >= Terrain.playerList.size()) {
                    currentPlayerIndex = 0;
                }
                spaceShoot = false;
            }
            currTank = Terrain.playerList.get(currentPlayerIndex);
            currTank.tick();

            //Level Restart
            if (Terrain.playerList.size() <= 1) {
                LVLover = true;
            } 

            //Parachute & Fuel counter
            this.image(this.windDirection, 730, 8);  
            this.image(this.fuelImage, 180, 10);
            this.text(currTank.getFuel(), 225, 40);
            this.image(this.parachuteImage, 270, 10);
            this.text(currTank.getParachute(), 310, 40);
            
            //Players turn display
            this.textSize(20);
            this.fill(0, 0, 0);
            this.text("Player " + currTank.getChar() + "'s Turn", 30, 40);
            //Base HUD, background 
            this.text("Health:", 350, 30);
            this.text("Power:", 350, 65);
            this.text(Math.abs(this.wind), 800, 35);
            this.text(currTank.getPower() , 420, 65);
            this.text(currTank.getHealth() , 640, 30);
            //White Base of HUD
            this.stroke(0, 0, 0);
            this.fill(255,255,255);
            this.strokeWeight(2);
            this.rect(425, 10, 200, 25);
            //Health HUD
            this.fill(currRed, currGreen, currBlue); 
            this.strokeWeight(2);
            this.rect(425, 10, 2*currTank.getHealth(), 25);
            //Power HUD
            this.stroke(GRAY);
            this.fill(currRed, currGreen, currBlue); 
            this.strokeWeight(6);
            this.rect(425, 10, 2*currTank.getPower(), 25);
            // Power line
            this.stroke(255, 0, 0);
            this.strokeWeight(2);
            this.line(425 + 2*currTank.getPower(), 2, 425 + 2*currTank.getPower(), 42);
            
            //Display scoreboard:
            this.stroke(0, 0, 0);
            this.noFill();
            this.strokeWeight(2);
            this.rect(700, 60, 150, 125);
            this.rect(700, 60, 150, 25);
            this.fill(0);
            this.text("Scores", 705, 80);
            //Player names:
            int Yscores = 105;
            for(String letter: abcd) {
                int[] colours = configReader.getPlayerColour(letter);
                int red = colours[0];
                int green = colours[1];
                int blue = colours[2];
                this.fill(red, green, blue);
                this.text("Player " + letter, 705, Yscores);
                Yscores += 25;
            }
            //Players scores   
            Yscores = 105;
            for (Integer score:playerScores) {
                this.fill(0);
                this.text(score, 800, Yscores);
                Yscores += 25;
            }
            //If the level is over the terrain needs to be setup again, with the new level
            if (LVLover) {
                LVLcount += 1;
                setup(); 
                LVLover = false;

            }
            if (restart) {
                LVLcount = 0;
                setup();
                for (Integer score: playerScores) {
                    score = 0;
                }
                restart = false;

            }
        }
    }

    public static void main(String[] args) {
        PApplet.main("Tanks.App");
        
    }   
}
