/**
 * 22/01/2011 19:05:27 Copyright (C) 2006 Darío L. García
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

import java.util.Date;
import java.util.Formatter;

import android.content.Context;
import android.text.TextUtils;

/**
 * Esta clase ofrece métodos comunes para tratar cadenas
 * 
 * @author D. García
 */
public class StringHelper {

	/**
	 * Indica si el texto pasado representa un valor de filtro utilizable.<br>
	 * No es utilizable si es null, está vacia o compuesta de espacios
	 * 
	 * @param testedText
	 *            Texto a comprobar
	 * @return false si la cadena no puede utilizarse para fitrar
	 */
	public static boolean isFilterText(CharSequence testedText) {
		boolean estaVacia = TextUtils.isEmpty(testedText);
		return !estaVacia && TextUtils.getTrimmedLength(testedText) > 0;
	}

	/**
	 * Convierte el monto expresado como double a Cadena con precision de 2 decimales
	 */
	public static CharSequence convertirDecimal(Double monto) {
		if (monto == null) {
			return "0.00";
		}
		StringBuilder builder = new StringBuilder();
		Formatter formatter = new Formatter(builder);
		formatter.format("%.2f", monto);

		CharSequence precioString = builder.toString();
		return precioString;
	}

	/**
	 * Convierte la fecha pasada en el formato d/m/a h:m
	 * 
	 * @param fecha
	 *            Fecha a representar
	 * @return La cadena vacia si la fecha es nula
	 */
	public static CharSequence convertirFechaCompleta(Date fecha) {
		if (fecha == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		Formatter formatter = new Formatter(builder);
		formatter.format("%td/%tm/%tY %tH:%tM", fecha, fecha, fecha, fecha, fecha);
		CharSequence fechaString = builder.toString();
		return fechaString;
	}

	/**
	 * Convierte la fecha pasada en el formato d/m/a
	 * 
	 * @param fecha
	 *            Fecha a representar
	 * @return La cadena vacia si la fecha es nula
	 */
	public static CharSequence convertirSoloFecha(Date fecha) {
		if (fecha == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		Formatter formatter = new Formatter(builder);
		formatter.format("%td/%tm/%tY", fecha, fecha, fecha);
		CharSequence fechaString = builder.toString();
		return fechaString;
	}

	/**
	 * Convierte la fecha pasada en el formato HH:mm
	 * 
	 * @param fecha
	 *            Fecha a representar
	 * @return La cadena vacía si la fecha es nula
	 */
	public static CharSequence convertirSoloHora(Date fecha) {
		if (fecha == null) {
			return "";
		}
		StringBuilder builder = new StringBuilder();
		Formatter formatter = new Formatter(builder);
		formatter.format("%tH:%tM", fecha, fecha);
		CharSequence fechaString = builder.toString();
		return fechaString;
	}

	/**
	 * Convierte el valor pasado a una cadena que expresa distancia como metros o Km.<br>
	 * Si el valor es menor a 1000, será expresado en metros. Ej: 980 m. Si es mayor a 1000 será con
	 * Km.
	 * 
	 * @param distanciaActual
	 *            Distancia a mostrar
	 * @return La cadena formateada o la cadena vacia si se pasa null
	 */
	public static CharSequence convertirDistancia(Double distanciaActual) {
		if (distanciaActual == null) {
			return "";
		}
		double valor = distanciaActual;
		String unidad = "m";
		if (valor > 1000) {
			valor = valor / 1000;
			unidad = "Km";
		}

		StringBuilder builder = new StringBuilder();
		Formatter formatter = new Formatter(builder);
		formatter.format("%.2f %s", valor, unidad);
		CharSequence fechaString = builder.toString();
		return fechaString;
	}

	/**
	 * Crea una cadena con uniendo las partes con el separador indicado, evitando los null (se
	 * saltea el elemento).<br>
	 * Por ejemplo: joinWithoutNull(", ", "1",null,"2" )<br>
	 * da como resultado: "1, 2"
	 * 
	 * @param separador
	 *            Cadena para meter entre partes validas
	 * @param partes
	 *            Array que indica los objetos a convertir en String (se usa el String.valueOf())
	 * @return La cadena con la union de los textos
	 */
	public static String joinWithoutNull(String separador, Object... partes) {
		StringBuilder builder = new StringBuilder();
		boolean firstTime = true;
		for (Object parte : partes) {
			if (parte == null) {
				continue;
			}
			if (firstTime) {
				firstTime = false;
			} else {
				builder.append(separador);
			}
			String parteText = String.valueOf(parte);
			builder.append(parteText);
		}
		return builder.toString();
	}

	/**
	 * Método común para iniciar el toString de una clase.<br>
	 * Antepone el nombre corto de la clase
	 * 
	 * @param instance
	 *            Instancia para la que se quiere realizar el toString()
	 * @return El builder para continuar agregando estado
	 */
	public static StringBuilder toString(Object instance) {
		Class<? extends Object> instanceClass = instance.getClass();
		StringBuilder builder = new StringBuilder(instanceClass.getSimpleName());
		return builder;
	}

	/**
	 * Convierte el valor pasado en un entero, rellenando con ceros a la izquierda si sus dígitos
	 * son menores a la cantidad de ceros indicada
	 * 
	 * @param currentValue
	 *            Valor a rellenar
	 * @param cantidadCeros
	 *            La cantidad de dígitos que debe tener el número
	 * @return El número formateado con los ceros indicados
	 */
	public static String padInteger(Number currentValue, int cantidadCeros) {
		StringBuilder builder = new StringBuilder();
		Formatter formatter = new Formatter(builder);
		formatter.format("%0" + cantidadCeros + "d", currentValue.intValue());
		String padded = builder.toString();
		return padded;
	}

	/**
	 * Procesa el texto de un recurso indicado por ID reemplazando las ocurrencias de ^1 ^2, etc por
	 * los argumentos pasados.<br>
	 * El texto "^1" tomará el valor del primero argumento, "^2" del segundo, y así hasta "^9" como
	 * máximo.
	 * 
	 * @param stringResourceId
	 *            Identificador del recurso string
	 * @param context
	 *            El contexto del cual obtenerlo
	 * @param args
	 *            Array de argumentos opcionales tomados del contexto
	 * @return El texto tomado por ID, y reemplazadas sus variables
	 */
	public static CharSequence withArguments(int stringResourceId, Context context, CharSequence... args) {
		CharSequence templatedText = context.getText(stringResourceId);
		CharSequence processedText = TextUtils.expandTemplate(templatedText, args);
		return processedText;
	}

	/**
	 * Procesa el texto del recurso indicado por ID como plantilla en la que las ocurrencias de ^1,
	 * ^2, etc argumentos son reemplazados por textos obtenidos de la variable args indicada. Cada
	 * int de la variable arg debe corresponder a un recurso string obtenible del contexto pasado,
	 * con el que se reemplazarán las variables de la plantilla
	 * 
	 * @param stringResourceId
	 *            EL id de recurso de la plantilla principal
	 * @param context
	 *            El contexto del cual obtener los recursos
	 * @param args
	 *            Los ids de recursos opcionales para obtener los textos de cada variable
	 * @return El texto con las variables reemplazadas de la pantalla
	 */
	public static CharSequence withIdArguments(int stringResourceId, Context context, int... args) {
		CharSequence[] textArgs = new CharSequence[args.length];
		for (int i = 0; i < args.length; i++) {
			CharSequence textArg = context.getText(args[i]);
			textArgs[i] = textArg;
		}
		return withArguments(stringResourceId, context, textArgs);
	}

}
