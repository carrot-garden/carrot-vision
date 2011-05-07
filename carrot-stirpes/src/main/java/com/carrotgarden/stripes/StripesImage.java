package com.carrotgarden.stripes;

import static java.lang.Math.*;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.WritableRaster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StripesImage {

	private static Logger log = LoggerFactory.getLogger(StripesImage.class);

	public static class Builder {

		private int width = 200;
		private int height = 200;
		private double angle = 0;
		private int period = 100;
		private Paint paint;

		public Builder width(int width) {
			this.width = width;
			return this;
		}

		public Builder height(int height) {
			this.height = height;
			return this;
		}

		public Builder angle(double angle) {
			this.angle = angle;
			return this;
		}

		public Builder period(int period) {
			this.period = period;
			return this;
		}

		public Builder paint(Paint paint) {
			this.paint = paint;
			return this;
		}

		public StripesImage build() {
			return new StripesImage(this);
		}

	}

	private final int width;
	private final int height;
	private final Direct direct;
	private final int period;

	private final Paint paint;

	private final BufferedImage backImage;

	private int offsetHor;
	private int offsetVer;

	//

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getPeriod() {
		return period;
	}

	public Direct getDirect() {
		return direct;
	}

	public Paint getPaint() {
		return paint;
	}

	public int getOffsetHorizontal() {
		return offsetHor;
	}

	public int getOffsetVertical() {
		return offsetVer;
	}

	//

	/** makes comesurate adjustments */
	public StripesImage(final Builder builder) {

		direct = Direct.fromAngle(builder.angle);

		period = makeEven(builder.period);

		width = makeComeasured(builder.width, period, direct.cos);
		height = makeComeasured(builder.height, period, direct.sin);

		if (builder.paint == null) {
			paint = makePaint();
		} else {
			paint = builder.paint;
		}

		backImage = makeBackgroundImage(width, height);

		if (!isSupported(backImage)) {
			throw new UnsupportedOperationException("wrong image format");
		}

		paintImage(backImage);

	}

	//

	private void paintImage(final BufferedImage image) {

		final Graphics2D desk = image.createGraphics();

		desk.setPaint(paint);

		desk.fillRect(0, 0, image.getWidth(), image.getHeight());

	}

	private int makeEven(final int value) {
		return value - value % 2;
	}

	private int makeComeasured(int length, final int period,
			final double projRatio) {

		if (abs(projRatio) > 0.001 && period > 0) {

			int projPeriod = (int) (period * projRatio);

			int count = length / projPeriod;

			count = count == 0 ? 1 : count;

			length = count * projPeriod;

		}

		length = makeEven(length);

		// log.debug("length={}", length);

		return length;

	}

	static BufferedImage makeBackgroundImage(final int width, final int height) {

		final GraphicsEnvironment local = GraphicsEnvironment
				.getLocalGraphicsEnvironment();

		final GraphicsDevice screen = local.getDefaultScreenDevice();

		final GraphicsConfiguration configuration = screen
				.getDefaultConfiguration();

		final BufferedImage image = configuration.createCompatibleImage(
				2 * width, 2 * height);

		// BufferedImage image = new BufferedImage(width, height,
		// BufferedImage.TYPE_INT_ARGB);

		return image;

	}

	//

	static boolean isSupported(final BufferedImage image) {

		final int type = image.getType();
		// log.debug("image type={}", type);

		switch (type) {
		case BufferedImage.TYPE_INT_ARGB:
		case BufferedImage.TYPE_INT_ARGB_PRE:
		case BufferedImage.TYPE_INT_RGB:
		case BufferedImage.TYPE_INT_BGR:
			break;
		default:
			log.error("wrong image type={}", type);
			return false;
		}

		return true;

	}

	//

	private Paint makePaint() {

		final int x1 = 0, y1 = 0;

		final int half = period / 2;

		final int x2 = (int) (direct.cos * half);

		final int y2 = (int) (direct.sin * half);

		return new GradientPaint(x1, y1, Color.black, x2, y2, Color.white, true);

	}

	public void resetOffset() {
		offsetHor = 0;
		offsetVer = 0;
	}

	//

	public void rollHorizont(final int offset) {

		offsetHor += offset;
		offsetHor %= width;

	}

	public void roll(final int offset) {

		final int by_X = direct.signX * offset;
		final int by_Y = direct.signY * offset;

		rollHorizont(by_X);
		rollVertical(by_Y);

	}

	DataBufferInt getDataBuffer() {

		final WritableRaster raster = backImage.getRaster();

		final DataBufferInt buffer = (DataBufferInt) raster.getDataBuffer();

		return buffer;

	}

	public void rollVertical(final int offset) {

		offsetVer += offset;

		offsetVer %= height;

	}

	//

	public void drawTo(final Graphics2D graphics, final int x, final int y) {

		final int row = (width - offsetHor) % width;
		final int col = (height - offsetVer) % height;

		// log.debug("row={} col={}", row, col);
		// log.debug("offsetHor={} offsetVer={}", offsetHor, offsetVer);
		// log.debug("width={} height={}", width, height);
		// log.debug("backImage.w={} backImage.h={}", backImage.getWidth(),
		// backImage.getHeight());

		final BufferedImage frontImage = backImage.getSubimage(row, col, width,
				height);

		final boolean isReady = graphics.drawImage(frontImage, x, y, null);

		if (!isReady) {
			throw new RuntimeException("FIXME");
		}

	}

}
