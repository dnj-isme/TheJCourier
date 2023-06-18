package app.main.game.object.boss.state.dash;

import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.game.object.boss.Boss;
import app.main.game.object.boss.BossState;
import app.main.game.object.boss.BossSword;
import app.main.game.object.player.Player;
import app.utility.Utility;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BossDashStart extends BossState {

  private static BossDashStart instance;

  public static BossDashStart load(Boss boss) {
    if (instance == null) {
      instance = new BossDashStart(boss);
    }
    return instance;
  }

  private Boss boss;
  private Image spriteThrow;
  private Image spriteDissapear;
  private Vector2 imageSize;
  private AssetManager assets;
  private BossSword sword;
  private boolean initialized;
  private long startAnim1 = -1;
  private boolean finishAnim1 = false;
  private long startAnim2 = -1;
  private boolean finishAnim2 = false;
  private boolean startThrow = false;

  private BossDashStart(Boss boss) {
    super(boss);
    this.boss = boss;
    assets = AssetManager.getInstance();
    spriteThrow = assets.findImage("boss_throw");
    spriteDissapear = assets.findImage("boss_throw_dissapear");
    imageSize = new Vector2(120.25, 120);
    reset();
  }

  public void reset() {
    initialized = false;
    startAnim1 = -1;
    finishAnim1 = false;
    startAnim2 = -1;
    finishAnim2 = false;
    sword = boss.getSword1();
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

      boolean left = boss.getPosition().minus(Player.getInstance(boss.getOwner()).getPosition()).getX() >= 0;
      double x = left ? 0 : GameScene.WIDTH - boss.getSize().getX();
      double y = Utility.frand(20 + 0.2 * (GameScene.HEIGHT - 20), 20 + 0.5 * (GameScene.HEIGHT - 20));
      
      boss.setFacing(left ? Vector2.LEFT() : Vector2.RIGHT());

      sword.setPosition(boss.getPosition());
      sword.setDestination(x, y);
      sword.startAnimation();
    } else if (finishAnim1 && sword.reachesDestination() && startAnim2 == -1) {
      startAnim2 = properties.getFrameCount();
      Platform.runLater(() -> {
        AudioFactory.createSfxHandler(assets.findAudio("sfx_boss_dissapear")).playThenDestroy();
      });
    } else if (finishAnim2) {
      reset();
      boss.setPosition(sword.getPosition());
      sword.reset();
      BossDash state = BossDash.load(boss);
      state.reset();
      boss.setState(state);
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {

  }

  private double cd = 2;

  @Override
  protected void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(boss.getPosition(), boss.getSize(), imageSize);

    if (!finishAnim1 || !sword.reachesDestination()) {
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

      context.drawImage(spriteDissapear, xPos, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(),
          renderPos.getY(), imageSize.getX() * -facing.getX(), imageSize.getY());
    }
  }
}
