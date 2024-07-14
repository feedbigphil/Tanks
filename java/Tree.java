package Tanks;

import processing.core.PApplet;
import processing.core.PImage;
import java.util.Random;
/**
 * The Tree class represents a tree object in the game.
 * It manages the position and drawing of trees on the terrain.
 */
public class Tree {
    private int x;
    private PImage sprite;
    private PApplet app;
    /**
     * Constructs a Tree object.
     * @param app The PApplet object for drawing.
     * @param filename The filename of the tree sprite image.
     */
    public Tree(PApplet app, String filename) {  
        this.sprite = app.loadImage(filename);
        sprite.resize(32, 32);   
        this.app = app;
        randomize();
    }
    /**
     * Randomizes the position of the tree within a specified range (+-30pixels).
     * The tree position is adjusted based on the terrain coordinates.
     */
    public void randomize() { 
        Random random = new Random(); 
        for(Cell tree: Terrain.treeList) { 
            x = tree.getX() * App.CELLSIZE; 
            x = random.nextInt(61) + (x - 30);
            if (x <= 0) {
                x = 1;
            }
            if (x >= 864) {
                x = 860;
            }
            tree.setX(x);
            tree.setY(Terrain.pixelHeights.get(x - 1));
        }
    }   
    /**
     * Draws the tree sprite on the terrain.
     * Trees are adjusted to appear above the terrain surface.
     */
    public void draw() {
        
        for(Cell tree: Terrain.treeList) {
            int treeY = tree.getY();
            int terrainY = Terrain.pixelHeights.get(tree.getX());
            if (treeY < terrainY) {
                tree.setY(treeY + 2);
            }  
            app.image(this.sprite, tree.getX() - 16, tree.getY() - 32); // - 16 & 32 are to adjust the point which the tree sits 
        }       
    }
}
