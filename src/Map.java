import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TileSet;
import org.newdawn.slick.tiled.TiledMap;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by ai on 2/18/14.
 */
public class Map extends Play{

    private TiledMap tiledMap;
    private List<Rectangle> collisionMap;
    private int tileWidth;
    private int tileHeight;

    public Map(int state) {
        super(state);
        try {
            this.tiledMap = new TiledMap("res/maps/" + state + ".tmx");
            this.tileWidth = this.tiledMap.getTileWidth();
            this.tileHeight = this.tiledMap.getTileHeight();

            //Adding all map tiles with property "collidable" = true, as rectangles to collision map list
            this.collisionMap = new LinkedList<Rectangle>();
            for (int layer = 0; layer < this.tiledMap.getLayerCount(); layer++) {
                for (int y = 0; y < this.tiledMap.getWidth(); y++) {
                    for (int x = 0; x < this.tiledMap.getHeight(); x++) {

                        int gid = this.tiledMap.getTileId(x, y, layer);
                        Rectangle tile = null;
                        int tileCoord = (y * this.tiledMap.getWidth()) + x;

                        if (this.tiledMap.getTileProperty(gid, "collidable", "false").equalsIgnoreCase("true")) {
                            tile = new Rectangle(x*tileWidth, y*tileHeight, tileWidth, tileHeight);
                        }

                        this.collisionMap.add(tileCoord, tile);

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render (GameContainer gc, Camera camera) throws SlickException  {
        //Where to render at
        int windowX = (int)(-camera.getPositionX() % tileWidth);
        int windowY = (int)(-camera.getPositionY() % tileHeight);

        // Source location on tile map
        int tileX = camera.getPositionX() / tileWidth;
        int tileY = camera.getPositionY() / tileHeight;

        // The size of the section to render
        int widthInTiles = (gc.getWidth() / tileWidth) + 2;
        int heightInTiles = (gc.getHeight() / tileHeight) + 2;


        this.tiledMap.render(windowX, windowY, tileX, tileY, widthInTiles, heightInTiles, false);
    }

    public List<Rectangle> getCollisionMap() {
        return this.collisionMap;
    }
    public Rectangle getMapRect () {
        return new Rectangle(0, 0,
                this.tiledMap.getWidth() * this.tileWidth,
                this.tiledMap.getHeight() * this.tileHeight
                );
    }
    public int getWidth() { return this.tiledMap.getWidth(); }
    public int getHeight() { return this.tiledMap.getHeight(); }
    public int getTileWidth() { return this.tileWidth; }
    public int getTileHeight() { return this.tileHeight; }
    public String toString() {
        String map = "Map object \n" +
                "Map size " + this.tiledMap.getWidth() + "x" + this.tiledMap.getHeight() +
                "\nTile size" + this.tileWidth + "x" + this.tileHeight;

        return map;
    }
}
