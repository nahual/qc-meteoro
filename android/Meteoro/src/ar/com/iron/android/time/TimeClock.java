/**
 * 02/02/2011 10:48:11 Copyright (C) 2006 Darío L. García
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
package ar.com.iron.android.time;

import android.os.SystemClock;

/**
 * Esta clase representa un reloj (o herramienta para tratar con el tiempo) de manera de abstraer
 * las operaciones necesarias para calcular tiempos transcurridos y desplazamientos en el tiempo
 * 
 * @author D. García
 */
public class TimeClock {

	private long startMoment;

	private TimeReference currentTime;

	/**
	 * Constructor que utiliza como referencia de tiempo, el {@link SystemClock#elapsedRealtime()}
	 */
	public TimeClock() {
		this.currentTime = new CurrentRealtimeReference();
		// Marcamos el inicio para inicializar el estado
		markStart();
	}

	/**
	 * Marca el momento considerado de inicio para este reloj. Los cálculos de tiempo transcurrido
	 * serán realizadas contra este instante como el momento 0
	 */
	public void markStart() {
		startMoment = getCurrentTime();
	}

	/**
	 * Devuelve el instante actual en milisegundos
	 * 
	 * @return El tiempo tomado como referecnia actual
	 */
	private long getCurrentTime() {
		return currentTime.getMoment();
	}

	/**
	 * Indica si ha transcurrido el tiempo indicado en milisegundos desde el momento marcado como
	 * inicio
	 * 
	 * @param timeInMillis
	 *            Tiempo contra el cual se hará la comparación
	 * @return true si ya transcurrieron más millis que los indicados. false si es igual o menor
	 */
	public boolean hasElapsed(long timeInMillis) {
		long elapsed = getElapsed();
		boolean olderThanReference = elapsed > timeInMillis;
		return olderThanReference;
	}

	/**
	 * Devuelve el tiempo transcurrido desde el momento marcado como 0
	 * 
	 * @return El tiempo transcurrido en millis
	 */
	public long getElapsed() {
		long elapsed = getCurrentTime() - startMoment;
		return elapsed;
	}

}
