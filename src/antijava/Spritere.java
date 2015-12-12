package antijava;

import java.util.Vector;

public class Spritere {
    public DomInterface dom;

    public void renderSprites() throws Exception {
        Vector<SpriteInterface> objects = dom.getAllDynamicObjects();
        if (objects == null)
            throw new Exception("Vector is null.");

        objects.forEach((sprite) -> sprite.draw());
    }
}
