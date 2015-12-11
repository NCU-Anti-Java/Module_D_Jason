package antijava;

import javax.swing.*;
import java.awt.*;

public class CharacterSprite extends Sprite {
    public int clientno;
    public int dir;
    public int speed;

    public CharacterSprite(int no) {
        clientno = no;
        update(0, 0, new Point(0, 0));
    }

    public void update(int dir, int speed, Point p) {
        this.dir = dir;
        this.speed = speed;
        this.pos = p;

        image = new ImageIcon("image/green-" + dir + ".png");
    }
}
