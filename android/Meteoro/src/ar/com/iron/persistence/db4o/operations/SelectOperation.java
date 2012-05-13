/**
 * 03/07/2011 01:13:13 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o.operations;

import java.util.ArrayList;
import java.util.List;

import ar.com.iron.persistence.PersistenceException;
import ar.com.iron.persistence.db4o.filters.Db4oFilter;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.Db4oException;
import com.db4o.ext.Db4oIOException;

/**
 * Esta clase representa la operación de búsqueda de instancias en una base Db4o
 * 
 * @author D. García
 */
public class SelectOperation implements Db4oOperation<List<Object>> {

	private Db4oFilter filter;

	/**
	 * @see ar.com.iron.persistence.db4o.operations.Db4oOperation#withDatabase(com.db4o.ObjectContainer)
	 */
	public List<Object> withDatabase(final ObjectContainer container) {
		ObjectSet<?> objectSet;
		try {
			objectSet = filter.executeOn(container);
		} catch (final Db4oIOException e) {
			throw new PersistenceException("Se produjo un error de IO buscando objetos", e);
		} catch (final DatabaseClosedException e) {
			throw new PersistenceException("El contenedor de db4o esta cerrado al resolver una query: " + filter, e);
		} catch (final Db4oException e) {
			throw new PersistenceException("Se produjo un error desconocido resolviendo la query de un filtro: "
					+ filter, e);
		}
		// Pasamos los objetos a una lista independiente de la base
		ArrayList<Object> results = new ArrayList<Object>(objectSet.size());
		for (Object persistible : objectSet) {
			results.add(persistible);
		}
		return results;
	}

	public static SelectOperation create(final Db4oFilter filter) {
		final SelectOperation select = new SelectOperation();
		select.filter = filter;
		return select;
	}

}
