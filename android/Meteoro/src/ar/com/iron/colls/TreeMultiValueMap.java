/**
 * 08/03/2011 11:22:39 Copyright (C) 2011 Darío L. García
 * 
 * <a rel="license" href="http://creativecommons.org/licenses/by/3.0/"><img
 * alt="Creative Commons License" style="border-width:0"
 * src="http://i.creativecommons.org/l/by/3.0/88x31.png" /></a><br />
 * <span xmlns:dct="http://purl.org/dc/terms/" href="http://purl.org/dc/dcmitype/Text"
 * property="dct:title" rel="dct:type">Software</span> by <span
 * xmlns:cc="http://creativecommons.org/ns#" property="cc:attributionName">Darío García</span> is
 * licensed under a <a rel="license" href="http://creativecommons.org/licenses/by/3.0/">Creative
 * Commons Attribution 3.0 Unported License</a>.
 */
package ar.com.iron.colls;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

/**
 * Esta clase representa un mapa ordenado por key de varios valores en una lista
 * 
 * @author D. García
 */
public class TreeMultiValueMap<K extends Comparable<? super K>, V> extends TreeMap<K, List<V>> {
	private static final long serialVersionUID = -427747876415618187L;

	/**
	 * Agrega un valor a este mapa, creando la lista contenedora de varios valores si no existe
	 * 
	 * @param key
	 *            Objeto usado como key
	 * @param value
	 *            Objeto usado como value que será agregado a la lista de valores bajo la key
	 * @return La lista valores de la key
	 */
	public List<V> putValue(K key, V value) {
		List<V> keyValues = get(key);
		if (keyValues == null) {
			keyValues = new ArrayList<V>();
			put(key, keyValues);
		}
		keyValues.add(value);
		return keyValues;
	};

	/**
	 * Devuelve una lista con todos los values de cada key agregados uno tras otro
	 * 
	 * @return La lista general de todos los values
	 */
	public List<V> getAllValues() {
		ArrayList<V> allValues = new ArrayList<V>();
		Set<java.util.Map.Entry<K, List<V>>> entries = entrySet();
		for (Entry<K, List<V>> entry : entries) {
			List<V> keyValues = entry.getValue();
			allValues.addAll(keyValues);
		}
		return allValues;
	}
}
