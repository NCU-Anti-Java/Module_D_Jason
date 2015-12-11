package antijava;

import javax.swing.*;

public class ItemSprite extends Sprite {
    public int index;
    public String name;
    public boolean shared;
    public int owner;

    public ItemSprite(String nameStr, int indexNo, boolean isShared, int posX, int posY) {
        index = indexNo;
        name = nameStr;
        shared = isShared;
        pos.x = posX;
        pos.y = posY;
        owner = -1;
        image = new ImageIcon("image/red.png");
    }
}
