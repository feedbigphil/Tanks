package Tanks;
/**
 * Represents a cell in the game grid, all the characters in the text file.
 */
public class Cell {
    private String type;
    private int x;
    private int y;

    /**
     * Constructs a Cell object with the specified type and coordinates.
     * @param type The type of the cell.
     * @param x The x-coordinate of the cell.
     * @param y The y-coordinate of the cell.
     */
    public Cell(String type, int x, int y){
        this.type = type;
        this.x = x; //these store stored in the grid coords ie 20x28
        this.y = y;

    }
    /**
     * Returns the x-coordinate of the cell.
     * @return The x-coordinate of the cell.
     */
    public int getX() {
        return this.x;
    }
    /**
     * Returns the y-coordinate of the cell.
     * @return The y-coordinate of the cell.
     */
    public int getY() {
        return this.y;
    }
    /**
     * Returns the type of the cell.
     * @return The type of the cell.
     */
    public String getType() {
        return this.type;
    }
    /**
     * Sets the x-coordinate of the cell.
     * @param newX The new x-coordinate of the cell.
     */
    public void setX(int newX) {
        this.x = newX;
    }
    /**
     * Sets the y-coordinate of the cell.
     * @param newY The new y-coordinate of the cell.
     */
    public void setY(int newY) {
        this.y = newY;
    }
}
