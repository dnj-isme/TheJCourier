package app.main.game.object.boss.state;

import java.util.TimerTask;

import app.main.controller.asset.AssetManager;
import app.main.game.object.boss.Boss;
import app.main.game.object.boss.BossState;
import app.main.game.object.boss.state.dash.BossDash;
import app.main.game.object.boss.state.dash.BossDashStart;
import app.main.game.object.boss.state.levitate.BossLevitateStart;
import app.utility.Utility;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BossIdleState extends BossState {

  private static BossIdleState instance;
  private int turn = 1;

  public static BossIdleState load(Boss boss) {
    if (instance == null) {
      instance = new BossIdleState(boss);
    }
    return instance;
  }

  private Boss boss;
  private Image sprite;
  private Vector2 imageSize;
  private AssetManager assets;
  private boolean initialized;

  private BossIdleState(Boss boss) {
    super(boss);
    this.boss = boss;
    assets = AssetManager.getInstance();
    sprite = assets.findImage("boss_idle");
    imageSize = new Vector2(120, 120);
    initialized = false;
  }
  
  public void reset() {
    initialized = false;
  }

  @Override
  public void update(RenderProperties properties) {
    if(!initialized) {
      boss.getDemonHiveController().hideAll();
      initialized = true;
      Utility.delayAction(3000, new TimerTask() {
        @Override
        public void run() {
          reset();
          turn++;
          if(boss.isFirst()) {
            boss.setState(BossDashStart.load(boss));
            boss.setFirst(false);
          }
          else if(turn % 2 == 1) {
            BossLevitateStart next = BossLevitateStart.load(boss);
            next.reset();
            boss.setState(next);
          }
          else {
            BossDashStart next = BossDashStart.load(boss);
            next.reset();
            boss.setState(next);
          }
          boss.getDemonHiveController().refresh();
        }
      });
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {

  }

  private double cd = 3;

  @Override
  protected void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(boss.getPosition(), boss.getSize(), imageSize);

    int index = (int) (properties.getFrameCount() / cd) % 8;

    Vector2 facing = boss.getFacing();
    if (facing.getX() >= 0) {
      renderPos.setX(renderPos.getX() + imageSize.getX());
    }

    double posX = imageSize.getX() * index;
    context.drawImage(sprite, posX, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(),
        imageSize.getX() * -facing.getX(), imageSize.getY());
  }
}
