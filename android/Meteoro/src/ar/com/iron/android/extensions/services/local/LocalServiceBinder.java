/**
 * 28/02/2011 23:52:55 Copyright (C) 2011 Darío L. García
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

import android.os.Binder;

/**
 * Binder para ser usado localmente (en misma VM) entre activities y servicios.<br>
 * Esta implementación permite que el activity acceda a métodos publicados por el servicio
 * directamente a través de un objeto intercomunicador.<br>
 * De esta manera si se sabe que el servicio corre local, no es necesario el overhead de AIDL.<br>
 * <br>
 * El objeto intercomunicador se encarga de manejar la comunicación inter-thread si es necesario
 * 
 * @author D. García
 */
public class LocalServiceBinder<T> extends Binder {

	/**
	 * Objeto utilizado para la intercomunicación entre activities y servicio
	 */
	private T intercommObject;

	/**
	 * Devuelve el objeto que el servicio ofrece como intercomunicador con los activities.<br>
	 * Si el servicio se ejecuta en otro thread este objeto se encarga del thread-safety
	 * 
	 * @return El objeto que permite comunicación directa con el servicio (a través de invocaciones
	 *         a métodos)
	 */
	public T getIntercommObject() {
		return intercommObject;
	}

	/**
	 * Crea el binder entre componentes que requiere un objeto para ser utilizado como
	 * intercomunicador.<br>
	 * 
	 * @param <T>
	 *            Tipo del objeto usado como intercomunicador
	 * @param intercommObject
	 *            Para casos simples, el propio servicio puede servir de interfaz con el activity.
	 *            NO puede ser null
	 * @return El binder que ofrecerá el intercomunicador brindado al activity
	 */
	public static <T> LocalServiceBinder<T> create(T intercommObject) {
		LocalServiceBinder<T> binder = new LocalServiceBinder<T>();
		binder.intercommObject = intercommObject;
		return binder;
	}

}
