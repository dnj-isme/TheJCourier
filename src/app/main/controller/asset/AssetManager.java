package app.main.controller.asset;

import java.io.File;
import java.util.HashMap;

import app.utility.Utility;
import javafx.scene.image.Image;
import javafx.scene.media.Media;

public final class AssetManager {
  private static AssetManager instance;

  public static AssetManager getInstance() {
    if (instance == null)
      instance = new AssetManager();
    return instance;
  }

  private boolean loaded = false;

  private HashMap<String, Media> audioCollection = new HashMap<>();
  private HashMap<String, Media> videoCollection = new HashMap<>();
  private HashMap<String, Image> imageCollection = new HashMap<>();

  private AssetManager() {
  }

  public void loadAsset() {
    if (loaded) {
      Utility.debug("Asset has already loaded");
    } else {
      loadImages();
      loadAudio();
      loadVideo();
      loaded = true;
    }
  }

  public Media findAudio(String key) {
    return audioCollection.get(key);
  }

  public Media findVideo(String key) {
    return videoCollection.get(key);
  }

  public Image findImage(String key) {
    return imageCollection.get(key);
  }

  public boolean isLoaded() {
    return loaded;
  }
  
  private void loadImages() {
    // TODO: This function is used to load picture assets
    Utility.debug("Loading Images");

    // TODO: Load Assets here
    imageCollection.put("icon", loadImage("assets/icon/icon.png"));
    imageCollection.put("player", loadImage("assets/sprite/ninja.png"));
    imageCollection.put("boss", loadImage("assets/sprite/boss.png"));
    imageCollection.put("main_menu", loadImage("assets/sprite/main_menu.png"));
    imageCollection.put("item", loadImage("assets/sprite/item.png"));
    imageCollection.put("frame", loadImage("assets/sprite/frame.png"));
    
    // Player Section
    imageCollection.put("player_idle", loadImage("assets/sprite/player/player_idle.png"));
    imageCollection.put("player_walk_top", loadImage("assets/sprite/player/player_walk_top.png"));
    imageCollection.put("player_walk_bottom", loadImage("assets/sprite/player/player_walk_bottom.png"));
    imageCollection.put("player_fall", loadImage("assets/sprite/player/player_fall.png"));
    imageCollection.put("player_jump", loadImage("assets/sprite/player/player_jump.png"));
    
    // [END]

    Utility.debug("Finished Loading Images");
  }

  private void loadAudio() {
    // TODO: This function is used to load audio assets
    Utility.debug("Loading Audio");

    // TODO: Load Assets here
    audioCollection.put("bgm_intro", loadAudio("assets/audio/song/bgm_intro.wav"));
    audioCollection.put("bgm_main", loadAudio("assets/audio/song/bgm_main.wav"));
    audioCollection.put("bgm_boss", loadAudio("assets/audio/song/bgm_boss.wav"));
    audioCollection.put("sample_music", loadAudio("assets/audio/song/bgm_sample.mp3"));

    // Game and UI SFX
    audioCollection.put("sfx_game_start", loadAudio("assets/audio/sfx/game_start.wav"));
    audioCollection.put("sfx_menu_cursor_16", loadAudio("assets/audio/sfx/menu_cursor_16.wav"));
    audioCollection.put("sfx_menu_select_16", loadAudio("assets/audio/sfx/menu_select_16.wav"));
    audioCollection.put("sfx_menu_cursor_8", loadAudio("assets/audio/sfx/menu_cursor_8.wav"));
    audioCollection.put("sfx_menu_select_8", loadAudio("assets/audio/sfx/menu_select_8.wav"));
    audioCollection.put("sfx_pause", loadAudio("assets/audio/sfx/pause.wav"));

    // Player General
    audioCollection.put("sfx_died", loadAudio("assets/audio/sfx/died.wav"));
    audioCollection.put("sfx_heal", loadAudio("assets/audio/sfx/heal.wav"));
    audioCollection.put("sfx_heal_1", loadAudio("assets/audio/sfx/singleHP.wav"));
    audioCollection.put("sfx_shuriken_pickup", loadAudio("assets/audio/sfx/ManaPickup.wav"));
    audioCollection.put("sfx_jump", loadAudio("assets/audio/sfx/jump.wav"));
    audioCollection.put("sfx_player_hurt", loadAudio("assets/audio/sfx/player_hurt.wav"));
    audioCollection.put("sfx_wingsuit", loadAudio("assets/audio/sfx/wingsuit_deploy.wav"));

    // Player Basic Attack
    audioCollection.put("sfx_sword_1", loadAudio("assets/audio/sfx/swordswing1.wav"));
    audioCollection.put("sfx_sword_2", loadAudio("assets/audio/sfx/swordswing2.wav"));
    audioCollection.put("sfx_sword_3", loadAudio("assets/audio/sfx/swordswing3.wav"));

    // Player Rope Dart
    audioCollection.put("sfx_rope_dart_ready", loadAudio("assets/audio/sfx/GraplouReady.wav"));
    audioCollection.put("sfx_rope_stick", loadAudio("assets/audio/sfx/rope_stick.wav"));
    audioCollection.put("sfx_rope_throw", loadAudio("assets/audio/sfx/RopeThrow.wav"));

    // Player Shuriken
    audioCollection.put("sfx_shuriken_throw", loadAudio("assets/audio/sfx/shuriken_throw.wav"));
    audioCollection.put("sfx_shuriken_stick", loadAudio("assets/audio/sfx/shuriken_stick.wav"));
    // [END]

    Utility.debug("Finished Loading Audio");
  }

  private void loadVideo() {
    // TODO: This function is used to load video assets
    Utility.debug("Loading Video");

    // TODO: Load Assets here
    videoCollection.put("intro", loadAudio("assets/video/intro_cut.mp4"));
    // [END]

    Utility.debug("Finished Loading Videos");
  }

  private Media loadAudio(String path) {
    return new Media(new File(path).toURI().toString());
  }

  private Image loadImage(String path) {
    return new Image(new File(path).toURI().toString());
  }
}
