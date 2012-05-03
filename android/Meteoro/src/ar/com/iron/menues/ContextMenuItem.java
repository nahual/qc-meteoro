/**
 * 20/04/2011 02:00:28 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.menues;

import android.app.Activity;

/**
 * Esta interfaz representa una opción de menú contextual de un elemento listado
 * 
 * @author D. García
 */
public interface ContextMenuItem<A extends Activity, E> extends CustomMenuItem {

	/**
	 * Determina la lógica a ejecutar cuando se selecciona un item del menu contextual
	 */
	boolean onSelection(A activity, E element);

}
