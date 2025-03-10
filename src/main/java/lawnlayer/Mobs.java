package lawnlayer;

import processing.core.PApplet;

/**
 * Interface for all mobile objects in game, which includes
 * player and all enemies.
 */
public interface Mobs {
  /**
   * Draw object
   * @param app PApplet for image() method.
   */
  public void draw(PApplet app);
  /**
   * Changes the speed mob is travelling at by a factor
   * @param factor the amount speed of mob changes by
   */
  public void changeSpeed(double factor);
  /**
   * Move along current direction
   */
  public void continueInDirection();
}
