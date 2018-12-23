import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.awt.Toolkit; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Pong_4X extends PApplet {

/**
 * Pong! 4X
 * An eXtremely multiplayer version of
 * the first computer game in history
 *
 * Designed by Jezz Lucena
 * jezzlucena@gmail.com
 */

 

/* Variable Initialization */

// Game mode boolean
boolean splash = true;
boolean gameover = false;
boolean instructions = true;

// Keypress capture booleans
boolean topPaddleLeft = false;
boolean topPaddleRight = false;
boolean bottomPaddleLeft = false;
boolean bottomPaddleRight = false;

boolean rightPaddleUp = false;
boolean rightPaddleDown = false;
boolean leftPaddleUp = false;
boolean leftPaddleDown = false;

// Ball direction booleans
boolean up = false;
boolean down = false;
boolean right = false;
boolean left = false;

// Player selection booleans
boolean topPlayer = false;
boolean rightPlayer = true;
boolean bottomPlayer = false;
boolean leftPlayer = true;

// Level counter
int stage = 1;

// Ball collision counter
int bouncesCounter = 0;

// Player score counters
int topScore = 0;
int bottomScore = 0;
int rightScore = 0;
int leftScore = 0;

// Ball speed - it increases exponentially
// with level progression
float speedFactor = 0;

// Paddle instances
hPaddle topPaddle;
hPaddle bottomPaddle;
vPaddle rightPaddle;
vPaddle leftPaddle;

// Ball
Ball pongBall;

/**
 * Variable initialization
 */
public void setup(){
  frameRate(100);
  noStroke();

  topPaddle = new hPaddle();
  bottomPaddle = new hPaddle();
  rightPaddle = new vPaddle();
  leftPaddle = new vPaddle();

  pongBall = new Ball();

  bouncesCounter = 0;

  leftPaddle.x = 0;
  rightPaddle.x = 490;

  topPaddle.y = 0;
  bottomPaddle.y = 490;

  size(500, 500);
}

/**
 * Keypress Capture
 */
public void keyPressed(){
  if(keyCode == ENTER){
    if(splash){
      splash = false;
      return;
    }

    if(gameover){
      instructions = true;
      pongBall.init();

      gameover=false;
      return;
    }
    instructions = !instructions;
  }

  // Instruction screen keypress capture
  if(instructions){
    if(keyCode == UP){
      topPlayer = !topPlayer;
    }
    if(keyCode == RIGHT){
      rightPlayer = !rightPlayer;
    }
    if(keyCode == DOWN){
      bottomPlayer = !bottomPlayer;
    }
    if(keyCode == LEFT){
      leftPlayer = !leftPlayer;
    }
  }

  // Top player's keypress capture
  if(key == 'x' || key == 'X'){
    topPaddleLeft = true;
  }
  if(key == 'c' || key == 'C'){
    topPaddleRight = true;
  }

  // Rigth player's keypress capture
  if(key == 'o' || key == 'O'){
    rightPaddleUp = true;
  }
  if(key == 'l' || key == 'L'){
    rightPaddleDown = true;
  }

  // Bottom player's keypress capture
  if(key == 'n' || key == 'N'){
    bottomPaddleLeft = true;
  }
  if(key == 'm' || key == 'M'){
    bottomPaddleRight = true;
  }

  // Left player's keypress capture
  if(key == 'q' || key == 'Q'){
    leftPaddleUp = true;
  }
  if(key == 'a' || key == 'A'){
    leftPaddleDown = true;
  }
}

/**
 * Key release capture
 */
public void keyReleased(){
  if(key == 'x' || key == 'X'){
    topPaddleLeft = false;
  }
  if(key == 'c' || key == 'C'){
    topPaddleRight = false;
  }

  if(key == 'o' || key == 'O'){
    rightPaddleUp = false;
  }
  if(key == 'l' || key == 'L'){
    rightPaddleDown = false;
  }

  if(key == 'n' || key == 'N'){
    bottomPaddleLeft = false;
  }
  if(key == 'm' || key == 'M'){
    bottomPaddleRight = false;
  }

  if(key == 'q' || key == 'Q'){
    leftPaddleUp = false;
  }
  if(key == 'a' || key == 'A'){
    leftPaddleDown = false;
  }
}

/**
 * Function that centralizes gameover and score display
 */
public void showScore(String playerName, int playerScore, int scoreCounter, int x, int y){
  textSize(18);
  text(playerName + " player's Score: " + playerScore, 15, 290 + (scoreCounter*40));
}

public void beep(){
  Toolkit.getDefaultToolkit().beep();
}

/**
 * Function that draws the sceeen for all game modes
 */
public void draw(){
  if(splash){
    showSplashScreen();
    return;
  }else if(instructions){
    showInstructionsScreen();
  }else if(gameover){
    showGameOverScreen();
  }else{
    showGameRunningScreen();
  }

  fill(255, 255, 255);
  textSize(15);
  text("Lv " + stage, 20, 30);
}

/**
 * Function that displays instructions
 */
public void showInstructionsScreen(){
  background(0);
  fill(255, 255, 255);

  textSize(50);
  text("Instructions", 110, 150);

  textSize(32);
  text("Use the arrows to", 115, 220);
  text("select the player", 125, 260);
  text("and Enter to start", 115, 300);
  text("or pause the game", 110, 340);

  textSize(15);

  if(topPlayer){
    fill(255, 255, 255);
  }else{
    fill(100, 100, 100);
  }
  rect(215, 0, 70, 10);
  text("X <-", 196, 25);
  text("-> C", 267, 25);

  if(rightPlayer){
    fill(255, 255, 255);
  }else{
    fill(100, 100, 100);
  }
  rect(490, 215, 10, 70);
  text("O", 476, 208);
  text("^", 477, 225);
  text("I", 480, 235);
  text("I", 480, 275);
  text("v", 478, 285);
  text("L", 477, 302);

  if(bottomPlayer){
    fill(255, 255, 255);
  }else{
    fill(100, 100, 100);
  }
  rect(215, 490, 70, 10);
  text("N <-", 196, 485);
  text("-> M", 267, 485);

  if(leftPlayer){
    fill(255, 255, 255);
  }else{
    fill(100, 100, 100);
  }
  rect(0, 215, 10, 70);
  text("Q", 13, 208);
  text("^", 14, 225);
  text("I", 17, 235);
  text("I", 17, 275);
  text("v", 15, 285);
  text("A", 14, 302);
}

/**
 * Function that draws the game screen
 */
public void showGameRunningScreen(){
  background(0);

  if(topPlayer){
    topPaddle.show();
  }

  if(rightPlayer){
    rightPaddle.show();
  }

  if(bottomPlayer){
    bottomPaddle.show();
  }

  if(leftPlayer){
    leftPaddle.show();
  }

  if(topPaddleLeft){
    topPaddle.moveLeft();
  }
  if(topPaddleRight){
    topPaddle.moveRight();
  }

  if(rightPaddleUp){
    rightPaddle.moveUp();
  }
  if(rightPaddleDown){
    rightPaddle.moveDown();
  }

  if(bottomPaddleLeft){
    bottomPaddle.moveLeft();
  }
  if(bottomPaddleRight){
    bottomPaddle.moveRight();
  }

  if(leftPaddleUp){
    leftPaddle.moveUp();
  }
  if(leftPaddleDown){
    leftPaddle.moveDown();
  }

  pongBall.move();
  pongBall.bounce();
  pongBall.show();

  if(pongBall.y > 508){
      topScore++;
      rightScore++;
      leftScore++;
      gameover = true;
  }
  if(pongBall.x < -8){
      topScore++;
      rightScore++;
      bottomScore++;
      gameover = true;
  }
  if(pongBall.y < -8){
      rightScore++;
      bottomScore++;
      leftScore++;
      gameover = true;
  }
  if(pongBall.x > 508){
      topScore++;
      bottomScore++;
      leftScore++;
      gameover = true;
  }
}

/**
 * Function that displays gameover screen
 */
public void showGameOverScreen(){
  background(0);
  fill(255, 255, 255);
  speedFactor = 0;
  stage = 1;
  bouncesCounter = 0;

  int scoreCounter = 0;

  if(topPlayer){
    showScore("Top", topScore, scoreCounter++, 15, 290);
  }

  if(rightPlayer){
    showScore("Right", rightScore, scoreCounter++, 15, 290);
  }

  if(bottomPlayer){
    showScore("Bottom", bottomScore, scoreCounter++, 15, 290);
  }

  if(leftPlayer){
    showScore("Left", leftScore, scoreCounter++, 15, 290);
  }

  textSize(36);
  text("Game Over!", 150, 200);
  textSize(20);
  text("Click to restart", 180, 230);

  if(mousePressed==true){
    pongBall.init();

    gameover=false;
  }
}

/**
 * Function that displays the game's splash screen
 */
public void showSplashScreen(){
  background(0);
  fill(255, 255, 255);

  textSize(100);
  text("Pong! 4X", 30, 200);
  textSize(20);
  text("Press Enter to start", 160, 350);

  textSize(15);
  text("by Jezz Lucena", 370, 490);
}

/**
 * Class that represents the player's paddle
 */
class Paddle{
  int x, y;

  Paddle(){
    x=215;
    y=215;
  }

  public void moveLeft(){
    if(x >= 0){
      x -= 5;
    }
  }

  public void moveRight(){
    if(x <= 440){
      x += 5;
    }
  }

  public void moveUp(){
    if(y >= 0){
      y -= 5;
    }
  }

  public void moveDown(){
    if(y <= 440){
      y += 5;
    }
  }
}

/**
 * Class that represents a player's horizontal paddle
 */
class hPaddle extends Paddle{
  /** Construtor n\u00e3o-parametrizado */
  hPaddle(){
  }

  public void show(){
    fill(255, 255, 255);
    rect(x, y, 70, 10);
  }
}

/**
 * Class that represents a player's vertical paddle
 */
class vPaddle extends Paddle {
  /** Construtor n\u00e3o-parametrizado */
  vPaddle(){
  }

  public void show(){
    fill(255, 255, 255);
    rect(x, y, 10, 70);
  }
}

/**
 * Class that represents the game's ball
 */
class Ball{
  int x, y;
  boolean up, right;

  Ball(){
    this.init();
  }

  public void init(){
    x = PApplet.parseInt(random(200, 301));
    y = PApplet.parseInt(random(200, 301));

    int xdirection = PApplet.parseInt(random(2));
    int ydirection = PApplet.parseInt(random(2));

    if(xdirection == 0){
      right = true;
    }else{ //xidrection==1
      right = false;
    }

    if(ydirection == 0){
      up = true;
    }else{ //ydirection==1
      up = false;
    }
  }

  public void move(){
    if(up == true){
      y = PApplet.parseInt(y - 1 - speedFactor);
    }else{
      y = PApplet.parseInt(y + 1 + speedFactor);
    }

    if(right == true){
      x = PApplet.parseInt(x + 1 + speedFactor);
    }else{
      x = PApplet.parseInt(x - 1 - speedFactor);
    }
  }

  public boolean isBounce(boolean isPlayer, int ballPoint, String playerName){
    int collisionPoint = isPlayer ? color(255, 255, 255) : color(0);
    boolean bounce = (!isPlayer && (ballPoint != collisionPoint)) || (isPlayer && (ballPoint == collisionPoint));

    if(isPlayer && bounce){
      bouncesCounter += 1;
      beep();
    }

    return bounce;
  }

  // Collision detection method
  public void bounce(){
    // Upper wall
    if(isBounce(topPlayer, get(PApplet.parseInt(x), PApplet.parseInt(y) - 5), "top")){
      up = false;
    }

    // Right wall
    if(isBounce(rightPlayer, get(PApplet.parseInt(x) + 15, PApplet.parseInt(y)), "right")){
      right = false;
    }

    // Lower wall
    if(isBounce(bottomPlayer, get(PApplet.parseInt(x), PApplet.parseInt(y) + 15), "bottom")){
      up = true;
    }

    // Left wall
    if(isBounce(leftPlayer, get(PApplet.parseInt(x) - 5, PApplet.parseInt(y)), "left")){
      right = true;
    }

    if(bouncesCounter >= 10){
      bouncesCounter = 0;
      stage += 1;
      speedFactor = (stage * 0.5f);
    }
  }

  public void show(){
    fill(255, 255, 255);
    rect(x, y, 16, 16);
  }
}
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Pong_4X" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
