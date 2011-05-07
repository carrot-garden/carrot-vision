package tutorial;

import org.restlet.Application;
import org.restlet.Restlet;
import org.restlet.data.ChallengeScheme;
import org.restlet.resource.Directory;
import org.restlet.security.ChallengeAuthenticator;
import org.restlet.security.MapVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleApplication extends Application {

	static final Logger log = LoggerFactory.getLogger(SampleApplication.class);

	@Override
	public Restlet createInboundRoot() {

		log.info("root?");

		// Create a simple password verifier
		MapVerifier verifier = new MapVerifier();
		verifier.getLocalSecrets().put("user", "pass".toCharArray());

		// Create a Guard
		ChallengeAuthenticator authenticator = new ChallengeAuthenticator(
				getContext(), ChallengeScheme.HTTP_BASIC, "ROOT");

		authenticator.setVerifier(verifier);

		// Create a Directory able to return a deep hierarchy of files
		Directory directory = new Directory(getContext(), "file://"
				+ RESTLET.USER_HOME);
		directory.setListingAllowed(true);

		authenticator.setNext(directory);

		return authenticator;

	}

}
