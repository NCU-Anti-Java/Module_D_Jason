package antijava;

import java.awt.*;
import java.util.Vector;

public interface DomInterface {
    void addVirtualCharacter(int clientno) throws Exception;
    void addItem(String name, int index, boolean shared, int x, int y) throws Exception;
    void updateVirtualCharacter(int clientno, int dir, int speed, int x, int y) throws Exception;
    void updateItem(int index, boolean shared, int owner) throws Exception;
    Vector getAllDynamicObjects();
    Point getVirtualCharacterXY() throws Exception;
    void keyGETPressed() throws Exception;
}
