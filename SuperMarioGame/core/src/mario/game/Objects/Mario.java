package mario.game.Objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import mario.game.Main;
import mario.game.Worlds.MainCodeProgram;

public class Mario extends Sprite {
    public World world;
    private TextureRegion dimariostovi;
    private TextureRegion didmariosoka;
    private Animation<TextureRegion>  didmariobega;
    private Animation <TextureRegion> augamario;
    private TextureRegion mariomirsta;
    private TextureRegion mariosoka;
    private Animation<TextureRegion> mariobega;
    private TextureRegion mariostovi;
    public Body b2body;
    public enum State { kristi, sokti, Stoveti, begti, augti, mirti};
    public State Dabartinebusena;
    public State ankstesnebusena;

    public static int newLevelnew = 0;
    private float bustimer;
    private boolean runright;
    private boolean mariobig;
    private boolean begimoauganimacija;
    private boolean dimarioapibrlaiaks;
    private boolean isnaujapibrlaikas;
    private boolean mariomire;
    public static boolean naujasmariolygis;
    public Mario( MainCodeProgram screen)
    {
        this.world = screen.getWorld();
        Dabartinebusena = State.Stoveti;
        ankstesnebusena = State.Stoveti;
        bustimer = 0;
        runright = true;

        Array<TextureRegion> frames = new Array<TextureRegion>();
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"), i*16, 0, 16, 16));}
        mariobega = new Animation(0.1f, frames);
        frames.clear();
        for (int i = 1; i < 4; i++) {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), i*16, 0, 16, 32));}
        didmariobega = new Animation(0.1f, frames);

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 240, 0, 16, 32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0, 0, 16, 32));
        augamario = new Animation(0.2f, frames);

        mariosoka = new TextureRegion(screen.getAtlas().findRegion("little_mario"), 80,0,16,16);
        didmariosoka = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 80,0,16,32);

        defineMario();
        mariostovi = new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16);
        dimariostovi = new TextureRegion(screen.getAtlas().findRegion("big_mario"), 0,0,16,32);

        mariomirsta = new TextureRegion(screen.getAtlas().findRegion("little_mario"),96,0,16,16);
        setBounds(0,0, 16/ Main.PPM,16/ Main.PPM);
        setRegion(mariostovi);
    }
    public void update(float dt)
    {
        if(mariobig)
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2 - 6 / Main.PPM);
        else
            setPosition(b2body.getPosition().x - getWidth()/2, b2body.getPosition().y - getHeight()/2);
        setRegion(getFrame(dt));
        if(dimarioapibrlaiaks)
            defineBigMario();
        if(isnaujapibrlaikas)
            redefineMario();
    }

    public TextureRegion getFrame(float dt)
    {
        Dabartinebusena = getState();


        TextureRegion region;
        switch (Dabartinebusena){
            case mirti:
                region = mariomirsta;
                break;
            case augti:
                region = augamario.getKeyFrame(bustimer);
                if(augamario.isAnimationFinished(bustimer))
                    begimoauganimacija = false;
                break;
            case sokti:
                region =  mariobig ? didmariosoka : mariosoka;
                break;
            case begti:
                region = mariobig ? didmariobega.getKeyFrame(bustimer,true) : mariobega.getKeyFrame(bustimer,true);
                break;
            case kristi:
            case Stoveti:
            default:
                region = mariobig ? dimariostovi : mariostovi;
                break;
        }
        if((b2body.getLinearVelocity().x<0||!runright)&& !region.isFlipX())
        {

            region.flip(true,false);
            runright = false;
        }
        else if((b2body.getLinearVelocity().x>0 || runright) && region.isFlipX())
        {

            region.flip(true,false);
            runright = true;
        }
        bustimer = Dabartinebusena == ankstesnebusena ? bustimer + dt : 0;
        ankstesnebusena = Dabartinebusena;
        return region;
    }
    public State getState()
    {
        if(mariomire)
            return  State.mirti;
        else if(begimoauganimacija)
            return  State.augti;
        else if(b2body.getLinearVelocity().y>0 || (b2body.getLinearVelocity().y<0&&ankstesnebusena == State.sokti)) {

            return State.sokti;
        }
        else if(b2body.getLinearVelocity().y<0)
            return State.kristi;
        else if(b2body.getLinearVelocity().x != 0)
            return State.begti;
        else
            return State.Stoveti;

    }
    public void grow()
    {
        if( !isBig() ) {
            begimoauganimacija = true;
            mariobig = true;
            dimarioapibrlaiaks = true;
            setBounds(getX(), getY(), getWidth(),getHeight()*2);
        }
    }
    public void defineBigMario()
    {
        Vector2 currentPosition = b2body.getPosition();
        world.destroyBody(b2body);

        BodyDef bdef = new BodyDef();
        bdef.position.set(currentPosition.add(0, 10 / Main.PPM));
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(5/ Main.PPM);
        fdef.filter.categoryBits = Main.mariowhit;
        fdef.filter.maskBits = Main.ground |
                Main.coinwhit |
                Main.blockwhit|
                Main.enemywhit|
                Main.objectwhit|
                Main.enemyheadwhit|
                Main.itemwhit;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);
        shape.setPosition(new Vector2(0, -14 / Main.PPM));
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Main.PPM, 6 / Main.PPM), new Vector2(2 / Main.PPM, 6 / Main.PPM));
        fdef.filter.categoryBits = Main.marioheadwhit;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
        dimarioapibrlaiaks = false;
    }

    public void defineMario()
    {
        BodyDef bdef = new BodyDef();
        bdef.position.set(250 / Main.PPM, 32/ Main.PPM);
        System.out.println(bdef.position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ Main.PPM);
        fdef.filter.categoryBits = Main.mariowhit;
        fdef.filter.maskBits = Main.ground |
                Main.coinwhit |
                Main.blockwhit|
                Main.enemywhit|
                Main.objectwhit|
                Main.enemyheadwhit|
                Main.itemwhit;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Main.PPM, 6 / Main.PPM), new Vector2(2 / Main.PPM, 6 / Main.PPM));
        fdef.filter.categoryBits = Main.marioheadwhit;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
    }
    public void hit(Enemy enemy)
    {

        if(enemy instanceof  Turtle && ((Turtle)enemy).getCurrentState() == Turtle.State.STANDING_SHELL)
        {
            ((Turtle)enemy).kick(this.getX() <= enemy.getX() ? Turtle.KICK_RIIGHT_SPEED : Turtle.KICK_LEFT_SPEED);
        }
        else{
            if(mariobig) {
                mariobig = false;
                isnaujapibrlaikas = true;
                setBounds(getX(), getY(), getWidth(), getHeight() / 2);
            }
            else{

                mariomire = true;
                Filter filter = new Filter();
                filter.maskBits = Main.nothing;
                for (Fixture fixture : b2body.getFixtureList())
                    fixture.setFilterData(filter);
                b2body.applyLinearImpulse(new Vector2(0, 4f), b2body.getWorldCenter(), true);
            }

        }
    }

    public boolean isBig(){
        return mariobig;
    }
    public boolean isDead()
    {
        return mariomire;
    }
    public boolean isNewLevelMario()
    {
        return naujasmariolygis;
    }
    public float getStateTimer()
    {
        return bustimer;
    }
    public void redefineMario()
    {
        Vector2 position = b2body.getPosition();
        world.destroyBody(b2body);
        BodyDef bdef = new BodyDef();
        bdef.position.set(position);
        bdef.type = BodyDef.BodyType.DynamicBody;
        b2body = world.createBody(bdef);

        FixtureDef fdef = new FixtureDef();
        CircleShape shape = new CircleShape();
        shape.setRadius(6/ Main.PPM);
        fdef.filter.categoryBits = Main.mariowhit;
        fdef.filter.maskBits = Main.ground |
                Main.coinwhit |
                Main.blockwhit|
                Main.enemywhit|
                Main.objectwhit|
                Main.enemyheadwhit|
                Main.itemwhit;

        fdef.shape = shape;
        b2body.createFixture(fdef).setUserData(this);

        EdgeShape head = new EdgeShape();
        head.set(new Vector2(-2 / Main.PPM, 6 / Main.PPM), new Vector2(2 / Main.PPM, 6 / Main.PPM));
        fdef.filter.categoryBits = Main.marioheadwhit;
        fdef.shape = head;
        fdef.isSensor = true;
        b2body.createFixture(fdef).setUserData(this);
        isnaujapibrlaikas = false;
    }

}