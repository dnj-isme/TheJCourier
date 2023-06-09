package app.main.game.object.other;

import app.main.controller.asset.AssetManager;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.RenderProperties;
import javafx.scene.image.Image;

public class FloorTile extends GameObject{

  private Image sprite;
  
  public FloorTile(GameScene owner) {
    super(owner);
    setLayer(ObjectLayer.Foreground);
    AssetManager assetManager = AssetManager.getInstance();
    sprite = assetManager.findImage("floor_tile");
  }

  @Override
  public void render(RenderProperties properties) {
    properties.getContext().drawImage(sprite, getPosition().getX(), getPosition().getY());
  }
}
