import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by ai on 2/18/14.
 */
public class Map extends Play{

    private Image tileSheetImage;
    private Document XMLmap;
    private NodeList tileList;
    private int tileLayers;
    private Node tileSheet;
    private static Node tileWidth;
    private static Node tileHeight;
    private static Node mapWidth;
    private static Node mapHeight;

    public Map(int state) {
        super(state);
        this.XMLmap = getDocument("res/maps/" + state + ".xml");
    }

    public void Map() {

    }

    public void init (GameContainer gc, StateBasedGame sbg) throws SlickException {
        /** XML Map parsing **/
        this.tileList = XMLmap.getElementsByTagName("tile");
        this.tileLayers = XMLmap.getElementsByTagName("layer").getLength();
        this.tileSheet = XMLmap.getElementsByTagName("tileset").item(0).getAttributes().getNamedItem("name");
        this.tileWidth = XMLmap.getElementsByTagName("map").item(0).getAttributes().getNamedItem("tilewidth");
        this.tileHeight = XMLmap.getElementsByTagName("map").item(0).getAttributes().getNamedItem("tileheight");
        this.mapWidth = XMLmap.getElementsByTagName("map").item(0).getAttributes().getNamedItem("width");
        this.mapHeight = XMLmap.getElementsByTagName("map").item(0).getAttributes().getNamedItem("height");



        try {
            //Loading tile sheet image
            String tileSheetSource = "res/tiles/" + tileSheet.getNodeValue() + ".png";
            System.out.println(this.tileList.item(0).getAttributes().item(0).getNodeValue());
            this.tileSheetImage = new org.newdawn.slick.Image(tileSheetSource);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void render (GameContainer gc, StateBasedGame sbg, Graphics g) throws SlickException  {


        //Rendering map by layers
        int tilesPerRow = this.tileSheetImage.getWidth() / Integer.parseInt(this.tileWidth.getNodeValue());
        int tWidth = Integer.parseInt(this.tileWidth.getNodeValue());
        int tHeight = Integer.parseInt(this.tileHeight.getNodeValue());

        int tilesOnScreenX = gc.getWidth() / tWidth;
        int tilesOnScreenY = gc.getHeight() / tHeight;
        int startRow = Math.abs(Play.getCameraObj().getPositionX() / tWidth);
        int startCol = Math.abs(Play.getCameraObj().getPositionY() / tHeight);



        for (int mapLayer = 1; mapLayer <= this.tileLayers; mapLayer++)
        {
            int tileIterator = 0;
            for (int row = startCol; row < (startCol + tilesOnScreenY) + 4; row++) {
                for (int col = startRow; col < (startRow + tilesOnScreenX) + 4; col++) {

                    //Getting tile GID from parsed XML
                    tileIterator = (row * Integer.parseInt(this.mapWidth.getNodeValue())) + col;
                    int tileID = Integer.parseInt(this.tileList.item(tileIterator).getAttributes().item(0).getNodeValue());

                    //Calculating current tilesheet coordinates from given GID
                    int tileRow = 0;
                    int tileCol = 0;
                    if (tileID % tilesPerRow == 0) {

                        tileRow = (tileID / tilesPerRow) - 1;
                        tileCol = tilesPerRow - 1;

                    } else {

                        tileRow = tileID / tilesPerRow;
                        tileCol = (tileID % tilesPerRow) - 1;

                    }


                    int cameraPositionX = Play.getCameraObj().getPositionX();
                    int cameraPositionY = Play.getCameraObj().getPositionY();
                    g.drawImage(this.tileSheetImage,
                            /** (x,y) (dx,dy) On screen **/
                            (col * tWidth) - cameraPositionX,
                            (row * tHeight) - cameraPositionY,
                            ((col * tWidth) - cameraPositionX) + tWidth,
                            ((row * tHeight) - cameraPositionY) + tHeight,
                            /** (x,y) From tilesheet **/
                            tileCol * tWidth, tileRow * tHeight,
                            (tileCol * tWidth) + tWidth, (tileRow * tHeight) + tHeight);

                    tileIterator++;
                }
            }

        }
    }

    public static int getMapHeight() {
        return Integer.parseInt(mapHeight.getNodeValue());
    }

    public static int getMapWidth() {
        return Integer.parseInt(mapWidth.getNodeValue());
    }

    public static int getTileWidth() {
        return Integer.parseInt(tileWidth.getNodeValue());
    }
    public static int getTileHeight() {
        return Integer.parseInt(tileHeight.getNodeValue());
    }

    private static Document getDocument(String XMLfile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            return builder.parse(new InputSource(XMLfile));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
