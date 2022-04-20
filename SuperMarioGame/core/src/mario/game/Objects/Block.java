package mario.game.Objects;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.physics.box2d.Body;
import mario.game.Main;
import mario.game.Scene.Hud;
import mario.game.Worlds.MainCodeProgram;

public class Block extends Tile{
   // public Body b2body;
    public Block(MainCodeProgram screen, MapObject obj)
    {
        super(screen, obj);
        fixture.setUserData(this);
        setCategoryFilter(Main.blockwhit);
    }
    @Override
    public void onHeadHit(Mario mario) {
        if(mario.isBig()){
            setCategoryFilter(Main.destroyedwhit);
            getCell().setTile(null);
            Hud.countScore(1000);
        }
    }
}
