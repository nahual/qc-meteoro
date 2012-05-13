/**
 * 08/06/2011 21:27:20 Copyright (C) 2011 Darío García
 */
package ar.com.iron.colls;

/**
 * Esta interfaz define el contrato que debe cumplir la función o código para obtener la distancia
 * de cada punto a ordenar
 * 
 * @author D. García
 */
public interface DistanceCalculator<T> {

	/**
	 * Calcula y devuelve la distancia desde un punto de referencia implicito en este calculator al
	 * punto pasado, representado por un objeto
	 * 
	 * @param point
	 *            El objeto desde el cual se debe obtener la distancia
	 * @return El valor de distancia para comparar con los otros puntos en el ordenamiento
	 */
	public Double calculateDistanceTo(T point);

}
