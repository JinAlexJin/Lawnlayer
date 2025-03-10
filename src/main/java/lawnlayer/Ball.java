package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.List;
import java.util.ArrayList;

/**
 * Ball is the class that handles all the moving and collision
 * logics for ball which is controlled by the player.
 */
public class Ball implements Mobs {
  public int iniX;
  public int iniY;
  public int x;
  public int y;
  public int direction;
  public int dirBuffer;
  public int UP = 38;
  public int DOWN = 40;
  public int LEFT = 37;
  public int RIGHT = 39;
  public int speed;
  public boolean pressingKey;
  public List<int[]> pathList;

  public PImage sprite;
  public Path path;

  /**
   * Ball constructor
   *
   * @param x       The starting x position for ball
   * @param y       The starting y position for ball
   * @param sprite  The PImage of ball
   * @param path    Class path used to reset collision detection
   */
  public Ball(int x, int y, PImage sprite, Path path)
  {
    // Starting position and direction
    this.x = x;
    this.iniX = x;
    this.y = y;
    this.iniY = y;
    this.direction = 0;
    this.speed = 2;
    this.pathList = new ArrayList<int[]>();

    this.sprite = sprite; // Ball PImage
    this.path = path; // Path class
  }

  /**
   * Handles ball movement, including moving along the tiles,
   * changing direction, complete path and fill grass.
   * This method will return true if ball has collided with its own path,
   * and return false otherwise.
   * @return true if ball has collided with its own path, false otherwise.
   */
  public boolean tick() {
    // Check if ball is currently alligned with a tile
    boolean onGrid = (this.x % 20 == 0 && this.y % 20 == 0);
    if (onConcrete() && onGrid) {
      // When on concrete check if player is pressing direction key
      if (!this.pressingKey) {
        this.direction = 0; // When not pressing the ball stays stationary
      } else {
        this.direction = this.dirBuffer; // When pressing change direction
      }
    } else if (onGrid) {
      // Tile that is currently on the grid at this position
      char gridInfo = App.grid[(this.y - App.TOPBAR) / App.SPRITESIZE][this.x / App.SPRITESIZE];

      if (gridInfo == ' ' || gridInfo == 'A') {
        // Dirt tile, update the tile to P, add current position to list that tracks current path of ball
        App.grid[(this.y - App.TOPBAR) / App.SPRITESIZE][this.x / App.SPRITESIZE] = 'P';
        int[] toAdd = {this.x / App.SPRITESIZE, (this.y - App.TOPBAR) / App.SPRITESIZE};
        this.pathList.add(toAdd);
      } else if (gridInfo == 'P' || gridInfo == 'C') {
        // Ball crashes into its own path, causes death
        death();
        return true;
      }
      // Ball moves towards next direction when a tile is finished
      this.direction = this.dirBuffer;

      updateGrass(); // Check if path has been completed
    }

    continueInDirection(); // Continue moving in current direction

    return false; // There is no collision
  }

  /**
   * Draw ball in its current position
   *
   * @param app PApplet required for image() method.
   */
  public void draw(PApplet app) {
    app.image(this.sprite, this.x, this.y);
  }

  /**
   * When player loses a life, reset ball to starting position,
   * reset path drawn to soil, and remove path recorded.
   */
  public void death()
  {
    // Reset ball starting position and direction
    this.x = this.iniX;
    this.y = this.iniY;
    this.direction = 0;
    this.dirBuffer = 0;

    // Remove path on grid before death
    for (int i = 0; i < this.pathList.size(); i++) {
      int x = this.pathList.get(i)[0];
      int y = this.pathList.get(i)[1];
      App.grid[y][x] = ' ';
    }
    // Clear path
    this.pathList.removeAll(this.pathList);
  }

  /**
   * Changes the speed ball is travelling at by a factor
   * @param factor the amount speed of ball changes by
   */
  public void changeSpeed(double factor) {
    // Makes sure ball will be on grid for detecting next tile
    if (this.x % 4 != 0 || this.y % 4 != 0) {
      continueInDirection();
    }
    this.speed *= factor;
  }

  /**
   * Handle player input direction change
   * @param key contains the value of the most recent
   * key on the keyboard that was used
   */
  public void changeDir(int key)
  {
    if (key == UP && this.direction != DOWN) {
      this.dirBuffer = UP;
    } else if (key == DOWN && this.direction != UP) {
      this.dirBuffer = DOWN;
    } else if (key == LEFT && this.direction != RIGHT) {
      this.dirBuffer = LEFT;
    } else if (key == RIGHT && this.direction != LEFT) {
      this.dirBuffer = RIGHT;
    }
  }

  /**
   * Move along current direction
   */
  public void continueInDirection()
  {
    if (this.direction == UP && this.y != App.TOPBAR) {
      this.y -= this.speed;
    } else if (this.direction == DOWN && this.y != App.HEIGHT - App.SPRITESIZE) {
      this.y += this.speed;
    } else if (this.direction == LEFT && this.x != 0) {
      this.x -= this.speed;
    } else if (this.direction == RIGHT && this.x != App.WIDTH - App.SPRITESIZE) {
      this.x += this.speed;
    }
  }

  /*
   * Check if a path has been completed
   */
  private void updateGrass()
  {
    // Get coordinates of block ball is about to reach
    int xPos, yPos;
    if (this.direction == UP) {
      xPos = this.x / App.SPRITESIZE;
      yPos = (this.y - App.TOPBAR - App.SPRITESIZE) / App.SPRITESIZE;
    } else if (this.direction == DOWN){
      xPos = this.x / App.SPRITESIZE;
      yPos = (this.y - App.TOPBAR + App.SPRITESIZE) / App.SPRITESIZE;
    } else if (this.direction == LEFT) {
      xPos = (this.x - App.SPRITESIZE) / App.SPRITESIZE;
      yPos = (this.y - App.TOPBAR) / App.SPRITESIZE; 
    } else {
      xPos = (this.x + App.SPRITESIZE) / App.SPRITESIZE;
      yPos = (this.y - App.TOPBAR) / App.SPRITESIZE;
    }

    // If ball reaches concrete or grass block, a path has been completed
    if (App.grid[yPos][xPos] == 'X' || App.grid[yPos][xPos] == 'G') {
      // Update path in grid to grass
      for (int i = 0; i < this.pathList.size(); i++) {
        int x = this.pathList.get(i)[0];
        int y = this.pathList.get(i)[1];
        App.grid[y][x] = 'G';
      }
      // Clear any collision propagation and reset path
      this.path.collided = false;
      this.pathList.clear();
    }
  }

  /*
   * Check if ball is on concrete
   */
  private boolean onConcrete()
  {
    // Calculate crrent position on grid
    int xPos = this.x / App.SPRITESIZE;
    int yPos = (this.y - App.TOPBAR) / App.SPRITESIZE;
    // Check for concrete
    if (App.grid[yPos][xPos] == 'X')
    {
      return true;
    }
    else
    {
      return false;
    }
  }

  public List<int[]> getPathList() {
    return this.pathList;
  }

}
