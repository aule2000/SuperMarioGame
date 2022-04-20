package mario.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.awt.SystemTray;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import mario.game.OtherWorldCreate.GetFromAndroidApp;
import mario.game.Worlds.MainCodeProgram;
import mario.game.Worlds.inputs;

public class Main extends Game {

    public SpriteBatch spriteBatch;
    public static int Width = 400;
    public static int Height = 208;
    public static final short objectwhit = 32;
    public static final short mariowhit= 2;
    public static final short itemwhit = 256;
    public static float PPM = 100;
    public static final short marioheadwhit = 512;
    public static final short blockwhit = 4;
    public static final short coinwhit = 8;
    public static final short nothing = 0;
    public static final short ground = 1;
    public static final short destroyedwhit = 16;
    public static final short enemywhit = 64;
    public static final short enemyheadwhit = 128;

    @Override
    public void create () {
        Thread t = new Thread(new GetFromAndroidApp());
        t.start();
        spriteBatch = new SpriteBatch();
        setScreen(new MainCodeProgram(this,null,spriteBatch));
    }


    @Override
    public void dispose() {
        super.dispose();
        spriteBatch.dispose();
    }


}