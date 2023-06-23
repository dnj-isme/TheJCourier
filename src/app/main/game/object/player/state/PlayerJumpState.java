package app.main.game.object.player.state;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.Utility;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class PlayerJumpState extends PlayerState {
  private static PlayerJumpState instance;

  public static PlayerJumpState load(Player player) {
    if (instance == null) {
      instance = new PlayerJumpState(player);
    }
    return instance;
  }

  private Image sprite;
  private Vector2 imageSize;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private Player player;
  private KeyCode jumpBind;
  private AssetManager assetManager;

  private PlayerJumpState(Player player) {
    super(player);
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();
    jumpBind = keyBinding.getBinding(KeyBinding.JUMP);
    assetManager = AssetManager.getInstance();

    AssetManager asset = AssetManager.getInstance();
    sprite = asset.findImage("player_jump");
    imageSize = new Vector2(60, 60);
  }

  private int cd = 10;

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSize);

    int ticks = (int) properties.getFrameCount() / cd;

    int spriteX = ticks % 4 * 60;

    Vector2 facing = player.getFacing();
    if (facing.getX() < 0) {
      renderPos.setX(renderPos.getX() + 60);
    }

    context.drawImage(sprite, spriteX, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
  }

  @Override
  public void update(RenderProperties properties) {
    detectGlide();
    hanldeHorizontalMovement();

    if (eventObserver.isPressing(keyBinding.getBinding(KeyBinding.JUMP)) && player.isTouchingGround()) {
      // Condition 1: Player can jump when touching the ground
      player.setReleaseJump(false);
      player.setCloudStep(false);
      player.getVelocity().setY(-player.getJumpForce());
      player.setJump(true);
      player.setTeleport(true);
      Platform.runLater(() -> {        
        AudioFactory.createSfxHandler(assetManager.findAudio("sfx_jump")).playThenDestroy();
      });
    } else if (eventObserver.isPressing(keyBinding.getBinding(KeyBinding.JUMP)) && player.canCloudStep()
        && player.isReleaseJump()) {
      // Condition 2: Player can jump again if cloud step is active
      player.setReleaseJump(false);
      player.setCloudStep(false);
      player.getVelocity().setY(-player.getJumpForce());
      player.setJump(true);
      player.setTeleport(true);
      Platform.runLater(() -> {        
        AudioFactory.createSfxHandler(assetManager.findAudio("sfx_jump")).playThenDestroy();
      });
    } else if (!eventObserver.isPressing(keyBinding.getBinding(KeyBinding.JUMP))) {
      // Condition 3: Limit the player's vertical velocity
      player.getVelocity().setY(Math.max(player.getVelocity().getY() * 0.8, player.getVelocity().getY()));
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    handleGravity(properties.getFixedDeltaTime(), player.getGravity());
  }
}
