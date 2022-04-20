package mario.game.OtherWorldCreate;

import com.badlogic.gdx.physics.box2d.*;

import mario.game.Main;
import mario.game.Objects.Enemy;
import mario.game.Objects.Items.Item;
import mario.game.Objects.Mario;
import mario.game.Objects.Tile;

public class WorldContact implements ContactListener {
    public static int collids = 0;
    @Override
    public void beginContact(Contact contact) {

        Fixture fixA = contact.getFixtureA();
        Fixture fixB = contact.getFixtureB();

        int cdef = fixA.getFilterData().categoryBits | fixB.getFilterData().categoryBits;

        switch (cdef) {

            case Main.marioheadwhit | Main.blockwhit:
            case Main.marioheadwhit | Main.coinwhit:
                if (fixA.getFilterData().categoryBits == Main.marioheadwhit)
                    ((Tile) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else
                    ((Tile) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;
            case Main.enemyheadwhit | Main.mariowhit:
                if (fixA.getFilterData().categoryBits == Main.enemyheadwhit)
                    ((Enemy) fixA.getUserData()).hitOnHead((Mario) fixB.getUserData());
                else
                    ((Enemy) fixB.getUserData()).hitOnHead((Mario) fixA.getUserData());
                break;
            case Main.enemywhit | Main.objectwhit:

                if (fixA.getFilterData().categoryBits == Main.enemywhit)
                    ((Enemy) fixA.getUserData()).reverseVelocity(true, false);
                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true, false);
                break;
            case Main.mariowhit | Main.enemywhit:
                if (fixA.getFilterData().categoryBits == Main.mariowhit)
                    ((Mario) fixA.getUserData()).hit((Enemy)fixB.getUserData());
                else
                    ((Mario) fixB.getUserData()).hit((Enemy) fixA.getUserData());
                break;
            case Main.enemywhit | Main.enemywhit:  /// jei du susitinka turi pakeisti kryptis priesai
                ((Enemy) fixA.getUserData()).onEnemyHit((Enemy)fixB.getUserData());
                ((Enemy) fixB.getUserData()).onEnemyHit((Enemy)fixA.getUserData());
                break;
            case Main.itemwhit | Main.objectwhit:
                if (fixA.getFilterData().categoryBits == Main.itemwhit)
                    ((Item) fixA.getUserData()).reverseVelocity(true, false);
                else
                {
                    ((Item) fixB.getUserData()).reverseVelocity(true, false);}
                break;
            case Main.itemwhit | Main.mariowhit:
                if (fixA.getFilterData().categoryBits == Main.itemwhit)
                    ((Item) fixA.getUserData()).use((Mario) fixB.getUserData());
                else
                    ((Item) fixB.getUserData()).use((Mario) fixA.getUserData());
                break;
            case Main.mariowhit | Main.objectwhit:
                // Gdx.app.log("Paliecia objekta","");
                System.out.println("labas");
                collids++;
                break;

        }
    }
    @Override
    public void endContact(Contact contact) {}

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {}

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {}

}