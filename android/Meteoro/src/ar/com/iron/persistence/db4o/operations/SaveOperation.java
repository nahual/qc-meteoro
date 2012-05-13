/**
 * 02/07/2011 23:48:26 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o.operations;

import ar.com.iron.persistence.PersistenceException;
import ar.com.iron.persistence.persistibles.Persistible;

import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oException;

/**
 * Esta clase representa la operación de guardado usando Db4o
 * 
 * @author D. García
 */
public class SaveOperation implements Db4oOperation<Long> {

	private Persistible persistible;

	/**
	 * @see ar.com.iron.persistence.db4o.operations.Db4oOperation#withDatabase(com.db4o.ObjectContainer)
	 */
	public Long withDatabase(final ObjectContainer container) {
		// Lo guardamos una vez para adquirir ID
		saveInstance(container);
		final long persistibleId = container.ext().getID(persistible);
		persistible.setId(persistibleId);
		// Lo guardamos de nuevo con ID definido
		saveInstance(container);
		return persistibleId;
	}

	/**
	 * @param container
	 */
	private void saveInstance(final ObjectContainer container) {
		try {
			container.store(persistible);
		} catch (final DatabaseClosedException e) {
			throw new PersistenceException(
					"El contenedor db4o estaba cerrado al guardar una instancia: " + persistible, e);
		} catch (final DatabaseReadOnlyException e) {
			throw new PersistenceException("No se puede guardar la instancia en una base de solo lectura: "
					+ persistible, e);
		} catch (final Db4oException e) {
			throw new PersistenceException("Se produjo un error desconocido al guardar la instancia: " + persistible, e);
		}
	}

	public static SaveOperation create(final Persistible newPersistible) {
		final SaveOperation createOperation = new SaveOperation();
		createOperation.persistible = newPersistible;
		return createOperation;
	}

}
