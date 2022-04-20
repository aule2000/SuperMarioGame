package mario.game.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;

import mario.game.Main;
import mario.game.Worlds.MainCodeProgram;

public class Turtle extends Enemy {
    public  static  final int KICK_LEFT_SPEED = -2;
    public  static  final int KICK_RIIGHT_SPEED = 2;
    public enum State {WALKING, STANDING_SHELL, MOVING_SHELL, DEAD}
    public State currentState;
    public State previousState;
    private float stateTime;
    private Animation walkAnimation;
    private Array<TextureRegion> frames;
    private TextureRegion shell;
    private float deadRotationDegrees;
    private boolean destroyed;
    public Turtle(MainCodeProgram screen, float x, float y) {
        super(screen, x, y);
        frames = new Array<TextureRegion>();
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"),0,0,16,24));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("turtle"),16,0,16,24));
        shell = new TextureRegion(screen.getAtlas().findRegion("turtle"),64,0,16,24);
        walkAnimation = new Animation(0.2f, frames);
        currentState = previousState = State.WALKING;
        deadRotationDegrees = 0;

        setBounds(getX(),getY(),16 / Main.PPM, 24/Main.PPM);
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
    public void onEnemyHit(Enemy enemy)
    {
        if(enemy instanceof  Turtle)
        {
            if(((Turtle)enemy).currentState == State.MOVING_SHELL && currentState != State.MOVING_SHELL){
                killed();
            }
            else if(currentState == State.MOVING_SHELL && ((Turtle)enemy).currentState == State.WALKING)
                return;
            else
                reverseVelocity(true,false);
        }
        else if(currentState != State.MOVING_SHELL)
            reverseVelocity(true,false);
    }
    public TextureRegion getFrame(float dt)
    {
        TextureRegion region;

        switch (currentState)
        {
            case STANDING_SHELL:
            case MOVING_SHELL:
                region = shell;
                break;
            case WALKING:
            default:
                region = (TextureRegion) walkAnimation.getKeyFrame(stateTime,true);
                break;
        }

        if(velocity.x > 0 && region.isFlipX() == false)//jei i desine eina apsukti animacija
            region.flip(true,false);
        if(velocity.x <0 && region.isFlipX() == true)//ir i kita puse
            region.flip(true,false);
        stateTime = currentState == previousState ? stateTime + dt : 0;
        previousState = currentState;
        return region;
    }

    @Override
    public void update(float dt) {
        setRegion(getFrame(dt));
        if(currentState == State.STANDING_SHELL && stateTime >5)
        {
            currentState = State.WALKING;
            velocity.x = 1;
        }
        setPosition(b2body.getPosition().x - getWidth() / 2, b2body.getPosition().y - 8/ Main.PPM);

        if(currentState == State.DEAD){
            deadRotationDegrees += 3;
            rotate(deadRotationDegrees);
            if(stateTime >5 && !destroyed){
                world.destroyBody(b2body);
                destroyed = true;
            }
        }
        else
            b2body.setLinearVelocity(velocity);
    }

    @Override
    public void hitOnHead(Mario mario) {
        if(currentState != State.STANDING_SHELL)
        {
            currentState = State.STANDING_SHELL;
            velocity.x = 0;
        }
        else {
            kick(mario.getX() <= this.getX() ? KICK_RIIGHT_SPEED : KICK_LEFT_SPEED);
        }
    }
    public void draw(Batch batch)
    {
        if(!destroyed)
            super.draw(batch);
    }
    public void kick(int speed)
    {
        velocity.x = speed;
        currentState = State.MOVING_SHELL;
    }
    public State getCurrentState(){
        return currentState;
    }
    public void killed(){
        currentState = State.DEAD;
        Filter filter = new Filter();
        filter.maskBits = Main.nothing;

        for(Fixture fixture : b2body.getFixtureList())
            fixture.setFilterData(filter);
        b2body.applyLinearImpulse(new Vector2(0, 5f), b2body.getWorldCenter(), true);
    }
}
