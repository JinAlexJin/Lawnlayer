package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.Random;

/**
 * Enemies is the base class for all enemies created in game,
 * it handles all basic logics of enemies, including moving and collision.
 */
public class Enemies implements Mobs{
  protected int speed;

  public int x;
  public int y;

  public int NE = 0;
  public int SE = 1;
  public int SW = 2;
  public int NW = 3;
  public int direction;
  public char[][] grid = new char[App.GRIDHEIGHT][App.GRIDWIDTH];

  public PImage sprite;

  /**
   * Enemies constructor.
   * @param spawn   how beetle will be spawn
   * (random or on a specific tile).
   * @param sprite  PImage of sprite.
   * @param grid    Grid of tiles.
   */
  public Enemies(String spawn, PImage sprite, char[][] grid) {
    this.speed = 2;
    this.sprite = sprite;
    this.grid = grid;
    initialisePosition(spawn);
  }

  /**
   * Move the object and check if it has bounced or hit a path
   */
  public void tick() {
    // Check if need to change direction when object is on the grid
    this.grid = App.grid;
    if (this.x % App.SPRITESIZE == 0 && this.y % App.SPRITESIZE == 0)
    {
      collisionHandling();
    }
    continueInDirection(); // Move along current direction
  }

  /**
   * Draw object
   * @param app PApplet for image() method.
   */
  public void draw(PApplet app) {
    app.image(this.sprite, this.x, this.y);
  }

  /**
   * Changes the speed ball is travelling at by a factor
   * @param factor the amount speed of ball changes by
   */
  public void changeSpeed(double factor) {
    if (this.x % 2 != 0) {
      continueInDirection();
    }
    this.speed *= factor;
  }

  /**
   * Move along current direction
   */
  public void continueInDirection() {
    // Move in direction by speed defined
    if (this.direction == NE) {
      this.y -= this.speed;
      this.x += this.speed;
    } else if (this.direction == SE) {
      this.y += this.speed;
      this.x += this.speed;
    } else if (this.direction == SW) {
      this.y += this.speed;
      this.x -= this.speed;
    } else {
      this.y -= this.speed;
      this.x -= this.speed;
    }
  }

  /*
   * Initialise object position
   */
  protected void initialisePosition(String spawn) {
    Random rand = new Random();
    // Initialise starting location by randomly generate x and y coordinates
    if (spawn.equals("random")) {
      this.x = (rand.nextInt(App.GRIDWIDTH - 1) + 1) * App.SPRITESIZE;
      this.y = (rand.nextInt(App.GRIDHEIGHT - 1) + 1) * App.SPRITESIZE + App.TOPBAR;
      // If coordinate generated is on concrete then re-run generation
      while (this.grid[(this.y - App.TOPBAR) / App.SPRITESIZE][this.x / App.SPRITESIZE] == 'X') {
        this.x = (rand.nextInt(App.GRIDWIDTH - 1) + 1) * App.SPRITESIZE;
        this.y = (rand.nextInt(App.GRIDHEIGHT - 1) + 1) * App.SPRITESIZE + App.TOPBAR;
      }
    } else {
        // Process given starting location
        String[] coordinates = spawn.split(",");
        this.x = Integer.parseInt(coordinates[0]) * App.SPRITESIZE;
        this.y = Integer.parseInt(coordinates[1]) * App.SPRITESIZE + App.TOPBAR;
    }
    this.direction = rand.nextInt(4); // Generate direction
  }

  /*
   * Check for possibel collision
   */
  public void collisionHandling() {
    int xPos = this.x / App.SPRITESIZE;
    int yPos = (this.y - App.TOPBAR) / App.SPRITESIZE;
    char right = this.grid[yPos][xPos + 1];
    char up = this.grid[yPos - 1][xPos];
    char down = this.grid[yPos + 1][xPos];
    char left = this.grid[yPos][xPos - 1];
    char upright = this.grid[yPos - 1][xPos + 1];
    char upleft = this.grid[yPos - 1][xPos - 1];
    char downright = this.grid[yPos + 1][xPos + 1];
    char downleft = this.grid[yPos + 1][xPos - 1];

    // Check the direction object is travelling in
    // Then check if object hits side or corner
    // If hit path then mark path hit as collided
    // Bounce off in opposite direction
    if (this.direction == NE) {
      if (right != ' ' && up != ' ') {
        this.direction = 2;
        if (upright == 'P') {
          this.grid[yPos - 1][xPos + 1] = 'C';
        } 
      } else if (right != ' ') {
        this.direction = 3;
        if (right == 'P') {
          this.grid[yPos][xPos + 1] = 'C';
        }
      } else if (up != ' ') {
        this.direction = 1;
        if (up == 'P') {
          this.grid[yPos - 1][xPos] = 'C';
        }
      } else if (upright != ' ') {
        this.direction = 2;
        if (upright == 'P') {
          this.grid[yPos - 1][xPos + 1] = 'C';
        }
      }

    } else if (this.direction == SE) {
      if (right != ' ' && down != ' ') {
        this.direction = 3;
        if (downright == 'P') {
          this.grid[yPos + 1][xPos + 1] = 'C';
        }
      } else if (right != ' ') {
        this.direction = 2;
        if (right == 'P') {
          this.grid[yPos][xPos + 1] = 'C';
        }
      } else if (down != ' ') {
        this.direction = 0;
        if (down == 'P') {
          this.grid[yPos + 1][xPos] = 'C';
        }
      } else if (downright != ' ') {
        this.direction = 3;
        if (downright == 'P') {
          this.grid[yPos + 1][xPos + 1] = 'C';
        }
      }
    }

    else if (this.direction == SW) {
      if (left != ' ' && down != ' ') {
        this.direction = 0;
        if (downleft == 'P') {
          this.grid[yPos + 1][xPos - 1] = 'C';
        }
      } else if (left != ' ') {
        this.direction = 1;
        if (left == 'P') {
          this.grid[yPos][xPos - 1] = 'C';
        }
      } else if (down != ' ') {
        this.direction = 3;
        if (down == 'P') {
          this.grid[yPos + 1][xPos] = 'C';
        }
      } else if (downleft != ' ') {
        this.direction = 0;
        if (downleft == 'P') {
          this.grid[yPos + 1][xPos - 1] = 'C';
        }
      }

    } else {
      if (left != ' ' && up != ' ') {
        this.direction = 1;
        if (upleft == 'P') {
          this.grid[yPos - 1][xPos - 1] = 'C';
        }
      } else if (left != ' ') {
        this.direction = 0;
        if (left == 'P') {
          this.grid[yPos][xPos - 1] = 'C';
        }
      } else if (up != ' ') {
        this.direction = 2;
        if (up == 'P') {
          this.grid[yPos - 1][xPos] = 'C';
        }
      } else if (upleft != ' ') {
        this.direction = 1;
        if (upleft == 'P') {
          this.grid[yPos - 1][xPos - 1] = 'C';
        }
      }
    }
  }
}
