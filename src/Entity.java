import org.lwjgl.opengl.Display;
import org.newdawn.slick.*;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Transform;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ai on 2/18/14.
 */
public class Entity {
    public enum Direction {DOWN, LEFT, RIGHT, UP}

    private static int ID = 0; //Entity class id

    private boolean collisionDirection[]; //Collision values of every direction
    private boolean onMove, onMoveDown, onMoveUp, onMoveLeft, onMoveRight; //Moving states
    private boolean isAttacking, isColliding, isLive; //Action states

    private int positionX, positionY; //World coordinates
    private int screenX, screenY; //Screen coordinates
    private int entityID; // Unique entity ID

    //Entity stats
    private int moveSpeed, health, armor, attackDamage;
    private int level, experience;

    private int animationCount;
    private int currentAnimation;

    private Direction moveDirection; //enum for directions
    private Rectangle entityRec; //sprite sized rectangle

    //Lists for sprite animation
    private ArrayList<Image> entitySpriteImage;
    private ArrayList<Animation> entityAnimation;
    private ArrayList<SpriteSheet> entitySpriteSheet;


    private Gun gun;
    /*
    TODO
    Cant set up default values through default constructor
     */
    public Entity (int x, int y) {
        this();
        this.setPositionX(x);
        this.setPositionY(y);
    }

    public Entity () {
        //Setting up default stats
        this.setIsLive(true);
        this.setMoveSpeed(6);
        this.setHealth(1000);
        this.setArmor(10);
        this.setAttackDamage(100);
    }

    public void init (GameContainer gc) throws SlickException {
        this.entityID = ID;
        ID++;

        //Clearing collision states
        this.collisionDirection = new boolean[4];
        this.clearCollisionDirection();

        //Setting default animation
        this.setCurrentAnimation();

        //Initiating gun instance
        try {
            this.gun = new Gun();
            this.setIsAttacking(false);
        } catch (Exception e) {
            System.out.println("Problem initiating entity gun.\n");
            e.printStackTrace();
        }
        //Default move triggers
        this.setOnMoveUp(false);
        this.setOnMoveDown(false);
        this.setOnMoveRight(false);
        this.setOnMoveLeft(false);

        //Setting up sprite sheet images and animation
        try {
            /***********************************************************
             * Like moveDirection enum
             spriteImage[0] == DOWN
             spriteImage[1] == LEFT
             spriteImage[2] == RIGHT
             spriteImage[3] == UP
             **********************************************************/

            this.animationCount = 4;
            this.entityRec = new Rectangle(0, 0, 32, 48);
            this.entitySpriteImage = new ArrayList<Image>(this.animationCount);
            this.entitySpriteSheet = new ArrayList<SpriteSheet>(this.animationCount);
            this.entityAnimation = new ArrayList<Animation>(this.animationCount);

            //Loading sprite sheet file for individual sprite sheet
            for (int i = 0; i < this.animationCount; i++) {
                this.entitySpriteImage.add(i, new Image("res/tiles/charsheet2.png").getSubImage(
                        0,
                        (int) entityRec.getHeight() * i, 130, (int) entityRec.getHeight()
                ));
            }

            for (int i = 0; i < this.animationCount; i++) {
                //Setting up sprite sheets
                this.entitySpriteSheet.add(i, new SpriteSheet(this.entitySpriteImage.get(i), 32, 48));

                //Creating animation instance with sprite sheet object and duration between frames
                this.entityAnimation.add(i, new Animation(this.entitySpriteSheet.get(i), 200));
                this.entityAnimation.get(i).stop();
            }

            //Default animation
            this.entityAnimation.get(this.getCurrentAnimation()).stop();


        } catch (SlickException e) {
            e.printStackTrace();
        }
    }

    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException  {
        this.gun.render(gc, sbg, g);
        this.entityAnimation.get(this.getCurrentAnimation()).
                draw(this.screenX, this.screenY);

    }

    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        if (this.getHealth() <= 0)
            this.die();
        this.updateOnScreenPosition();
        this.checkMoveStates(delta);

        entityAnimation.get(this.getCurrentAnimation()).update(delta);
        this.gun.update(this.getCenterPosition(), this.getOnMove(), this.getMoveSpeed());

    }

    private void checkMoveStates(int delta) {
        if (this.getOnMoveUp()) {
            if (!this.isAttacking())
                this.setCurrentAnimation(moveDirection.UP.ordinal());
            this.entityAnimation.get(this.getCurrentAnimation()).start();
            this.moveUp(delta);
        }
        if (this.getOnMoveDown()) {
            if (!this.isAttacking())
                this.setCurrentAnimation(moveDirection.DOWN.ordinal());
            this.entityAnimation.get(this.getCurrentAnimation()).start();
            this.moveDown(delta);
        }
        if (this.getOnMoveRight()) {
            if (!this.isAttacking())
                this.setCurrentAnimation(moveDirection.RIGHT.ordinal());
            this.entityAnimation.get(this.getCurrentAnimation()).start();
            this.moveRight(delta);
        }
        if (this.getOnMoveLeft()) {
            if (!this.isAttacking())
                this.setCurrentAnimation(moveDirection.LEFT.ordinal());
            this.entityAnimation.get(this.getCurrentAnimation()).start();
            this.moveLeft(delta);
        }
        if (!this.getOnMoveUp() && !this.getOnMoveDown() && !this.getOnMoveRight() && !this.getOnMoveLeft()) {
            this.entityAnimation.get(this.getCurrentAnimation()).stop();
            this.setOnMove(false);

        }
    }

    private void updateOnScreenPosition() {
        this.screenX = this.getPositionX() - Camera.getCameraObj().getPositionX();
        this.screenY = this.getPositionY() - Camera.getCameraObj().getPositionY();
    }

    private void die() {
        this.setIsLive(false);
    }
    //Gets a notification about collisions from collision class
    public void notify(Direction direction, boolean collision) {
        this.setCollisionDirection(direction, collision);
        this.setColliding(collision);
    }

    //Gets notification about bullet hit
    public void notify (Bullet b, Entity attacker) {
        int trueDamage = attacker.getAttackDamage() - this.getArmor();
        if (trueDamage < 0) trueDamage = 0;

        this.setHealth(this.getHealth() - trueDamage);
        attacker.getGun().destroyBullet(b);
    }


    public int moveUp(int delta){
        int mov = 0;
        if (!this.getCollisionDirection(Entity.Direction.UP)) {
            if (this.getPositionY() > 0) {
                int speed = (int)((float)this.getMoveSpeed() * (float)delta * 0.02f);
                this.setPositionY(this.getPositionY() - speed);
                mov = 1;
            }
        }
        else
            this.setOnMoveUp(false);
        return mov;
    };
    public int moveDown(int delta){
        int mov = 0;
        if (!this.getCollisionDirection(Entity.Direction.DOWN)) {
            int speed = (int)((float)this.getMoveSpeed() * (float)delta * 0.02f);
            this.setPositionY(this.getPositionY() + speed);
            mov = 1;
        }
        else
            this.setOnMoveDown(false);
        return mov;
    };
    public int moveLeft(int delta){
        int mov = 0;
        if (!this.getCollisionDirection(Entity.Direction.LEFT)) {
            if (this.getPositionX() > 0) {
                int speed = (int)((float)this.getMoveSpeed() * (float)delta * 0.02f);
                this.setPositionX(this.getPositionX() - speed);
                mov = 1;
            }
        }
        else
            this.setOnMoveLeft(false);
        return mov;
    };
    public int moveRight(int delta){
        int mov = 0;
        if (!this.getCollisionDirection(Entity.Direction.RIGHT)) {
            int speed = (int)((float)this.getMoveSpeed() * (float)delta * 0.02f);
            this.setPositionX(this.getPositionX() + speed);
            mov = 1;
        }
        else
            this.setOnMoveRight(false);

        return mov;
    };


    public void setPositionX (int val) { this.positionX = val; }
    public void setPositionY (int val) { this.positionY = val; }
    public void setOnScreenX (int val) { this.screenX = val; }
    public void setOnScreenY (int val) { this.screenY = val; }
    public void setOnMoveDown (boolean val) {
        if (val) this.setOnMove(true);
        this.onMoveDown = val;
    }
    public void setOnMoveUp (boolean val) {
        if (val) this.setOnMove(true);
        this.onMoveUp = val;
    }
    public void setOnMoveRight (boolean val) {
        if (val) this.setOnMove(true);
        this.onMoveRight = val;
    }
    public void setOnMoveLeft (boolean val) {
        if (val) this.setOnMove(true);
        this.onMoveLeft = val;
    }

    public void setAttackDirection (Vector2f dir) {
        this.gun.setAttackDirection(dir);
    }
    public void setIsAttacking(boolean val) {
        this.gun.setShooting(val);
        this.isAttacking = val;
    }
    public void setIsLive(boolean val) {
        this.isLive = val;
    }

    public void syncSpeed(int delta) {
        if ((this.getMoveSpeed() * delta) > 0)
            this.setMoveSpeed(this.getMoveSpeed() * delta);
    }
    public void setMoveSpeed(int val) {
        if (val >= 0)
            this.moveSpeed = val;
    }
    public void setHealth(int val) {
        if (val > 0) this.health = val;
        else this.health = 0;
    }
    public void setArmor(int val) {
        if (val > 0) this.armor = val;
    }
    public void setAttackDamage(int val) {
        if (val > 0) this.attackDamage = val;
    }
    public void setOnMove (boolean val) {
        this.onMove = val;
    }

    public void setColliding (boolean val) {
        this.isColliding = val;
    }
    public void setCollisionDirection(Direction direction, boolean val) {
        this.collisionDirection[direction.ordinal()] = val;
    }

    /************* OVERLOADING ex. *************/
    public void setCurrentAnimation () {
        this.currentAnimation = 0;
    }
    public void setCurrentAnimation (int val) {
        if ((val < this.animationCount) && (val >= 0)) {
            this.currentAnimation = val;
        }
    }

    public void clearCollisionDirection() {
        this.collisionDirection[0] = false;
        this.collisionDirection[1] = false;
        this.collisionDirection[2] = false;
        this.collisionDirection[3] = false;

    }


    public boolean getCollisionDirection(Direction direction) { return this.collisionDirection[direction.ordinal()]; }
    public boolean isColliding() { return this.isColliding; }
    public boolean isReloading() { return gun.isReloading(); }
    public boolean isAttacking() { return this.isAttacking; }
    public boolean isLive() { return this.isLive; }

    public boolean getOnMoveDown () { return this.onMoveDown; }
    public boolean getOnMoveUp () { return this.onMoveUp; }
    public boolean getOnMoveRight () { return this.onMoveRight; }
    public boolean getOnMoveLeft () { return this.onMoveLeft; }
    public boolean getOnMove () { return this.onMove; }

    public int getID() { return this.entityID; }
    public int getMoveSpeed() { return this.moveSpeed; }
    public int getHealth() { return this.health; }
    public int getArmor() { return this.armor; }
    public int getAttackDamage() { return this.attackDamage; }

    public int getCurrentAnimation () { return this.currentAnimation; }
    public int getGunClipSize() { return gun.getClipSize(); }
    public int getGunBulletCount() { return gun.getBulletCount(); }

    public Gun getGun() { return this.gun; };
    public LinkedList<Bullet> getActiveBullets() { return this.gun.getActiveBullets(); }
    public Vector2f getCenterPosition() { return new Vector2f(this.getPositionX() + 161, this.getPositionY() + 240); }
    public Vector2f getPosition() { return new Vector2f(this.getPositionX(), this.getPositionY()); }
    public Rectangle getEntityRec() { return new Rectangle(this.getPositionX(), this.getPositionY(), this.entityRec.getWidth(), this.entityRec.getHeight()); }
    public Rectangle getCollisionRec() {
        return new Rectangle(
                this.getPositionX(),
                this.getPositionY(),
                this.entityRec.getWidth(),
                this.entityRec.getHeight());
    }

    public int getOnScreenX () { return this.screenX; }
    public int getOnScreenY () { return this.screenY; }
    public int getPositionX () { return this.positionX; }
    public int getPositionY () { return this.positionY; }

//    @Override
//    public String toString() {
//        String entity = "Entity object \n" +
//                "Entity position  x=" + this.getPositionX() + " y=" + this.getPositionY() +
//                "\n Number of animations: " + this.entityAnimation.size() +
//                "\nAttacking: " + this.isAttacking() +
//                "\nColliding: " + this.isColliding() +
//                "\nMoving: " + this.getOnMove();
//
//        return entity;
//    }
}