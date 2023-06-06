package app.main.game.object.other;

import java.util.HashMap;
import java.util.Vector;

import app.utility.Utility;
import app.utility.canvas.GameScene;
import app.utility.canvas.Vector2;

public class DemonHiveController {
  public enum HiveTag {
    A, B, C, D, E
  }

  private GameScene owner;
  private Vector<DemonHive> hives;
  private HashMap<HiveTag, Boolean> visibility;
  
  public DemonHiveController(GameScene owner) {
    this.owner = owner;
    hives = new Vector<DemonHive>();
    visibility = new HashMap<>();
    boolean randAB = Utility.chance(50);
    boolean randCD = Utility.chance(50);
    visibility.put(HiveTag.A, randAB);
    visibility.put(HiveTag.B, !randAB);
    visibility.put(HiveTag.C, randCD);
    visibility.put(HiveTag.D, !randCD);
    visibility.put(HiveTag.E, randCD);
  }
  
  private int playerHit = -1;
  
  public void receiveHit(int playerHit) {
    if(this.playerHit == playerHit) return;
    this.playerHit = playerHit;
    for (DemonHive demonHive : hives) {
      demonHive.updateState();
    }
  }
  
  public void add(Vector2 position, HiveTag tag) {
    DemonHive ins = new DemonHive(owner, this);
    ins.setPosition(position);
    ins.setVisible(visibility.get(tag));
    hives.add(ins);
    owner.addGameObject(ins);
  }

  public Vector<DemonHive> getHives() {
    return hives;
  }
}
