package bench.flicker;

import java.awt.GridLayout;

import javax.swing.JApplet;

@SuppressWarnings("serial")
public class Face extends JApplet {

	/* enable/disable -Dsun.java2d.opengl=true */

	static final int STEP = 1;

	private NoFlickeringFace fc;
	private FlickeringFace ff;

	// Face(){
	@Override
	public void init() {

		// super("animation");

		setLayout(new GridLayout(1, 2, 1, 1));

		fc = new NoFlickeringFace();
		ff = new FlickeringFace();

		add(fc);
		add(ff);

		setVisible(true);

		validate();

	}

	public static void main(String args[]) {

		Face face = new Face();

		face.init();

	}

}
