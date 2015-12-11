import antijava.Dom;
import antijava.ScenereInterface;
import antijava.Spritere;
import antijava.UimInterface;

import javax.swing.*;

public class ModuleD {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Test");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(416, 440);
        frame.setVisible(true);

        UimInterface uim = () -> frame;
        ScenereInterface scenere = () -> {};

        Spritere spritere = new Spritere();
        spritere.uim = uim;
        spritere.dom = new Dom();

        // Samples
        spritere.dom.addItem("", 1, true, 0, 0);
        spritere.dom.addItem("", 2, true, 50, 50);
        spritere.dom.addItem("", 3, true, 100, 100);

        spritere.dom.addVirtualCharacter(1);
        spritere.dom.addVirtualCharacter(2);
        spritere.dom.addVirtualCharacter(3);
        spritere.dom.addVirtualCharacter(4);
        spritere.dom.updateVirtualCharacter(1, 0, 0, 150, 150);
        spritere.dom.updateVirtualCharacter(2, 1, 0, 200, 200);
        spritere.dom.updateVirtualCharacter(3, 2, 0, 250, 250);
        spritere.dom.updateVirtualCharacter(4, 3, 0, 300, 300);

        // Render thread
        class RenderLoop implements Runnable {
            @Override
            public void run() {
                while (true) {
                    try {
                        scenere.renderScene();
                        spritere.renderSprites();
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            }
        }

        Thread renderThread = new Thread(new RenderLoop());
        renderThread.start();
    }
}
