package app.main.game.object.player.state;

import app.main.controller.asset.AssetManager;
import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class PlayerSpawnedState extends PlayerState {

  private static PlayerSpawnedState instance;

  public static PlayerSpawnedState load(Player player) {
    if (instance == null) {
      instance = new PlayerSpawnedState(player);
    }
    return instance;
  }
  
  private Image sprite;
  private Vector2 imageSize;
  private Player player;
  private AssetManager assetManager;

  private PlayerSpawnedState(Player player) {
    super(player);
    assetManager = AssetManager.getInstance();
    this.player = player;
    imageSize = new Vector2(60, 60);
    sprite = assetManager.findImage("player_respawn");
  }
  
  private long startAnim = -1;
  private long cd = 5;
  
  public void resetStartAnim() {
    startAnim = -1;
  }

  @Override
  public void render(RenderProperties properties) {

    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(player.getPosition(), player.getSize(), imageSize);

    Vector2 facing = player.getFacing();
    if (facing.getX() < 0) {
      renderPos.setX(renderPos.getX() + 60);
    }
    
    long index = (properties.getFrameCount() / cd ) % 3;
    
    if(index >= 2) {
      
      index = 1;
      player.setSpawn(false);
      if(player.isReverseSpawn()) {
        player.setInvisible(true);
      }
    }
    
    if(player.isReverseSpawn()) {
      index = 1 - index;
    }
    
    context.drawImage(sprite, index * 60, 0, 60, 60, renderPos.getX(), renderPos.getY(), 60 * facing.getX(), 60);
  }

  @Override
  public void update(RenderProperties properties) {
    // TODO Auto-generated method stub
    if(startAnim == -1) {
      startAnim = properties.getFrameCount();
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub

  }

}
