import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.Path;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.PathFinder;


/**
 * Created by root on 4/21/14.
 */
public class Enemy extends Entity implements Mover, MovableEnemy {
    private Path currentPath;
    private AI enemyAI;

    public Enemy() {
        super();
    }
    public Enemy(int x, int y) {
        super(x, y);
        this.enemyAI = new AI(0);
        this.enemyAI.init();
        this.currentPath = new Path();

        this.currentPath = this.enemyAI.findPath(
                this,
                super.getPositionX(),
                super.getPositionY(),
                super.getPositionX() + 2000,
                super.getPositionY() + 100
                );
    }


    @Override
    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException  {
        super.render(gc, sbg, g);
        this.enemyAI.render(gc, sbg, g);

    }

    @Override
    public void update (GameContainer gc, StateBasedGame sbg, int delta) throws SlickException {
        super.update(gc, sbg, delta);
    }

    public void moveUp (boolean val) {
        this.setOnMoveUp(val);
    }

    public void moveDown (boolean val) {
        this.setOnMoveDown(val);
    }

    public void moveRight (boolean val) {
        this.setOnMoveRight(val);
    }

    public void moveLeft (boolean val) {
        this.setOnMoveLeft(val);
    }

}

