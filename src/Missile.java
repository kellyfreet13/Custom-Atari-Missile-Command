import java.awt.*;

/**
 * A missile has the attributes listed below, and is implemented
 * by both the attacker and the defender
 * Drawn based upon its location as well as its color
 */
public class Missile {
    /**
     * Starting location of the missile
     */
    private Point start;

    /**
     * Current location of the missile
     */
    private Point location;

    /**
     * Ending location of the missile
     */
    private Point end;

    /**
     * Speed of the missile
     */
    private int speed;

    /**
     * Color of the missile
     */
    private Color color;

    /**
     * Type of the missile;
     * 0 for enemy 1 for friendly
     */
    private int type;

    /**
     * True if missile has not been destroyed,
     * false otherwise
     */
    private boolean active;

    /**
     * Amount to be moved in the x axis when position is
     * updated
     */
    private double moveAmtX;

    /**
     * Amount to be moved in the y axis when position is
     * updated
     */
    private double moveAmtY;

    /**
     * Ticks elapsed
     * Not implemented but I kept it here since it
     * was in the UML Diagram
     */
    private int ticksElapsed;

    /**
     * Constructs a missile object
     * Set active to be true initially
     * @param s starting point of the missile
     * @param e ending point of the missile
     * @param sp speed of the missile
     * @param t type of the missile
     * @param c color of the missile
     */
    public Missile(Point s, Point e,
                    int sp, int t, Color c){
        start = s;
        end = e;
        location = s;
        speed = sp;
        type = t;
        color = c;
        active = true;
    }

    /**
     * Checks if the missile has reached its end location;
     * i.e. if end = current
     * If so, set active = false and location = end
     * to avoid oscillation due to slight overshoot
     */
    public void move(){
        if(location.getX() == end.getX() &&
                location.getY() == end.getY()){
            active = false;
            location = end;
            return;
        } else {

            double start_x = start.getX();
            double start_y = start.getY();
            double end_x = end.getX();
            double end_y = end.getY();

            moveAmtX = end_x - start_x;
            moveAmtY = end_y - start_y;

            double mag = Math.sqrt(moveAmtX * moveAmtX +
                    moveAmtY * moveAmtY);

            double R = speed;

            if(mag < speed){
                active = false;
                location = end;
                return;
            }

            double temp_x = location.getX();
            double temp_y = location.getY();
            location.setLocation(temp_x + (moveAmtX * R / mag),
                    temp_y + (moveAmtY * R / mag));
        }

    }

    /**
     * Draws the missile based upon its current location as
     * well as its color
     * @param g graphics passed in from paint component
     */
    public void draw(Graphics g){
        g.setColor(Color.white);
        g.drawLine((int)start.getX(), (int)start.getY(),
                (int)location.getX(), (int)location.getY());
        g.setColor(color);
        g.fillRect((int)location.getX(), (int)location.getY(),
                6, 6);
        this.move();
    }

    /**
     * Getter for whether or not the missile is active
     * @return true if missile is active, false otherwise
     */
    public boolean isActive(){
        return this.active;
    }

    /**
     * returns current location of missile
     * @return current location of missile
     */
    public Point getLocPoint(){
        return this.location;
    }

}
