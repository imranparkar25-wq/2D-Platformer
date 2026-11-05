import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class GamePanel extends JPanel implements Runnable, KeyListener {

    final int TILE_SIZE = 32;
    final int COLS = 40;
    final int ROWS = 30;
    final int WIDTH = TILE_SIZE * COLS;
    final int HEIGHT = TILE_SIZE * ROWS;
    public static final int STATE_START = 0;
    public static final int STATE_PLAY = 1;
    public static final int STATE_GAMEOVER = 2;
    public static final int STATE_WIN = 3;
    int gameState = STATE_START;

    Rectangle startButton = new Rectangle(220, 200, 200, 60);
    Rectangle quitButton = new Rectangle(220, 260, 200, 60);

    boolean leftPressed = false;
    boolean rightPressed = false;
    boolean jumpPressed = false;

    Player player;
    Enemy enemy;
    Goal goal;

    Sprite[] sprites;

    Platform[] platforms = new Platform[] {
            new Platform(0, 200, 200, 20),
            new Platform(250, 150, 400, 20),
            new Platform(700, 400, 100, 20),
            new Platform(100, 440, 450, 20),
            new Platform(0, 800, 300, 20),
            new Platform(300, 700, 450, 20),
            new Platform(750, 750, 200, 20),
            new Platform(1000, 700, 200, 20)
    };

    Spike[] spikes = new Spike[] {
            new Spike(600, 130),
            new Spike(400, 680),
            new Spike(775, 730)
    };

    Torch[] torches;

    StillEnemy stillEnemy = new StillEnemy(575, 425);

    public void resetGame() {

        player = new Player(0, 0);
        enemy = new Enemy(300, 400, 200, 500);
        goal = new Goal(1100, 600);
        sprites = new Sprite[] {
                enemy,
                goal
        };
        torches = new Torch[] {
                new Torch(50, 225),
                new Torch(400, 175),
                new Torch(740, 425),
                new Torch(275, 465),
                new Torch(150, 825),
                new Torch(500, 725)
        };
    }

    public GamePanel() {
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered(true);
        resetGame();
        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int mouseX = e.getX();
                int mouseY = e.getY();
                if (gameState == STATE_START) {
                    if (startButton.contains(mouseX, mouseY)) {
                        gameState = STATE_PLAY;
                        resetGame();
                    }
                }
                if (gameState == STATE_GAMEOVER) {
                    if (startButton.contains(mouseX, mouseY)) {
                        gameState = STATE_PLAY;
                        resetGame();
                    }
                }
                if (gameState == STATE_WIN) {
                    if (startButton.contains(mouseX, mouseY)) {
                        gameState = STATE_PLAY;
                        resetGame();
                    }
                    if (quitButton.contains(mouseX, mouseY)) {
                        System.exit(0);
                    }
                }
            }
        });
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    Thread gameThread;
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double FPS = 60.0;
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            leftPressed = true;
        }
        if (key == KeyEvent.VK_D) {
            rightPressed = true;
        }
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_SPACE) {
            jumpPressed = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_A) {
            leftPressed = false;
        }
        if (key == KeyEvent.VK_D) {
            rightPressed = false;
        }
        if (key == KeyEvent.VK_W || key == KeyEvent.VK_SPACE) {
            jumpPressed = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // not used
    }

    public void update() {
        boolean playerMoved = player.moving || jumpPressed;
        if (gameState == STATE_PLAY) {
            player.moving = false;
            if (leftPressed) {
                player.moveLeft();
                player.moving = true;
            }
            if (rightPressed) {
                player.moveRight();
                player.moving = true;
            }
            if (jumpPressed) {
                player.jump();
            }
            player.update();
            if (player.y > HEIGHT) {
                gameState = STATE_GAMEOVER;
            }
            Rectangle playerBox = player.getBounds();
            for (Platform p : platforms) {
                Rectangle platformBox = p.getBounds();
                if (playerBox.intersects(platformBox)) {
                    // ONLY resolve downward collision
                    if (player.yVelocity >= 0) {
                        // snap ON TOP of platform
                        player.y = p.y - 64;
                        player.yVelocity = 0;
                        player.isJumping = false;
                    }
                }
            }
            for (Spike s : spikes) {
                s.update();
            }
            stillEnemy.shouldAnimate = playerMoved;
            stillEnemy.update();
            enemy.update();
            goal.update();
            if (player.getBounds().intersects(enemy.getBounds())) {
                gameState = STATE_GAMEOVER;
            }
            for (Spike s : spikes) {
                if (player.getBounds().intersects(s.getBounds())) {
                    gameState = STATE_GAMEOVER;
                }
            }
            if(player.getBounds().intersects(stillEnemy.getBounds())) {
                gameState = STATE_GAMEOVER;
            }
            if (player.getBounds().intersects(goal.getBounds())) {
                gameState = STATE_WIN;
            }
        }
    }

    public void drawStartScreen(Graphics g) {
        // Title
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(30f));
        g.drawString("My Platformer Game", 180, 120);
        // Button
        g.setColor(Color.GRAY);
        g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
        // Button text
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(20f));
        g.drawString("START", startButton.x + 65, startButton.y + 35);
    }

    public void drawGameOver(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(30f));
        g.drawString("GAME OVER", 200, 120);
        g.setFont(g.getFont().deriveFont(20f));
        g.drawString("Click to Restart", 200, 180);
        g.setColor(Color.GRAY);
        g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
        g.setColor(Color.BLACK);
        g.drawString("RESTART", startButton.x + 50, startButton.y + 35);
    }

    public void drawWinScreen(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(30f));
        g.drawString("YOU WIN!", 220, 120);
        g.setFont(g.getFont().deriveFont(20f));
        g.drawString("Click to Restart", 200, 180);
        g.setColor(Color.GRAY);
        g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
        g.setColor(Color.BLACK);
        g.drawString("RESTART", startButton.x + 50, startButton.y + 35);
        g.setColor(Color.BLACK);
        g.drawString("QUIT", quitButton.x + 75, quitButton.y + 35);
    }

    public void drawGame(Graphics g) {
        for (Platform p : platforms) {
            p.draw(g);
        }
        for (Spike s : spikes) {
            s.draw(g);
        }
        player.draw(g);
        enemy.draw(g);
        stillEnemy.draw(g);
        goal.draw(g);
        for (Torch t : torches) {
            t.draw(g);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (gameState == STATE_START) {
            drawStartScreen(g);
        }
        else if (gameState == STATE_PLAY) {
            drawGame(g);
        }
        else if (gameState == STATE_GAMEOVER) {
            drawGameOver(g);
        }
        else if (gameState == STATE_WIN) {
            drawWinScreen(g);
        }
    }
}