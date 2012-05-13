/**
 * 05/03/2011 15:47:59 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.persistence;

import android.util.Log;
import ar.com.iron.annotations.CantBeNull;

/**
 * Esta clase sirve de punto de acceso al motor de persistencia compartido por la aplicación y por
 * la capa de código genérico
 * 
 * @author D. García
 */
public abstract class PersistenceEngineVariable {

	/**
	 * Variable global que define qué engine utilizar
	 */
	private static PersistenceEngine instance;

	/**
	 * Establece el motor de persistencia que se utilizará en esta aplicación
	 * 
	 * @param instance
	 *            Motor de persistencia para guardar el estado de los objetos
	 */
	public static void setInstance(PersistenceEngine instance) {
		PersistenceEngineVariable.instance = instance;
	}

	/**
	 * Devuelve el motor de persistencia configurado para esta aplicación
	 * 
	 * @return El motor de persistencia establecido a nivel de aplicación
	 */
	@CantBeNull
	public static PersistenceEngine getInstance() {
		if (instance == null) {
			Log.e("PersistenceService", "No existe PersistenceEngine! Se declaró uno en el Aplication?");
			throw new RuntimeException(
					"No se definió el motor de persistencia desde la aplicación. Se definión la custom Application en el manifest?");
		}
		return instance;
	}

}
