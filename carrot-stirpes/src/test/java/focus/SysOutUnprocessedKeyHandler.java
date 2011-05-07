package focus;

import org.apache.pivot.wtk.Application.UnprocessedKeyHandler;
import org.apache.pivot.wtk.Keyboard;
import org.apache.pivot.wtk.Keyboard.KeyLocation;

// Forward the keyboard events to the active Window if there is one
// If the active Window does not consume the event, dump it to SysOut.
public class SysOutUnprocessedKeyHandler implements UnprocessedKeyHandler {

	public static final SysOutUnprocessedKeyHandler INSTANCE = new SysOutUnprocessedKeyHandler();

	@Override
	public void keyPressed(int keyCode, KeyLocation keyLocation) {
		System.out.println(String.format("%-40s KeyCode : %-4d  Modifiers : %d",
				"UnprocessedKeyHandler#keyPressed()", keyCode, Keyboard.getModifiers()));
	}
	@Override
	public void keyTyped(char character) {
		System.out.println(String.format("%-40s Char    : %-4c  Modifiers : %d",
				"UnprocessedKeyHandler#keyTyped()", character, Keyboard.getModifiers()));
	}
	@Override
	public void keyReleased(int keyCode, KeyLocation keyLocation) {
		System.out.println(String.format("%-40s KeyCode : %-4d  Modifiers : %d",
				"UnprocessedKeyHandler#keyReleased()", keyCode, Keyboard.getModifiers()));
	}

}
