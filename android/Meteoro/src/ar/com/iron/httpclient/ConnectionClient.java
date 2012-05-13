package ar.com.iron.httpclient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;

/**
 * Esta clase representa el cliente que realiza comunicación http para sincronizar mensajes con el
 * servidor.<br>
 * Es una implementación basada en la clase {@link DefaultHttpClient} provista por Android y realiza
 * la configuración de bajo nivel relacionado al cliente.<br>
 * Este cliente permite realizar conexiones https con dominios que no se poseen un certificado
 * confiable.
 * 
 * @author Maximiliano Vázquez
 * 
 */
public class ConnectionClient extends DefaultHttpClient {

	/**
	 * Puerto normalmente usado para HTTPS
	 */
	private static final int HTTPS_PORT = 443;

	/**
	 * Puerto común de los servidores http
	 */
	public static final int INTERNET_PORT = 80;

	/**
	 * Determina el tiempo en milisegundos antes de cancelar una conexión.
	 */
	private static final int CONNECTION_TIMEOUT_MS = 10000;

	/**
	 * Determina el encoding que se utilizará en los parámetros enviados.
	 */
	private static final String ENCODING = HTTP.UTF_8;

	/**
	 * Lista de pares de valores que representan los parámetros a enviar.
	 */
	private ArrayList<NameValuePair> nvps = new ArrayList<NameValuePair>();

	/**
	 * Constructor para una conexión estándar de tipo http
	 * 
	 * @throws ConnectionClientException
	 *             Si no se puede configurar correctamente la conexión
	 */
	public ConnectionClient() throws ConnectionClientException {
		// Permitimos cualquier certificado en HTTPS
		registerTrustAllScheme(HTTPS_PORT);
		HttpConnectionParams.setConnectionTimeout(this.getParams(), CONNECTION_TIMEOUT_MS);
	}

	/**
	 * Agrega un parámetro a enviar en la conexion
	 */
	public void addParameter(String paramName, String paramValue) {
		this.nvps.add(new BasicNameValuePair(paramName, paramValue));
	}

	/**
	 * Ejecuta un POST al destino especifado.
	 * 
	 * @throws BadStatusCodeException
	 *             Si el servidor no respondió con un OK
	 * @throws ConnectionClientException
	 *             Si se produjo un error de protocolo en el request
	 */
	public HttpResponse executePostMethod(String destination) throws BadStatusCodeException, ConnectionClientException {
		HttpPost httpost = new HttpPost(destination);
		try {
			httpost.setEntity(new UrlEncodedFormEntity(this.nvps, ENCODING));
		} catch (UnsupportedEncodingException e) {
			throw new ConnectionClientException("Falló seteo de encoding del request POST", e);
		}
		return executeHttpCommand(httpost);
	}

	/**
	 * Ejecuta el comando indicado devolviendo su respuesta si el resultado fue OK
	 * 
	 * @param httpCommand
	 *            Comando http a ejecutar
	 * @return La respuesta positiva
	 * @throws BadStatusCodeException
	 *             Si el servidor no respondió con un OK
	 * @throws ConnectionClientException
	 *             Si existe un error en la comunicación
	 */
	public HttpResponse executeHttpCommand(HttpUriRequest httpCommand) throws BadStatusCodeException,
			ConnectionClientException {
		try {
			HttpResponse response = this.execute(httpCommand);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new BadStatusCodeException(statusCode, response);
			}
			return response;
		} catch (UnsupportedEncodingException e) {
			throw new ConnectionClientException(e);
		} catch (ClientProtocolException e) {
			throw new ConnectionClientException(e);
		} catch (UnknownHostException e) {
			throw new ConnectionClientException("No se pudo determinar la IP del host pasado", e);
		} catch (IOException e) {
			throw new ConnectionClientException(e);
		}
	}

	/**
	 * Ejecuta un GET al destino especifado.
	 * 
	 * @throws BadStatusCodeException
	 *             Si el servidor no respondió con un OK
	 * @throws ConnectionClientException
	 *             Si se produjo un error de protocolo en el request
	 */
	public HttpResponse executeGetMethod(String destination) throws BadStatusCodeException, ConnectionClientException {
		HttpGet httGet = new HttpGet(destination);
		return executeHttpCommand(httGet);
	}

	/**
	 * Configura el cliente para permitir conexiones no confiables.
	 */
	public void registerTrustAllScheme(int port) throws ConnectionClientException {
		try {
			TrustAllSSLSocketFactory socketFactory = new TrustAllSSLSocketFactory();
			Scheme sch = new Scheme("https", socketFactory, port);
			this.getConnectionManager().getSchemeRegistry().register(sch);
		} catch (KeyManagementException e) {
			throw new ConnectionClientException(e);
		} catch (NoSuchAlgorithmException e) {
			throw new ConnectionClientException(e);
		} catch (KeyStoreException e) {
			throw new ConnectionClientException(e);
		} catch (UnrecoverableKeyException e) {
			throw new ConnectionClientException(e);
		}

	}

	/**
	 * Ejecutado para eliminar el estado anterior de los parámetros enviados al servidor
	 */
	public void nuevoMensaje() {
		nvps = new ArrayList<NameValuePair>();
	}

}