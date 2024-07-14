package Tanks;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;

import org.w3c.dom.css.RGBColor;
/**
 * Represents a tank in the game.
 */
public class Tank {
    private int x;
    private int y;
    private PApplet app;
    private Cell currPlayerCell; 
    private int fuel;
    private int parachutes;
    private int health;
    private String playerChar;
    private int[] colour = new int[3];
    private boolean ParachuteInUse = false;

    private float dx2;
    private float dy2;

    private int xVel;
    private float radiansVel;
    private float rads;

    //projectile values
    private int ProjectX;
    private int ProjectY;
    private int power;

    int counter = 2;
    int current_counter = 0;
    private float proVel = 30;
    float ay = -3.6f;

    private float vx;
    private float vy;

     /**
     * Constructs a Tank object with the specified parameters.
     * @param app The PApplet instance.
     * @param playerChar The character representing the player.
     * @param x The x-coordinate of the tank.
     * @param y The y-coordinate of the tank.
     * @param health The health points of the tank.
     * @param parachutes The number of parachutes the tank has.
     * @param fuel The fuel level of the tank.
     */
    public Tank(PApplet app, String playerChar, int x,  int y, int health, int parachutes, int fuel) {
        
        this.app = app;
        this.xVel = 0;
        
        this.dx2 = 15 * app.sin(0);
        this.dy2 = 15 * app.cos(0);
        this.rads = 0;

        this.fuel = fuel;
        this.health = health;
        this.parachutes = parachutes;
        this.playerChar = playerChar;
        this.x = x;
        this.y = y;

        //Initial velocity calculations
        this.vx = proVel * app.sin(rads);
        this.vy = -proVel * app.cos(rads); 
        
        this.power = 50;
        
    }
    /**
     * Gets the character representing the player.
     * @return The character representing the player.
     */
    public String getChar() {
        return playerChar;
    }
    /**
     * Gets the x-coordinate of the tank.
     * @return The x-coordinate of the tank.
     */
    public int getX() {
        return this.x;
    }
    /**
     * Gets the y-coordinate of the tank.
     * @return The y-coordinate of the tank.
     */
    public int getY() {
        return this.y;
    } 
    /**
     * Sets the x-coordinate of the tank.
     * @param newX The new x-coordinate of the tank.
     */
    public void setX(int newX) {
        this.x = newX;
        this.ProjectX = x;
    }
    /**
     * Sets the y-coordinate of the tank.
     * @param newY The new y-coordinate of the tank.
     */
    public void setY(int newY) {
        this.y = newY;
        this.ProjectY = y;
    }

    /**
     * Gets the health points of the tank.
     * @return The health points of the tank.
     */
    public int getHealth() {
        return this.health;
    }
    /**
     * Sets the health points of the tank.
     * @param newHealth The new health points of the tank, if newHealth is greater than 100, it sets health to 100.
     */
    public void setHealth(int newHealth) {
        if (newHealth > 100) {
            this.health = 100;
        } else {
          this.health = newHealth;  
        }
    }
    /**
     * Gets the horizontal velocity of the tank.
     * @return The horizontal velocity of the tank.
     */
    public float getVX() {
        return this.vx;
    }
    /**
     * Gets the vertical velocity of the tank.
     * @return The vertical velocity of the tank.
     */
    public float getVY() {
        return this.vy;
    }
    /**
     * Gets the projectile velocity of the tank.
     * @return The projectile velocity of the tank.
     */
    public float getProVel() {
        return this.proVel;
    }
    /**
     * Gets the power of the tank's turret.
     * @return The power of the tank's turret.
     */
    public int getPower() {
        return this.power;
    }
    /**
     * Gets the fuel level of the tank.
     * @return The fuel level of the tank.
     */
    public int getFuel() {
        return this.fuel;
    }
    /**
     * Adds extra fuel to the tank.
     * @param extraFuel The amount of extra fuel to add.
     */
    public void addFuel(int extraFuel) {
        this.fuel = fuel + extraFuel;
    }
    /**
     * Gets the number of parachutes the tank has.
     * @return The number of parachutes the tank has.
     */
    public int getParachute() {
        return this.parachutes;
    }
    /**
     * Adds a parachute to the tank's inventory.
     */
    public void addParachute() {
        this.parachutes++;
    }
    /**
     * Checks if the tank has any parachutes.
     * @return True if the tank has parachutes, otherwise False.
     */
    public boolean hasParachute() {
        if (parachutes >= 1) {
            return true;
        } else {
            return false;
        }
    } 
    /**
     * Checks if the tank is currently using a parachute.
     * @return True if the tank is using a parachute, otherwise False.
     */
    public boolean usingParachute() {  
        return ParachuteInUse;
    }
    /**
     * Sets the tank to use a parachute.
     */
    public void useParachute() {
        ParachuteInUse = true;
    }
    /**
     * Marks the tank as having used a parachute and decrements parachute by 1.
     */
    public void usedParachute() {
        this.parachutes--;
        ParachuteInUse = false;
    }
    
    /**
     * Updates the state of the tank, what actions it takes for what keys are being pressed.
     */
    public void tick() {
        
        if (App.isRightMove()) {
            drive("right");
        }
        if (App.isLeftMove()) {
            drive("left");
        }
        if (App.isUpMove()) {
            turretMove("up");
        }
        if (App.isDownMove()) {
            turretMove("down");
        }
        if (App.isIncreasePower()) {
            turretPower("increase");
        }
        if (App.isDecreasePower()) {
            turretPower("decrease");
        }
        
    }
    
    /**
     * Draws the tank on the screen with the specified color.
     * @param colour An array containing the RGB color values.
     */
    public void draw(int[] colour) {
        int red = colour[0];
        int green = colour[1];
        int blue = colour[2];

        app.fill(red, green, blue);
        app.stroke(red, green, blue);
        app.rect((float) (x - 8.5), y - 6, 17, 6);
        app.fill(0, 0, 0);
        app.stroke(0, 0, 0);
        app.arc((float)x, (float)y- 6.0f, 10.0f, 10.0f, (float)Math.PI, (float)Math.PI * 2);
        app.strokeWeight(3);
        app.line(x, y - 6, x + dx2 , y - 6 - dy2);
    }  
    /**
     * Moves the tank according to the specified direction.
     * @param direction The direction in which to move the tank ("left" or "right").
     */
    public void drive(String direction) {
        if (fuel > 0) {
            this.xVel = 1;
            fuel -= 1;
            if (direction.equals("right")) {
                this.x += this.xVel; 
                setX(this.x);
                setY(Terrain.pixelHeights.get(this.x)); //constantly adjust the Y height so it is that of terrian
            }
            if (direction.equals("left")) {
                this.x -= this.xVel; 
                setX(this.x);
                setY(Terrain.pixelHeights.get(this.x)); //constantly adjust the Y height so it is that of terrian
            }
        }
        
    }
    /**
     * Moves the tank's turret up or down.
     * @param direction The direction in which to move the turret ("up" or "down").
     */
    public void turretMove(String direction) {
        
        this.radiansVel = 0.05f;

        if (direction.equals("up")) {       
            rads += radiansVel;

            this.dx2 = 15 * app.sin(rads);
            this.dy2 = 15 * app.cos(rads);
        }
        if (direction.equals("down")) {       
            rads -= radiansVel;

            this.dx2 = 15 * app.sin(rads);
            this.dy2 = 15 * app.cos(rads);

        }
        vx = proVel * app.sin(rads);
        vy = -proVel * app.cos(rads); 
        
    }
    
    /**
     * Adjusts the power of the turret.
     * @param Pdirection The direction in which to adjust the power ("increase" or "decrease").
     */
    public void turretPower(String Pdirection) {

        if ((Pdirection.equals("increase")) && (power < health)) { // change the 100 to currPlayerCell.health
            power += 1;
        }
        if (Pdirection.equals("decrease") && (power > 0)) {
            power -= 1;
        }

        proVel = Math.round(1 + 0.5 * power);

        vx = proVel * app.sin(rads);
        vy = -proVel * app.cos(rads); 
    }

}
