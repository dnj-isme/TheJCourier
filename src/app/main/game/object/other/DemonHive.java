package app.main.game.object.other;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.game.object.Hittable;
import app.main.game.object.player.Player;
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

public class DemonHive extends GameObject implements Collidable, Updatable, Hittable {

  private DemonHiveController controller;
  private Image sprite;
  private Vector2 imageSize;
  private AssetManager assetManager;
  private GameController gameController;

  private boolean visible = false;
  private boolean trigger = false;

  public DemonHive(GameScene owner, DemonHiveController controller) {
    super(owner);
    this.controller = controller;
    visible = false;
    setLayer(ObjectLayer.Background);
    setSize(40, 40);

    gameController = GameController.getInstance();
    assetManager = AssetManager.getInstance();
    sprite = assetManager.findImage("demon_hive");
    imageSize = new Vector2(160, 160);
  }

  private long startAnim = -1;

  private double cd = 2;

  @Override
  public void render(RenderProperties properties) {
    // Row: 2
    // Col: 6
    // Appear: 1-6 (top)
    // Dissapear (2 bottom, 6-1 top)
    GraphicsContext context = properties.getContext();

    if (gameController.isHitbox() && visible) {
      context.setFill(Color.ORANGERED);
      context.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
    }

    int index = (int) ((properties.getFrameCount() - startAnim) / cd);
    index = visible ? Math.min(index, 5) : 7 - Math.min(index, 7);

    double posX = (index % 6) * imageSize.getX();
    double posY = index > 5 ? imageSize.getY() : 0;
    
    context.drawImage(sprite, posX, posY, imageSize.getX(), imageSize.getY(), getPosition().getX(),
        getPosition().getY(), getSize().getX(), getSize().getY());
  }

  @Override
  public void update(RenderProperties properties) {
    // TODO Auto-generated method stub
    if (trigger) {
      startAnim = properties.getFrameCount();
      trigger = false;
      visible = !visible;
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub

  }

  public boolean isVisible() {
    return visible;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  @Override
  public void receiveCollision(GameObject object) {
    if(object instanceof Player) {      
      controller.receiveHit(((Player) object).getAttackCount());
    }
  }

  public void updateState() {
    trigger = true;
  }
}
