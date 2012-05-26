package ar.nahual.meteoro;

import java.util.ArrayList;
import java.util.Collections;
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
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ContextMenuItem;
import ar.com.iron.persistence.DefaultOnFailurePersistenceOperationListener;
import ar.com.iron.persistence.PersistenceDao;
import ar.com.iron.persistence.PersistenceService;
import ar.com.iron.persistence.db4o.filters.AllInstancesFilter;
import ar.nahual.meteoro.model.CiudadPersistida;
import ar.nahual.meteoro.model.Pronostico;

public class VerCiudadActivity extends CustomListActivity<Pronostico> {

	public static final String CITY_ID = "CITY_ID";
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
		this.ciudades.clear();
		this.ciudades.addAll(result);
		if (ciudades.isEmpty()) {
			startActivityForResult(new Intent(this, AgregarCiudadActivity.class), 1);
		} else {
			mostrarLaCiudad(ciudades.get(0).getId());
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		mostrarLaCiudad(data.getLongExtra(VerCiudadActivity.CITY_ID, -1l));
	}

	/**
	 * Muestra en la pantalla los datos de la ciudad actual
	 * 
	 * @param ciudadPersistida
	 */
	private void mostrarLaCiudad(final Long idDeCiudad) {
		persistenceDao.getOf(CiudadPersistida.class, idDeCiudad, new DefaultOnFailurePersistenceOperationListener<CiudadPersistida>(getContext()) {
			@Override
			public void onSuccess(CiudadPersistida result) {
				ciudadActual = result;
				ViewHelper.findTextView(R.id.nombreCiudad_txt, getContentView()).setText(ciudadActual.getCityName());
				final RequestForecastTask requestForecastTask = new RequestForecastTask(VerCiudadActivity.this);
				requestForecastTask.execute(ciudadActual);			
			}
		}); 
	}

	protected void onPronosticoDisponible() {
		ViewHelper.findTextView(R.id.nombreCiudad_txt, getContentView()).setText(ciudadActual.getCityName());
		ViewHelper.findTextView(R.id.temperaturaCiudad_txt, getContentView()).setText(ciudadActual.getActual().getTemperature());
		this.notificarCambioEnLosDatos();
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getContextMenuItems()
	 */
	@Override
	public ContextMenuItem<? extends CustomableListActivity<Pronostico>, Pronostico>[] getContextMenuItems() {
		return null;
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementList()
	 */
	@Override
	public List<Pronostico> getElementList() {
		if (ciudadActual != null && ciudadActual.getFuturos() != null && ciudadActual.getFuturos().size() != 0) {
			return ciudadActual.getFuturos();
		} else {
			return Collections.nCopies(4, new Pronostico());
		}
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.model.CustomableListActivity#getElementRenderBlock()
	 */
	@Override
	public RenderBlock<Pronostico> getElementRenderBlock() {
		return new RenderBlock<Pronostico>() {
			@Override
			public void render(final View itemView, final Pronostico item, final LayoutInflater inflater) {
				final TextView diaPronosticoText = ViewHelper.findTextView(R.id.diaPronostico_txt, itemView);
				diaPronosticoText.setText(item.getDate());
				final TextView tempPronostico = ViewHelper.findTextView(R.id.tempPronostico_txt, itemView);
				tempPronostico.setText(String.valueOf(item.getTemperature()));
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