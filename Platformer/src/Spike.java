import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Spike implements Sprite{

    int x, y;
    int width = 32;
    int height = 32;
    int tick = 0;
    int baseWidth = 32;
    int baseHeight = 32;
    BufferedImage sprite;

    public Spike(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            sprite = ImageIO.read(
                    getClass().getResourceAsStream("/resources/Spike.png")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        tick++;
    }

    @Override
    public void draw(Graphics g) {
        int pulse = (int)(Math.sin(tick * 0.1) * 2);
        int drawWidth = baseWidth + pulse;
        int drawHeight = baseHeight + pulse;
        int drawX = x - pulse / 2;
        int drawY = y - pulse / 2;
        if (sprite != null) {
            g.drawImage(sprite, drawX, drawY, drawWidth, drawHeight, null);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x + 30, y + 9, width-25, height - 15);
    }
}