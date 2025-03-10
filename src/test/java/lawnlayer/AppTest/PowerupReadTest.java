package lawnlayer;


import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PowerupReadTest extends App {

    @Test
    public void PowerupReadTest() {
    App app = new App();
    app.noLoop(); //optional
    // Tell PApplet to create the worker threads for the program
    PApplet.runSketch(new String[] {"App"}, app);
    app.configPath = "src/test/java/lawnlayer/AppTest/config.json";
    app.setup();
    app.draw();
    app.nextLevel();
    app.delay(1000);
    app.nextLevel();
    app.draw();
    app.delay(1000);
    app.nextLevel();
    app.draw();
    app.delay(1000);
    }
}
