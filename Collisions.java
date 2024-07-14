package Tanks;

import processing.core.PApplet;
import java.util.ArrayList;
/**
 * The Collisions class manages collision-related logic in the game, such as explosions and damage calculation.
 * It handles interactions between projectiles, tanks, and the terrain.
 */
public class Collisions {
    private PApplet app;
    private int x;
    private int y;
    public static boolean isExploded = false;
    static ArrayList<Tank> tanksTOremove;
    private String tankSig;
   
    /**
     * Constructs a Collisions object.
     * @param app The PApplet object.
     * @param x The x-coordinate of the collision.
     * @param y The y-coordinate of the collision.
     * @param tankSig The signature of the tank involved in the collision.
     */
    public Collisions(PApplet app, int x, int y, String tankSig) {
        this.app = app;
        this.x = x;
        this.y = y; 
        this.tankSig = tankSig;
        tanksTOremove = new ArrayList<>(); //store the tanks that need to be removed
    }
    /**
     * Triggers an explosion animation at the collision point.
     */
    public void explosion() {
        //need to get this animated somehow 
    
        app.strokeWeight(2);
        // Red Circle
        app.stroke(255, 0, 0);
        app.fill(255, 0, 0);
        app.ellipse(x, y, 30, 30);//ends at 30 radius

        // Orange Circle
        app.stroke(255, 165, 0);
        app.fill(255, 165, 0);
        app.ellipse(x, y, 15, 15);//ends at 15 radius

        // Yellow Circle
        app.stroke(255, 255, 0);
        app.fill(255, 255, 0);
        app.ellipse(x, y, 6, 6);//ends at 6 radius
        isExploded = true; 
    }
    /**
     * Inflicts damage to a tank based on its proximity to the explosion.
     * @param tank The tank object to be damaged.
     * @param amount The amount of damage to be inflicted.
     */
    public void damage(Tank tank, int amount) {
        //need to iterate through the playerList and check the x & y coordinates, if they are contained within the circle then they get damaged 
        //also call the tankfall method in this 
        int newHealth = tank.getHealth() - amount;
        int tankX = tank.getX();
        int tankY = tank.getY();
        int terrainY = Terrain.pixelHeights.get(tankX);
        if (newHealth <= 0 || tankY > terrainY) {
            this.x = tankX;
            this.y = tankY;
            newHealth = 0; //just incase it goes and displays it 
            explosion();
            tanksTOremove.add(tank);
        }
        tank.setHealth(newHealth);
        //Updating score of the tank that shot the missile
        if (tank.getChar() != tankSig) {
             if (tankSig.equals("A")) {
            int currScore = App.playerScores.get(0);
            App.playerScores.set(0, currScore + amount);
            }
            if (tankSig.equals("B")) {
                int currScore = App.playerScores.get(1);
                App.playerScores.set(1, currScore + amount);
            }
            if (tankSig.equals("C")) {
                int currScore = App.playerScores.get(2);
                App.playerScores.set(2, currScore + amount);
            }
            if (tankSig.equals("D")) {
                int currScore = App.playerScores.get(3);
                App.playerScores.set(3, currScore + amount);
            }
        }
    }
    /**
     * Retrieves the status of the explosion.
     * @return true if an explosion has occurred, false otherwise.
     */
    public static boolean getIsExploded() {
        return isExploded;
    }
    public void tankfall() {

    }
    public void scoring() {

    }
    /**
     * Updates the terrain after an explosion.
     */
    public void terrainFall() {
        int start = Math.max(0, x - 30);
        int end = Math.min(Terrain.pixelHeights.size() - 1, x + 30);
        float radiusSquared = 30 * 30; // Squared radius as float for more accurate calculations

        for(int i = start; i < end; i++) {
            int deltaY = Terrain.pixelHeights.get(i) - y;
            int deltaX = i - x;
            float distanceSquared = deltaX * deltaX + deltaY * deltaY;

            if (distanceSquared < radiusSquared) {
                // Calculate height change based on a circular explosion effect
                float newHeight = (float)Math.sqrt(radiusSquared - distanceSquared);
                Terrain.pixelHeights.set(i, y + (int)newHeight);
            } else { //this is for smoothing the terrain after the explosions
                if (Terrain.pixelHeights.get(i) < y) {
                    int average = (y + Terrain.pixelHeights.get(i)) / 2;
                    int averageOfaverage = (average + y)/2;
                    Terrain.pixelHeights.set(i, averageOfaverage);
                }
            }
        }
        
        //this is for finding if the tank is in the blast radius
        for (Tank tank: Terrain.playerList) {
            int deltaX = tank.getX() - x;
            int deltaY = tank.getY() - y;
            float distanceSquared = deltaX * deltaX + deltaY * deltaY;

            if (distanceSquared < radiusSquared) {
                // Distribute damage based on distance
                float distance = (float)Math.sqrt(distanceSquared);
                if (distance < 5) {
                    damage(tank, 60);
                } else if (distance < 10) {
                    damage(tank, 40);
                } else if (distance < 15) {
                    damage(tank, 30);
                } else if (distance < 20) {
                    damage(tank, 20);
                } else if (distance < 30) {
                    damage(tank, 10);
                }
            }
        }
}   
}