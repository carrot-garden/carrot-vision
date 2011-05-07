package tutorial;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;

import org.jsslutils.sslcontext.PKIXSSLContextFactory;
import org.restlet.Server;
import org.restlet.data.Protocol;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.Get;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.restlet.Application;
import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.Server;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.util.ClientList;
import org.restlet.util.Series;
import org.restlet.util.ServerList;

public class MainServerSSL extends ServerResource {

	static final Logger log = LoggerFactory.getLogger(MainServerSSL.class);

	public static void main(String... args) throws Exception {

		log.info("started");

		// Create a new Component.
		Component component = new Component();

		ClientList clients = component.getClients();
		ServerList servers = component.getServers();

		Client client = clients.add(Protocol.FILE);

		// Add a new HTTPS server listening on port 8183
		Server server = servers.add(Protocol.HTTPS, 8183);

		// configure SSL store, key
		Series<Parameter> parameters = server.getContext().getParameters();
		RESTLET.loadServerSSL(parameters);

		// Create an application
		Restlet application = new SampleApplication();

		// Attach the sample application.
		component.getDefaultHost().attachDefault(application);

		for (Client c : clients) {
			log.info("client\n{}", c);
		}
		for (Server s : servers) {
			log.info("server\n{}", s);
		}

		// Start the component.
		component.start();

		log.info("finished");

	}

	@Get
	public String toString() {
		return "hello, world";
	}

}