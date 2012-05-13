/**
 * 20/06/2011 01:51:36 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz declara los métodos a definir para tratar los cambios de conectividad
 * 
 * @author D. García
 */
public interface ConnectivityChangeListener {

	/**
	 * Invocado al producirse un cambio de conectividad (se ganó o se perdió)
	 * 
	 * @param connected
	 *            true si se conectó, false si se desconectó
	 * @param network
	 *            red que se conectó o desconectó
	 */
	public void onConnectivityChanged(boolean connected, NetworkInfo network);
}
