package antijava.test;

import antijava.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.util.Vector;

import static org.junit.Assert.*;

public class DomTest {
    public Dom dom;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() {
        dom = new Dom();
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void tearDown() {
        System.setOut(null);
    }

    @Test
    public void testAddVirtualCharacterSingle() throws Exception {
        // Test adding a virtual character
        dom.addVirtualCharacter(1);
        assertEquals("Adding character failed. Size should be 1.",
                dom.dynamicObjects.size(), 1);

        // Test type of instance
        assertTrue("Should be a CharacterSprite instance.",
                dom.dynamicObjects.elementAt(0) instanceof CharacterSprite);

        // Test attributes
        assertTrue("Character's client no. should be 1.",
                ((CharacterSprite) dom.dynamicObjects.elementAt(0)).clientno == 1);
        assertEquals("Initial direction will be East (0).",
                ((CharacterSprite) dom.dynamicObjects.elementAt(0)).dir, 0);
        assertEquals("Initial speed will be 0.",
                ((CharacterSprite) dom.dynamicObjects.elementAt(0)).speed, 0);
        assertEquals("Initial position on x axis will be 0.",
                ((CharacterSprite) dom.dynamicObjects.elementAt(0)).pos.x, 0);
        assertEquals("Initial position on y axis will be 0.",
                ((CharacterSprite) dom.dynamicObjects.elementAt(0)).pos.y, 0);
    }

    @Test
    public void testAddVirtualCharacterMultiple() throws Exception {
        // Test adding multiple virtual characters
        dom.addVirtualCharacter(1);
        dom.addVirtualCharacter(2);
        dom.addVirtualCharacter(3);
        dom.addVirtualCharacter(4);
        assertEquals("Adding multiple characters failed. Size should be 4.",
                dom.dynamicObjects.size(), 4);
    }

    @Test
    public void testAddVirtualCharacterExcess() throws Exception {
        // Test adding 5 virtual characters and being rejected
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("The map already contains 4 characters. Can't add more characters.");

        dom.addVirtualCharacter(1);
        dom.addVirtualCharacter(2);
        dom.addVirtualCharacter(3);
        dom.addVirtualCharacter(4);
        dom.addVirtualCharacter(5);
    }

    @Test
    public void testAddVirtualCharacterSameNo() throws Exception {
        // Test adding same virtual character and being rejected
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Client no. 1 already exists.");

        dom.addVirtualCharacter(1);
        dom.addVirtualCharacter(1);
    }

    @Test
    public void testAddItemSingle() throws Exception {
        // Test adding a item
        dom.addItem("Item", 1, true, 3, -4);
        assertEquals("Adding item failed. Size should be 1.",
                dom.dynamicObjects.size(), 1);

        // Test type of instance
        assertTrue("Should be a ItemSprite instance.",
                dom.dynamicObjects.elementAt(0) instanceof ItemSprite);

        // Test attributes
        assertTrue("Item's index should be 1.",
                ((ItemSprite) dom.dynamicObjects.elementAt(0)).index == 1);
        assertEquals("Item's name should be \"Item\".",
                ((ItemSprite) dom.dynamicObjects.elementAt(0)).name, "Item");
        assertTrue("Item should can be shared.",
                ((ItemSprite) dom.dynamicObjects.elementAt(0)).shared);
        assertEquals("Initial position on x axis will be 0.",
                ((ItemSprite) dom.dynamicObjects.elementAt(0)).pos.x, 3);
        assertEquals("Initial position on y axis will be 0.",
                ((ItemSprite) dom.dynamicObjects.elementAt(0)).pos.y, -4);
        assertEquals("Item should not be owned by anyone at start.",
                ((ItemSprite) dom.dynamicObjects.elementAt(0)).owner, -1);
    }

    @Test
    public void testAddItemMultiple() throws Exception {
        // Test adding multiple items
        dom.addItem("Item1", 1, true, 3, -4);
        dom.addItem("Item2", 2, false, 4, 2);
        dom.addItem("Item3", 3, true, 0, 1);
        dom.addItem("Item4", 4, true, -2, -3);
        dom.addItem("Item5", 5, true, 1, 0);
        assertEquals("Adding multiple items failed. Size should be 5.",
                dom.dynamicObjects.size(), 5);
    }

    @Test
    public void testAddItemSameIndex() throws Exception {
        // Test adding items with same index and being rejected
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Item no. 1 already exists.");

        dom.addItem("Item1", 1, true, 3, -4);
        dom.addItem("Item2", 1, false, 4, 2);
    }

    @Test
    public void testUpdateVirtualCharacter() throws Exception {
        // Test updating character's status
        dom.addVirtualCharacter(1);
        dom.updateVirtualCharacter(1, 0, -1, 3, 4);

        // Get the only character instance
        CharacterSprite c = (CharacterSprite) dom.dynamicObjects.elementAt(0);

        // Test attributes
        assertEquals("Client no. should be 1.", c.clientno, 1);
        assertEquals("Direction should be East (0).", c.dir, 0);
        assertEquals("Speed should be -1.", c.speed, -1);
        assertEquals("Position on x axis should be 3.", c.pos.x, 3);
        assertEquals("Position on y axis should be 4.", c.pos.y, 4);
    }

    @Test
    public void testUpdateVirtualCharacterIllegalDir() throws Exception {
        // Test updating character's direction using illegal value and being rejected
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Direction is illegal.");

        dom.addVirtualCharacter(1);
        dom.updateVirtualCharacter(1, -1, -1, 3, 4);
    }

    @Test
    public void testUpdateVirtualCharacterNotExist() throws Exception {
        // Test updating character which doesn't exist and being rejected
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Character of client no. 3 doesn't exist.");

        dom.addVirtualCharacter(1);
        dom.updateVirtualCharacter(3, 0, -1, 3, 4);
    }

    @Test
    public void testUpdateItem() throws Exception {
        // Test updating a item
        dom.addItem("Item", 1, true, 3, -4);
        dom.addVirtualCharacter(1);
        dom.updateItem(1, false, 1);

        // Get item
        ItemSprite item = null;
        for (SpriteInterface s : dom.dynamicObjects) {
            if (s instanceof ItemSprite && ((ItemSprite) s).index == 1) {
                item = (ItemSprite) s;
                break;
            }
        }

        // Check attributes
        assertNotNull("Item should not be null.", item);
        assertEquals("Index of the item is not 1.", item.index, 1);
        assertFalse("The item should not be shared.", item.shared);
        assertEquals("The item should be owned by client no. 1.", item.owner, 1);
        assertEquals("Position on x axis should be 3.", item.pos.x, 3);
        assertEquals("Position on y axis should be 4.", item.pos.y, -4);
    }

    @Test
    public void testUpdateItemInvalidOwner() throws Exception {
        // Test updating a item with invalid owner no.
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Owner is invalid. Character of client no. 100 doesn't exist.");

        dom.addItem("Item", 1, true, 3, -4);
        dom.updateItem(1, false, 100);
    }

    @Test
    public void testUpdateItemAlreadyOwned() throws Exception {
        // Test updating a item which already owned by others.
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Item no. 1 is owned by client no. 1.");

        dom.addVirtualCharacter(1);
        dom.addVirtualCharacter(2);

        dom.addItem("Item", 1, true, 3, -4);
        dom.updateItem(1, false, 1);
        dom.updateItem(1, false, 2);
    }

    @Test
    public void testUpdateItemNotExist() throws Exception {
        // Test updating a non-exist item while character exists
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Item no. 2 doesn't exist.");

        dom.addVirtualCharacter(1);
        dom.updateItem(2, true, 1);
    }

    @Test
    public void testGetAllDynamicObjects() throws Exception {
        // Test getting a Vector contains sprites
        dom.addVirtualCharacter(1);
        dom.addItem("Item", 1, true, 3, -4);
        dom.addVirtualCharacter(2);

        Vector v = dom.getAllDynamicObjects();
        assertEquals("Should be a Vector contains 3 elements.", v.size(), 3);
        assertTrue("Element 1 should be a CharacterSprite.",
                v.elementAt(0) instanceof CharacterSprite);
        assertTrue("Element 2 should be a ItemSprite.",
                v.elementAt(1) instanceof ItemSprite);
        assertTrue("Element 3 should be a CharacterSprite.",
                v.elementAt(2) instanceof CharacterSprite);
    }

    @Test
    public void testGetAllDynamicObjectsEmpty() throws Exception {
        // Test getting a empty Vector
        assertEquals("Should be a empty Vector.", dom.getAllDynamicObjects().size(), 0);
    }

    @Test
    public void testGetVirtualCharacterXY() throws Exception {
        // Test getting position of character of current client
        dom.addVirtualCharacter(1);
        dom.updateVirtualCharacter(1, 1, 0, 3, 4);
        dom.currentClientNo = 1;
        Point p = dom.getVirtualCharacterXY();

        assertEquals("Position of x axis of this client should be 3.",
                p.x, 3);
        assertEquals("Position of y axis of this client should be 4.",
                p.y, 4);
    }

    @Test
    public void testGetVirtualCharacterXYNoCurrNo() throws Exception {
        // Test getting position when client no. of current client is not set
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Client no. of this client is not set.");

        dom.getVirtualCharacterXY();
    }

    @Test
    public void testGetVirtualCharacterXYNotExist() throws Exception {
        // Test getting position when character of current client doesn't exist
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Virtual character of this client doesn't exist.");

        dom.currentClientNo = 1;

        dom.getVirtualCharacterXY();
    }

    @Test
    public void testKeyGETPressedEastYes() throws Exception {
        // Test getting a item from east
        // Initialize TCPCM
        dom.tcpcm = new TcpcmInterface() {
            @Override
            public boolean connectServer(InetAddress serverip) {
                return false;
            }

            @Override
            public void inputMoves(int MoveCode) {
                System.out.print(MoveCode);
            }
        };

        // Add character of current client
        dom.addVirtualCharacter(2);
        dom.updateVirtualCharacter(2, 0, 0, 0, 0);
        dom.currentClientNo = 2;

        // Add a item to test
        dom.addItem("Item", 1, true, 1, 0);

        // Try
        dom.keyGETPressed();

        // Get item
        ItemSprite item = null;
        for (SpriteInterface s : dom.dynamicObjects) {
            if (s instanceof ItemSprite && ((ItemSprite) s).index == 1) {
                item = (ItemSprite) s;
                break;
            }
        }

        // Verify
        assertNotNull("Item should not be null.", item);
        assertEquals("Owner of item no. 1 should be 2.", item.owner, 2);
        assertEquals("Should be GET action (4).", outContent.toString(), "4");
    }

    @Test
    public void testKeyGETPressedSouthYes() throws Exception {
        // Test getting a item from south
        // Initialize TCPCM
        dom.tcpcm = new TcpcmInterface() {
            @Override
            public boolean connectServer(InetAddress serverip) {
                return false;
            }

            @Override
            public void inputMoves(int MoveCode) {
                System.out.print(MoveCode);
            }
        };

        // Add character of current client
        dom.addVirtualCharacter(2);
        dom.updateVirtualCharacter(2, 1, 0, 0, 0);
        dom.currentClientNo = 2;

        // Add a item to test
        dom.addItem("Item", 1, true, 0, -1);

        // Try
        dom.keyGETPressed();

        // Get item
        ItemSprite item = null;
        for (SpriteInterface s : dom.dynamicObjects) {
            if (s instanceof ItemSprite && ((ItemSprite) s).index == 1) {
                item = (ItemSprite) s;
                break;
            }
        }

        // Verify
        assertNotNull("Item should not be null.", item);
        assertEquals("Owner of item no. 1 should be 2.", item.owner, 2);
        assertEquals("Should be GET action (4).", outContent.toString(), "4");
    }

    @Test
    public void testKeyGETPressedNorthYes() throws Exception {
        // Test getting a item from north
        // Initialize TCPCM
        dom.tcpcm = new TcpcmInterface() {
            @Override
            public boolean connectServer(InetAddress serverip) {
                return false;
            }

            @Override
            public void inputMoves(int MoveCode) {
                System.out.print(MoveCode);
            }
        };

        // Add character of current client
        dom.addVirtualCharacter(2);
        dom.updateVirtualCharacter(2, 2, 0, 0, 0);
        dom.currentClientNo = 2;

        // Add a item to test
        dom.addItem("Item", 1, true, 0, 1);

        // Try
        dom.keyGETPressed();

        // Get item
        ItemSprite item = null;
        for (SpriteInterface s : dom.dynamicObjects) {
            if (s instanceof ItemSprite && ((ItemSprite) s).index == 1) {
                item = (ItemSprite) s;
                break;
            }
        }

        // Verify
        assertNotNull("Item should not be null.", item);
        assertEquals("Owner of item no. 1 should be 2.", item.owner, 2);
        assertEquals("Should be GET action (4).", outContent.toString(), "4");
    }

    @Test
    public void testKeyGETPressedWestYes() throws Exception {
        // Test getting a item from west
        // Initialize TCPCM
        dom.tcpcm = new TcpcmInterface() {
            @Override
            public boolean connectServer(InetAddress serverip) {
                return false;
            }

            @Override
            public void inputMoves(int MoveCode) {
                System.out.print(MoveCode);
            }
        };

        // Add character of current client
        dom.addVirtualCharacter(2);
        dom.updateVirtualCharacter(2, 3, 0, 0, 0);
        dom.currentClientNo = 2;

        // Add a item to test
        dom.addItem("Item", 1, true, -1, 0);

        // Try
        dom.keyGETPressed();

        // Get item
        ItemSprite item = null;
        for (SpriteInterface s : dom.dynamicObjects) {
            if (s instanceof ItemSprite && ((ItemSprite) s).index == 1) {
                item = (ItemSprite) s;
                break;
            }
        }

        // Verify
        assertNotNull("Item should not be null.", item);
        assertEquals("Owner of item no. 1 should be 2.", item.owner, 2);
        assertEquals("Should be GET action (4).", outContent.toString(), "4");
    }

    @Test
    public void testKeyGETPressedEastNo() throws Exception {
        // Test getting a item from east
        // Initialize TCPCM
        dom.tcpcm = new TcpcmInterface() {
            @Override
            public boolean connectServer(InetAddress serverip) {
                return false;
            }

            @Override
            public void inputMoves(int MoveCode) {
                System.out.print(MoveCode);
            }
        };

        // Add character of current client
        dom.addVirtualCharacter(2);
        dom.updateVirtualCharacter(2, 0, 0, 0, 0);
        dom.currentClientNo = 2;

        // Add a item to the west of character
        dom.addItem("Item", 1, true, -1, 0);

        // Try
        dom.keyGETPressed();

        // Get item
        ItemSprite item = null;
        for (SpriteInterface s : dom.dynamicObjects) {
            if (s instanceof ItemSprite && ((ItemSprite) s).index == 1) {
                item = (ItemSprite) s;
                break;
            }
        }

        // Verify
        assertNotNull("Item should not be null.", item);
        assertEquals("Owner of item no. 1 should not be owned.", item.owner, -1);
        assertEquals("Should not do GET action.", outContent.toString(), "");
    }

    @Test
    public void testKeyGETPressedSouthNo() throws Exception {
        // Test getting a item from south
        // Initialize TCPCM
        dom.tcpcm = new TcpcmInterface() {
            @Override
            public boolean connectServer(InetAddress serverip) {
                return false;
            }

            @Override
            public void inputMoves(int MoveCode) {
                System.out.print(MoveCode);
            }
        };

        // Add character of current client
        dom.addVirtualCharacter(2);
        dom.updateVirtualCharacter(2, 1, 0, 0, 0);
        dom.currentClientNo = 2;

        // Add a item to the north of character
        dom.addItem("Item", 1, true, 0, 1);

        // Try
        dom.keyGETPressed();

        // Get item
        ItemSprite item = null;
        for (SpriteInterface s : dom.dynamicObjects) {
            if (s instanceof ItemSprite && ((ItemSprite) s).index == 1) {
                item = (ItemSprite) s;
                break;
            }
        }

        // Verify
        assertNotNull("Item should not be null.", item);
        assertEquals("Owner of item no. 1 should not be owned.", item.owner, -1);
        assertEquals("Should not do GET action.", outContent.toString(), "");
    }

    @Test
    public void testKeyGETPressedNorthNo() throws Exception {
        // Test getting a item from north
        // Initialize TCPCM
        dom.tcpcm = new TcpcmInterface() {
            @Override
            public boolean connectServer(InetAddress serverip) {
                return false;
            }

            @Override
            public void inputMoves(int MoveCode) {
                System.out.print(MoveCode);
            }
        };

        // Add character of current client
        dom.addVirtualCharacter(2);
        dom.updateVirtualCharacter(2, 2, 0, 0, 0);
        dom.currentClientNo = 2;

        // Add a item to the south of character
        dom.addItem("Item", 1, true, 0, -1);

        // Try
        dom.keyGETPressed();

        // Get item
        ItemSprite item = null;
        for (SpriteInterface s : dom.dynamicObjects) {
            if (s instanceof ItemSprite && ((ItemSprite) s).index == 1) {
                item = (ItemSprite) s;
                break;
            }
        }

        // Verify
        assertNotNull("Item should not be null.", item);
        assertEquals("Owner of item no. 1 should not be owned.", item.owner, -1);
        assertEquals("Should not do GET action.", outContent.toString(), "");
    }

    @Test
    public void testKeyGETPressedWestNo() throws Exception {
        // Test getting a item from west
        // Initialize TCPCM
        dom.tcpcm = new TcpcmInterface() {
            @Override
            public boolean connectServer(InetAddress serverip) {
                return false;
            }

            @Override
            public void inputMoves(int MoveCode) {
                System.out.print(MoveCode);
            }
        };

        // Add character of current client
        dom.addVirtualCharacter(2);
        dom.updateVirtualCharacter(2, 3, 0, 0, 0);
        dom.currentClientNo = 2;

        // Add a item to the east of character
        dom.addItem("Item", 1, true, 1, 0);

        // Try
        dom.keyGETPressed();

        // Get item
        ItemSprite item = null;
        for (SpriteInterface s : dom.dynamicObjects) {
            if (s instanceof ItemSprite && ((ItemSprite) s).index == 1) {
                item = (ItemSprite) s;
                break;
            }
        }

        // Verify
        assertNotNull("Item should not be null.", item);
        assertEquals("Owner of item no. 1 should not be owned.", item.owner, -1);
        assertEquals("Should not do GET action.", outContent.toString(), "");
    }

    @Test
    public void testKeyGETPressedTCPCMNotExist() throws Exception {
        // Test TCPCM doesn't exist case
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("TCPCM is not initialized.");

        // Add character of current client
        dom.addVirtualCharacter(2);
        dom.updateVirtualCharacter(2, 3, 0, 0, 0);
        dom.currentClientNo = 2;

        // Add a item to the east of character
        dom.addItem("Item", 1, true, 1, 0);

        // Try
        dom.keyGETPressed();
    }

    @Test
    public void testGetVirtualCharacterCountZero() throws Exception {
        // Test counting a vector with no any character
        assertEquals("Character count should be 0.", dom.getVirtualCharacterCount(), 0);
    }

    @Test
    public void testGetVirtualCharacterCountNotZero() throws Exception {
        // Test counting a vector with some characters
        dom.addVirtualCharacter(1);
        dom.addVirtualCharacter(2);
        dom.addVirtualCharacter(3);
        assertEquals("Character count should be 3.", dom.getVirtualCharacterCount(), 3);
    }

    @Test
    public void testGetVirtualCharacterByNo() throws Exception {
        // Test getting character by client no.
        dom.addVirtualCharacter(1);

        CharacterSprite s = dom.getVirtualCharacterByNo(1);
        assertNotNull("Character no. 1 should exist.", s);
        assertEquals("Client no. of the CharacterSprite should be 1.", s.clientno, 1);
    }

    @Test
    public void testGetVirtualCharacterByNoNotExist() throws Exception {
        // Test getting a not exist character
        CharacterSprite s = dom.getVirtualCharacterByNo(1);
        assertNull("Character no. 1 should not exist.", s);
    }

    @Test
    public void getVirtualCharacterDir() throws Exception {
        // Test getting direction of current client's character
        dom.addVirtualCharacter(1);
        dom.updateVirtualCharacter(1, 1, 2, 3, 4);
        dom.currentClientNo = 1;
        assertEquals("Direction of the character should be North (1).",
                dom.getVirtualCharacterDir(), 1);
    }

    @Test
    public void getVirtualCharacterDirCurrentNotSet() throws Exception {
        // Test getting direction of current client's character
        // while current client no. is not set
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Client no. of current client is not set.");

        dom.addVirtualCharacter(1);
        dom.updateVirtualCharacter(1, 1, 2, 3, 4);

        dom.getVirtualCharacterDir();
    }

    @Test
    public void getVirtualCharacterDirCurrentNotExist() throws Exception {
        // Test getting direction of current client's character
        // while character of current client doesn't exist
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Virtual character of current client is not found.");

        dom.currentClientNo = 1;

        dom.getVirtualCharacterDir();
    }


    @Test
    public void testGetItemByIndex() throws Exception {
        // Test getting item by index
        dom.addItem("Item", 1, true, 12, 34);

        ItemSprite s = dom.getItemByIndex(1);
        assertNotNull("Item should not be null.", s);
        assertEquals("Index of the item should be 1.", s.index, 1);
        assertTrue("Item should can be shared.", s.shared);
        assertEquals("Position on x axis should be 12.", s.pos.x, 12);
        assertEquals("Position on y axis should be 34.", s.pos.y, 34);
    }

    @Test
    public void testGetItemByIndexNotExist() throws Exception {
        // Test getting non-exist item by index
        ItemSprite s = dom.getItemByIndex(1);
        assertNull("Item should be null.", s);
    }

    @Test
    public void testGetItemByPos() throws Exception {
        // Test getting item by position
        dom.addItem("Item", 1, true, 12, 34);

        ItemSprite s = dom.getItemByPos(new Point(12, 34));
        assertNotNull("Item should not be null.", s);
        assertEquals("Index of the item should be 1.", s.index, 1);
        assertTrue("Item should can be shared.", s.shared);
        assertEquals("Position on x axis should be 12.", s.pos.x, 12);
        assertEquals("Position on y axis should be 34.", s.pos.y, 34);
    }

    @Test
    public void testGetItemByPosNotExist() throws Exception {
        // Test getting non-exist item by position
        ItemSprite s = dom.getItemByPos(new Point(12, 34));
        assertNull("Item should be null.", s);
    }
}