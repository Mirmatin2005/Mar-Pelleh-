package com.example.marpelleh;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.shape.*;
import java.util.HashMap;
import java.util.Map;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.util.Duration;

public class BoardUi extends Application{
    private static final int Tile = 60;
    private static final int Width = 10;
    private static final int Height = 10;
    private static final double TOKEN_RADIUS = Tile * 0.22;

    private Circle[] players;    
    private int[] positions;           
    private int currentPlayerIndex = 0;
    private Label turnLabel;

    private Map<Integer, Integer> harekat;


    @Override
    public void start(Stage stage) {
        harekat = new HashMap<>();

        harekat.put(22, 7);
        harekat.put(34, 11);
        harekat.put(98, 26);
        harekat.put(78, 41);
        harekat.put(86, 48);

        harekat.put(3, 80);
        harekat.put(6, 32);
        harekat.put(38, 96);
        harekat.put(45, 66);
        harekat.put(50, 89);

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);

        int cellNumber = 1;
        for (int rFromBottom = 0; rFromBottom < Height; rFromBottom++) {
            boolean leftToRight = (rFromBottom % 2 == 0);
            for (int colInRow = 0; colInRow < Width; colInRow++) {
                int col = leftToRight ? colInRow : (Width - 1 - colInRow);
                int row = Height - 1 - rFromBottom;

                Rectangle tile = new Rectangle(Tile, Tile);
                tile.setFill((row + col) % 2 ==0 ? Color.LIGHTGREEN : Color.LIGHTBLUE);
                tile.setStroke(Color.BLACK);
                tile.setStrokeWidth(1);
                tile.setStrokeType(StrokeType.INSIDE);

                Text number = new Text(String.valueOf(cellNumber));
                number.setFill(Color.BLACK);
                number.setStyle("-fx-font: 20 arial;");

                StackPane cell = new StackPane(tile, number);

                grid.add(cell, col, row);

                cellNumber++;
            }
        }
        Pane tokenLayer = new Pane();
        Pane snakeLadderLayer = new Pane();

        tokenLayer.setPickOnBounds(false);
        tokenLayer.setPrefSize(Width * Tile, Height * Tile);


        Circle player1 = makeToken(Color.RED);
        Circle player2 = makeToken(Color.BLUE);

        players = new Circle[]{player1, player2};
        positions = new int[]{1, 1};

        tokenLayer.getChildren().addAll(player1, player2);

        PlaceTokenAtCell(player1, 1, Corner.BALA_CHAP);
        PlaceTokenAtCell(player2, 1, Corner.BALA_RAST);


        drawSnake(snakeLadderLayer, 22, 7);
        drawSnake(snakeLadderLayer, 34, 11);
        drawSnake(snakeLadderLayer, 98, 26);
        drawSnake(snakeLadderLayer, 78, 41);
        drawSnake(snakeLadderLayer, 86, 48);

        drawLadder(snakeLadderLayer, 3, 80);
        drawLadder(snakeLadderLayer, 6, 32);
        drawLadder(snakeLadderLayer, 38, 96);
        drawLadder(snakeLadderLayer, 45, 66);
        drawLadder(snakeLadderLayer, 50, 89);

        turnLabel = new Label("نوبت بازیکن قرمز است");
        turnLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Dice dice = new Dice();
        dice.setOnRoll(diceValue -> {
            Circle currentToken = players[currentPlayerIndex];

            int oldPos = positions[currentPlayerIndex];
            int intermediatePos = oldPos + diceValue;
            if (intermediatePos > 100) intermediatePos = 100;
            positions[currentPlayerIndex] = intermediatePos;

            int finalIntermediatePos = intermediatePos;
            moveToken(currentToken, oldPos, intermediatePos,
                    currentPlayerIndex == 0 ? Corner.BALA_CHAP : Corner.BALA_RAST, () -> {

                 if (harekat.containsKey(finalIntermediatePos)) {
                     int finalPos = harekat.get(finalIntermediatePos);
                     positions[currentPlayerIndex] = finalPos;

                     moveToken(currentToken, finalIntermediatePos, finalPos,
                             currentPlayerIndex == 0 ? Corner.BALA_CHAP : Corner.BALA_RAST, () -> {
                         checkWinAndNextTurn(dice);
                     });
                 }
                 else {
                     checkWinAndNextTurn(dice);
                 }
            });
        });

        Pane board = new Pane(grid, snakeLadderLayer, tokenLayer);
        BorderPane root = new BorderPane();
        root.setTop(turnLabel);
        BorderPane.setAlignment(turnLabel, Pos.CENTER);
        root.setCenter(board);
        root.setRight(dice.getUI());

        Scene scene = new Scene(root, Width * Tile, Height * Tile);
        stage.setScene(scene);
        stage.show();
    }

    private Circle makeToken(Color color){
        Circle c = new Circle(TOKEN_RADIUS);
        c.setFill(color);
        c.setStroke(Color.BLACK);
        c.setStrokeWidth(1);
        return c;
    }
    enum Corner{BALA_CHAP, BALA_RAST, PAYIN_CHAP, PAYIN_RAST}

    private void PlaceTokenAtCell(Circle token, int cell, Corner corner){
        double[] center = cellCenter(cell, corner);
        token.setCenterX(center[0]);
        token.setCenterY(center[1]);
    }
    private void moveToken(Circle token, int fromCell, int toCell, Corner corner, Runnable onFinished){
        double[] target = cellCenter(toCell, corner);
        double tx = target[0];
        double ty = target[1];

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO,
                        new KeyValue(token.centerXProperty(), token.getCenterX()),
                        new KeyValue(token.centerYProperty(), token.getCenterY())
                ),
                new KeyFrame(Duration.millis(500),
                        new KeyValue(token.centerXProperty(), tx),
                        new KeyValue(token.centerYProperty(), ty)
                )
        );
        timeline.setOnFinished(e -> {
            if (onFinished != null) onFinished.run();
        });

        timeline.play();
    }

    private double[] cellCenter(int cell, Corner corner) {
        int[] rc = cellToRowCol(cell);
        int row = rc[0];
        int col = rc[1];

        double x = col * Tile;
        double y = (Height - 1 - row) * Tile;

        double cx = switch (corner) {
            case BALA_CHAP -> x + TOKEN_RADIUS;
            case BALA_RAST -> x + Tile - TOKEN_RADIUS;
            case PAYIN_CHAP -> x + TOKEN_RADIUS;
            case PAYIN_RAST -> x + Tile - TOKEN_RADIUS;
        };
        double cy = switch (corner) {
            case BALA_CHAP -> y + TOKEN_RADIUS;
            case BALA_RAST -> y + TOKEN_RADIUS;
            case PAYIN_CHAP -> y + Tile - TOKEN_RADIUS;
            case PAYIN_RAST -> y + Tile - TOKEN_RADIUS;
        };
        return new double[]{cx, cy};
    }

    private int[]cellToRowCol(int cell){
        if(cell < 1) cell = 1;
        if (cell > 100) cell = 100;

        int idx = cell - 1;
        int rFromBottom = idx / Width;
        int colInRow = idx % Width;

        boolean leftToRight = (rFromBottom % 2 == 0);
        int col = leftToRight ? colInRow : (Width - 1 - colInRow);

        return new int[]{rFromBottom, col};
    }

    private void checkWinAndNextTurn(Dice dice) {
        if (positions[currentPlayerIndex] == 100) {
            turnLabel.setText("🎉 بازیکن " +
                    (currentPlayerIndex == 0 ? "قرمز" : "آبی") + " برنده شد!");
            dice.setDisable(true);
            return;
        }

        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
        String playerName = (currentPlayerIndex == 0) ? "قرمز" : "آبی";
        turnLabel.setText("نوبت بازیکن " + playerName + " است");
    }

    private void drawSnake(Pane layer, int from, int to) {
        double[] start = cellCenter(from, Corner.BALA_CHAP);
        double[] end = cellCenter(to, Corner.PAYIN_CHAP);

        double ctrlX1 = (start[0] + end[0]) / 2 + 40;
        double ctrlY1 = start[1];
        double ctrlX2 = (start[0] + end[0]) / 2 - 40;
        double ctrlY2 = end[1];

        javafx.scene.shape.CubicCurve snake = new javafx.scene.shape.CubicCurve(
                start[0], start[1],
                ctrlX1, ctrlY1,
                ctrlX2, ctrlY2,
                end[0], end[1]
        );
        snake.setStroke(Color.DARKGREEN);
        snake.setStrokeWidth(6);
        snake.setFill(null);

        layer.getChildren().add(snake);
    }

    private void drawLadder(Pane layer, int from, int to) {
        double[] start = cellCenter(from, Corner.PAYIN_RAST);
        double[] end = cellCenter(to, Corner.BALA_RAST);

        javafx.scene.shape.Line side1 = new javafx.scene.shape.Line(start[0], start[1], end[0], end[1]);
        javafx.scene.shape.Line side2 = new javafx.scene.shape.Line(start[0]-15, start[1], end[0]-15, end[1]);

        side1.setStroke(Color.SADDLEBROWN);
        side1.setStrokeWidth(4);
        side2.setStroke(Color.SADDLEBROWN);
        side2.setStrokeWidth(4);

        for (int i=0; i<=5; i++) {
            double t = i / 5.0;
            double x1 = start[0] + (end[0] - start[0]) * t;
            double y1 = start[1] + (end[1] - start[1]) * t;

            double x2 = start[0]-15 + (end[0]-15 - (start[0]-15)) * t;
            double y2 = start[1] + (end[1] - start[1]) * t;

            javafx.scene.shape.Line step = new javafx.scene.shape.Line(x1, y1, x2, y2);
            step.setStroke(Color.SIENNA);
            step.setStrokeWidth(3);
            layer.getChildren().add(step);
        }

        layer.getChildren().addAll(side1, side2);
    }

}
