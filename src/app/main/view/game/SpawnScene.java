package app.main.view.game;

import java.util.TimerTask;

import app.main.controller.GameController;
import app.main.controller.asset.AssetManager;
import app.main.controller.audio.AudioFactory;
import app.main.controller.audio.AudioHandler;
import app.main.controller.scene.SceneController;
import app.main.game.scene.SpawnRoom;
import app.utility.Utility;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class SpawnScene extends GamePageTemplate{
  private GameController controller;
  private AudioHandler introMusic;
  private AudioHandler loopMusic;
  private boolean loopMode = false;
  
  public SpawnScene() {
    super(new SpawnRoom());
    controller = GameController.getInstance();
    getBase().setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
    AssetManager asset = AssetManager.getInstance();
    introMusic = AudioFactory.createMusicHandler(asset.findAudio("bgm_intro"), false);
    loopMusic = AudioFactory.createMusicHandler(asset.findAudio("bgm_main"), true);
    introMusic.getPlayer().setOnEndOfMedia(() -> {
      loopMusic.play();
      introMusic.stop();
      loopMode = true;
    });
    Utility.delayAction(1000, new TimerTask() {
      @Override
      public void run() {
        if(!getGameScene().isPaused()) introMusic.play();
      }
    });
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
      for(double volume = loopMusic.getPlayer().getVolume(); volume >= 0.01; volume /= 1.5) {
		if(introMusic.getPlayer() != null && !introMusic.isDisposed()) {
			introMusic.getPlayer().setVolume(volume);
		}
		loopMusic.getPlayer().setVolume(volume);
		try {
		  Thread.sleep(20);
		} catch (InterruptedException e) {
		  // TODO Auto-generated catch block
		  e.printStackTrace();
		}
      }
	  if(introMusic.getPlayer() != null && !introMusic.isDisposed()) {
    	  introMusic.getPlayer().setVolume(0);
          introMusic.stop();
      }
      loopMusic.getPlayer().setVolume(0);
      loopMusic.stop();
    });
  }

  @Override
  public void handleResume() {
    loopMusic.setVolume(controller.getMusic());
    introMusic.setVolume(controller.getMusic());
    if(loopMode) {
      loopMusic.play();
    }
    else {
      introMusic.play();
    }
  }
}
