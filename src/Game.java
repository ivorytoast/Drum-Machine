
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.hazzlesoftware.sound.Sound;

public class Game extends JPanel implements KeyListener, Runnable {

	Thread thread;
	Sound snare;
	Sound bass;
	boolean playSound = true;
	int playerOneX = 400;
	int playerOneY = 600;
	int playerTwoX = 500;
	int playerTwoY = 600;
	int cursorX = 0;
	ArrayList<Integer> snareHits = new ArrayList<Integer>();
	ArrayList<Integer> bassHits = new ArrayList<Integer>();
	//int[] bassHits = {100, 150, 200, 250, 300, 350, 400, 450, 500};
	int snareDrumTrackCounter = 0;
	int bassDrumTrackCounter = 0;
	int cursorHeight = 100;
	long mainProgramCounter = 0;

	public Game() {
		super();
		addKeyListener(this);
		setBackground(Color.WHITE);
		thread = new Thread(this);
		addKeyListener(this);
		//snareHits.add(28);
		//snareHits.add(50);
		//bassHits.add(15);
		//bassHits.add(24);
		//bassHits.add(33);
		//bassHits.add(44);
		//bassHits.add(47);
		//bassHits.add(57);
	}

	public void paintComponent(Graphics g) {
		int width = getWidth();
		int height = getHeight();
		// Code
		g.setColor(Color.RED);
		g.drawLine(cursorX, ((height / 2) - (cursorHeight / 2 + 50)), cursorX, ((height / 2) + cursorHeight / 2 + 50)); //cursor
		g.setColor(Color.BLACK);
		g.drawLine(10, ((height / 2) - (cursorHeight / 2)), width - 10, ((height / 2) - cursorHeight / 2)); //--3--
		g.drawLine(10, (height / 2), width - 10, (height / 2)); //--2--
		g.drawLine(10, ((height / 2) + (cursorHeight / 2)), width - 10, ((height / 2) + cursorHeight / 2)); //--1--
		g.setColor(Color.GREEN);
		for (int i = 0; i < snareHits.size(); i++) {
			g.setColor(Color.GREEN);
			g.fillRect(snareHits.get(i), height / 2, 2, 20);
		}
		g.setColor(Color.PINK);
		for (int i = 0; i < bassHits.size(); i++) {
			g.fillRect(bassHits.get(i), height / 2 + 50, 2, 20);
		}
		if (cursorX < width - 50)
			cursorX = cursorX + 1;
		if (cursorX == snareHits.get(snareDrumTrackCounter)) {
			try {
				snare = new Sound("snare.wav");
				if (cursorX == snareHits.get(snareDrumTrackCounter))
					snare.play();
				if (snare.isRunning()) {
					snare = new Sound("snare.wav");
				}
				if (snareDrumTrackCounter < snareHits.size() - 1)
					snareDrumTrackCounter++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (cursorX == bassHits.get(bassDrumTrackCounter)) {
			try {
				bass = new Sound("bass.wav");
				if (cursorX == bassHits.get(bassDrumTrackCounter))
					bass.play();
				if (bass.isRunning()) {
					bass = new Sound("bass.wav");
				}
				if (bassDrumTrackCounter < bassHits.size() - 1)
					bassDrumTrackCounter++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		}
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_W) {
			snareHits.add((int) mainProgramCounter);
		}
		if (e.getKeyCode() == KeyEvent.VK_A) {
			bassHits.add((int) mainProgramCounter);
		}
		if (e.getKeyCode() == KeyEvent.VK_D) {
			for (int i = 0; i < snareHits.size(); i++) {
				System.out.println(snareHits.get(i));
			}
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			System.out.println("Down");
			playerOneY = playerOneY + 10;
			repaint();
		}
		if (e.getKeyCode() == KeyEvent.VK_UP) {
			System.out.println(playerTwoY);
			playerTwoY = playerTwoY - 10;
			repaint();
		}
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
			System.out.println("Left");
			playerTwoX = playerTwoX - 10;
			repaint();
		}
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
			System.out.println("Right");
			playerTwoX = playerTwoX + 10;
			repaint();
		}
		if (e.getKeyCode() == KeyEvent.VK_DOWN) {
			System.out.println("Down");
			playerTwoY = playerTwoY + 10;
			repaint();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		/*
		 * if (e.getKeyCode() == KeyEvent.VK_W) { System.out.println("Up");
		 * playerOneY = playerOneY - 25; repaint(); } if (e.getKeyCode() ==
		 * KeyEvent.VK_A) { System.out.println("Left"); } if (e.getKeyCode() ==
		 * KeyEvent.VK_D) { System.out.println("Right"); } if (e.getKeyCode() ==
		 * KeyEvent.VK_S) { System.out.println("Down"); }
		 */
	}

	public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		Game game = new Game();
		JFrame application = new JFrame();
		game.readFile();
		game.snare = new Sound("snare.wav");
		game.bass = new Sound("bass.wav");
		application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		application.add(game);
		application.addKeyListener(game);
		application.setSize(1000, 1000);
		application.setVisible(true);
		game.thread.start();
	}

	public void run() {
		while (true) {
			super.repaint();
			try {
				Thread.sleep(40);
				mainProgramCounter++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void readFile() throws IOException {
		String line = "";
		try {
            Scanner input = new Scanner(System.in);
            File file = new File("/Users/Anthony/Desktop/Main/ECLIPSE/Hazzle-Music/output2.txt");
            input = new Scanner(file);
            while (input.hasNextLine()) {
            	int bassStart = 0;
            	int bassEnd = 0;
            	String bassHitsString = "";
                line = input.nextLine();
                int snareStart = line.indexOf("/");
                int snareEnd = line.indexOf("/", 1);
                String snareHitsString = line.substring((snareStart + 1), (snareEnd - 1));
                if (line.indexOf("/", snareEnd + 1) != -1) { 
                	bassStart = line.indexOf("/", snareEnd + 1); 
                	bassEnd = line.indexOf("/", bassStart + 1);
                	bassHitsString = line.substring((bassStart + 1), (bassEnd - 1));
                } else {
                	bassHitsString = line.substring(snareEnd + 1, line.length() - 1); 
                }
                System.out.println(line);
                //System.out.println(snareHits);
                //System.out.println(bassHits);
                String[] snareNumbers = snareHitsString.split(",");
                //System.out.println("Number to be added: " + numbers[0]);
                for (int j = 0; j < snareNumbers.length; j++) {
                	int value = Integer.parseInt(snareNumbers[j]);
               		snareHits.add(value);
               	}
             
                String[] bassNumbers = bassHitsString.split(",");
                	for (int j = 0; j < bassNumbers.length; j++) {
                		int value = Integer.parseInt(bassNumbers[j]);
                		bassHits.add(value);
                	}
                }
           
            System.out.println(snareHits);
            System.out.println(bassHits);
            input.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
	}
}
