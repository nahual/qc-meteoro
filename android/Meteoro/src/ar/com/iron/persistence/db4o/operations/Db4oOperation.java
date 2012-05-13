/**
 * 02/07/2011 23:41:45 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o.operations;

import com.db4o.ObjectContainer;

/**
 * Esta interfaz representa una operación realizada sobre una base Db4o
 * 
 * @author D. García
 */
public interface Db4oOperation<T> {

	/**
	 * Realiza operaciones sobre la base de datos devolviendo los resultados obtenidos.<br>
	 * 
	 * @param container
	 *            Objeto contenedor de db4o con el que se hacen todas las operaciones
	 * @return Los resultados de la operación
	 */
	public T withDatabase(ObjectContainer container);

}
