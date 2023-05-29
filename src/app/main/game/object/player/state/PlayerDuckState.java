package app.main.game.object.player.state;

import app.main.controller.KeyBinding;
import app.main.controller.asset.AssetManager;
import app.main.controller.scene.SceneEventObserver;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.Utility;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlayerDuckState extends PlayerState {

  private static PlayerDuckState instance;
  
  public static PlayerDuckState load(Player player) {
    if(instance == null) {
      instance = new PlayerDuckState(player);
    }
    return instance;
  }
  
  private Image sprite;
  private Vector2 imageSize;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private Player player;
  private long duckFrameStarted = -1;

  private PlayerDuckState(Player player) {
    super(player);
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();

    AssetManager asset = AssetManager.getInstance();
    sprite = asset.findImage("player_duck");
    imageSize = new Vector2(60, 60);
  }

  
  // Index = 1 x 3
  private int cd = 2;
  
  @Override
  public void render(RenderProperties properties) {
    // TODO Auto-generated method stub
    if(duckFrameStarted != -1) {
      GraphicsContext context = properties.getContext();
      Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSize);
      
      int index = 2 - Math.min(2, (int) (properties.getFrameCount() - duckFrameStarted) / cd);

      int spriteX = index * 60;
      
      Vector2 facing = player.getFacing();
      if(facing.getX() < 0) {
        renderPos.setX(renderPos.getX() + 60);
      }
      
      context.drawImage(sprite, spriteX, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
    }
  }
  
  @Override
  public void update(RenderProperties properties) {
    updatePlayerDirection();
    player.setVelocity(Vector2.ZERO());
    
    if(eventObserver.isPressing(keyBinding.getBinding(KeyBinding.DUCK)) && player.isTouchingGround()) {
      if(duckFrameStarted == -1) {
        duckFrameStarted = properties.getFrameCount();
      }
      player.setDuck(true);
    }
    else {
      duckFrameStarted = -1;
      player.setDuck(false);
    }
  }
  
  public void reset() {
    duckFrameStarted = -1;
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub

  }
}
