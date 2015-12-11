package antijava.test;

import antijava.Dom;
import antijava.Spritere;
import antijava.UimInterface;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class SpritereTest {
    private Spritere spritere;

    @Before
    public void setUp() throws Exception {
        spritere = new Spritere();
    }

    @Test
    public void testRenderSprites() throws Exception {
        class uim implements UimInterface {
            private JFrame frame;

            private uim() {
                frame = new JFrame("Test");
                frame.setContentPane(new JPanel());
                frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
                frame.setSize(400, 400);
                frame.setVisible(true);
            }

            public JFrame getWindow() {
                return frame;
            }
        }
        spritere.uim = new uim();

        spritere.dom = new Dom();

        spritere.dom.addItem("", 1, true, 1, 1);

        spritere.renderSprites();
    }
}