package mario.game.Objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import mario.game.Worlds.MainCodeProgram;

public abstract class Enemy extends Sprite {
    protected World world;
    protected MainCodeProgram screen;
    public Body b2body;
    public Vector2 velocity;
    public Enemy(MainCodeProgram screen, float x, float y)
    {
        this.world = screen.getWorld();
        this.screen = screen;
        setPosition(x,y);
        defineEnemy();
        velocity = new Vector2(-1,-2);
        b2body.setActive(false);
    }

    protected  abstract void defineEnemy();
    public abstract  void update(float dt);
    public abstract void hitOnHead(Mario mario);
    public abstract void onEnemyHit(Enemy enemy);
    public void reverseVelocity(boolean x, boolean y)//ejimai i kaire arba desine
    {
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}
