/**
 * 27/01/2011 16:19:13 Copyright (C) 2006 Darío L. García
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
package ar.com.iron.helpers;

/**
 * Esta clase permite comparar con equals tomando en cuenta el null
 * 
 * @author D. García
 */
public class EqualHelper {

	/**
	 * Indica si los valores pasados sin iguales tomando en cuenta que cualquiera puede ser null en
	 * al comparación. En cuyo caso serán iguales si ambos son null
	 * 
	 * @return true si son considerables iguales
	 */
	public static boolean eq(Object valor1, Object valor2) {
		if (valor1 == null) {
			return valor2 == null;
		}
		return valor1.equals(valor2);
	}

}
