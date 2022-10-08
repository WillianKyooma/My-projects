package com.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.danki.Game;

import danki.world.Camera;

public class BulletShoot extends Entity{
private double dx;
private  double dy;
private double spd = 4;
private int life = 30,curLife = 0;
private BufferedImage[] sprites;	

	
public BulletShoot(int x, int y, int width,int height,BufferedImage sprite, double dx, double dy) {
super(x,y,width,height,sprite);	

this.dx= dx;
this.dy = dy;





sprites = new BufferedImage[1];
sprites[0] = Game.spritesheet.getSprite(53,188,9,10);


}
	
	
public void Tick() {
	x+=dx*spd;
	y+=dy*spd;
	curLife++;
	if(curLife== life) {
	Game.bullets.remove(this);
	return;
	}
}
	
public void render(Graphics g) {
	
	
g.drawImage(BULLET_SHOOT,this.getX()-Camera.x ,this.getY() - Camera.y,4,4, null);
//g.setColor(Color.YELLOW);
//g.fillOval(this.getX()- Camera.x,this.getY() - Camera.y, 3, 3);
}

}
