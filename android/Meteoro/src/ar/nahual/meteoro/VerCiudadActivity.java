package ar.nahual.meteoro;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import ar.com.iron.android.extensions.activities.CustomListActivity;
import ar.com.iron.android.extensions.activities.model.CustomableListActivity;
import ar.com.iron.android.extensions.adapters.RenderBlock;
import ar.com.iron.android.extensions.services.local.LocalServiceConnectionListener;
import ar.com.iron.android.extensions.services.local.LocalServiceConnector;
import ar.com.iron.helpers.ToastHelper;
import ar.com.iron.helpers.ViewHelper;
import ar.com.iron.menues.ActivityMenuItem;
import ar.com.iron.menues.ContextMenuItem;
import ar.com.iron.persistence.DefaultOnFailurePersistenceOperationListener;
import ar.com.iron.persistence.PersistenceDao;
import ar.com.iron.persistence.PersistenceService;
import ar.com.iron.persistence.db4o.filters.AllInstancesFilter;
import ar.nahual.meteoro.model.CiudadPersistida;
import ar.nahual.meteoro.model.Pronostico;

public class VerCiudadActivity extends CustomListActivity<Pronostico> {

	public static int CHOOSE_FIRST_CITY = 1;
	public static int CHOOSE_OTHER_CITY = 2;
	public static final Map<String, Integer> ICONOS_ESTADO = new HashMap<String, Integer>();

	private final Collection<CiudadPersistida> ciudades = new LinkedHashSet<CiudadPersistida>();
	private CiudadPersistida ciudadActual;

	private LocalServiceConnector<PersistenceDao> persistenceConector;
	private PersistenceDao persistenceDao;

	private static Integer getIconoEstado(final String estado) {
		if (ICONOS_ESTADO.isEmpty()) {
			ICONOS_ESTADO.put("cloudy", R.drawable.status_cloudy);
			ICONOS_ESTADO.put("partly cloudy", R.drawable.status_partly_cloudy);
			ICONOS_ESTADO.put("clear", R.drawable.status_clear);
			ICONOS_ESTADO.put("showers", R.drawable.status_showers);
			ICONOS_ESTADO.put("fog", R.drawable.status_fog);
			ICONOS_ESTADO.put("scattered showers", R.drawable.status_scattered_showers);
			ICONOS_ESTADO.put("windy", R.drawable.status_windy);
			ICONOS_ESTADO.put("snow", R.drawable.status_snow);
			ICONOS_ESTADO.put("storm", R.drawable.status_storm);
			ICONOS_ESTADO.put("snow rain", R.drawable.status_snow_rain);
		}
		final String normalizado = estado.toLowerCase();

		final Integer iconoPredefinido = ICONOS_ESTADO.get(normalizado);
		if (iconoPredefinido != null) {
			return iconoPredefinido;
		}
		return R.drawable.status_unknown;
	}

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
		mostrarLaPrimeraCiudadDisponible();
	}

	/**
	 * Muestra la priemra ciudad disponible en la base o pide una nueva
	 */
	private void mostrarLaPrimeraCiudadDisponible() {
		final AllInstancesFilter todasLasCiudades = new AllInstancesFilter(CiudadPersistida.class);
		persistenceDao.findAllMatching(todasLasCiudades,
				new DefaultOnFailurePersistenceOperationListener<List<CiudadPersistida>>(getContext()) {
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
			startActivityForResult(new Intent(this, AgregarCiudadActivity.class), CHOOSE_FIRST_CITY);
		} else {
			mostrarLaCiudad(ciudades.iterator().next().getId());
		}
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		if (resultCode != RESULT_OK) {
			if (requestCode == CHOOSE_FIRST_CITY) {
				// Cancelo la seleccion de ciudad en la primera vez. Salimos
				finish();
			}
			return;
		}
		final long cityId = data.getLongExtra(AgregarCiudadActivity.SELECTED_CITY, -1);
		if (cityId == -1) {
			ToastHelper.create(getContext()).showShort("El ID de ciudad es invalido");
			finish();
			return;
		}
		mostrarLaCiudad(cityId);
	}

	/**
	 * Muestra en la pantalla los datos de la ciudad actual
	 * 
	 * @param ciudadPersistida
	 */
	private void mostrarLaCiudad(final Long idDeCiudad) {
		persistenceDao.getOf(CiudadPersistida.class, idDeCiudad,
				new DefaultOnFailurePersistenceOperationListener<CiudadPersistida>(getContext()) {
					@Override
					public void onSuccess(final CiudadPersistida result) {
					    ciudades.add(result);
						ciudadActual = result;
						actualizarVistaDeCiudad();
						cargarPronosticoDelBackend();
					}
				});
	}

	/**
	 * Actualiza la vista de la ciudad con los datos disponibles actualmente
	 */
	protected void actualizarVistaDeCiudad() {
		ViewHelper.findTextView(R.id.nombreCiudad_txt, getContentView()).setText(ciudadActual.getCityName());
		final Pronostico estadoActual = ciudadActual.getActual();
		Integer iconoDeEstado = R.drawable.status_unknown;
		String temperature = getString(R.string.placeholder_not_available);
		String humedad = getString(R.string.placeholder_not_available);
		String pressure = getString(R.string.placeholder_not_available);
		String actualizado = getString(R.string.label_last_updated);
		if (estadoActual != null) {
			iconoDeEstado = getIconoEstado(estadoActual.getStatus());
			temperature = estadoActual.getTemperature();
			humedad = estadoActual.getHumidity();
			pressure = estadoActual.getPressure();
			// Haciendo que la fecha ultimo update siempre aparezca como 9:00 del dia actual
			final Calendar ultimoUpdate = GregorianCalendar.getInstance();
			ultimoUpdate.set(Calendar.HOUR_OF_DAY,9);
			ultimoUpdate.clear(Calendar.MINUTE);
			ultimoUpdate.clear(Calendar.SECOND);
			actualizado += ultimoUpdate.getTime().toLocaleString();
		}
		final ImageView img = ViewHelper.findImageView(R.id.estado_img, getContentView());
		img.setImageResource(iconoDeEstado);
		ViewHelper.findTextView(R.id.temperatura_txt, getContentView()).setText(temperature);
		ViewHelper.findTextView(R.id.humedad_txt, getContentView()).setText(humedad);
		ViewHelper.findTextView(R.id.pressure_txt, getContentView()).setText(pressure);
		ViewHelper.findTextView(R.id.ultimoUpdate_txt, getContentView()).setText(actualizado);
		this.notificarCambioEnLosDatos();
	}

	/**
	 * Intenta solicitar al backend la info de pronostico de la ciudad actual
	 * 
	 */
	protected void cargarPronosticoDelBackend() {
		// Solo actualizamos si es necesario
		if (ciudadActual != null && !ciudadActual.tienePronosticoActualizado()) {
			// Disparamos el pedido al backend para actualizar los datos
			mostrarSpinnerDeLoading();
			final RequestForecastTask requestForecastTask = new RequestForecastTask(VerCiudadActivity.this);
			requestForecastTask.execute(ciudadActual);
		}
	}

	protected void onPronosticoDisponible() {
		// Ocultamos el spinner de progreso
		ocultarSpinnerDeLoading();
		actualizarVistaDeCiudad();
	}

	protected void mostrarSpinnerDeLoading() {
		final ProgressBar progressBar = ViewHelper.findProgressBar(R.id.loading_prg, getContentView());
		progressBar.setVisibility(View.VISIBLE);
	}

	private void ocultarSpinnerDeLoading() {
		final ProgressBar progressBar = ViewHelper.findProgressBar(R.id.loading_prg, getContentView());
		progressBar.setVisibility(View.GONE);
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
				diaPronosticoText.setText(diaRelativo(item.getDate()));
				final ImageView img = ViewHelper.findImageView(R.id.estadoPronostico_img, itemView);
				img.setImageResource(getIconoEstado(item.getStatus()));
				final TextView minTempPronostico = ViewHelper.findTextView(R.id.minTempPronostico_txt, itemView);
				minTempPronostico.setText(String.valueOf(item.getMin()));
				final TextView maxTempPronostico = ViewHelper.findTextView(R.id.maxTempPronostico_txt, itemView);
				maxTempPronostico.setText(String.valueOf(item.getMax()));
			}
		};
	}

	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	public static String diaRelativo(final String strDate) {
	    try {
    	    final Calendar today = GregorianCalendar.getInstance();
    	    final Calendar date = GregorianCalendar.getInstance();
    	    date.setTime(formatter.parse(strDate));
    	    if (date.get(Calendar.DAY_OF_WEEK) == today.get(Calendar.DAY_OF_WEEK)) {
    	        return "hoy";
    	    } else if (date.get(Calendar.DAY_OF_WEEK) == today.get(Calendar.DAY_OF_WEEK)+1 || date.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY && today.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
    	        return "mañana";
    	    }
    	    return DateFormat.format("EEEE", date).toString();
	    } catch (Exception e) {
	        return strDate;
	    }
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
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		startService(new Intent(this, PronosticoUpdateService.class));
	}

	/**
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		stopService(new Intent(this, PronosticoUpdateService.class));
		super.onStop();
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#getMenuItems()
	 */
	@Override
	public ActivityMenuItem<? extends CustomListActivity<Pronostico>>[] getMenuItems() {
	    VerCiudadMenu.SIGUIENTE.setEnabled(ciudades.size() > 1);
		return VerCiudadMenu.values();
	}

	/**
	 * Invocado cuando se elige del menu la opcion para agregar una ciudad
	 */
	public void onAgregarCiudadSelected() {
		startActivityForResult(new Intent(this, AgregarCiudadActivity.class), CHOOSE_OTHER_CITY);
	}

	/**
	 * Invocado cuando el usuario decide borrar la ciudad actual
	 */
	public void borrarCiudad() {
		persistenceDao.delete(ciudadActual, new DefaultOnFailurePersistenceOperationListener<CiudadPersistida>(
				getContext()) {
			@Override
			public void onSuccess(final CiudadPersistida result) {
				onCiudadBorrada();
			}
		});
	}

	/**
	 * Invocado cuando la ciudad actual fue borrada
	 */
	protected void onCiudadBorrada() {
		mostrarLaPrimeraCiudadDisponible();
	}

	/**
	 * Pasa a la siguiente ciudad de las disponibles
	 */
	public void cambiarDeCiudad() {
		persistenceDao.findAllMatching(new AllInstancesFilter(CiudadPersistida.class),
				new DefaultOnFailurePersistenceOperationListener<List<CiudadPersistida>>(getContext()) {
					@Override
					public void onSuccess(final List<CiudadPersistida> result) {
						onCiudadesDisponiblesParaCambio(result);
					}
				});
	}

	/**
	 * Invocado al disponer de ciudades para cambio
	 * 
	 * @param result
	 */
	protected void onCiudadesDisponiblesParaCambio(final List<CiudadPersistida> result) {
		if (result.size() < 2) {
			// No hay 2 o mas ciudades para cambiar, no tiene sentido seguir
			return;
		}
		int ciudadMostradaIndex = -1;
		for (int i = 0; i < result.size(); i++) {
			final CiudadPersistida ciudadDeReferencia = result.get(i);
			if (ciudadDeReferencia.getId().equals(ciudadActual.getId())) {
				ciudadMostradaIndex = i;
				break;
			}
		}
		// Haciendo que se muestre la ciudad anterior, en vez de la siguiente
		int proximaCiudad = ciudadMostradaIndex - 1;
		if (proximaCiudad < 0) {
			proximaCiudad = result.size() - 1;
		}
		final CiudadPersistida ciudadAmostrar = result.get(proximaCiudad);
		mostrarLaCiudad(ciudadAmostrar.getId());
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#initMessageReceivers()
	 */
	@Override
	public void initMessageReceivers() {
		// Escuchamos pedidos de actualizacion de mensajes
		registerMessageReceiver(ActualizarPronostico.ACTION, new BroadcastReceiver() {
			@Override
			public void onReceive(final Context context, final Intent intent) {
				cargarPronosticoDelBackend();
			}
		});
	}

	/**
	 * @see ar.com.iron.android.extensions.activities.CustomListActivity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		persistenceConector.unbindFromService(this);
		super.onDestroy();
	}
}
