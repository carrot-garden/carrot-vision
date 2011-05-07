package tutorial;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainClient {

	static final Logger log = LoggerFactory.getLogger(MainClient.class);

	public static void main(String... args) throws Exception {

		log.info("started");

		ClientResource client = new ClientResource("http://www.carrotgarden.com");
		
		Representation rep1 = client.get();
		log.info("{}", rep1.getText());

		Representation rep2 = client.get();
		log.info("{}", rep2.getText());

		log.info("finished");

	}

}