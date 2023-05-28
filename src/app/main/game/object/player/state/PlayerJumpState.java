package app.main.game.object.player.state;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

public class PlayerJumpState extends PlayerState{
  private static PlayerJumpState instance;
  
  public static PlayerJumpState load(Player player) {
    if(instance == null) {
      instance = new PlayerJumpState(player);
    }
    return instance;
  }
  
  private Image sprite;
  private Vector2 imageSize;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private Player player;

  private PlayerJumpState(Player player) {
    super(player);
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();

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
    if(facing.getX() < 0) {
      renderPos.setX(renderPos.getX() + 60);
    }

    context.drawImage(sprite, spriteX, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
  }
  
  @Override
  public void update(RenderProperties properties) {
    super.update(properties);
    
    if(player.isTouchingGround() || player.canCloudStep()) {
      KeyCode jumpBind = keyBinding.getBinding(KeyBinding.JUMP);
      if(eventObserver.isPressing(jumpBind)) {
        player.setCloudStep(false);
        eventObserver.setPressing(jumpBind, false);
        player.getVelocity().setY(-player.getJumpForce());
      }
    }
  }
  
  @Override
  public void fixedUpdate(RenderProperties properties) {
    super.fixedUpdate(properties);
    
    handleGravity(properties.getFixedDeltaTime(), player.getGravity());
  }
}
