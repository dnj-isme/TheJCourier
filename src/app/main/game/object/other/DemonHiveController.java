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
  private HashMap<DemonHive, HiveTag> hiveTags;
  private HashMap<HiveTag, Boolean> visibility;
  
  public DemonHiveController(GameScene owner) {
    this.owner = owner;
    hives = new Vector<DemonHive>();
    visibility = new HashMap<>();
    hiveTags = new HashMap<>();
    randomize();
  }
  
  public void randomize() {
    boolean rand1 = Utility.chance(50);
    boolean rand2 = Utility.chance(50);
    boolean rand3 = Utility.chance(50);
    visibility.put(HiveTag.A, rand1);
    visibility.put(HiveTag.B, rand2);
    visibility.put(HiveTag.C, !rand1);
    visibility.put(HiveTag.D, !rand2);
    visibility.put(HiveTag.E, !rand1 && !rand2 ? true : rand3);
  }
  
  public void hideAll() {
    for (DemonHive demonHive : hives) {
      if(demonHive.isVisible()) {
        demonHive.updateState();
      }
    }
  }
  
  public void refresh() {
    randomize();
    for (DemonHive demonHive : hives) {
      if(demonHive.isVisible() != visibility.get(hiveTags.get(demonHive))) {
        demonHive.updateState();
      }
    }
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
    hiveTags.put(ins, tag);
    owner.addGameObject(ins);
  }

  public Vector<DemonHive> getHives() {
    return hives;
  }
}
