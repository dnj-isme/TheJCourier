package app.main.game.object.player.state;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlayerFallState extends PlayerState {
  
  private static PlayerFallState instance;
  
  public static PlayerFallState load(Player player) {
    if(instance == null) {
      instance = new PlayerFallState(player);
    }
    return instance;
  }
  
  private Image sprite;
  private Vector2 imageSize;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private Player player;
  private AssetManager assetManager;

  private PlayerFallState(Player player) {
    super(player);
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();

    assetManager = AssetManager.getInstance();
    sprite = assetManager.findImage("player_fall");
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
    if(facing.getX() < 0) {
      renderPos.setX(renderPos.getX() + 60);
    }

    context.drawImage(sprite, spriteX, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
  }
  
  @Override
  public void update(RenderProperties properties) {
    detectGlide();
    
    if(eventObserver.isPressing(keyBinding.getBinding(KeyBinding.JUMP)) && player.isReleaseJump() && player.canCloudStep()) {
      player.setReleaseJump(false);
      player.setCloudStep(false);
      player.getVelocity().setY(-player.getJumpForce());
      player.setJump(true);
      player.setTeleport(true);
      Platform.runLater(() -> {        
        AudioFactory.createSfxHandler(assetManager.findAudio("sfx_jump")).playThenDestroy();
      });
    }
    
    hanldeHorizontalMovement();
  }
  
  @Override
  public void fixedUpdate(RenderProperties properties) {
    handleGravity(properties.getFixedDeltaTime(), player.getGravity());
  }
}
