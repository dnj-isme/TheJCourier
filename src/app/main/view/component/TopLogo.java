package app.main.view.component;

import app.main.controller.asset.FontManager;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;

public class TopLogo extends Label implements Component {

    public TopLogo() {
        super("The JCourier");
        setFont(FontManager.loadFont(48));
        setTextFill(Color.WHITE);
    }
}
