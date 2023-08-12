package app.main.game.object.boss.state.levitate;

import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.game.object.boss.Boss;
import app.main.game.object.boss.BossState;
import app.utility.Utility;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BossLevitate extends BossState{
  private enum LevitatePosition {
    None, Left, Right
  }
  private static BossLevitate instance;
  public static BossLevitate load(Boss boss) {
    if (instance == null) {
      instance = new BossLevitate(boss);
    }
    return instance;
  }

  private Vector2 imageSize;

  private Boss boss;
  private AssetManager assets;
  private Image bossLevitateSpawn;
  private Image bossLevitate;
  private boolean baseStart;
  private long stateStart;

  private double timeStart = -1;

  private double startAnim = -1;
  private boolean transition = false;
  private boolean loop = false;

  private LevitatePosition position = LevitatePosition.None;

  private BossLevitate(Boss boss) {
    super(boss);
    this.boss = boss;
    assets = AssetManager.getInstance();
    bossLevitate = assets.findImage("boss_levitate");
    bossLevitateSpawn = assets.findImage("boss_levitate_spawn");
    imageSize = new Vector2(120, 120);
    reset();
  }

  public void reset() {
    timeStart = -1;
    startAnim = -1;
    transition = false;
    position = LevitatePosition.None;
    loop = false;
    baseStart = false;
    stateStart = -1;
  }

  @Override
  public void update(RenderProperties properties) {
    if (!baseStart) {
      baseStart = true;
      stateStart = boss.getOwner().getTimeSpent();
      startAnim = properties.getFrameCount();
      transition = false;
      boss.activateFire();
      switchPosition();
    }

    // Check if state has reached 15 seconds. If so, force transition.
    long currentTime = boss.getOwner().getTimeSpent();
    if (currentTime - stateStart >= 15000) {
      if(!transition) {
        transition = true;
        loop = false;
        startAnim = properties.getFrameCount();
      }
      int index = (int) ((properties.getFrameCount() - startAnim) / 2.5);
      if(index > 5) {
        reset();
        boss.deactivateFire();
        BossLevitateCooldown state = BossLevitateCooldown.load(boss);
        state.reset();
        boss.setState(state);
      }
    }

    if (loop) {
      if (currentTime - timeStart >= 3500) {
        Platform.runLater(() -> AudioFactory.createSfxHandler(assets.findAudio("sfx_boss_disappear")).playThenDestroy());
        transition = true;
        loop = false;
        startAnim = properties.getFrameCount();
      }
    } else if (transition) {
      int index = (int) ((properties.getFrameCount() - startAnim) / 2.5);
      if(index > 5) {
        switchPosition();
        transition = false;
        startAnim = properties.getFrameCount();
      }
    } else {
      int index = (int) ((properties.getFrameCount() - startAnim) / 2.5);
      if(index > 5) {
        loop = true;
        timeStart = boss.getOwner().getTimeSpent();
      }
    }
  }


  private void switchPosition() {
    switch (position) {
      case None:
        position = Utility.chance(50) ? LevitatePosition.Left: LevitatePosition.Right;
        break;
      case Left:
        position = LevitatePosition.Right;
        break;
      case Right:
        position = LevitatePosition.Left;
        break;
    }
    boss.setPosition(position == LevitatePosition.Right ? GameScene.WIDTH - boss.getSize().getX() - 30 : 30, 60);
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void render(RenderProperties properties) {
    if(!baseStart) return;

    GraphicsContext context = properties.getContext();
    Vector2 renderPos = Vector2.renderBottomCenter(boss.getPosition(), boss.getSize(), imageSize.copy());
    renderPos.addY(10);

    if(loop && !transition) {
      int index = (int) (properties.getFrameCount() / 2.5) % 8;
      double posX = imageSize.getX() * index;
      context.drawImage(bossLevitate, posX, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(), imageSize.getX(), imageSize.getY());
    } else {
      int index = (int) ((properties.getFrameCount() - startAnim) / 2.5);
      if(index > 5) {
        index = 5;
      }
      if(transition) {
        index = 5 - index;
      }

      double posX = imageSize.getX() * index;
      context.drawImage(bossLevitateSpawn, posX, 0, imageSize.getX(), imageSize.getY(), renderPos.getX(), renderPos.getY(), imageSize.getX(), imageSize.getY());
    }
  }
}
