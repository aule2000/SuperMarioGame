package mario.game.Worlds;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextArea;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import mario.game.Main;

public class inputs extends Game implements Disposable {
    private Skin skin;
    private Viewport viewport;
    public Stage stage;
    private Table table;
    private TextButton button;
    public TextArea TF;
    private Main zaidimas;
    public SpriteBatch spriteBatch;
    private int i = 0;


    private static ServerSocket ss;
    private static Socket s;
    private static InputStreamReader isr;
    private static BufferedReader br;
    private static String message = "";

   public inputs(){

   }
    public inputs(final SpriteBatch sb, final Main zaidimas){

        viewport = new FitViewport(1200, 624, new OrthographicCamera());
        stage = new Stage(viewport, sb);

        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        button = new TextButton("Send", skin, "default");
        TF = new TextArea("", skin);

        TF.setWidth(300);
        TF.setHeight(570);
        TF.setPosition(0,54);

        button.setWidth(300);
        button.setHeight(54);
        button.setPosition(0, 0);

        try {
            while(true) {
                ss = new ServerSocket(5000);
                System.out.println("Server running at port 5000");
                s = ss.accept();
                isr = new InputStreamReader(s.getInputStream());
                br = new BufferedReader(isr);
                message = br.readLine();
                System.out.println(message);

                isr.close();
                br.close();
                ss.close();
                s.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



        button.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {

                        zaidimas.setScreen(new MainCodeProgram(zaidimas,TF.getText(),zaidimas.spriteBatch));
                        TF.setText("");

                button.setTouchable(Touchable.enabled);
            }
        });
        stage.addActor(TF);
        stage.addActor(button);
        Gdx.input.setInputProcessor(stage);
        Gdx.input.setInputProcessor(stage);
    }



    public void setButtonTouchable(){
        button.setTouchable(Touchable.enabled);
    }

    @Override
    public void create() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
