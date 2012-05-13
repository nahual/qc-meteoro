/**
 * 05/03/2011 15:39:01 Copyright (C) 2011 Darío L. García
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

/**
 * Esta interfaz define un punto de abstracción del motor de persistencia concreto de manera que
 * tanto bases SQl como db40 tengan una interfaz común
 * 
 * @author D. García
 */
public interface PersistenceEngine {

	/**
	 * Devuelve el dao que permite derivar operaciones a este motor de persistencia.<br>
	 * El dao tiene los métodos que permiten guardar, listar, borrar, etc
	 * 
	 * @return El dao para interactuar con este motor
	 */
	PersistenceDao getDao();

	/**
	 * Este método es invocado para aquellos motores que necesiten liberar recursos pesados si no
	 * van a ser utilizados.<br>
	 * Este método es llamado al detenerse el servicio de persistencia
	 */
	void releaseResources();

}
