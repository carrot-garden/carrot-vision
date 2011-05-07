package bench.array;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainArray implements Application {

	static final Logger log = LoggerFactory.getLogger(MainArray.class);

	private Window window = null;

	@Override
	public void startup(final Display display, final Map<String, String> properties)
			throws Exception {

		log.info("started");


		window = new Window();

		window.setWidth(500);
		window.setHeight(500);


		window.open(display);

	}

	@Override
	public boolean shutdown(boolean optional) {

		if (window != null) {
			window.close();
		}

		return false;
	}

	@Override
	public void suspend() {
		log.info("suspend");
	}

	@Override
	public void resume() {
		log.info("resume");
	}

}