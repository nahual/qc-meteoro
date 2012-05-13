/**
 * 
 */
package ar.com.iron.persistence;

import ar.com.iron.android.extensions.services.BackgroundTask;

/**
 * Esta clase es una generalización de las operaciones de persistencia que ocurren en background. Se
 * deja el método execute pendiente para las subclases y se deriva el success o failure al listener
 * 
 * @author D. García
 */
public abstract class PersistenceBackgroundTask extends BackgroundTask<Object> {

	private final PersistenceOperationListener<Object> persistenceOperationListener;

	@SuppressWarnings("unchecked")
	public PersistenceBackgroundTask(PersistenceOperationListener<?> listener) {
		this.persistenceOperationListener = (PersistenceOperationListener<Object>) listener;
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundTask#onFailure(java.lang.Exception)
	 */
	@Override
	public void onFailure(Exception exceptionThrown) {
		persistenceOperationListener.onFailure(exceptionThrown);
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundTask#onSuccess(java.lang.Object)
	 */
	@Override
	public void onSuccess(Object result) {
		persistenceOperationListener.onSuccess(result);
	}
}
