/**
 * 01/03/2011 00:42:21 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.services.local;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * Esta clase implementa la interconexión entre activity y servicio, pero permite la obtención de un
 * objeto intercomunicador entre ambos. Esto sólo es posible si ambos corren en la misma VM (mismo
 * proceso para android).<br>
 * Esta conexión se encarga de activar el servicio cuando es solicitado así como desconectarlo,
 * permitiendo a través de un {@link LocalServiceConnectionListener} que el activity sea notificado
 * de estos eventos.<br>
 * El servicio debe utilizar como binder un {@link LocalServiceBinder}
 * 
 * @author D. García
 * @param <T>
 *            Tipo de objeto usado como intercomunicador entre servicio y listener
 */
public class LocalServiceConnector<T> implements ServiceConnection {
	/**
	 * Clase que identifica el servicio a ser invocado
	 */
	private Class<? extends LocallyBindableService<T>> serviceClass;

	/**
	 * Listener al que se le derivaran los eventos y abstraerá de la comunicación real
	 */
	private LocalServiceConnectionListener<T> connectionListener;

	/**
	 * El objeto de comunicación actual que representa el enlace efectivo entre ambas puntas
	 */
	private T currentItercomm;

	/**
	 * @see android.content.ServiceConnection#onServiceConnected(android.content.ComponentName,
	 *      android.os.IBinder)
	 */
	public void onServiceConnected(ComponentName className, IBinder binder) {
		if (!(binder instanceof LocalServiceBinder)) {
			throw new RuntimeException("Esta conexión no acepta otra cosa que "
					+ LocalServiceBinder.class.getSimpleName() + ": " + binder);
		}
		if (!className.getClassName().equals(serviceClass.getName())) {
			throw new RuntimeException("Esta conexión sólo puede usarse con el servicio[" + serviceClass + "]: "
					+ className);
		}
		if (connectionListener == null) {
			// Si no hay listener no hacemos nada
			return;
		}
		@SuppressWarnings("unchecked")
		LocalServiceBinder<T> expectedBinder = (LocalServiceBinder<T>) binder;
		currentItercomm = expectedBinder.getIntercommObject();
		if (currentItercomm == null) {
			throw new RuntimeException("El binder del servicio brindó null como intercomunicador. Es un valor invalido");
		}
		connectionListener.onServiceConnection(currentItercomm);
	}

	/**
	 * @see android.content.ServiceConnection#onServiceDisconnected(android.content.ComponentName)
	 */
	public void onServiceDisconnected(ComponentName className) {
		disconnectListener();
	}

	/**
	 * Elimina el objeto intercomm y notifica al listener
	 */
	private void disconnectListener() {
		if (connectionListener != null) {
			connectionListener.onServiceDisconnection(currentItercomm);
		}
		currentItercomm = null;
	}

	/**
	 * Crea esta conexión para ser usada con un servicio local de la clase especificada.<br>
	 * 
	 * @param serviceClass
	 *            La clase con la que se declaró el servicio local
	 * @return La conexión que puede ser establecida y cerrada cuantas veces sea necesario
	 */
	public static <T> LocalServiceConnector<T> create(Class<? extends LocallyBindableService<T>> serviceClass) {
		LocalServiceConnector<T> connection = new LocalServiceConnector<T>();
		connection.serviceClass = serviceClass;
		return connection;
	}

	public LocalServiceConnectionListener<T> getConnectionListener() {
		return connectionListener;
	}

	public void setConnectionListener(LocalServiceConnectionListener<T> connectionListener) {
		this.connectionListener = connectionListener;
	}

	/**
	 * El objeto utilizado como intercomunicador, o null si no hay una conexión efectiva
	 * 
	 * @return El objeto que representa la intercomunicación
	 */
	public T getCurrentItercomm() {
		return currentItercomm;
	}

	/**
	 * Inicia la conexión en forma asíncrona. El listener será invocado al resultar exitosa
	 */
	public void bindToService(Context currentContext) {
		Intent serviceIntent = new Intent(currentContext, serviceClass);
		boolean binded = currentContext.bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
		if (!binded) {
			throw new RuntimeException(
					"No fue posible conectar con el servicio local: Binded = false. El servicio fue declarado?");
		}
	}

	/**
	 * Detiene la conexión con el servicio de esta conexión. Se dejarán de recibir atualizaciones.
	 * El objeto intercomm será descartado.<br>
	 * Este método no tiene efecto si ya se desbindeó del servicio
	 * 
	 * @param currentContext
	 *            El contexto actual
	 */
	public void unbindFromService(Context currentContext) {
		if (currentItercomm != null) {
			disconnectListener();
			currentContext.unbindService(this);
		}
	}

}
