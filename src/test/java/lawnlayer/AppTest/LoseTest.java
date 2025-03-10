package lawnlayer;


import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoseTest extends App {

    @Test
    public void LoseTest() {
    App app = new App();
    app.noLoop(); //optional
    // Tell PApplet to create the worker threads for the program
    PApplet.runSketch(new String[] {"App"}, app);
    app.setup();
    app.delay(1000); //to give time to initialise stuff before drawing begins
    app.lives = 0;
    app.draw();
    }
}
