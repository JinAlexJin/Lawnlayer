package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleBallTest extends App {

    @Test
    public void SimpleBallTest() {
        char[][] grid = new char[32][64];
        Ball ball = new Ball(0, App.TOPBAR, new PImage(), new Path(grid));
        ball.tick();
        ball.death();
        ball.changeDir(ball.UP);
        ball.changeDir(ball.DOWN);
        ball.changeDir(ball.LEFT);
        ball.changeDir(ball.RIGHT);
    }
}
