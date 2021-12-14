package cs1302.game;

import java.util.Random;
import java.util.logging.Level;

import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * Code for a basic Snake game. The player can move the snake in any direction
 * with the arrow keys not opposite to its current direction, and the goal is
 * to eat as many apples as possible without running into yourself!
 */
public class Snake extends Game {

    private final double movementValue = 20.0;
    private int frameCounter;

    private Random rng;       // random number generator
    private ArrayList<Rectangle> snake; // rectangles to represent the snake
    private Rectangle apple;  // represents the food for the snake
    private int score;
    private double xDirection;
    private double yDirection;
    private boolean isWelcome;
    private Button start;
    private Label scoreLabel;

    /**
     * Construct a {@code Snake} object.
     */
    public Snake() {
        super(640, 640, 1000);            // call parent constructor
//        setLogLevel(Level.INFO);             // enable logging
        this.rng = new Random();             // random number generator
        this.apple = new Rectangle(20, 20, new ImagePattern(new Image("file:resources/Apple.png")));
        this.snake = new ArrayList<Rectangle>();  // initializes ArrayList
        snake.add(new Rectangle(20, 20, new ImagePattern(new Image("file:resources/" +
                                                                   "SnakeHeadUp.png"))));
        this.score = 0;
        this.frameCounter = 0;
    } // DemoGame

    /** {@inheritDoc} */
    @Override
    protected void init() {
        welcome();
        scoreLabel = new Label("Score: " + String.valueOf(score));
        scoreLabel.setTranslateX(300);
        getChildren().add(scoreLabel);
        scoreLabel.toFront();
        scoreLabel.setTextFill(Color.BLACK);
    } // init

    /** {@inheritDoc} */
    @Override
    protected void update() {

        // update player position
        if (!isWelcome) {

            if (xDirection != movementValue) {
                isKeyPressed( KeyCode.LEFT, () -> {
                    snake.get(0).setFill(new ImagePattern(new Image("file:resources/" +
                                                                    "SnakeHeadLeft.png")));
                    xDirection = -movementValue;
                    yDirection = 0.0;
                });
            }

            if (xDirection != -movementValue) {
                isKeyPressed(KeyCode.RIGHT, () -> {
                    snake.get(0).setFill(new ImagePattern(new Image("file:resources/" +
                                                                    "SnakeHeadRight.png")));
                    xDirection = movementValue;
                    yDirection = 0.0;
                });
            }


            if (yDirection != -movementValue) {
                isKeyPressed(KeyCode.DOWN, () -> {
                    snake.get(0).setFill(new ImagePattern(new Image("file:resources/" +
                                                                    "SnakeHeadDown.png")));
                    xDirection = 0.0;
                    yDirection = movementValue;
                });
            }

            if (yDirection != movementValue) {
                isKeyPressed(  KeyCode.UP, () -> {
                    snake.get(0).setFill(new ImagePattern(new Image("file:resources/" +
                                                                    "SnakeHeadUp.png")));
                    xDirection = 0.0;
                    yDirection = -movementValue;
                });
            }

            if (frameCounter % 200 == 0) {
                if (snake.get(0).getX() < 0 || snake.get(0).getX() > getWidth() - 20 ||
                    snake.get(0).getY() < 0 || snake.get(0).getY() > getHeight() - 20) {
                    gameOver();
                } // if
                appleCheck();
                updateSnake();
                snake.get(0).setX(snake.get(0).getX() + xDirection);
                snake.get(0).setY(snake.get(0).getY() + yDirection);
                snake.get(0).toBack();
                apple.toBack();
                checkLoss();
                frameCounter = 1;
            } else {
                frameCounter++;
            } // if
        } // if
    } // update

    /**
     * Checks if the snakes head matches the location of the apple.
     */
    private void appleCheck() {
        if (snake.get(0).getX() == apple.getX() && snake.get(0).getY() == apple.getY()) {
            snake.add(new Rectangle(20, 20, new ImagePattern(new Image("file:resources/" +
                                                                       "SnakeBody.png"))));
            getChildren().add(snake.get(snake.size() - 1));
            snake.get(snake.size() - 1).setX(snake.get(snake.size() - 2).getX() + 20.0);
            snake.get(snake.size() - 1).setY(snake.get(snake.size() - 2).getX() + 20.0);
            randomApple();
            updateSnake();
            score++;
            scoreLabel.setText("Score: " + String.valueOf(score));
        } // if
    } // appleCheck

    /**
     * Changes the location of the apple to a random location.
     */
    private void randomApple() {
        int random = rng.nextInt((int)(getWidth() - apple.getWidth()));
        apple.setX(random - random % 20);
        random = rng.nextInt((int)(getHeight() - apple.getHeight()));
        apple.setY(random - random % 20);
        apple.toBack();
    } // randomApple

    /**
     * Consecutively updates each part of the tail of the snake.
     */
    private void updateSnake() {
        for (int i = snake.size() - 1; i > 0; i--) {
            snake.get(i).setX(snake.get(i - 1).getX());
            snake.get(i).setY(snake.get(i - 1).getY());
            snake.get(i).toBack();
        } // for
    } // updateSnake

    /**
     * Checks if the snake ran into itself and the game is over.
     */
    private void checkLoss() {
        for (int i = snake.size() - 1; i > 0; i--) {
            if (snake.get(0).getX() == snake.get(i).getX() &&
                snake.get(0).getY() == snake.get(i).getY()) {
                gameOver();
                break;
            } // if
        } // for
    } // checkLoss

    /**
     * Builds and displays the welcome screen when called.
     */
    private void welcome() {
        clearBoard();
        super.setBackground(new Background(new BackgroundFill(new ImagePattern(
            new Image("file:resources/SnakeBackground.png")),CornerRadii.EMPTY,new Insets(0))));
        isWelcome = true;
        start = new Button("Start");
        start.setLayoutY(450);
        start.setLayoutX(270);
        start.setMinHeight(60);
        start.setMinWidth(100);
        getChildren().add(start);
        start.setOnAction(e -> {
            getChildren().remove(start);
            isWelcome = false;
            reset();
            buildBoard();
        });

    } // welcome

    /**
     * Builds and displays the game over screen when the player loses.
     */
    private void gameOver() {
        clearBoard();
        super.setBackground(new Background(new BackgroundFill(new ImagePattern(
            new Image("file:resources/GameOver.png")),CornerRadii.EMPTY,new Insets(0))));
        scoreLabel.setTextFill(Color.WHITE);
        start.setText("Start Over");
        getChildren().add(start);
        isWelcome = true;
    }  // gameOver

    /**
     * Removes the snake and apple from the board.
     */
    private void clearBoard() {
        for (Rectangle s : snake) {
            getChildren().remove(s);
        } // for
        getChildren().remove(apple);
    } // clearBoard

    /**
     * Resets the snake and apple location.
     */
    private void reset() {

        snake = new ArrayList<Rectangle>();
        snake.add(new Rectangle(20, 20, new ImagePattern(new Image("file:resources/" +
                "SnakeHeadUp.png"))));
        randomApple();
        xDirection = 0.0;
        yDirection = 0.0;

    } // reset

    /**
     * Builds a fresh board with a snake of length 1 and random apple.
     */
    private void buildBoard() {
        super.setBackground(new Background(new BackgroundFill(new ImagePattern(
            new Image("file:resources/SnakeBoard.png")),CornerRadii.EMPTY,new Insets(0))));
        getChildren().addAll(snake.get(0), apple);
        snake.get(0).setX(getWidth() / 2);
        snake.get(0).setY(getHeight() / 2);
        randomApple();
        xDirection = 0.0;
        yDirection = 0.0;
        score = 0;
        scoreLabel.setText("Score: " + score);
        scoreLabel.setTextFill(Color.BLACK);
    } // buildBoard

} // Snake
