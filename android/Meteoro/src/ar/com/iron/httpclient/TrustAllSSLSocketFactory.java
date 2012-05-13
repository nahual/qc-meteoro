package ar.com.iron.httpclient;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;

/**
 * Esta clase permite que el cliente realize conexiones https con dominios que no poseen un
 * certificado autenticado por una entidad registrante.<br>
 * Se utiliza en conjunto con {@link ConnectionClient}.
 * 
 * @author Maximiliano Vazquez
 * 
 */
class TrustAllSSLSocketFactory extends SSLSocketFactory {

	private final javax.net.ssl.SSLSocketFactory factory;

	public TrustAllSSLSocketFactory() throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException,
			UnrecoverableKeyException {
		super(null);

		SSLContext sslcontext = SSLContext.getInstance("TLS");
		sslcontext.init(null, new TrustManager[] { new TrustAllManager() }, null);
		this.factory = sslcontext.getSocketFactory();
		this.setHostnameVerifier(new AllowAllHostnameVerifier());
	}

	public static SocketFactory getDefault() throws Exception {
		return new TrustAllSSLSocketFactory();
	}

	@Override
	public Socket createSocket() throws IOException {
		return this.factory.createSocket();
	}

	@Override
	public Socket createSocket(Socket socket, String s, int i, boolean flag) throws IOException {
		return this.factory.createSocket(socket, s, i, flag);
	}

	public Socket createSocket(InetAddress inaddr, int i, InetAddress inaddr1, int j) throws IOException {
		return this.factory.createSocket(inaddr, i, inaddr1, j);
	}

	public Socket createSocket(InetAddress inaddr, int i) throws IOException {
		return this.factory.createSocket(inaddr, i);
	}

	public Socket createSocket(String s, int i, InetAddress inaddr, int j) throws IOException {
		return this.factory.createSocket(s, i, inaddr, j);
	}

	public Socket createSocket(String s, int i) throws IOException {
		return this.factory.createSocket(s, i);
	}

	public String[] getDefaultCipherSuites() {
		return this.factory.getDefaultCipherSuites();
	}

	public String[] getSupportedCipherSuites() {
		return this.factory.getSupportedCipherSuites();
	}
}