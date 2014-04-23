import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.particles.ConfigurableEmitter;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Random;

/**
 *Bullet class
 * Calculates position an
 */
public class Bullet {

    private static int ID;
    private int bulletID, gunID;
    private Image bulletImage;
    private Vector2f bulletCoord, startPoint, endPoint;
    private Rectangle bulletRec;
    private float speed, angle, recoil;
    private boolean destroy;

    public Bullet(Vector2f start, Vector2f end, boolean isMoving, float playerSpeed, int gunID) {
        try {
            this.gunID = gunID;
            this.bulletID = ID;
            ID++;

            this.bulletImage = new Image("res/bullet.png");

            this.speed = 25f;

            this.startPoint = new Vector2f(start);
            this.startPoint.x += 16;
            this.startPoint.y += 24; //Sprite center

            this.endPoint = new Vector2f(end);
            this.endPoint.x += Camera.getCameraObj().getPositionX();
            this.endPoint.y += Camera.getCameraObj().getPositionY();

            this.bulletCoord = this.startPoint;
            this.bulletRec = new Rectangle(
                    this.bulletCoord.getX(),
                    this.bulletCoord.getY(),
                    this.bulletImage.getWidth(),
                    this.bulletImage.getHeight());

            float deltaX = this.endPoint.getX() - this.startPoint.getX();
            float deltaY = this.endPoint.getY() - this.startPoint.getY();

            this.angle = (float)Math.atan2(deltaY, deltaX) * 180/3.14f;

            this.recoil = 0;
            if (isMoving) {
                this.recoil = (float)Math.random() * playerSpeed;
            }

            if ((Math.random() * 1) > 0.5)
                this.angle += this.recoil;
            else
                this.angle -= this.recoil;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        if (!destroy)
            g.drawImage(
                this.bulletImage,
                this.getBulletCoord().getX() - Camera.getCameraObj().getPositionX(),
                this.getBulletCoord().getY() - Camera.getCameraObj().getPositionY());
    }

    public void update (int delta) throws SlickException {
        if (!destroy) {
            int speed = (int)((float)this.speed * (float)delta * 0.02f);
            float x = this.getBulletCoord().getX() + (speed * ((float)Math.cos(this.angle * 3.14/180)));
            float y = this.getBulletCoord().getY() + (speed * ((float)Math.sin(this.angle * 3.14/180)));
            this.getBulletCoord().set(x, y);
            this.getBulletRec().setX(x);
            this.getBulletRec().setY(y);
        }
    }

    public void setSpeed(float val) {
        if (val > 0)
            this.speed = val;
    }
    public static void clearID() { ID = 0; }
    public void setBulletCoord(Vector2f newCoord) {
        this.bulletCoord = newCoord;
    }

    public int getBulletID() { return this.bulletID; }
    public int getGunID() { return this.gunID; }
    public float getSpeed() { return this.speed; }
    public Rectangle getBulletRec() { return this.bulletRec; }
    public Vector2f getBulletCoord() { return this.bulletCoord; }

    @Override
    public String toString() {
        String bullet = "Bullet object \n" + "Bullet speed: " + this.getSpeed();
        return bullet;
    }
}
