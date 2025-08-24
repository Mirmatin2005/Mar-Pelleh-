package com.example.marpelleh;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.util.Random;
import java.util.function.Consumer;

public class Dice {
    private final Button rollBtn;
    private final Label resultLabel;
    private final VBox uiBox;
    private final Random random;
    private Consumer<Integer> onRollCallback;

    public Dice() {
        rollBtn = new Button("Roll Dice");
        resultLabel = new Label("🎲 Roll the dice!");
        random = new Random();

        rollBtn.setOnAction(e -> roll());

        uiBox = new VBox(10, rollBtn, resultLabel);
        uiBox.setMinWidth(120);
        uiBox.setStyle("-fx-alignment: center;");
    }

    void roll() {
        int dice = random.nextInt(6) + 1;
        resultLabel.setText("You rolled: " + dice);

        if (onRollCallback != null) {
            onRollCallback.accept(dice);
        }
    }

    public VBox getUI() {
        return uiBox;
    }


    public void setOnRoll(Consumer<Integer> callback) {
        this.onRollCallback = callback;
    }

    public void setDisable(boolean b) {
        rollBtn.setDisable(b);
        resultLabel.setDisable(b);
    }
}
