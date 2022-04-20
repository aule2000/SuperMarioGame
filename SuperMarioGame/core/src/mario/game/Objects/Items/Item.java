package mario.game.Objects.Items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import mario.game.Main;
import mario.game.Objects.Mario;
import mario.game.Worlds.MainCodeProgram;

public abstract class Item extends Sprite {
    protected MainCodeProgram screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;

    public Item(MainCodeProgram screen, float x, float y)
    {
        this.screen = screen;
        this.world = screen.getWorld();
        setPosition(x,y);
        setBounds(getX(),getY(), 16/ Main.PPM, 16/Main.PPM);
        defineItem();
        toDestroy = false;
        destroyed = false;
    }

    public abstract void defineItem();
    public abstract void use(Mario mario);

    public void update(float dt)
    {
        if(toDestroy && !destroyed)
        {
            world.destroyBody(body);
            destroyed = true;
        }
    }

    public void draw(Batch batch)
    {
        if(!destroyed)
            super.draw(batch);
    }

    public void destroy()
    {
        toDestroy = true;
    }
    public void reverseVelocity(boolean x, boolean y)//ejimai i kaire arba desine
    {
        if(x)
            velocity.x = -velocity.x;
        if(y)
            velocity.y = -velocity.y;
    }
}