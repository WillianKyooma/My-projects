package com.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.danki.Game;

import danki.com.graficos.Spritesheet;
import danki.world.Camera;
import danki.world.World;

public class Player extends Entity {
	public boolean right,left,down,up;
	public int right_dir = 0, left_dir = 1;
	public int up_dir = 2, down_dir = 3;
	public int dir= right_dir;
	public double speed = 1.6;
	
	
	private int frames= 0, MaxFrames = 5,index = 0,maxIndex = 3;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] UpPlayer;
	private BufferedImage[] DownPlayer;
	
	
	private BufferedImage playerDamage;
	
	private boolean  arma = false;
	
	public int ammo=0;
	
	public boolean shoot = false, mouseShoot = false;
	
	
	public boolean isDamaged = false;
    private int damageFrames = 0;
	
	
	
	public  double life = 100, maxlife = 100;
	public int mx,my;
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
	super(x, y, width, height, sprite);
		
	rightPlayer = new BufferedImage[4];	
	leftPlayer  = new BufferedImage[4];	
	UpPlayer    = new BufferedImage[4];
	DownPlayer  = new BufferedImage[4];
	
	playerDamage = Game.spritesheet.getSprite(135, 205, 16, 16);
   
	
	for(int i = 0;i < 4; i++) {
		rightPlayer[i]= Game.spritesheet.getSprite(162+(i*17),11,16,16);

		if(arma) {
		
		}
		
	}
	
	for(int i = 0;i < 4; i++) {
	
       leftPlayer[i]= Game.spritesheet.getSprite(241+(i*17),11,16,16);

       
       if(arma) {
    	
       }
		}
	
	for(int i = 0;i < 4; i++) {
		UpPlayer[i]= Game.spritesheet.getSprite(70+(i*14),11,14,16);

		
	}
	
	for(int i = 0;i < 4; i++) {
	
       DownPlayer[i]= Game.spritesheet.getSprite(0+(i*16),11,16,15);


       
		}
	
	
	}
	
	public void tick() {
		depth = 1;
		moved = false;
		
		if (right && World.isFree((int)(x+speed), this.getY())){
			moved= true;
			dir = right_dir;
			 x+=speed;}
		
	 else if (left &&World.isFree((int)(x-speed), this.getY())) {
		 moved= true;
		   dir = left_dir;
			 x-=speed;}
		
		 if(up && World.isFree(this.getX(), (int) (y- speed))) {
			 moved= true;
			 dir = up_dir;
		     y-=speed;}
		 
		 else if(down && World.isFree(this.getX(), (int) (y+speed))) {
			 moved= true;
			dir= down_dir;
		     y+=speed;}
		 
		if (moved) {
			moved= true;
			frames++;
			if(frames == MaxFrames) {
				frames= 0;
				index++;
				if (index > maxIndex)
					index= 0;
			}
		}
		
		this.checkColisioLifePack();
		this.checkColisionAmmo();
		this.checkColisionGun();
		
		
	if(isDamaged) {
	this.damageFrames++;
	if(this.damageFrames == 10) {
	this.damageFrames = 0;
	isDamaged = false;
	}
	}
	
	if(shoot ) {
		shoot= false;
		if(arma && ammo > 0) {
		ammo--;
		//criar bala e atirar
		shoot = false;
		int dx=0;
		int px=0;
		int py=6;
		if(dir == right_dir) {
		px=9;
		dx=1;		
		}else {
		px = 9;
		dx = 1;
		}
		
		System.out.println("Bang");
if(dir == right_dir) {
	 dx = 1;
}else {
 dx = -1;
}
		
BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,0);
Game.bullets.add(bullet);
	}
	
	}
	
	if(mouseShoot) {
		System.out.println("Atirou");
		mouseShoot= false;
		
		//System.out.println(angle);
		if(arma && ammo > 0) {
		ammo--;
		//criar bala e atirar
		
		shoot = false;
		
		int py=0, px=0;
		double angle = 0;
		if(dir == right_dir) {

	 px=18;
	 angle =  (Math.atan2(my- ( this.getY()+8 - Camera.y), mx-(this.getX()+px- Camera.x)));	
			}else {
	 angle =  (Math.atan2(my- ( this.getY()+8 - Camera.y), mx-(this.getX()+px- Camera.x)));
			px = 9;
			
			}
		double dx=  Math.cos(angle);
		double dy=Math.sin(angle);
		
BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,dy);
Game.bullets.add(bullet);
	}
		
	}
	
	if(life <=0) {
	Game.gameState = "GAME_OVER";
	}
		
	updateCamera();	
		
	}
	
	public void updateCamera() {
	Camera.x = Camera.Clamp(this.getX()-(Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
	Camera.y = Camera.Clamp(this.getY()-(Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
	}
	
	
	
	
	public void checkColisionGun() {for(int i = 0; i <  Game.entities.size(); i++) {
		Entity atual  = Game.entities.get(i);
		if (atual instanceof Sword) {
		if( Entity.isColliding(this, atual)) {
			arma = true;
			//System.out.println("Pegou a espada");
			//System.out.println("Munição atual"+ ammo);
			Game.entities.remove(i);
			return;
			
		}
		}
	}
		
	}
	
	
	public void checkColisionAmmo() {for(int i = 0; i <  Game.entities.size(); i++) {
		Entity atual  = Game.entities.get(i);
		if (atual instanceof Bullet) {
		if( Entity.isColliding(this, atual)) {
			ammo += 20;
			//System.out.println("Munição atual"+ ammo);
			Game.entities.remove(i);
			return;
			
		}
		}
	}
		
	}
	
	
	
	 //contiunue aqui
	public void  checkColisioLifePack() {
	for(int i = 0; i <  Game.entities.size(); i++) {
		Entity atual  = Game.entities.get(i);
		if (atual instanceof Lifepack) {
		if( Entity.isColliding(this, atual)) {
			life +=20;
			if(life > 100)
			life = 100;
			Game.entities.remove(i);
			return;
			
		}
		}
	}
	
		}
	
		 
	 
		
	
	
	public void render(Graphics g) {
	if(!isDamaged) {	
	if(dir == right_dir) {
	g.drawImage(rightPlayer[index],this.getX()- Camera.x,this.getY()- Camera.y,null);
	
	if(arma){
		g.drawImage(Entity.SWORD_RIGTH,this.getX()+4-Camera.x,this.getY()+4  -Camera.y, null);
			
		}
	}else if (dir ==left_dir) {
	g.drawImage(leftPlayer[index],this.getX()- Camera.x,this.getY()- Camera.y,null);
	
if(arma){
	g.drawImage(Entity.SWORD_LEFT,this.getX()-1 -Camera.x,this.getY()+6- Camera.y, null);
		
	}
	}
	//repetir a mesma coisa para UP e Down^
	if(dir == up_dir) {
		g.drawImage(UpPlayer[index],this.getX()- Camera.x,this.getY()- Camera.y,null);
		
		if(arma){
			g.drawImage(Entity.SWORD_UP,this.getX()+2-Camera.x,this.getY()-10-Camera.y, null);
				
			}
		}else if ( dir == down_dir) {
		g.drawImage(DownPlayer[index],this.getX()- Camera.x,this.getY()- Camera.y,null);
		
		if(arma){
			g.drawImage(Entity.SWORD_DOWN,this.getX()+8-Camera.x,this.getY()+8-Camera.y, null);
				
			}
		}
	 }else {
			g.drawImage(playerDamage,this.getX()-Camera.x,this.getY()- Camera.y, null );
		}
	
}
}
