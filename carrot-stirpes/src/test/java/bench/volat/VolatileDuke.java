package bench.volat;

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

/*
 * @(#)VolatileDuke.java	1.5 02/06/11
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.VolatileImage;

import javax.swing.ImageIcon;
import javax.swing.JApplet;

/**
 * VolatileDuke
 * 
 * This test app displays 4 versions of the same applet, running together. The
 * different apps vary in the volatility of the images they use. The basic
 * applet loads the duke.gif image, copies it into a sprite image, creates a
 * back bufffer and then renders frames. Each frame rendering consists of some
 * onscreen drawing (which should be invisible to the user unless something bad
 * happened to the images/surfaces; if Java2D is working correctly this should
 * not happen. Note that there may be some flickering, however, as we draw the
 * onscreen content first before copying the back buffer.), some rendering to
 * the back buffer (coloring the background and writing some text into it),
 * copying the sprite to a different location in the back buffer, and then
 * copying the back buffer onto the screen.
 * 
 * In order to test the VolatileImage implementation in Java2D on Windows, start
 * the application and then select a DOS box on the screen. Once the DOS window
 * is active, hit Alt-Enter, which forces DOS to go into fullscreen mode. This
 * triggers the loss of all DirectDraw surfaces, which affects all VolatileImage
 * objects inside of Java2D. Hite Alt-Enter again and observe the contents of
 * the application again. If everything is working correctly, there should be no
 * rendering artifacts. Things to watch out for are: contents of the onscreen
 * window (e.g., the string "Onscreen content" in any of the applet frames),
 * volatile images not appearing (e.g., maybe Duke has become a solid black
 * rectangle), and freezing of any of the animations.
 * 
 * This app can also be run in "performance" mode by using the -perf flag. This
 * mode runs the 4 volatile/non-volatile combinations in turn at some number of
 * iterations each. Each test is timed and the results are printed on stdout.
 * Running VolatileDuke in this mode is the only way to get a true performance
 * comparison of the various different image configurations; due to differences
 * in thread implementations on various platforms, running all configurations
 * together (as in the default case of VolatileDuke) will not necessarily give
 * the results you expect.
 * 
 * Finally, the app can run in single-demo mode, where the user specifies one
 * particular test to run. The flags are -nn, -nv, -vn, and -vv, standing for
 * the various combinations of non-volatile (n) and volatile (v) of the sprite
 * and the back-buffer.
 */

@SuppressWarnings("serial")
public class VolatileDuke extends JApplet implements Runnable {

	Image dukeImage;

	private static int windowW = 1000, windowH = 500;
	private static int spriteW, spriteH;

	private Thread thread;

	private int xLoc = 0, yLoc = 0;
	private int xStep = 7, yStep = 3;

	Image sprite;
	Image backBuffer;

	boolean bbVolatile = false;
	static int labelHeight = 15;

	static String volatileString = "Volatile";
	static String nonVolatileString = "Non-Vol";

	static boolean basicGraphics = false;

	String bbString;
	int width, height;

	public VolatileDuke(boolean bbVolatile) {

		super();

		this.bbVolatile = bbVolatile;
		if (bbVolatile)
			bbString = volatileString;
		else
			bbString = nonVolatileString;

	}

	/**
	 * Load the duke.gif image, create the sprite and back buffer images, and
	 * render the content into the sprite.
	 */
	public synchronized boolean initOffscreen() {

		if (dukeImage == null) {

			dukeImage = new ImageIcon("duke.gif").getImage();

			// spriteW = dukeImage.getWidth(null);
			// spriteH = dukeImage.getHeight(null) + labelHeight;

			spriteW = 255;
			spriteH = 200 + labelHeight;

			System.out.print("spriteW=" + spriteW);

			sprite = createImage(spriteW, spriteH);

			restoreSpriteContent();

		}

		if (backBuffer == null || width != getWidth() || height != getHeight()) {

			width = getWidth();
			height = getHeight();

			if (width == 0 || height == 0) {
				// window not created yet
				return false;
			}

			if (bbVolatile)
				backBuffer = createVolatileImage(width, height);
			else
				backBuffer = createImage(width, height);

		}

		return true;

	}

	/**
	 * Prepare the sprite location variables for the next frame
	 */
	public void step() {
		xLoc += xStep;
		yLoc += yStep;
		if (xLoc < 0) {
			xLoc = xStep;
			xStep = -xStep;
		} else if ((xLoc + spriteW) > getWidth()) {
			xLoc = getWidth() - spriteW - xStep;
			xStep = -xStep;
		}
		if (yLoc < 0) {
			yLoc = yStep;
			yStep = -yStep;
		} else if ((yLoc + spriteH) > getHeight()) {
			yLoc = getHeight() - spriteH - yStep;
			yStep = -yStep;
		}
	}

	/**
	 * For any of our images that are volatile, if the contents of the image
	 * have been lost since the last reset, reset the image and restore the
	 * contents. Note that these operations may not succeed (if the surfaces are
	 * not yet restoreable), but we will stay in our rendering loop and keep
	 * calling this method until we succeed.
	 */
	public void resetRestoreVolatileImages() {
		GraphicsConfiguration gc = this.getGraphicsConfiguration();
		if (bbVolatile) {
			int valCode = ((VolatileImage) backBuffer).validate(gc);
			if (valCode == VolatileImage.IMAGE_INCOMPATIBLE) {
				backBuffer = createVolatileImage(width, height);
			}
		}
	}

	/**
	 * Renders the sprite that we will use. We fill the sprite with a background
	 * color, copy in the image that we loaded, and label the sprite according
	 * to its volatility setting.
	 */
	public void restoreSpriteContent() {
		Graphics2D g2 = (Graphics2D) sprite.getGraphics();
		g2.setColor(Color.green);
		g2.fillRect(0, 0, spriteW, spriteH);
		g2.drawImage(dukeImage, null, null);
		g2.dispose();
	}

	/**
	 * Rendering loop of the applet. We first render into the onscreen window
	 * (this rendering should never been seen by the user (apart from
	 * flickering) unless there's a rendering problem with the back buffer. We
	 * then loop on rendering into the back buffer and copying that buffer onto
	 * the screen. The inner loop should usually iterate only once; it will
	 * repeat if there is a problem with any of the volatile surfaces.
	 */
	@Override
	public void paint(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if (!initOffscreen()) {
			return;
		}

		// Render the back buffer and copy it to the screen. Loop until
		// the buffer contents are correct (this will only loop when/if
		// surface are lost, causing content loss on volatile images).
		try {
			do {
				resetRestoreVolatileImages();
				Graphics2D gBB = (Graphics2D) backBuffer.getGraphics();
				gBB.setColor(Color.green);
				gBB.fillRect(0, 0, getWidth(), getHeight());
				if (!basicGraphics) {
					gBB.fillRect(1, 1, getWidth() - 1, getHeight() - 1);
					gBB.setColor(Color.black);
					gBB.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
					gBB.drawString(bbString, 5, getHeight() - 1);
				}
				gBB.drawImage(sprite, xLoc, yLoc, this);
				gBB.dispose();
				g.drawImage(backBuffer, 0, 0, this);
			} while ((bbVolatile && ((VolatileImage) backBuffer).contentsLost()));
		} catch (Exception e) {
			System.out.println("Exception during paint: " + e);
		}
	}

	@Override
	public void start() {
		thread = new Thread(this);
		thread.setPriority(Thread.MIN_PRIORITY);
		thread.start();
	}

	@Override
	public synchronized void stop() {
		thread = null;
	}

	@Override
	public void run() {
		Thread me = Thread.currentThread();
		while (thread == me) {
			step();
			Graphics g = getGraphics();
			update(g);
			g.dispose();
		}
		thread = null;
	}

	public long runPerf(int iterations) {
		Graphics g = getGraphics();
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < iterations; ++i) {
			step();
			update(g);
		}
		Toolkit.getDefaultToolkit().sync();
		return System.currentTimeMillis() - startTime;
	}

	/**
	 * Want none or only one of: perfMode nonVolMode volMode
	 */
	static boolean checkParameters(boolean perfMode, boolean nonVolMode,
			boolean volMode) {
		if (perfMode) {
			return !(nonVolMode || volMode);
		} else if (nonVolMode) {
			return !(perfMode || volMode);
		} else if (volMode) {
			return !(perfMode || nonVolMode);
		}
		return true;
	}

	static void printUsage() {
		System.out.println("Usage: \n"
				+ "    java VolatileDuke [ -v | -n | -perf] [-basic]\n"
				+ "where: \n" + "	    -v: Uses VolatileImage for back buffer\n"
				+ "	    -n: Uses BufferedImage for back buffer\n"
				+ "	    -perf: Runs performance test\n"
				+ "	    -basic: no border rectangles or text\n");
	}

	public static void main(String argv[]) {
		boolean perfMode = false;
		boolean volMode = false;
		boolean nonVolMode = false;

		Frame f = new Frame("Java 2D(TM) Demo - VolatileDuke");
		for (int i = 0; i < argv.length; ++i) {
			if (argv[i].equals("-perf")) {
				perfMode = true;
			} else if (argv[i].equals("-basic")) {
				basicGraphics = true;
			} else if (argv[i].equals("-n")) {
				nonVolMode = true;
			} else if (argv[i].equals("-v")) {
				volMode = true;
			} else {
				printUsage();
				System.exit(0);
			}
		}
		if (!checkParameters(perfMode, nonVolMode, volMode)) {
			printUsage();
			System.exit(0);
		}
		if (perfMode) {
			f.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			});
			int i, j;
			boolean boolArray[] = { false, true };
			int windowSizes[] = { 100, 300, 500, 700 };
			System.out.println("VolatileDuke Performance Test\n");
			System.out
					.println("     Window Size      Back-Buffer     Milliseconds");
			System.out
					.println("     -----------      -----------     ------------");
			for (i = 0; i < windowSizes.length; ++i) {
				for (j = 0; j < 2; ++j) {
					VolatileDuke demo = new VolatileDuke(boolArray[j]);
					f.add(demo);
					f.pack();
					f.show();
					f.setSize(new Dimension(windowSizes[i], windowSizes[i]));
					long perfTime = demo.runPerf(500);
					System.out.print("        " + windowSizes[i] + " x "
							+ windowSizes[i] + "   ");
					if (boolArray[j] == false)
						System.out.print("  non-volatile");
					else
						System.out.print("    volatile  ");
					System.out.println("        " + perfTime);
					f.remove(demo);
				}
			}
			System.exit(0);
		} else if (volMode || nonVolMode) {
			final VolatileDuke demo;
			if (nonVolMode) {
				demo = new VolatileDuke(false);
			} else { // nonVolMode == true;
				demo = new VolatileDuke(true);
			}
			f.add(demo);
			f.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}

				@Override
				public void windowDeiconified(WindowEvent e) {
					demo.start();
				}

				@Override
				public void windowIconified(WindowEvent e) {
					demo.stop();
				}
			});
			f.pack();
			f.setSize(new Dimension(windowW, windowH));
			f.show();
			demo.start();
		} else {
			f.setLayout(new GridLayout(1, 2));
			final VolatileDuke demo1 = new VolatileDuke(false);
			f.add(demo1);
			final VolatileDuke demo2 = new VolatileDuke(true);
			f.add(demo2);
			f.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}

				@Override
				public void windowDeiconified(WindowEvent e) {
					demo1.start();
					demo2.start();
				}

				@Override
				public void windowIconified(WindowEvent e) {
					demo1.stop();
					demo2.stop();
				}
			});
			f.pack();
			f.setSize(new Dimension(windowW, windowH));
			f.show();
			demo1.start();
			demo2.start();
		}
	}
}
