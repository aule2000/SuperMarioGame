package mario.game.Objects;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;

import mario.game.Main;
import mario.game.Worlds.MainCodeProgram;

public class Gomba extends Enemy {
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;
    public Gomba(MainCodeProgram screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        for (int i = 0; i < 2; i++)
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"), i * 16, 0, 16, 16));
        walkAnimation = new Animation(0.4f, frames);
        stateTime = 0;
        setBounds(getX(), getY(), 16/ Main.PPM, 16/ Main.PPM);
        setToDestroy = false;
        destroyed = false;
    }
    public void update(float dt)
    {
        stateTime += dt;
        if(setToDestroy && !destroyed)
        {
            world.destroyBody(b2body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"),32,0,16,16));
            stateTime = 0;
        }
        else if(!destroyed){
            b2body.setLinearVelocity(velocity);// vaiksciojimas i kaire ar desine
            setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - getHeight() / 2);
            //setRegion(walkAnimation.getKeyFrame(stateTime, true));
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime,true));
        }
    }
    @Override
    protected void defineEnemy() {
        BodyDef bdef = new BodyDef();
        bdef.position.set(getX(), getY());
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ Main.PPM);
        fdef.filter.categoryBits = Main.enemywhit;
        fdef.filter.maskBits = Main.ground | Main.coinwhit | Main.blockwhit| Main.enemywhit|Main.objectwhit|Main.mariowhit;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        //galva prieso
        PolygonShape head = new PolygonShape();
        Vector2[] vertice = new Vector2[4];
        vertice[0] = new Vector2(-5,8).scl(1/Main.PPM);
        vertice[1] = new Vector2(5,8).scl(1/Main.PPM);
        vertice[2] = new Vector2(-3,3).scl(1/Main.PPM);
        vertice[3] = new Vector2(3,3).scl(1/Main.PPM);
        head.set(vertice);
        fdef.shape = head;
        fdef.restitution = 0.5f; //sokineja mario jei uzsoka ant galvos cia
        fdef.filter.categoryBits = Main.enemyheadwhit;
        b2body.createFixture(fdef).setUserData(this);
    }
    public void onEnemyHit(Enemy enemy){
        if(enemy instanceof  Turtle && ((Turtle)enemy).currentState == Turtle.State.MOVING_SHELL)
            setToDestroy = true;
        else
            reverseVelocity(true, false);
    }
    public void draw(Batch batch)
    {
        if(!destroyed || stateTime < 1)
            super.draw(batch);
    }
    @Override
    public void hitOnHead(Mario mario) {
        setToDestroy = true;
       // SuperMario.manager.get("audio/sounds/stomp.wav", Sound.class).play();
    }

}