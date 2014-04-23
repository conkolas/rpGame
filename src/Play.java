/**
 * Play State Class
 * Created by ai on 2/17/14.
 *
 * Responsible for rendering map and game entities,
 * catching keyboard inputs in the game play and setting camera states
 */
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;


public class Play extends BasicGameState {

    private Input input; //For catching keyboard events
    private MouseListener mouse;
    private Collision collision;
    private ArrayList<Entity> entityList; //All entities in the game
    private Enemy enemy;
    private static Player player; //Main player instance
    private static Map map;
    private static Camera camera;

    public Play (int state) {

    }

    public void init (GameContainer gc, StateBasedGame sbg) throws SlickException {
        this.input = new Input(0);
        this.input.addMouseListener(mouse);
        Keyboard.enableRepeatEvents(true);

        gc.getGraphics().setAntiAlias(true);

        try {
            //Initializing map instance
            this.map = new Map(5);
            this.collision = new Collision(this.map.getCollisionMap(), this.map.getWidth(), this.map.getHeight());
        } catch (Exception e) {
            System.out.println("Map creation exception");
            e.printStackTrace();
        }

        try {
            //Initializing camera
            this.camera = new Camera(0);

        } catch (Exception e) {
            System.out.println("Camera creation exception");
            e.printStackTrace();
        }

        try {
            this.entityList = new ArrayList();
            //Creating player entity
            this.player = new Player();

            this.entityList.add(this.player);
            this.enemy = new Enemy(180, 200);
            this.entityList.add(enemy);

            Enemy enemy2 = new Enemy(180, 400);
            this.entityList.add(enemy2);
            for (Entity entity : this.entityList)
                entity.init(gc);

            /***** INTERFACE EX ******/
            MovableEnemy en = this.enemy;
            en.moveRight(true);
            /*************************/

        } catch (Exception e) {
            System.out.println("Player creation exception");
            e.printStackTrace();
        }


    }

    /******************* Play view *******************/
    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        this.map.render(gc, Camera.getCameraObj());
        for (Entity entity : this.entityList)
            entity.render(gc, sbg, g);
        this.collision.render(gc, sbg, g);
    }

    /******************* Play logic *******************/
    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        for (Iterator<Entity> it = this.entityList.iterator(); it.hasNext();) {
            Entity e = it.next();
            if (!e.isLive())
                it.remove();
        }
        checkPressedKeys();

        //Checking entities collision with map objects
        this.collision.entityMapCollision(this.entityList);
        for (Entity entity : this.entityList) {
            this.collision.bulletCollision(entity, this.entityList);
            entity.update(gc, sbg, delta);
        }

        this.camera.update(gc, sbg, delta);
    }


    /******************* Key/Mouse events *******************/
    public void mouseDragged(int oldx, int oldy, int newx, int newy) {
        this.player.setMouseDragged(true);
        this.player.setAttackDirection(new Vector2f(newx, newy));
        this.player.setPlayerDirection(newx, newy);
    }
    public void mouseReleased(int button, int x, int y) {
        this.player.setIsAttacking(false);
        this.player.setMouseDragged(false);
    }
    public void mousePressed(int button, int x, int y) {
        this.player.setIsAttacking(true);
        this.player.setPlayerDirection(x, y);
        this.player.setAttackDirection(new Vector2f(x, y));
    }

    /*
    Catching a keyboard input when key is pressed
     */
    private void checkPressedKeys() {
        if (Mouse.isGrabbed()) {
            mouseDragged(0,0, Mouse.getEventX(), Mouse.getEventY());
        }
        if (Mouse.isButtonDown(0)) {
            this.player.setIsAttacking(true);
        } else this.player.setIsAttacking(false);
        if (Keyboard.isKeyDown(Input.KEY_UP) || Keyboard.isKeyDown(Input.KEY_W)) {
            this.player.setOnMoveUp(true);
        } else this.player.setOnMoveUp(false);

        if (Keyboard.isKeyDown(Input.KEY_DOWN) || Keyboard.isKeyDown(Input.KEY_S)) {
            this.player.setOnMoveDown(true);
        } else this.player.setOnMoveDown(false);


        if (Keyboard.isKeyDown(Input.KEY_LEFT) || Keyboard.isKeyDown(Input.KEY_A)){
            this.player.setOnMoveLeft(true);
        } else this.player.setOnMoveLeft(false);

        if (Keyboard.isKeyDown(Input.KEY_RIGHT) || Keyboard.isKeyDown(Input.KEY_D)){
            this.player.setOnMoveRight(true);
        } else this.player.setOnMoveRight(false);

    }
//    public void keyPressed(int key, char c) {
//        if ((key == Input.KEY_UP) || (key == Input.KEY_W)) {
//            this.player.setOnMoveUp(true);
//        }
//
//        if ((key == Input.KEY_DOWN) || (key == Input.KEY_S)) {
//            this.player.setOnMoveDown(true);
//        }
//
//        if ((key == Input.KEY_LEFT) || (key == Input.KEY_A)){
//            this.player.setOnMoveLeft(true);
//        }
//
//        if ((key == Input.KEY_RIGHT) || (key == Input.KEY_D)){
//            this.player.setOnMoveRight(true);
//        }
//    }


    /*
     Catching a keyboard input when key is released
    */
//    public void keyReleased(int key, char c) {
//        if ((key == Input.KEY_UP) || (key == Input.KEY_W)) {
//            this.player.setOnMoveUp(false);
//        }
//
//        if ((key == Input.KEY_DOWN) || (key == Input.KEY_S)) {
//            this.player.setOnMoveDown(false);
//        }
//
//        if ((key == Input.KEY_LEFT) || (key == Input.KEY_A)){
//            this.player.setOnMoveLeft(false);
//        }
//
//        if ((key == Input.KEY_RIGHT) || (key == Input.KEY_D)){
//            this.player.setOnMoveRight(false);
//        }
//    }



    /******************* Set/Get *******************/
    public static Map getCurrentMap() { return map; }
    public static Camera getCameraObj() {
        return camera;
    }
    public int getID () {
        return 1;
    }

    @Override
    public String toString() {
        String play = "Play object \n" + "Entities in action: " + this.entityList.size();
        return play;
    }
}