package app.main.view.component;

import app.main.controller.asset.FontManager;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class Copyright extends VBox implements Component {
    
    public Copyright() {
        Label label1 = new Label("Created by JC22-2");
        Label label2 = new Label("Inspired by The Messenger");
        Font font12 = FontManager.loadFont(12);
        label1.setFont(font12);
        label2.setFont(font12);
        
        this.getChildren().addAll(label1, label2);
    }
}
