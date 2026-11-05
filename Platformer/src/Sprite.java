import java.awt.Graphics;
import java.awt.Rectangle;
public interface Sprite {
    void update();
    void draw(Graphics g);
    Rectangle getBounds();
}