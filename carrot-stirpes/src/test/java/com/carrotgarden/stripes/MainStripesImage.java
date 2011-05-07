package com.carrotgarden.stripes;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bench.app.AppFrame;

@SuppressWarnings("serial")
public class MainStripesImage extends AppFrame {

	static {
		// -Dsun.java2d.opengl=True
		System.setProperty("sun.java2d.opengl", "True");
	}

	private static Logger log = LoggerFactory.getLogger(TestStripesImage.class);

	public static void main(String[] args) throws Exception {

		log.info("started");

		final MainStripesImage app = new MainStripesImage();

		app.setVisible(true);

		log.info("finished");

		final Thread thread = new Thread() {
			@Override
			public void run() {
				while (true) {

					app.repaint(0);

					try {

						// log.info("paint");

						image000.roll(1);
						image045.roll(1);
						image090.roll(1);
						image135.roll(1);
						image180.roll(1);
						image225.roll(1);
						image270.roll(1);
						image315.roll(1);

						sleep(40);

					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		thread.start();

		thread.join();

	}

	final static int W = 100;
	final static int H = 100;
	final static int P = 25;

	final static StripesImage image000 = new StripesImage.Builder()
			.angle(Direct.ANGLE_000.angle).width(W).height(H).period(P).build();

	final static StripesImage image045 = new StripesImage.Builder()
			.angle(Direct.ANGLE_045.angle).width(W).height(H).period(P).build();

	final static StripesImage image090 = new StripesImage.Builder()
			.angle(Direct.ANGLE_090.angle).width(W).height(H).period(P).build();

	final static StripesImage image135 = new StripesImage.Builder()
			.angle(Direct.ANGLE_135.angle).width(W).height(H).period(P).build();

	final static StripesImage image180 = new StripesImage.Builder()
			.angle(Direct.ANGLE_180.angle).width(W).height(H).period(P).build();

	final static StripesImage image225 = new StripesImage.Builder()
			.angle(Direct.ANGLE_225.angle).width(W).height(H).period(P).build();

	final static StripesImage image270 = new StripesImage.Builder()
			.angle(Direct.ANGLE_270.angle).width(W).height(H).period(P).build();

	final static StripesImage image315 = new StripesImage.Builder()
			.angle(Direct.ANGLE_315.angle).width(W).height(H).period(P).build();

	private boolean firstTime = true;

	private BufferedImage bi;

	private Graphics2D big;

	@Override
	public void paint(final Graphics g) {

		final Graphics2D desk = (Graphics2D) g;

		// final Rectangle bounds = getBounds();
		// final GraphicsEnvironment local = GraphicsEnvironment
		// .getLocalGraphicsEnvironment();
		// final GraphicsDevice screen = local.getDefaultScreenDevice();
		// final GraphicsConfiguration configuration = screen
		// .getDefaultConfiguration();
		// final BufferedImage image = configuration.createCompatibleImage(
		// bounds.width, bounds.height);
		// final Graphics2D imgageDesk = image.createGraphics();

		//

		if (firstTime) {
			Dimension dim = getSize();
			int w = dim.width;
			int h = dim.height;
			bi = (BufferedImage) createImage(w, h);
			big = bi.createGraphics();
			firstTime = false;
		}
		//

		image000.drawTo(big, 60, 60);
		image045.drawTo(big, 60, 160);
		image090.drawTo(big, 60, 260);
		image135.drawTo(big, 60, 360);
		image180.drawTo(big, 260, 60);
		image225.drawTo(big, 260, 160);
		image270.drawTo(big, 260, 260);
		image315.drawTo(big, 260, 360);

		// imageHor.drawTo(desk, 25, 30);
		// imageVer.drawTo(desk, 275, 25);
		// imageAng045.drawTo(desk, 25, 275);
		// imageAng135.drawTo(desk, 275, 275);

		desk.drawImage(bi, 0, 0, null);

	}

	@Override
	public void update(Graphics g) {
		paint(g);
	}

}
