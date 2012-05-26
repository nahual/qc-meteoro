/**
 * 26/05/2012 16:18:08 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import ar.com.iron.android.extensions.activities.CustomActivity;
import ar.com.iron.android.extensions.services.local.LocalServiceConnectionListener;
import ar.com.iron.android.extensions.services.local.LocalServiceConnector;
import ar.com.iron.helpers.ToastHelper;
import ar.com.iron.persistence.DefaultOnFailurePersistenceOperationListener;
import ar.com.iron.persistence.PersistenceDao;
import ar.com.iron.persistence.PersistenceOperationListener;
import ar.com.iron.persistence.PersistenceService;
import ar.com.iron.persistence.db4o.filters.AllInstancesFilter;
import ar.nahual.meteoro.model.CiudadPersistida;

/**
 * 
 * @author D. García
 */
public class SplasActivity extends CustomActivity {

	private final List<CiudadPersistida> ciudades = new ArrayList<CiudadPersistida>();
	private CiudadPersistida ciudadActual;

	private LocalServiceConnector<PersistenceDao> persistenceConector;
	private PersistenceDao persistenceDao;

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#setUpComponents()
	 */
	@Override
	public void setUpComponents() {
		persistenceConector = LocalServiceConnector.create(PersistenceService.class);
		persistenceConector.setConnectionListener(new LocalServiceConnectionListener<PersistenceDao>() {
			@Override
			public void onServiceDisconnection(final PersistenceDao disconnectedIntercomm) {
				// No hacemos nada
			}

			@Override
			public void onServiceConnection(final PersistenceDao intercommObject) {
				onPersistenceDaoDisponible(intercommObject);
			}
		});
		persistenceConector.bindToService(this);
	}

	/**
	 * Invocado cuando tenemos disponible el dao para los datos
	 * 
	 * @param intercommObject
	 */
	protected void onPersistenceDaoDisponible(final PersistenceDao intercommObject) {
		this.persistenceDao = intercommObject;
		final AllInstancesFilter todasLasCiudades = new AllInstancesFilter(CiudadPersistida.class);
		persistenceDao.findAllMatching(todasLasCiudades, new DefaultOnFailurePersistenceOperationListener<List<CiudadPersistida>>(getContext()) {
			@Override
			public void onSuccess(final List<CiudadPersistida> result) {
				onCiudadesCargadasDeLaBase(result);
			}
		});
	}

	protected void onCiudadesCargadasDeLaBase(final List<CiudadPersistida> result) {
		if (result.isEmpty()) {
			startActivityForResult(new Intent(this, AgregarCiudadActivity.class), 1);
		} else {
			final CiudadPersistida primeraCiudad = result.get(0);
			startActivity(new Intent(this, VerCiudadActivity.class));
		}
	}

	/**
	 * Muestra en la pantalla los datos de la ciudad actual
	 * 
	 * @param ciudadPersistida
	 */
	private void mostrarLaCiudad(final CiudadPersistida ciudadPersistida) {
		ciudadActual = ciudadPersistida;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	@Override
	public int getLayoutIdForActivity() {
		return R.layout.splash_view;
	}

	/**
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		persistenceConector.unbindFromService(this);
		super.onStop();
	}

}