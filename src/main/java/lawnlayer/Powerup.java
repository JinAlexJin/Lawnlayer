package lawnlayer;

import processing.core.PImage;
import processing.core.PApplet;
import java.util.Random;
import java.lang.Math;
import java.util.List;
import java.util.ArrayList;

/**
 * Powerup class handles spawning, activating and resetting powerups.
 */
public class Powerup {
  private final static int MAXDURATION = 10;
  private final static int DELAY = 4;
  private double ballSPeedFactor = 2;
  private double enemySpeedFactor = 0.5;
  private boolean ballSpeedChanged = false;
  private boolean enemySpeedChanged = false;
  private int nextSpawnTime;
  private int activeTime;
  private int x;
  private int y;
  private boolean generated = false;
  private boolean applied = false;
  private Random rand;
  private int powerupConfig;

  /**
   * Powerup constructor, also sets the timer for next powerup spawn.
   * 
   * @param powerupConfig   Indicates which powerups will be spawn.
   */
  public Powerup(int powerupConfig) {
    this.activeTime = 0;
    this.rand = new Random();
    this.powerupConfig = powerupConfig;
    this.nextSpawnTime = (this.rand.nextInt(DELAY) + 7) * App.FPS;
  }

  /**
   * Counts amount of time till new powerup spawn and spawns then,
   * also check whether powerup has been activated then start a countdown
   * till powerup wears off.
   *
   * @param worms       A list of Worm to apply speed change to.
   * @param beetles     A list of Beetle to apply speed change to.
   * @param ball        Ball object to apply speed change to.
   */
  public void tick(List<Worm> worms, List<Beetle> beetles, Ball ball) {
    if (this.generated) {
      // Check if powerup has been consumed, if it has then
      // apply speed changes and start timer for powerup
      if (App.grid[this.y][this.x] != 'A') {
        this.activeTime -= 1;

        if (!applied) {
          if (this.powerupConfig == 2) {
            int random = rand.nextInt(2);
            if (random == 0) {
              enemyPowerup(worms, beetles, this.enemySpeedFactor);
              this.enemySpeedChanged = true;
            } else {
              ballPowerup(ball, this.ballSPeedFactor);
              this.ballSpeedChanged = true;
            }
          } else if (this.powerupConfig == 0) {
            ballPowerup(ball, this.ballSPeedFactor);
            this.ballSpeedChanged = true;
          } else if (this.powerupConfig == 1) {
            enemyPowerup(worms, beetles, this.enemySpeedFactor);
            this.enemySpeedChanged = true;
          }
          this.applied = true;
        }
        this.nextSpawnTime = 0;

        if (this.activeTime == 0) {
          // When powerup wears off revert the speed changes
          this.generated = false;
          if (this.enemySpeedChanged) {
            enemyPowerup(worms, beetles, 1 / this.enemySpeedFactor);
            this.enemySpeedChanged = false;
          } else {
            ballPowerup(ball, 1 / this.ballSPeedFactor);
            this.ballSpeedChanged = false;
          }
          this.applied = false;
          this.nextSpawnTime = (this.rand.nextInt(DELAY) + 7) * App.FPS;
        }
      }
    } else {
      // Count down till the next powerup spawn
      this.nextSpawnTime -= 1;
      if (this.nextSpawnTime == 0) {
        newPowerup();
        this.activeTime = MAXDURATION * App.FPS;
      }
    }
  }

  /*
   * Apply powerup effect to ball by factor of parameter speed.
   */
  private void ballPowerup(Ball ball, double speed) {
    ball.changeSpeed(speed);
  }

  /*
   * Apply powerup effect to enemies by factor of parameter speed.
   */
  private void enemyPowerup(List<Worm> worms, List<Beetle> beetles, double speed) {
    for (Worm worm: worms) {
      worm.changeSpeed(speed);
    }
    for (Beetle beetle: beetles) {
      beetle.changeSpeed(speed);
    }
  }

  /**
   * Display countdown timer on screen.
   * 
   * @param app PApplet used to write on screen.
   */
  public void drawTimer(PApplet app) {
    app.fill(255, 255, 255);
    app.text("Powerup timer: " + (int)Math.floor(this.activeTime / App.FPS), 300, 50);
  }

  /*
   * Generate location of new powerup on a dirt tile.
   */
  private void newPowerup() {
    // Generate powerup location
    this.x = this.rand.nextInt(63) + 1;
    this.y = this.rand.nextInt(31) + 1;
    // If coordinate generated is on concrete or grass then re-run generation
    while (App.grid[this.y][this.x] == 'X' || App.grid[this.y][this.x] == 'G') {
      this.x = this.rand.nextInt(63) + 1;
      this.y = this.rand.nextInt(31) + 1;
    }
    App.grid[this.y][this.x] = 'A';
    this.generated = true;
  }
}
