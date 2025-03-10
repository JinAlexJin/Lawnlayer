package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.Random;

/**
 * Worm class inherits from Enemies class,
 * it handles all movement and collision for worms.
 */
public class Worm extends Enemies {
  /**
   * Worm constructor.
   * @param spawn   how beetle will be spawn
   * (random or on a specific tile)
   * @param sprite  PImage of sprite
   * @param grid    Grid of tiles.
   */
    public Worm(String spawn, PImage sprite, char[][] grid) {
        super(spawn, sprite, grid);
    }
}
