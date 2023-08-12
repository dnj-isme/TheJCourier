package app.main.game.object.other;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.utility.canvas.*;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Statue2 extends GameObject {
    private Image sprite;
    private boolean reversed = false;
    private GameController controller;
    private static final Vector2 imageSize = new Vector2(80, 100);
    public Statue2(GameScene owner, double posX, double posY, boolean reversed) {
        super(owner);
        sprite = AssetManager.getInstance().findImage("statue2");
        setLayer(ObjectLayer.Foreground);
        setPosition(posX, posY);
        setSize(imageSize.getX(), imageSize.getY());
        controller = GameController.getInstance();
        this.reversed = reversed;
    }

    private double cd = 8;
    @Override
    public void render(RenderProperties properties) {
        GraphicsContext context = properties.getContext();
        Vector2 renderPos = Vector2.renderCenter(getPosition(), getSize(), imageSize);

        int ticks = (int) (properties.getFrameCount() / cd);
        int spriteX = (int) (ticks % 4 * imageSize.getX());

        // if the object is reversed, offset the render position and negate the width
        if (reversed) {
            renderPos.setX(renderPos.getX() + imageSize.getX());
        }

        // use a negative width to draw the image reversed
        double width = reversed ? -imageSize.getX() : imageSize.getX();

        context.drawImage(sprite, spriteX, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(),
                renderPos.getY(), width, getSize().getY());
    }

}
