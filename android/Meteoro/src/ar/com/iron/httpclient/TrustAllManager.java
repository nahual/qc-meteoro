package ar.com.iron.httpclient;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

/**
 * Implementacion de un {@link X509TrustManager} que no realiza ninguna validacion.<br>
 * Se utiliza en conjunto con {@link ConnectionClient}.
 * 
 * @author Maximiliano Vazquez
 * 
 */
class TrustAllManager implements X509TrustManager {

	public void checkClientTrusted(X509Certificate[] cert, String authType) throws CertificateException {
	}

	public void checkServerTrusted(X509Certificate[] cert, String authType) throws CertificateException {
	}

	public X509Certificate[] getAcceptedIssuers() {
		return null;
	}
}