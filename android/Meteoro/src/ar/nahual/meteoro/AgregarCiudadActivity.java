/**
 * 30/04/2012 17:35:18 Copyright (C) 2011 10Pines S.R.L.
 */
package ar.nahual.meteoro;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import ar.com.iron.android.extensions.activities.CustomActivity;
import ar.com.iron.android.extensions.services.local.LocalServiceConnectionListener;
import ar.com.iron.android.extensions.services.local.LocalServiceConnector;
import ar.com.iron.helpers.ToastHelper;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.persistence.PersistenceDao;
import ar.com.iron.persistence.PersistenceOperationListener;
import ar.com.iron.persistence.PersistenceService;
import ar.nahual.meteoro.model.Ciudad;
import ar.nahual.meteoro.model.CiudadPersistida;

/**
 * 
 * @author D. García
 */
public class AgregarCiudadActivity extends CustomActivity {

	private AutoCompleteTextView ciudadAutoComplete;
	public static List<Ciudad> ciudadesDisponibles;
	public static List<String> nombresCiudades;
	private ArrayAdapter<String> autoCompleteAdapter;
	private ProgressDialog cityProgress;
	private Ciudad selectedCiudad;
	private Button saveButton;
	private CheckBox usaHumedad;
	private CheckBox usaPresion;
	private CheckBox usaRayosUv;
	private CheckBox usaSensacionTermica;
	private CheckBox usaTemperatura;

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

		usaHumedad = ViewHelper.findCheckBox(R.id.usaHumedad, getContentView());
		usaPresion = ViewHelper.findCheckBox(R.id.usaPresion, getContentView());
		usaRayosUv = ViewHelper.findCheckBox(R.id.usaRayosUv, getContentView());
		usaSensacionTermica = ViewHelper.findCheckBox(R.id.usaSensacionTermica, getContentView());
		usaTemperatura = ViewHelper.findCheckBox(R.id.usaTemperatura, getContentView());

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
		final CiudadPersistida persistible = CiudadPersistida.create(selectedCiudad);
		persistenceDao.save(persistible, new PersistenceOperationListener<CiudadPersistida>() {
			@Override
			public void onSuccess(final CiudadPersistida result) {
				onCiudadAgregada(result);
			}

			@Override
			public void onFailure(final Exception exceptionThrown) {
				ToastHelper.create(getContext()).showShort(
						"Error al guardar en la base: " + exceptionThrown.getMessage());
			}
		});
	}

	/**
	 * Invocado al guardar la ciudad agregada
	 */
	protected void onCiudadAgregada(final CiudadPersistida result) {
		ToastHelper.create(this).showShort("Ciudad \"" + result.getCityName() + "\" agregada");
		finish();
	}

	/**
	 * @param textoDeLaCiudad
	 */
	protected void onTextoCambiado(final String textoDeLaCiudad) {
		selectedCiudad = null;
		if (textoDeLaCiudad.length() > 3) {
			for (final Ciudad ciudad : ciudadesDisponibles) {
				if (ciudad.getName().equals(textoDeLaCiudad)) {
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
			ciudadesDisponibles = new ArrayList<Ciudad>();
			requestCiudades();
		}
		return nombresCiudades;
	}

	/**
	 * Recibe las ciudades en background
	 */
	public void onCiudadesDisponibles(final List<Ciudad> result) {
		this.ciudadesDisponibles = result;
		nombresCiudades.clear();
		for (final Ciudad ciudad : result) {
			nombresCiudades.add(ciudad.getName());
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
