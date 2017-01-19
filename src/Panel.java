import java.io.*;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Panel instantiates the window that gameplay occurs in.
 * Creates a thread that refreshes the screen with updated
 * information and events, such as missles, explosions, and
 * batteries/cities.
 */
public class Panel extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    /**
     * Array of city objects
     */
    private City[] cities;

    /**
     * Array of battery objects
     */
    private Battery[] batteries;

    /**
     * ArrayList of missile objects
     */
    private ArrayList<Missile> missiles;

    /**
     * ArrayList of explosions
     */
    private ArrayList<Explosion> explosions;

    /**
     * ArraryList of friendly missiles fired from the batteries
     */
    private ArrayList<Missile> friendlyMissiles;

    /**
     * ArrayList of missiles to be removed, as to avoid issues
     * with removing from arraylists before it is safe
     */
    private ArrayList<Missile> missilesToRemove;

    /**
     * Point location of cursor, also where the cross hairs are located
     */
    private Point mousePosition;

    /**
     * Instantiates each of the data members, creates a new thread that
     * calls repaint() and checks for round/game over, and calls
     * start() on that thread
     */
    public Panel() {
        cities = new City[6];
        batteries = new Battery[3];
        missiles = new ArrayList<Missile>();
        explosions = new ArrayList<Explosion>();
        friendlyMissiles = new ArrayList<Missile>();
        missilesToRemove = new ArrayList<Missile>();


        this.setBackground(Color.BLACK);

        //instantiating cities
        int x = 180;
        int y = 870;
        Color city_color_0 = new Color(65, 86, 159);
        Color city_color_1 = new Color(109, 123, 181);
        Color general;
        for (int i = 0; i < cities.length; i++) {
            Point p = new Point(x, y);
            general = (i % 2 == 0) ? city_color_0 : city_color_1;
            City c = new City(p, general);
            cities[i] = c;
            if (i != 2) {
                x += 70;
            } else {
                x += 180 + 70;
            }
        }

        //instantiating batteries
        x = 0;
        y = 870;
        Color battery_color_0 = new Color(155, 20, 10);
        for (int i = 0; i < batteries.length; i++) {
            Point p = new Point(x, y);
            Battery batt = new Battery(p, battery_color_0);
            batteries[i] = batt;
            x += 180 + 210;
        }
        //center of the screen
        mousePosition = new Point(480, 480);
        addMouseMotionListener(this);
        addMouseListener(this);
        addKeyListener(this);
        setFocusable(true);

        Thread a = new Thread() {
            public void run() {
                int count = 0;
                try {
                    while (!isOver()) {
                        if(count < 375 && count % 25 == 0)
                            addEnemyMissiles();
                        count++;

                        repaint();
                        if(!roundOver())
                            Thread.sleep(35);
                        else {
                            Thread.sleep(1500);
                            populateBatteries();
                            count = 0;
                        }
                    }
                } catch (InterruptedException e) {}
            }
        };
        a.start();
    }

    /**
     * Plays a song with specified argument file name
     * @param filename string name of file to be played
     */
    public void playSound(String filename){
        try{
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(new File(filename)));
            clip.start();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Repopulates the batteries with new references once the round is over
     */
    public void populateBatteries(){
        int x = 0;
        int y = 870;
        Color battery_color = new Color(155, 20, 10);
        for(int i = 0; i < batteries.length; i++){
            Point p = new Point(x, y);
            batteries[i] = new Battery(p, battery_color);
            x += 180 + 210;
        }
        missiles.clear();
        friendlyMissiles.clear();
        explosions.clear();
    }

    /**
     * Game is over when none of the cities are active, or the size of
     * friendly missiles = 0
     * @return true if round is over
     */
    public boolean isOver(){

        int count = 0;
        for(City c: cities)
            if(!c.isActive())
                count++;
        if(count == 6 && friendlyMissiles.size() == 0) {
            playSound("game-over-trumpet.wav");
            return true;
        }
        else
            return false;
    }

    /**
     * Round is over when all the enemy missiles have been fired and destroyed
     * and when the player's missiles have all been fired
     * @return true if former case is satisfied
     */
    public boolean roundOver(){

        int count = 0;
        for (Battery b: batteries){
            count += b.getNumMissiles();
        }

        if(missiles.size() == 0 && friendlyMissiles.size() == 0) {
            playSound("round-over.wav");
            return true;
        }
        if(count > 0)
            return false;
        return false;
    }

    /**
     * Loops through each data member and draws those that are active
     * Clears temporary trash bin (to be removed ArrayList) at the end
     * of the function as to avoid issues with removing from ArrayLists
     *
     * Also checks for collisions and updates explosions, missiles, cities,
     * and batteries accordingly
     *
     * calls super.paintComponent(g) first
     * @param g graphics to be used to draw
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        //collision detection
        for (int i = 0; i < explosions.size(); i++){
            for(int j = 0; j < missiles.size(); j ++){
                if(explosions.get(i).contains(missiles.get(j).getLocPoint())){
                    Point x = missiles.get(j).getLocPoint();
                    missilesToRemove.add(missiles.get(j));
                    Explosion e = new Explosion(x);
                    explosions.add(e);
                }
            }
        }

        for (int i = 0; i < batteries.length; i++){
            batteries[i].draw(g);

        }
        for (int i = 0; i < cities.length; i++){
            cities[i].draw(g);

        }
        for (Missile m : missiles) {
            if(m.isActive()) {
                m.draw(g);
            }
            else{
                Point p = m.getLocPoint();
                missilesToRemove.add(m);
                Explosion e = new Explosion(p);
                explosions.add(e);
                playSound("explosion_1.wav");
            }

        }
        for(int i = 0; i < friendlyMissiles.size(); i++){
            Missile m = friendlyMissiles.get(i);
            if(m.isActive()){
                m.draw(g);
            } else {
                Point exp = m.getLocPoint();
                friendlyMissiles.remove(i);
                Explosion e = new Explosion(exp);
                explosions.add(e);
                playSound("explosion_1.wav");
            }
        }

        for(int i = 0; i < explosions.size(); i++){
            if(explosions.get(i).isActive()) {
                explosions.get(i).draw(g);
            }
        }
        //drawing cross hairs
        g.setColor(Color.white);
        int x = (int) mousePosition.getX();
        int y = (int) mousePosition.getY();
        g.drawLine(x, y, x + 7, y);
        g.drawLine(x, y, x, y + 7);
        g.drawLine(x, y, x - 7, y);
        g.drawLine(x, y, x, y - 7);

        for(Missile m: missilesToRemove) {
            Point extended_1 = new Point((int)(m.getLocPoint().getX())-35,
                    (int)(m.getLocPoint().getY()));
            Point extended_2 = new Point((int)(m.getLocPoint().getX())-90,
                    (int)(m.getLocPoint().getY()));
            for(City city: cities){
                city.isHit(extended_1);
            }
            for(Battery battery: batteries){
                battery.isHit(extended_2);
            }
            missiles.remove(m);
        }
        missilesToRemove.clear();

    }

    /**
     * Adds a missile to the enemy missile ArrayList,
     * and chooses a random target as long as the chosen target isActive()
     * or it is not out of missiles. If the chosen target is inactive or
     * out of missiles, another random target is selected.
     * Rinse and repeat until a valid target is selected
     */
    public void addEnemyMissiles() {
        //drawing missiles

        int rand_start = (int) (Math.random() * 960);

        boolean chosen = false;
        int rand_target = 0;
        while(!chosen){
            rand_target = (int) (Math.random() * 9);
            if(rand_target >= 0 && rand_target < 3){
                if(batteries[rand_target].getNumMissiles() > 0) {
                    chosen = true;
                }
            } else if( rand_target >= 3 && rand_target < 9){
                if(cities[rand_target-3].isActive()) {
                    chosen = true;
                }
            }
        }

        Point target_point = null;
        switch (rand_target) {
            case 0:
                target_point = new Point(90, 870);
                break;
            case 1:
                target_point = new Point(480, 870);
                break;
            case 2:
                target_point = new Point(870, 870);
                break;
            case 3:
                target_point = new Point(215, 870);
                break;
            case 4:
                target_point = new Point(285, 870);
                break;
            case 5:
                target_point = new Point(355, 870);
                break;
            case 6:
                target_point = new Point(605, 870);
                break;
            case 7:
                target_point = new Point(675, 870);
                break;
            case 8:
                target_point = new Point(745, 870);
                break;
        }

        missiles.add(new Missile(new Point(rand_start, 0), target_point,
                5, 0, new Color(255, 0, 13)));

    }

    /**
     * Adds a missile to the friendly missile ArrayList
     * and calculates the shortest distance to the point
     * specified by the user's click based on the distance formula
     * and whether or not the closest chosen battery has any missiles.
     *
     * If a battery is not active, the next closest battery to the
     * specified location is selected
     */
    public void fireFriendlyMissile(){
        Point battery_fire_point0 = new Point(90, 870);
        Point battery_fire_point1 = new Point(480, 870);
        Point battery_fire_point2 = new Point(870, 870);

        int x_0 = (int)mousePosition.getX();
        int y_0 = (int)mousePosition.getY();
        int x_1 = (int)battery_fire_point0.getX();
        int y_1 = (int)battery_fire_point0.getY();
        int x_2 = (int)battery_fire_point1.getX();
        int y_2 = (int)battery_fire_point1.getY();
        int x_3 = (int)battery_fire_point2.getX();
        int y_3 = (int)battery_fire_point2.getY();

        int distance_0 = (int)Math.sqrt( (Math.pow((x_0-x_1), 2)) + (Math.pow((y_0-y_1), 2)) );
        int distance_1 = (int)Math.sqrt( (Math.pow((x_0-x_2), 2)) + (Math.pow((y_0-y_2), 2)) );
        int distance_2 = (int)Math.sqrt( (Math.pow((x_0-x_3), 2)) + (Math.pow((y_0-y_3), 2)) );

        int small = 0;
        if(distance_0 < distance_1 && distance_0 < distance_2)
            small = distance_0;
        else if (distance_1 < distance_0 && distance_1 < distance_2)
            small = distance_1;
        else if (distance_2 < distance_0 && distance_2 < distance_1)
            small = distance_2;

        boolean canFire[] = new boolean[3];
        for(int j = 0; j < canFire.length; j++){
            if(batteries[j].getNumMissiles() > 0)
                canFire[j] = true;
            else
                canFire[j] = false;
        }

        Point start = null;
        if(small == distance_0 && canFire[0]){
            start = battery_fire_point0;
            batteries[0].removeMissiles();
        } else if (small == distance_1 && canFire[1]){
            start = battery_fire_point1;
            batteries[1].removeMissiles();
        } else if(small == distance_2 && canFire[2]) {
            start = battery_fire_point2;
            batteries[2].removeMissiles();
        } else {
            if(canFire[0] && canFire[1] && canFire[2]){
                if(distance_0 < distance_1 && distance_0 < distance_2) {
                    start = battery_fire_point0;
                    batteries[0].removeMissiles();
                }
                else if (distance_1 < distance_0 && distance_1 < distance_2) {
                    start = battery_fire_point1;
                    batteries[1].removeMissiles();
                }
                else if (distance_2 < distance_0 && distance_2 < distance_1) {
                    start = battery_fire_point2;
                    batteries[2].removeMissiles();
                }
            } else if (canFire[0] && canFire[1]) {
                if(distance_0 < distance_1 ) {
                    start = battery_fire_point0;
                    batteries[0].removeMissiles();
                }
                else if (distance_1 < distance_0 ) {
                    start = battery_fire_point1;
                    batteries[1].removeMissiles();
                }
            } else if(canFire[0] && canFire[2]) {
                if(distance_0 < distance_2) {
                    start = battery_fire_point0;
                    batteries[0].removeMissiles();
                }
                else if (distance_2 < distance_0) {
                    start = battery_fire_point2;
                    batteries[2].removeMissiles();
                }
            } else if(canFire[1] && canFire[2]){
                if (distance_1 <  distance_2) {
                    start = battery_fire_point1;
                    batteries[1].removeMissiles();
                }
                else if (distance_2 < distance_1) {
                    start = battery_fire_point2;
                    batteries[2].removeMissiles();
                }
            } else if(canFire[0]){
                start = battery_fire_point0;
                batteries[0].removeMissiles();
            } else if(canFire[1]){
                start = battery_fire_point1;
                batteries[1].removeMissiles();
            } else if(canFire[2]){
                start = battery_fire_point2;
                batteries[2].removeMissiles();
            }
        }
        if(start != null) {
            Point end = new Point(x_0, y_0);
            Missile m = new Missile(start, end, 12, 1, new Color(75, 178, 44));
            friendlyMissiles.add(m);
        } else {
            return;
        }
    }

    /**
     * Not used, but necessary when implementing KeyListener
     * @param e key event from keyboard
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * If the key event e value is either the space bar
     * a friendly missile is fired in the same fashion as mouse click
     * (see fireFriendlyMissile())
     * if the key event e is 1, 2, or 3 keys,
     * a friendly missile is fired from the respective battery as long
     * as the battery has any missiles left
     *
     * @param e key event from keyboard
     */
    @Override
    public void keyPressed(KeyEvent e) {
        int count = 0;
        for(Battery battery: batteries)
            if(battery.getNumMissiles() > 0 )
                count++;
        if(count > 0) {
            playSound("bullet-whizzing-by.wav");
        }
        else {
            playSound("stapler.wav");
        }
        Point end = new Point((int)mousePosition.getX(),
                (int)mousePosition.getY());
        Point start = null;
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            fireFriendlyMissile();
        } else if (e.getKeyCode() == KeyEvent.VK_1 && batteries[0].getNumMissiles() > 0){
            start = new Point(90, 870);
            Missile m = new Missile(start, end, 5, 1, new Color(75, 178, 44));
            friendlyMissiles.add(m);
            batteries[0].removeMissiles();
        } else if (e.getKeyCode() == KeyEvent.VK_2 && batteries[1].getNumMissiles() > 0){
            start = new Point(480, 870);
            Missile m = new Missile(start, end, 5, 1, new Color(75, 178, 44));
            friendlyMissiles.add(m);
            batteries[1].removeMissiles();
        } else if (e.getKeyCode() == KeyEvent.VK_3 && batteries[2].getNumMissiles() > 0){
            start = new Point(870, 870);
            Missile m = new Missile(start, end, 5, 1, new Color(75, 178, 44));
            friendlyMissiles.add(m);
            batteries[2].removeMissiles();
        }
    }

    /**
     * Not used, but necessary when implementing KeyListener
     * @param e key event from keyboard
     */
    @Override
    public void keyReleased(KeyEvent e) {

    }

    /**
     * Not used, but necessary when implementing mouseListener
     * @param e mouse event from mouse
     */
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    /**
     * Fires a friendly missile, and plays the shooting sound
     * @param e mouse event from mouse
     */
    @Override
    public void mousePressed(MouseEvent e) {
        fireFriendlyMissile();
        int count = 0;
        for(Battery battery: batteries)
            if(battery.getNumMissiles() > 0 )
                count++;
        if(count > 0) {
            playSound("bullet-whizzing-by.wav");
        }
        else {
            playSound("stapler.wav");
        }
    }

    /**
     * Not used, but necessary when implementing mouseListener
     * @param e mouse event from mouse
     */
    @Override
    public void mouseReleased(MouseEvent e) {

    }

    /**
     * Not used, but necessary when implementing mousemotionListener
     * @param e mouse event from mouse
     */
    @Override
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Not used, but necessary when implementing mousemotionListener
     * @param e mouse event from mouse
     */
    @Override
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Not used, but necessary when implementing mousemotionListener
     * @param e mouse event from mouse
     */
    @Override
    public void mouseDragged(MouseEvent e) {

    }

    /**
     * Updates the mouse position, which is used by the cross hairs
     * as well as the location that a frienldy missile is to be fired to
     * @param e
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition.setLocation(e.getX(), e.getY());
    }

}

