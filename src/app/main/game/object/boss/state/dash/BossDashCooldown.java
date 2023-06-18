package app.main.game.object.boss.state.dash;

import java.util.TimerTask;

import app.main.controller.asset.AssetManager;
import app.main.game.object.boss.Boss;
import app.main.game.object.boss.BossState;
import app.main.game.object.boss.state.BossIdleState;
import app.utility.Utility;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BossDashCooldown extends BossState {
  private static BossDashCooldown instance;

  public static BossDashCooldown load(Boss boss) {
    if (instance == null) {
      instance = new BossDashCooldown(boss);
    }
    return instance;
  }

  private Boss boss;
  private Image sprite;
  private Vector2 imageSize;
  private AssetManager assets;
  private boolean initialized;

  private BossDashCooldown(Boss boss) {
    super(boss);
    this.boss = boss;
    assets = AssetManager.getInstance();
    sprite = assets.findImage("boss_cooldown");
    imageSize = new Vector2(120, 120);
    reset();
  }

  public void reset() {
    initialized = false;
  }

  @Override
  public void update(RenderProperties properties) {
    if (!initialized) {
      initialized = true;
      bossFacingPlayer();
      Platform.runLater(() -> {
        Utility.delayAction(5000, new TimerTask() {
          @Override
          public void run() {
            BossIdleState state = BossIdleState.load(boss);
            state.reset();
            boss.setState(state);
          }
        });
      });
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub

  }

  double cd = 2.5;

  @Override
  protected void render(RenderProperties properties) {
    Vector2 renderPos = Vector2.renderBottomCenter(boss.getPosition(), boss.getSize(), imageSize);
    GraphicsContext context = properties.getContext();

    int index = (int) ((properties.getFrameCount() / cd) % 8);
    double xPos = 120 * index;
    context.drawImage(sprite, xPos, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(),
        imageSize.getX(), imageSize.getY());
  }
}
