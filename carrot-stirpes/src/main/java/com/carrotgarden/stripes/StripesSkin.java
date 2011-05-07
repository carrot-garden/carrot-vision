package com.carrotgarden.stripes;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import org.apache.pivot.wtk.ActivityIndicator;
import org.apache.pivot.wtk.ApplicationContext;
import org.apache.pivot.wtk.ApplicationContext.ScheduledCallback;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.skin.ComponentSkin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.carrotgarden.stripes.Stripes.Event;

public class StripesSkin extends ComponentSkin implements StripesListener {

	private static final Logger log = LoggerFactory
			.getLogger(StripesSkin.class);

	private Stripes stripes;

	private StripesImage stripesImage;

	private ApplicationContext.ScheduledCallback updateCallback;

	@Override
	public void install(final Component component) {

		super.install(component);

		stripes = (Stripes) component;

		stripes.getListenerList().add(this);

	}

	private void buildStripesImage() {

		stripesImage = new StripesImage.Builder()
		//
				.width(getWidth()).height(getHeight()) //
				.period(stripes.getPeriod()) //
				.angle(stripes.getRadians()) //
				.paint(stripes.getPaint()) //
				.build();

	}

	@Override
	public void layout() {
	}

	@Override
	public int getPreferredWidth(int height) {
		return 200;
	}

	@Override
	public int getPreferredHeight(int width) {
		return 200;
	}

	@Override
	public void paint(final Graphics2D graphics) {

		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		graphics.setClip(stripes.getClip());

		if (stripesImage == null) {
			buildStripesImage();
		}

		stripesImage.drawTo(graphics, 0, 0);

	}

	@Override
	public void onEvent(final Event event) {

		switch (event) {

		case CLIP:
			// noop
			break;
		case PERIOD:
			buildStripesImage();
			break;
		case PAINT:
			buildStripesImage();
			break;
		case SIZE:
			buildStripesImage();
			break;
		case ANGLE:
			buildStripesImage();
			break;
		case ENABLE:
			enable();
			break;
		case DISABLE:
			disable();
			break;
		case CYCLE:
			disable();
			enable();
			break;
		case STEP:
			// noop
			break;
		default:
			log.error("wrong event={}", event);
			break;
		}

	}

	private void enable() {
		updateCallback = ApplicationContext.scheduleRecurringCallback(
				repaintTask, stripes.getCycle());
	}

	private void disable() {
		if (updateCallback != null) {
			updateCallback.cancel();
			updateCallback = null;
		}
	}

	private final Runnable repaintTask = new Runnable() {
		@Override
		public void run() {

			stripesImage.roll(stripes.getStep());

			repaintComponent();

		}
	};

}
