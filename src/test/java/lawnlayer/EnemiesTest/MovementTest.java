package lawnlayer;


import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MovementTest extends App {

    @Test
    public void MovementTest() {
        int startX = 2 * App.SPRITESIZE;
        int startY = 2 * App.SPRITESIZE + App.TOPBAR;
        char[][] grid = new char[32][64];
        Enemies enemy = new Enemies("2,2", new PImage(), grid);
        enemy.direction = enemy.NE;
        enemy.continueInDirection();
        assertEquals(enemy.x, startX + enemy.speed);
        assertEquals(enemy.y, startY - enemy.speed);
        enemy = new Enemies("2,2", new PImage(), grid);
        enemy.direction = enemy.SE;
        enemy.continueInDirection();
        assertEquals(enemy.x, startX + enemy.speed);
        assertEquals(enemy.y, startY + enemy.speed);
        enemy = new Enemies("2,2", new PImage(), grid);
        enemy.direction = enemy.SW;
        enemy.continueInDirection();
        assertEquals(enemy.x, startX - enemy.speed);
        assertEquals(enemy.y, startY + enemy.speed);
        enemy = new Enemies("2,2", new PImage(), grid);
        enemy.direction = enemy.NW;
        enemy.continueInDirection();
        assertEquals(enemy.x, startX - enemy.speed);
        assertEquals(enemy.y, startY - enemy.speed);
    }
}
