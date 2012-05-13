/**
 * 01/02/2011 19:41:46 Copyright (C) 2006 Darío L. García
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

import android.location.Location;
import ar.com.iron.annotations.MayBeNull;

/**
 * Esta interfaz define el método que debe implementar un listener de la locación que desea ser
 * invocado al determinar la locación actual
 * 
 * @author D. García
 */
public interface OnLocationFoundListener {

	/**
	 * Invocado al determinarse la ubicación actual. <br>
	 * La ubicación pasada puede ser la última conocida o null si no hubo forma de determinar la
	 * ubicación actual después del tiempo máximo
	 * 
	 * @param currentLocation
	 *            Locación actual del dispositivo, una anterior, o null
	 */
	public void onLocationFound(@MayBeNull Location currentLocation);

}
