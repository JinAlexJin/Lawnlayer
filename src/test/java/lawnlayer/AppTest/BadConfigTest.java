package lawnlayer;


import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BadConfigTest extends App {

    @Test
    public void GoalCompleteTest() {
    App app = new App();
    app.noLoop(); //optional
    // Tell PApplet to create the worker threads for the program
    PApplet.runSketch(new String[] {"App"}, app);
    app.configPath = "src/test/java/lawnlayer/AppTest/badConfig.json";
    app.setup();
    app.delay(1000);
    }
}
