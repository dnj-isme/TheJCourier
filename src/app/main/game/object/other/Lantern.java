package app.main.game.object.other;

import app.main.controller.GameController;
import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Lantern extends GameObject implements Collidable, Updatable {

  private static Vector2 SIZE = new Vector2(32, 32);
  private Image sprite;
  private Vector2 imageSize;
  private GameController controller;
  
  public Lantern(GameScene owner) {
    super(owner);

    AssetManager asset = AssetManager.getInstance();
    sprite = asset.findImage("lantern");
    imageSize = new Vector2(40, 40);
    setSize(SIZE);
    setLayer(ObjectLayer.Block);
    controller = GameController.getInstance();
  }

  int cd = 4;

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderCenter(getPosition(), getSize(), imageSize);

    int index = (int) (properties.getFrameCount() / cd) % 4;

    int spriteX = index * 40;

    context.setFill(Color.RED);
    context.fillRect(renderPos.getX(), renderPos.getY(), getSize().getX(), getSize().getY());

    context.drawImage(sprite, spriteX, 40, imageSize.getX(), imageSize.getY(), renderPos.getX(),
        renderPos.getY(), getSize().getX(), getSize().getY());
  }

  @Override
  public void receiveCollision(GameObject object) {
    // TODO Auto-generated method stub

  }

  @Override
  public void update(RenderProperties properties) {
    // TODO Auto-generated method stub

  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub

  }

}
