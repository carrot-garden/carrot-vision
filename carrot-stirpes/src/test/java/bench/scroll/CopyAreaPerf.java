package bench.scroll;

/*
 * Copyright 2002 Sun Microsystems, Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

import java.awt.*;
import java.awt.event.*;

public class CopyAreaPerf extends Canvas implements Runnable {

	public static int SCROLLSIZE = 4;
	public static final int TESTW = 500;
	public static final int TESTH = 500;

	Thread animator;

	Image img;
	int imgw;
	int imgh;

	public void run() {

		Thread me = Thread.currentThread();

		Graphics imgg = null;
		Graphics scrg = getGraphics();

		int mode = 0;
		int count = 0;

		long start = System.currentTimeMillis();

		while (animator == me) {

			int w = getWidth();
			int h = getHeight();

			if (img == null || imgw != w || imgh != h) {

				img = createImage(w, h);

				imgw = w;
				imgh = h;

				if (imgg != null) {
					imgg.dispose();
					imgg = null;
				}
			}

			if (imgg == null) {
				imgg = img.getGraphics();
				imgg.setColor(Color.lightGray);
				imgg.fillRect(0, 0, w, h);
				imgg.setColor(Color.red);
				imgg.fillRect(0, 0, w - 1, 1);
				imgg.setColor(Color.blue);
				imgg.fillRect(w - 1, 0, 1, h - 1);
				imgg.setColor(Color.green);
				imgg.fillRect(1, h - 1, w - 1, 1);
				imgg.setColor(Color.yellow);
				imgg.fillRect(0, 1, 1, h - 1);
			}

			switch (mode) {

			case 0: // scroll down

				if (count == 0) {

					count = (h + SCROLLSIZE - 1) / SCROLLSIZE;

					for (int i = 1; i < SCROLLSIZE; i *= 2) {
						imgg.copyArea(1, 0, w - 2, i, 0, i);
					}

				}

				imgg.copyArea(1, 0, w - 2, h - SCROLLSIZE - 1, 0, SCROLLSIZE);

				break;

			case 1: // scroll left

				if (count == 0) {
					count = (w + SCROLLSIZE - 1) / SCROLLSIZE;
					for (int i = 1; i < SCROLLSIZE; i *= 2) {
						imgg.copyArea(0, 1, i, h - 2, i, 0);
					}
				}

				imgg.copyArea(0, 1, w - SCROLLSIZE - 1, h - 2, SCROLLSIZE, 0);

				break;

			case 2: // scroll up

				if (count == 0) {
					count = (h + SCROLLSIZE - 1) / SCROLLSIZE;
					for (int i = 1; i < SCROLLSIZE; i *= 2) {
						imgg.copyArea(1, h - i, w - 2, i, 0, -i);
					}
				}

				imgg.copyArea(1, SCROLLSIZE + 1, w - 2, h - SCROLLSIZE - 1, 0,
						-SCROLLSIZE);
				break;

			case 3: // scroll right

				if (count == 0) {
					count = (w + SCROLLSIZE - 1) / SCROLLSIZE;
					for (int i = 1; i < SCROLLSIZE; i *= 2) {
						imgg.copyArea(w - i, 1, i, h - 2, -i, 0);
					}
				}

				imgg.copyArea(SCROLLSIZE + 1, 1, w - SCROLLSIZE - 1, h - 2,
						-SCROLLSIZE, 0);
				break;

			default:
				throw new InternalError("mode out of range");
			}

			if (--count == 0) {
				mode = (mode + 1) & 3;
				long end = System.currentTimeMillis();
				System.out.println("sequence took " + (end - start) + " ms");
				start = end;
			}

			scrg.drawImage(img, 0, 0, null);

		}

	}

	public synchronized void start() {
		animator = new Thread(this);
		animator.start();
	}

	public synchronized void stop() {
		animator = null;
	}

	public Dimension getPreferredSize() {
		return new Dimension(TESTW, TESTH);
	}

	public static void main(String argv[]) {
		if (argv.length > 0) {
			SCROLLSIZE = Integer.parseInt(argv[0]);
		}
		Frame f = new Frame();
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		CopyAreaPerf cap = new CopyAreaPerf();
		f.add(cap);
		f.pack();
		f.show();
		cap.start();
	}
}
