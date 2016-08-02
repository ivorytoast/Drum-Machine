
/*
 * Program designed to allow the user to play music.
 * After the user plays a specific beat, it gets recorded.
 * This recorded piece of music transposed into sheet music.
 * 
 * It will allow for playback of the music just played
 * It will use a metronome
 * 
 * To play music -- the user hits a button
 * A record is kept in a growing array that has a "hit" object
 * Records the object hit and when (timer)
 * 
 * Could be an array that is at time intervals -- when it -- it would 
 * round to the cloest time intervals -- in other words -- quantizing it
 * All you would have to do then is go through the array in that exact time
 * interval for playback and it would play the same notes back
 * 
 */

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class KeyListenerTester extends JFrame implements KeyListener, Runnable {

    JLabel label;
    Thread thread;
    Thread trackThread;
    long counter = 0;
    ArrayList<Stroke> track = new ArrayList<Stroke>();
    PrintWriter writer = null;
    boolean mainThreadRunning = true;
    boolean trackThreadRunning = true;
    int i = 0;
    int size = 0;
    File yourFile;
    AudioInputStream stream;
    AudioFormat format;
    DataLine.Info info;
    Clip clip;

    public KeyListenerTester(String s) {
        super(s);
        JPanel p = new JPanel();
        label = new JLabel("Key Listener!");
        p.add(label);
        add(p);
        addKeyListener(this);
        setSize(200, 100);
        setVisible(true);
        thread = new Thread(this);
        trackThread = new Thread(this);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {}
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {}
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {}
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {}
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
        	track.add(new Stroke("snare", counter));
        	System.out.println("Snare hit at: " + counter);
        	playSound("snare.wav");
        	size++;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
        	track.add(new Stroke("hi-hat", counter));
        	System.out.println("Hi-hat hit at: " + counter);
        	playSound("hihat.wav");
        	size++;
        }
        if (e.getKeyCode() == KeyEvent.VK_UP) {
        	//displayTrack();
        	writeToFile();
        	stopMainThread();
        }
    }
    
    public void writeToFile() {
    	for (int i = 0; i < track.size(); i++) {
    		writer.println(track.get(i).drum + "," + track.get(i).time);
    	}
    	writer.close();
    }
    
    public void stopMainThread() {
    	try {
			//thread.join();
			mainThreadRunning = false;
			System.out.println("---PLAYING---");
			counter = 0;
		    i = 0;
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    
    public void playSound(String soundName)
    {
      try 
      {
       AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
       Clip clip = AudioSystem.getClip();
       clip.open(audioInputStream);
       clip.start();
      }
      catch(Exception ex)
      {
        System.out.println("Error with playing sound.");
        ex.printStackTrace( );
      }
    }

    public static void main(String[] args) {
    	KeyListenerTester main = new KeyListenerTester("Hello");
    	main.thread.start();
		try {
			main.writer = new PrintWriter("output2.txt", "UTF-8");
		} catch (Exception e) {
			System.out.println("Help");
		}
    }

	@Override
	public void run() {
		while (true) {
			if (mainThreadRunning) {
				try {
					thread.sleep(50);
					counter++;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					trackThread.sleep(50);
					counter++;
					if (trackThreadRunning && track.get(i).time == counter && i < size) {
						System.out.println(track.get(i).drum);
						if (track.get(i).drum.equalsIgnoreCase("hi-hat")) {
							playSound("hihat.wav");
						} 
						if (track.get(i).drum.equalsIgnoreCase("snare")) {
							playSound("snare.wav");
						}
						i++;
						if (i == size) {
							trackThreadRunning = false;
						}
 					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public synchronized void start() {
		thread.start();
	}
	
	public void displayTrack() {
		for (int i = 0; i < track.size(); i++) {
			System.out.println(track.get(i).drum);
		}
	}
	
	public void runTrack() {
		
	}
}
