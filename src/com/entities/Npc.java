package com.entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.danki.Game;

public class Npc extends Entity{
	public String[] frases = new String[2];
	public static  boolean showMessage = false;
	public static boolean show = false;
	public int curIndex = 0;
	public int curIndexMsg = 0;
	public int  fraseIndex = 0;
	public int Time = 0;
	public int maxTime = 0;
	
	
	public Npc(int x, int y, int width, int height, BufferedImage sprite) 
	{
	 super(x, y, width, height, sprite);
  
    frases[0] = "Pegue a espada";
    frases[1] = "destrua eles ";
	
	   }

	
public void tick() {
	
int xPlayer = Game.player.getX();
int yplayer = Game.player.getY();


int xNpc = (int)x;
int yNpc = (int)y;

if(Math.abs(xPlayer - xNpc)<  30 && 
	Math.abs(yplayer - yNpc)< 30) {
 if( show == false)
	showMessage = true;
    show = true;
 }else 
 {
	//showMessage = false;
 }


	
if(showMessage) {
	
this.Time++;
if(this.Time >= this.maxTime) {
this.Time = 0;
if(curIndexMsg < frases[fraseIndex].length()-1)
{
 curIndexMsg++;
 }else {
	   
 if(fraseIndex < frases.length -1)
 fraseIndex++; 
 curIndexMsg = 0;
	       }
	 
	  }


  }


}









///NPC CONTINUAR AQUI
public void render(Graphics g) {
super.render(g);

if(showMessage) {
	g.setColor(Color.red);
	g.fillRect(9,9,Game.WIDTH-18,Game.HEIGHT-18);
	
	g.setColor(Color.white);
	g.fillRect(10,10,Game.WIDTH-20,Game.HEIGHT-20);
	
	
	
	g.setFont(new Font("Arial",Font.BOLD,9));
	g.setColor(Color.black);
	
	g.drawString(frases[fraseIndex].substring(0,curIndexMsg),(int) x,(int)y);
	g.drawString(">Pressione enter pra fechar<",(int) x+10,(int) y+13);
	
}
}
}
