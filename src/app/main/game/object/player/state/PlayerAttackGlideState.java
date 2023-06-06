package app.main.game.object.player.state;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.Utility;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlayerAttackGlideState extends PlayerState {

  private static PlayerAttackGlideState instance;

  public static PlayerAttackGlideState load(Player player) {
    if (instance == null) {
      instance = new PlayerAttackGlideState(player);
    }
    return instance;
  }

  private Image sprite;
  private Vector2 imageSize;
  private Player player;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private AssetManager assetManager;

  private PlayerAttackGlideState(Player player) {
    super(player);
    this.player = player;
    eventObserver = SceneEventObserver.getInstance();
    keyBinding = KeyBinding.getIntance();

    assetManager = AssetManager.getInstance();
    sprite = assetManager.findImage("player_glider_attack");
    imageSize = new Vector2(60, 60);
  }

  private double cd = 1.5;

  @Override
  public void render(RenderProperties properties) {
    int index = 7;
    Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), Player.SIZE, imageSize);
    GraphicsContext context = properties.getContext();
    if (player.getStartAttackFrame() != -1) {
      index = Math.min(7, (int) ((properties.getFrameCount() - player.getStartAttackFrame()) / cd));
    }
    if (index >= 8) {
      index = 7;
      player.setFinishedAttack(true);
    }

    double spriteX = index * imageSize.getX();

    Vector2 facing = player.getFacing();
    if (facing.getX() < 0) {
      renderPos.addX(imageSize.getX());
    }

    context.drawImage(sprite, spriteX, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
  }

  @Override
  public void update(RenderProperties properties) {
    detectGlide();

    if (player.isAttacking() && player.isGlideHit() && player.getAttackCount() != player.getLastHitOnGlide()) {
      player.setGlideHit(false);
      player.setLastHitOnGlide(player.getAttackCount());
      player.getVelocity().setY(-player.getSmallJumpForce());
    }

    Vector2 direction = updatePlayerDirection();
    double x = direction.getX() * player.getMoveSpeed();
    player.getVelocity().setX(x);
    if (player.isGliding()) {
      if (player.isTouchingGround()) {
        player.setGlide(false);
      }
    }

    if (player.getStartAttackFrame() == -1) {
      player.setStartAttackFrame(properties.getFrameCount());
      player.addAttackCount();
      player.setAttack(true);
      player.setReleaseAttack(false);
      player.setCloudStep(true);
      Platform.runLater(() -> {
        AudioFactory.createSfxHandler(assetManager.findAudio("sfx_sword_" + Utility.random(1, 3))).playThenDestroy();
      });
    }

    if (player.isFinishedAttack()) {
      player.setFinishedAttack(false);
      player.setAttack(false);
      player.setStartAttackFrame(-1);
      player.setGlideHit(false);
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    handleGravity(properties.getFixedDeltaTime(), player.getGravity(), player.getGlideFall());
  }
}
