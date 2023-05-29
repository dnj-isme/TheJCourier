package app.main.game.object.player.state;

import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.canvas.RenderProperties;

public class PlayerAttackWalkState extends PlayerState{

  private static PlayerAttackWalkState instance;
  
  public static PlayerAttackWalkState load(Player player) {
    if(instance == null) {
      instance = new PlayerAttackWalkState(player);
    }
    return instance;
  }

  public PlayerAttackWalkState(Player player) {
    super(player);
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
