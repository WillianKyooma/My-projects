package danki.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.danki.Game;
import com.entities.*;

import danki.com.graficos.Spritesheet;

public class World {
public static Tile[] tiles;
public static  int WIDTH,HEIGHT;
public static final int TILE_SIZE = 16;

public World( String patch) {
	/*Game.player.setX(0);
	Game.player.setY(0);
	WIDTH = 300;
	HEIGHT = 300;
	tiles = new Tile[WIDTH*HEIGHT];
	
	
for(int xx= 0; xx < WIDTH;xx++) {
		for(int yy = 0; yy < HEIGHT; yy++) {
		
		tiles[xx+yy*WIDTH] = new WallTile(xx*16,yy*16, Tile.TILE_WALL);	
		}
	}
	
	
	int dir = 0;
	int xx  =  0, yy = 0;
	
	for(int i= 0; i < 200; i++) {
		tiles[xx+yy*WIDTH] = new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);
	
	if (dir == 0) {
		//direita
	
		if(xx < WIDTH) {
			xx++;
		}
	}else if(dir == 1) {
	//esquerda
		
		if(xx > 0) {
			xx--;
		}
	} else if(dir == 2) {
	//baixo
		if(yy < HEIGHT) {
			yy++;
		}
	} else if (dir == 3) {
	//cima
		if(yy > 0) {
			yy--;
		}
	}
	
	if(Game.rand.nextInt(100) < 50) {
		dir = Game.rand.nextInt(4);
	}
	
	tiles[xx+yy*WIDTH] = new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);
	
	}*/
	
	
	try {
		BufferedImage map = ImageIO.read(getClass().getResource(patch));
		int[]pixels = new int[map.getWidth()* map.getHeight()];
		WIDTH = map.getWidth();
		HEIGHT = map.getHeight();
		tiles = new Tile[map.getWidth()* map.getHeight()];
		map.getRGB(0, 0,map.getWidth(),map.getHeight(),pixels,0,map.getWidth());
		for(int xx=0; xx < map.getWidth(); xx++) {
			
		 for(int yy=0; yy < map.getHeight(); yy++) {
			int pixelAtual = pixels[xx+(yy * map.getWidth())];
			tiles[xx + (yy* WIDTH)]= new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);
			if(pixelAtual== 0xFF000000) {
				//floor
			tiles[xx + (yy* WIDTH)]= new FloorTile(xx*16,yy*16, Tile.TILE_FLOOR);
			} else if(pixelAtual == 0xFFFFFFFF) {
			//Wall
			tiles[xx + (yy* WIDTH)]= new WallTile(xx*16,yy*16, Tile.TILE_WALL);
			}else if(pixelAtual == 0XFF0026FF) {
			//Player	
			Game.player.setX(xx*16);
			Game.player.setY(yy*16);
						}else if(pixelAtual == 0xFFFF0000) {
					//enemy
			Enemy en = new Enemy (xx*16,yy*16,16,16, Entity.ENEMY_EN);
	     Game.entities.add(en);
	     Game.enemies.add(en);
			
			}else if(pixelAtual == 0xFFFF6A00) {
			//sword//WEAPON
				Game.entities.add(new Sword(xx*16,yy*16,16,16, Entity.SWORD_EN));
				
			}else if(pixelAtual == 0xFFFF7F7F) {
				
			//life pack
				Game.entities.add(new Lifepack(xx*16,yy*16,16,16, Entity.LIFEPACK_EN));
				
			}else if(pixelAtual == 0xFFFFD800) {
			//Bullet
				Game.entities.add(new Bullet(xx*16,yy*16,16,16, Entity.BULLET_EN));
			}
		 }
		}
		
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static boolean isFree(int xnext, int ynext) {
 int x1 = xnext / TILE_SIZE;
 int  y1 = ynext / TILE_SIZE;
 
 int x2 = (xnext+TILE_SIZE-1)/ TILE_SIZE;
 int y2 = ynext / TILE_SIZE;
 
 int x3 = xnext / TILE_SIZE;
 int y3 = (ynext +TILE_SIZE-1)/ TILE_SIZE;
 
 int x4 = (xnext + TILE_SIZE-1) / TILE_SIZE;
 int y4 = (ynext +TILE_SIZE-1)/ TILE_SIZE;
 
 return !(tiles[x1 + (y1*World.WIDTH)] instanceof WallTile ||
		 tiles[x2 + (y2*World.WIDTH)] instanceof WallTile ||
		 tiles[x3 + (y3*World.WIDTH)] instanceof WallTile ||
		 tiles[x4 + (y4*World.WIDTH)] instanceof WallTile
		 );
}

public static void restartGame(String Level) {
	Game.entities.clear();
	Game.enemies.clear();
	Game.entities = new ArrayList<Entity>();
	Game.enemies = new ArrayList<Enemy>();
	Game.spritesheet = new Spritesheet("/spritesheet.png");
	Game.player = new Player(0, 0, 16, 16, Game.spritesheet.getSprite(0, 11, 16, 16));
	Game.entities.add(Game.player);
	Game.world = new World("/"+Level);
	return;
}





  public void Render(Graphics g) {
	int xstart = Camera.x >> 4 ; 
	int ystart = Camera.y >> 4 ;
	
	int xfinal = xstart + (Game.WIDTH >> 4); 
	int yfinal = ystart + (Game.HEIGHT >> 4); 
	
	
	for(int xx = xstart; xx <= xfinal; xx++) {
		for(int yy = ystart; yy <= yfinal ; yy++) {
			if (xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) {
				continue;
			}
			Tile tile = tiles[xx + (yy*WIDTH)]; 
			tile.Render(g);
		}
	}
}
  
  public static void RenderMiniMap() {
	  for(int i = 0; i < Game.miniMapPixels.length; i++) {
		Game.miniMapPixels[i] = 0;
	  }
	 for(int xx = 0; xx < WIDTH;xx++) {
		 
	for(int yy = 0; yy <HEIGHT; yy++) {
	if( tiles[xx + (yy*WIDTH)] instanceof WallTile) {
	Game.miniMapPixels[xx+(yy*WIDTH)]= 0Xff0000;
	}
	}
	int xPlayer = Game.player.getX()/16;
	int yPlayer = Game.player.getY()/16;
	
	Game.miniMapPixels[xPlayer +(yPlayer*WIDTH)]= 0x0000ff;
	 }
  }

}
