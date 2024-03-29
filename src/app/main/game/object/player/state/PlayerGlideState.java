package app.main.game.object.player.state;

import app.main.controller.asset.AssetManager;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlayerGlideState extends PlayerState {

  private static PlayerGlideState instance;

  public static PlayerGlideState load(Player player) {
    if (instance == null) {
      instance = new PlayerGlideState(player);
    }
    return instance;
  }

  private Image glideSprite;
  private Vector2 imageSize;
  private Player player;

  private PlayerGlideState(Player player) {
    super(player);
    this.player = player;

    AssetManager asset = AssetManager.getInstance();
    glideSprite = asset.findImage("player_glider");
    imageSize = new Vector2(60, 60);
  }

  private int cd = 10;

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), Player.SIZE, imageSize);

    int ticks = (int) properties.getFrameCount() / cd;
    int x = ticks % 8 * 60;

    Vector2 facing = player.getFacing();
    if (facing.getX() < 0) {
      renderPos.setX(renderPos.getX() + 60);
    }

    context.drawImage(glideSprite, x, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
  }

  @Override
  public void update(RenderProperties properties) {
    detectGlide();

    if (player.isAttacking() && player.isGlideHit() && player.getAttackCount() != player.getLastHitOnGlide()) {
      player.setGlideHit(false);
      player.setLastHitOnGlide(player.getAttackCount());
      player.getVelocity().setY(-player.getSmallJumpForce());
    }

    if (player.isGliding()) {
      Vector2 direction = updatePlayerDirection();
      double x = direction.getX() * player.getMoveSpeed();
      player.getVelocity().setX(x);

      if (player.isTouchingGround()) {
        player.setGlide(false);
        player.setLastHitOnGlide(-1);
        player.setGlideHit(false);
      }
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    handleGravity(properties.getFixedDeltaTime(), player.getGravity(), player.getGlideFall());
  }
}
