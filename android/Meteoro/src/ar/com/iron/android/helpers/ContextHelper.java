/**
 * 28/01/2011 12:52:11 Copyright (C) 2006 Darío L. García
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
package ar.com.iron.android.helpers;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.PowerManager;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;

/**
 * Esta clase brinda algunos métodos para simplificar el acceso a elementos del contexto.<br>
 * Facilita recordar cómo se hacían algunas cosas específicas de la plataforma.
 * 
 * @author D. García
 */
public class ContextHelper {

	/**
	 * Devuelve el administrador de notificaciones de estado del sistema, con el cuál se pueden
	 * mostrar mensajes de estado en background
	 * 
	 * @param contexto
	 *            Contexto actual de la app
	 * @return El manager de las notificaciones
	 */
	public static NotificationManager getNotificationManager(final Context contexto) {
		return getSystemManager(contexto, Context.NOTIFICATION_SERVICE, NotificationManager.class);
	}

	/**
	 * Devuelve el administrador de alarmas del sistema, con el cuál se pueden ejecutar tareas por
	 * fuera del ciclo de vida de la aplicación
	 * 
	 * @param contexto
	 *            Contexto actual de la app
	 * @return El manager de las alarmas
	 */
	public static AlarmManager getAlarmManager(final Context contexto) {
		return getSystemManager(contexto, Context.ALARM_SERVICE, AlarmManager.class);
	}

	/**
	 * Devuelve el administrador de conexiones de datos del sistema con el cual se puede conocer el
	 * estado actual, el tipo de conexión y si hay más opciones
	 * 
	 * @param contexto
	 *            El contexto de android
	 * @return El manager para las conexiones
	 */
	public static ConnectivityManager getConnectivityManager(final Context contexto) {
		return getSystemManager(contexto, Context.CONNECTIVITY_SERVICE, ConnectivityManager.class);
	}

	/**
	 * Devuelve el administrador de inputs del sistema, con el cuál se pueden ocultar o mostrar el
	 * teclado virtual
	 * 
	 * @param contexto
	 *            Contexto actual de la app
	 * @return El manager del input
	 */
	public static InputMethodManager getKeyboardManager(final Context contexto) {
		return getSystemManager(contexto, Context.INPUT_METHOD_SERVICE, InputMethodManager.class);
	}

	/**
	 * Devuelve el administrador de posiciones y GPS del sistema, con el cuál se puede interactuar y
	 * conocer la posición actual del dispositivo
	 * 
	 * @param contexto
	 *            Contexto de la app
	 * @return El manager de locación geográfica
	 */
	public static LocationManager getLocationManager(final Context contexto) {
		return getSystemManager(contexto, Context.LOCATION_SERVICE, LocationManager.class);
	}

	/**
	 * Devuelve el administrador de las opciones de energía del dispositivo, con el cuál se puede
	 * mantener activo el dispositivo
	 * 
	 * @param contexto
	 *            Contexto de la app
	 * @return El manager de la energía
	 */
	public static PowerManager getPowerManager(final Context contexto) {
		return getSystemManager(contexto, Context.POWER_SERVICE, PowerManager.class);
	}

	/**
	 * Devuelve el generador de vistas a partir de layouts en XML
	 * 
	 * @param contexto
	 *            El contexto desde el cuál tomar el inflater
	 * @return El generador de layouts
	 */
	public static LayoutInflater getLayoutInflater(final Context contexto) {
		return getSystemManager(contexto, Context.LAYOUT_INFLATER_SERVICE, LayoutInflater.class);
	}

	/**
	 * Devuelve el servicio del sistema indicado casteándolo a la clase necesaria y verificando que
	 * no cambie de implementación.<br>
	 * Se produce una {@link RuntimeException} si el servicio no existe o no es de la clase esperada
	 * 
	 * @param contexto
	 *            Contexto del cual tomar el servicio
	 * @param serviceName
	 *            El nombre del servicio buscado
	 * @param expectedType
	 *            Tipo esperado de la invocación
	 * @return El servicio de sistema
	 */
	@SuppressWarnings("unchecked")
	private static <T> T getSystemManager(final Context contexto, final String serviceName, final Class<T> expectedType) {
		final Object systemService = contexto.getSystemService(serviceName);
		if (systemService == null) {
			throw new RuntimeException("El dispositivo no cuenta con el servicio de sistema pedido: " + serviceName);
		}
		final Class<? extends Object> serviceType = systemService.getClass();
		if (!expectedType.isAssignableFrom(serviceType)) {
			throw new RuntimeException("El servicio[" + serviceType + "] no es del tipo esperado[" + expectedType + "]");
		}
		return (T) systemService;
	}

	/**
	 * Convierte la medida pasada en pixels dp, a píxeles reales utilizando el contexto
	 * 
	 * @param contexto
	 *            Contexto del dispositivo
	 * @param dps
	 *            Medida independiente de la densidad
	 * @return La cantidad de píxeles reales utilizados para representar la medida en dps
	 */
	public static int convertDpToPixels(final Context contexto, final int dps) {
		final Resources resources = contexto.getResources();
		final float scale = resources.getDisplayMetrics().density;
		final int pixels = (int) (dps * scale);
		return pixels;
	}

}
