/**
 * 05/03/2011 15:35:15 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.com.iron.persistence;

import android.content.Intent;
import ar.com.iron.android.extensions.services.BackgroundProcess;
import ar.com.iron.android.extensions.services.BackgroundService;
import ar.com.iron.android.extensions.services.local.LocalServiceBinder;
import ar.com.iron.android.extensions.services.local.LocallyBindableService;

/**
 * Esta clase representa el servicio de persistenca de objetos que utiliza un motor de persistencia
 * que puede ser una base sql, db4o o cualquier otro medio<br>
 * <br>
 * Para utilizarlo se debe definir un motor de persistencia con
 * {@link PersistenceEngineVariable#setInstance(PersistenceEngine)}, y agregar al manifest la
 * siguiente línea:<br>
 * <br>
 * &lt;service android:name="ar.com.iron.persistence.PersistenceService" /&gt;
 * 
 * 
 * @author D. García
 */
public class PersistenceService extends BackgroundService implements LocallyBindableService<PersistenceDao> {

	/**
	 * Permite el enlace con los otros componentes de android
	 */
	private LocalServiceBinder<PersistenceDao> serviceBinder;

	/**
	 * El motor concreto para la persistencia
	 */
	private PersistenceEngine persistenceEngine;

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#beforeProcessStart()
	 */
	@Override
	protected void beforeProcessStart() {
		persistenceEngine = PersistenceEngineVariable.getInstance();
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#afterProcessStart()
	 */
	@Override
	protected void afterProcessStart() {
		PersistenceDao dao = persistenceEngine.getDao();
		dao.setBackgroundProcess(getBackgroundProcess());
		serviceBinder = LocalServiceBinder.create(dao);
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#createBackgroundThread()
	 */
	@Override
	protected BackgroundProcess createBackgroundThread() {
		return new BackgroundProcess(this, "PersistenceService");
	}

	/**
	 * @see ar.com.iron.android.extensions.services.BackgroundService#onBind(android.content.Intent)
	 */
	@Override
	public LocalServiceBinder<PersistenceDao> onBind(Intent intent) {
		return serviceBinder;
	}

	/**
	 * @see android.app.Service#onUnbind(android.content.Intent)
	 */
	@Override
	public boolean onUnbind(Intent intent) {
		persistenceEngine.releaseResources();

		// Queremos que vuelvan a invocar onBind
		return false;
	}

}
