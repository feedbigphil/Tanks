package Tanks;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;
/**
 * The configReader class reads and parses configuration files for the game.
 * It retrieves level layouts, background images, foreground colors, tree images,
 * and player colors from the configuration file.
 */
public class configReader {
    private JSONObject config;
    private PApplet parent;
    private String directoryPNG;
    private String directoryLevel;
    /**
     * Constructs a configReader object.
     * @param parent The PApplet object for loading the configuration file.
     * @param filePath The file path to the configuration file.
     */
    public configReader(PApplet parent, String filePath) {
        this.parent = parent;
        this.directoryPNG = "/Users/philippefreisler/Documents/USYD/Computer Science/tanks_scaffold/src/main/resources/Tanks/";
        this.directoryLevel = "/Users/philippefreisler/Documents/USYD/Computer Science/tanks_scaffold/";
        config = parent.loadJSONObject(filePath);
    }
    /**
     * Retrieves the array of levels from the configuration file.
     * @return The JSONArray containing the levels.
     */
    public JSONArray getLevels() {
        return config.getJSONArray("levels");
    }
    /**
     * Retrieves a specific level from the configuration file.
     * @param index The index of the level to retrieve.
     * @return The JSONObject representing the specified level.
     */
    public JSONObject getLevel(int index) {
        return getLevels().getJSONObject(index);
    }
    /**
     * Retrieves the layout file path for a specific level.
     * @param levelIndex The index of the level.
     * @return The file path to the layout file.
     */
    public String getLayout(int levelIndex) {
        return this.directoryLevel + getLevel(levelIndex).getString("layout");
    }
    /**
     * Retrieves the background image file path for a specific level.
     * @param levelIndex The index of the level.
     * @return The file path to the background image.
     */
    public String getBackground(int levelIndex) {
        return this.directoryPNG + getLevel(levelIndex).getString("background");
    }
    /**
     * Retrieves the background image file path for a specific level.
     * @param levelIndex The index of the level.
     * @return The file path to the background image.
     */
    public int[] getForegroundColour(int levelIndex) {
        String colour = getLevel(levelIndex).getString("foreground-colour");
        
        String[] rgbValues = colour.split(",\\s*");
        int red = Integer.parseInt(rgbValues[0]);
        int green = Integer.parseInt(rgbValues[1]);
        int blue = Integer.parseInt(rgbValues[2]);
        int[] colourInt = {red, green, blue};
        
        return colourInt;
    }
    /**
     * Retrieves the tree image file path for a specific level.
     * @param levelIndex The index of the level.
     * @return The file path to the tree image, or null if no trees are specified.
     */
    public String getTrees(int levelIndex) {
        if (getLevel(levelIndex).hasKey("trees")) {
            return this.directoryPNG + getLevel(levelIndex).getString("trees");
        } else {
            return null;
        }
    }
    /**
     * Retrieves the player colors from the configuration file.
     * @return The JSONObject containing player colors.
     */
    public JSONObject getPlayerColours() {
        return config.getJSONObject("player_colours");
    }
    /**
     * Retrieves the color for a specific player.
     * @param player The player identifier, Character.
     * @return An array containing the RGB values of the player's color.
     */
    public int[] getPlayerColour(String player) {
        String[] rgbValues = getPlayerColours().getString(player).split(",\\s*");
        int red = Integer.parseInt(rgbValues[0]);
        int green = Integer.parseInt(rgbValues[1]);
        int blue = Integer.parseInt(rgbValues[2]);
        int[] colourInt = {red, green, blue};

        return colourInt;
    }
}
