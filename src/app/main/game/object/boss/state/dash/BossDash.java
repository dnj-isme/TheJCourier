package app.main.game.object.boss.state.dash;

import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.game.object.boss.Boss;
import app.main.game.object.boss.BossState;
import app.main.game.object.player.Player;
import app.utility.Utility;
import app.utility.canvas.GameScene;
import app.utility.canvas.RenderProperties;
import app.utility.canvas.Vector2;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BossDash extends BossState{
  private static BossDash instance;

  public static BossDash load(Boss boss) {
    if (instance == null) {
      instance = new BossDash(boss);
    }
    return instance;
  }
  
  private enum DashState {
    SPAWN, PREPARE, CHARGE, STRIKE
  }
  
  private final Boss boss;
  private final AssetManager assets;
  private DashState state;
  private Vector2 direction;
  private Vector2 previousWall;

  private final Image bossWall;
  private final Image bossWallSpawn;
  private final Image bossCeil;
  private final Image bossDashHorizontal;
  private final Image bossDashUp;
  private final Image bossDashDown;
  private final Image bossBlink;
  private final Image bossGround;
  
  private boolean first = true;
  private double startAnim = -1;
  private int dashCount = 0;

  private BossDash(Boss boss) {
    super(boss);
    this.boss = boss;
    
    assets = AssetManager.getInstance();
    
    bossBlink = assets.findImage("boss_blink");
    bossCeil = assets.findImage("boss_ceil");
    bossDashDown = assets.findImage("boss_dash_down");
    bossDashHorizontal = assets.findImage("boss_dash_horizontal");
    bossDashUp = assets.findImage("boss_dash_up");
    bossWallSpawn = assets.findImage("boss_wall_spawn");
    bossWall = assets.findImage("boss_wall");
    bossGround = assets.findImage("boss_ground");
    reset();
  }

  public void reset() {
    first = true;
    startAnim = -1;
    state = DashState.SPAWN;
    direction = Vector2.ZERO();
    dashCount = Utility.random(4, 8);
  }

  @Override
  public void update(RenderProperties properties) {
    if(first) {
      AudioFactory.createSfxHandler(assets.findAudio("sfx_boss_reappear")).playThenDestroy();
      first = false;
      startAnim = properties.getFrameCount();
      state = DashState.SPAWN;
    }
    switch (state) {
    case PREPARE:
      if(startAnim == -1) {
        startAnim = properties.getFrameCount();
      }
      
      if(properties.getFrameCount() - startAnim >= 30) {
        AudioFactory.createSfxHandler(assets.findAudio("sfx_boss_blink")).playThenDestroy();
        startAnim = properties.getFrameCount();
        state = DashState.CHARGE;
        previousWall = touchesWall();
      }
      break;
    case STRIKE:
      if(startAnim == -1) {
        determineNextPos(properties.getFrameCount());
      }
      
      Utility.cls();
      System.out.println("Position   = " + boss.getPosition());
      System.out.println("Boundary X = " + (GameScene.WIDTH - boss.getSize().getX()));
      System.out.println("Boundary Y = " + (GameScene.HEIGHT - boss.getSize().getY() - 20));
      System.out.println(touchesWall());
      
      if(!touchesWall().equals(Vector2.ZERO()) && !touchesWall().equals(previousWall)) {
        startAnim = properties.getFrameCount();
        direction = Vector2.ZERO();
        state = DashState.PREPARE;
        dashCount--;
        if(dashCount == 0) {
          reset();
          BossDashCooldown state = BossDashCooldown.load(boss);
          state.reset();
          boss.setState(state);
        }
      }
    default:
      break;
    }
  }

  @Override
  public void fixedUpdate(RenderProperties properties) {
    if(state == DashState.STRIKE) {
      double dashSpeed = 800;
      Vector2 velocity = direction.mult(dashSpeed * properties.getFixedDeltaTime());
      boss.setPosition(
          Utility.clamp(boss.getPosition().getX() + velocity.getX(), 0, GameScene.WIDTH - boss.getSize().getX()),
          Utility.clamp(boss.getPosition().getY() + velocity.getY(), 0, GameScene.HEIGHT - boss.getSize().getY() - 20)
          );
    }
  }
  
  private Vector2 touchesWall() {
    Vector2 position = boss.getPosition();
    Vector2 size = boss.getSize();
    
    Vector2 output = Vector2.ZERO();
    if(position.getX() <= 0) output.setX(-1);
    else if (position.getX() >= GameScene.WIDTH - size.getX()) output.setX(1);
    
    if (position.getY() <= 0) output.setY(-1);
    else if (position.getY() >= GameScene.HEIGHT - size.getY() - 20) output.setY(1);
    
    return output;
  }

  @Override
  protected void render(RenderProperties properties) {
    GraphicsContext context = properties.getContext();
    
    long index;
    Vector2 renderPos;
    Image sprite;
    double xPos;
    
    switch (state) {
    case SPAWN:
      // 120 x 78
      // 6 x 1
      index = (int) ((properties.getFrameCount() - startAnim) / 4);
      System.out.println(index);
      if(index < 6) {
        renderPos = Vector2.renderLeftCenter(boss.getPosition(), boss.getSize(), new Vector2(120, 78));
        xPos = index * 120;
        context.drawImage(bossWallSpawn, xPos, 0, 120, 78, renderPos.getX(), renderPos.getY(), 120, 78);
        break;
      }
      else {
        state = DashState.PREPARE;
        startAnim = -1;
      }
    case PREPARE:
    case CHARGE:
      if(touchesWall().getY() == 0)
      if(boss.getPosition().getY() != 0) {
        renderPos = Vector2.renderCenter(boss.getPosition(), boss.getSize(), new Vector2(37, 57));
        int facing = 1;
        if(renderPos.getX() > 10) {
          facing = -1;
          renderPos.setAdd(37, 0);
        }
        context.drawImage(bossWall, 0, 0, 37, 57, renderPos.getX(), renderPos.getY(), 37 * facing, 57);
        if(state == DashState.CHARGE) {
          
          renderPos = Vector2.renderCenter(boss.getPosition(), boss.getSize(), new Vector2(20, 20));
          index = (int) ((properties.getFrameCount() - startAnim) / 2);
          xPos = index * 20;
          context.drawImage(bossBlink, xPos, 0, 20, 20, renderPos.getX() + 3 * -boss.getFacing().getX(), renderPos.getY() - 20, 20, 20);
          
          if(index >= 8) {
            state = DashState.STRIKE;
            startAnim = -1;
          }
        }
      }
      else {
        if(touchesWall().getY() == -1) {
          renderPos = Vector2.renderTopCenter(boss.getPosition(), boss.getSize(), new Vector2(57, 33));

          context.drawImage(bossCeil,renderPos.getX(), renderPos.getY());
        }
        else {
          renderPos = Vector2.renderTopCenter(boss.getPosition(), boss.getSize(), new Vector2(38, 54));
          
          context.drawImage(bossGround, renderPos.getX(), renderPos.getY());

        }
        if(state == DashState.CHARGE) {

          renderPos = Vector2.renderCenter(boss.getPosition(), boss.getSize(), new Vector2(20, 20));
          index = (int) ((properties.getFrameCount() - startAnim) / 2);
          xPos = index * 20;
          context.drawImage(bossBlink, xPos, 0, 20, 20, renderPos.getX() + 20, renderPos.getY() - 15, 20, 20);

          if(index >= 8) {
            state = DashState.STRIKE;
            startAnim = -1;
          }
        }
      }
      break;
    case STRIKE:      
      if(dashCount > 2) {
        // Kondisi Normal: Horizonal
        renderPos = Vector2.renderCenter(boss.getPosition(), boss.getSize(), new Vector2(77, 74));
        sprite = bossDashHorizontal;
        int facing = 1;
        if(direction.getX() > 0) {
          facing = -1;
          renderPos.setAdd(77, 0);
        }
        context.drawImage(sprite, 0, 0, 77, 74, renderPos.getX(), renderPos.getY(), 77 * facing, 74);
      }
      else {
        renderPos = Vector2.renderCenter(boss.getPosition(), boss.getSize(), new Vector2(74, 77));
        sprite = direction.getY() > 0 ? bossDashDown : bossDashUp;
        context.drawImage(sprite, renderPos.getX(), renderPos.getY());
      }
      break;
    default:
      break;
    }
  }
  
  private void determineNextPos(long startAnim) {
    if(dashCount == 2) {
      if(previousWall.getY() == -1) {
        dashCount++;
        Vector2 playerPos = Player.getInstance(boss.getOwner()).getPosition();
        direction = playerPos.minus(boss.getPosition()).getNormalized();
        return;
      }
      direction = new Vector2(
          (GameScene.WIDTH - boss.getSize().getX())/2, 
          0)
          .minus(boss.getPosition()).getNormalized();
    }
    else if (dashCount == 1) {
      direction = new Vector2(
          (GameScene.WIDTH - boss.getSize().getX())/2,
          (GameScene.HEIGHT - boss.getSize().getY() - 20)/2)
          .minus(boss.getPosition()).getNormalized();
    }
    else {      
      Vector2 playerPos = Player.getInstance(boss.getOwner()).getPosition();
      direction = playerPos.minus(boss.getPosition()).getNormalized();
    }
    this.startAnim = startAnim;
    AudioFactory.createSfxHandler(assets.findAudio("sfx_boss_dash")).playThenDestroy();
  }
}
