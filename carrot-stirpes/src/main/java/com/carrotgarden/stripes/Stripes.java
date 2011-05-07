package com.carrotgarden.stripes;

import java.awt.Paint;
import java.awt.Shape;
import java.net.URL;

import org.apache.pivot.beans.Bindable;
import org.apache.pivot.util.ListenerList;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.ActivityIndicator;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Skin;
import org.apache.pivot.wtk.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.pivot.beans.BXML;
import org.apache.pivot.collections.Map;

public class Stripes extends Component implements Bindable {

	static final Logger log = LoggerFactory.getLogger(Stripes.class);

	static {

		Theme theme = Theme.getTheme();
		theme.set(Stripes.class, StripesSkin.class);

	}

	public static enum Event {
		PAINT, CLIP, CYCLE, SIZE, PERIOD, ANGLE, ENABLE, DISABLE, STEP,
	}

	static class StripesListenerList extends ListenerList<StripesListener>
			implements StripesListener {
		@Override
		public void onEvent(Event event) {
			for (StripesListener listener : this) {
				listener.onEvent(event);
			}
		}
	}

	private final StripesListenerList listenerList = new StripesListenerList();

	public StripesListenerList getListenerList() {
		return listenerList;
	}

	public Stripes() {

		installSkin(Stripes.class);

		setSize(100, 100);

		setActive(true);

	}

	@BXML
	/** will used default paint if null */
	private Paint paint;

	public Paint getPaint() {
		return paint;
	}

	public void setPaint(Paint paint) {
		this.paint = paint;
		listenerList.onEvent(Event.PAINT);
	}

	@BXML
	/** stripes external clipping, if any */
	private Shape clip;

	public Shape getClip() {
		return clip;
	}

	public void setClip(final Shape clip) {
		if (clip == null) {
			throw new IllegalArgumentException("clip==null");
		}
		this.clip = clip;
		listenerList.onEvent(Event.CLIP);
	}

	@BXML
	/** updates time period in millis*/
	private int cycle = 40;

	public int getCycle() {
		return cycle;
	}

	public void setCycle(int cycle) {
		if (cycle <= 0) {
			throw new IllegalArgumentException("cycle <= 0");
		}
		this.cycle = cycle;
		listenerList.onEvent(Event.CYCLE);
	}

	@BXML
	/** space pattern period in pixels */
	private int period = 100;

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		if (period <= 0) {
			throw new IllegalArgumentException("period <= 0");
		}
		this.period = period;
		listenerList.onEvent(Event.PERIOD);
	}

	@BXML
	/** cycle space step in pixels */
	private int step = 1;

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
		listenerList.onEvent(Event.STEP);
	}

	@BXML
	/** space pattern period in pixels */
	private double angle = 0.0;

	public double getRadians() {
		return angle;
	}

	public void setRadians(double radians) {
		this.angle = radians;
		listenerList.onEvent(Event.ANGLE);
	}

	private static final double PI2 = Math.PI * 2;

	public int getDegrees() {
		return (int) (360 * getRadians() / PI2);
	}

	public void setDegrees(int degrees) {
		setRadians(PI2 * degrees / 360);
	}

	@BXML
	/** space pattern period in pixels */
	private boolean isActive;

	public boolean getActive() {
		return isActive;
	}

	public void setActive(final boolean on) {

		if (isActive == on) {
			return;
		}

		isActive = on;

		if (on) {
			listenerList.onEvent(Event.ENABLE);
		} else {
			listenerList.onEvent(Event.DISABLE);
		}

	}

	//

	public void setSize(int width, int height) {
		super.setSize(width, height);
		listenerList.onEvent(Event.SIZE);
	}

	@Override
	public void initialize(Map<String, Object> namespace, URL location,
			Resources resources) {
	}

}
