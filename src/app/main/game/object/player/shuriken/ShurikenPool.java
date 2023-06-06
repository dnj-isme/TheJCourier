package app.main.game.object.player.shuriken;

import java.util.Vector;

import app.main.game.object.player.Player;
import app.utility.Utility;
import app.utility.canvas.GameScene;

public class ShurikenPool {
  
  private Vector<Shuriken> shurikens;
  private int index;
  
  public ShurikenPool(int max, GameScene owner) {
    if(max <= 0) {
      Utility.debug("This pool must store at least 1 shuriken");
    }
    shurikens = new Vector<Shuriken>();
    for(int i = 0; i < max; i++) {
      Shuriken ins = new Shuriken(owner);
      owner.addGameObject(ins);
      shurikens.add(ins);
    }
    index = 0;
  }
  
  public Shuriken deploy(Player player) {
    int count = 0;
    Shuriken deployed = null;
    int len = shurikens.size();
    for(int i = (index + count) % len; count < len; count++) {
      Shuriken target = shurikens.get(i);
      if(!target.isEnabled()) {
        target.initialize(player);
        target.start();
        deployed = target;
        index = (index + 1) % len;
        break;
      }      
    }
    return deployed;
  }
  
  public void stopAll() {
    for (Shuriken shuriken : shurikens) {
      shuriken.stop();
    }
  }
}
