import javax.swing.*;

/**
 * Frame creates a new panel, which begins the game-play
 * initializes the window size, and adds the panel to
 * the pane.
 *
 * the main method constructs a new frame
 */
public class Frame extends JFrame {

    /**
     * Panel variable for frame
     */
    private Panel p;

    /**
     * Instantiates a new panel, sets window size,
     * and adds to the content pane
     */
    public Frame() {
        setBounds(25, 25, 960, 960);
        p = new Panel();
        getContentPane().add(p);

    }

    /**
     * Instantiates a frame, sets the title,
     * sets the standard close operation, and
     * sets the visibility to true
     * @param args standard for main
     */
    public static void main(String[] args) {
        Frame f = new Frame();
        f.setTitle("Arcade missiles");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);
        System.out.println("Make sure to have your sound on!");
    }
}
