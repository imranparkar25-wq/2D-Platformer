import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Player implements Sprite{

    int x;
    int y;
    int walkTick = 0;
    boolean moving = false;
    int direction = 1;
    int speed = 4;
    int yVelocity = 0;
    int gravity = 1;
    boolean isJumping = false;

    BufferedImage sprite;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            sprite = ImageIO.read(
                    getClass().getResourceAsStream("/resources/PlayerSprite.png")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        yVelocity += gravity;
        y += yVelocity;
        if (moving && !isJumping) {
            walkTick++;
        } else {
            walkTick = 0;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (sprite != null) {
            int bounce = 0;
            if (moving && !isJumping) {
                bounce = (int)(Math.sin(walkTick * 0.3) * 3);
            }
            Graphics2D g2d = (Graphics2D) g;
            if (direction == -1) {
                g2d.drawImage(sprite, x + 64, y + bounce, -64, 64, null);
            } else {
                g2d.drawImage(sprite, x, y + bounce, 64, 64, null);
            }
        }
    }

    public void moveLeft() {
        x -= speed;
        direction = -1;
    }

    public void moveRight() {
        x += speed;
        direction = 1;
    }

    public void jump() {
        if (!isJumping) {
            yVelocity = -15;
            isJumping = true;
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x+8, y+8, 64, 64);
    }
}