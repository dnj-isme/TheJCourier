package app.main.game.object.other;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.utility.canvas.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Throne extends GameObject {
    private Image sprite;
    private GameController controller;
    private static final Vector2 imageSize = new Vector2(120, 120);
    public Throne(GameScene owner, double posX, double posY) {
        super(owner);
        sprite = AssetManager.getInstance().findImage("throne");
        setLayer(ObjectLayer.Foreground);
        setPosition(posX, posY);
        setSize(imageSize.getX(), imageSize.getY());
        controller = GameController.getInstance();
    }

    private double cd = 8;
    @Override
    public void render(RenderProperties properties) {
        GraphicsContext context = properties.getContext();
        Vector2 renderPos = Vector2.renderCenter(getPosition(), getSize(), imageSize);

        int ticks = (int) (properties.getFrameCount() / cd);
        int spriteX = (int) (ticks % 4 * imageSize.getX());

        context.drawImage(sprite, spriteX, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(),
                renderPos.getY(), getSize().getX(), getSize().getY());
    }
}
