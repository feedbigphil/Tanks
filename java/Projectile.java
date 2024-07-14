package Tanks;

import processing.core.PApplet;
import processing.core.PImage;
/**
 * The Projectile class represents a projectile fired by a tank.
 * It calculates the trajectory of the projectile based on initial velocity, wind speed, and gravity.
 */
public class Projectile {
    private PApplet app;
    private int x;
    private int y;
    private float initialVelocity;
    private float vy;
    private float vx;
    private boolean isProjectileInAir = true;
    private String tankSig;

    int counter = 2;
    int current_counter = 0;
    float ay = -3.6f;
    private int w = App.getWind(); 
     /**
     * Constructs a new Projectile object.
     * @param app The PApplet object for drawing.
     * @param x The initial x-coordinate of the projectile.
     * @param y The initial y-coordinate of the projectile.
     * @param initialVelocity The initial velocity of the projectile.
     * @param vx The horizontal velocity of the projectile.
     * @param vy The vertical velocity of the projectile.
     * @param tankSig The signature of the tank firing the projectile, tanks character.
     */
    public Projectile(PApplet app, int x, int y, float initialVelocity, float vx, float vy, String tankSig) {
        this.app = app;
        this.x = x;
        this.y = y;
        this.initialVelocity = initialVelocity;
        this.vx = vx;
        this.vy = vy;
        this.w = w;
        this.tankSig = tankSig;
    }
    /**
     * Simulates the trajectory of the projectile.
     * It updates the position of the projectile and checks for collisions with tanks and terrain.
     */
    public void shoot() {
        //this is the acceleration affected by wind ** NEED TO ADJUST THIS 
        if (isProjectileInAir) {

            w = (int)(w * 0.3 * 0.3);

            if (current_counter % counter == 0) {
                vy -= ay;
                vx += w;
            }
            current_counter++;
            
            // draw projectile using constant velocity and gravity
            x += Math.round(vx);
            y += Math.round(vy);

            app.fill(0, 0, 0);
            app.ellipse(x, y, 10, 10); 
            for(Tank tank: Terrain.playerList) {
                int rangeLower = tank.getX() - 16;
                int rangeUpper = tank.getY() + 16;
                int heightLower = tank.getY() + 16;
                int heightUpper = tank.getY() - 16;

                if (x >= rangeLower && x <= rangeUpper && y <= heightLower && y >= heightUpper) {
                    Collisions collisions = new Collisions(app, x, y, tankSig);
                    collisions.explosion();
                    collisions.terrainFall();
                    isProjectileInAir = false;
                }
            }

            if ((x <= 0) || (x >= 864) || (y <= 0) || (y >= 640)) {
                isProjectileInAir = false;
                
            }
            else if (y >= Terrain.pixelHeights.get(x)) {
                Collisions collisions = new Collisions(app, x, y, tankSig);
                collisions.explosion();
                collisions.terrainFall();
                isProjectileInAir = false;
            }  
        }
    }
    /**
     * @return True if the projectile has collided, otherwise False.
     */
    public boolean isCollided() {
        return !isProjectileInAir;
    }
    /**
     * Gets the x-coordinate of the projectile.
     * @return The x-coordinate of the projectile.
     */
    public int getX() {
        return x;
    }
    /**
     * Gets the y-coordinate of the projectile.
     * @return The y-coordinate of the projectile.
     */
    public int getY() {
        return y;
    }
    
}
