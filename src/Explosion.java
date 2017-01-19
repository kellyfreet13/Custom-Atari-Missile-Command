import java.awt.*;

/**
 * Explosion that is drawn as a circle, that can explode enemy missiles
 * if the enemy missiles are contained within the blast radius of the
 * explosion - this is checked in the contains method
 */
public class Explosion extends Rectangle {
    /**
     * If the explosions radius is increasing or decreasing
     */
    private boolean expanding;

    /**
     * If the explosion is active and should be drawn
     */
    private boolean active;

    /**
     * Location of the explosion
     */
    private Point location;

    /**
     * Radius of the explosion
     */
    private int radius;

    /**
     * Constructor for explosion, radius is initially set to 4
     * and expanding is initialized to true
     * @param p point location that the explosion will
     *          be initialized to
     */
    public Explosion(Point p){
        location = p;
        active = true;
        radius = 4;
        expanding = true;
    }

    /**
     * Draws the explosion based upon its current location,
     * color, and radius
     * @param g graphics passed in from paint component
     */
    public void draw(Graphics g){
        g.setColor(Color.white);
        g.fillOval((int)location.getX() - radius/2, (int)location.getY() - radius/2,
                radius, radius);
        this.move();
    }

    /**
     * Expands and contracts the explosion
     * Increases radius if expanding = true;
     * otherwise it decreases the radius
     * Expanding will be set to the opposite value when
     * radius reaches a value of 150,
     * and when the radius is less than 0 active = false
     *
     * Updates the location of the explosion so it appears to
     * be expanding from the center point of the explosion
     * as opposed to expanding from the corner of the unseen box
     */
    public void move(){
        if(expanding){
            radius += 16;
        } else {
            radius -= 12;
        }
        super.setLocation((int)(location.getX() - radius/2), (int)(location.getY() - radius/2));
        super.setSize(radius, radius);
        if(radius >= 150)
            expanding = false;
        else if (radius <= 0)
            active = false;

    }

    /**
     * Whether or not the explosion is active
     * @return if explosion is active
     */
    public boolean isActive(){
        return this.active;
    }

    /**
     * Checks if the argument is within the blast radius
     * of the explosion
     * @param p point location to check if it is contained in the
     *          explosion
     * @return if point p is contained in the explosion
     */
    public boolean contains(Point p){
        return super.contains(p);
    }

    /**
     * Getter for the location of the explosin
     * @return
     */
    public Point getLocPoint(){
        return location;
    }

}
