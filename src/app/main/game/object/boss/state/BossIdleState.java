package app.main.game.object.boss.state;

import app.main.game.object.boss.Boss;
import app.main.game.object.boss.BossState;
import app.utility.canvas.RenderProperties;

public class BossIdleState extends BossState{

  private static BossIdleState instance;

  public static BossIdleState load(Boss boss) {
    if (instance == null) {
      instance = new BossIdleState(boss);
    }
    return instance;
  }

  private BossIdleState(Boss boss) {
    super(boss);
  }

  @Override
  public void update(RenderProperties properties) {
    
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    
  }

  @Override
  protected void render(RenderProperties properties) {
    
  }
}
