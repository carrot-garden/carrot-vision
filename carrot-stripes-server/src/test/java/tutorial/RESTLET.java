package tutorial;

import java.io.FileReader;
import java.util.Properties;

import org.restlet.data.Parameter;
import org.restlet.ext.ssl.PkixSslContextFactory;
import org.restlet.util.Series;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import tutorial.ssl.CarrotClientSslContextFactory;

public class RESTLET {

	public static final String PROP_CLIENT = "client";
	public static final String PROP_SERVER = "server";

	public static final String PROP_sslContextFactory = "sslContextFactory";

	public static final String PROP_truststorePath = "truststorePath";
	public static final String PROP_truststorePassword = "truststorePassword";

	public static final String PROP_keystorePath = "keystorePath";
	public static final String PROP_keystorePassword = "keystorePassword";

	public static final String PROP_keyPassword = "keyPassword";

	public static final String USER_HOME = System.getProperty("user.home");
	public static final String USER_SSL_FOLDER = USER_HOME + "/.ssl";
	public static final String USER_SSL_PROPS_FILE = USER_SSL_FOLDER
			+ "/carrotgarden-java-keys.properties";

	static final Logger log = LoggerFactory.getLogger(MainServerSSL.class);

	static {
		log.debug("USER_HOME={}", USER_HOME);
	}

	public static void loadClientSSL(Series<Parameter> params) throws Exception {
		loadSSL(params, PROP_CLIENT);
	}

	public static void loadServerSSL(Series<Parameter> params) throws Exception {
		loadSSL(params, PROP_SERVER);
	}

	public static void loadSSL(Series<Parameter> params, String type)
			throws Exception {

		Properties props = new Properties();
		props.load(new FileReader(USER_SSL_PROPS_FILE));

		//

		type = type + ".";

		String contextFactory = PkixSslContextFactory.class.getName();

		//

		String path1 = props.getProperty(type + PROP_truststorePath);
		String truststorePath = //
		"".equals(path1) ? null : USER_SSL_FOLDER + "/" + path1;
		log.debug("truststorePath {}", truststorePath);

		String truststorePassword = props.getProperty(//
				type + PROP_truststorePassword);

		//

		String path2 = props.getProperty(type + PROP_keystorePath);
		String keystorePath = //
		"".equals(path2) ? null : USER_SSL_FOLDER + "/" + path2;
		log.debug("keystorePath {}", keystorePath);

		String keystorePassword = props.getProperty(//
				type + PROP_keystorePassword);

		//

		params.add(PROP_sslContextFactory, contextFactory);

		params.add(PROP_truststorePath, truststorePath);
		params.add(PROP_truststorePassword, truststorePassword);

		params.add(PROP_keystorePath, keystorePath);
		params.add(PROP_keystorePassword, keystorePassword);
		params.add(PROP_keyPassword, keystorePassword);

	}
}
