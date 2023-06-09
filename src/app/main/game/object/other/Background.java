package app.main.game.object.other;

import app.main.controller.asset.AssetManager;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import javafx.scene.image.Image;

public class Background extends GameObject{

  private Image sprite;
  
  public Background(GameScene owner) {
    super(owner);
    sprite = AssetManager.getInstance().findImage("background");
  }

  @Override
  public void render(RenderProperties properties) {
    properties.getContext().drawImage(sprite, 0, 0, 640, 400, 0, 0, GameScene.WIDTH, GameScene.HEIGHT);
  }
}
