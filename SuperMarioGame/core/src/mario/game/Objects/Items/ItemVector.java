package mario.game.Objects.Items;

import com.badlogic.gdx.math.Vector2;

public class ItemVector {
    public Vector2 position;
    public Class<?> type;

    public ItemVector(Vector2 position, Class<?> type)
    {
        this.position = position;
        this.type = type;
       // System.out.println(position);
    }
}