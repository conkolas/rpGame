/**
 * Created by ai on 2/16/14.
 */

import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
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
        this.addState(new Menu(menuState));
        this.addState(new Play(playState));
    }

    /** Initializing states and entering menu **/
    public void initStatesList (GameContainer gc) throws SlickException {
        //Initializing states
        this.getState(menuState).init(gc, this);
        this.getState(playState).init(gc, this);

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
                this.enterState(playState);
                break;
            case Input.KEY_ESCAPE:
                gameContainer.exit();
            default:
                this.enterState(defaultState);
                break;
        }
    }
    /*********************************************/


    public static void main (String[] args) {

        //Creating game window
        AppGameContainer gameContainer;
        try {
            gameContainer = new AppGameContainer(new GameEngine(gamename));
            gameContainer.setDisplayMode(screenWidth, screenHeight, false);
            gameContainer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static Rectangle getScreenRect() {
        Rectangle screenRect = new Rectangle(0,0, screenWidth, screenHeight);
        return screenRect;
    }
}

