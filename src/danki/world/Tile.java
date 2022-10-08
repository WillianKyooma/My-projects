package danki.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.danki.Game;

public class Tile {
public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(284,244, 16,16);
public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(262,244, 16,16);

private BufferedImage Sprite;
private int x,y;

public Tile(int x, int y,BufferedImage sprite) {
 this.x = x;
 this.y= y;
 this.Sprite = sprite;
}

public void Render(Graphics g) {
 g.drawImage(Sprite, x - Camera.x, y - Camera.y, null);
}
}
