package tutorial;

import java.io.IOException;

import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainServer extends ServerResource {

	static final Logger log = LoggerFactory.getLogger(MainServer.class);

	public static void main(String... args) throws Exception {

		log.info("started");

		// Create the HTTP server and listen on port 8182
		new Server(Protocol.HTTP, 8182, MainServer.class).start();

		log.info("finished");

	}

	@Get
	public String toString() {
		return "hello, world";
	}

}