package app.utility.canvas;

public enum ObjectLayer {
  Default, Background, Foreground, Block, Player, VFX, Frontground, UI;

  public int getIndex() {
    switch (this) {
    case Background:
      return 1;
    case Foreground:
      return 2;
    case Block:
      return 3;
    case Player:
      return 4;
    case VFX:
      return 5;
    case Frontground:
      return 6;
    case UI:
      return 7;
    default:
      return 0;
    }
  }
}