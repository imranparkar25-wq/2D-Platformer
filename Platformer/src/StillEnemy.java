import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class StillEnemy implements Sprite {

    int x, y;
    BufferedImage sprite;
    final int HITBOX_OFFSET_X = 50;
    final int HITBOX_OFFSET_Y = 50;
    final int HITBOX_WIDTH = 46;  // 96 - 4 total horizontal padding (approx)
    final int HITBOX_HEIGHT = 46; // adjust if needed visually
    int animationTick = 0;
    boolean shouldAnimate = false;

    public StillEnemy(int x, int y) {
        this.x = x;
        this.y = y;

        try {
            sprite = ImageIO.read(
                    getClass().getResourceAsStream("/resources/Dragon.png")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() {
        if (shouldAnimate) {
            animationTick++;
            shouldAnimate = false;
        }
    }

    @Override
    public void draw(Graphics g) {
        int offset = (int)(Math.sin(animationTick * 0.1) * 5);
        g.drawImage(sprite, x, y + offset, 96, 96, null);
    }

    @Override
    public Rectangle getBounds() {
        int offset = getOffset(); // your animation offset
        return new Rectangle(
                x + HITBOX_OFFSET_X,
                y + HITBOX_OFFSET_Y + offset,
                HITBOX_WIDTH,
                HITBOX_HEIGHT
        );
    }

    public int getOffset() {
        return (int)(Math.sin(animationTick * 0.1) * 5);
    }
}