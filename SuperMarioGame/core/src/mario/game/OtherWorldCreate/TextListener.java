package mario.game.OtherWorldCreate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

public class TextListener implements Input.TextInputListener {

    String text;

    public void create() {

    }

    public void render() {
    }

    @Override
    public void input(String text) {
        this.text = text;
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            Gdx.input.getTextInput(this, "Title", "Default text", "Hint");
        }
        Gdx.app.log("Command console",text);
    }

    @Override
    public void canceled() {
        text = "cancelled";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}