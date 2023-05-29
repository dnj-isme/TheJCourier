package app.main.game.object.player;

import app.main.game.object.player.state.PlayerAttackGlideState;
import app.main.game.object.player.state.PlayerDuckState;
import app.main.game.object.player.state.PlayerGlideState;

public enum PlayerSize {
  TOP, BOTTOM, FULL;

  public static PlayerSize getSize(PlayerState state) {
    if (state instanceof PlayerAttackGlideState || state instanceof PlayerGlideState) {
      return PlayerSize.TOP;
    } else if (state instanceof PlayerDuckState) {
      return PlayerSize.BOTTOM;
    } else {
      return PlayerSize.FULL;
    }
  }
}