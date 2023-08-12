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
      stop();
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
    if(player != null && !disposed) {
      player.stop();
      player.dispose();
      disposed = true;
    }
  }

  public boolean isLoop() {
    return loop;
  }

  public void setLoop(boolean loop) {
    this.loop = loop;
  }

  public boolean isDisposed() {
    return disposed;
  }

  public void setVolume(double volume) {
    if(player != null && !disposed) {
      player.setVolume(volume);
    }
  }
}
