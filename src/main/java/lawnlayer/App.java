package lawnlayer;

import org.checkerframework.checker.units.qual.A;
import processing.core.PApplet;
import processing.core.PImage;
import processing.data.JSONObject;
import processing.data.JSONArray;
import processing.core.PFont;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.lang.Math;

/**
 * App is the class that handles the connection between different
 * objects and levels, it also displays the game on screen.
 */
public class App extends PApplet {

  public static final int WIDTH = 1280;
  public static final int HEIGHT = 720;
  public static final int SPRITESIZE = 20;
  public static final int TOPBAR = 80;
  public static final int GRIDHEIGHT = 32;
  public static final int GRIDWIDTH = 64;

  public static final int FPS = 60;

  public static char[][] grid = new char[GRIDHEIGHT][GRIDWIDTH];
  public boolean gridInitialised = false;

  public String configPath;
  public JSONObject json;
  public JSONArray levels;
  public int lives;

  public int level;
  public int goal;

  public PImage grass;
  public PImage concrete;
  public PImage worm;
  public PImage beetle;
  public PImage powerup;
  public PFont font;

  public Ball ball;
  public Path path;
  public Powerup powerupClass;
  public int powerupConfig; // -1: none, 0: ball, 1: enemy, 2: both
  public static List<int[]> pathList;
  public List<Worm> worms = new ArrayList<Worm>();
  public List<Beetle> beetles = new ArrayList<Beetle>();

  /**
   * Constructor for App
   */
  public App() {
    this.configPath = "config.json";
  }

  /**
   * Initialise the setting of the window size.
   */
  public void settings() {
    size(WIDTH, HEIGHT);
  }

  /**
   * Load all resources such as images.
   * Initialise the elements such as the player, enemies and map elements.
   */
  public void setup() {
    frameRate(FPS);

    // Load images during setup
    this.grass = loadImage(this.getClass().getResource("grass.png").getPath());
    this.concrete = loadImage(this.getClass().getResource("concrete_tile.png").getPath());
    this.worm = loadImage(this.getClass().getResource("worm.png").getPath());
    this.beetle = loadImage(this.getClass().getResource("beetle.png").getPath());
    this.powerup = loadImage(this.getClass().getResource("powerup.png").getPath());

    processConfig(); // Read config file
    this.level = 0; // First level to play
    this.font = createFont("AquaKana.ttc", 128); // Font for writing in top bar

    setUpLevel(); // Set up the first level
  }

  /**
   * Draw all elements in the game by current frame,
   * also call all mobs' logic.
   */
  public void draw() {
    drawBackground(levels.getJSONObject(this.level).getString("outlay")); // Draw current grid
    if (this.powerupConfig != -1) {
      this.powerupClass.tick(this.worms, this.beetles, this.ball);
      this.powerupClass.drawTimer(this);
    }

    // Process and draw all enemies
    for (Worm worm: this.worms) {
      worm.tick();
      worm.draw(this);
    }
    for (Beetle beetle: this.beetles) {
      beetle.tick();
      beetle.draw(this);
    }

    // Process and draw ball, and check if ball collided with its own path
    if (this.ball.tick() == true) {
      this.path.collided = false;
      this.lives -= 1;
    }
    this.ball.draw(this);
    pathList = ball.getPathList();

    // Check if red path has propagated onto ball, if so initiate death sequence
    if (this.path.deathCheck() == true) {
      this.ball.death();
      this.lives -= 1;
    }

    // Level completed
    if (goalComplete()) {
      nextLevel();
    }

    // All lives used
    if (this.lives == 0) {
      background(0, 0, 0);
      textSize(125);
      fill(255, 255, 255);
      text("Game over", WIDTH / 4, HEIGHT / 4, WIDTH * 3 / 4, HEIGHT * 3 / 4);
    }
  }

  /*
   * Clear current level and set up next level
   */
  public void nextLevel() {
    // Check if last level
    if (this.level == this.levels.size() - 1) {
      background(0, 0, 0);
      textSize(160);
      fill(255, 255, 255);
      text("You win", WIDTH / 4, HEIGHT / 4, WIDTH * 3 / 4, HEIGHT * 3 / 4);
    } else {
      this.level += 1;
      // Clear enemies and reset grid
      this.beetles.clear();
      this.worms.clear();
      grid = new char[GRIDHEIGHT][GRIDWIDTH];
      this.gridInitialised = false; // Flag for re-reading concrete 
      setUpLevel(); // Set up next level
    }
  }

  /*
   * Create ball and eneny objects by interpreting the config file
   */
  private void setUpLevel() {
    drawBackground(this.levels.getJSONObject(this.level).getString("outlay")); // Initialise the grid for current level

    // Initialise objects
    this.path = new Path(grid);
    this.ball = new Ball(0, TOPBAR, loadImage(this.getClass().getResource("ball.png").getPath()), path);

    JSONObject curLevel = this.levels.getJSONObject(this.level);
    this.goal = (int)Math.floor(curLevel.getDouble("goal") * 100.0); // Convert goal to percentage
    JSONArray enemies = curLevel.getJSONArray("enemies");
    // Create all enemies
    for (int i = 0; i < enemies.size(); i++) {
      JSONObject enemy = enemies.getJSONObject(i);
      if (enemy.getInt("type") == 0) {
        this.worms.add(new Worm(enemy.getString("spawn"), this.worm, App.grid)); 
      } else {
        this.beetles.add(new Beetle(enemy.getString("spawn"), this.beetle, App.grid));
      }
    }
    // Set up powerup
    String powerup = curLevel.getString("powerups");
    if (powerup.equals("none")) {
      this.powerupConfig = -1;
    } else if (powerup.equals("ball")) {
      this.powerupConfig = 0;
    } else if (powerup.equals("enemy")) {
      this.powerupConfig = 1;
    } else {
      this.powerupConfig = 2;
    }
    this.powerupClass = new Powerup(this.powerupConfig);
  }

  /*
   * Check whether enough grass has been filled to reach level goal, update top bar progress text
   */
  private boolean goalComplete() {
    int countGrass = 0;
    int countDirt = 0;

    // Iterate through grid to check for filled grass and count
    for (char[] row: grid) {
      for (char block: row) {
        if (block == 'G') {
          countGrass += 1;
        } else if (block != 'X') {
          countDirt += 1;
        }
      }
    }

    // Calculate percentage grass captured
    int capturedRatio = Math.round(countGrass * 100 / (countGrass + countDirt));    

    // Update progress text in top bar
    textSize(40);
    fill(255, 255, 255);
    text(capturedRatio + "%/" + this.goal + "%", 1000, 50);

    // Check whether enough grass has been captured
    if (capturedRatio >= this.goal) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Key press detection
   */
  public void keyPressed() {
    if (key == CODED) {
      this.ball.pressingKey = true; // Record holding key
      this.ball.changeDir(keyCode); // Change ball movement
    }
  }

  /**
   * Key holding detection
   */
  public void keyReleased() {
    this.ball.pressingKey = false; // No longer holding key
  }

  /*
   * Read config file
   */
  private void processConfig() {
    this.json = loadJSONObject(this.configPath);
    this.levels = json.getJSONArray("levels");
    this.lives = json.getInt("lives");
  }

  /*
   * Draw background, top bar and record grid
   */
  private void drawBackground(String fileName) {
    background(108, 73, 37); // Background colour

    // Top bar information
    textSize(40);
    fill(255, 255, 255);
    text("Lives: " + this.lives, 30, 50);
    textSize(20);
    text("Level " + (this.level + 1), 1200, 70);

    // Initialise grid if have not done so
    if (this.gridInitialised == false) {
      try {
        File f = new File(fileName);
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
    }

    // Draw different tiles according to grid
    for (int y = 0; y < grid.length; y++) {
      for (int x = 0; x < grid[y].length; x++) {
        // X: concrete, P: path, G: grass, C: collided path(propagating), A: powerup
        if (grid[y][x] == 'X') {
          image(this.concrete, x * SPRITESIZE, y * SPRITESIZE + TOPBAR);
          int[] toAdd = {x, y};
        } else if (grid[y][x] == 'P') {
          fill(108, 73, 37);
          stroke(108, 73, 37);
          rect(x * SPRITESIZE, y * SPRITESIZE + TOPBAR, SPRITESIZE, SPRITESIZE);
          fill(0, 255, 0);
          rect(x * SPRITESIZE + 2, y * SPRITESIZE + TOPBAR + 2, 16, 16);
        } else if (grid[y][x] == 'G') {
          image(this.grass, x * SPRITESIZE, y * SPRITESIZE + TOPBAR);
        } else if (grid[y][x] == 'C') {
          fill(255, 0, 0);
          rect(x * SPRITESIZE + 2, y * SPRITESIZE + TOPBAR + 2, 16, 16);
        } else if (grid[y][x] == 'A') {
          image(this.powerup, x * SPRITESIZE, y * SPRITESIZE + TOPBAR);
        }
      }
    }
  }

  public char[][] getGrid() {
    return grid;
  }

  public void setGrid(int y, int x, char val) {
    grid[y][x] = val;
  }

  public void replaceGrid(char[][] grid) {
    App.grid = grid;
  }

  public static List<int[]> getPathList() {
      return pathList;
  }
  /*
   * Main method
   */
  public static void main(String[] args) {
    PApplet.main("lawnlayer.App");
  }
}
