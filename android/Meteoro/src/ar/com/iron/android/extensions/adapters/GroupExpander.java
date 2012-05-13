/**
 * 08/03/2011 02:34:57 Copyright (C) 2011 Darío L. García
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

import java.util.List;

/**
 * Esta interfaz define el método invocado sobre cada grupo para obtener sus elementos
 * 
 * @author D. García
 * @param <G>
 *            Tipo de objeto para los grupos evaluados
 * @param <E>
 *            Tipo de los objetos para los elementos del grupo
 */
public interface GroupExpander<G, E> {

	/**
	 * Este método es invocado en cada grupo para conocer sus elementos.<br>
	 * Es invocado cada vez que se notifica al adapter de cambios en los datos (y la primera vez)
	 * 
	 * @param group
	 *            El grupo que debe ser expandido
	 * @param expandedElements
	 *            La lista a la que se deben agregar los elementos del grupo (el orden será
	 *            respetado)
	 */
	public void expand(G group, List<E> expandedElements);
}
