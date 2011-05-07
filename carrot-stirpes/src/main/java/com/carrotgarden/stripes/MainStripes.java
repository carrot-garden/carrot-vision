package com.carrotgarden.stripes;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.Window;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainStripes implements Application {

	static {

		/** http://download.oracle.com/javase/1.5.0/docs/guide/2d/flags.html */

		// -Dsun.java2d.opengl=True
		System.setProperty("sun.java2d.opengl", "True");

		// -Dsun.awt.noerasebackground=true
		System.setProperty("sun.awt.noerasebackground", "true");

		// -Dsun.java2d.pmoffscreen=false
		System.setProperty("sun.java2d.pmoffscreen", "true");

		// org.apache.pivot.wtk.disablevolatilebuffer
		System.setProperty("org.apache.pivot.wtk.disablevolatilebuffer",
				"true");

		// org.apache.pivot.wtk.debugpaint
		// System.setProperty("org.apache.pivot.wtk.debugpaint", "true");

	}

	static final Logger log = LoggerFactory.getLogger(MainStripes.class);

	private Window window = null;

	@Override
	public void startup(final Display display,
			final Map<String, String> properties) throws Exception {

		log.info("started; 1");

		BXMLSerializer bxml = new BXMLSerializer();

		window = BXML.read(bxml, this);

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