/**
 * 30/04/2012 17:35:18 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import ar.com.iron.android.extensions.activities.CustomActivity;
import ar.com.iron.android.extensions.services.local.LocalServiceConnectionListener;
import ar.com.iron.android.extensions.services.local.LocalServiceConnector;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.persistence.DefaultOnFailurePersistenceOperationListener;
import ar.com.iron.persistence.PersistenceDao;
import ar.com.iron.persistence.PersistenceService;
import ar.com.iron.persistence.db4o.filters.Db4oFilter;
import ar.nahual.meteoro.model.CiudadPersistida;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

/**
 * 
 * @author D. García
 */
public class AgregarCiudadActivity extends CustomActivity {

	private AutoCompleteTextView ciudadAutoComplete;
	public static List<CiudadPersistida> ciudadesDisponibles;
	public static List<String> nombresCiudades;
	private ArrayAdapter<String> autoCompleteAdapter;
	private ProgressDialog cityProgress;
	private CiudadPersistida selectedCiudad;
	private Button saveButton;

	private LocalServiceConnector<PersistenceDao> persistenceConector;
	private PersistenceDao persistenceDao;

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	@Override
	public int getLayoutIdForActivity() {
		return R.layout.add_city_view;
	}

	/**
	 * Pide las ciudades para popular el autocomplete
	 */
	private void requestCiudades() {
		cityProgress = ProgressDialog.show(this, "Ciudades disponibles", "Buscando ciudades disponibles...", true,
				false);
		final RequestCitiesTask requestCitiesTask = new RequestCitiesTask(this);
		requestCitiesTask.execute("");
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomActivity#setUpComponents()
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

		ciudadAutoComplete = ViewHelper.findAutoComplete(R.id.nombreParcialCiudad, getContentView());
		saveButton = ViewHelper.findButton(R.id.agregarBtn, getContentView());
		saveButton.setEnabled(false);
		saveButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				onSaveClicked();
			}
		});

		autoCompleteAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,
				getNombresCiudades());
		ciudadAutoComplete.setAdapter(autoCompleteAdapter);
		ciudadAutoComplete.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {
			}

			@Override
			public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {
			}

			@Override
			public void afterTextChanged(final Editable s) {
				final String textoDeLaCiudad = s.toString();
				onTextoCambiado(textoDeLaCiudad);
			}
		});
	}

	protected void onPersistenceDaoDisponible(final PersistenceDao intercommObject) {
		this.persistenceDao = intercommObject;
	}

	/**
	 * Invocado al guadar la ciudad
	 */
	protected void onSaveClicked() {
		persistenceDao.findAllMatching(new Db4oFilter() {
			@Override
			public ObjectSet<?> executeOn(ObjectContainer container) {
				return container.queryByExample(selectedCiudad);
			}
		}, new DefaultOnFailurePersistenceOperationListener<List<CiudadPersistida>>(getContext()) {

			@Override
			public void onSuccess(List<CiudadPersistida> result) {
				if (result.size() == 0) {
					persistenceDao.save(selectedCiudad, new DefaultOnFailurePersistenceOperationListener<CiudadPersistida>(getContext()) {
						@Override
						public void onSuccess(final CiudadPersistida result) {
							onCiudadAgregada(result);
						}
					});
				} else {
					Log.d("DB", "La ciudad ya existe");
					onCiudadAgregada(selectedCiudad);
				}
			}
		});
	}

	/**
	 * Invocado al guardar la ciudad agregada
	 */
	protected void onCiudadAgregada(final CiudadPersistida result) {
		finish();
	}

	/**
	 * @param textoDeLaCiudad
	 */
	protected void onTextoCambiado(final String textoDeLaCiudad) {
		selectedCiudad = null;
		if (textoDeLaCiudad.length() > 3) {
			for (final CiudadPersistida ciudad : ciudadesDisponibles) {
				if (ciudad.getCityName().equals(textoDeLaCiudad)) {
					selectedCiudad = ciudad;
					break;
				}
			}
		}
		final boolean hayCiudadElegida = selectedCiudad != null;
		saveButton.setEnabled(hayCiudadElegida);
	}

	public List<String> getNombresCiudades() {
		if (nombresCiudades == null) {
			nombresCiudades = new ArrayList<String>();
			ciudadesDisponibles = new ArrayList<CiudadPersistida>();
			requestCiudades();
		}
		return nombresCiudades;
	}

	/**
	 * Recibe las ciudades en background
	 */
	public void onCiudadesDisponibles(final List<CiudadPersistida> result) {
		this.ciudadesDisponibles = result;
		nombresCiudades.clear();
		for (final CiudadPersistida ciudad : result) {
			nombresCiudades.add(ciudad.getCityName());
		}
		autoCompleteAdapter.notifyDataSetChanged();
		cityProgress.cancel();
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
