package app.main.game.object.other;

import app.main.controller.asset.AssetManager;
import app.utility.canvas.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Foreground1 extends GameObject {
    private Image sprite;
    private Vector2 imageSize = new Vector2(80, 60);

    public Foreground1(GameScene owner, double posX, double posY) {
        super(owner);
        setLayer(ObjectLayer.Frontground);
        setPosition(posX, posY);
        setSize(imageSize.copy());
        sprite = AssetManager.getInstance().findImage("foreground1");
    }

    @Override
    public void render(RenderProperties properties) {
        GraphicsContext context = properties.getContext();
        Vector2 renderPos = Vector2.renderCenter(getPosition(), getSize(), getSize());
        context.drawImage(sprite, 0, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(),
                renderPos.getY(), getSize().getX(), getSize().getY());
    }
}
