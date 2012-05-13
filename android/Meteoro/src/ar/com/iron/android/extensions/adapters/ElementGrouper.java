/**
 * 08/03/2011 10:48:55 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.adapters;

/**
 * Esta interfaz define el grupo que corresponde a un elemento dado.<br>
 * A través de implementaciones de esta interfaz se construye la información para mostrar los
 * elementos agrupados.<br>
 * El elemento utilizado como grupo debe ser comparable
 * 
 * @author D. García
 * @param <G>
 *            Tipo de objetos utilizados como grupos, debe ser comparable
 * @param <E>
 *            Tipo de objetos usados como elementos
 */
public interface ElementGrouper<G, E> {

	/**
	 * Devuelve el objeto que representa el grupo del elemento pasado.<br>
	 * El objeto devuelto debe ser comparable para establecer un orden y una igualdad entre grupos
	 * 
	 * @param element
	 *            Elemento a evaluar
	 * @return El grupo asignado
	 */
	public G getGroupOf(E element);

}
