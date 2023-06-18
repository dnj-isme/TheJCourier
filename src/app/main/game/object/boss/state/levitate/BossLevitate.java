package app.main.game.object.boss.state.levitate;

import app.main.controller.asset.AssetManager;
import app.main.game.object.boss.Boss;
import app.main.game.object.boss.BossState;
import app.main.game.object.boss.BossSword;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.image.Image;

public class BossLevitate extends BossState{
  private static BossLevitate instance;

  public static BossLevitate load(Boss boss) {
    if (instance == null) {
      instance = new BossLevitate(boss);
    }
    return instance;
  }

  private Boss boss;
  private AssetManager assets;
  private Image bossLevitateSpawn;
  private Image bossLevitate;

  private BossLevitate(Boss boss) {
    super(boss);
    this.boss = boss;
    assets = AssetManager.getInstance();
    bossLevitate = assets.findImage("boss_levitate");
    bossLevitateSpawn = assets.findImage("boss_leviate_spawn");
    reset();
  }

  public void reset() {

  }

  @Override
  public void update(RenderProperties properties) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub
    
  }

  @Override
  protected void render(RenderProperties properties) {
    // TODO Auto-generated method stub
    
  }
}
