/**
 * 27/01/2011 17:15:14 Copyright (C) 2006 Darío L. García
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

import android.database.Cursor;

/**
 * Esta clase facilita algunas operaciones con los cursores a la base simplificando el código
 * necesario para algunas operaciones comunes
 * 
 * @author D. García
 */
public class CursorHelper {

	/**
	 * Devuelve el contenido accesible a través del cursor con getString, verificando que no sea
	 * null antes. Si es null, devuelve el valor default indicado
	 * 
	 * @param columnIndex
	 *            Índice de la columna para acceder
	 * @param cursor
	 *            El cursor del cual sacar los datos
	 * @param defaultValue
	 *            Valor por defecto si es null
	 * @return El valor del registro en la columna indicada o el valor por defecto si era null
	 */
	public static String getString(int columnIndex, Cursor cursor, String defaultValue) {
		if (cursor.isNull(columnIndex)) {
			return defaultValue;
		}
		return cursor.getString(columnIndex);
	}

	/**
	 * Devuelve el contenido accesible a través del cursor con getInt, verificando que no sea null
	 * antes. Si es null, devuelve el valor default indicado
	 * 
	 * @param columnIndex
	 *            Índice de la columna para acceder
	 * @param cursor
	 *            El cursor del cual sacar los datos
	 * @param defaultValue
	 *            Valor por defecto si es null
	 * @return El valor del registro en la columna indicada o el valor por defecto si era null
	 */
	public static Integer getInteger(int columnIndex, Cursor cursor, Integer defaultValue) {
		if (cursor.isNull(columnIndex)) {
			return defaultValue;
		}
		return cursor.getInt(columnIndex);
	}

	/**
	 * Devuelve el contenido accesible a través del cursor con getLong, verificando que no sea null
	 * antes. Si es null, devuelve el valor default indicado
	 * 
	 * @param columnIndex
	 *            Índice de la columna para acceder
	 * @param cursor
	 *            El cursor del cual sacar los datos
	 * @param defaultValue
	 *            Valor por defecto si es null
	 * @return El valor del registro en la columna indicada o el valor por defecto si era null
	 */
	public static Long getLong(int columnIndex, Cursor cursor, Long defaultValue) {
		if (cursor.isNull(columnIndex)) {
			return defaultValue;
		}
		return cursor.getLong(columnIndex);
	}

	/**
	 * Devuelve el contenido accesible a través del cursor con getBlob, verificando que no sea null
	 * antes. Si es null, devuelve el valor default indicado
	 * 
	 * @param columnIndex
	 *            Índice de la columna para acceder
	 * @param cursor
	 *            El cursor del cual sacar los datos
	 * @param defaultValue
	 *            Valor por defecto si es null
	 * @return El valor del registro en la columna indicada o el valor por defecto si era null
	 */
	public static byte[] getBlob(int columnIndex, Cursor cursor, byte[] defaultValue) {
		if (cursor.isNull(columnIndex)) {
			return defaultValue;
		}
		return cursor.getBlob(columnIndex);
	}

	/**
	 * Devuelve el contenido accesible a través del cursor con getDouble, verificando que no sea
	 * null antes. Si es null, devuelve el valor default indicado
	 * 
	 * @param columnIndex
	 *            Índice de la columna para acceder
	 * @param cursor
	 *            El cursor del cual sacar los datos
	 * @param defaultValue
	 *            Valor por defecto si es null
	 * @return El valor del registro en la columna indicada o el valor por defecto si era null
	 */
	public static Double getDouble(int columnIndex, Cursor cursor, Double defaultValue) {
		if (cursor.isNull(columnIndex)) {
			return defaultValue;
		}
		return cursor.getDouble(columnIndex);
	}

}
