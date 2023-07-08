package app.main.controller.audio;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class AudioHandler {
  private MediaPlayer player;
  private boolean loop;

  public AudioHandler(MediaPlayer player, boolean loop) {
    this.player = player;
    this.loop = loop;
  }

  public void playThenDestroy() {
    player.play();
    player.setOnEndOfMedia(() -> {
      player.stop();
      player.dispose();
    });
  }

  public void play() {
    player.play();
    if (loop) {
      player.setOnEndOfMedia(() -> {
        player.seek(Duration.millis(0));
        player.play();
      });
    }
  }

  public MediaPlayer getPlayer() {
    return player;
  }
  
  public void pause() {
    player.pause();
  }
  
  public void resume() {
    player.play();
  }

  private boolean disposed = false;
  public void stop() {
    if(player != null) {
      player.stop();
      player.dispose();
    }
  }

  public boolean isLoop() {
    return loop;
  }

  public void setLoop(boolean loop) {
    this.loop = loop;
  }
}
