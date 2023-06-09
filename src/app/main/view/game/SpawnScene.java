package app.main.view.game;

import java.util.TimerTask;

import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.scene.SceneController;
import app.main.game.scene.SpawnRoom;
import app.utility.Utility;
import app.utility.canvas.GameScene;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class SpawnScene extends GamePageTemplate{
  private MediaPlayer introMusic;
  private MediaPlayer loopMusic;
  private boolean loopMode = false;
  
  public SpawnScene() {
    super(new SpawnRoom());
    getBase().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    AssetManager asset = AssetManager.getInstance();
    introMusic = AudioFactory.createMusicHandler(asset.findAudio("bgm_intro"), false).getPlayer();
    loopMusic = AudioFactory.createMusicHandler(asset.findAudio("bgm_main"), true).getPlayer();
    introMusic.setOnEndOfMedia(() -> {
      loopMusic.play();
      loopMode = true;
    });
    introMusic.play();
    loopMode = false;
    

    ((SpawnRoom) super.getGameScene()).setOnTransition(() -> {
      handleStop();
      SceneController.getInstance().switchScene(new BossScene());
    });
  }

  @Override
  public void handlePause() {
    if(loopMode) {
      loopMusic.pause();
    }
    else {
      introMusic.pause();
    }
  }
  
  public void handleStop() {
    Platform.runLater(() -> {
      for(double volume = loopMusic.getVolume(); volume >= 0.01; volume /= 1.5) {
        introMusic.setVolume(volume);
        loopMusic.setVolume(volume);
        System.out.println(volume);
        try {
          Thread.sleep(20);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
      introMusic.setVolume(0);
      loopMusic.setVolume(0);
      loopMusic.stop();
      introMusic.stop();
    });
  }

  @Override
  public void handleResume() {
    if(loopMode) {
      loopMusic.play();
    }
    else {
      introMusic.play();
    }
  }
}
