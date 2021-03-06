package bench.flicker;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class FlickeringFace extends Canvas implements Runnable {

	private Image face;
	protected Thread th;
	private final boolean running;
	private boolean flagx, flagy;
	private int posx = 0, posy = 0;
	private final int index = 0;
	private URL url;

	FlickeringFace() {

		super();

		setVisible(true);
		setSize(500, 500);

		running = true;
		flagx = true;
		flagy = true;
		url = this.getClass().getResource("fa2.png");

		try {
			face = ImageIO.read(url);

		} catch (IOException e) {

			e.printStackTrace();
		}
		url = null;

		th = new Thread(this);
		th.start();

	}

	@Override
	public void run() {

		while (true) {
			try {
				Thread.sleep(10);
			} catch (Exception ex) {

			}
			repaint();
		}
	}

	@Override
	public void paint(Graphics g) {

		try {
			animation(g);

		} catch (Exception ex) {
			th.stop();
		}
	}

	public void animation(Graphics g) {
		g.setColor(Color.GREEN);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());

		g.drawImage(face, posx, posy, null);

		if (posx < this.getWidth() - face.getWidth(null) && flagx) {
			posx += Face.STEP;
		} else {
			flagx = false;
		}
		if (!flagx) {
			posx -= Face.STEP;
			if (posx <= 0) {
				flagx = true;
			}
		}

		if (posy < this.getHeight() - face.getHeight(null) && flagy) {
			posy += Face.STEP;
		} else {
			flagy = false;
		}
		if (!flagy) {
			posy -= Face.STEP;
			if (posy <= 0) {
				flagy = true;
			}
		}

	}

}
