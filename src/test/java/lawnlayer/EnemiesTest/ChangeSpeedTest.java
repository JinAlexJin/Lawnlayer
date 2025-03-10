package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ChangeSpeedTest extends App {

    @Test
    public void ChangeSpeedTest() {
        int startX = 2 * App.SPRITESIZE;
        int startY = 2 * App.SPRITESIZE + App.TOPBAR;
        Enemies enemy = new Enemies("2,2", new PImage(), new char[32][64]);
        int speed = enemy.speed;
        enemy.changeSpeed(0.5);
        assertEquals(enemy.speed, speed / 2);
        enemy.tick();
        enemy.changeSpeed(2);
        assertEquals(enemy.speed, speed);
    }
}
