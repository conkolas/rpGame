/**
 * Play State Class
 * Created by ai on 2/17/14.
 *
 * Responsible for rendering map and game entities,
 * catching keyboard inputs in the game play and setting camera states
 */
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.*;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;


public class Play extends BasicGameState {

    private Input input; //For catching keyboard events
    private MouseListener mouse;
    private static Player player; //Main player instance
    private Map map;
    private static Camera camera;

    public Play (int state) {

    }

    public void init (GameContainer gc, StateBasedGame sbg) throws SlickException {
        this.input = new Input(0);
        this.input.addMouseListener(mouse);
        try {
            //Initializing map instance
            this.map = new Map(3);
            this.map.init(gc, sbg);

            //Initializing camera
            this.camera = new Camera(0);

            //Creating player entity
            this.player = new Player();
            this.player.init(gc, sbg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /******************* Play view *******************/
    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        this.map.render(gc, sbg, g);
        this.player.render(gc, sbg, g);

    }

    /******************* Play logic *******************/
    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {

        this.player.update(gc, sbg, delta);
        this.camera.update(gc, sbg, delta);

    }


    /******************* Key/Mouse events *******************/

   public void mouseMoved(int oldx, int oldy, int newx, int newy) {
       this.player.setPlayerDirection(newx, newy);
   }

    /*
    Catching a keyboard input when key is pressed
     */
    public void keyPressed(int key, char c) {

        if ((key == Input.KEY_UP) || (key == Input.KEY_W)) {
            this.player.setOnMoveUp(true);
            this.camera.setOnMoveUp(true);
        }

        if ((key == Input.KEY_DOWN) || (key == Input.KEY_S)) {
            this.player.setOnMoveDown(true);
            this.camera.setOnMoveDown(true);
        }

        if ((key == Input.KEY_LEFT) || (key == Input.KEY_A)){
            this.player.setOnMoveLeft(true);
            this.camera.setOnMoveLeft(true);
        }

        if ((key == Input.KEY_RIGHT) || (key == Input.KEY_D)){
            this.player.setOnMoveRight(true);
            this.camera.setOnMoveRight(true);
        }
    }


    /*
     Catching a keyboard input when key is released
    */
    public void keyReleased(int key, char c) {
        if ((key == Input.KEY_UP) || (key == Input.KEY_W)) {
            this.player.setOnMoveUp(false);
            this.camera.setOnMoveUp(false);
        }

        if ((key == Input.KEY_DOWN) || (key == Input.KEY_S)) {
            this.player.setOnMoveDown(false);
            this.camera.setOnMoveDown(false);
        }

        if ((key == Input.KEY_LEFT) || (key == Input.KEY_A)){
            this.player.setOnMoveLeft(false);
            this.camera.setOnMoveLeft(false);
        }

        if ((key == Input.KEY_RIGHT) || (key == Input.KEY_D)){
            this.player.setOnMoveRight(false);
            this.camera.setOnMoveRight(false);
        }
    }



    /******************* Set/Get *******************/

    public static Entity getPlayerObj() {
        return player;
    }
    public static Camera getCameraObj() {
        return camera;
    }
    public int getID () {
        return 1;
    }


}