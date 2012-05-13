/**
 * 03/07/2011 00:53:31 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o.operations;

import ar.com.iron.persistence.PersistenceException;
import ar.com.iron.persistence.persistibles.Persistible;

import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.Db4oException;
import com.db4o.ext.InvalidIDException;

/**
 * Esta clase representa la operacion de obtención de una entidad por ID
 * 
 * @author D. García
 */
public class GetOperation implements Db4oOperation<Persistible> {
	private Long persistibleId;

	public static GetOperation create(final Long instanceId) {
		final GetOperation name = new GetOperation();
		name.persistibleId = instanceId;
		return name;
	}

	/**
	 * @see ar.com.iron.persistence.db4o.operations.Db4oOperation#withDatabase(com.db4o.ObjectContainer)
	 */
	public Persistible withDatabase(final ObjectContainer container) {
		Object object;
		try {
			object = container.ext().getByID(persistibleId);
		} catch (final DatabaseClosedException e) {
			throw new PersistenceException("El contenedor db4o estaba cerrado al buscar objeto por id", e);
		} catch (final InvalidIDException e) {
			throw new PersistenceException("El id usado es rechazado como invalido por db4o: " + persistibleId, e);
		} catch (final Db4oException e) {
			throw new PersistenceException("Se produjo un error desconocido al buscar por ID: " + persistibleId, e);
		}
		return (Persistible) object;
	}
}
