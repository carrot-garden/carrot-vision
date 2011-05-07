package focus;

import org.apache.pivot.util.ListenerList;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.Keyboard.KeyLocation;

public class FocusUtils {

	// Forward a KeyPress event to another component
	public static boolean forwardKeyPressed(final Component destination, final int keyCode, final KeyLocation keyLocation) {
		boolean consumed = false;
		if (destination != null) {
			ListenerList<ComponentKeyListener> listeners = destination.getComponentKeyListeners();
			for (ComponentKeyListener listener : listeners) {
				consumed |= listener.keyPressed(destination, keyCode, keyLocation);
			}
		}
		return consumed;
	}

	// Forward a KeyReleased event to another component
	public static boolean forwardKeyReleased(final Component destination, final int keyCode, final KeyLocation keyLocation) {
		boolean consumed = false;
		if (destination != null) {
			ListenerList<ComponentKeyListener> listeners = destination.getComponentKeyListeners();
			for (ComponentKeyListener listener : listeners) {
				consumed |= listener.keyReleased(destination, keyCode, keyLocation);
			}
		}
		return consumed;
	}

	// Forward a KeyTyped event to another component
	public static boolean forwardKeyTyped(final Component destination, final char c) {
		boolean consumed = false;
		if (destination != null) {
			ListenerList<ComponentKeyListener> listeners = destination.getComponentKeyListeners();
			for (ComponentKeyListener listener : listeners) {
				consumed |= listener.keyTyped(destination, c);
			}
		}
		return consumed;
	}

}
