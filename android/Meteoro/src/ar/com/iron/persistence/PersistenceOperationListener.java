/**
 * 05/03/2011 16:51:53 Copyright (C) 2011 Darío L. García
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
 * Esta interfaz permite al activity realizar acciones posteriores a la persistencia exitosa
 * 
 * @author D. García
 * @param <T>
 *            Tipo del objeto esperado
 */
public interface PersistenceOperationListener<T> {

	/**
	 * Invocado con el objeto resultado de la operación de persistencia
	 * 
	 * @param result
	 *            El objeto resultado
	 */
	public void onSuccess(T result);

	/**
	 * Invocado cuando se produce una excepción en la operación de persistencia
	 * 
	 * @param exceptionThrown
	 *            Excepción producida
	 */
	public void onFailure(Exception exceptionThrown);

}
