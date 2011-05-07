package tutorial;

import java.io.IOException;

import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.ClientList;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Request;
import org.restlet.Response;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.Form;
import org.restlet.data.Method;
import org.restlet.data.Parameter;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;

public class MainClientSSL {

	static final Logger log = LoggerFactory.getLogger(MainClientSSL.class);

	public static void main(String... args) throws Exception {

		log.info("started");

		// Create a new Component.
		Component component = new Component();

		ClientList clients = component.getClients();

		// Define our Restlet HTTPS client.
		Client client = clients.add(Protocol.HTTPS);

		// configure SSL store, key
		Series<Parameter> parameters = client.getContext().getParameters();
		RESTLET.loadClientSSL(parameters);

		//
		component.start();

		// The URI of the resource "list of items".
		Reference root = new Reference("https://localhost:8183");

		Request request = new Request(Method.GET, root);

		// Send an authenticated request using the Basic authentication scheme.
		ChallengeResponse authentication = new ChallengeResponse(
				ChallengeScheme.HTTP_BASIC, "user", "pass");

		request.setChallengeResponse(authentication);

		// Send the request
		log.info("handle?");
		Response response = client.handle(request);
		log.info("handle!");

		// Should be 200
		log.info("{}", response.getStatus());

		log.info("finished");

	}

}
