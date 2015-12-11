package antijava;

import javax.swing.*;
import java.util.Vector;

public class Spritere {
    public DomInterface dom;
    public UimInterface uim;
    public JPanel panel;

    public void renderSprites() {
        panel = new JPanel();
        panel.setLayout(null);
        panel.setSize(400, 400);

        Vector<Sprite> objects = dom.getAllDynamicObjects();
        objects.forEach((sprite) -> sprite.draw(panel));

        uim.getWindow().setContentPane(panel);
        uim.getWindow().revalidate();
    }
}
