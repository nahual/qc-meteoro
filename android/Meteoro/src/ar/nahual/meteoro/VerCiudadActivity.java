package ar.nahual.meteoro;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomListActivity;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.android.extensions.services.local.LocalServiceConnectionListener;
import ar.com.iron.android.extensions.services.local.LocalServiceConnector;
import ar.com.iron.helpers.ToastHelper;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;
import ar.com.iron.persistence.PersistenceDao;
import ar.com.iron.persistence.PersistenceOperationListener;
import ar.com.iron.persistence.PersistenceService;
import ar.com.iron.persistence.db4o.filters.AllInstancesFilter;
import ar.nahual.meteoro.model.CiudadPersistida;
import ar.nahual.meteoro.model.PronosticoDiario;

public class VerCiudadActivity extends CustomListActivity<PronosticoDiario> {

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
		persistenceDao.findAllMatching(todasLasCiudades, new PersistenceOperationListener<List<CiudadPersistida>>() {
			@Override
			public void onFailure(final Exception exceptionThrown) {
				ToastHelper.create(getContext()).showShort(
						"Se produjo un error al acceder a las bases guardadas: " + exceptionThrown.getMessage());
			}

			@Override
			public void onSuccess(final List<CiudadPersistida> result) {
				onCiudadesCargadasDeLaBase(result);
			}
		});
	}

	protected void onCiudadesCargadasDeLaBase(final List<CiudadPersistida> result) {
		this.ciudades.clear();
		this.ciudades.addAll(result);
		if (ciudades.isEmpty()) {
			startActivity(new Intent(this, AgregarCiudadActivity.class));
		} else {
			mostrarLaCiudad(ciudades.get(0));
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
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getContextMenuItems()
	 */
	@Override
	public ContextMenuItem<? extends CustomableListActivity<PronosticoDiario>, PronosticoDiario>[] getContextMenuItems() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementList()
	 */
	@Override
	public List<PronosticoDiario> getElementList() {
		final List<PronosticoDiario> pronosticos = new ArrayList<PronosticoDiario>();
		pronosticos.add(PronosticoDiario.create("Martes", 22));
		pronosticos.add(PronosticoDiario.create("Miercoles", 23));
		pronosticos.add(PronosticoDiario.create("jueves", 24));
		return pronosticos;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementRenderBlock()
	 */
	@Override
	public RenderBlock<PronosticoDiario> getElementRenderBlock() {
		return new RenderBlock<PronosticoDiario>() {
			@Override
			public void render(final View itemView, final PronosticoDiario item, final LayoutInflater inflater) {
				final TextView diaPronosticoText = ViewHelper.findTextView(R.id.diaPronostico_txt, itemView);
				diaPronosticoText.setText(item.getDia());
				final TextView tempPronostico = ViewHelper.findTextView(R.id.tempPronostico_txt, itemView);
				tempPronostico.setText(String.valueOf(item.getTemp()));
			}
		};
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getLayoutIdForElements()
	 */
	@Override
	public int getLayoutIdForElements() {
		return R.layout.city_day_item;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableActivity#getLayoutIdForActivity()
	 */
	@Override
	public int getLayoutIdForActivity() {
		return R.layout.city_view;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#getContextMenuHeaderTitleOrId()
	 */
	@Override
	public Object getContextMenuHeaderTitleOrId() {
		return null;
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