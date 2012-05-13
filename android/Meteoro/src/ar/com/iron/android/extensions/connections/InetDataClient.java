/**
 * 20/06/2011 01:00:10 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.com.iron.android.extensions.connections;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import ar.com.iron.android.extensions.messages.IntentReceptor;
import ar.com.iron.httpclient.ConnectionClient;

/**
 * Esta clase representa un cliente que permite conexiones de datos a través de internet con
 * sistemas externos.<br>
 * A través de esta conexión se pueden solicitar requests HTTP y HTTPS, dependiendo del estado de
 * conectividad de android para realizarlas correctamente
 * 
 * @author D. García
 */
public class InetDataClient {

	private Context context;

	/**
	 * Receptor de intents que permite la comunicación en background
	 */
	private IntentReceptor intentReceptor;

	/**
	 * Indica si este cliente tiene conectividad con el exterior
	 */
	private AtomicBoolean hasConnectivity;

	/**
	 * Cliente para las conexiones de datos
	 */
	private ConnectionClient connectionClient;

	private ConnectivityChangeListener connectivityListener;

	public ConnectivityChangeListener getConnectivityListener() {
		return connectivityListener;
	}

	public void setConnectivityListener(ConnectivityChangeListener connectivityListener) {
		this.connectivityListener = connectivityListener;
	}

	public IntentReceptor getIntentReceptor() {
		if (intentReceptor == null) {
			intentReceptor = new IntentReceptor(context);

		}
		return intentReceptor;
	}

	public static InetDataClient create(Context context) {
		InetDataClient connection = new InetDataClient();
		connection.context = context;
		connection.hasConnectivity = new AtomicBoolean(true);
		connection.connectionClient = new ConnectionClient();
		connection.initListeners();
		return connection;
	}

	/**
	 * Indica si este cliente tiene conectividad con el exterior a través de la plataforma android
	 * 
	 * @return true si se tiene conectividad
	 */
	public boolean hasConnectivity() {
		return hasConnectivity.get();
	}

	/**
	 * Indica si este cliente esta desconectado del exterior debido a la carencia de conectividad en
	 * la plataforma.<br>
	 * Es el valor contrario a {@link #hasConnectivity()}
	 * 
	 * @return true si no se dispone de conexiones salientes
	 */
	public boolean isDisconnected() {
		return !hasConnectivity();
	}

	/**
	 * Inicializa los listeners para conocer el estado actual de la red
	 */
	private void initListeners() {
		getIntentReceptor().registerMessageReceiver(ConnectivityManager.CONNECTIVITY_ACTION, new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				onConnectivityChanged(intent);
			}
		});
	}

	/**
	 * Invocado cuando hay cambios de conectividad en la plataforma
	 * 
	 * @param intent
	 *            Intent con los datos del cambio
	 */
	protected void onConnectivityChanged(Intent intent) {
		boolean connectivityLoosed = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
		boolean hadConnectivity = this.hasConnectivity.get();
		this.hasConnectivity.set(!connectivityLoosed);
		if (connectivityListener != null && hadConnectivity == connectivityLoosed) {
			// Es un cambio de estado, tenemos que notificarlo
			NetworkInfo network = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
			connectivityListener.onConnectivityChanged(!connectivityLoosed, network);
		}
	}

	/**
	 * Libera los recursos utilizados por este cliente antes de ser descartado
	 */
	public void releaseResources() {
		releaseListeners();
	}

	/**
	 * Desregistra los listeners de estado
	 */
	private void releaseListeners() {
		getIntentReceptor().unregisterMessageReceivers();
	}

	/**
	 * Genera un request GET HTTP (o HTTPS), en la URI indicada
	 * 
	 * @param url
	 *            El string del request get
	 * @return La respuesta de del servidor
	 * @throws FailedRequestException
	 *             Si el request falla por algún error. La excepción contendrá el error original
	 *             como causa, o un flag si es un problema de conectividad
	 */
	public HttpResponse request(String url) throws FailedRequestException {
		if (isDisconnected()) {
			throw new FailedRequestException("No existe conectividad para el request", true);
		}
		HttpGet httGet = new HttpGet(url);
		return makeRequest(httGet);
	}

	/**
	 * Genera un request GET HTTP (o HTTPS), en la URI indicada
	 * 
	 * @param httpRequest
	 *            Request saliente
	 * @return La respuesta de del servidor
	 * @throws FailedRequestException
	 *             Si el request falla por algún error. La excepción contendrá el error original
	 *             como causa, o un flag si es un problema de conectividad
	 */
	public HttpResponse makeRequest(HttpUriRequest httpRequest) {
		if (isDisconnected()) {
			throw new FailedRequestException("No existe conectividad para el request", true);
		}
		try {
			HttpResponse response = connectionClient.execute(httpRequest);
			return response;
		} catch (UnsupportedEncodingException e) {
			throw new FailedRequestException("El encoding usado en el request no es soportado", isDisconnected(), e);
		} catch (ClientProtocolException e) {
			throw new FailedRequestException("Se produjo un error en el protocolo http", isDisconnected(), e);
		} catch (UnknownHostException e) {
			throw new FailedRequestException("No se puede llegar al host solicitado", isDisconnected(), e);
		} catch (IOException e) {
			throw new FailedRequestException("Se produjo un error de IO durante el request", isDisconnected(), e);
		}
	}
}
