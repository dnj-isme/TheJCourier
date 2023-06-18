package app.main.game.object.boss.state.levitate;

import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.game.object.boss.Boss;
import app.main.game.object.boss.BossState;
import app.main.game.object.boss.BossSword;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BossLevitateStart extends BossState {

  private static BossLevitateStart instance;

  public static BossLevitateStart load(Boss boss) {
    if (instance == null) {
      instance = new BossLevitateStart(boss);
    }
    return instance;
  }

  private final Boss boss;
  private final Image spriteThrow;
  private final Image spriteDisappear;
  private final Vector2 imageSize;
  private final AssetManager assets;
  private BossSword sword1;
  private BossSword sword2;
  private boolean initialized;
  private long startAnim1 = -1;
  private boolean finishAnim1 = false;
  private long startAnim2 = -1;
  private boolean finishAnim2 = false;
  private boolean startThrow = false;

  private BossLevitateStart(Boss boss) {
    super(boss);
    this.boss = boss;
    assets = AssetManager.getInstance();
    spriteThrow = assets.findImage("boss_throw");
    spriteDisappear = assets.findImage("boss_throw_dissapear");
    imageSize = new Vector2(120.25, 120);
    reset();
  }

  public void reset() {
    initialized = false;
    startAnim1 = -1;
    finishAnim1 = false;
    startAnim2 = -1;
    finishAnim2 = false;
    sword1 = boss.getSword1();
    sword2 = boss.getSword2();
    startThrow = false;
  }

  @Override
  public void update(RenderProperties properties) {
    if (!initialized) {
      initialized = true;
      startAnim1 = properties.getFrameCount();
      boss.getDemonHiveController().refresh();
      super.bossFacingPlayer();
    }

    if (finishAnim1 && !startThrow) {
      startThrow = true;
      // TODO: Throw Sword to both sides
      sword1.setPosition(boss.getPosition().copy());
      sword2.setPosition(boss.getPosition().copy());
      sword1.setDestination(0, boss.getPosition().getY());
      sword2.setDestination(GameScene.WIDTH - sword2.getSize().getX(), boss.getPosition().getY());
      sword1.startAnimation();
      sword2.startAnimation();
    } else if (finishAnim1 && sword1.reachesDestination() && startAnim2 == -1) {
      startAnim2 = properties.getFrameCount();
      Platform.runLater(() -> AudioFactory.createSfxHandler(assets.findAudio("sfx_boss_dissapear")).playThenDestroy());
    } else if (finishAnim2) {
      reset();
      sword1.reset();
      sword2.reset();
      BossLevitate state = BossLevitate.load(boss);
      state.reset();
      boss.setState(state);
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {

  }

  private final double cd = 2;

  @Override
  protected void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(boss.getPosition(), boss.getSize(), imageSize);

    if (!finishAnim1 || !sword1.reachesDestination()) {
      int index = (int) ((double) (properties.getFrameCount() - startAnim1) / cd);
      if (index >= 7) {
        index = 6;
        finishAnim1 = true;
      }
      double xPos = (index % 5) * imageSize.getX();
      double yPos = index < 5 ? 0 : imageSize.getY();

      Vector2 facing = boss.getFacing();
      if (facing.getX() >= 0) {
        renderPos.setX(renderPos.getX() + imageSize.getX());
      }

      context.drawImage(spriteThrow, xPos, yPos, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(),
          imageSize.getX() * -facing.getX(), imageSize.getY());
    } else if (!finishAnim2) {
      int index = (int) ((double) (properties.getFrameCount() - startAnim2) / cd);

      if (index >= 6) {
        index = 5;
        finishAnim2 = true;
      }

      Vector2 facing = boss.getFacing();
      if (facing.getX() >= 0) {
        renderPos.setX(renderPos.getX() + imageSize.getX());
      }

      double xPos = index * imageSize.getX();

      context.drawImage(spriteDisappear, xPos, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(),
          renderPos.getY(), imageSize.getX() * -facing.getX(), imageSize.getY());
    }
  }
}
