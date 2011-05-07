package com.carrotgarden.stripes;

import static org.junit.Assert.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestStripesImage {

	private static Logger log = LoggerFactory.getLogger(TestStripesImage.class);

	// @Test
	public void testRollVertical() {

		StripesImage image = new StripesImage.Builder()//
				.width(5).height(5).period(5).build();

		assertEquals(image.getWidth(), 4);
		assertEquals(image.getHeight(), 4);
		assertEquals(image.getPeriod(), 4);

		DataBufferInt buffer = image.getDataBuffer();

		int[] data = buffer.getData();
		log.info("{}", data);

		//

		data[0] = -1;
		data[1] = -2;
		data[2] = -3;
		data[3] = -4;

		data[4] = -10;
		data[5] = -20;
		data[6] = -30;
		data[7] = -40;

		data[8] = -100;
		data[9] = -200;
		data[10] = -300;
		data[11] = -400;

		data[12] = -1000;
		data[13] = -2000;
		data[14] = -3000;
		data[15] = -4000;

		log.info("{}", data);

		//

	}

	@Test
		public void testRollHorizont() {
	
			BufferedImage drawImage = StripesImage.makeBackgroundImage(5, 5);
	
			DataBufferInt drawBuffer = (DataBufferInt) drawImage.getRaster()
					.getDataBuffer();
	
			int[] drawData = drawBuffer.getData();
	
			assertEquals(drawData.length, 10 * 10);
	
			//
	
			StripesImage testImage = new StripesImage.Builder()//
					.width(5).height(5).period(5).build();
	
			assertEquals(testImage.getWidth(), 4);
			assertEquals(testImage.getHeight(), 4);
			assertEquals(testImage.getPeriod(), 4);
	
			DataBufferInt testBuffer = testImage.getDataBuffer();
	
			int[] testData = testBuffer.getData();
			for (int k = 0; k < testData.length; k++) {
				testData[k] = k;
			}
			log.info("testData\n{}", testData);
	
			//
	
			Graphics2D drawGraph = drawImage.createGraphics();
	
			testImage.rollHorizont(+2);
	
			testImage.drawTo(drawGraph, 2, 2);
	
			log.info("drawData\n{}", drawData);
	
		}

	@Test
	public void testArray() {

		final int[] array1 = new int[] { 1, 2, 3 };
		log.info("{}", array1);

		final int length = array1.length;

		System.arraycopy(array1, 0, array1, 1, length - 1);

		assertTrue(array1[0] == 1);
		assertTrue(array1[1] == 1);
		assertTrue(array1[2] == 2);

		log.info("{}", array1);

		//

		final int[] array2 = new int[] { 1, 2, 3 };
		log.info("{}", array2);

		System.arraycopy(array2, 1, array2, 0, length - 1);

		assertTrue(array2[0] == 2);
		assertTrue(array2[1] == 3);
		assertTrue(array2[2] == 3);

		log.info("{}", array2);

	}

	@Test
	public void testShift() {

		final int size = 7;

		int shift;

		final int shiftPlus = +11;
		final int shiftMinus = -13;

		shift = shiftPlus % size;
		assertTrue(shift == 4);
		log.info("shift={} shiftPlus={}", shift, shiftPlus);

		shift = shiftMinus % size;
		assertTrue(shift == -6);
		log.info("shift={} shiftMinus={}", shift, shiftMinus);

	}

}
