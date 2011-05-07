package bench.app;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

@SuppressWarnings("serial")
public class AppFrame extends Frame {

	public AppFrame() {
		this("AppFrame");
	}

	public AppFrame(String title) {
		super(title);
		createUI();
	}

	protected void createUI() {
		setSize(500, 500);
		center();

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});
	}

	public void center() {

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		Dimension frameSize = getSize();

		int x = (screenSize.width - frameSize.width) / 5;
		int y = (screenSize.height - frameSize.height) / 2;

		setLocation(x, y);

	}

}