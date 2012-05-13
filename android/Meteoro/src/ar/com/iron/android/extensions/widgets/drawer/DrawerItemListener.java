/**
 * 27/02/2011 20:23:28 Copyright (C) 2011 Darío L. García
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
package ar.com.iron.android.extensions.widgets.drawer;

/**
 * Esta interfaz define métodos invocados cuando el usuario interactúa con el drawer de items
 * 
 * @author D. García
 * @param <T>
 *            Tipo de los items seleccionables
 */
public interface DrawerItemListener<T> {

	/**
	 * Invocado por el controller cuando el usuario clickea (selecciona) un item del drawer
	 * 
	 * @param selectedItem
	 *            El elemento seleccionado
	 */
	public void onItemSelection(T selectedItem);

}
