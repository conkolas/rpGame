import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by ai on 2/18/14.
 */
public class Entity {

    private int positionX, positionY;

    public Entity () {

    }
    public Entity (int x, int y) {
        this.setPositionX(x);
        this.setPositionY(y);
    }

    public void init (GameContainer gc, StateBasedGame sbg) throws SlickException {

    }

    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException  {
       //Player.getAnimation().draw(this.onScreenX - this.offsetX, this.onScreenY - this.offsetY);
    }

    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        //System.out.println(this.positionX + "  " + this.positionY);
    }

    public void moveUp(){
        this.positionY--;
    };
    public void moveDown(){
        this.positionY++;
    };
    public void moveLeft(){
        this.positionX--;
    };
    public void moveRight(){
        this.positionX++;
    };


    public int getPositionX () {
        return this.positionX;
    }
    public int getPositionY () {
        return this.positionY;
    }

    public void setPositionX (int val) {
        this.positionX = val;
    }
    public void setPositionY (int val) {
        this.positionY = val;
    }

}