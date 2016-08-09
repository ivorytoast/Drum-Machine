
package com.hazzlesoftware.sound;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
	
	public static final int LOOP_CONTINUOUSLY = Clip.LOOP_CONTINUOUSLY;
	private AudioInputStream ais;
	private Clip clip;
	private Thread activeThread;
	
	/**
	 * Instantiates a new Sound object from the specified file.
	 * @param soundFile The audio file the be used.
	 */
	public Sound(File soundFile) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		ais = AudioSystem.getAudioInputStream(soundFile);
		clip = AudioSystem.getClip();
	}
	
	/**
	 * Instantiates a new Sound object from the specified String.
	 * @param path Path to the audio file.
	 */
	public Sound(String path) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
		this(new File(path));
	}
	
	public Sound(InputStream soundStream) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		ais = AudioSystem.getAudioInputStream(soundStream);
		clip = AudioSystem.getClip();
	}
	
	public void open() throws LineUnavailableException, IOException {
		clip.open(ais);
	}
	
	public boolean isOpen() {
		boolean here = false;
		if (clip != null) {
			here = true;
		    System.out.println("Its here!");
		} else {
			System.out.println("There is no clip here!");
		}
		return here;
	}
	
	public void play() throws LineUnavailableException, IOException {
		if(!clip.isOpen())
			open();
			
		if(!clip.isRunning()) {
			clip.start();
			active(1);
		}
	}
	
	public void loop(int count) throws LineUnavailableException, IOException {
		if(!clip.isOpen()) {
			open();
			System.out.println("The clip wasn't open -- now it is");
		}
			
		if(!clip.isRunning()) {
			clip.loop(count);
			//active(count);
		}
	}
	
	public void pause() {
		if(clip.isRunning())
			activeThread.interrupt();
	}
	
	public void setMicrosecondPosition(long us) {
		clip.setMicrosecondPosition(us);
	}
	
	public void close() throws IOException {
		if (activeThread != null) {
			//activeThread.interrupt();
		}
		clip.flush();
		clip.close();
		clip = null;
		ais.close();
		ais = null;
		System.gc();
	}
	
	public boolean isRunning() {
		return clip.isRunning();
	}
	
	/**
	 * Runs the activeThread which guarantees that the sound will be playable.<br>
	 * The thread will loop <tt>TIMES</tt> times.
	 */
	void active(final int TIMES) {
		Runnable active = () -> {
			//calculates how many more ms it will take before the sound ends
			long ms = (clip.getMicrosecondLength() - clip.getMicrosecondPosition()) / 1000;
			if(TIMES != LOOP_CONTINUOUSLY)
				ms *= TIMES;
				
			try {
				do {
					if (ms > 0)
						Thread.sleep(ms);
				} while(TIMES == LOOP_CONTINUOUSLY);
			} catch(InterruptedException e) {
				clip.stop();
			}
		};
		
		activeThread = new Thread(active);
		activeThread.start();
	}
	
	public void dispose() {
		try {
			close();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}

}
