package app.main.game.object.player.swing;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.game.object.other.DemonHive;
import app.main.game.object.player.Player;
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

public class PlayerGlideSwing extends GameObject implements Collidable, Updatable {

  public static final Vector2 SIZE = new Vector2(20, 50);

  // 256 x 52
  // 4 x 2

  // Note:
  // 1st row: normal
  // 2nd row: reversed

  private Player player;

  private Image sprite;
  private Vector2 imageSize;

  private double startFrame;
  private GameController controller;

  public PlayerGlideSwing(GameScene owner, Player player) {
    super(owner);
    setLayer(ObjectLayer.VFX);
    setTag(ObjectTag.Player);

    this.player = player;
    setSize(SIZE);

    AssetManager asset = AssetManager.getInstance();
    sprite = asset.findImage("player_swing_bottom");
    imageSize = new Vector2(26, 74);
    startFrame = -1;
    controller = GameController.getInstance();
  }

  public void reset() {
    startFrame = -1;
  }

  private double cd = 2;

  @Override
  public void render(RenderProperties properties) {
    // TODO Auto-generated method stub
    GraphicsContext context = properties.getContext();

    if (controller.isHitbox() && player.isAlive()) {
      context.setFill(Color.LIGHTBLUE);
      context.fillRect(getPosition().getX(), getPosition().getY(), getSize().getX(), getSize().getY());
    }

    if (startFrame != -1) {
      Vector2 renderPos = Vector2.renderCenter(getPosition(), getSize(), imageSize);

      int index = Math.min(4, (int) ((properties.getFrameCount() - startFrame) / cd));

      if (index >= 4)
        return;

      double xPos = index * imageSize.getX();

      Vector2 facing = player.getFacing().copy();
      int reversed = 1;

      if (player.getAttackCount() % 2 == 1) {
        reversed = -1;
      }

      if (facing.getX() < 0 != (reversed == -1)) {
        renderPos.addX(imageSize.getX());
      }

      context.drawImage(sprite, xPos, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(),
          imageSize.getX() * facing.getX() * reversed, imageSize.getY());
    }
  }

  @Override
  public void receiveCollision(GameObject object) {
    // TODO Auto-generated method stub

  }

  @Override
  public void update(RenderProperties properties) {
    // TODO Auto-generated method stub
    double newPosX = player.getPosition().getX() + player.getSize().getX() / 2 - this.getSize().getX() / 2;
    double newPosY = player.getPosition().getY() + player.getSize().getY() - 10;
    setPosition(new Vector2(newPosX, newPosY));
    if (!player.isGliding())
      setSize(Vector2.ZERO());
    else
      setSize(SIZE);

    if (player.isAttacking() && player.isGliding()) {
      if (startFrame == -1) {
        startFrame = properties.getFrameCount();
      }
    } else {
      startFrame = -1;
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub

  }

  public boolean collides(GameObject enemy) {
    if (enemy instanceof DemonHive && !((DemonHive) enemy).isVisible())
      return false;

    Vector2 posA = this.getPosition();
    Vector2 sizeA = this.getSize();
    Vector2 posB = enemy.getPosition();
    Vector2 sizeB = enemy.getSize();

    double leftA = posA.getX();
    double rightA = posA.getX() + sizeA.getX();
    double topA = posA.getY();
    double bottomA = posA.getY() + sizeA.getY();

    double leftB = posB.getX();
    double rightB = posB.getX() + sizeB.getX();
    double topB = posB.getY();
    double bottomB = posB.getY() + sizeB.getY();

    return rightA >= leftB && leftA <= rightB && bottomA >= topB && topA <= bottomB;
  }
}
