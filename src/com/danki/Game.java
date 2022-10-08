package com.danki;

import java.awt.Canvas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.image.DataBufferInt;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
//import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
//import java.awt.Graphics2D;
//import java.awt.Image;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.entities.BulletShoot;
import com.entities.Enemy;
import com.entities.Entity;
import com.entities.Npc;
import com.entities.Player;
import danki.com.graficos.Spritesheet;
import danki.com.graficos.UI;
import danki.world.*;

public class Game extends Canvas implements Runnable,KeyListener, MouseListener, MouseMotionListener{

	private static final long serialVersionUID = 1L;

	public static JFrame frame;

	private Thread thread;

	private boolean isRunning = true;

	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 3;
	public static String DataBufferInt = "DataBufferInt";
    public static String gameState = "MENU";
    private boolean showMessageGameOver = true;
    private int framesGameOver = 0;
    private boolean restartGame = false;
    //SISTEMA CUTSCENE
    public static int entrance = 1;
    public static int starting = 2;
    public static int playing = 3;
    public static int estate_scene = playing;
    public  int timeScene = 0, maxTimeScene = 60*3;
    public Menu menu;
    
    public int []pixels;
    public BufferedImage lightmap;
    public int[]lightMapPixels;
    public static int[]  miniMapPixels;
    public static  BufferedImage minimap;
    public int mx,my;
	private int CUR_LEVEL = 1, MAX_LEVEL = 2;
	private BufferedImage image;
	
	public boolean saveGame = false;

	public static List<Entity> entities;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	
    public static  World world;
	public static Spritesheet spritesheet;
   public static Player player;
   public static Random rand;
   public UI ui;
  // public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixefont.ttf"); 
   //public Font newfont;
		public Game() {
		
         rand = new Random();
			addKeyListener(this);
			addMouseListener(this);
			addMouseMotionListener(this);
			setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
			//setPreferredSize(new Dimension(Toolkit.getDefaultToolkit().getScreenSize()));
			initFrame();
			  
	//iniciando objeto
			
		ui = new UI();	
		
			image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
			 
			try {
				lightmap = ImageIO.read(getClass().getResource("/lightmap.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			lightMapPixels = new int [lightmap.getWidth()* lightmap.getHeight()];
			lightmap.getRGB(0,0, lightmap.getWidth(), lightmap.getHeight(),lightMapPixels,0,lightmap.getWidth());
			pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData(); 
			entities = new ArrayList<Entity>();
			enemies = new ArrayList<Enemy>();
			bullets = new ArrayList<BulletShoot>();
			spritesheet = new Spritesheet("/spritesheet.png");
			player = new Player(0, 0, 16, 16, spritesheet.getSprite(0, 11, 16, 16));
			Npc npc = new Npc(32,32,16,16,spritesheet.getSprite(332, 11, 16, 16));
			entities.add(npc);
			entities.add(player);
			world = new World("/level1.png");
			
			minimap = new BufferedImage(World.WIDTH, World.HEIGHT, BufferedImage.TYPE_INT_RGB);
			miniMapPixels = ((DataBufferInt)minimap.getRaster().getDataBuffer()).getData();
			menu = new Menu();
		 /*  try {
			newfont = Font.createFont(Font.TRUETYPE_FONT, stream).deriveFont(70f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
	}

	public void initFrame() {

		frame = new JFrame("Game01");
		frame.add(this);
		frame.setUndecorated(true);
		frame.setResizable(true);
				
				
				
				
				
				
		frame.pack();
		//icone da Janela
		Image imagem = null;
		try {
			imagem = ImageIO.read(getClass().getResource("/icon.png"));
			
					} catch (IOException e) {
			e.printStackTrace();
		}
		
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Image image = toolkit.getImage(getClass().getResource("/icon.png"));
		//Cursor c = toolkit.createCustomCursor(image,new Point(0,0),"img");
		//frame.setCursor(c);
		frame.setIconImage(imagem);
		frame.setAlwaysOnTop(true);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);

	}

	public synchronized void start() {

		thread = new Thread(this);

		isRunning = true;

		thread.start();

	}

	public synchronized void stop() {

		isRunning = false;

		try {

			thread.join();

		} catch (InterruptedException e) {

			e.printStackTrace();

		}

	}

	public static void main(String args[]) {

		Game game = new Game();

		game.start();

	}

	public void tick() {
		Sound.Clips.music.Loop();
		if(gameState == "NORMAL") {
			if(this.saveGame) {
			this.saveGame= false;
			
			
			String[] opt1 = {"level"};
			int[] opt2 = {this.CUR_LEVEL};
			Menu.saveGame(opt1,opt2,10);
			System.out.println("Game saved");
			}
         this.restartGame = false;
         if(Game.estate_scene == Game.playing) {
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.tick();

		for(int i1 =0; i1 < bullets.size(); i1++) {
		bullets.get(i1).Tick();
		}
		} 
          	
		}else {
		if(Game.estate_scene == Game.entrance) {
			///VOLTAR AQUI SISTEMA DE CUTSCENE!!
			if(player.getX() < 100) {
			player.x++;
			}else {
			System.out.println("entrada feita");
			Game.estate_scene = Game.starting;
			}
		}else if(Game.estate_scene == Game.starting) {
			timeScene++;
			if(timeScene == maxTimeScene) {
			Game.estate_scene = Game.playing;
			}
		}
		}
		
		
		if(enemies.size() == 0) {
		//avançar para próximo nível
		CUR_LEVEL++;
		if(CUR_LEVEL > MAX_LEVEL) {
		CUR_LEVEL= 1;
		
		}
		String newWorld = "Level"+ CUR_LEVEL+".png";
		World.restartGame(newWorld);
		}
		} 
		 else if(gameState == "GAME_OVER") {
		this.framesGameOver++;
		if(this.framesGameOver ==35) {
		this.framesGameOver= 0;
		if(this.showMessageGameOver)
		this.showMessageGameOver = false;
		else
		this.showMessageGameOver = true;
		
		}
		if(restartGame) {
		this.restartGame = false;
		this.gameState = "NORMAL";
		System.out.println("Reiniciar");
			CUR_LEVEL = 1;
			String newWorld = "Level"+ CUR_LEVEL+".png";
			World.restartGame(newWorld);
		}
		}else if(gameState == "MENU") {
		menu.Tick();
			
		}
		
		}
	
	
	
	public void applyLight() {
		
	for(int xx = 0; xx < Game.WIDTH; xx++) {
		for(int yy = 0; yy < Game.HEIGHT; yy++) {
			if(lightMapPixels[xx+(yy*Game.WIDTH)] == 0xffffffff) {
			int pixel = Pixel.getLightBlend(pixels[xx+yy*WIDTH], 0X808080,0);
			pixels[xx+yy*WIDTH]= pixel;
			}
			
		pixels[xx+(yy*Game.WIDTH)]= 0;
		}
	}
		
		
		
	}
	
	

	public <T> void render() {

		BufferStrategy bs = this.getBufferStrategy();

		if (bs == null) {

			this.createBufferStrategy(3);

			return;

		}

		Graphics g = image.getGraphics();
  
		//g.setColor(new Color(0,0, 0));

		//g.fillRect(0, 0, WIDTH, HEIGHT);
//renderização do game.
		applyLight();
		world.Render(g);
		Collections.sort(entities,Entity.nodeSorter);
		
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i =0; i < bullets.size(); i++) {
			bullets.get(i).render(g);
			}
		
		
		ui.render(g);
	
         /***/
	g.dispose();	
	
	g = bs.getDrawGraphics();	
	//g.drawImage(image, 0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height, null);
	g.drawImage(image, 0, 0,WIDTH*SCALE, HEIGHT*SCALE, null);
	g.setFont(new Font ( "arial", Font.BOLD,17));
	g.setColor(Color.white);
	g.drawString("Arrow :"+ player.ammo, 605, 20);
	/*g.setFont(newfont);
	g.drawString("teste com a nova fonte", 90,90);*/
	//applyLight();
	if(gameState == "GAME_OVER") {
		Graphics2D g2 = (Graphics2D)g;
		g2.setColor(new Color (0,0,0,100));
		g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);
		g.drawImage(image, 0, 0, WIDTH * SCALE, HEIGHT * SCALE, null);
		g.setFont(new Font ( "arial", Font.BOLD,60));
		g.setColor(Color.red);
		g.drawString("YOU DIED", WIDTH*SCALE/2- 100,(HEIGHT*SCALE)/2- 20);
		g.setFont(new Font ( "arial", Font.BOLD,40));
		if(showMessageGameOver)
		g.drawString(">PRESS ENTER TO CONTINUE<", WIDTH*SCALE/2- 300,(HEIGHT*SCALE)/2+ 40);
		
	}else if(gameState == "MENU") {
		menu.Render(g);
		
	}
	
	
	if(Game.estate_scene == Game.starting) {
		g.setFont(new Font ( "arial", Font.BOLD,40));
		g.drawString("Ready?", 300, 200);
		
	}
	//World.RenderMiniMap();
	
	
	//g.drawImage(minimap,600,30,World.WIDTH*5,World.HEIGHT*5,null);
	bs.show();

	}

	public void run() {
		
		long lastTime = System.nanoTime();

		double amountOfTicks = 60.0;

		double ns = 1000000000 / amountOfTicks;

		double delta = 0;

		int frames = 0;

		double timer = System.currentTimeMillis();
        requestFocus();
		while (isRunning) {

			long now = System.nanoTime();

			delta += (now - lastTime) / ns;

			lastTime = now;

			if (delta >= 1) {

				tick();

				render();

				frames++;

				delta--;

			}

			if (System.currentTimeMillis() - timer >= 1000) {

				System.out.println("FPS : " + frames);

				frames = 0;

				timer += 1000;

			}

		}

		stop();

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	
	public void keyPressed(KeyEvent e) {
	if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
			e.getKeyCode() == KeyEvent.VK_D){
		
		player.right = true;
		//execute tal ação
		
	}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
			 e.getKeyCode() == KeyEvent.VK_A) {
		player.left = true;
	}
	if(e.getKeyCode() == KeyEvent.VK_UP ||
		e.getKeyCode() == KeyEvent.VK_W) {
		
		if(gameState == "MENU") {
			menu.up = true;
		}
		
		player.up = true;
	}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
		e.getKeyCode() == KeyEvent.VK_S){
		player.down = true;
			
		if(gameState == "MENU") {
			menu.down = true;
		}
		
	}
	
	if(e.getKeyCode() == KeyEvent.VK_X) {
	player.shoot = true;
	}
	
	
	
	if(e.getKeyCode()== KeyEvent.VK_ENTER) {
		
	if(e.getKeyCode() == KeyEvent.VK_ENTER) {
		Npc.showMessage = false;
	}
		
		
	this.restartGame = true;
    if(gameState == "MENU") {
    	
    	menu.enter = true;
    }
	}
	
	
	if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
		gameState = "MENU";
		menu.pause = true;
	}
	
	
	
	if(e.getKeyCode()== KeyEvent.VK_SPACE) {
		
		if(Game.gameState == "NORMAL")
		this.saveGame = true;
	}
	
	}

	@Override
	public void keyReleased(KeyEvent e) { 
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D){
				
				player.right = false;
				
				
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
					 e.getKeyCode() == KeyEvent.VK_A) {
				
				player.left = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
				
				player.up = false;
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S){
				player.down = false;
					
			}
}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	player.mouseShoot = true;
	player.mx = (e.getX()/3 ) ;
	player.my = (e.getY()/ 3) ;
	System.out.println(player.mx);
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		this.mx = e.getX();
		this.my = e.getY();
		
	}
	
		
		
	}

