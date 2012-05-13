/**
 * 03/07/2011 01:52:54 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o.filters;

import ar.com.iron.persistence.persistibles.Persistible;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * Esta clase representa el filtro que devuelve todas las instancias de una clase
 * 
 * @author D. García
 */
public class AllInstancesFilter implements Db4oFilter {

	private final Class<? extends Persistible> filteredClass;

	public AllInstancesFilter(final Class<? extends Persistible> persistibleClass) {
		this.filteredClass = persistibleClass;
	}

	/**
	 * @see ar.com.iron.persistence.db4o.filters.Db4oFilter#executeOn(com.db4o.ObjectContainer)
	 */
	public ObjectSet<?> executeOn(final ObjectContainer container) {
		final ObjectSet<?> allInstances = container.query(filteredClass);
		return allInstances;
	}

}
