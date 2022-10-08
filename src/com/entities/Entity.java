package com.entities;


import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.List;

import com.danki.Game;

import danki.world.Camera;
import danki.world.Node;
import danki.world.Vector2i;

public class Entity {

	
public static BufferedImage LIFEPACK_EN= Game.spritesheet.getSprite(16*21, 185, 16, 16);	
public static BufferedImage SWORD_EN= Game.spritesheet.getSprite(0*10,154,10, 16);
public static BufferedImage ENEMY_EN= Game.spritesheet.getSprite(14*17,241,16,16);
public static BufferedImage BULLET_EN= Game.spritesheet.getSprite(14*0,185,10,16);

public static BufferedImage Enemy_FEEDBACK= Game.spritesheet.getSprite(306,210, 16, 16);
public static BufferedImage BULLET_SHOOT= Game.spritesheet.getSprite(53,188,9,10);

public static BufferedImage SWORD_RIGTH= Game.spritesheet.getSprite(9,157,18,10);
public static BufferedImage SWORD_LEFT= Game.spritesheet.getSprite(114,134,18,8);
public static BufferedImage SWORD_DOWN= Game.spritesheet.getSprite(145,123,11,16);
public static BufferedImage SWORD_UP= Game.spritesheet.getSprite(92,126,6, 12);
public double x;
public double y;
protected double width;
protected double height;

public int depth;
protected List<Node>patch;
private BufferedImage sprite;
private int maskx,masky,mwidth,mheight;



public Entity(int x, int y, int width,int height,BufferedImage sprite) {

this.x = x;
this.y = y;
this.width = width;       
this.height= height;
this.sprite= sprite;


this.maskx = 0;
this.masky = 0;
this.mwidth= width;
this.mheight =height;


}

public void setMask(int maskx, int masky, int mwidth, int mheight) {
	this.maskx = maskx;
	this.masky = masky;
	this.mwidth= mwidth;
	this.mheight =mheight;
}

public void setX(int newX) {
 this.x =newX;
}

public void setY(int newY) {
 this.y = newY;
}

public int getX() {
return (int) this.x;
}

public int getY() {
return(int) this.y;
}

public double getWidth() {
return this.width;
}

public double getHeight() {
return this.height;
}


public void tick() {}


public double calculateDistance(int x1, int y1,int x2,int y2) {
return Math.sqrt((x1 - x2)* (x1- x2) + (y1-y2)* (y1- y2));
}

public void followPatch(List<Node>patch) {
	if(patch != null) {
	  if(patch.size()> 0) {
		 Vector2i target = patch.get(patch.size()-1).tile;
		 //xprev =x;
		 //yprev=y;
		 if(x < target.x*16) {
			 x++;
			
		 }else if(x > target.x * 16) {
			 x--;
		 }
		 if(y < target.y * 16) {
			  y++;
		  }
		 else if(y> target.y * 16) {
			 y--;
		 }
		 
		 if(x == target.x * 16 && y == target.y * 16) {
			 patch.remove(patch.size() - 1);
		 }
	  }
	  
	}
}

public static Comparator<Entity> nodeSorter = new Comparator<Entity>() {
	@Override
	// Dois objetos que serão comparados quando rodar o algoritmo
	public int compare(Entity n0, Entity n1) {
		if (n1.depth < n0.depth) {
			return +1;
		}
		if (n1.depth > n0.depth) {
			return -1;
		}

		return 0;
	}
};	















public static  boolean isColliding(Entity e1, Entity e2) {
	
 Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY()+ e1.masky, e1.mwidth,e1.mheight);  
 Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY()+ e2.masky, e2.mwidth,e2.mheight);
 
 return e1Mask.intersects(e2Mask);
}


public  void render(Graphics g) {
 g.drawImage(sprite,this.getX()- Camera.x,this.getY()- Camera.y,null);
// g.setColor(Color.red);
// g.fillRect(this.getX()+ maskx- Camera.x,this.getY()+ masky- Camera.y, mwidth, mheight);
}




}
