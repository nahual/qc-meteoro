/**
 * 01/02/2011 19:34:38 Copyright (C) 2006 Darío L. García
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA
 */
package ar.com.iron.android.location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import ar.com.iron.android.helpers.ContextHelper;
import ar.com.iron.android.time.TimeClock;
import ar.com.iron.annotations.MayBeNull;

/**
 * Esta clase representa una tarea en background que intenta determinar una ubicación actual
 * utilizando los providers de locación antes de un tiempo determinado.<br>
 * <br>
 * Esta clase intentará esperar por el GPS si hay uno disponible para determinar la ubicación más
 * exacta. Si no encuentra GPS utilizará la ubicación de cualquier otro proveedor (teniendo mayor
 * prioridad la red).<br>
 * Si se agota el tiempo máximo de espera se intentará obtener la mejor última localización (GPS,
 * NETWORK, otros providers). Si finalmente no encuentra ninguna localización, se determina null
 * como {@link Location} actual. <br>
 * <br>
 * La forma normal de uso de esta clase es invocándola directamente, o subclaseandola para
 * implementar comportamiento especial durante la detección.<br>
 * Si el momento de ejecutar esta tarea no existe ningún proveedor activo, se permite realizar algún
 * comportamiento especial extendiendo esta clase.<br>
 * <br>
 * Esta tarea debe ser creada y ejecutada en el thread principal para recibir los updates de los
 * providers. Se utiliza este thread para esperar los updates y revisar si llegaron cada cierto
 * tiempo.
 * 
 * @author D. García
 */
public class DetectLocationBgTask {

	/**
	 * Tiempo máximo para adquirir la ubicación (1 min)
	 */
	public static final long DEFAULT_TASK_TIMEOUT = 1 * 60 * 1000;

	/**
	 * Tiempo de espera entre chequeos para ver si se recibió un update
	 */
	public static final long DEFAULT_CHECK_INTERVAL = 500;

	/**
	 * Cantidad de tiempo máxima que se puede consumir en esta tarea (en millis)
	 */
	private long taskTimeout;

	/**
	 * Cantidad de tiempo que se espera entre chequeos por la nueva locación
	 */
	private long checkInterval;

	private OnLocationFoundListener listener;

	/**
	 * Manager para pedir la posición actual
	 */
	private LocationManager locationManager;

	/**
	 * Handler utilizado para esperar los updates
	 */
	private Handler threadHandler;

	/**
	 * Usado para calcular los momentos de chequeo
	 */
	private TimeClock clock;

	/**
	 * Locaciones recibidas indexadas por provider
	 */
	private Map<String, Location> receivedLocations;

	private boolean taskEnded;

	private LocationListener locationListener = new LocationListener() {
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// No nos interesan los cambios de estado
		}

		public void onProviderEnabled(String provider) {
			// No lo registramos
		}

		public void onProviderDisabled(String provider) {
			// No lo consideramos

		}

		public void onLocationChanged(Location location) {
			onLocationReceived(location);
		}
	};

	/**
	 * Tarea que verifica si se acabó el tiempo de espera a intervalos regulares
	 */
	private Runnable chequearVencimientoTimeout = new Runnable() {
		public void run() {
			onTimeoutCheck();
		}
	};

	public DetectLocationBgTask(Context contexto) {
		locationManager = ContextHelper.getLocationManager(contexto);
		threadHandler = new Handler();
		taskTimeout = DEFAULT_TASK_TIMEOUT;
		checkInterval = DEFAULT_CHECK_INTERVAL;
		clock = new TimeClock();
		// Lo creamos con 3 lugares que probablemente no hagan falta mas
		receivedLocations = new HashMap<String, Location>(3);
		taskEnded = false;
	}

	/**
	 * Invocado cuando cualquiera de los providers notifica a su listener con una nueva ubicación
	 * 
	 * @param location
	 */
	protected void onLocationReceived(Location location) {
		String provider = location.getProvider();
		receivedLocations.put(provider, location);
		if (LocationManager.GPS_PROVIDER.equals(provider)) {
			// Obtuvimos posición de GPS, terminamos
			finishNow();
		}
		// Si el GPS no está activado no lo esperamos, terminamos con la primera locación que
		// conseguimos
		boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if (!isGpsEnabled) {
			finishNow();
		}

	}

	public OnLocationFoundListener getListener() {
		return listener;
	}

	public void setListener(OnLocationFoundListener listener) {
		this.listener = listener;
	}

	/**
	 * Ejecutado antes de comenzar el proceso de detección de la locación
	 */
	protected void onPreExecute() {
	}

	/**
	 * Ejecutado después de determinar la ubicación del dispositivo
	 * 
	 * @param currentLocation
	 *            La ubicación actual. Puede ser null si se acabó el tiempo o no habían providers
	 */
	protected void onPostExecute(@MayBeNull Location currentLocation) {
	}

	/**
	 * Invocado cuando al ejecutar esta tarea no existe ningún proveedor del cuál obtener una
	 * ubicación actualizada.<br>
	 * La locación puede ser obtenida desde la última conocida aunque no existan proveedores activos
	 */
	protected void onNoneProviderEnabled() {

	}

	/**
	 * Ejecuta esta tarea pidiendo actualización en background de la locación. Si transcurrido el
	 * tiempo límite no se obtuvo una se usa la última conocida, o en el peor de los casos null.<br>
	 * <br>
	 * Esta tarea esperará hasta obtener la ubicación del GPS (sólo si está activado), y utilizará
	 * la próxima más exacta si no lo consigue
	 */
	public void executeInBg() {
		// Registramos el inicio de todo
		clock.markStart();
		onPreExecute();

		checkEnabledProviders();
		registerLocationListeners();
		checkTaskTimeoutLater();
	}

	public long getCheckInterval() {
		return checkInterval;
	}

	/**
	 * Agrega una tarea a la cola de mensajes del thread actual para verificar si se agotó el tiempo
	 * de espera definido
	 */
	private void checkTaskTimeoutLater() {
		long esperaDeChequeo = getCheckInterval();
		threadHandler.postDelayed(chequearVencimientoTimeout, esperaDeChequeo);
	}

	public long getTaskTimeout() {
		return taskTimeout;
	}

	public void setTaskTimeout(long taskTimeout) {
		this.taskTimeout = taskTimeout;
	}

	public void setCheckInterval(long checkInterval) {
		this.checkInterval = checkInterval;
	}

	/**
	 * Invocado regularmente para controlar si se recibieron nuevas locaciones
	 */
	protected void onTimeoutCheck() {
		long esperaMaxima = getTaskTimeout();
		if (!clock.hasElapsed(esperaMaxima)) {
			// Todavía no llegamos al deadline
			checkTaskTimeoutLater();
			return;
		}
		// Se acabó el tiempo de espera, debemos terminar
		finishNow();
	}

	/**
	 * Termina la tarea actual utilizando la mejor locación disponible
	 */
	public void finishNow() {
		if (isTaskEnded()) {
			// Si ya terminamos, no ejecutamos todo nuevamente
			// Este chequeo es por si quedan listeners activos después de terminado
			return;
		}
		taskEnded = true;

		unregisterLocationListeners();
		// Quitamos la tarea de chequeo en caso de que esté pendiente
		threadHandler.removeCallbacks(chequearVencimientoTimeout);
		Location location = getBestLocationAvailable();
		onPostExecute(location);
		if (listener != null) {
			listener.onLocationFound(location);
		}
	}

	/**
	 * Devuelve la mejor locación encontrada por esta tarea al momento.<br>
	 * Si la tarea está en progreso es posible que encuentre mejores locaciones después.<br>
	 * Puede consultarse si la tarea ya terminó con {@link #isTaskEnded()}
	 * 
	 * @return La mejor locación determinable con el estado actual de actualizaciones de los
	 *         proveedores de locación
	 */
	public Location getBestLocationAvailable() {
		Location gpsLocation = getReceivedGpsLocation();
		if (gpsLocation != null) {
			return gpsLocation;
		}
		Location networkLocation = getReceivedNetworkLocation();
		if (networkLocation != null) {
			return networkLocation;
		}
		Location otherProviderLocation = getOtherProviderReceivedLocation();
		if (otherProviderLocation != null) {
			return otherProviderLocation;
		}
		return getBestLastKnownLocation(locationManager);
	}

	/**
	 * Devuelve la locación recibida de otro provider que no es GPS, ni Network.<br>
	 * Se devuelve la primera encontrada de las recibidas
	 * 
	 * @return La locación recibida o null si no se recibió ninguna
	 */
	private Location getOtherProviderReceivedLocation() {
		Set<Entry<String, Location>> entrySet = receivedLocations.entrySet();
		for (Entry<String, Location> entry : entrySet) {
			String provider = entry.getKey();
			if (LocationManager.GPS_PROVIDER.equals(provider)) {
				// No nos interesa la del GPS
				continue;
			}
			if (LocationManager.NETWORK_PROVIDER.equals(provider)) {
				// No nos interesa la del network
				continue;
			}
			// Esta sí nos interesa
			Location otherLocation = entry.getValue();
			return otherLocation;
		}
		return null;
	}

	/**
	 * Devuelve la mejor locación de las últimas conocidas.<br>
	 * Este método le da prioridad al GPS, luego a las redes y finalmente a cualquiera
	 * 
	 * @return La última locación conocida, o null si no se pudo recuperar ninguna
	 */
	public static Location getBestLastKnownLocation(LocationManager locationManager) {
		// Intentamos con GPS
		Location lastGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		if (lastGpsLocation != null) {
			return lastGpsLocation;
		}
		// Luego con network
		Location lastNetworkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (lastNetworkLocation != null) {
			return lastNetworkLocation;
		}
		// Con el resto
		List<String> otherProviders = locationManager.getAllProviders();
		otherProviders.remove(LocationManager.GPS_PROVIDER);
		otherProviders.remove(LocationManager.NETWORK_PROVIDER);

		for (String otherProvider : otherProviders) {
			Location lastKnownLocation = locationManager.getLastKnownLocation(otherProvider);
			if (lastKnownLocation != null) {
				return lastKnownLocation;
			}
		}
		return null;
	}

	/**
	 * Devuelve la locación determinada con el provider de redes
	 * 
	 * @return La locación recibida o null si no se recibió ninguna
	 */
	private Location getReceivedNetworkLocation() {
		return receivedLocations.get(LocationManager.NETWORK_PROVIDER);
	}

	/**
	 * Devuelve la locación devuelta por el GPS si es que hay una
	 * 
	 * @return la locación que indicó el provider GPS o null si no se recibió ninguna
	 */
	public Location getReceivedGpsLocation() {
		return receivedLocations.get(LocationManager.GPS_PROVIDER);
	}

	/**
	 * Registra el listener de la locación para recibir las actualizaciones de posición en todos los
	 * providers disponibles. Se guarda la lista de los providers en los cuales se registró
	 */
	protected void registerLocationListeners() {
		List<String> allProviders = locationManager.getAllProviders();
		for (String provider : allProviders) {
			locationManager.requestLocationUpdates(provider, 0, 0, locationListener);
		}
	}

	/**
	 * Desregistra el listener de todos los providers, de manera de no recibir más actualizaciones
	 */
	protected void unregisterLocationListeners() {
		locationManager.removeUpdates(locationListener);
	}

	/**
	 * Verifica si existen providers habilitados
	 */
	private void checkEnabledProviders() {
		List<String> enabledProviders = locationManager.getProviders(true);
		if (enabledProviders.size() == 0) {
			onNoneProviderEnabled();
		}
	}

	/**
	 * Indica si esta tarea ya terminó. Sea por que se obtuvo una locación, o por que se acabó el
	 * tiempo de espera
	 * 
	 * @return true si esta tarea ya no esperará nuevas actualizaciones
	 */
	public boolean isTaskEnded() {
		return taskEnded;
	}

}
