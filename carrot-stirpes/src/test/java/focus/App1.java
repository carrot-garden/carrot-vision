package focus;

import java.awt.Color;

import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Alert;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.ApplicationContext.DisplayHost;
import org.apache.pivot.wtk.Border;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.DesktopApplicationContext;
import org.apache.pivot.wtk.Dialog;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Frame;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.WindowClassListener;
import org.apache.pivot.wtk.Keyboard.KeyLocation;
import org.apache.pivot.wtk.MessageType;
import org.apache.pivot.wtk.TextInput;
import org.apache.pivot.wtk.Window;

/**
 * Application demonstrating how to direct keyboard events to the active
 * org.apache.pivot.wtk.Window, if the active window doesn't contain any
 * focusable Components.
 *
 * Relates to the following Apache Pivot mailing list thread.
 * http://apache-pivot
 * -users.399431.n3.nabble.com/KeyListeners-on-ImageView-tp2600622p2622383.html
 *
 *
 * It opens 2 native windows, each containing 4 types of Pivot Windows. - Window
 * - Frame - Dialog - Alert
 *
 * The Windows are given unique titles for identification. A
 * ComponentKeyListener is added to each Window. The listener simply consumes
 * any key events when there is no modifier being pressed.
 *
 * The Application implements UnprocessedKeyHandler, but delegates to a separate
 * handler class (Either SysOutUnprocessedKeyHandler or
 * ActiveWindowUnprocessedKeyHandler) (Comment/uncomment as required)
 *
 *
 * Usage - java focus.PivotWindowFocusTestApp
 *
 * When the 2 native windows open, clicking on the various Windows will update
 * the native frame title, and the active Frame, Alert & Dialog will be notable
 * by it's blue frame (not Window though).
 *
 * Click on any Pivot Window in either native window and press keys with &
 * without modifiers such as CONTROL, SHIFT or ALT.
 *
 * Refer to SysOut.
 */
public class App1 implements Application,
		Application.UnprocessedKeyHandler {

	private static final int NATIVE_WINDOW_WIDTH = 400;
	private static final int NATIVE_WINDOW_HEIGHT = 200;

	// Default to a NoOp handler
	private UnprocessedKeyHandler unprocessedKeyHandler = new NoOpUnprocessedKeyHandler();

	// CKL which consumes all key events unless a modifier is pressed
	private ComponentKeyListener simpleConsumer = new SimpleKeyEventConsumer();

	@Override
	public void startup(final Display display,
			final Map<String, String> properties) throws Exception {

		display.setName("ROOT Display");

		java.awt.Window host = display.getHostWindow();

		host.setSize(NATIVE_WINDOW_WIDTH, NATIVE_WINDOW_HEIGHT);

		// Create a 1st display
		final Display primaryDisplay = DesktopApplicationContext.createDisplay(
				NATIVE_WINDOW_WIDTH, NATIVE_WINDOW_HEIGHT, 100, 100, false,
				true, false, host, null);

		// Handler that dumps keyboard info to SysOut
		// unprocessedKeyHandler = SysOutUnprocessedKeyHandler.INSTANCE;

		// Delegate to a custom handler which will forward unprocessed
		// keyboard events to the active window's ComponentKeyListeners.
		unprocessedKeyHandler = ActiveWindowUnprocessedKeyHandler.INSTANCE;

		// Create a 2nd display
		final Display secondaryDisplay = DesktopApplicationContext
				.createDisplay(NATIVE_WINDOW_WIDTH, NATIVE_WINDOW_HEIGHT, 200,
						200, false, true, false, host, null);

		// Name both Displays to ease identification
		primaryDisplay.setName("Primary Display");
		secondaryDisplay.setName("Secondary Display");

		// Populate both Displays with various Windows which contain no
		// focusable Components.
		populateDisplay(primaryDisplay, false, true);
		populateDisplay(secondaryDisplay, true, true);

		Window.getWindowClassListeners().add(new WindowClassListener() {

			@Override
			public void activeWindowChanged(Window previousActiveWindow) {
				System.out.printf("Active window changed from %s to %s%n",
						windowTitleOrNull(previousActiveWindow),
						windowTitleOrNull(Window.getActiveWindow()));
			}
		});
	}

	protected Object windowTitleOrNull(Window w) {
		return w == null ? "null" : w.getTitle();
	}

	// Add various Windows (which contain no focusable Components) to the
	// Display
	// (This could alternatively load and open Windows from one or more BXML
	// files)
	private void populateDisplay(final Display display, boolean makeFocusable,
			boolean multiComponents) {
		// org.apache.pivot.wtk.Window
		final Window window = new Window(createBorder(Color.GREEN,
				Window.class, makeFocusable));
		window.setTitle(getTitle("Window", display));
		if (multiComponents)
			window.setLocation(10, 30);
		else {
			window.setMaximized(true);
			window.requestFocus();
		}
		window.open(display);

		if (multiComponents) {
			// org.apache.pivot.wtk.Frame
			final Frame frame = new Frame(getTitle("Frame", display),
					createBorder(Color.CYAN, Frame.class, makeFocusable));
			frame.setLocation(300, 30);
			frame.open(display);

			// org.apache.pivot.wtk.Dialog
			final boolean modal = false;
			final Dialog dialog = new Dialog(getTitle("Dialog", display),
					createBorder(Color.MAGENTA, Dialog.class, makeFocusable),
					modal);
			dialog.open(display);
			dialog.setLocation(10, 250);

			// org.apache.pivot.wtk.Alert
			final Alert alert = new Alert(MessageType.INFO, "Alert", null,
					createBorder(Color.ORANGE, Alert.class, makeFocusable),
					modal);
			alert.setTitle(getTitle("Alert", display));
			// Remove any buttons to ensure that there are no
			// focusable Components contained in the Alert
			while (alert.getOptions().getLength() > 0) {
				alert.getOptions().remove(0, 1);
			}
			alert.open(display);
			alert.setLocation(300, 250);
		}

		// Add ComponentKeyListeners to each of the Windows
		for (Component child : display) {
			child.getComponentKeyListeners().add(simpleConsumer);
		}
	}

	// Create a Border containing an ImageView
	private static Border createBorder(final Color color,
			final Class<? extends Component> klass, boolean makeFocusable) {
		Component content;
		if (makeFocusable) {
			content = new TextInput();
		} else {
			final ImageView imageView = new ImageView();
			imageView.setImage("/focus/logo-256x256.png");
			imageView.getStyles().put("fill", true);
			content = imageView;
		}

		final Border border = new Border();
		border.setTitle(klass.getName());
		border.getStyles().put("color", color);
		border.getStyles().put("thickness", 3);
		border.getStyles().put("padding", 10);
		border.setPreferredSize(220, 150);
		border.setContent(content);
		return border;
	}

	private static String getTitle(final String text, final Display display) {
		return String.format("[%s] %s", display.getName(), text);
	}

	// Application.UnprocessedKeyHandler delegation
	@Override
	public void keyTyped(char character) {
		unprocessedKeyHandler.keyTyped(character);
	}

	@Override
	public void keyPressed(int keyCode, KeyLocation keyLocation) {
		unprocessedKeyHandler.keyPressed(keyCode, keyLocation);
	}

	@Override
	public void keyReleased(int keyCode, KeyLocation keyLocation) {
		unprocessedKeyHandler.keyReleased(keyCode, keyLocation);
	}

	public static void main(String[] args) {
		// Enable the 'debug focus' decorator by setting a System property
		// which will be processed when the ApplicationContext is created
		System.setProperty("org.apache.pivot.wtk.debugfocus", "true");
		DesktopApplicationContext.main(App1.class, args);
	}

	@Override
	public boolean shutdown(boolean optional) throws Exception {
		return false;
	}

	@Override
	public void suspend() throws Exception {
	}

	@Override
	public void resume() throws Exception {
	}

}
