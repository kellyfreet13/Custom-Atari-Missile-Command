import java.awt.*;

/**
 *  A Battery contains and begins with 10 missiles, and
 *  loses a missile from it's stash each time it fires
 *  a missile at incoming enemy missiles
 *
 *  Drawn based upon its location as well as its color
 */
public class Battery extends Rectangle {

    /**
     * Number of missiles each individual battery has
     */
    private int nunMissiles;

    /**
     * Color of the missile when drawn;
     * Green for friendly
     * Red for enemy
     */
    private Color color;

    /**
     * Constructs a battery object, initializes the number
     * of missiles to 10
     * @param loc location of the battery
     * @param c color of the missile when drawn
     */
    public Battery(Point loc, Color c){
        super.setLocation(loc);
        this.color = c;
        this.nunMissiles = 10;
    }

    /**
     * Draws the battery based upon their current location
     * as well as their color
     * @param g graphics passed in from paintComponent
     */
    public void draw(Graphics g){
        int x = (int) super.getLocation().getX();
        int y = (int) super.getLocation().getY();
        if(nunMissiles > 0) { //will draw battery as 'active'
            g.setColor(this.color);
            g.fillRect(x, y, 180, 50);
        } else { //if the battery is out of missiles, it will be a grey box
            g.setColor(new Color(131, 131, 131));
            g.drawRect(x, y, 180, 50);
        }
        //drawing the number of missiles the battery has
        String numMis =""+ nunMissiles;
        g.setColor(Color.white);
        Font currentFont = g.getFont();
        Font newFont = currentFont.deriveFont(currentFont.getSize() + 5);
        g.setFont(newFont);
        g.drawString(numMis, x + 90, y + 25);
    }

    /**
     * Get the number of missiles the battery has
     * @return number of missiles
     */
    public int getNumMissiles(){
        return this.nunMissiles;
    }

    /**
     * Remove a missile from the battery;
     * subtract 1 from the number of missiles
     */
    public void removeMissiles(){
        nunMissiles--;
    }

    /**
     * Gets the missiles current location by using the rectangles
     * location; this is why super is called
     * @return current location of the battery
     */
    public Point getLocPoint(){
        return super.getLocation();
    }

    /**
     * Check whether or not a battery is hit. i.e.
     * If a missiles end location is equal to the location
     * of the battery, then the battery has been hit
     * and its number of missiles is set to 0
     * @param p location to check against location of the battery
     * @return whether or not battery has been hit
     */
    public boolean isHit(Point p){
        if(super.getLocation().equals(p)){
            nunMissiles = 0;
            return true;
        } else {
            return false;
        }
    }
}
