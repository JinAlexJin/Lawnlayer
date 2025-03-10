package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.Random;

/**
 * Beetle class inherits from Enemies class, it handles
 * all logic for the beetle enemy in game, with added functionality
 * of removing grass compared to Enemies class.
 */
public class Beetle extends Enemies {
  /**
   * Beetle constructor.
   * @param spawn   how beetle will be spawn
   * (random or on a specific tile).
   * @param sprite  PImage of sprite.
   * @param grid    Grid of tiles.
   */
  public Beetle(String spawn, PImage sprite, char[][] grid) {
    super(spawn, sprite, grid);
  }

  /*
   * Check for possible collision
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
    // If hit grass then turn grass back into soil
    // Bounce off in opposite direction
    if (this.direction == NE) {
      if (right != ' ' && up != ' ') {
        this.direction = 2;
        if (upright == 'P') {
          this.grid[yPos - 1][xPos + 1] = 'C';
        } else if (upright == 'G') {
          this.grid[yPos - 1][xPos + 1] = ' ';
        }
      } else if (right != ' ') {
        this.direction = 3;
        if (right == 'P') {
          this.grid[yPos][xPos + 1] = 'C';
        } else if (right == 'G') {
          this.grid[yPos][xPos + 1] = ' ';
        }
      } else if (up != ' ') {
        this.direction = 1;
        if (up == 'P') {
          this.grid[yPos - 1][xPos] = 'C';
        } else if (up == 'G') {
          this.grid[yPos - 1][xPos] = ' ';
        }
      } else if (upright != ' ') {
        this.direction = 2;
        if (upright == 'P') {
          this.grid[yPos - 1][xPos + 1] = 'C';
        } else if (upright == 'G') {
          this.grid[yPos - 1][xPos + 1] = ' ';
        }
      }

    } else if (this.direction == SE) {
      if (right != ' ' && down != ' ') {
        this.direction = 3;
        if (downright == 'P') {
          this.grid[yPos + 1][xPos + 1] = 'C';
        } else if (downright == 'G') {
          this.grid[yPos + 1][xPos + 1] = ' ';
        }
      } else if (right != ' ') {
        this.direction = 2;
        if (right == 'P') {
          this.grid[yPos][xPos + 1] = 'C';
        } else if (right == 'G') {
          this.grid[yPos][xPos + 1] = ' ';
        }
      } else if (down != ' ') {
        this.direction = 0;
        if (down == 'P') {
          this.grid[yPos + 1][xPos] = 'C';
        } else if (down == 'G') {
          this.grid[yPos + 1][xPos] = ' ';
        }
      } else if (downright  != ' '){
        this.direction = 3;
        if (downright == 'P') {
          this.grid[yPos + 1][xPos + 1] = 'C';
        } else if (downright == 'G') {
          this.grid[yPos + 1][xPos + 1] = ' ';
        }
      }

    } else if (this.direction == SW) {
      if (left != ' ' && down != ' ') {
        this.direction = 0;
        if (downleft == 'P') {
          this.grid[yPos + 1][xPos - 1] = 'C';
        }
        else if (downleft == 'G') {
          this.grid[yPos + 1][xPos - 1] = ' ';
        }
      } else if (left != ' ') {
        this.direction = 1;
        if (left == 'P') {
          this.grid[yPos][xPos - 1] = 'C';
        } else if (left == 'G') {
          this.grid[yPos][xPos - 1] = ' ';
        }
      } else if (down != ' ') {
        this.direction = 3;
        if (down == 'P') {
          this.grid[yPos + 1][xPos] = 'C';
        } else if (down == 'G') {
          this.grid[yPos + 1][xPos] = ' ';
        }
      } else if (downleft != ' ') {
        this.direction = 0;
        if (downleft == 'P') {
          this.grid[yPos + 1][xPos - 1] = 'C';
        }
        else if (downleft == 'G') {
          this.grid[yPos + 1][xPos - 1] = ' ';
        }
      }

    } else {
      if (left != ' ' && up != ' ') {
        this.direction = 1;
        if (upleft == 'P') {
          this.grid[yPos - 1][xPos - 1] = 'C';
        } else if (upleft == 'G') {
          this.grid[yPos - 1][xPos - 1] = ' ';
        }
      } else if (left != ' ') {
        this.direction = 0;
        if (left == 'P') {
          this.grid[yPos][xPos - 1] = 'C';
        } else if (left == 'G') {
          this.grid[yPos][xPos - 1] = ' ';
        }
      } else if (up != ' ') {
        this.direction = 2;
        if (up == 'P') {
          this.grid[yPos - 1][xPos] = 'C';
        } else if (up == 'G') {
          this.grid[yPos - 1][xPos] = ' ';
        }
      } else if (upleft != ' ') {
        this.direction = 1;
        if (upleft == 'P') {
          this.grid[yPos - 1][xPos - 1] = 'C';
        } else if (upleft == 'G') {
          this.grid[yPos - 1][xPos - 1] = ' ';
        }
      }
    }
  }
}
