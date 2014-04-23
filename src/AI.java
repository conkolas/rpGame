/**
 * Created by root on 4/21/14.
 */
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Curve;
import org.newdawn.slick.geom.Point;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.*;

public class AI extends Play implements PathFinder{
    private Map map;
    private Path debugPath;
    private PathFinder pathFinder;
    private int searchDepth;

    public AI(int state) {
        super(state);
    }

    @Override
    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException {

        if (this.debugPath != null) {
            Curve pathCurve;
            for (int i = 1; i < this.debugPath.getLength() - 1; i++) {
                if (this.debugPath.getStep(i) != null) {
                    if (this.debugPath.getStep(i + 1) != null) {

                        //Making a line between two steps
                        Vector2f sp = new Vector2f(this.debugPath.getStep(i).getX()*32 - Camera.getCameraObj().getPositionX(),
                                                    this.debugPath.getStep(i).getY()*32 - Camera.getCameraObj().getPositionY());
                        Vector2f dp = new Vector2f(this.debugPath.getStep(i + 1).getX()*32 - Camera.getCameraObj().getPositionX(),
                                                    this.debugPath.getStep(i + 1).getY()*32 - Camera.getCameraObj().getPositionY());

                        //Checking degree between two steps
                        double angle = Math.atan2(dp.y-sp.y,dp.x-sp.x)*180.0/Math.PI;
                        if ((angle % 45 == 0) && (angle != 0) && (angle % 90 != 0)) {
                            Point mid = new Point((sp.getX() + dp.getX())/2, (sp.getY() + dp.getY())/2 );
                            g.draw(new Rectangle(mid.getX(), mid.getY(), 8, 8));
                            pathCurve = new Curve(sp, sp, dp, dp);
                            Rectangle r = new Rectangle(mid.getX() - (this.map.getTileWidth()/2),
                                    mid.getY() - (this.map.getTileHeight()/2),
                                    this.map.getTileWidth(),
                                    this.map.getTileHeight());
                            g.draw(r);
                            //if (this.map.blocked((int)r.getX(), (int)r.getY())) {}
                                g.draw(pathCurve);
                        }
                    }
                    Path.Step s = this.debugPath.getStep(i);
                    g.draw(new Rectangle(s.getX()*32 - Camera.getCameraObj().getPositionX(),
                            s.getY()*32 - Camera.getCameraObj().getPositionY(), 5, 5));
                }
            }
        }
    }

    public void init() {
        try {
            this.map = super.getCurrentMap();
            this.searchDepth = this.map.getHeightInTiles();
            this.pathFinder = new AStarPathFinder(this.map, searchDepth, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Path findPath(Mover mover, int i, int i2, int i3, int i4) {
        int sx = (Math.round(i/this.map.getTileWidth()));
        int sy = (Math.round(i2/this.map.getTileHeight()));
        int dx = (Math.round(i3/this.map.getTileWidth()));
        int dy = (Math.round(i4/this.map.getTileHeight()));

        this.debugPath = this.pathFinder.findPath(mover, sx, sy, dx, dy);
        return this.debugPath;
    }

}
