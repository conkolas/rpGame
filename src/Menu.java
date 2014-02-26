/**
 * Created by ai on 2/17/14.
 */
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.state.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;


public class Menu extends BasicGameState {

    private Input input; //Handles menu input interactions
    public Image tileSheetImage;//For now

    public Menu (int state) {

    }

    public void init (GameContainer gc, StateBasedGame sbg) throws SlickException {
        input = gc.getInput();

    }

    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException  {

        String mousePosition = "x: " + input.getMouseX() + " y: " + input.getMouseY();
        g.drawString(mousePosition, 10, 30);

        g.drawString("Press ENTER to play!", gc.getScreenWidth() / 4, gc.getScreenHeight() / 3);
    }

    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

    }

    public int getID () {
        return 0;
    }

}
