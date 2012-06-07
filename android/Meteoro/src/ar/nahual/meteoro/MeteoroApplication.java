/**
 * 13/05/2012 17:42:57 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro;

import android.content.Intent;
import ar.com.iron.android.extensions.applications.Db4oApplication;
import ar.com.iron.persistence.db4o.Db4oConfiguration;
import ar.nahual.meteoro.model.CiudadPersistida;

/**
 * Esta clase es la implementación con Db4o
 * 
 * @author D. García
 */
public class MeteoroApplication extends Db4oApplication {

	private static MeteoroApplication app;
	private MeteoroBackend backend;

	public static MeteoroBackend getBackend() {
		return app.backend;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		MeteoroApplication.app = this;
		this.backend = new MeteoroBackend(getApplicationContext());
		startService(new Intent(this, PronosticoUpdateService.class));
	}

	/**
	 * @see ar.com.iron.android.extensions.applications.CustomApplication#onTerminate()
	 */
	@Override
	public void onTerminate() {
		stopService(new Intent(this, PronosticoUpdateService.class));
	}

	/**
	 * @see ar.com.iron.android.extensions.applications.Db4oApplication#registerPersistentClasses(ar.com.iron.persistence.db4o.Db4oConfiguration)
	 */
	@Override
	protected void registerPersistentClasses(final Db4oConfiguration configuration) {
		configuration.addPersistibleClass(CiudadPersistida.class);
	}

	/**
	 * @see ar.com.iron.android.extensions.applications.CustomApplication#getMainThreadName()
	 */
	@Override
	protected String getMainThreadName() {
		return "Meteoro";
	}
}
