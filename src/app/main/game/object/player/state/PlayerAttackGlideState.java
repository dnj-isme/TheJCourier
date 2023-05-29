package app.main.game.object.player.state;

import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.canvas.RenderProperties;

public class PlayerAttackGlideState extends PlayerState{

  private static PlayerAttackGlideState instance;
  
  public static PlayerAttackGlideState load(Player player) {
    if(instance == null) {
      instance = new PlayerAttackGlideState(player);
    }
    return instance;
  }

  public PlayerAttackGlideState(Player player) {
    super(player);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void render(RenderProperties properties) {
    // TODO Auto-generated method stub
    
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
