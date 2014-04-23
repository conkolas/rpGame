import org.newdawn.slick.*;
import org.newdawn.slick.fills.GradientFill;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Shape;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;

import java.util.*;

/**
 * Created by ai on 3/24/14.
 */
public class Collision {

    private List<Rectangle> map, pot;
    private int mapWidth, mapHeight, tileWidth, tileHeight;
    private boolean mapCollision;
    private Line top, bottom, left, right;

    public Collision(List<Rectangle> _map, int mapWidth, int mapHeight) {
        this.map = new ArrayList<Rectangle>();
        this.pot = new ArrayList<Rectangle>();
        this.map = _map;
        this.mapCollision = false;

        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;

        int i = 0;
        while (this.map.get(i) == null) {
            i++;
        }
        this.tileWidth = (int)this.map.get(i).getWidth();
        this.tileHeight = (int)this.map.get(i).getHeight();

    }

    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {
//        if (!this.pot.isEmpty())
//            for (Rectangle r : this.pot) {
//                if (r != null)
//                g.draw(new Rectangle(
//                        (float)r.getX() - Camera.getCameraObj().getPositionX(),
//                        (float)r.getY() - Camera.getCameraObj().getPositionY(),
//                        r.getWidth(), r.getHeight()));
//            }
    }

    /************* Entities and map collision *************/
    public void entityMapCollision(ArrayList<Entity> entities) {
        for (Entity entity : entities) {

            //Clearing collision states
            entity.setColliding(false);
            entity.clearCollisionDirection();
            this.mapCollision = false;

            //Getting potential collision tiles for check
            List<Rectangle> collisionMap;
            Vector2f object = new Vector2f(entity.getPositionX(), entity.getPositionY());
            if (entity instanceof Player)
                collisionMap = getPotentialCollisionMap(object);
            else collisionMap = new LinkedList<Rectangle>();

            //Checking for entities collision with map tiles
            for (int i = 0; i < collisionMap.size(); i++) {
                try {
                    if (collisionMap.get(i) != null) {
                        //Creating map tile rectangle with camera viewport coordinates
                        Rectangle collisionRec = new Rectangle(
                                collisionMap.get(i).getX() - Camera.getCameraObj().getPositionX(),
                                collisionMap.get(i).getY() - Camera.getCameraObj().getPositionY(),
                                collisionMap.get(i).getWidth(),
                                collisionMap.get(i).getHeight()
                        );
                        checkEntityRecCollision(entity, collisionRec);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //Check for collisions between entities
        checkEntitiesCollision(entities);
    }

    /************* Entities collision with other entities objects*************/
    public void checkEntitiesCollision(ArrayList<Entity> entities) {
        for (Entity entity : entities) {
            List<Entity> entitiesInRange = getPotentialCollisionEntities(entity.getPosition(), entities);
            for (int i = 0; i < entitiesInRange.size(); i++) {
                if (!entity.equals(entities.get(i))) {
                    //Creating map tile rectangle with camera viewport coordinates
                    Rectangle collisionRec = entities.get(i).getCollisionRec();
                    checkEntityRecCollision(entity, collisionRec);
                }
            }
        }
    }

    public void bulletCollision(Entity entity, List<Entity> entities) {
        LinkedList<Bullet> bullets = entity.getActiveBullets();
        try {
            if (!bullets.isEmpty()) {
                ListIterator<Bullet> it = (ListIterator<Bullet>) bullets.iterator();
                if (it.hasNext())
                while (!bullets.isEmpty()) {
                    Bullet b = it.next();

                    //Checking for bullet collision with map
                    checkBulletMapCollision (b, entities);

                    //Checking for bullet collision with entities
                    checkBulletEntityCollision(b, entity, entities);
                }
            }
        } catch (Exception e) {
            //If bullet list gets empty throws java.util.NoSuchElementException
        }
    }
    private void checkBulletEntityCollision (Bullet b, Entity attacker, List<Entity> entities) {
        List<Entity> entitiesInRange = getPotentialCollisionEntities(b.getBulletCoord(), entities);
        for (Entity e : entitiesInRange) {
            if (b.getGunID() != e.getID())
                if (b.getBulletRec().intersects(e.getCollisionRec())) {
                    e.notify(b, attacker);
                }
        }
    }

    private void checkBulletMapCollision (Bullet b, List<Entity> entities) {
        //Getting potential collision tiles for check
        List<Rectangle> collisionMap;
        Vector2f object = b.getBulletCoord();
        collisionMap = getPotentialCollisionMap(object);

        for (int i=0; i<collisionMap.size(); i++) {
            if (collisionMap.get(i).intersects(b.getBulletRec())) {
                for (Entity entity : entities) {
                    if (b.getGunID() == entity.getID())
                        //If entity is owner of this bullet, then destroy it from entity gun
                        entity.getGun().destroyBullet(b);
                }
            }
        }
    }

    private void checkEntityRecCollision (Entity entity, Rectangle collisionRec) {
        //Checking every side of entity rectangle for collision
        Rectangle e = entity.getCollisionRec();
        if (collisionRec.intersects(getRecLine(e, "TOP"))) {
            entity.notify(Entity.Direction.UP, true);
        }
        if (collisionRec.intersects(getRecLine(e, "BOTTOM"))) {
            entity.notify(Entity.Direction.DOWN, true);
        }
        if (collisionRec.intersects(getRecLine(e, "LEFT"))) {
            entity.notify(Entity.Direction.LEFT, true);
        }
        if (collisionRec.intersects(getRecLine(e, "RIGHT"))) {
            entity.notify(Entity.Direction.RIGHT, true);
        }
    }

    private List<Rectangle> getPotentialCollisionMap(Vector2f object) {
        List<Rectangle> newMap = new ArrayList<Rectangle>();

        Rectangle mapTile = new Rectangle(0, 0, this.tileWidth, this.tileHeight);
        int tileX = (Math.round(object.getX()/mapTile.getWidth()));
        int tileY = (Math.round(object.getY()/mapTile.getHeight()));

        /////////////////////////////////////////////////////////////////////
        int x = 0;
        int y = 0;

        if (tileY + 1 > this.mapWidth) y = tileY;
        else y = tileY + 1;
        int centerBottom = (y * this.mapWidth) + (tileX);

        if (tileY - 1 < 0) y = 0;
        else y = tileY - 1;
        int centerTop = (y * this.mapWidth) + (tileX);

        if (tileY - 1 < 0) y = 0;
        else y = tileY - 1;
        if (tileX - 1 < 0) x = 0;
        else x = tileX - 1;
        int leftTop = (y * this.mapWidth) + x;

        if (tileX - 1 < 0) x = 0;
        else x = tileX - 1;
        int leftCenter = ((tileY) * this.mapWidth) + x;
        if (tileY + 1 > this.mapWidth) y = tileY;
        else y = tileY + 1;
        if (tileX - 1 < 0) x = 0;
        else x = tileX - 1;
        int leftBottom = (y * this.mapWidth) + x;

        if (tileY - 1 < 0) y = 0;
        else y = tileY - 1;
        if (tileX + 1 > this.mapWidth) x = tileX;
        else x = tileX + 1;
        int rightTop = (y * this.mapWidth) + x;
        int rightCenter = ((tileY) * this.mapWidth) + x;
        if (tileY + 1 > this.mapWidth) y = tileY;
        else y = tileY + 1;
        int rightBottom = (y * this.mapWidth) + x;


        if (this.map.get(centerBottom) != null) {
            this.pot.add(this.map.get(centerBottom));
            newMap.add(this.map.get(centerBottom));
        }
        if (this.map.get(centerTop) != null) {
            this.pot.add(this.map.get(centerTop));
            newMap.add(this.map.get(centerTop));
        }

        if (this.map.get(leftTop) != null) {
            this.pot.add(this.map.get(leftTop));
            newMap.add(this.map.get(leftTop));
        }
        if (this.map.get(leftCenter) != null) {
            this.pot.add(this.map.get(leftCenter));
            newMap.add(this.map.get(leftCenter));
        }
        if (this.map.get(leftBottom) != null) {
            this.pot.add(this.map.get(leftBottom));
            newMap.add(this.map.get(leftBottom));
        }

        if (this.map.get(rightTop) != null) {
            this.pot.add(this.map.get(rightTop));
            newMap.add(this.map.get(rightTop));
        }
        if (this.map.get(rightCenter) != null) {
            this.pot.add(this.map.get(rightCenter));
            newMap.add(this.map.get(rightCenter));
        }
        if (this.map.get(rightBottom) != null) {
            this.pot.add(this.map.get(rightBottom));
            newMap.add(this.map.get(rightBottom));
        }

        ////////////////////////////////////////////////////////////////////////


        return newMap;
    }
    private ArrayList<Entity> getPotentialCollisionEntities(Vector2f object, List<Entity> entities) {
        ArrayList<Entity> newMap = new ArrayList<Entity>();
        Rectangle mapTile = new Rectangle(0, 0, this.tileWidth, this.tileHeight);
        Rectangle collisionRange = new Rectangle(
                object.getX() - mapTile.getWidth() * 2,
                object.getY() - mapTile.getHeight() * 2,
                mapTile.getWidth() * 5,
                mapTile.getHeight() * 5);

        for (Entity entity : entities) {
            if (entity.getEntityRec().intersects(collisionRange))
                newMap.add(entity);
        }

        return newMap;
    }
    private Line getRecLine (Rectangle rec, String side) {
        int camX = Camera.getCameraObj().getPositionX();
        int camY = Camera.getCameraObj().getPositionY();
        Line ret = new Line(0, 0);
        if (side.equals("TOP")) {
            Line eTop = new Line(
                    rec.getX() - camX,
                    rec.getY() - 3 - camY,
                    rec.getX() + rec.getWidth() - camX,
                    rec.getY() - 3 - camY);
            ret = eTop;
        }
        if (side.equals("BOTTOM")) {
            Line eBottom = new Line(
                rec.getX() - camX,
                rec.getY() + rec.getHeight() + 3 - camY,
                rec.getX() + rec.getWidth() - camX,
                rec.getY() + rec.getHeight() + 3 - camY);
            ret = eBottom;
        }
        if (side.equals("RIGHT")) {
            Line eRight = new Line(
                rec.getX() + rec.getWidth() + 3 - camX,
                rec.getY() - camY,
                rec.getX() + rec.getWidth() + 3 - camX,
                rec.getY() + rec.getHeight() - camY);
            ret = eRight;
        }
        if (side.equals("LEFT")) {
            Line eLeft = new Line(
                rec.getX() - 3 - camX,
                rec.getY() - camY,
                rec.getX() - 3 - camX,
                rec.getY() + rec.getHeight() - camY);
            ret = eLeft;
        }

        return ret;
    }

    public boolean isMapColliding() { return this.mapCollision; }
    @Override
    public String toString() {
        String collision = "Collision object \n" + "Collidable objects: " + this.map.size();
        return collision;
    }
}
