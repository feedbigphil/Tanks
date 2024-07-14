package Tanks;

import java.util.ArrayList;
import processing.core.PApplet;
import java.util.Collections;
import java.util.Comparator;
/**
 * Represents the terrain in the game.
 */
public class Terrain {   
    char[][] terrainMap;
    ArrayList<Cell> heightList;
    static ArrayList<Tank> playerList;
    static ArrayList<Cell> treeList;
    static ArrayList<Integer> pixelHeights;
    static PApplet app;
    String filename;
    Integer numerator;

    /**
     * Constructs a Terrain object with the specified PApplet instance and filename.
     * @param app The PApplet instance.
     * @param filename The filename of the given text file.
     */
    public Terrain(PApplet app, String filename) {
        this.app = app;
        this.filename = filename;
        terrainMapping(); // terrainMapping method to load the terrain map when the object is created
    }
    /**
     * Loads the terrain data from the file and initializes terrain-related variables. 
     */
    public void terrainMapping() { // Doesnt store the objects in order, Ie reads line by line and adds them scanning down 
        terrainMap = new char[20][28]; //20 rows and 28 columns
        heightList = new ArrayList<>(); //used to store terrain coordinate cell objects ('X' in the text file)
        treeList = new ArrayList<>(); // tree only needs to store one value, because the height of it is always the same as at of the X coordinate it is at
        playerList = new ArrayList<>(4);
        pixelHeights = new ArrayList<>(); //896 length array to store each pixel's height 

        String filePath = app.sketchPath(filename);
        String[] lines = app.loadStrings(filePath);
        
        //for loop used to read the text file and add Cell object to their corresponding arraylist 
        for (int i = 0; i < lines.length && i < 20; i++) {
            String line = lines[i];
            for (int j = 0; j < line.length() && j < 28; j++) {
                terrainMap[i][j] = line.charAt(j);
                if (line.charAt(j) == 'X') {
                    heightList.add(new Cell("X", j, i));
                } else if (line.charAt(j) == 'T') {
                    treeList.add(new Cell("T", j, i));
                } else if (line.charAt(j) == 'A' || line.charAt(j) == 'B' || line.charAt(j) == 'C' || line.charAt(j) == 'D') {
                    playerList.add(new Tank(app, Character.toString(line.charAt(j)), j, i, 100, 3, 250));
                }
            }
        }
        Collections.sort(heightList, (cell1, cell2) -> Integer.compare(cell1.getX(), cell2.getX()));  //to sort the heights in order of their x values 
        Collections.sort(playerList, Comparator.comparing(tank -> tank.getChar())); //sorts tanks based on their characters
        for(Cell cell: heightList) {
            for(int t = 0; t < 31; t++) {
                pixelHeights.add(cell.getY() * 32);
                // System.out.println(pixelHeights); 
            }
            pixelHeights.add(cell.getY() * 32); //add on the last cell an extra 32 times 
        } 
        calculateMA(pixelHeights);  
        calculateMA(pixelHeights);
        Initialise();
        // System.out.println("Terrain first tank X: " + playerList.get(0).getX());
    }
    /**
     * Initializes the starting position of the tanks.
     */
    public void Initialise() {
        //initialises the starting position of the tanks 
          for(Tank player: playerList) {  
              player.setX(player.getX() * App.CELLSIZE);
              player.setY(pixelHeights.get(player.getX()));
          }
    }
    
    /**
     * Calculates the moving average of the pixel heights.
     * @param p The list of pixel heights.
     */
    public void calculateMA(ArrayList<Integer> p) {
        
        for(int pix = 0; pix < p.size() - 31; pix++) {
            //calculate MA formular here 
            int numerator = 0;
            for(int i = 0; i <= 31; i++) { //creates the numerator by adding the next 32 pixel heights
                numerator += p.get(pix + i);
          
            }
            p.set(pix, numerator / 32);
            
        }    
    }
    /**
     * Draws the terrain with specified colors.
     * @param colours An array containing the RGB color values.
     */
    public void draw(int[] colours) {
        int red = colours[0];
        int green = colours[1];
        int blue = colours[2];
        app.stroke(red, green, blue);
        for(int i = 0; i < pixelHeights.size(); i++) {
            float x = i;
            float topY = pixelHeights.get(i);
        
            app.line(x, topY, x, 640);
        }

    }
    public static void main(String[] args) {
        // This is not a PApplet instance, so you can't use "this.app"
        // Instead, you need to create a PApplet instance and pass it to the Terrain constructor
        PApplet app = new PApplet();
        
    }
}