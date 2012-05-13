/**
 * 08/06/2011 21:25:24 Copyright (C) 2011 Darío García
 */
package ar.com.iron.colls;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * Esta clase es una implementación de comparator que delega en un bloque de código para calcular la
 * distancia a cada
 * 
 * @author D. García
 */
public class DistanceComparator<T> implements Comparator<T> {

	private DistanceCalculator<T> distanceCalculator;

	private boolean useCacheForDistance = true;

	private Map<T, Double> localCache;

	public int compare(final T compared, final T reference) {
		final Double comparedDistance = getDistanceFrom(compared);
		final Double referenceDistance = getDistanceFrom(reference);
		return (int) (comparedDistance - referenceDistance);
	}

	/**
	 * Devuelve la distancia del punto pasado obtenida del cache o directamente desde el objeto
	 * 
	 * @param point
	 *            El objeto a evaluar por distancia
	 * @return La distancia obtenida
	 */
	private Double getDistanceFrom(final T point) {
		Double distance = null;
		if (useCacheForDistance) {
			distance = getCachedDistanceFor(point);
		}
		if (distance == null) {
			distance = this.distanceCalculator.calculateDistanceTo(point);
			if (distance == null) {
				throw new RuntimeException("El calculador de distancia devolvio null para: " + point);
			}
		}
		return distance;
	}

	/**
	 * Devuelve la distancia conservada en la caché local
	 * 
	 * @param point
	 *            El punto desde el que se obtendrá la distancia
	 * @return La distancia cacheada o null si no hay ninguna
	 */
	private Double getCachedDistanceFor(final T point) {
		return getLocalCache().get(point);
	};

	public Map<T, Double> getLocalCache() {
		if (localCache == null) {
			localCache = new HashMap<T, Double>();
		}
		return localCache;
	}

	public static <T> DistanceComparator<T> create(final DistanceCalculator<T> distanceCalculator,
			final boolean doCacheDistances) {
		final DistanceComparator<T> comparator = new DistanceComparator<T>();
		comparator.distanceCalculator = distanceCalculator;
		comparator.useCacheForDistance = doCacheDistances;
		return comparator;
	}
}
