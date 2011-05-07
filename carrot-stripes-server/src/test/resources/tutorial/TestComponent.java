package tutorial;

import org.restlet.Client;
import org.restlet.Component;
import org.restlet.Guard;
import org.restlet.Restlet;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Request;
import org.restlet.data.Response;
import org.restlet.resource.StringRepresentation;

/**
 * Simple application code that illustrates the usage of HTTP_BASIC
 * authentication.
 */
public class TestComponent extends Component {
	public static void main(String[] args) throws Exception {
		// Create the component that listens to the 8182 port.
		Component component = new TestComponent();
		component.getServers().add(Protocol.HTTP, 8182);

		// Restlet that simply replies to requests with an "hello, world" text
		// message
		Restlet restlet = new Restlet() {
			@Override
			public void handle(Request request, Response response) {
				response.setEntity(new StringRepresentation("hello, world",
						MediaType.TEXT_PLAIN));
			}
		};

		// Guard the restlet with BASIC authentication.
		// Use the basic guard
		Guard guard = new Guard(component.getContext().createChildContext(),
				ChallengeScheme.HTTP_BASIC, "Sample application.");
		// Load a single static login/secret pair.
		guard.getSecrets().put("login", "secret".toCharArray());

		guard.setNext(restlet);
		component.getDefaultHost().attachDefault(guard);

		// Start the component
		component.start();
		// Launch the test client
		testClient();
		// Stop the component
		component.stop();
	}

	/**
	 * Client test code.
	 */
	public static void testClient() {
		Reference reference = new Reference("http://localhost:8182/");
		Client client = new Client(Protocol.HTTP);
		Request request = new Request(Method.GET, reference);

		// Send an authenticated request using the Basic authentication
		// scheme.
		ChallengeResponse authentication = new ChallengeResponse(
				ChallengeScheme.HTTP_BASIC, "login", "secret");
		request.setChallengeResponse(authentication);

		// Send the request
		Response response = client.handle(request);
		// Should be 200
		System.out.println(response.getStatus());
	}
}
