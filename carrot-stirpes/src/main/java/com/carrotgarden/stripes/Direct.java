package com.carrotgarden.stripes;

import static java.lang.Math.*;

public enum Direct {

	DEFAULT(PI * 0.00, 1, 1), //

	ANGLE_000(PI * 0.00, +1, +0), //
	ANGLE_045(PI * 0.25, +1, +1), //
	ANGLE_090(PI * 0.50, +0, +1), //
	ANGLE_135(PI * 0.75, -1, +1), //
	ANGLE_180(PI * 1.00, -1, +0), //
	ANGLE_225(PI * 1.25, -1, -1), //
	ANGLE_270(PI * 1.50, +0, -1), //
	ANGLE_315(PI * 1.75, +1, -1), //

	;

	public final double angle;
	public final double sin;
	public final double cos;

	public final int signX;
	public final int signY;

	Direct(final double angle, final int signX, final int signY) {

		this.angle = angle;

		this.sin = sin(angle);
		this.cos = cos(angle);

		this.signX = signX;
		this.signY = signY;

	}

	private static final Direct[] ENUM_VALS = values();

	private static final double PI2 = 2 * PI;

	private static final double ANGLE_ERR = PI / 16;

	public static Direct fromAngle(final double angle) {

		double normal = angle % PI2;
		normal = normal > 0 ? normal : PI2 - normal;

		for (final Direct direct : ENUM_VALS) {
			if (abs(normal - direct.angle) < ANGLE_ERR) {
				return direct;
			}
		}

		return DEFAULT;

	}

}
