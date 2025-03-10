package lawnlayer;

import java.util.*;

/**
 * Path class keeps track of status of collisions when enemies
 * collide with player path.
 */
public class Path {
  public int countdown = 3;
  public boolean collided = false;
  public char[][] grid;
  public int pathListIndex;

  public Path(char[][] grid) {
    this.countdown = 3;
    this.collided = false;
    this.grid = grid;
  }
    /**
     * Checks if red path has propagated onto player,
     * if its has then player is dead and return true,
     * else return false.
     *
     * @return true if player has died from enemy collision,
     * false if player is still alive
     */
    public boolean deathCheck() {
      // Check for new collision
      if (collided == false) {
        collisionCheck();
      }

      if (collided == true && App.pathList.size() != 0) {
        // Path propagating
        this.countdown -= 1; // 3 pixels per tile
        if (this.countdown == 0) {
          if (this.pathListIndex >= App.pathList.size()) {
            // Player is dead, reset flags, return true
            this.countdown = 3;
            this.collided = false;
            return true;
          } else {
            // Propagate path, reset countdown
            int xPos = App.pathList.get(this.pathListIndex)[0];
            int yPos = App.pathList.get(this.pathListIndex)[1];
            this.pathListIndex++;
            App.grid[yPos][xPos] = 'C';
            this.countdown = 3;
          }
        }
      }
      return false;
    }

  /*
   * Check if path is already collided
   */
  private void collisionCheck() {
    // Search for C value in grid
    for (int y = 0; y < App.grid.length; y++) {
      for (int x = 0; x < App.grid[y].length; x++) {
        if (App.grid[y][x] == 'C') {
          this.collided = true;
          int xPos = x;
          int yPos = y;
          setIndex(xPos, yPos);
        }
      }
    }
  }

  /*
   * Find the index of collided tile
   */
  private void setIndex(int xPos, int yPos) {
    List<int[]> pathList = App.getPathList();
    for (int i = 0; i < pathList.size(); i++) {
      if (pathList.get(i)[0] == xPos && pathList.get(i)[1] == yPos) {
        this.pathListIndex = i;
        return;
      }
    }
  }
}
