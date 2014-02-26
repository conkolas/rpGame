import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by ai on 2/19/14.
 */

public class Player extends Entity {
    public enum Direction {DOWN, LEFT, RIGHT, UP}


    private boolean onMoveDown, onMoveUp, onMoveLeft, onMoveRight;

    private int animationCount;
    private int currentAnimation;

    private Direction moveDirection;
    private Image[] playerSpriteImage;
    private Animation[] playerAnimation;
    private SpriteSheet[] playerSpriteSheet;


    private int onScreenX, onScreenY, offsetX, offsetY;
    private Vector2f playerCenter;
    private Polygon topScreen, bottomScreen, leftScreen, rightScreen;
    private static int startOnScreenX, startOnScreenY;


    public Player() {
    }

    public void init (GameContainer gc, StateBasedGame sbg) throws SlickException {
        super.setPositionX(gc.getWidth()/2);
        super.setPositionY(gc.getHeight()/2);

        this.offsetX = 0;
        this.offsetY = 0;
        this.startOnScreenX = this.onScreenX = gc.getWidth()/2;
        this.startOnScreenY = this.onScreenY = gc.getHeight()/2;
        try {
            /***********************************************************
             * Like moveDirection enum
            spriteImage[0] == DOWN
            spriteImage[1] == LEFT
            spriteImage[2] == RIGHT
            spriteImage[3] == UP
             **********************************************************/

            this.animationCount = 4;
            this.playerSpriteImage = new Image[this.animationCount];
            this.playerSpriteSheet = new SpriteSheet[this.animationCount];
            this.playerAnimation = new Animation[this.animationCount];

            //Loading sprite sheet file for individual sprite sheet
            for (int i = 0; i < this.animationCount; i++) {
                //getSubImage(x, y, width, height)
                this.playerSpriteImage[i] = new Image("res/tiles/charsheet2.png").getSubImage(0, 48 * i, 130, 48);
            }

            for (int i = 0; i < this.animationCount; i++) {
                //Setting up sprite sheets
                this.playerSpriteSheet[i] = new SpriteSheet(this.playerSpriteImage[i], 32, 48);

                //Creating animation instance with sprite sheet object and duration between frames
                this.playerAnimation[i] = new Animation(this.playerSpriteSheet[i], 600);
                this.playerAnimation[i].stop();
            }

            //Default move triggers
            this.setOnMoveUp(false);
            this.setOnMoveDown(false);
            this.setOnMoveRight(false);
            this.setOnMoveLeft(false);

            //Default animation
            this.setPlayerDirection(0, 0);
            this.playerAnimation[this.getCurrentAnimation()].stop();

        } catch (SlickException e) {
            e.printStackTrace();
        }

    }

    /******************* Player on screen *******************/
    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException  {
        //Player position
        String position = "x: " + super.getPositionX() + " y: " + super.getPositionY();
        g.drawString(position, 10, 30);

        //Player
        this.playerAnimation[this.getCurrentAnimation()].draw(this.getOnScreenX() - this.getOffsetX(),
                this.getOnScreenY() - this.getOffsetY());
    }


    /******************* Players logic *******************/
    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        super.update(gc, sbg, delta);
        //Checking for movement and changing sprite sheet animation interval
        if (this.getOnMoveUp()) {
            this.moveUp();
            this.playerAnimation[this.getCurrentAnimation()].start();
        }
        if (this.getOnMoveDown()) {
            this.moveDown();
            this.playerAnimation[this.getCurrentAnimation()].start();
        }
        if (this.getOnMoveRight()) {
            this.moveRight();
            this.playerAnimation[this.getCurrentAnimation()].start();
        }
        if (this.getOnMoveLeft()) {
            this.moveLeft();
            this.playerAnimation[this.getCurrentAnimation()].start();
        }
        if (!onMoveUp && !onMoveDown && !onMoveRight && !onMoveLeft) {
            this.playerAnimation[this.getCurrentAnimation()].stop();

        }

        //Player center on the screen coordinates for making polygons
        this.playerCenter = new Vector2f(this.getOnScreenX() - this.getOffsetX() + 16,
                this.getOnScreenY() - this.getOffsetY() + 24);

        this.makeScreenPolygons(gc, this.playerCenter);

        playerAnimation[this.getCurrentAnimation()].update(delta);
    }

    /******************* Players moving logic *******************/
    //Overriding Entity class moving methods
    @Override
    public void moveUp(){
        //Checking map and camera bounds
        if (super.getPositionY() > 0) {
            if ((Camera.getCameraObj().getPositionY() <= 0) && (super.getPositionY() != 0)) {
                Camera.getCameraObj().setBoundY(true);
                this.offsetY++;
            }
            //this.playerAnimation.start();
            super.moveUp();
        }

    };

    @Override
    public void moveDown(){
        if (this.offsetY > 0)
            this.offsetY--;
        else
            Camera.getCameraObj().setBoundY(false);

        //this.playerAnimation.start();
        super.moveDown();
    };

    @Override
    public void moveLeft(){
        if (super.getPositionX() > 0) {
            if ((Camera.getCameraObj().getPositionX() <= 0) && (super.getPositionX() != 0)) {
                Camera.getCameraObj().setBoundX(true);
                this.offsetX++;
            }
            //this.playerAnimation.start();
            super.moveLeft();
        }
    };

    @Override
    public void moveRight(){
        if (this.offsetX > 0)
            this.offsetX--;
        else
            Camera.getCameraObj().setBoundX(false);

       // this.playerAnimation.start();
        super.moveRight();
    };

    //Divides screen in four triangles where top of triangle is player center
    public void makeScreenPolygons (GameContainer gc, Vector2f centerPoint) {
        this.topScreen = new Polygon();
        topScreen.addPoint(0,0);
        topScreen.addPoint(gc.getWidth(),0);
        topScreen.addPoint(centerPoint.getX(),
                centerPoint.getY()

        );

        this.leftScreen = new Polygon();
        leftScreen.addPoint(0, 0);
        leftScreen.addPoint(0, gc.getHeight());
        leftScreen.addPoint(centerPoint.getX(),
                centerPoint.getY()
        );

        this.rightScreen = new Polygon();
        rightScreen.addPoint(gc.getWidth(), 0);
        rightScreen.addPoint(gc.getWidth(), gc.getHeight());
        rightScreen.addPoint(centerPoint.getX(),
                centerPoint.getY()
        );

        this.bottomScreen = new Polygon();
        bottomScreen.addPoint(0, gc.getHeight());
        bottomScreen.addPoint(gc.getWidth(), gc.getHeight());
        bottomScreen.addPoint(centerPoint.getX(),
                centerPoint.getY()
        );
    }


    /******************* Set/Get *******************/
    public void setOnMoveDown (boolean val) {
        this.onMoveDown = val;

    }
    public void setOnMoveUp (boolean val) {
        this.onMoveUp = val;

    }
    public void setOnMoveRight (boolean val) {
        this.onMoveRight = val;

    }
    public void setOnMoveLeft (boolean val) {
        this.onMoveLeft = val;
    }

    public void setCurrentAnimation (int val) {
        if ((val < this.animationCount) && (val >= 0)) {
            this.currentAnimation = val;
        }
    }

    //Checking which screen side is pointed by mouse and changing direction
    public void setPlayerDirection(int x, int y) {

        if (this.topScreen.contains(x,y)) {
            this.setCurrentAnimation(moveDirection.UP.ordinal());
        }

        if (this.bottomScreen.contains(x,y)) {
            this.setCurrentAnimation(moveDirection.DOWN.ordinal());
        }

        if (this.leftScreen.contains(x,y)) {
            this.setCurrentAnimation(moveDirection.LEFT.ordinal());
        }

        if (this.rightScreen.contains(x,y)) {
            this.setCurrentAnimation(moveDirection.RIGHT.ordinal());
        }
    }
    public int getOnScreenX () { return this.onScreenX; }
    public int getOnScreenY () { return this.onScreenY; }
    public int getOffsetX () { return this.offsetX; }
    public int getOffsetY () { return this.offsetY; }

    public boolean getOnMoveDown () { return this.onMoveDown; }
    public boolean getOnMoveUp () { return this.onMoveUp; }
    public boolean getOnMoveRight () { return this.onMoveRight; }
    public boolean getOnMoveLeft () { return this.onMoveLeft; }
    public int getCurrentAnimation () { return this.currentAnimation; }

}
