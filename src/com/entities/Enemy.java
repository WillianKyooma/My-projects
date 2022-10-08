package com.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.danki.Game;

import danki.world.AStar;
import danki.world.Camera;
import danki.world.Vector2i;
import danki.world.World;

public class Enemy extends Entity {
public String[] frases = new String[5];
private double speed = 0.1;
private int masky = 8, maskx= 8, maskw = 10, maskh = 10;
private int frames= 0, MaxFrames = 10,index = 0,maxIndex = 1;
//private boolean moved = false;
private int life=3;
private BufferedImage[] sprites;
private boolean isDamaged = false;
private  int damageFrames = 10, damageCurrent=0;

	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite(256,211,16,16);
		sprites[1] = Game.spritesheet.getSprite(256+16,211,16,16);
	
		frases[0] = "Em nome do rei:)";
		
		}
	
	
	public void tick() {
	
	
	//if (Game.rand.nextInt(100)< 30) {	
		//masky = 8;
		//maskx= 8;
		//maskw = 5;
	//maskh = 5;
	
		
		
	/*if(this.calculateDistance(this.getX(), this.getY(), Game.player.getX(), Game.player.getY() )< 80) {	
	if(this.isCollidingWithPlayer()== false) {
	if( x < Game.player.getX() && World.isFree((int) (x+speed),getY() )
			&& !isColidding ((int) (x+speed),getY() )) {
	x+=speed;
	}else if(x> Game.player.getX() && World.isFree((int) (x-speed),getY() )
		&&	!isColidding((int)(x-speed) , getY())) {
		x-=speed;
	}
	
	if( y < Game.player.getY() && World.isFree(this.getX() ,(int)(y+speed) )
			&&  !isColidding(this.getX(),(int) (y+speed))) {
		y+=speed;
		
		}else if(y> Game.player.getY() && World.isFree( getX(), (int)(y-speed))
			&&  !isColidding(this.getX(),(int) (y-speed))) {
			y-=speed;
			
			
		}
	}else {
		//estamos colidindo
		if(Game.rand.nextInt(100) < 10) {
		Game.player.life-= Game.rand.nextInt(2);
		Game.player.isDamaged = true;
		//System.out.println("Life"+ Game.player.life);
		}
		if(Game.player.life <= 0) {
			//game over!
			
		}else{
		
	}
		*/
		depth= 0;
		if(!isCollidingWithPlayer()) {
		if(patch == null || patch.size() == 0) {
			Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
			Vector2i end = new Vector2i((int) (Game.player.x / 16), (int) (Game.player.y / 16));
			patch = AStar.findPatch(Game.world,start, end);
		}
		}else {
		if(new Random().nextInt(100)> 5) {
		Game.player.life-=Game.rand.nextInt(3);
		Game.player.isDamaged = true;
		}
			
			
		}
		
		if(new Random().nextInt(100)< 30)
		followPatch(patch);
		frames++;
		if(new Random().nextInt(100)< 5) {
			Vector2i start = new Vector2i((int)(x/16), (int)(y/16));
			Vector2i end = new Vector2i((int) (Game.player.x / 16), (int) (Game.player.y / 16));
			patch = AStar.findPatch(Game.world,start, end);
		}
		if(frames == MaxFrames) {
			frames= 0;
			index++;
			if (index > maxIndex)
				index= 0;
		}
		
		collidingBullet();
		if(life <=0) {
			destroySelf();
			return;
		}
		
		
		if(isDamaged) {
		this.damageCurrent++;
		if(this.damageCurrent == this.damageFrames) {
			this.damageCurrent = 0;
			this.isDamaged = false;
		}
		}
	}
	
	public void destroySelf() {
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}
	
	
	
	public void collidingBullet() {
	for(int i=0; i < Game.bullets.size(); i++) {
	Entity e =  Game.bullets.get(i);
	if( e instanceof BulletShoot) {
		
		
	if(Entity.isColliding(this, e)) {
		isDamaged= true;
		life--;
		Game.bullets.remove(i);
		System.out.println("colisão");
		return;
	}
	}
	}
	
	}
	
	
	
public boolean isCollidingWithPlayer() {
	Rectangle EnemyCurrent = new Rectangle(this.getX()+maskx,this.getY() +  masky, maskw,maskh);
	Rectangle Player = new Rectangle(Game.player.getX(), Game.player.getY(),16,16);
	
	return EnemyCurrent.intersects(Player);
}
	

public boolean isColidding(int xnext, int ynext) {
	Rectangle enemyCurrent = new Rectangle(xnext+ maskx, ynext+masky, maskw,maskh);
	
	
	for(int i=0; i < Game.enemies.size(); i++) {
	Enemy e = Game.enemies.get(i);
	if(e == this)
	continue;
	
	Rectangle targetEnemy = new Rectangle(e.getX()+ maskx,e.getY()+ masky, maskw,maskh);
	if(enemyCurrent.intersects(targetEnemy)) {
	return true;
	}
	}
	
	
	return false;
}

public void render(Graphics g) {
	if(!isDamaged)
	g.drawImage(sprites[index], this.getX() - Camera.x,this.getY()- Camera.y,null);
	
	
	
	else 
		g.drawImage(Entity.Enemy_FEEDBACK, this.getX() - Camera.x,this.getY()- Camera.y,null);
	 } 
		 
	 
 
 //g.setColor(Color.blue);
 //g.fillRect(getX()+ maskx - Camera.x, getY()+ masky- Camera.y,maskw,maskh);
}



