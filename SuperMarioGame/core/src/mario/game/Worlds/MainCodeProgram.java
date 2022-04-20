package mario.game.Worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.org.apache.xpath.internal.operations.Variable;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import mario.game.Main;
import mario.game.Objects.Enemy;
import mario.game.Objects.Items.Item;
import mario.game.Objects.Items.ItemVector;
import mario.game.Objects.Items.Mushroom;
import mario.game.Objects.Mario;
import mario.game.OtherWorldCreate.OtherWorldCreator;
import mario.game.OtherWorldCreate.TextListener;
import mario.game.OtherWorldCreate.WorldContact;
import mario.game.Scene.Hud;

public class MainCodeProgram extends JFrame implements Screen {

    private Main zaidimas;
    private OrthographicCamera orthographicCamera;
    private TextureAtlas textureAtlas;
    public static int kurislevel=1;
    private Mario zaidejas;
    public Stage stage;
    private Hud hud;
    private TmxMapLoader maploader;
    private TiledMap tilemap;
    static int walks = 0;
    private OrthogonalTiledMapRenderer renderer;
    private Viewport viewport;
    private World world;
    private Array<Item> items;
    private LinkedBlockingQueue<ItemVector> itemstoSpawn;
    String text="";
    private Box2DDebugRenderer b2dr;
    private OtherWorldCreator otherWorldCreator;
    public  String kodas;
    public SpriteBatch spriteBatch;
    public boolean b =false;
    public static ArrayList<String> firstintkint = new ArrayList<String>();
   public static ArrayList<Integer> firstintvalue = new ArrayList<Integer>();  //int
   public static ArrayList<String> firstdoublekint = new ArrayList<String>();
    public static ArrayList<Double> firstdoublevalue = new ArrayList<Double>();  //double
    public static ArrayList<String> firstboolkint = new ArrayList<String>();
    public  static ArrayList<String> firstboolvalue = new ArrayList<String>();   //bool
    public  static ArrayList<String> firststringkint = new ArrayList<String>();
    public static ArrayList<String> firststringvalue = new ArrayList<String>(); //string
    public static ArrayList<String> firstcharkint =  new ArrayList<String>();
    public static ArrayList<char[]> firstcharvalue =  new ArrayList<char[]>();  //char


    private Skin skin;
    private Table table;
    private TextButton button;
    public TextArea TF;

    private static String textInput = null;
    public static TextListener TextListener = new TextListener();
    private static String[] textInputString = null;
    private static ArrayList<Variable> VariableList = new ArrayList<>();


    public MainCodeProgram(Main zaidimas, String text,SpriteBatch spriteBatch ) {
        if(text == null){
            textureAtlas = new TextureAtlas("All.pack");
            this.zaidimas = zaidimas;
            orthographicCamera = new OrthographicCamera();
            viewport = new FitViewport(Main.Width / Main.PPM, Main.Height / Main.PPM, orthographicCamera);
            viewport.apply();
            hud = new Hud(zaidimas.spriteBatch);
            naujaslygis();
            otherWorldCreator = new OtherWorldCreator(this);
            zaidejas = new Mario( this);
            world.setContactListener(new WorldContact());
            items = new Array<Item>();
            itemstoSpawn = new LinkedBlockingQueue<ItemVector>();
        }
    }
    public TextureAtlas getAtlas(){return textureAtlas;}
    @Override
    public void show() {}
    int i = 0, kur = 1;
    public void handleInput(float dt) {
        if (zaidejas.Dabartinebusena != Mario.State.mirti) {
            String gauta = "";
            if(TextListener.getText()!= null)
            {
                gauta = TextListener.getText();
                if(gauta.equals("reset")){
                    zaidimas.setScreen(new MainCodeProgram(zaidimas,null,zaidimas.spriteBatch));
                    TextListener.setText(null);
                }
                else{
                    input(gauta);
                    TextListener.setText(null);
                }

            }


            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                JPanel jp = new JPanel();
                JButton mygtukas = new JButton("Vykdyti veiksmus");
                final JTextArea jt = new JTextArea(12, 20);
                JFrame window = new JFrame("Įvedimas");
                window.setSize(300, 300);
                window.setVisible(true);
                jp.add(jt);
                window.add(jp);
                mygtukas.setBounds(5, 5, 100, 100);
                jp.add(mygtukas);
                mygtukas.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        if(jt.getText().isEmpty()) {
                            System.out.println("Nera komandos!!!");
                        }
                        else {
                            textInput = jt.getText();
                            input(textInput.toString());
                            jt.setText("");

                        }
                    }
                });
            }
        }
    }
    public void spawnItem(ItemVector idef)
    {
        itemstoSpawn.add(idef);
    }
    public void handleSpawningItems(){
        if(!itemstoSpawn.isEmpty())
        {
            ItemVector idef = itemstoSpawn.poll();
            if(idef.type == Mushroom.class){items.add(new Mushroom(this, idef.position.x,idef.position.y));}
        }
    }
    public void update(float dt)
    {
       handleInput(dt);
        handleSpawningItems();
        world.step(1 / 60f, 6, 2);
        zaidejas.update(dt);
        for (Enemy enemy: otherWorldCreator.getEbemies()) {
            enemy.update(dt);
            if(enemy.getX() < zaidejas.getX() + 224 / Main.PPM){enemy.b2body.setActive(true);}
        }
        for(Item item : items){item.update(dt);}
        hud.refresh(dt);
        if(zaidejas.Dabartinebusena != Mario.State.mirti){orthographicCamera.position.x = zaidejas.b2body.getPosition().x;}
        orthographicCamera.update();
        renderer.setView(orthographicCamera);
    }
    @Override
    public void render(float delta) {
        update(delta);
        int k = 0;
       Gdx.gl.glClearColor(0,0,0,1);
       Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();
        b2dr.render(world, orthographicCamera.combined);
        zaidimas.spriteBatch.setProjectionMatrix(orthographicCamera.combined);
        zaidimas.spriteBatch.begin();
        zaidejas.draw(zaidimas.spriteBatch);
        for (Enemy enemy: otherWorldCreator.getEbemies())
            enemy.draw(zaidimas.spriteBatch);
        for(Item item : items)
            item.draw(zaidimas.spriteBatch);
        zaidimas.spriteBatch.end();
        zaidimas.spriteBatch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();

        if ((zaidejas.getX() > 15 && kurislevel < 4)) {
            kurislevel++;
            if(kurislevel!=4){
                zaidimas.setScreen(new MainCodeProgram(zaidimas,"",spriteBatch));
                dispose();
            }
        }
        if(gameOver())
        {
            kurislevel = 1;
            zaidimas.setScreen(new GameOverWorld(zaidimas));
            dispose();
        }
        if (kurislevel==4)
        {
            zaidimas.setScreen(new WinWindow(zaidimas));
            dispose();
        }
    }
    @Override
    public void resize(int width, int height) {viewport.update(width,height);}
    public World getWorld(){ return world;}
    public TiledMap getMap(){return tilemap;}
    @Override
    public void pause() {}
    @Override
    public void resume() {}
    @Override
    public void hide() {}
    public boolean gameOver()
    {
        if(zaidejas.Dabartinebusena == Mario.State.mirti && zaidejas.getStateTimer() > 3){return true;}
        if(kurislevel == 5){return true;}
        else if(zaidejas.getY() <0){return true;}
        else{return false;}
    }
    public boolean Youwonn()
    {
        if(kurislevel==4){return true;}
        else{return false;}
    }
    public  void naujaslygis()
    {
        maploader = new TmxMapLoader();
        if(kurislevel == 1){tilemap = maploader.load("mario1lvl.tmx");
        }
        if (kurislevel == 2){tilemap = maploader.load("mario2lvl.tmx");}
        if (kurislevel == 3){tilemap = maploader.load("mario3lvl.tmx");}
        if (kurislevel == 4){
            zaidimas.setScreen(new WinWindow(zaidimas));}
            renderer = new OrthogonalTiledMapRenderer(tilemap, 1  / Main.PPM);
            orthographicCamera.position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);

        world = new World(new Vector2(0, -10 ), true);
        b2dr = new Box2DDebugRenderer();
    }
    @Override
    public void dispose() {
        tilemap.dispose();
        renderer.dispose();
        world.dispose();
        b2dr.dispose();
        hud.dispose();
    }
    ArrayList<Integer> ifassss = new ArrayList<Integer>();
    public void input(String text) {
        ArrayList<String> pseudokodas = new ArrayList<String>();
        this.text = text;
        i = 0;
        String str = text;
        String split[] = str.split("/");

        for (String spacebar : split) {
            pseudokodas.add(spacebar);
            i = 0;
        }
        int foras = 0;
        for (int i = 0;i < pseudokodas.size(); i++) {
            if ((pseudokodas.get(i).equals("moveleft") && (zaidejas.b2body.getLinearVelocity().x >= -2))||(pseudokodas.get(i).equals("moveleft;") && (zaidejas.b2body.getLinearVelocity().x >= -2))){moveleft();} //eiti i kaire
            if (pseudokodas.get(i).equals("moveright") && (zaidejas.b2body.getLinearVelocity().x <= 2)||(pseudokodas.get(i).equals("moveright;") && (zaidejas.b2body.getLinearVelocity().x <= 2))) {moveright();} // eiti i desine
            if(pseudokodas.get(i).equals("moverighttowall")||pseudokodas.get(i).equals("moverighttowall;")){moverighttowall();} //eiti i desine kol atsitrenkia
            if(pseudokodas.get(i).equals("movelefttowall")||pseudokodas.get(i).equals("movelefttowall;")){movelefttowall();}  //eiti i kaire kol atsitrenkia
            if(pseudokodas.get(i).equals("stop")||pseudokodas.get(i).startsWith("stop;")){stop();}   //sustoti
            if(pseudokodas.get(i).equals("jump")||pseudokodas.get(i).equals("jump;")){jump();}   //sokti  i virsu
            if(pseudokodas.get(i).equals("jumpright")||pseudokodas.get(i).equals("jumpright;")){jumpright();}  //sokti i desine
            if(pseudokodas.get(i).equals("jumpleft")||pseudokodas.get(i).equals("jumpleft;")) {jumpleft();} //sokti i kaire
            if(pseudokodas.get(i).equals("ifstrikeinrightwallback")||pseudokodas.get(i).equals("ifstrikeinrightwallback;")){ifstrikeinrightwallback();} //jei atsitrenkia desineje paeik atgal
            if(pseudokodas.get(i).startsWith("ifstrikeleftwallback")||pseudokodas.get(i).equals("ifstrikeleftwallback")){ifstrikeleftwallback();}//jei atsitrenkia desineje paeik atgal
            if(pseudokodas.get(i).startsWith("int")){recognizeint(pseudokodas.get(i));}
            if(pseudokodas.get(i).startsWith("double")){recognizedouble(pseudokodas.get(i));}
            if(pseudokodas.get(i).startsWith("String")){recognizestring(pseudokodas.get(i));}
            if(pseudokodas.get(i).startsWith("char")){recognizechar(pseudokodas.get(i));}
            if(pseudokodas.get(i).startsWith("if")){ifas(pseudokodas.get(i),0);}
            if(pseudokodas.get(i).startsWith("boolean")){recognizebool(pseudokodas.get(i));}
            if(pseudokodas.get(i).startsWith("for"))
            {
                recognizefor(pseudokodas.get(i));
                foras++;
            }
            if(pseudokodas.get(i).startsWith("while")){
                recognizewhile(pseudokodas.get(i));}
            if(pseudokodas.get(i).endsWith("--")||pseudokodas.get(i).endsWith("--;"))
            {
                if(foras == 0){minuminus(pseudokodas.get(i));}
                else{foras--;}
            }
            if(pseudokodas.get(i).endsWith("++")||pseudokodas.get(i).endsWith("++;"))
            {
                if(foras==0){plusplus(pseudokodas.get(i));}
                else{foras--;}
            }
            if(istheend){             //jeigu zaidimo pabaiga tada nunulinti viska
                walks = 0;
                istheend = false;
            }
        }
    }
    void plusplus(String zodis) {
        if(zodis.startsWith(firstintkint.get(0))){firstintvalue.set(0,  firstintvalue.get(0)+1);}
    }
    void minuminus(String zodis) {
        if(zodis.startsWith(firstintkint.get(0))){firstintvalue.set(0,  firstintvalue.get(0)-1);}
    }
    void recognizewhile(String zodis) {
        String firstvaluestr = null, secondvaluestr = null;
        int where1 = 2, where2 = 4, whereinthemiddle = 3, secondvalueint =0, run =0, next = 0;
        boolean symboleilbool1 = false, symboleilbool2 = false, iscontinue = true, boolinteger1 = false, boolinteger2 = false;
        Integer firstvalueint = null;
        String while2[] = zodis.split(" ");
        try {
            firstvalueint = (Integer.parseInt(while2[where1]));
            if (firstvalueint != 0) {boolinteger2 = true;}
        } catch (NumberFormatException e) {
            for (int c = 0; c < firstcharkint.size(); c++) {
                if (while2[where1].equals(firstcharkint.get(c))) {
                    symboleilbool1 = true;
                    firstvaluestr = String.valueOf(firstcharkint.get(c));
                }
            }
            for (int c = 0; c < firststringkint.size(); c++) {
                if (while2[where1].equals(firststringkint.get(c))) {
                    symboleilbool1 = true;
                    firstvaluestr = firststringvalue.get(c);
                }
            }
            for (int c = 0; c < firstintkint.size(); c++) {
                if (while2[where1].equals(firstintkint.get(c))) {
                    firstvalueint = firstintvalue.get(c);
                    boolinteger1 = true;
                }
            }
            for (int c = 0; c < firstdoublekint.size(); c++) {
                if (while2[where1].equals(firstdoublekint.get(c))) {
                    firstvaluestr = firstdoublevalue.get(c).toString();
                    symboleilbool1 = true;
                }
            }
            for (int c = 0; c < firstboolkint.size(); c++) {
                if (while2[where1].equals(firstboolkint.get(c))) {
                    symboleilbool1 = true;
                    firstvaluestr = firstboolvalue.get(c);
                }
            }
            if (symboleilbool1 == false && boolinteger1 == false) {firstvaluestr = while2[where1]; }
        }
        try {
            secondvalueint = (Integer.parseInt(while2[where2]));
            if (secondvalueint != 0) {boolinteger2 = true;}
        } catch (NumberFormatException e) {
            for (int c = 0; c < firstintkint.size(); c++) {
                if (while2[where2].equals(firstintkint.get(c))) {
                    boolinteger2 = true;
                    secondvalueint = firstintvalue.get(c);
                }
            }
            for (int c = 0; c < firstdoublekint.size(); c++) {
                if (while2[where2].equals(firstdoublekint.get(c))) {
                    secondvaluestr = firstdoublevalue.get(c).toString();
                    symboleilbool2 = true;
                }
            }
            for (int c = 0; c < firstcharkint.size(); c++) {
                if (while2[where2].equals(firstcharkint.get(c))) {
                    symboleilbool2 = true;
                    secondvaluestr = String.valueOf(firstcharvalue.get(c));
                }
            }
            for (int c = 0; c < firststringkint.size(); c++) {
                if (while2[where2].equals(firststringvalue.get(c))) {
                    symboleilbool2 = true;
                    secondvaluestr = firststringvalue.get(c);
                }
            }
            for (int c = 0; c < firstboolkint.size(); c++) {
                if (while2[where2].equals(firstboolkint.get(c))) {
                    symboleilbool2 = true;
                    secondvaluestr = firstboolvalue.get(c);
                }
            }
        }
        if (symboleilbool2 == false && boolinteger2 == false) {secondvaluestr = while2[where2];}
        int isincreasedecrease = 0, place1 = 0;
        if (isif==1&&inforisif!=0) {place1 = 13;}
        if (isif==0&&inforisif!=0) {place1 = 6;}
        if (isif==1&&inforisif==0) {place1 = 10;}
        if (isif==0&&inforisif==0) {place1 = 5;}

        String in2 = while2[place1];
        String[] inif2 = in2.split("[{;}]");
        for (int d  =0; d <inif2.length ;d++){
            if (inif2[d].startsWith(while2[where1]) && inif2[d].endsWith("--")) {isincreasedecrease=1;}
            if (inif2[d].startsWith(while2[where1]) && inif2[d].endsWith("++")) {isincreasedecrease=2;}
            if (inif2[d].startsWith(while2[where2]) && inif2[d].endsWith("--")) {isincreasedecrease=1;}
            if (inif2[d].startsWith(while2[where2]) && inif2[d].endsWith("++")) {isincreasedecrease=2;}
        }
        if (iscontinue && isif == 0) {
            if (( symboleilbool1&&boolinteger1==false)||(symboleilbool1&&boolinteger2==false)||(boolinteger1==false&&boolinteger2==false)) {
                if (firstvaluestr.equals(secondvaluestr) && while2[2].equals("==")){run++;}
                if (!firstvaluestr.equals(secondvaluestr) && while2[2].equals("!=")){run++;}
            }
            while (firstvalueint == secondvalueint && while2[whereinthemiddle].equals("==") && firstvalueint + secondvalueint != 0) {
                run++;
                if(isincreasedecrease==1){firstvalueint--;}
                if(isincreasedecrease==2){firstvalueint++;}
            }
            while (firstvalueint != secondvalueint && while2[whereinthemiddle].equals("!=") && firstvalueint + secondvalueint != 0) {
                run++;
                if(isincreasedecrease==1){firstvalueint--;}
                if(isincreasedecrease==2){firstvalueint++;}
            }
            while (firstvalueint < secondvalueint && while2[whereinthemiddle].equals("<") && firstvalueint + secondvalueint != 0) {
                if(isincreasedecrease==1){firstvalueint--;}
                if(isincreasedecrease==2){firstvalueint++;}
                run++;
            }
            while (firstvalueint > secondvalueint && while2[whereinthemiddle].equals(">") && firstvalueint + secondvalueint != 0) {
                if(isincreasedecrease==1){firstvalueint--;}
                if(isincreasedecrease==2){firstvalueint++;}
                run++;
            }
            while (firstvalueint <= secondvalueint && while2[whereinthemiddle].equals("<=") && firstvalueint + secondvalueint != 0) {
                run++;
                if(isincreasedecrease==1){firstvalueint--;}
                if(isincreasedecrease==2){firstvalueint++;}
            }
            while (firstvalueint >= secondvalueint && while2[whereinthemiddle].equals(">=") && firstvalueint + secondvalueint != 0) {
                run++;
                if(isincreasedecrease==1){firstvalueint--;}
                if(isincreasedecrease==2){firstvalueint++;}
            }
            if(run==0) {iscontinue = false;}
        }
        int place2 = 0;
        if (isif==1 && inforisif!=0) {place2 = 13;}
        if (isif==0 && inforisif!=0) {place2 = 6;}
        if (isif==1 && inforisif==0) {place2 = 10;}
        if (isif==0 && inforisif==0) {place2 = 5; }
        String in1 = while2[place2];
        String[] inif3 = in1.split("[{;}]");
        while(run > 0 && iscontinue) {
            for (int c = 0; c < inif3.length; c++) {
                if (inif3[c].equals("moveright") && (zaidejas.b2body.getLinearVelocity().x <= 2)) {moveright();}
                else if (inif3[c].equals("moveleft") && (zaidejas.b2body.getLinearVelocity().x >= -2)) {moveleft();}
                else if (inif3[c].equals("jump")) {jump();}
                else if (inif3[c].equals("jumpright")) {jumpright();}
                else if (inif3[c].equals("jumpleft")) {jumpleft();}
                else if (inif3[c].equals("moverighttowall")) {moverighttowall();}
                else if (inif3[c].equals("movelefttowall")) {movelefttowall();}
                else if (inif3[c].equals("stop")) {stop();}
                else if (inif3[c].equals("ifstrikeinrightwallback")) {ifstrikeinrightwallback();}
                else if (inif3[c].equals("ifstrikeleftwallback")) {ifstrikeleftwallback();}
                if (inif3[c].startsWith("if") || inif3[c].startsWith(";if")) {
                    isif++;
                    ifassss.add(11 + c);
                    String ifass = "";
                    String ifassa = "if(";
                    int ifvietapab = 0;
                    for (int g = 0; g < while2.length; g++) {
                        if (while2[g].endsWith("}")) {ifvietapab = g;}
                    }
                    for (int d = 6; d < ifvietapab + 1; d++) {
                        ifassa += " " + while2[d];
                    }
                    ifas(ifassa, 0);
                }
            }
            run--;
            if (isincreasedecrease == 1) {firstintvalue.set(0, firstintvalue.get(0) - 1);}
            if (isincreasedecrease == 2) {firstintvalue.set(0, firstintvalue.get(0) + 1);}
        }
    }
    public static int isif = 0, inforisif =0, wasif = 0;
    public static String inifisfor=null, inifiswhile = null;

    void ifas(String a, int b) {
        boolean int1 = false, int2 = false, stringarchar = false, stringarchar2 = false, artesti = true;
        int vykdyk = 0, tolyn =0;
        String pirmareiksme = null, antrareiksme = null;
        Integer pirmareiksme2 = null;
        double antrareiksme2 = 0;
        isif = b;
        String ifas = a;
        String splitif[] = ifas.split(" ");
        for (int l = 0; l < splitif.length; l++) {
            if(splitif[l].equals("1")){
            }
        }
        int kuriojvietoj1=2, kuriojvietoj2 =4, kuriojvietojvidurio = 3;
        if (isif==0) {
            try {
                pirmareiksme2 = (Integer.parseInt(splitif[kuriojvietoj1]));
                if(pirmareiksme2!=0){int2=true;}
            } catch (NumberFormatException e) {

                for (int p = 0; p < firstcharkint.size(); p++) {
                    if(splitif[kuriojvietoj1].equals(firstcharkint.get(p)))
                    {
                        stringarchar=true;
                        pirmareiksme= String.valueOf(firstcharvalue.get(p));
                    }
                }

                for (int p = 0; p < firststringvalue.size(); p++) {
                    if(splitif[kuriojvietoj1].equals(firststringkint.get(p)))
                    {
                        stringarchar=true;
                        pirmareiksme=firststringvalue.get(p);
                    }
                }

                for (int p = 0; p < firstintkint.size(); p++) {
                    if(splitif[kuriojvietoj1].equals(firstintkint.get(p)))
                    {
                        pirmareiksme2=firstintvalue.get(p);
                        int1=true;
                    }
                }
                for (int p = 0; p < firstdoublekint.size(); p++) {
                    if(splitif[kuriojvietoj1].equals(firstdoublekint.get(p)))
                    {
                        pirmareiksme=firstdoublevalue.get(p).toString();
                        stringarchar=true;
                    }
                }


                for (int p = 0; p < firstboolvalue.size(); p++) {
                    if(splitif[kuriojvietoj1].equals(firstboolkint.get(p)))
                    {
                        stringarchar=true;
                        pirmareiksme=firstboolvalue.get(p);
                    }
                }
                if(stringarchar==false&&int1==false){pirmareiksme=splitif[kuriojvietoj1];}
            }
            try {
                antrareiksme2 = (Integer.parseInt(splitif[kuriojvietoj2]));
                if(antrareiksme2!=0){int2=true;}
            } catch (NumberFormatException e) {
                for (int p = 0; p < firstintkint.size(); p++) {
                    if(splitif[kuriojvietoj2].equals(firstintkint.get(p)))
                    {
                        int2=true;
                        antrareiksme2=firstintvalue.get(p);
                    }
                }
                for (int p = 0; p < firstdoublekint.size(); p++) {
                    if(splitif[kuriojvietoj2].equals(firstdoublekint.get(p)))
                    {
                        antrareiksme=firstdoublevalue.get(p).toString();
                        stringarchar2=true;
                    }
                }
                for (int p = 0; p < firstcharkint.size(); p++) {
                    if(splitif[kuriojvietoj2].equals(firstcharkint.get(p)))
                    {
                        stringarchar2=true;
                        antrareiksme= String.valueOf(firstcharvalue.get(p));
                    }
                }
                for (int p = 0; p < firststringvalue.size(); p++) {
                    if(splitif[kuriojvietoj2].equals(firststringkint.get(p)))
                    {
                        stringarchar2=true;
                        antrareiksme=firststringvalue.get(p);
                    }
                }
                for (int p = 0; p < firstboolvalue.size(); p++) {
                    if(splitif[kuriojvietoj2].equals(firstboolkint.get(p)))
                    {
                        stringarchar2=true;
                        antrareiksme=firstboolvalue.get(p);
                    }
                }
            }
            if(stringarchar2==false&&int2==false){antrareiksme=splitif[kuriojvietoj2];}
        }

        if (isif==1)
        {
            try {
                pirmareiksme2 = (Integer.parseInt(splitif[kuriojvietoj1]));
                if(pirmareiksme2!=0)
                {int2=true;}
            } catch (NumberFormatException e) {
                for (int l = 0; l < firstintkint.size(); l++) {
                    if(splitif[kuriojvietoj1].equals(firstintkint.get(l)))
                    {
                        int1=true;
                        pirmareiksme2=firstintvalue.get(l);
                    }
                }
                for (int l = 0; l < firstcharkint.size(); l++) {
                    if(splitif[kuriojvietoj1].equals(firstcharkint.get(l)))
                    {
                        stringarchar=true;
                        pirmareiksme= String.valueOf(firstcharvalue.get(l));
                    }
                }
                for (int l = 0; l < firststringvalue.size(); l++) {
                    if(splitif[kuriojvietoj1].equals(firststringkint.get(l)))
                    {
                        stringarchar=true;
                        pirmareiksme=firststringvalue.get(l);
                    }
                }
                for (int l = 0; l < firstboolvalue.size(); l++) {
                    if(splitif[kuriojvietoj1].equals(firstboolkint.get(l)))
                    {
                        stringarchar=true;
                        pirmareiksme=firstboolvalue.get(l);
                    }
                }
                for (int l = 0; l < firstdoublekint.size(); l++) {
                    if(splitif[kuriojvietoj1].equals(firstdoublekint.get(l)))
                    {
                        pirmareiksme=firstdoublevalue.get(l).toString();
                        stringarchar=true;
                    }
                }
                if(stringarchar==false&&int1==false)
                {  pirmareiksme=splitif[kuriojvietoj1]; }
            }
            try {
                antrareiksme2 = (Integer.parseInt(splitif[kuriojvietoj2]));
                if(antrareiksme2!=0)
                { int2=true; }
            }

            catch (NumberFormatException e) {
                for (int l = 0; l < firstintkint.size(); l++) {
                    if(splitif[kuriojvietoj2].equals(firstintkint.get(l)))
                    {
                        int2=true;
                        antrareiksme2=firstintvalue.get(l);
                    }
                }
                for (int l = 0; l < firstcharkint.size(); l++) {
                    if(splitif[kuriojvietoj2].equals(firstcharkint.get(l)))
                    {
                        stringarchar2=true;
                        antrareiksme= String.valueOf(firstcharvalue.get(l));
                    }
                }
                for (int l = 0; l < firststringvalue.size(); l++) {
                    if(splitif[kuriojvietoj2].equals(firststringkint.get(l)))
                    {
                        stringarchar=true;
                        antrareiksme=firststringvalue.get(l);
                    }
                }
                for (int l = 0; l < firstboolvalue.size(); l++) {
                    if(splitif[kuriojvietoj2].equals(firstboolkint.get(l)))
                    {
                        stringarchar2=true;
                        antrareiksme=firstboolvalue.get(l);
                    }
                }
                for (int l = 0; l < firstdoublekint.size(); l++) {
                    if(splitif[kuriojvietoj2].equals(firstdoublekint.get(l)))
                    {
                        antrareiksme=firstdoublevalue.get(l).toString();
                        stringarchar2=true;

                    }
                }
                if(stringarchar2==false&&int2==false){antrareiksme=splitif[kuriojvietoj2];}
            }
        }

        if (artesti&&isif==0) {

            if (( stringarchar&&int1==false)||(stringarchar&&int2==false)||(int1==false&&int2==false)) {
                if (pirmareiksme.equals(antrareiksme) && splitif[2].equals("=="))
                    vykdyk++;
                else if (!pirmareiksme.equals(antrareiksme) && splitif[2].equals("!="))
                    vykdyk++;
            }
            else if (pirmareiksme2 == antrareiksme2 && splitif[kuriojvietojvidurio].equals("==") && pirmareiksme2 + antrareiksme2 != 0) {
                vykdyk++;
            } else if (pirmareiksme2 != antrareiksme2 && splitif[kuriojvietojvidurio].equals("!=") && pirmareiksme2 + antrareiksme2 != 0) {
                vykdyk++;
            } else if (pirmareiksme2 < antrareiksme2 && splitif[kuriojvietojvidurio].equals("<") && pirmareiksme2 + antrareiksme2 != 0) {
                vykdyk++;
            } else if (pirmareiksme2 > antrareiksme2 && splitif[kuriojvietojvidurio].equals(">") && pirmareiksme2 + antrareiksme2 != 0) {
                vykdyk++;
            } else if (pirmareiksme2 <= antrareiksme2 && splitif[kuriojvietojvidurio].equals("<=") && pirmareiksme2 + antrareiksme2 != 0) {
                vykdyk++;
            } else if (pirmareiksme2 >= antrareiksme2 && splitif[kuriojvietojvidurio].equals(">=") && pirmareiksme2 + antrareiksme2 != 0) {
                vykdyk++;
            } else {
                artesti = false;
                vykdyk = 0;
            }
        }
        else if (artesti&&isif==1) {
            if(inforisif==0){kuriojvietojvidurio+=5;}
            if (pirmareiksme2 == antrareiksme2 && splitif[kuriojvietojvidurio].equals("==") && pirmareiksme2 + antrareiksme2 != 0) {vykdyk++;}
            else if (pirmareiksme2 != antrareiksme2 && splitif[kuriojvietojvidurio].equals("!=") && pirmareiksme2 + antrareiksme2 != 0) {vykdyk++;}
            else if (pirmareiksme2 < antrareiksme2 && splitif[kuriojvietojvidurio].equals("<") && pirmareiksme2 + antrareiksme2 != 0) {vykdyk++;}
            else if (pirmareiksme2 > antrareiksme2 && splitif[kuriojvietojvidurio].equals(">") && pirmareiksme2 + antrareiksme2 != 0) {vykdyk++;}
            else if (pirmareiksme2 <= antrareiksme2 && splitif[kuriojvietojvidurio].equals("<=") && pirmareiksme2 + antrareiksme2 != 0) {vykdyk++;}
            else if (pirmareiksme2 >= antrareiksme2 && splitif[kuriojvietojvidurio].equals(">=") && pirmareiksme2 + antrareiksme2 != 0) {vykdyk++;}
            else {
                artesti = false;
                vykdyk = 0;
            }

        }
        wasif=vykdyk;
        if (vykdyk==0){
            int vieta = 0;
            if (isif==1&&inforisif!=0) {vieta = 13;}
            if (isif==0&&inforisif!=0) {vieta = 6; }
            if (isif==1&&inforisif==0) { vieta = 10; }
            if (isif==0&&inforisif==0) { vieta = 5; }
            }
        if ( vykdyk != 0 )
        {
            int vieta = 0;
            if (isif==1&&inforisif!=0) {
                vieta = 13;
            }
            if (isif==0&&inforisif!=0) {
                vieta = 6;
            }
            if (isif==1&&inforisif==0) {
                vieta = 10;
            }
            if (isif==0&&inforisif==0) {
                vieta = 5;
            }
            String viduje = splitif[vieta];
            String[] ife = viduje.split("[{;}]");
            int kiek = ife.length;
            for (int l = 0; l < ife.length; l++) {
                if (ife[l].equals("moveright") && (zaidejas.b2body.getLinearVelocity().x <= 2)) {
                    moveright();
                } else if (ife[l].equals("moveleft") && (zaidejas.b2body.getLinearVelocity().x >= -2)) {
                    moveleft();
                } else if (ife[l].equals("jump")) {
                    jump();
                } else if (ife[l].equals("jumpright")) {
                    jumpright();
                } else if (ife[l].equals("jumpleft")) {
                    jumpleft();
                } else if (ife[l].equals("moverighttowall")) {
                    moverighttowall();
                } else if (ife[l].equals("movelefttowall")) {
                    movelefttowall();
                } else if (ife[l].equals("stop")) {
                    stop();
                } else if (ife[l].equals("ifstrikeinrightwallback")) {
                    ifstrikeinrightwallback();
                } else if (ife[l].equals("ifstrikeleftwallback")) {
                    ifstrikeleftwallback();
                }

            }


        }
        isif=0;
        inforisif=0;
        wasif=vykdyk;
    }

    void ifstrikeinrightwallback() {
        if(WorldContact.collids!=0){
            for(int i = 0; i < 6; i++){
                moveleft();
            }
            WorldContact.collids=0;
        }
    }
    void ifstrikeleftwallback() {
        if(WorldContact.collids!=0){
            for(int i = 0; i < 6; i++){
                moveright();
            }
            WorldContact.collids=0;
        }
    }
    void stop() {
        try {Thread.sleep(100);} catch (InterruptedException e) {e.printStackTrace();}
    }

    public static int isfor = 0;
    public void recognizefor(String zodis)
    {
        String splitfor[] = zodis.split("[ (),]");
        int firstvalue1 = 0;
        int secondvalue1 = 0;
        int run = 0;
        boolean artesti=true;
        boolean int1 = false;
        boolean int2 = false;
        String firstvalue = null;
        String secondvalue = null;
        try {firstvalue1=(Integer.parseInt(splitfor[4]));  //4
        }
        catch(NumberFormatException e)
        {

            for (int a = 0; a < firstintkint.size(); a++) {
                if(splitfor[4].equals(firstintkint.get(a)))
                {
                    int1=true;
                    secondvalue1=firstintvalue.get(a);
                }
            }
            if(!int1) {
                firstvalue=splitfor[4];
            }
        }
        try {secondvalue1=(Integer.parseInt(splitfor[8]));
        }
        catch(NumberFormatException e)
        {
            for (int a = 0; a < firstintkint.size(); a++) {
                if(splitfor[8].equals(firstintkint.get(a)))
                {
                    int2=true;
                    secondvalue1=firstintvalue.get(a);
                }
            }
            if(!int2) {secondvalue=splitfor[8];}
        }
        int isif1 = 0; //yraif
        int iselse = 0;  //yraelse

        ArrayList<Integer> if1 = new ArrayList<Integer>();  //if
        ArrayList<Integer> else1 = new ArrayList<Integer>();  //else
        ArrayList<Integer> for1 = new ArrayList<Integer>();       //for
        ArrayList<Integer> parentheses = new ArrayList<Integer>();  //skliaustai
        for (int a = 0; a <splitfor.length ; a++) {
            if(splitfor[a].endsWith("}")) {
                parentheses.add(a);
            }
        }
        String in = null;
        int where = 0;
        if(isfor == 0){in = splitfor[11]; //11
        }
        else{
            { for (int b = 0; b <splitfor.length ; b++) {
                if (splitfor[b].startsWith("{")&&splitfor[b-1].endsWith("++")) {
                    {in = splitfor[b];}
                }}}
        }
        {
            String[] infor = in.split("[{;}]");
            int count = infor.length;
            for (int h = 0; h < count; h++) {
                if (infor[h].equals("if")) {
                    isif1++;
                    if1.add(11 + h);
                    inforisif++;
                }
                if (infor[h].equals("else")) {
                    iselse++;
                    else1.add(11 + h);
                }
                if (infor[h].startsWith("for")) {
                    isfor++;
                    if(h!=0){
                        for1.add(11 + h);
                        kur=11+h;}
                    else {for1.add(11);
                        kur=11;}
                }
                if (infor[h].equals("moveright")) {

                    for (int p = firstvalue1; p < secondvalue1; p++) {
                        moveright();
                    }
                } else if (infor[h].equals("moveleft")) {
                    for (int p = firstvalue1; p < secondvalue1; p++) {
                        moveleft();
                    }

                } else if (infor[h].equals("jump")) {
                    for (int p = firstvalue1; p < secondvalue1; p++) {
                        jump();
                    }
                } else if (infor[h].equals("jumpright")) {
                    for (int p = firstvalue1; p < secondvalue1; p++) {
                        jumpright();
                    }

                } else if (infor[h].equals("jumpleft")) {
                    for (int p = firstvalue1; p < secondvalue1; p++) {
                        jumpleft();
                    }
                } else if (infor[h].equals("moverighttowall")) {
                    for (int p = firstvalue1; p < secondvalue1; p++) {
                        moverighttowall();
                    }
                } else if (infor[h].equals("movelefttowall")) {
                    for (int p = firstvalue1; p < secondvalue1; p++) {
                        movelefttowall();
                    }
                } else if (infor[h].equals("stop")) {
                    for (int p = firstvalue1; p < secondvalue1; p++) {
                        stop();
                    }
                } else if (infor[h].equals("ifstrikeinrightwallback")) {
                    for (int p = firstvalue1; p < secondvalue1; p++) {
                        ifstrikeinrightwallback();
                    }
                } else if (infor[h].equals("ifstrikeleftwallback")) {
                    for (int p= firstvalue1; p < secondvalue1; p++) {
                        ifstrikeleftwallback();
                    }}
                String inforiswhile = "while( ";
                if(infor[h].startsWith("while")) {
                    int where1 =0;
                    for (int k = 0; k < splitfor.length; k++) {
                        if(splitfor[k].contains("while")){
                            where1 = k;}
                    }
                    for (int k = where1+1; k < splitfor.length; k++) {
                        if(k==where1+1){inforiswhile+=splitfor[k];}
                        else if(k==where1+2){inforiswhile+=splitfor[k]+" ";}
                        else if(k==where+3){inforiswhile+=splitfor[k]+" ";}
                        else if(k == where1+4){inforiswhile+=splitfor[k]+" ";}
                        else if(k == where1+5){inforiswhile+=splitfor[k]+") ";}
                        else if(k == where1+6){inforiswhile+=splitfor[k];}
                        else{inforiswhile+=splitfor[k]+" ";}
                    }

                    for (int p = firstvalue1; p < secondvalue1; p++) {
                        recognizewhile(inforiswhile);
                    }
                }}
        }
        if(isif1!=0) {
            String if5 = "";
            for (int g = if1.get(0); g < splitfor.length; g++) {
                if5 += " "+splitfor[g];
            }
            for (int p = firstvalue1; p <secondvalue1; p++) {
                inforisif = 1;
                ifas(if5,0);
            }
            isif1--;
        }

        if(isfor!=0) {
            String forasss = "";
            for (int d = kur-1; d < splitfor.length; d++) {
                if(d!=-1){
                    forasss += " "+splitfor[d];
                }
            }
            for (int p = firstvalue1; p <secondvalue1; p++) {
                recognizefor(forasss);
            }
            isfor--;
        }
    }
    public void moveright()
    {
        zaidejas.b2body.applyLinearImpulse(new Vector2(0.1f, 0), zaidejas.b2body.getWorldCenter(), true);
        walks++;
    }
    public void moveleft()
    {
        zaidejas.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), zaidejas.b2body.getWorldCenter(), true);
        walks++;
    }
    public void jump()
    {
        if(istheend){
            try {Thread.sleep(1*walks);
            } catch (InterruptedException e) {e.printStackTrace();}}
        else{try {Thread.sleep(50*walks);
        } catch (InterruptedException e) {e.printStackTrace();}}
        walks = 0;
        if(zaidejas.b2body.getLinearVelocity().y == 0){
            if(b == true) {
                zaidejas.b2body.applyLinearImpulse(new Vector2(0, 4f), zaidejas.b2body.getWorldCenter(), true);
            }
        }

        else if(zaidejas.b2body.getLinearVelocity().y != 0){
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            zaidejas.b2body.applyLinearImpulse(new Vector2(0, 4f), zaidejas.b2body.getWorldCenter(), true);
        }
    }
    public void jumpright() {
        try {Thread.sleep(50 * walks);} catch (InterruptedException e) {e.printStackTrace();}
        walks = 0;
        if (zaidejas.b2body.getLinearVelocity().y == 0) {
            zaidejas.b2body.applyLinearImpulse(new Vector2(1f, 0), zaidejas.b2body.getWorldCenter(), true);
            zaidejas.b2body.applyLinearImpulse(new Vector2(0, 4f), zaidejas.b2body.getWorldCenter(), true);
        }
        else if (zaidejas.b2body.getLinearVelocity().y != 0) {
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            zaidejas.b2body.applyLinearImpulse(new Vector2(1f, 0), zaidejas.b2body.getWorldCenter(), true);
            zaidejas.b2body.applyLinearImpulse(new Vector2(0, 4f), zaidejas.b2body.getWorldCenter(), true);
        }
    }
    boolean istheend = false;
    public void jumpleft()
    {
        try {Thread.sleep(50*walks);} catch (InterruptedException e) {e.printStackTrace();}
        if(zaidejas.b2body.getLinearVelocity().y == 0) {
            zaidejas.b2body.applyLinearImpulse(new Vector2(-1f, 0), zaidejas.b2body.getWorldCenter(), true);
            zaidejas.b2body.applyLinearImpulse(new Vector2(0, 4f), zaidejas.b2body.getWorldCenter(), true);
        }
        else if(zaidejas.b2body.getLinearVelocity().y != 0){
            try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}
            zaidejas.b2body.applyLinearImpulse(new Vector2(-1f, 0), zaidejas.b2body.getWorldCenter(), true);
            zaidejas.b2body.applyLinearImpulse(new Vector2(0, 4f), zaidejas.b2body.getWorldCenter(), true);
        }
    }
    public void moverighttowall()
    {
        istheend = true;

        try {Thread.sleep(walks);} catch (InterruptedException e) {e.printStackTrace();}
        while(WorldContact.collids==0)
        {
            try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
            if((zaidejas.b2body.getLinearVelocity().x <= 2)){
                walks++;
                zaidejas.b2body.applyLinearImpulse(new Vector2(0.1f, 0), zaidejas.b2body.getWorldCenter(), true);}
        }
    }
    public void movelefttowall()
    {
        istheend = true;
        try {Thread.sleep(walks);} catch (InterruptedException e) {e.printStackTrace();}
        while(WorldContact.collids == 0)
        {
            try {Thread.sleep(10);} catch (InterruptedException e) {e.printStackTrace();}
            if((zaidejas.b2body.getLinearVelocity().x >= -2)){
                walks++;
                zaidejas.b2body.applyLinearImpulse(new Vector2(-0.1f, 0), zaidejas.b2body.getWorldCenter(), true);}
        }
    }

    void recognizeint(String zodis) {
        String convertstrtoint = null;
        int reiksme = 0;
        String int2 = "";
        String int3[] = zodis.split("[ ;'']");
        for (int c = 0; c < int3.length ; c++) {
            if(int3[c].startsWith("int")) {
                firstintkint.add(int3[c+1]);
                try {reiksme = (Integer.parseInt(int3[c+3]));} catch (NumberFormatException e) {}
                firstintvalue.add(reiksme);
                int2+="int"+" "+int3[c+1]+" "+"="+" "+int3[c+3]+";";
            }
            convertstrtoint = zodis.replace(int2,"");
        }
        input(convertstrtoint);
    }

    void recognizedouble(String zodis) {
        String convertstrtodouble = null;
        String double2 = "";
        String double3[] = zodis.split("[ ;]");
        double reiksme = 4;
        for (int c = 0; c < double3.length ; c++) {
            if(double3[c].startsWith("double")) {
                firstdoublekint.add(double3[c+1]);
                try {reiksme = (Double.parseDouble(double3[c+3]));} catch (NumberFormatException e) {}
                firstdoublevalue.add(reiksme);
                double2+="double"+" "+double3[c+1]+" "+"="+" "+double3[c+3]+";";
            }
            convertstrtodouble=zodis.replace(double2,"");
        }
        input(convertstrtodouble);
    }
    void recognizebool(String zodis) {
        String convertoldstrtostr = null;
        String bool1 = "";
        String string2[] = zodis.split("[ ;]");
        for (int c = 0; c < string2.length ; c++) {
            if(string2[c].startsWith("boolean")) {
                firstboolkint.add(string2[c+1]);
                firstboolvalue.add(string2[c+3]);
                bool1+="boolean"+" "+string2[c+1]+" "+"="+" "+string2[c+3]+";";
            }
            convertoldstrtostr=zodis.replace(bool1,"");
        }
        input(convertoldstrtostr);
    }
    void recognizestring(String zodis) {
        String convertoldstrtostr = null;
        String string1 = "";
        String string3[] = zodis.split("[ ;]");
        for (int c = 0; c < string3.length ; c++) {
            if(string3[c].startsWith("String")) {
                firststringkint.add(string3[c+1]);
                firststringvalue.add(string3[c+3]);
                string1+="String"+" "+string3[c+1]+" "+"="+" "+string3[c+3]+";";
            }
            convertoldstrtostr=zodis.replace(string1,"");
        }
        input(convertoldstrtostr);
    }
    void recognizechar(String zodis) {
        String convertoldstrtostr = null;
        String char1 = "";
        String char3[] = zodis.split("[ ;'']");
        for (int c = 0; c < char3.length ; c++) {
            if(char3[c].startsWith("char")) {
                firstcharkint.add(char3[c+1]);
                firstcharvalue.add(char3[c+4].toCharArray());
                char1+="char"+" "+char3[c+1]+" "+"="+" '"+char3[c+4]+"';";
            }
            convertoldstrtostr=zodis.replace(char1,"");
        }
        input(convertoldstrtostr);
    }


}