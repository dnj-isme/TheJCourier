package app.main.controller.audio;

import app.main.controller.GameController;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class AudioFactory {
  public static AudioHandler createSfxHandler(Media media) {
    GameController gameController = GameController.getInstance();
    MediaPlayer player = new MediaPlayer(media);
    player.setVolume((double) gameController.getSfx() / (double) 10);
    return new AudioHandler(player, false);
  }

  public static AudioHandler createMusicHandler(Media media, boolean loop) {
    GameController gameController = GameController.getInstance();
    MediaPlayer player = new MediaPlayer(media);
    player.setVolume((double) gameController.getMusic() / (double) 10);
    return new AudioHandler(player, loop);
  }
}
