import org.lwjgl.Sys;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.*;

/**
 * Created by ai on 3/6/14.
 */
public class Gun extends Entity {
    private int ownerID;
    private boolean isShooting, isReloading; // Gun state vars
    private int gunSpeed, nextShoot; // Shooting rate vars
    private int reloadTime, reloading;
    private int clipSize, bulletCount;

    private Vector2f attackDirection;
    private LinkedList<Bullet> bullets;

    public Gun() {
        try {
            this.ownerID = super.getID();
            this.attackDirection = new Vector2f();
            this.setGunSpeed(5);
            this.setClipSize(30);
            this.setReloadTime(100);
            this.setIsReloading(false);

            this.bulletCount = 0;
            this.bullets = new LinkedList<Bullet>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*********** Creates new bullet instance ***********/
    public void shoot (Vector2f startPoint, boolean isMoving, float playerSpeed) {
        Bullet bullet = new Bullet(startPoint,
                this.getAttackDirection(), isMoving, playerSpeed, this.getOwnerID());
        this.bullets.add(bullet);

        this.bulletCount++;
    }

    /*********** Gun mechanics ***********/
    /*
     * Checks for clip reload
     * Controls shooting speed
     * Calls bullets update method
     */
    public void update (Vector2f playerCenterPosition, boolean isMoving, float playerSpeed, int delta) throws SlickException {
        //Checking bullets in clip
        //If there is enought bullets, then shoot
        //if not - reload
        if (this.bulletCount == this.getClipSize()) {
            //If reloaded, then reset counters
            if (this.getReloadTime() <= this.reloading) {
                this.reloading = 0;
                this.bulletCount = 0;
                this.bullets.clear(); //Clearing bullet list
                Bullet.clearID();
                this.setIsReloading(false);
            }
            else {
                this.reloading++;
                this.setIsReloading(true);
            }
        } else {
            if (this.isShooting()) {
                //Shooting delay
                if (this.nextShoot > this.getGunSpeed()) {
                    this.shoot(playerCenterPosition, isMoving, playerSpeed);
                    this.nextShoot = 0;
                } else
                    this.nextShoot++;
            }
        }

        for (Bullet bullet : bullets)
            bullet.update(delta);

    }
    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
        for (Bullet bullet : bullets)
            bullet.render(gc, sbg, g);
    }

    public void setGunSpeed(int val) {
        if (val > 0)
            this.gunSpeed = val;
    }
    public void setClipSize(int val) {
        if (val > 0)
            this.clipSize = val;
    }
    public void setReloadTime(int val) {
        if (val > 0)
            this.reloadTime = val;
    }
    public void setShooting(boolean val) {
        this.isShooting = val;
    }
    public void setIsReloading(boolean val) {
        this.isReloading = val;
    }

    public void setAttackDirection (Vector2f dir) {
        if (dir != null)
            this.attackDirection = dir;
    }

    public void destroyBullet(Bullet bullet) {
        try {
            Iterator<Bullet> it = this.bullets.iterator();
            if (!this.bullets.isEmpty())
                while (it.hasNext()) {
                    if(it.next().equals(bullet)) {
                        it.remove();
                    }
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean isShooting() { return this.isShooting; }
    public boolean isReloading() { return this.isReloading; }
    public LinkedList<Bullet> getActiveBullets() { return this.bullets; }
    public int getOwnerID() { return this.ownerID; }
    public int getClipSize() { return this.clipSize; }
    public int getGunSpeed() { return this.gunSpeed; }
    public int getReloadTime() { return this.reloadTime; }
    public int getBulletCount() { return this.bulletCount; }

    public Vector2f getAttackDirection () { return this.attackDirection; }

    public String toString() {
        String gun = "Gun object \n" +
                "Clip size " + this.getClipSize() +
                "\nRate " + this.getGunSpeed() + "bullets/s" +
                "\nReload time " + this.getReloadTime();

        return gun;
    }
}
