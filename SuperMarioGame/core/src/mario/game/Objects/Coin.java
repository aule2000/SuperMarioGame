package mario.game.Objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import mario.game.Main;
import mario.game.Objects.Items.ItemVector;
import mario.game.Objects.Items.Mushroom;
import mario.game.Scene.Hud;
import mario.game.Worlds.MainCodeProgram;


public class Coin extends Tile {
    private static TiledMapTileSet tileset;
    private final int BLANK_COIN = 28;
    public Coin(MainCodeProgram screen, MapObject object)
    {
        super(screen, object);
        tileset = map.getTileSets().getTileSet("tileset geras");
        fixture.setUserData(this);
        setCategoryFilter(Main.coinwhit);
    }
    @Override
    public void onHeadHit(Mario mario) {
        if(getCell().getTile().getId() == BLANK_COIN);
        else {
            if (object.getProperties().containsKey("mushroom")) {
                screen.spawnItem(new ItemVector(new Vector2(body.getPosition().x, body.getPosition().y + 16 / Main.PPM), Mushroom.class)); //idejimas grybo virs coin bloko
            }
            else
            getCell().setTile(tileset.getTile(BLANK_COIN));
            Hud.countScore(200);
        }
    }
}