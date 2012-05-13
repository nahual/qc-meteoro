/**
 * 03/07/2011 01:10:20 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o.operations;

import ar.com.iron.persistence.PersistenceException;
import ar.com.iron.persistence.persistibles.Persistible;

import com.db4o.ObjectContainer;
import com.db4o.ext.DatabaseClosedException;
import com.db4o.ext.DatabaseReadOnlyException;
import com.db4o.ext.Db4oException;
import com.db4o.ext.Db4oIOException;

/**
 * Esta clase representa la operación de borrado de una instancia de la base db4o
 * 
 * @author D. García
 */
public class DeleteOperation implements Db4oOperation<Void> {

	private Persistible deletedPersistible;

	public static DeleteOperation create(final Persistible deletedPersistible) {
		final DeleteOperation deleteOperation = new DeleteOperation();
		deleteOperation.deletedPersistible = deletedPersistible;
		return deleteOperation;
	}

	/**
	 * @see ar.com.iron.persistence.db4o.operations.Db4oOperation#withDatabase(com.db4o.ObjectContainer)
	 */
	public Void withDatabase(final ObjectContainer container) {
		try {
			container.delete(deletedPersistible);
		} catch (final Db4oIOException e) {
			throw new PersistenceException("Se produjo un error de IO al borrar una instancia: " + deletedPersistible,
					e);
		} catch (final DatabaseClosedException e) {
			throw new PersistenceException("La base esta cerrada al borrar una instancia: " + deletedPersistible, e);
		} catch (final DatabaseReadOnlyException e) {
			throw new PersistenceException("No se pede borrar la instancia en base de solo lectura: "
					+ deletedPersistible, e);
		} catch (final Db4oException e) {
			throw new PersistenceException("Error desconocido al borrar instancia: " + deletedPersistible, e);
		}
		return null;
	}

}
