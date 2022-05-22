import javax.swing.*;

import java.awt.*;
import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.util.*;




public class undertale extends JPanel implements KeyListener, MouseListener, Runnable{
	
	public static int counter;
	public static int gameState = 0;
	public static int eventState = 0;
	public static int mouseX;
	public static int mouseY;
	
	// chara variables
	public static int charaX = 478;
	public static int charaY = 222;
	public static int charaSpeed = 3;
	
    // Creating a path to import the pictures
    public static File path = new File("assets/charaAnimation");
    public static File[] charaFile = path.listFiles();
    public static BufferedImage[] charaImages = new BufferedImage[10];
	public static int curChara = 2;  // know which frame of chara to show
	
	//IMAGES
	
	public static BufferedImage titleScreen;

	// maps
    public static File path2 = new File("assets/maps/ruins");
    public static File[] ruinsFile = path2.listFiles();
    public static BufferedImage[] ruinsImages = new BufferedImage[4];
	public static int curRuins = 0;  // know which frame of ruins to show
	
    public static File path3 = new File("assets/maps/snowden");
    public static File[] snowdenFile = path3.listFiles();
    public static BufferedImage[] snowdenImages = new BufferedImage[4];
	public static int curSnowden = 0;  // know which frame of snowden to show
	
    public static File path4 = new File("assets/maps/temp");
    public static File[] tempFile = path4.listFiles();
    public static BufferedImage[] tempImages = new BufferedImage[4];
	public static int curTemp = 0;  // know which frame of temp to show
	
	
	BufferedImage fadeStart;
	BufferedImage fadeEnd;
	
	
	// position


	
	// other
	animation animation = new animation(this);
	charaAnimation charaAnimation = new charaAnimation(this);

	public undertale() {
		
		// 10 pixels less height and width than the background because
		// for some reason there is extra space when defining the frame
		// to be the same size as the background image
		setPreferredSize(new Dimension(990, 615));  
		setBackground(new Color(0, 0, 0));
		
		// import images
		try {
			titleScreen = ImageIO.read(new File("assets/undertalestartmenu.png"));
			
			for (int i = 0; i < 4; i++) {
                ruinsImages[i] = ImageIO.read(ruinsFile[i]);
                snowdenImages[i] = ImageIO.read(snowdenFile[i]);
                // tempImages[i] = ImageIO.read(tempFile[i]);


			}

            for (int i = 0; i < 10; i++) {
                charaImages[i] = ImageIO.read(charaFile[i]);
            }
	    
		}
		catch (Exception e) {
			System.out.println("Image does not exist");
		}
		
		// create thread
		Thread thread = new Thread(this);
		thread.start();
		
		// add listeners
		addKeyListener(this);
		this.setFocusable(true);
		addMouseListener(this);
		
		// other


	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("UNDERTALE");
		undertale panel = new undertale();
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
		frame.setResizable(false);

		
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, animation.alpha));

		// fade
		
		if (animation.fading) {
			if (!animation.faded) {
				g2d.drawImage(fadeStart, 0, 0, null);
				
				// fade out the character as well
				if (gameState - 1 != 0 && 1 <= gameState && gameState <= 3) {
			        g2d.drawImage(charaImages[curChara], charaX, charaY, null);
				}
			}
			else {
				g2d.drawImage(fadeEnd, 0, 0, null);
				// fade in the character as well
				if (1 <= gameState && gameState <= 3) {
			        g2d.drawImage(charaImages[curChara], charaX, charaY, null);
				}
			}

		}
		// the start menu
		else if (gameState == 0) {
			g2d.drawImage(titleScreen, 0, 0, null);

			
		}
		
		// exploration 
		else if (gameState == 1) {
			g2d.drawImage(ruinsImages[curRuins], 0, 0, null);
			try {
				Thread.sleep(100);
		        g2d.drawImage(charaImages[curChara], charaX, charaY, null);
			} 
			catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
			
		}				
	}
	


	
	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
		mouseX = e.getX();
		mouseY = e.getY();
		if (gameState == 0) {
			
			// play game
			if (370 <= mouseX && mouseX <= 670 && 185 <= mouseY && mouseY <= 270) {
				System.out.println("play");
				animation.fading = true;
				animation.wait = true;
				fadeStart = titleScreen;
				fadeEnd = ruinsImages[0];
				gameState = 1;
				animation.fade(fadeStart, fadeEnd, "slow");
				

			}
			
			// about 
			else if (370 <= mouseX && mouseX <= 670 && 300 <= mouseY && mouseY <= 390) {
				System.out.println("about");
				
				// change to about screen
				eventState = 2;


			}
			
			else if (370 <= mouseX && mouseX <= 670 && 420 <= mouseY && mouseY <= 505) {
				System.out.println("quit");
				System.exit(0); // terminates the program
			}
		}
		
		repaint();
		
	}


	@Override
    public void keyPressed(KeyEvent e) {
		if (!animation.fading && 1 <= gameState && gameState <= 3) {

	        if(e.getKeyChar() == 'w')
	        {
	            System.out.println("w");
	            charaY -= charaSpeed;
	            charaAnimation.key = 1;
	        }
	        else if(e.getKeyChar() == 'a')
	        {
	            System.out.println("a");
	            charaX -= charaSpeed;
	            charaAnimation.key = 2;
	        }
	        else if(e.getKeyChar() == 's')
	        {
	            System.out.println("s");
	            charaY += charaSpeed;
	            charaAnimation.key = 3;
	        }
	        else if(e.getKeyChar() == 'd')
	        {
	            System.out.println("d");
	            charaX += charaSpeed;
	            charaAnimation.key = 4;
	        }
	        charaAnimation.run();
		}
    }

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void run() {
		
	}
	
	
	// create an objects to measure the boundaries
	public static class dimension{
		
		public int x;
		public int y;
		public dimension(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	
	
	
	
///////////////////////////////////////////////////////	
	
	
	
	// useless methods
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
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



}
