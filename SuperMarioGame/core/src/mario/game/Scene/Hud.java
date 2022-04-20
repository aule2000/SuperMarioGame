package mario.game.Scene;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import mario.game.Main;
import mario.game.Worlds.MainCodeProgram;

public class Hud implements Disposable {
    private static int score = 0;
    private static Integer Score_total;
     private Integer worldTimer;
     private float timer;
    Label Timercountshow;
    Label timeshow;
    Label levelnumbershow;
    public Stage stage;
    private Viewport viewport;
    Label levelshow;
    private static Label scoreShow;
    Label marioshowname;

    public Hud(SpriteBatch sb)
    {
        worldTimer = 1000;              //kiek sekundziu zaidimas tesis
        timer = 0;
        Score_total = 0;
        viewport = new FitViewport(Main.Width, Main.Height, new OrthographicCamera());
        stage = new Stage(viewport, sb);
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        Timercountshow = new Label(String.format("%03d", worldTimer), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        scoreShow = new Label(String.format("%06d", Score_total+score), new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        timeshow = new Label("TIME LEFT", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        if(MainCodeProgram.kurislevel == 1)
            levelnumbershow = new Label("1", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        if(MainCodeProgram.kurislevel == 2)
            levelnumbershow = new Label("2", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        if(MainCodeProgram.kurislevel == 3)
            levelnumbershow = new Label("3", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        levelshow = new Label("LEVEL", new Label.LabelStyle(new BitmapFont(), Color.BLUE));
        marioshowname = new Label("MARIO", new Label.LabelStyle(new BitmapFont(), Color.BLUE));

        table.add(marioshowname).expandX().padTop(10);
        table.add(levelshow).expandX().padTop(10);
        table.add(timeshow).expandX().padTop(10);
        table.row();
        table.add(scoreShow).expandX();
        table.add(levelnumbershow).expandX();
        table.add(timeshow).expandX();
        stage.addActor(table);
    }
    public static void countScore(int value)
    {
        score += value;
        Score_total += value;
        scoreShow.setText(String.format("%06d", Score_total));
    }

    public void refresh (float dt)  //update
    {
        timer += dt;
        if(timer>=1)
        {
            worldTimer--;
            timeshow.setText(String.format("%03d",worldTimer));
            timer = 0;
        }
        if(worldTimer==0) {
            MainCodeProgram.kurislevel = 5;
        }
    }
    public  static int getScore(){return Score_total;}
    @Override
    public void dispose() {stage.dispose();}
}