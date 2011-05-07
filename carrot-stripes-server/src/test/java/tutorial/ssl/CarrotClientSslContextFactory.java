package tutorial.ssl;

/**
 */

import javax.net.ssl.SSLContext;

import org.jsslutils.sslcontext.X509SSLContextFactory;
import org.jsslutils.sslcontext.X509TrustManagerWrapper;
import org.jsslutils.sslcontext.trustmanagers.TrustAllClientsWrappingTrustManager;
import org.jsslutils.sslcontext.trustmanagers.TrustAllServersWrappingTrustManager;
import org.restlet.data.Parameter;
import org.restlet.engine.security.DefaultSslContextFactory;
import org.restlet.engine.security.SslContextFactory;
import org.restlet.util.Series;

public class CarrotClientSslContextFactory extends SslContextFactory {

	private final X509SSLContextFactory factory = new X509SSLContextFactory();

	@Override
	public SSLContext createSslContext() throws Exception {
		synchronized (this) {

			return factory.buildSSLContext();

		}
	}

	@Override
	public void init(Series<Parameter> parameters) {

		X509TrustManagerWrapper trustManagerWrapper = new TrustAllServersWrappingTrustManager.Wrapper();

		factory.setTrustManagerWrapper(trustManagerWrapper);

	}

	@Override
	protected final DefaultSslContextFactory clone()
			throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
