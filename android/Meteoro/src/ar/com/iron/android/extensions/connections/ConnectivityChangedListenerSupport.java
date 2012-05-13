/**
 * 20/06/2011 01:53:51 Copyright (C) 2011 Darío L. García
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

import android.net.NetworkInfo;

/**
 * Esta clase implementa el listener de conectividad dividiendo el evento de dos invocaciones
 * diferentes al conectar o desconectar
 * 
 * @author D. García
 */
public abstract class ConnectivityChangedListenerSupport implements ConnectivityChangeListener {

	/**
	 * @see ar.com.iron.android.extensions.connections.ConnectivityChangeListener#onConnectivityChanged(boolean,
	 *      android.net.NetworkInfo)
	 */
	public void onConnectivityChanged(boolean connected, NetworkInfo networkInfo) {
		if (connected) {
			onConnectivityGained(networkInfo);
		} else {
			onConnectivityLoosed(networkInfo);
		}
	}

	/**
	 * Invocado cuando se pierde la conectividad con el exterior
	 * 
	 * @param networkInfo
	 *            Red desconectada
	 */
	public abstract void onConnectivityLoosed(NetworkInfo networkInfo);

	/**
	 * Invocado cuando la conectividad es retomada
	 * 
	 * @param networkInfo
	 *            Red conectada
	 */
	public abstract void onConnectivityGained(NetworkInfo networkInfo);
}
