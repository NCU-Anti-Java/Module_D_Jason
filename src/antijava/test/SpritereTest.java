package antijava.test;

import antijava.DomInterface;
import antijava.SpriteInterface;
import antijava.Spritere;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SpritereTest {
    private Spritere spritere;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        spritere = new Spritere();

        spritere.dom = new DomInterface() {
            public Vector<SpriteInterface> dynamicObjects = new Vector<>();

            @Override
            public void addVirtualCharacter(int clientno) throws Exception {
                dynamicObjects.add(() -> System.out.print("C" + clientno));
            }

            @Override
            public void addItem(String name, int index, boolean shared, int x, int y) throws Exception {
                dynamicObjects.add(() -> System.out.print("I" + index));
            }

            @Override
            public void updateVirtualCharacter(int clientno, int dir, int speed, int x, int y) throws Exception {

            }

            @Override
            public void updateItem(int index, boolean shared, int owner) throws Exception {

            }

            @Override
            public Vector getAllDynamicObjects() {
                return dynamicObjects;
            }

            @Override
            public Point getVirtualCharacterXY() throws Exception {
                return null;
            }

            @Override
            public void keyGETPressed() throws Exception {

            }
        };

        System.setOut(new PrintStream(outContent));
    }

    @Test
    public void testRenderSpritesCharacterSingle() throws Exception {
        // Test drawing single character
        spritere.dom.addVirtualCharacter(1);

        spritere.renderSprites();

        assertEquals("Only character 1 should be drawn.", outContent.toString(), "C1");
    }

    @Test
    public void testRenderSpritesCharacterMultiple() throws Exception {
        // Test drawing multiple characters
        spritere.dom.addVirtualCharacter(1);
        spritere.dom.addVirtualCharacter(2);
        spritere.dom.addVirtualCharacter(3);
        spritere.dom.addVirtualCharacter(4);

        spritere.renderSprites();

        String output = outContent.toString();
        assertTrue("Character 1 should be drawn.", output.contains("C1"));
        assertTrue("Character 2 should be drawn.", output.contains("C2"));
        assertTrue("Character 3 should be drawn.", output.contains("C3"));
        assertTrue("Character 4 should be drawn.", output.contains("C4"));
    }

    @Test
    public void testRenderSpritesItemSingle() throws Exception {
        // Test drawing single item
        spritere.dom.addItem("", 1, true, 1, 1);

        spritere.renderSprites();

        assertEquals("Only item 1 should be drawn.", outContent.toString(), "I1");
    }

    @Test
    public void testRenderSpritesItemMultiple() throws Exception {
        // Test drawing multiple items
        spritere.dom.addItem("", 1, true, 1, 1);
        spritere.dom.addItem("", 2, true, 1, 1);
        spritere.dom.addItem("", 3, true, 1, 1);

        spritere.renderSprites();

        String output = outContent.toString();
        assertTrue("Item 1 should be drawn.", output.contains("I1"));
        assertTrue("Item 2 should be drawn.", output.contains("I2"));
        assertTrue("Item 3 should be drawn.", output.contains("I3"));
    }

    @Test
    public void testRenderSpritesMix() throws Exception {
        // Test drawing items and characters
        spritere.dom.addItem("", 1, true, 1, 1);
        spritere.dom.addVirtualCharacter(3);
        spritere.dom.addItem("", 2, true, 1, 1);
        spritere.dom.addVirtualCharacter(4);

        spritere.renderSprites();

        String output = outContent.toString();
        assertTrue("Item 1 should be drawn.", output.contains("I1"));
        assertTrue("Item 2 should be drawn.", output.contains("I2"));
        assertTrue("Character 3 should be drawn.", output.contains("C3"));
        assertTrue("Character 4 should be drawn.", output.contains("C4"));
    }

    @Test
    public void testRenderSpritesEmpty() throws Exception {
        // Test drawing from empty vector
        spritere.renderSprites();

        assertEquals("No sprite should be drawn.", outContent.toString(), "");
    }

    @Test
    public void testRenderSpritesNull() throws Exception {
        // Test when Vector is null
        expectedEx.expect(Exception.class);
        expectedEx.expectMessage("Vector is null.");

        spritere.dom = new DomInterface() {
            @Override
            public void addVirtualCharacter(int clientno) throws Exception {

            }

            @Override
            public void addItem(String name, int index, boolean shared, int x, int y) throws Exception {

            }

            @Override
            public void updateVirtualCharacter(int clientno, int dir, int speed, int x, int y) throws Exception {

            }

            @Override
            public void updateItem(int index, boolean shared, int owner) throws Exception {

            }

            @Override
            public Vector getAllDynamicObjects() {
                return null;
            }

            @Override
            public Point getVirtualCharacterXY() throws Exception {
                return null;
            }

            @Override
            public void keyGETPressed() throws Exception {

            }
        };

        spritere.renderSprites();
    }
}
