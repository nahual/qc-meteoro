/**
 * 02/07/2011 23:28:31 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o;

import java.util.List;

import android.content.Context;
import ar.com.iron.android.extensions.services.BackgroundProcess;
import ar.com.iron.android.extensions.services.BackgroundTask;
import ar.com.iron.persistence.DataFilter;
import ar.com.iron.persistence.PersistenceBackgroundTask;
import ar.com.iron.persistence.PersistenceDao;
import ar.com.iron.persistence.PersistenceOperationListener;
import ar.com.iron.persistence.db4o.filters.Db4oFilter;
import ar.com.iron.persistence.db4o.operations.DeleteOperation;
import ar.com.iron.persistence.db4o.operations.GetOperation;
import ar.com.iron.persistence.db4o.operations.SaveOperation;
import ar.com.iron.persistence.db4o.operations.SelectOperation;
import ar.com.iron.persistence.messages.PersistibleChangedMessage;
import ar.com.iron.persistence.persistibles.Persistible;

/**
 * Esta clase representa el punto de acceso al motor de persistencia de DB4o
 * 
 * @author D. García
 */
public class Db4oDao implements PersistenceDao {

	private BackgroundProcess process;
	private Db4oEngine db4oEngine;

	public static Db4oDao create(final Db4oEngine db4oEngine) {
		final Db4oDao dao = new Db4oDao();
		dao.db4oEngine = db4oEngine;
		return dao;
	}

	/**
	 * Envía la tarea al proceso background para procesarla en otro hilo
	 * 
	 * @param saveOperation
	 *            La operación a realizar
	 */
	private void processInBg(final BackgroundTask<Object> saveOperation) {
		process.addTask(saveOperation);
	}

	/**
	 * @see ar.com.iron.persistence.PersistenceDao#save(ar.com.iron.persistence.persistibles.Persistible,
	 *      ar.com.iron.persistence.PersistenceOperationListener)
	 */
	public <T extends Persistible> void save(final T newItem,
			final PersistenceOperationListener<T> persistenceOperationListener) {
		final BackgroundTask<Object> saveOperation = new PersistenceBackgroundTask(persistenceOperationListener) {
			@Override
			public Object execute(final Context backgroundContext) {
				int changeType;
				if (newItem.getId() == null) {
					changeType = PersistibleChangedMessage.CREATED_CHANGE_TYPE;
				} else {
					changeType = PersistibleChangedMessage.UPDATED_CHANGE_TYPE;
				}
				final SaveOperation saveOperation = SaveOperation.create(newItem);
				db4oEngine.doExclusivelyInDatabase(saveOperation);

				// Enviamos el intent para los que quieran ser notificados del cambio
				final PersistibleChangedMessage persistibleChangedMessage = new PersistibleChangedMessage(newItem,
						changeType);
				process.getContexto().sendBroadcast(persistibleChangedMessage);

				return newItem;
			}
		};
		processInBg(saveOperation);
	}

	/**
	 * @see ar.com.iron.persistence.PersistenceDao#setBackgroundProcess(ar.com.iron.android.extensions.services.BackgroundProcess)
	 */
	public void setBackgroundProcess(final BackgroundProcess process) {
		this.process = process;
	}

	/**
	 * @see ar.com.iron.persistence.PersistenceDao#findAllMatching(ar.com.iron.persistence.DataFilter,
	 *      ar.com.iron.persistence.PersistenceOperationListener)
	 */
	public <T> void findAllMatching(final DataFilter filter,
			final PersistenceOperationListener<List<T>> persistenceOperationListener) {
		if (!(filter instanceof Db4oFilter)) {
			throw new IllegalArgumentException("Este dao solo admite filtros instancias de " + Db4oFilter.class
					+ " pero recibió: " + filter);
		}
		final Db4oFilter db4oFilter = (Db4oFilter) filter;
		final BackgroundTask<Object> listOperation = new PersistenceBackgroundTask(persistenceOperationListener) {
			@Override
			public Object execute(final Context backgroundContext) {
				final SelectOperation selectOperation = SelectOperation.create(db4oFilter);
				final List<Object> results = db4oEngine.doExclusivelyInDatabase(selectOperation);
				return results;
			}
		};
		processInBg(listOperation);
	}

	/**
	 * @see ar.com.iron.persistence.PersistenceDao#delete(ar.com.iron.persistence.persistibles.Persistible,
	 *      ar.com.iron.persistence.PersistenceOperationListener)
	 */
	public <T extends Persistible> void delete(final T element,
			final PersistenceOperationListener<T> persistenceOperationListener) {
		final BackgroundTask<Object> listOperation = new PersistenceBackgroundTask(persistenceOperationListener) {
			@Override
			public Object execute(final Context backgroundContext) {
				final Long elementId = element.getId();
				if (elementId == null) {
					// No hace falta borrarlo
					return element;
				}
				final DeleteOperation deleteOperation = DeleteOperation.create(element);
				db4oEngine.doExclusivelyInDatabase(deleteOperation);
				// Enviamos el intent para notificar del evento
				final PersistibleChangedMessage persistibleChangedMessage = new PersistibleChangedMessage(element,
						PersistibleChangedMessage.DELETED_CHANGE_TYPE);
				process.getContexto().sendBroadcast(persistibleChangedMessage);
				return element;
			}
		};
		processInBg(listOperation);
	}

	/**
	 * @see ar.com.iron.persistence.PersistenceDao#getOf(java.lang.Class, java.lang.Long,
	 *      ar.com.iron.persistence.PersistenceOperationListener)
	 */
	public <T extends Persistible> void getOf(final Class<T> expectedClass, final Long id,
			final PersistenceOperationListener<T> persistenceOperationListener) {
		final BackgroundTask<Object> getOperation = new PersistenceBackgroundTask(persistenceOperationListener) {
			@Override
			public Object execute(final Context backgroundContext) {
				final GetOperation getOperation = GetOperation.create(id);
				final Persistible foundPersistible = db4oEngine.doExclusivelyInDatabase(getOperation);
				return foundPersistible;
			}
		};
		processInBg(getOperation);
	}

}
