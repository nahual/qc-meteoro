/**
 * 02/07/2011 23:24:43 Copyright (C) 2011 Darío García
 */
package ar.com.iron.android.extensions.applications;

import ar.com.iron.persistence.PersistenceEngineVariable;
import ar.com.iron.persistence.db4o.Db4oConfiguration;
import ar.com.iron.persistence.db4o.Db4oEngine;

/**
 * Esta clase define el setup inicial básico para una aplicación que utiliza DB4o como motor de
 * persistencia
 * 
 * @author D. García
 */
public abstract class Db4oApplication extends CustomApplication {

	/**
	 * @see ar.com.iron.android.extensions.applications.CustomApplication#onCreate()
	 */
	@Override
	public void onCreate() {
		super.onCreate();

		final Db4oConfiguration persistenceConfiguration = getPersistenceConfiguration();
		final Db4oEngine sqliteEngine = Db4oEngine.create(this, persistenceConfiguration);
		PersistenceEngineVariable.setInstance(sqliteEngine);
	}

	/**
	 * Define la configuración para utilizar con el motor de persistencia
	 * 
	 * @return La configuración que define el mapeo de clases y el archivo para utilizar como base
	 */
	protected Db4oConfiguration getPersistenceConfiguration() {
		final Db4oConfiguration configuration = Db4oConfiguration.create();
		registerPersistentClasses(configuration);
		return configuration;
	}

	/**
	 * Define las entidades persistentes de la aplicación
	 * 
	 * @param configuration
	 *            La configuración en la que se deben agregar los mapeos
	 */
	protected abstract void registerPersistentClasses(Db4oConfiguration configuration);

}
