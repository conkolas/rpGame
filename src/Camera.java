import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

/**
 * Created by ai on 2/18/14.
 *
 *
 */
public class Camera extends Play {
    private int cameraX, cameraY;
    private boolean onMoveDown, onMoveUp, onMoveLeft, onMoveRight;
    private boolean boundX = false;
    private boolean boundY = false;

    public Camera(int state) {
        super(state);
        this.cameraX = 0;
        this.cameraY = 0;
        this.onMoveUp = false;
        this.onMoveDown = false;
        this.onMoveRight = false;
        this.onMoveLeft = false;
    }

    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if (this.onMoveUp) this.moveUp();
        if (this.onMoveDown) this.moveDown();
        if (this.onMoveRight) this.moveRight();
        if (this.onMoveLeft) this.moveLeft();
    }
    public void moveUp(){
        if (!this.boundY)
            if (this.cameraY > 0)
                this.cameraY--;

    };
    public void moveDown(){
        if (!this.boundY)
            this.cameraY++;
    };
    public void moveLeft(){
        if (!this.boundX)
            if (this.cameraX > 0)
                this.cameraX--;
            else
                this.cameraX = 0;
    };
    public void moveRight(){
        if (!this.boundX)
            this.cameraX++;
    };
    public int getPositionX () {
        return this.cameraX;
    }
    public int getPositionY () {
        return this.cameraY;
    }
    public void setPositionX (int val) {
        this.cameraX = val;
    }
    public void setPositionY (int val) {
        this.cameraY = val;
    }
    public void setBoundX(boolean val) { this.boundX = val;}
    public void setBoundY(boolean val) { this.boundY = val;}
    public void setOnMoveDown (boolean val) { this.onMoveDown = val; }
    public void setOnMoveUp (boolean val) { this.onMoveUp = val; }
    public void setOnMoveRight (boolean val) { this.onMoveRight = val; }
    public void setOnMoveLeft (boolean val) { this.onMoveLeft = val; }
}
