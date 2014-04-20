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
    private boolean onMove;
    private boolean boundX = false;
    private boolean boundY = false;

    public Camera(int state) {
        super(state);
        this.cameraX = 0;
        this.cameraY = 0;
        this.onMove = false;
    }

    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        this.onMove = false;
    }
    public void moveUp(int speed){
        this.onMove = true;
        if (!this.boundY)
            if (this.cameraY > 0)
                this.cameraY -= speed;

    };
    public void moveDown(int speed){
        this.onMove = true;
        if (!this.boundY)
            this.cameraY += speed;
    };
    public void moveLeft(int speed){
        this.onMove = true;
        if (!this.boundX)
            if (this.cameraX > 0)
                this.cameraX -= speed;
            else
                this.cameraX = 0;
    };
    public void moveRight(int speed){
        this.onMove = true;
        if (!this.boundX)
            this.cameraX += speed;
    };
    public int getPositionX () {
        return this.cameraX;
    }
    public int getPositionY () {
        return this.cameraY;
    }
    public boolean getOnMove() { return this.onMove; }
    public void setPositionX (int val) {
        this.cameraX = val;
    }
    public void setPositionY (int val) {
        this.cameraY = val;
    }
    public void setBoundX(boolean val) { this.boundX = val;}
    public void setBoundY(boolean val) { this.boundY = val;}
    @Override
    public String toString() {
        String camera = "Camera object \n" +
        "Camera position  x=" + this.getPositionX() + " y=" + this.getPositionY();

        return camera;
    }
}
