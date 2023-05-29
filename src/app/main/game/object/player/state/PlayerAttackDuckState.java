package app.main.game.object.player.state;

import app.main.game.object.player.Player;
import app.main.game.object.player.PlayerState;
import app.utility.canvas.RenderProperties;

public class PlayerAttackDuckState extends PlayerState {

  private static PlayerAttackDuckState instance;
  
  public static PlayerAttackDuckState load(Player player) {
    if(instance == null) {
      instance = new PlayerAttackDuckState(player);
    }
    return instance;
  }

  public PlayerAttackDuckState(Player player) {
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
