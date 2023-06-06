package app.utility.canvas;

public enum ObjectLayer {
  Default,
  Background,
  Block,
  VFX,
  Foreground,
  UI;
  
  public int getIndex() {
    switch (this) {
    case Background:
      return 2;
    case Block:
      return 3;
    case Foreground:
      return 5;
    case UI:
      return 6;
    case VFX:
      return 4;
    default:
      return 1;
    }
  }
}