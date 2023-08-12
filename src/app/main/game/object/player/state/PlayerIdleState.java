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

public class PlayerIdleState extends PlayerState {
  
  private static PlayerIdleState instance;
  
  public static PlayerIdleState load(Player player) {
    if(instance == null) {
      instance = new PlayerIdleState(player);
    }
    return instance;
  }

  // Note:
  // Size: 60 x 60
  // Tiles: 8 x 2
  // Unused tiles: the 2nd row
  private Image sprite;
  private Vector2 imageSize;
  private SceneEventObserver eventObserver;
  private KeyBinding keyBinding;
  private Player player;
  
  private PlayerIdleState(Player player) {
    super(player);
    this.player = player;
    eventObserver = player.getEventObserver();
    keyBinding = player.getKeyBinding();
    
    AssetManager asset = AssetManager.getInstance();
    sprite = asset.findImage("player_idle");
    imageSize = new Vector2(60, 60);    
  }
  
  private int cd = 5;

  @Override
  public void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSize);

    int ticks = (int) properties.getFrameCount() / cd;
    
    int topX = ticks % 4 * 60;
    int bottomX = 240 + ticks % 4 * 60;
    
    Vector2 facing = player.getFacing();
    if(facing.getX() < 0) {
      renderPos.setX(renderPos.getX() + 60);
    }
    
    context.drawImage(sprite, topX, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
    context.drawImage(sprite, bottomX, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
  }

  @Override
  public void update(RenderProperties properties) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub
    
  }
}
