/**
 * 03/07/2011 01:16:58 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o.filters;

import ar.com.iron.persistence.DataFilter;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * Esta interfaz representa un filtro a aplicar sobre los datos de la base Db4o para obtener un
 * conjunto de objetos
 * 
 * @author D. García
 */
public interface Db4oFilter extends DataFilter {

	/**
	 * Ejecuta este filtro expresándolo como query de Db4o y devuelve los resultados de su ejecución
	 * 
	 * @param container
	 *            La base sobre la que se aplica este filtro
	 * @return El conjunto de objetos que cumple con el resultado
	 */
	ObjectSet<?> executeOn(ObjectContainer container);

}
