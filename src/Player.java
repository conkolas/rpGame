import org.newdawn.slick.*;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;

/**
 * Created by ai on 2/19/14.
 */

public class Player extends Entity {
    public enum Direction {DOWN, LEFT, RIGHT, UP}
    private boolean mouseDragged;

    private Direction moveDirection;

    private int onScreenX, onScreenY, offsetX, offsetY;
    private Vector2f playerCenter;
    private Polygon topScreen, bottomScreen, leftScreen, rightScreen;
    private static int startOnScreenX, startOnScreenY;


    public Player() {
        this.playerCenter = new Vector2f(0, 0);
    }

    @Override
    public void init (GameContainer gc) throws SlickException {
        super.init(gc);
        super.setPositionX(gc.getWidth() / 2);
        super.setPositionY(gc.getHeight()/2);

        this.offsetX = 0;
        this.offsetY = 0;
        this.startOnScreenX = this.onScreenX = gc.getWidth()/2;
        this.startOnScreenY = this.onScreenY = gc.getHeight()/2;
        try {
            //Player default direction
            this.setMouseDragged(false);
            this.makeScreenPolygons(gc, this.playerCenter);
            this.setPlayerDirection(1, 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /******************* Player on screen *******************/
    @Override
    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException  {
        super.render(gc, sbg, g);
        //Player position string
        String position = "x: " + super.getPositionX() + " y: " + super.getPositionY();
        g.drawString(position, 10, 30);

        if (super.isReloading()) {
            g.drawString("Reloading", 10, 50);
        } else {
            String s = "Bullets: " + (super.getGunClipSize() - super.getGunBulletCount());
            g.drawString(s, 10, 50);
        }
        //Player position on screen
        int onScreenX = this.getOnScreenX() - this.getOffsetX();
        int onScreenY = this.getOnScreenY() - this.getOffsetY();
        super.setOnScreenX(onScreenX);
        super.setOnScreenY(onScreenY);
    }


    /******************* Players logic *******************/
    @Override
    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        super.update(gc, sbg, delta);

        if (super.getOnMoveUp()) {
            if (!this.isMouseDragged())
                super.setCurrentAnimation(moveDirection.UP.ordinal());
        }
        if (super.getOnMoveDown()) {
            if (!this.isMouseDragged())
                super.setCurrentAnimation(moveDirection.DOWN.ordinal());
        }
        if (super.getOnMoveRight()) {
            if (!this.isMouseDragged())
                super.setCurrentAnimation(moveDirection.RIGHT.ordinal());
        }
        if (super.getOnMoveLeft()) {
            if (!this.isMouseDragged())
                super.setCurrentAnimation(moveDirection.LEFT.ordinal());
        }


        //Player center on the screen coordinates for making polygons
        int playerX = this.getOnScreenX() - this.getOffsetX();
        int playerY = this.getOnScreenY() - this.getOffsetY();
        this.playerCenter = new Vector2f(
                playerX + super.getEntityRec().getWidth() / 2,
                playerY + super.getEntityRec().getHeight() / 2
        );

        this.makeScreenPolygons(gc, this.playerCenter);

    }

    /******************* Players moving logic *******************/
    //Overriding Entity class moving methods
    @Override
    public int moveUp(int delta){
        if (super.moveUp(delta) == 1) {
            //Checking map and camera bounds
            int speed = (int)((float)super.getMoveSpeed() * (float)delta * 0.02f);
            if ((Camera.getCameraObj().getPositionY() <= 0) && (super.getPositionY() != 0)) {
                Camera.getCameraObj().setBoundY(true);
                this.offsetY += speed;
            }
            Camera.getCameraObj().moveUp(speed);
        }
        return 1;
    };

    @Override
    public int moveDown(int delta){
        if (super.moveDown(delta) == 1) {
            int speed = (int)((float)super.getMoveSpeed() * (float)delta * 0.02f);
            if (this.offsetY > 0)
                this.offsetY -= speed;
            else
                Camera.getCameraObj().setBoundY(false);

            Camera.getCameraObj().moveDown(speed);
        }
        return 1;
    };

    @Override
    public int moveLeft(int delta){
        if (super.moveLeft(delta) == 1) {
            int speed = (int)((float)super.getMoveSpeed() * (float)delta * 0.02f);
            if ((Camera.getCameraObj().getPositionX() <= 0) && (super.getPositionX() != 0)) {
                Camera.getCameraObj().setBoundX(true);
                this.offsetX += speed;
            }
            Camera.getCameraObj().moveLeft(speed);
        }
        return 1;
    };

    @Override
    public int moveRight(int delta){
        if (super.moveRight(delta) == 1) {
            int speed = (int)((float)super.getMoveSpeed() * (float)delta * 0.02f);
            if (this.offsetX > 0)
                this.offsetX -= speed;
            else
                Camera.getCameraObj().setBoundX(false);

            Camera.getCameraObj().moveRight(speed);
        }
        return 1;
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

    public boolean isMouseDragged() { return this.mouseDragged; }
    public void setMouseDragged(boolean val) { this.mouseDragged = val; }

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

    public Vector2f getCenterPosition() {
        return new Vector2f(this.getPositionX(), this.getPositionY());
    }

    @Override
    public String toString() {
        String player = "Player object \n" + 
               "Player position  x=" + this.getPositionX() + " y=" + this.getPositionY();
        return player;
    }
}
