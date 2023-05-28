package app.main.controller;

import java.util.Vector;

import app.utility.Utility;
import javafx.scene.input.KeyCode;

public final class KeyBinding {
  private static KeyBinding instance;
  
  public static KeyBinding getIntance() {
    if(instance == null) {
      instance = new KeyBinding();
    }
    return instance;
  }
  
  public void reset() {
    list.clear();
    list.add(KeyCode.S);
    list.add(KeyCode.A);
    list.add(KeyCode.D);
    list.add(KeyCode.ENTER);
    list.add(KeyCode.SPACE);
    list.add(KeyCode.J);
    list.add(KeyCode.K);
    list.add(KeyCode.L);
  }
  
  private KeyBinding() {
    list = new Vector<KeyCode>();
    reset();
  }

  public static final int DUCK = 0;
  public static final int LEFT = 1;
  public static final int RIGHT = 2;
  public static final int PAUSE = 3;
  public static final int JUMP = 4;
  public static final int ATTACK = 5;
  public static final int SHURIKEN = 6;
  public static final int TELEPORT = 7;
  
//  private KeyCode duck;
//  private KeyCode left;
//  private KeyCode right;
//  private KeyCode pause;
//  
//  private KeyCode jump;
//  private KeyCode attack;
//  private KeyCode shuriken;
//  private KeyCode teleport;
  
  private Vector<KeyCode> list;

  public KeyCode getBinding(int keyCode) {
    return list.get(keyCode);
  }
  
  public void setBinding(int keyCode, KeyCode key) {
    int idx = list.indexOf(key);
    if(idx == -1 && !list.contains(key)) {
      list.set(keyCode, key);
      Utility.debug("Not Found");
    }
    else {
      KeyCode prev = list.get(keyCode);
      list.set(keyCode, key);
      list.set(idx, prev);
      Utility.debug("Found");
    }
  }
  
  public void debug() {
    for (int i = 0; i < list.size(); i++) {
      System.out.println(list.get(i).toString());
    }
  }
}
