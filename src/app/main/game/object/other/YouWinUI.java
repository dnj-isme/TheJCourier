package app.main.game.object.other;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.controller.asset.FontManager;
import app.main.controller.audio.AudioFactory;
import app.utility.Utility;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.RenderProperties;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

public class YouWinUI extends GameObject {
    private Font titleFont;
    private Font textFont;
    private boolean easterEgg;
    private Image sprite;

    public YouWinUI(GameScene owner) {
        super(owner);
        setLayer(ObjectLayer.UI);
        visible = false;
        titleFont = FontManager.loadFont(16);
        textFont = FontManager.loadFont(10);
        easterEgg = Utility.chance(GameController.getInstance().isDebug() ? 50 : 1);
        sprite = AssetManager.getInstance().findImage("frame");
        setSize(300, 100);
        setPosition((GameScene.WIDTH - getSize().getX()) / 2, (GameScene.HEIGHT - getSize().getY()) / 2);
    }

    private boolean visible = false;

    public boolean isVisible() {
        return visible;
    }

    public void hide() {
        visible = false;
    }

    public void show() {
        if(!visible) {
            visible = true;
            AudioFactory.createSfxHandler(AssetManager.getInstance().findAudio(easterEgg ? "easter_egg" : "you_win")).playThenDestroy();
        }
    }

    @Override
    public void render(RenderProperties properties) {
        if(visible) {
            GraphicsContext context = properties.getContext();
            context.drawImage(sprite, getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());

            context.setFill(Color.WHITE);
            context.setTextAlign(TextAlignment.CENTER);
            double xCenter = GameScene.WIDTH / 2;

            // 1. Title 1
            double yPos = getPosition().getY() + 30;
            context.setFont(titleFont);
            String text = easterEgg ? "Mission Passed:" : "You Win!";
            context.fillText(text, xCenter, yPos);

            // 2. Title 2 (if Easter Egg)
            if(easterEgg) {
                yPos += 20;
                context.fillText("Respect +", xCenter, yPos);
            }

            // 3. Time Passed
            yPos += 20;
            context.setFont(textFont);
            context.fillText(getOwner().getTimeSpentFormat(), xCenter, yPos);

            // 4. Press ENTER
            yPos += 16;
            context.fillText("Press ENTER to Continue...", xCenter, yPos);
            context.setTextAlign(TextAlignment.LEFT);
        }
    }
}
