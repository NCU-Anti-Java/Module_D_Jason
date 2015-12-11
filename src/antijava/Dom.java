package antijava;

import java.awt.*;
import java.util.Vector;

public class Dom implements DomInterface {
    public TcpcmInterface tcpcm;

    public int currentClientNo = -1;
    public Vector<Sprite> dynamicObjects = new Vector<>();

    // Add a new virtual character to map
    public void addVirtualCharacter(int clientno) throws Exception {
        // Check number of characters on map
        if (getVirtualCharacterCount() >= 4)
            throw new Exception("The map already contains 4 characters. Can't add more characters.");

        // Check if character with same clientno exists
        if (getVirtualCharacterByNo(clientno) != null)
            throw new Exception("Client no. " + clientno + " already exists.");

        // Add new character
        dynamicObjects.add(new CharacterSprite(clientno));
    }

    // Add a new item to map
    public void addItem(String name, int index, boolean shared, int x, int y) throws Exception {
        // Check if item with same index exists
        if (getItemByIndex(index) != null)
            throw new Exception("Item no. " + index + " already exists.");

        dynamicObjects.add(new ItemSprite(name, index, shared, x, y));
    }

    // Update character with specific client no.
    public void updateVirtualCharacter(int clientno, int dir, int speed, int x, int y) throws Exception {
        // Check direction
        if (dir < 0 || dir > 3)
            throw new Exception("Direction is illegal.");

        // Get specific character
        CharacterSprite s = getVirtualCharacterByNo(clientno);

        if (s != null) {
            // Update attributes
            s.update(dir, speed, new Point(x, y));
        } else {
            throw new Exception("Character of client no. " + clientno + " doesn't exist.");
        }
    }

    public void updateItem(int index, boolean shared, int owner) throws Exception {
        // Update item with specific index

        // Check if owner exists
        if (owner != -1 && getVirtualCharacterByNo(owner) == null)
            throw new Exception("Owner is invalid. Character of client no. " + owner + " doesn't exist.");

        // Get item with specific index
        ItemSprite s = getItemByIndex(index);

        // Check if item is null
        if (s == null)
            throw new Exception("Item no. " + index + " doesn't exist.");

        // Check if the item is owned by others
        if (s.owner != -1)
            throw new Exception("Item no. " + index + " is owned by client no. " + s.owner + ".");

        // Update
        s.shared = shared;
        s.owner = owner;
    }

    public Vector getAllDynamicObjects() {
        // Get a Vector which contains all the dynamic objects
        return dynamicObjects;
    }

    public Point getVirtualCharacterXY() throws Exception {
        // Get position of character of current client
        // Check current client no.
        if (currentClientNo == -1)
            throw new Exception("Client no. of this client is not set.");

        // Get character by client no.
        CharacterSprite s = getVirtualCharacterByNo(currentClientNo);

        // Return position
        if (s != null) return s.pos;

        throw new Exception("Virtual character of this client doesn't exist.");
    }

    public void keyGETPressed() throws Exception {
        // When GET key is pressed, try get item the character is facing
        // Check if TCPCM is initialized
        if (tcpcm == null)
            throw new Exception("TCPCM is not initialized.");

        // Get direction of the character
        int dir = getVirtualCharacterDir();

        // Get position of the character
        Point p = getVirtualCharacterXY();

        // Get position that the character is facing
        Point facingPos = new Point(p.x, p.y);

        if (dir == 0) // East
            facingPos.x += 1;
        else if (dir == 1) // South
            facingPos.y -= 1;
        else if (dir == 2) // North
            facingPos.y += 1;
        else if (dir == 3) // West
            facingPos.x -= 1;

        // Try get item on the position that the character is facing
        ItemSprite item = getItemByPos(facingPos);

        // See can get or not
        if (item != null && item.shared && item.owner == -1) {
            updateItem(item.index, true, currentClientNo);
            tcpcm.inputMoves(4);
        }

        // Do nothing if can't get item
    }

    // Other methods

    public int getVirtualCharacterCount() {
        // Count how many characters on map
        int count = 0;

        for (Sprite s : dynamicObjects) {
            if (s instanceof CharacterSprite) {
                count++;
            }
        }

        return count;
    }

    public CharacterSprite getVirtualCharacterByNo(int clientno) {
        // Return CharacterSprite with specified client no.
        for (Sprite s : dynamicObjects) {
            if (s instanceof CharacterSprite && ((CharacterSprite) s).clientno == clientno) {
                return (CharacterSprite) s;
            }
        }

        return null;
    }

    public int getVirtualCharacterDir() throws Exception {
        // Return direction of current client's character
        if (currentClientNo == -1)
            throw new Exception("Client no. of current client is not set.");

        CharacterSprite s = getVirtualCharacterByNo(currentClientNo);

        if (s != null) return s.dir;

        throw new Exception("Virtual character of current client is not found.");
    }

    public ItemSprite getItemByIndex(int index) {
        // Get item by its index
        for (Sprite s : dynamicObjects) {
            if (s instanceof ItemSprite && ((ItemSprite) s).index == index) {
                return (ItemSprite) s;
            }
        }

        return null;
    }

    public ItemSprite getItemByPos(Point p) {
        // Get item by its position
        for (Sprite s : dynamicObjects) {
            if (s instanceof ItemSprite && s.pos.x == p.x && s.pos.y == p.y) {
                return (ItemSprite) s;
            }
        }

        return null;
    }
}
