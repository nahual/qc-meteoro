/**
 * 01/03/2011 00:50:35 Copyright (C) 2011 Darío L. García
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

import android.app.Service;
import android.content.Intent;

/**
 * Esta interfaz es una marca para los servicios que son bindeables localmente. Sirve más como
 * recordatorio que el método {@link Service#onBind(android.content.Intent)} debe devolver una
 * instancia de {@link LocalServiceBinder}
 * 
 * @author D. García
 * @param <T>
 *            Tipo del objeto usado como itercomunicador
 */
public interface LocallyBindableService<T> {

	/**
	 * Invocado durante el proceso de binding entre el servicio y el activity este método debe
	 * devolver in {@link LocalServiceBinder} para que la conexión usando un
	 * {@link LocalServiceConnector} sea exitosa
	 * 
	 * @param intent
	 *            El intent que origina la conexión
	 * @return El binder que ofrece un intercomunicador entre los componentes
	 */
	public LocalServiceBinder<T> onBind(Intent intent);

}
