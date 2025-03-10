package lawnlayer;

import processing.core.PApplet;
import processing.core.PImage;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;

public class CollisionHandlingTest extends App {
    public char[][] grid = new char[32][64];

    @Test
    public void CollisionHandlingTest() {
        try {
            File f = new File("src/test/java/lawnlayer/EnemiesTest/levelTemplate.txt");
            Scanner scan = new Scanner(f);
            int y = 0;
            // Scan for concrete block
            while (scan.hasNextLine()) {
                String data = scan.nextLine();
                int x = 0;
                for (char block: data.toCharArray()) {
                    grid[y][x] = block;
                    x++;
                }
                y++;
            }
            scan.close();
            this.gridInitialised = true;
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        Enemies enemy = new Enemies("2,2", new PImage(), this.grid);
        enemy.grid = this.grid;
        char[] testBlocks = {' ', 'P', 'G', 'C'};
        for (char block: testBlocks) {
            // NE
            enemy.direction = enemy.NE;
            enemy.grid[2][3] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.NE);
            } else if (block == 'P') {
                assertEquals(enemy.direction, enemy.NW);
                assertEquals(enemy.grid[2][3], 'C');
            } else {
                assertEquals(enemy.direction, enemy.NW);
            }
            enemy.grid[2][3] = ' ';
            enemy.direction = enemy.NE;
            enemy.grid[1][2] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.NE);
            } else {
                assertEquals(enemy.direction, enemy.SE);
            }
            enemy.grid[1][2] = ' ';
            enemy.direction = enemy.NE;
            enemy.grid[1][3] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.NE);
            } else {
                assertEquals(enemy.direction, enemy.SW);
                if (block == 'P') {
                    assertEquals(enemy.grid[1][3], 'C');
                }
            }
            enemy.grid[1][3] = ' ';
            enemy.direction = enemy.NE;
            enemy.grid[1][3] = block;
            enemy.grid[2][3] = block;
            enemy.grid[1][2] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.NE);
            } else {
                assertEquals(enemy.direction, enemy.SW);
                if (block == 'P') {
                    assertEquals(enemy.grid[1][3], 'C');
                }
            }
            enemy.grid[1][3] = ' ';
            enemy.grid[2][3] = ' ';
            enemy.grid[1][2] = ' ';

            // SE
            enemy.direction = enemy.SE;
            enemy.grid[2][3] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.SE);
            } else if (block == 'P') {
                assertEquals(enemy.direction, enemy.SW);
                assertEquals(enemy.grid[2][3], 'C');
            } else {
                assertEquals(enemy.direction, enemy.SW);
            }
            enemy.grid[2][3] = ' ';
            enemy.direction = enemy.SE;
            enemy.grid[3][2] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.SE);
            } else {
                assertEquals(enemy.direction, enemy.NE);
            }
            enemy.grid[3][2] = ' ';
            enemy.direction = enemy.SE;
            enemy.grid[3][3] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.SE);
            } else {
                assertEquals(enemy.direction, enemy.NW);
                if (block == 'P') {
                    assertEquals(enemy.grid[3][3], 'C');
                }
            }
            enemy.grid[3][3] = ' ';
            enemy.direction = enemy.SE;
            enemy.grid[2][3] = block;
            enemy.grid[3][2] = block;
            enemy.grid[3][3] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.SE);
            } else {
                assertEquals(enemy.direction, enemy.NW);
                if (block == 'P') {
                    assertEquals(enemy.grid[3][3], 'C');
                }
            }
            enemy.grid[3][3] = ' ';
            enemy.grid[2][3] = ' ';
            enemy.grid[3][2] = ' ';

            // SW
            enemy.direction = enemy.SW;
            enemy.grid[2][1] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.SW);
            } else if (block == 'P') {
                assertEquals(enemy.direction, enemy.SE);
                assertEquals(enemy.grid[2][1], 'C');
            } else {
                assertEquals(enemy.direction, enemy.SE);
            }
            enemy.grid[2][1] = ' ';
            enemy.direction = enemy.SW;
            enemy.grid[3][2] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.SW);
            } else {
                assertEquals(enemy.direction, enemy.NW);
            }
            enemy.grid[3][2] = ' ';
            enemy.direction = enemy.SW;
            enemy.grid[3][1] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.SW);
            } else {
                assertEquals(enemy.direction, enemy.NE);
                if (block == 'P') {
                    assertEquals(enemy.grid[3][1], 'C');
                }
            }
            enemy.grid[3][1] = ' ';
            enemy.direction = enemy.SW;
            enemy.grid[3][1] = block;
            enemy.grid[3][2] = block;
            enemy.grid[2][1] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.SW);
            } else {
                assertEquals(enemy.direction, enemy.NE);
                if (block == 'P') {
                    assertEquals(enemy.grid[3][1], 'C');
                }
            }
            enemy.grid[3][1] = ' ';
            enemy.grid[2][1] = ' ';
            enemy.grid[3][2] = ' ';

            // NW
            enemy.direction = enemy.NW;
            enemy.grid[2][1] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.NW);
            } else if (block == 'P') {
                assertEquals(enemy.direction, enemy.NE);
                assertEquals(enemy.grid[2][1], 'C');
            } else {
                assertEquals(enemy.direction, enemy.NE);
            }
            enemy.grid[2][1] = ' ';
            enemy.direction = enemy.NW;
            enemy.grid[1][2] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.NW);
            } else {
                assertEquals(enemy.direction, enemy.SW);
            }
            enemy.grid[1][2] = ' ';
            enemy.direction = enemy.NW;
            enemy.grid[1][1] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.NW);
            } else {
                assertEquals(enemy.direction, enemy.SE);
                if (block == 'P') {
                    assertEquals(enemy.grid[1][1], 'C');
                }
            }
            enemy.grid[1][1] = ' ';
            enemy.direction = enemy.NW;
            enemy.grid[1][1] = block;
            enemy.grid[1][2] = block;
            enemy.grid[2][1] = block;
            enemy.collisionHandling();
            if (block == ' ') {
                assertEquals(enemy.direction, enemy.NW);
            } else {
                assertEquals(enemy.direction, enemy.SE);
                if (block == 'P') {
                    assertEquals(enemy.grid[1][1], 'C');
                }
            }
            enemy.grid[1][1] = ' ';
            enemy.grid[2][1] = ' ';
            enemy.grid[1][2] = ' ';
        }

    }
}
