package focus;

import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.ComponentKeyListener;
import org.apache.pivot.wtk.Keyboard;
import org.apache.pivot.wtk.Keyboard.KeyLocation;
import org.apache.pivot.wtk.Window;

// Consume all keyboard events unless a Modifier is pressed
public class SimpleKeyEventConsumer implements ComponentKeyListener {

	@Override
	public boolean keyPressed(Component component, int keyCode, KeyLocation keyLocation) {
		return consume(component);
	}
	@Override
	public boolean keyReleased(Component component, int keyCode, KeyLocation keyLocation) {
		return consume(component);
	}
	@Override
	public boolean keyTyped(Component component, char character) {
		return consume(component);
	}

	// Consume all keyboard events unless a Modifier is pressed
	private static boolean consume(Component component) {
		final boolean consumed = (Keyboard.getModifiers() == 0);
		if (consumed) {
			final Window window = (Window) component;
			System.out.println(String.format("Consumed by  %-40s Modifiers : %d", window.getTitle(), Keyboard.getModifiers()));
		}
		return consumed;
	}

}
