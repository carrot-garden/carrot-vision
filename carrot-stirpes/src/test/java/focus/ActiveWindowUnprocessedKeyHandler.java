package focus;

import org.apache.pivot.wtk.Application.UnprocessedKeyHandler;
import org.apache.pivot.wtk.Keyboard.KeyLocation;
import org.apache.pivot.wtk.Window;

// Forward the keyboard events to the active Window if there is one
// If the active Window does not consume the event, dump it to sysout.
public class ActiveWindowUnprocessedKeyHandler extends SysOutUnprocessedKeyHandler implements UnprocessedKeyHandler {

	public static final ActiveWindowUnprocessedKeyHandler INSTANCE = new ActiveWindowUnprocessedKeyHandler();

	@Override
	public void keyPressed(int keyCode, KeyLocation keyLocation) {
		final Window activeWindow = Window.getActiveWindow();
		final boolean consumed = FocusUtils.forwardKeyPressed(activeWindow, keyCode, keyLocation);
		if (!consumed) {
			super.keyPressed(keyCode, keyLocation);
		}
	}
	@Override
	public void keyTyped(char character) {
		final Window activeWindow = Window.getActiveWindow();
		final boolean consumed = FocusUtils.forwardKeyTyped(activeWindow, character);
		if (!consumed) {
			super.keyTyped(character);
		}
	}
	@Override
	public void keyReleased(int keyCode, KeyLocation keyLocation) {
		final Window activeWindow = Window.getActiveWindow();
		final boolean consumed = FocusUtils.forwardKeyReleased(activeWindow, keyCode, keyLocation);
		if (!consumed) {
			super.keyReleased(keyCode, keyLocation);
		}
	}

}
