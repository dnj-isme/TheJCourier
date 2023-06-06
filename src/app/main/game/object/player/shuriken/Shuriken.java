package app.main.game.object.player.shuriken;

import app.main.controller.GameController;
import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.utility.Utility;
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

public class Shuriken extends GameObject implements Collidable, Updatable {

  private Vector2 direction;
  private int speed = 800;
  private static final Vector2 SIZE = new Vector2(16, 16);
  private Image sprite;
  private Vector2 imageSize;
  private GameController controller;
  private AssetManager asset;

  private boolean enabled;
  private boolean initialized;

  public Shuriken(GameScene owner) {
    super(owner);
    setPosition(new Vector2());
    direction = new Vector2();
    enabled = false;
    initialized = false;
    setSize(SIZE);
    setLayer(ObjectLayer.VFX);

    asset = AssetManager.getInstance();
    sprite = asset.findImage("player_shuriken");
    imageSize = new Vector2(20, 20);
    enabled = false;
    initialized = false;
    controller = GameController.getInstance();
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public boolean isInitialized() {
    return initialized;
  }

  public void setInitialized(boolean initialized) {
    this.initialized = initialized;
  }

  public void initialize(Player player) {
    direction = player.getFacing().copy();
    setPosition(player.getPosition().copy());
    if (direction.equals(Vector2.LEFT())) {
      getPosition().subsX(player.getSize().getX() + SIZE.getX());
    }

    initialized = true;
  }

  public void start() {
    if (enabled) {
      Utility.debug("Shuriken is already started");
    } else if (initialized) {
      AudioFactory.createSfxHandler(asset.findAudio("sfx_shuriken_throw")).playThenDestroy();
      ;
      enabled = true;
    } else {
      Utility.debug("Shuriken isn't initialized yet!");
    }
  }

  public void stop() {
    if (!enabled) {
      Utility.debug("Shuriken isn't starting, so no need to stop it");
    } else {
      AudioFactory.createSfxHandler(asset.findAudio("sfx_shuriken_stick")).playThenDestroy();
      ;
      enabled = false;
      initialized = false;
      setPosition(new Vector2());
      direction = new Vector2();
    }
  }

  private double cd = 2;

  @Override
  public void render(RenderProperties properties) {
    if (!enabled)
      return;
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderCenter(getPosition(), getSize(), imageSize);

    if (controller.isHitbox()) {
      context.setFill(Color.LIGHTBLUE);
      context.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
    }

    int ticks = (int) (properties.getFrameCount() / cd);

    int xPos = ticks % 4 * 20;

    context.drawImage(sprite, xPos, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(),
        imageSize.getX(), imageSize.getY());
  }

  @Override
  public void receiveCollision(GameObject object) {
    // TODO Auto-generated method stub

  }

  @Override
  public void update(RenderProperties properties) {
    if (!enabled)
      return;
    Vector2 pos = getPosition();
    if (pos.getX() <= 0 || pos.getX() >= GameScene.WIDTH - getSize().getX()) {
      stop();
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    if (!enabled)
      return;
    getPosition().setAdd(direction.mult(speed * properties.getFixedDeltaTime()));
  }
}
