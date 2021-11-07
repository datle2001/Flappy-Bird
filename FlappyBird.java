package flappyBird;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.Timer;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import javax.swing.JFrame;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {
  
  public static FlappyBird flappyBird;
  public static final int WIDTH = 750, HEIGHT = 750;
  public Render renderer;
  public Rectangle bird;
  public ArrayList<Rectangle> columns;
  public Random rand;
  public int ticks, yMotion, score;
  private final int SPEED = 8;
  public boolean gameOver = false;
  public boolean started = false;

  public FlappyBird() {
    JFrame jframe = new JFrame();
    Timer timer = new Timer(20, this);
    rand = new Random();

    renderer = new Render();
    bird = new Rectangle(WIDTH/2 -10, HEIGHT/2-10, 20, 20);
    columns = new ArrayList();
    jframe.add(renderer);

    jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    jframe.setSize(WIDTH, HEIGHT);
    jframe.setVisible(true);
    jframe.addMouseListener(this);
    jframe.addKeyListener(this);
    jframe.setResizable(false);
    jframe.setTitle("Flappy Bird");

    addColumn(true);
    addColumn(false);
    timer.start();
  }
  @Override
  public void mouseClicked(MouseEvent e) {
    jump();
  }
  @Override
  public void mouseEntered(MouseEvent e) {
  }
  @Override
  public void mouseExited(MouseEvent e) {
  }
  @Override
  public void mousePressed(MouseEvent e) {
  }
  @Override
  public void mouseReleased(MouseEvent e) {
  }
  public void addColumn(boolean start) {
    int spaceY = 90 + rand.nextInt(100);
    int spaceX = 500 + rand.nextInt(150);
    int width = 100;
    int height = 50 + rand.nextInt(300);
    if(start) {
      columns.add(new Rectangle(WIDTH + width + columns.size()*300, HEIGHT - 120 - height, width, height));
      columns.add(new Rectangle(WIDTH + width + (columns.size()-1)*300, 0, width, HEIGHT - height - spaceY - 120));
    } 
    else {
      columns.add(new Rectangle(columns.get(columns.size()-1).x + spaceX, HEIGHT - 120 - height, width, height));
      columns.add(new Rectangle(columns.get(columns.size()-1).x, 0, width, HEIGHT - height - spaceY - 120));
    }
  }
  public void paintColumn(Graphics g, Rectangle column) {
    g.setColor(Color.GREEN.darker());
    g.fillRect(column.x, column.y, column.width, column.height);
  }
  public void jump() {
    if(gameOver) {
      bird = new Rectangle(WIDTH/2 -10, HEIGHT/2-10, 20, 20);
      columns.clear();
      addColumn(true);
      addColumn(false);
      gameOver = false;
      score = 0;
    }
    if(!started) {
      started = true;
    }
    else if(!gameOver) {
      if(yMotion > 0) yMotion = 0;
      yMotion -= 10;
      
    }
  }
  @Override
  public void actionPerformed(ActionEvent e) {
    ticks++;
    if(started) {
      for(int i = 0; i<columns.size(); i++) {
        Rectangle rect = columns.get(i);
        rect.x -= SPEED;
      }
      if(ticks % 2 == 0 && yMotion < 15) {
        yMotion += 2;
      }
      //add columns when 2 columns pass the left side
      for(int i = 0; i<columns.size(); i++) {
        Rectangle column = columns.get(i);
        if(column.x + column.width < 0) {
          columns.remove(i);
          if(column.y == 0) {
            addColumn(false);
          }
        }
      }
      bird.y += yMotion;
      for(Rectangle column : columns) {
        if(column.y == 0 && bird.x + bird.width/2 >= column.x + column.width/2-4
        && bird.x + bird.width/2 <= column.x + column.width/2+3) {
          score++;
        }
        if(column.intersects(bird)) {
          gameOver = true;
          bird.x = column.x-bird.width+1;
        }
      }
      if(bird.y + bird.height > HEIGHT-120|| bird.y < 0) {
        bird.y = HEIGHT-120 - bird.height;
        gameOver = true;
      }
      if(bird.y + yMotion >= HEIGHT) {
        bird.y = HEIGHT-120 - bird.height;
      }
    }
    renderer.repaint();
  }
  public void repaint(Graphics g) {
    g.setColor(Color.CYAN);
    g.fillRect(0, 0, WIDTH, HEIGHT);

    g.setColor(Color.ORANGE);
    g.fillRect(0, HEIGHT-120, WIDTH, 120);

    g.setColor(Color.GREEN);
    g.fillRect(0, HEIGHT-120, WIDTH, 20);

    g.setColor(Color.RED);
    g.fillRect(bird.x, bird.y, bird.width, bird.height);

    for(Rectangle column : columns) {
      paintColumn(g, column);
    }
    g.setColor(Color.white);
    g.setFont(new Font("Arial", 1, 100));

    if(!started) {
      g.drawString("Click to start!", 75, HEIGHT/2-50);
    }

    if(gameOver) {
      g.drawString("Game Over!", 75, HEIGHT/2-50);
    }
    if(!gameOver && started) {
      g.drawString("Score: " + String.valueOf(score), 0, 75);
    }
  }
  public static void main(String[] args) {
    flappyBird = new FlappyBird();
  }
  @Override
  public void keyTyped(KeyEvent e) {
    if(e.getKeyChar() == KeyEvent.VK_SPACE){
      jump();
    }
  }
  @Override
  public void keyPressed(KeyEvent e) {
    // TODO Auto-generated method stub
    
  }
  @Override
  public void keyReleased(KeyEvent e) {
    // TODO Auto-generated method stub
    
  }  
}


