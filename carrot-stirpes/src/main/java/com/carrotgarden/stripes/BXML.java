package com.carrotgarden.stripes;

import org.apache.pivot.beans.BXMLSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BXML {

	private static final Logger log = LoggerFactory.getLogger(BXML.class);

	@SuppressWarnings("unchecked")
	public static <T> T read(final BXMLSerializer bxml, final Object instance)
			throws Exception {

		final Class<?> klaz = instance.getClass();

		final String klazResource = klaz.getSimpleName() + ".bxml";

		// log.debug("klazResource={}",klazResource);

		return (T) bxml.readObject(klaz, klazResource);

	}

}
