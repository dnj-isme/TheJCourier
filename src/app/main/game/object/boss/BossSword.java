package app.main.game.object.boss;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.utility.Utility;
import app.utility.canvas.Collidable;
import app.utility.canvas.GameObject;
import app.utility.canvas.GameScene;
import app.utility.canvas.ObjectLayer;
import app.utility.canvas.ObjectTag;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.util.function.IntToDoubleFunction;

public class BossSword extends GameObject implements Updatable, Collidable {

  private final Image sprite;
  private final Vector2 imageSize;

  private Vector2 destination;
  private boolean started = false;
  private Vector2 direction;

  public BossSword(GameScene owner) {
    super(owner);
    setLayer(ObjectLayer.VFX);
    setTag(ObjectTag.Enemy);
    setSize(35, 35);

    AssetManager assets = AssetManager.getInstance();
    imageSize = new Vector2(50, 50);
    sprite = assets.findImage("sword_throw");
    reset(owner);
  }

  public void reset(GameScene owner) {
    setOwner(owner);
    started = false;
    destination = Vector2.ZERO();
    direction = Vector2.LEFT();
    setPosition(Vector2.ZERO());
  }

  public void setDestination(Vector2 target) {
    this.destination = target.copy();
    direction = target.minus(getPosition()).getNormalized();
  }

  public void setDestination(double x, double y) {
    setDestination(new Vector2(x, y));
  }

  public void startAnimation() {
    started = true;
  }

  @Override
  public void render(RenderProperties properties) {
    if (!started)
      return;

    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderCenter(getPosition(), getSize(), imageSize);

    double cd = 2.5;
    int index = (int) (properties.getFrameCount() / cd) % 8;

    double posX = imageSize.getX() * index;

    if(GameController.getInstance().isHitbox()) {
      context.setFill(Color.RED);
      context.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
    }
    
    context.drawImage(sprite, posX, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(),
        imageSize.getX(), imageSize.getY());
  }

  @Override
  public void receiveCollision(GameObject object) {

  }

  @Override
  public void update(RenderProperties properties) {
    if (started) {

      if (reachesDestination() && !getPosition().equals(destination)) {
        setPosition(destination.copy());
        direction = Vector2.ZERO();
      }
    }
  }

  public boolean reachesDestination() {
    return Vector2.distance(getPosition(), destination) <= 20;
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    if(!started) return;
    double speed = 800;
    Vector2 movement = direction.getNormalized().mult(speed * properties.getFixedDeltaTime());
    setPosition(Utility.clamp(getPosition().getX() + movement.getX(), 0, GameScene.WIDTH - getSize().getX()),
        Utility.clamp(getPosition().getY() + movement.getY(), 0, GameScene.HEIGHT - getSize().getY() - 20));
  }

  public Object getDestination() {
    return  destination;
  }
}
