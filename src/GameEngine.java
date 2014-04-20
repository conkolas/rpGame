/**
 * Created by ai on 2/16/14.
 */

import org.lwjgl.input.Cursor;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.ImageData;
import org.newdawn.slick.state.*;

public class GameEngine extends StateBasedGame {

    /** Game states **/
    public static final String gamename = "RPG";
    public static final int screenHeight = 600;
    public static final int screenWidth = 800;
    public static final int defaultState = 1;
    public static final int menuState = 0;
    public static final int playState = 1;

    /** Service **/
    private Input input;
    private GameContainer gameContainer;

    /** Adding new states to the list **/

    public GameEngine (String gamename) {
        super(gamename);
        this.addState(new Menu(GameEngine.menuState));
        this.addState(new Play(GameEngine.playState));

    }

    /** Initializing states and entering menu **/
    public void initStatesList (GameContainer gc) throws SlickException {
        //Initializing states
        this.gameContainer = gc;
//        this.getState(GameEngine.menuState).init(gc, this);
//        this.getState(GameEngine.playState).init(gc, this);

        this.enterState(GameEngine.defaultState);

        input = gc.getInput();
    }

    /**
     * ***************************************
     * TODO: INPUT HANDLING CLASS!
     *****************************************
     */
    @Override
    public void keyPressed(int key, char c) {
        super.keyPressed(key, c);

        switch (key) {
            case Input.KEY_ENTER:
                this.getScreenRect();
                this.enterState(playState);
                break;
            case Input.KEY_ESCAPE:
                this.gameContainer.exit();
            default:
                this.enterState(defaultState);
                break;
        }
    }
    /*********************************************/


    public static void main (String args[]) {
        String gamename = "RPG v0";
        int screenHeight = 720;
        int screenWidth = 1200;
        //Creating game window
        try {
            AppGameContainer gameContainer = new AppGameContainer(new GameEngine(gamename));
            gameContainer.setDisplayMode(screenWidth, screenHeight, false);
            gameContainer.setTargetFrameRate(31);
            gameContainer.setVSync(true);
            gameContainer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Rectangle getScreenRect() {
        Rectangle screenRect = new Rectangle(0,0, screenWidth, screenHeight);
        return screenRect;
    }

    public String toString() {
        String engine = "Engine object \n" +
                "Windows size " + this.getScreenRect().getWidth() + "x" + this.getScreenRect().getHeight();

        return engine;
    }
}

