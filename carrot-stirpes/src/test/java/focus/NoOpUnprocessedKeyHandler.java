package focus;

import org.apache.pivot.wtk.Application.UnprocessedKeyHandler;
import org.apache.pivot.wtk.Keyboard.KeyLocation;

public class NoOpUnprocessedKeyHandler implements UnprocessedKeyHandler {

	public NoOpUnprocessedKeyHandler() {
	}

	@Override
	public void keyTyped(char character) {
	}

	@Override
	public void keyPressed(int keyCode, KeyLocation keyLocation) {
	}

	@Override
	public void keyReleased(int keyCode, KeyLocation keyLocation) {
	}

}
