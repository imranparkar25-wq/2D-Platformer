import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Torch implements Sprite {

    int x, y;
    BufferedImage sprite;

    public Torch(int x, int y) {
        this.x = x;
        this.y = y;
        try {
            sprite = ImageIO.read(
                    getClass().getResourceAsStream("/resources/Torch.png")
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update() { }

    @Override
    public void draw(Graphics g) {
        g.drawImage(sprite, x, y, 32, 32, null);
    }

    @Override
    public Rectangle getBounds() {
        return new Rectangle(x, y, 32, 32);
    }
}