package app.main.controller.asset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;

import javafx.scene.text.Font;

public class FontManager {

  private static HashMap<Integer, Font> fontHistory = new HashMap<>();
  
  public static Font loadFont(int size) {
    if(fontHistory.containsKey(size)) {
      return fontHistory.get(size);
    }
    else {
      Font font = null;
      try {
        font = Font.loadFont(
            new FileInputStream(new File("assets/font/prstartk-webfont.ttf")), 
            size);
        
        fontHistory.put(size, font);
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      return font;
    }
  }

}
