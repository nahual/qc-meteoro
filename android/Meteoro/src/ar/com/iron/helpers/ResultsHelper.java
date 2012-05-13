/**
 * 21/01/2011 11:02:03 Copyright (C) 2006 Darío L. García
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

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

/**
 * Esta clase ofrece métodos para operar sobre listas de resultados
 * 
 * @author D. García
 */
public class ResultsHelper {

	public static enum FilterType {
		/**
		 * Se queda con los máximos de la comparación
		 */
		MAX {
			public <T> boolean isWorstOrEqualsThan(T referent, T compared, Comparator<? super T> orden) {
				boolean isSmaller = orden.compare(referent, compared) >= 0;
				return isSmaller;
			};
		},
		/**
		 * Se queda con los mínimos de la comparación
		 */
		MIN {
			public <T> boolean isWorstOrEqualsThan(T referent, T compared, Comparator<? super T> orden) {
				boolean isBigger = orden.compare(referent, compared) <= 0;
				return isBigger;
			};
		};

		/**
		 * Indica si el elemento comparado es peor que el referente respecto del orden indicado,
		 * dado este tipo de filtro.<br>
		 * El elemento es peor, si este filtro es mínimo y el comparado es mayor. O si se busca el
		 * máximo y la comparación da que es menor
		 * 
		 * @param <T>
		 *            tipo de los elementos
		 * @param referent
		 *            El objeto usado como referente para la comparación
		 * @param compared
		 *            El elemento comparado
		 * @param orden
		 *            El orden que indica la relación entre ellos
		 * @return true si el comparado debe posicionarse después del referente
		 */
		public abstract <T> boolean isWorstOrEqualsThan(T referent, T compared, Comparator<? super T> orden);
	}

	/**
	 * A partir de las lista pasada esta función se queda con los primeros n elementos que son
	 * mínimos con respecto al comparador indicado
	 * 
	 * @param cantidadFiltrada
	 *            Cantidad de elementos a preservar en la lista devuelta
	 * @param elementosAFiltrar
	 *            Los objetos que se deben filtrar
	 * @param orden
	 *            Relación de orden entre los elementos que permite encontrar el mínimo
	 * @return La lista ordenada de menor a mayor de los primeros elementos
	 */
	public static <T> List<T> filterFirstMin(int cantidadFiltrada, Iterable<? extends T> elementosAFiltrar,
			Comparator<? super T> orden) {
		return filterFirstElements(cantidadFiltrada, elementosAFiltrar, orden, FilterType.MIN);
	}

	/**
	 * A partir de las lista pasada esta función se queda con los primeros n elementos que son
	 * máximos con respecto al comparador indicado
	 * 
	 * @param cantidadFiltrada
	 *            Cantidad de elementos a preservar en la lista devuelta
	 * @param elementosAFiltrar
	 *            Los objetos que se deben filtrar
	 * @param orden
	 *            Relación de orden entre los elementos que permite encontrar el máximo
	 * @return La lista ordenada de mayor a menor de los primeros elementos
	 */
	public static <T> List<T> filterFirstMax(int cantidadFiltrada, Iterable<? extends T> elementosAFiltrar,
			Comparator<? super T> orden) {
		return filterFirstElements(cantidadFiltrada, elementosAFiltrar, orden, FilterType.MAX);
	}

	/**
	 * A partir de la lista pasada devuelve una nueva lista con los mejores N elementos.<br>
	 * Cuáles son mejores se indica con el tipo de filtro, y la relación de comparación entre ellos
	 * con el comparator
	 * 
	 * @param <T>
	 *            Tipo de los elementos
	 * @param cantidadFiltrada
	 *            Cantidad máxima de elementos devueltos
	 * @param elementosAFiltrar
	 *            Los elementos a ordenar y filtrar
	 * @param orden
	 *            La relación de orden entre los elementos
	 * @param filterType
	 *            El tipo de extremo buscado, los máximos o mínimos de la relación
	 * @return La lista ordenada de los primeros máximos o mínimos acotada a la cantidad indicada
	 */
	private static <T> List<T> filterFirstElements(int cantidadFiltrada, Iterable<? extends T> elementosAFiltrar,
			Comparator<? super T> orden, FilterType filterType) {
		LinkedList<T> filtered = new LinkedList<T>();

		for (T elemento : elementosAFiltrar) {
			if (filtered.isEmpty()) {
				// Es el primer elemento
				filtered.add(elemento);
				continue;
			}
			int elementIndex = filtered.size();
			// Buscamos en reversa el lugar que le corresponde
			for (; elementIndex > 0; elementIndex--) {
				int previousPosition = elementIndex - 1;
				T possibleBetter = filtered.get(previousPosition);
				if (filterType.isWorstOrEqualsThan(possibleBetter, elemento, orden)) {
					// Llegamos al lugar que le corresponde
					break;
				}
			}

			if (elementIndex < filtered.size()) {
				// Es mejor que los valores actuales
				filtered.add(elementIndex, elemento);
				if (filtered.size() > cantidadFiltrada) {
					// Si nos excedimos, sacamos al peor
					filtered.removeLast();
				}
			} else {
				// Hay que agregarlo al final
				if (filtered.size() < cantidadFiltrada) {
					// Todavía queda lugar para agregarlo
					filtered.add(elemento);
				}
				continue;
			}
		}

		return filtered;
	}

}
