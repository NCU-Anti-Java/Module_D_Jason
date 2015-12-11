package antijava;

import javax.swing.*;
import java.awt.*;

public class Sprite {
    public Point pos = new Point();
    public ImageIcon image = new ImageIcon();

    public void draw(JPanel panel) {
        JLabel l = new JLabel();
        l.setIcon(image);
        l.setBounds(pos.x, 400 - pos.y - image.getIconHeight(), image.getIconWidth(), image.getIconHeight());
        panel.add(l);
    }
}