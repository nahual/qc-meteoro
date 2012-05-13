/**
 * 02/07/2011 23:27:19 Copyright (C) 2011 Darío García
 */
package ar.com.iron.persistence.db4o;

import java.util.concurrent.atomic.AtomicReference;

import android.content.Context;
import android.util.Log;
import ar.com.iron.concurrency.CodeMutex;
import ar.com.iron.helpers.FileHelper;
import ar.com.iron.persistence.PersistenceDao;
import ar.com.iron.persistence.PersistenceEngine;
import ar.com.iron.persistence.PersistenceException;
import ar.com.iron.persistence.db4o.operations.Db4oOperation;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ext.Db4oException;
import com.db4o.ext.Db4oIOException;

/**
 * Esta clase representa el motor de persistencia usando DB4o
 * 
 * @author D. García
 */
public class Db4oEngine implements PersistenceEngine {

	/**
	 * Cantidad máxima de espera por el mutex de la base. 3 min
	 */
	private static final long DB_TIMEOUT_LIMIT = 3 * 60 * 1000;

	/**
	 * Mutex para impedir que varios hilos operen sobre la misma base a la vez
	 */
	private CodeMutex dbMutex;

	/**
	 * Configuración para la base
	 */
	private Db4oConfiguration configuration;

	private Context context;

	/**
	 * Base utilizada actualmente para las operaciones
	 */
	private ObjectContainer currentDb;

	/**
	 * @see ar.com.iron.persistence.PersistenceEngine#getDao()
	 */
	public PersistenceDao getDao() {
		return Db4oDao.create(this);
	}

	/**
	 * Ejecuta código que necesita acceso a la base en forma exclusiva
	 * 
	 * @param db4oOperation
	 *            La operación a realizar en la base
	 */
	public <T> T doExclusivelyInDatabase(final Db4oOperation<T> db4oOperation) {
		final AtomicReference<T> resultados = new AtomicReference<T>();
		final Runnable ejecutarOperacionExclusiva = new Runnable() {
			public void run() {
				final T results = doInDatabase(db4oOperation);
				resultados.set(results);
			}
		};
		dbMutex.runExclusively(ejecutarOperacionExclusiva);

		return resultados.get();
	}

	/**
	 * Ejecuta la operación pasada en la base de datos. Este método abre una transacción en la cuál
	 * se ejecuta la operación pasada pero no asegura el acceso exclusivo al archivo. Para ello debe
	 * utilizar el método {@link #doExclusivelyInDatabase(Db4oOperation)}
	 * 
	 * @param <T>
	 *            Tipo de resultado devuelto
	 * @param db4oOperation
	 *            La operación aplicada sobre la base
	 * @return El resultado
	 * @throws PersistenceException
	 *             Si se produjo un problema de acceso a la base
	 */
	private <T> T doInDatabase(final Db4oOperation<T> db4oOperation) throws PersistenceException {
		final ObjectContainer db = getCurrentDb();
		boolean shouldCommit = false;
		try {
			final T result = db4oOperation.withDatabase(db);
			shouldCommit = true;
			return result;
		} catch (final Exception e) {
			Log.e("Db4oEngine", "Error en el motor db4o", e);
			throw new PersistenceException(e);
		} finally {
			if (shouldCommit) {
				db.commit();
			} else {
				try {
					db.rollback();
				} catch (final Db4oException e) {
					Log.e("Db4oEngine", "Error al rollbackear");
					releaseResources();
				}
			}
		}
	}

	public static Db4oEngine create(final Context context, final Db4oConfiguration configuration) {
		final Db4oEngine engine = new Db4oEngine();
		engine.context = context;
		engine.configuration = configuration;
		engine.dbMutex = CodeMutex.create();
		engine.dbMutex.setResourceName("Base de datos");
		engine.dbMutex.setTimeoutLimit(DB_TIMEOUT_LIMIT);
		return engine;
	}

	/**
	 * @see ar.com.iron.persistence.PersistenceEngine#releaseResources()
	 */
	public void releaseResources() {
		if (currentDb != null) {
			try {
				currentDb.close();
			} catch (final Db4oIOException e) {
				Log.e("Db4oEngine", "Error al cerrar la base db4o", e);
			}
		}
		currentDb = null;
	}

	/**
	 * Devuelve la base de Db4o abierta para operar sobre ella
	 * 
	 * @return El contenedor de objetos abierta mientras no se liberen recursos
	 */
	public ObjectContainer getCurrentDb() {
		if (currentDb == null) {
			currentDb = openNewDb();
		}
		return currentDb;
	}

	/**
	 * Abre una nueva base para acceder a los datos mientras dure la sesión del servicio de
	 * persistencia
	 * 
	 * @return La base contenedora abierta y creada
	 * @throws PersistenceException
	 *             Si se produce un error al acceder a la base
	 */
	private ObjectContainer openNewDb() throws PersistenceException {
		final EmbeddedConfiguration internalConfig = configuration.getInternalConfig();
		final String databasePath = FileHelper.getPathFromData(context, configuration.getDatabaseDir(),
				configuration.getDatabaseName());
		ObjectContainer db;
		try {
			db = Db4oEmbedded.openFile(internalConfig, databasePath);
		} catch (final Db4oException e) {
			throw new PersistenceException("No fue posible abrir la base db4o en: " + databasePath, e);
		}
		return db;
	}
}
