import java.awt.*;

/**
 * A city is defenseless without the battery with missiles to protect is from
 * incoming enemy missiles. If all the cities are lost, the game is considered to be over
 */
public class City extends Rectangle {
    /**
     * Whether or not the city has been destroyed,
     * i.e. hit by a missile
     */
    private boolean active;

    /**
     * Color of the city when drawn
     */
    private Color color;

    /**
     * Constructor for city
     * @param loc point location of city
     * @param c color of city
     */
    public City(Point loc, Color c){
        super.setLocation(loc);
        this.color = c;
        active = true;
    }

    /**
     * Draws a city based on its location as well as its color
     * @param g graphics passed in from paint component
     */
    public void draw(Graphics g){
        int x = (int) super.getLocation().getX();
        int y = (int) super.getLocation().getY();
        if(active) {
            g.setColor(this.color);
            g.fillRect(x, y, 70, 50);
        } else {
            g.setColor(new Color(131, 131, 131));
            g.drawRect(x, y, 70, 50);
        }
    }

    /**
     * A city is active as long as isHit() returns false
     * and changes the data member
     * @return whether or not the city is active
     */
    public boolean isActive(){
        return active;
    }

    /**
     * Same as battery's isHit() method, a city is hit
     * when a missile reaches the city. This is why point p
     * is checked for equality against the location of
     * the battery
     *
     * If the missile has hit the city, this method sets
     * active = false
     * @param p point to be checked for equality against
     *          city's current location
     * @return if point p is = city's location
     */
    public boolean isHit(Point p){
        if(super.getLocation().getX() == p.getX())
            active = false;
        else
            return false;
        return true;
    }

    /**
     * Gets location of city with super's getLocation()
     * @return location of the city
     */
    public Point getLocPoint(){
        return super.getLocation();
    }

}
