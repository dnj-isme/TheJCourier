package app.main.game.object.boss;

import app.main.game.object.player.Player;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Updatable;
import app.utility.canvas.Vector2;

public abstract class BossState implements Updatable{
  private Boss boss;
  
  public BossState(Boss boss) {
    this.boss = boss;
  }

  protected abstract void render(RenderProperties properties);

  protected void moveTo(Vector2 target, double speed) {
    
  }

  public void bossFacingPlayer() {
    boolean left = boss.getPosition().minus(Player.getInstance(boss.getOwner()).getPosition()).getX() >= 0;
    boss.setFacing(left ? Vector2.LEFT() : Vector2.RIGHT());
  }
}
