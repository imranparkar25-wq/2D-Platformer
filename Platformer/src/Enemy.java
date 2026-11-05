import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Enemy implements Sprite{

    int x;
    int y;
    int width = 48;
    int height = 48;
    int speed = 2;
    int direction = 1; // 1 = right, -1 = left
    int leftBound;
    int rightBound;

    BufferedImage sprite;

    public Enemy(int x, int y, int leftBound, int rightBound) {
        this.x = x;
        this.y = y;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
        try {
            sprite = ImageIO.read(
                    getClass().getResourceAsStream("/resources/Enemy.png")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        x += speed * direction;
        if (x <= leftBound) {
            x = leftBound;
            direction = 1;
        }
        if (x + width >= rightBound) {
            x = rightBound - width;
            direction = -1;
        }
    }

    @Override
    public void draw(Graphics g) {
        if (direction == 1) {
            g.drawImage(sprite, x + width, y, -width, height, null);
        } else {
            g.drawImage(sprite, x, y, width, height, null);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 6, y + 6, 16, 16);
    }
}