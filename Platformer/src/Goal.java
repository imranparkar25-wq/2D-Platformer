import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Goal implements Sprite{

    int x, y;
    int width = 48;
    int height = 48;
    int baseY;
    double time = 0;

    BufferedImage sprite;

    public Goal(int x, int y) {
        this.x = x;
        this.y = y;
        this.baseY = y;
        try {
            sprite = ImageIO.read(
                    getClass().getResourceAsStream("/resources/Goal.png")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

   @Override
    public void draw(Graphics g) {
        if (sprite != null) {
            g.drawImage(sprite, x, y, width, height, null);
        }
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void update() {
        time += 0.05; // speed of animation
        y = baseY + (int)(Math.sin(time) * 5);
    }
}