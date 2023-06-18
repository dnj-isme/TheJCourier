package app.main.controller;

import app.main.controller.asset.AssetManager;
import app.utility.DBConnect;

public final class GameController {
  private GameController() {}
  
  private static GameController instance;
  public static GameController getInstance() {
    if(instance == null) {
      instance = new GameController();
    }
    return instance;
  }

  private AssetManager assets;
  private boolean debug = false;
  private boolean invincible = false;
  private boolean hitbox = false;
  
  private int music = 6;
  private int sfx = 8;
  private double FPS = 60;
  
  private boolean showTimer = true;
  
  public boolean isShowTimer() {
    return showTimer;
  }

  public void setShowTimer(boolean showTimer) {
    this.showTimer = showTimer;
  }

  public boolean isDebug() {
    return debug;
  }

  public void setDebug(boolean debug) {
    this.debug = debug;
  }

  public boolean isInvincible() {
    return invincible;
  }

  public void setInvincible(boolean invincible) {
    this.invincible = invincible;
  }

  public boolean isHitbox() {
    return hitbox;
  }

  public void setHitbox(boolean hitbox) {
    this.hitbox = hitbox;
  }

  public int getMusic() {
    return music;
  }

  public void setMusic(int music) {
    this.music = music;
  }

  public int getSfx() {
    return sfx;
  }

  public void setSfx(int sfx) {
    this.sfx = sfx;
  }

  public void configure(boolean debug, boolean invincible, boolean hitbox) {
    this.debug = debug;
    this.invincible = invincible;
    this.hitbox = hitbox;
    
    try {
      DBConnect.getInstance();
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    
    assets = AssetManager.getInstance();
    assets.loadAsset();
    
    KeyBinding.getIntance();
  }
  

  public void setFPS(double FPS) {
    this.FPS = FPS;
  }

  public double getFPS() {
    return FPS;
  }
}
