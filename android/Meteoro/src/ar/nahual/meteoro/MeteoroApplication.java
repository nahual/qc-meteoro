/**
 * 13/05/2012 17:42:57 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro;

import ar.com.iron.android.extensions.applications.Db4oApplication;
import ar.com.iron.persistence.db4o.Db4oConfiguration;

/**
 * Esta clase es la implementación con Db4o
 * 
 * @author D. García
 */
public class MeteoroApplication extends Db4oApplication {

	/**
	 * @see ar.com.iron.android.extensions.applications.Db4oApplication#registerPersistentClasses(ar.com.iron.persistence.db4o.Db4oConfiguration)
	 */
	@Override
	protected void registerPersistentClasses(final Db4oConfiguration configuration) {

	}

	/**
	 * @see ar.com.iron.android.extensions.applications.CustomApplication#getMainThreadName()
	 */
	@Override
	protected String getMainThreadName() {
		return "Meteoro";
	}

}
